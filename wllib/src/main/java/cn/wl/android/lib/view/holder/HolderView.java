package cn.wl.android.lib.view.holder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by JustBlue on 2019-08-27.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class HolderView extends FrameLayout {

    public static final int STATUS_NONE = Integer.MIN_VALUE;

    private int mCurrentStatus = STATUS_NONE;

    private BaseHolder mHideHolder;
    private BaseHolder mShowHolder;

    private ValueAnimator mAnimator;

    private SparseArray<BaseHolder> mHolderPool;
    private BaseAdapter mViewAdapter;

    // 是否不响应任何事件
    private boolean isLockTouch;
    // 第一次显示时，不执行动画
    private boolean isFirstShow;
    private boolean isOnceLayout;
    private Drawable mBackground;

    private float mCurrentAlpha = 0F;

    public HolderView(@NonNull Context context) {
        this(context, null);
    }

    public HolderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HolderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    /**
     * 初始化参数
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        mHolderPool = new SparseArray();

        mBackground = getBackground();
        if (mBackground == null) {
            setBackgroundColor(Color.WHITE);
            mBackground = getBackground();
        }
        mBackground.setAlpha(0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        BaseHolder holder = this.mShowHolder;

        if (holder != null) {
            // 如指定当前界面拦截事件, 不再下发事件;
            // 及为true界面覆盖的View将不会收的事件
            return isLockTouch
                    || super.dispatchTouchEvent(ev)
                    || holder.interceptTouchEvent();
        }

        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isOnceLayout) {
            isOnceLayout = true;

            checkAndSwitchLayout();
        }

        if (mShowHolder != null || mHideHolder != null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            setMeasuredDimension(0, 0);
        }
    }

    /**
     * 检查并切换界面显示
     */
    private void checkAndSwitchLayout() {
        int status = this.mCurrentStatus;

        if (status != STATUS_NONE) {
            trySwitchLayout(status);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        this.isFirstShow = true;
        super.draw(canvas);
    }

    /**
     * 设置适配器
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        if (this.mViewAdapter == adapter) {
            return;
        }

        BaseAdapter old = this.mViewAdapter;

        if (old != null) {
            tryEndTransition();

            unbindOldAdapter(old);
        }

        this.mViewAdapter = adapter;
        int status = this.mCurrentStatus;

        if (status != STATUS_NONE) {
            trySwitchLayout(status);
        }
    }

    /**
     * 尝试切换当前显示的界面
     *
     * @param status
     */
    private void trySwitchLayout(int status) {
        BaseAdapter adapter = this.mViewAdapter;
        BaseHolder current = this.mShowHolder;

        if (status == STATUS_NONE) {
            if (current != null) {
                onExitTransition(current);
            } else {
                mHideHolder = null;
                requestLayout();
            }
            return;
        }

        if (isOnceLayout && adapter != null) {
            SparseArray<BaseHolder> pool = this.mHolderPool;
            BaseHolder holder = pool.get(status);

            if (holder == null) {
                holder = adapter.onCreateHolder(this, status);
                if (holder == null) {
                    throw new IllegalArgumentException(" HolderView -> unknown holder status! ");
                }


                holder.bindViewToHolderView(this);
                pool.put(status, holder);
            }

            if (current == null) {
                onEnterTransition(holder);
            } else {
                onPerformTransition(holder, current);
            }
        }
    }

    /**
     * 执行进入动画
     *
     * @param holder
     */
    private void onEnterTransition(BaseHolder holder) {
        tryEndTransition();

        mShowHolder = holder;
        mHideHolder = null;

        requestLayout();

        holder.switchContentVisible(true);

        boolean allowTrans = holder.isTransparentBackground();
        boolean allowAnim = checkAllowBackgroundAnim(allowTrans);

        mViewAdapter.onConvert(holder);

        if (!isFirstShow) {
            isFirstShow = true;

            BaseAdapter adapter = HolderView.this.mViewAdapter;

            if (adapter != null) {
                adapter.onShowTransition(mShowHolder, 1);
            }
            if (allowAnim) {
                setBackgroundAlpha(getProgressAlpha(allowTrans, 1));
            }
            return;
        } else {
            mViewAdapter.onShowTransition(holder, 0);
        }

        lockTouch();
        startTransition(new OPL() {
            @Override
            void onProgress(float progress) {
                unlockTouch();

                BaseAdapter adapter = HolderView.this.mViewAdapter;

                if (adapter != null) {
                    adapter.onShowTransition(mShowHolder, progress);
                }

                if (allowAnim) {
                    setBackgroundAlpha(getProgressAlpha(allowTrans, progress));
                }
            }

            @Override
            void onComplete() {
                unlockTouch();

                BaseAdapter adapter = HolderView.this.mViewAdapter;
                if (adapter != null) {
                    adapter.onCompleteShow(mShowHolder);
                }

                if (allowAnim) {
                    setBackgroundAlpha(getProgressAlpha(allowTrans, 1));
                }
            }
        });
    }

    private float getProgressAlpha(boolean isTrans, float progress) {
        return isTrans ? 1 - progress : progress;
    }

    /**
     * 判断当前界面过渡是否需要背景过渡动画
     *
     * @param trans
     * @return
     */
    private boolean checkAllowBackgroundAnim(boolean trans) {
        if (trans) return mCurrentAlpha == 1;

        return mCurrentAlpha == 0;
    }

    private void setBackgroundAlpha(float progress) {
        mCurrentAlpha = progress;
        mBackground.setAlpha((int) (255 * progress));
    }

    private void lockTouch() {
        isLockTouch = true;
    }

    private void unlockTouch() {
        isLockTouch = false;
    }

    /**
     * 执行退出动画
     *
     * @param current
     */
    private void onExitTransition(final BaseHolder current) {
        tryEndTransition();

        mHideHolder = current;
        mShowHolder = null;
        mViewAdapter.onConvert(current);
        mViewAdapter.onHideTransition(current, 0);

        current.switchContentVisible(true);

        boolean allowAnim = checkAllowBackgroundAnim(true);

        lockTouch();
        startTransition(new OPL() {
            @Override
            void onProgress(float progress) {
                BaseAdapter adapter = HolderView.this.mViewAdapter;

                if (adapter != null) {
                    adapter.onHideTransition(mHideHolder, progress);
                }
                if (allowAnim) {
                    setBackgroundAlpha(getProgressAlpha(true, progress));
                }
            }

            @Override
            void onComplete() {
                unlockTouch();

                BaseAdapter adapter = HolderView.this.mViewAdapter;
                if (adapter != null) {
                    adapter.onCompleteHide(mHideHolder);
                }
                mHideHolder.switchContentVisible(false);

                if (allowAnim) {
                    setBackgroundAlpha(getProgressAlpha(true, 1));
                }

                mHideHolder = null;
                requestLayout();
            }
        });
    }

    /**
     * 切换holder
     *
     * @param holder
     * @param current
     */
    private void onPerformTransition(BaseHolder holder, BaseHolder current) {
        tryEndTransition();

        mShowHolder = holder;
        mHideHolder = current;

        current.stickFromHolderView();
        holder.stickFromHolderView();

        current.switchContentVisible(true);
        holder.switchContentVisible(true);

        mViewAdapter.onConvert(holder);
        mViewAdapter.onHolderTransform(
                holder, current, 0);

        boolean allowTrans = holder.isTransparentBackground();
        boolean allowAnim = checkAllowBackgroundAnim(allowTrans);

        lockTouch();
        startTransition(new OPL() {
            @Override
            void onProgress(float progress) {
                BaseAdapter adapter = HolderView.this.mViewAdapter;
                if (adapter != null) {
                    adapter.onHolderTransform(mShowHolder, mHideHolder, progress);
                }
                if (allowAnim) {
                    setBackgroundAlpha(getProgressAlpha(allowTrans, progress));
                }
            }

            @Override
            void onComplete() {
                unlockTouch();

                BaseAdapter adapter = HolderView.this.mViewAdapter;
                if (adapter != null) {
                    adapter.onCompleteShow(mShowHolder);
                    adapter.onCompleteHide(mHideHolder);
                }

                if (allowAnim) {
                    setBackgroundAlpha(getProgressAlpha(allowTrans, 1));
                }
                mHideHolder.switchContentVisible(false);
            }
        });
    }

    /**
     * 开始执行动画
     *
     * @param opl
     */
    private void startTransition(final OPL opl) {
        ValueAnimator animator =
                ValueAnimator.ofFloat(0, 1);
        animator.setDuration(550);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();

            opl.progressRow(fraction);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                opl.completeRow();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                opl.completeRow();
            }
        });
        animator.start();

        this.mAnimator = animator;
    }

    /**
     * 尝试结束当前界面过渡动画
     */
    private void tryEndTransition() {
        ValueAnimator animator = this.mAnimator;

        if (animator != null) {
            animator.end();
        }
    }

    /**
     * 解绑上一个Adapter
     *
     * @param old
     */
    private void unbindOldAdapter(BaseAdapter old) {
        SparseArray<? extends BaseHolder> array = this.mHolderPool;

        for (int i = 0; i < array.size(); i++) {
            BaseHolder holder = array.valueAt(i);
            if (holder != null) {
                old.onDestroyHolder(holder);

                holder.removeFromHolderView();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        isFirstShow = false;
        isOnceLayout = false;
        tryEndTransition();
    }

    /**
     * 通过状态改变界面显示布局
     *
     * @param status
     */
    public void showStatus(int status) {
        if (mCurrentStatus == status) {
            return;
        }

        this.mCurrentStatus = status;
        trySwitchLayout(status);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        try {
            tryStickHolderView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 尝试置顶当前界面
     *
     * @throws Exception
     */
    private void tryStickHolderView() throws Exception {
        ViewParent parent = getParent();

        if (parent != null && parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            int count = group.getChildCount();

            for (int i = 0; i < count; i++) {
                View child = group.getChildAt(i);

                if (child == this) {
                    if (i < count - 1) {
                        ViewGroup.LayoutParams params = getLayoutParams();
                        group.addView(this, params);
                    }
                }
            }
        }
    }
}
