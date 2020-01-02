package cn.wl.android.lib.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import cn.wl.android.lib.ui.internal.IAlertAction;
import cn.wl.android.lib.ui.internal.ILazyLoad;
import cn.wl.android.lib.ui.internal.ILifeCycle;
import cn.wl.android.lib.ui.internal.IRxOption;
import cn.wl.android.lib.ui.internal.IStatusAction;
import cn.wl.android.lib.ui.internal.LifecycleAnn;
import cn.wl.android.lib.utils.result.RxResult;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public abstract class BaseCommonFragment extends Fragment implements ILazyLoad, IStatusAction, ILifeCycle, IAlertAction, IRxOption {

    private EventBus mEventBus;
    private RxResult mRxResult;
    private RxPermissions mPermissions;

    private PublishSubject<Integer> mCycleSubject = PublishSubject.create();

    protected Boolean isLazyLoaded = false;

    protected Activity mActivity;
    protected Fragment mFragment;

    private Boolean mLazyLock = true;     // 是否开启懒加载
    private Boolean mFragStarted = false; // 记录Fragment是否进入onStart()
    private Boolean mFragVisible = false; // 记录Fragment的显示状态
    private Boolean mCallVisible = false; // 记录是否调用setUserVisibleHint


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragment = this;

        beforeCreateView(savedInstanceState);

        publishLifecycle(CYC_CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Object view = getLayoutResource();

        if (view instanceof Integer) {
            int layoutId = (int) view;
            return inflater.inflate(layoutId, null);
        } else if (view instanceof View) {
            return (View) view;
        } else {
            throw new IllegalArgumentException("activity must set Content View!");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        internalViewCreated(view, savedInstanceState);

        initViewCreated(view, savedInstanceState);
    }

    /**
     * 加载先于{@link #onViewCreated(View, Bundle)}的资源
     *
     * @param view
     * @param savedInstanceState
     */
    @CallSuper
    protected void internalViewCreated(View view, Bundle savedInstanceState) {

    }


    /**
     * 获取{@link RxResult}对象
     *
     * @return
     */
    @Override
    public RxResult getRxResult() {
        if (mRxResult == null) {
            mRxResult = new RxResult(this);
        }

        return mRxResult;
    }

    @Override
    public RxPermissions getRxPermission() {
        if (mPermissions == null) {
            mPermissions = new RxPermissions(mFragment);
        }
        return mPermissions;
    }

    /**
     * 获取{@link EventBus}对象
     *
     * @return
     */
    protected EventBus getEventBus() {
        if (mEventBus == null) {
            mEventBus = EventBus.getDefault();
        }
        return mEventBus;
    }

    @Override
    public void setUserVisibleHint(boolean show) {
        super.setUserVisibleHint(show);

        mCallVisible = true;
        mFragVisible = show;

        switchFragmentState();
    }

    /**
     * 初始化Fragment状态,
     */
    private void initFragmentState() {
        mFragStarted = false;
        isLazyLoaded = false;
    }

    /**
     * 切换Fragment显示状态
     * <p>
     * 注: 默认实现懒加载, 可以通过[setLazyEnable]实现是否开启懒加载
     */
    private void switchFragmentState() {
        if (mCallVisible) {
            /* 此处判断ViewPager加载的Fragment */
            if (mFragVisible && mFragStarted) lazyLoad();
        } else {
            /* 此处判断正常加载的Fragment */
            if (mFragStarted) lazyLoad();
        }
    }

    /**
     * 尝试懒加载
     */
    protected void lazyLoad() {
        if (!isLazyLoaded) {
            isLazyLoaded = true;

            loadData();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        initFragmentState();
        mCallVisible = false;

        publishLifecycle(FYC_DETTCH);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (Activity) context;
        publishLifecycle(FYC_ATTACH);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        publishLifecycle(CYC_START_);

        if (!mFragStarted) {
            mFragStarted = true;
            switchFragmentState();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        publishLifecycle(CYC_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();

        publishLifecycle(CYC_PASUE_);
    }

    @Override
    public void onStop() {
        super.onStop();

        publishLifecycle(CYC_STOP_X);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mEventBus != null) {
            try {
                mEventBus.unregister(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        publishLifecycle(CYC_DESTROY);
        mCycleSubject.onComplete();
    }

    @Override
    public Context getOptionContext() {
        return getActivity();
    }

    /**
     * 发布生命周期
     *
     * @param cycle
     */
    private void publishLifecycle(@LifecycleAnn int cycle) {
        mCycleSubject.onNext(cycle);
    }

    /**
     * 创建界面资源
     *
     * @return
     */
    protected abstract Object getLayoutResource();

    /**
     * 设计界面之前调用
     *
     * @param savedInstanceState
     */
    protected void beforeCreateView(Bundle savedInstanceState) {

    }

    /**
     * 在{@link AppCompatActivity#setContentView(View)}之后调用
     * 用户初始化界面控件
     *
     * @param savedInstanceState
     */
    protected abstract void initViewCreated(View view, Bundle savedInstanceState);

    @Override
    public <T> ObservableTransformer<T, T> bindDestroy() {
        return upstream -> upstream.takeUntil(mCycleSubject
                .filter(cycle -> cycle == CYC_DESTROY)
                .take(1)
        );
    }

    @Override
    public void doOnDestroy(Runnable runDestroy) {
        mCycleSubject
                .filter(cycle -> cycle == CYC_DESTROY)
                .subscribe(data -> runDestroy.run());
    }

    @Override
    public boolean isLazyLoaded() {
        return isLazyLoaded;
    }

    @Override
    public void retryLoadData() {
        loadData();
    }

    @Override
    public void loadData() {

    }


    /**
     * 界面安全的延时操作
     * 当界面销毁的时候，自动注销当前延时
     *
     * @param time
     * @param runDelay
     * @return
     */
    protected Disposable timerDelay(int time, Runnable runDelay) {
        return Observable.timer(time, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindDestroy())
                .subscribe(d -> runDelay.run());
    }

    /**
     * 界面安全的倒计时, 倒计时间隔1S
     * 当时间为0或界面销毁时, 自动注销当前倒计时
     *
     * @param time   倒计时起始时间
     * @param doDown 倒计时时间回调
     * @return
     */
    protected Disposable countdown(int time, Consumer<Integer> doDown) {
        return Observable.interval(1, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindDestroy())
                .map(t -> (int) (time - t))
                .takeUntil(t -> t >= 0)
                .subscribe(t -> doDown.accept(t));
    }


    /**
     * 文字改变事件监听
     *
     * @param tView
     * @param doNext
     */
    protected void doTextChange(@NonNull TextView tView, @NonNull Consumer<CharSequence> doNext) {
        final TextWatcher tw = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    doNext.accept(tView.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        tView.addTextChangedListener(tw);

        doOnDestroy(() -> tView.removeTextChangedListener(tw));
    }

    /**
     * 拦截布局{@link View#onLayout(boolean, int, int, int, int)}}布局改变
     *
     * @param view
     * @param runLayout
     */
    protected void doLayout(@NonNull View view, @NonNull Runnable runLayout) {
        ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                runLayout.run();
            }
        };

        view.getViewTreeObserver().addOnGlobalLayoutListener(listener);

        doOnDestroy(() -> view.getViewTreeObserver().removeOnGlobalLayoutListener(listener));
    }

}
