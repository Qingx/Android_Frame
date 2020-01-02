package cn.wl.android.lib.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * Created by JustBlue on 2019-09-05.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 监听界面输入法弹起状态
 */
public final class InputHelper implements ViewTreeObserver.OnGlobalLayoutListener {

    private static int mInputHeight = -1;
    private static Rect mVisibleRect = new Rect();
    private static final int NO_CHANGE = ConvertUtils.dp2px(4);
    private static final int MIN_HEIGHT = ConvertUtils.dp2px(50);

    /**
     * 初始化界面显示区域
     *
     * @param v
     */
    public static void initVisibleRect(final View v) {
        if (v.isLaidOut()) {
            v.getWindowVisibleDisplayFrame(mVisibleRect);
        } else {
            v.post(() -> {
                v.getWindowVisibleDisplayFrame(mVisibleRect);
            });
        }
    }

    private View mView;
    private int lastHeight = -1;
    private boolean lastStatus = false;
    private OnInputListener mListener;

    public InputHelper(View view) {
        this.mView = view;
        this.mView.getViewTreeObserver()
                .addOnGlobalLayoutListener(this);
    }

    public void clear() {
        this.mView.getViewTreeObserver()
                .removeOnGlobalLayoutListener(this);
        this.mView = null;
    }

    public void setListener(OnInputListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        mView.getWindowVisibleDisplayFrame(rect);

        if (mVisibleRect.isEmpty()) {
            mVisibleRect.set(rect);
        }

        int offset = mVisibleRect.bottom - rect.bottom;

        if (offset >= MIN_HEIGHT) {
            updateInputHeight(offset);

            publishChangeShow(true, offset, rect);
        } else {
            publishChangeShow(false, offset, rect);
        }
    }

    /**
     * 保存全局输入法高度
     *
     * @param height
     */
    private void updateInputHeight(int height) {
        mInputHeight = height;
    }

    private void publishChangeShow(boolean show, int offset, Rect visibleRect) {
        int absOff = Math.abs(offset - lastHeight);
        if (show == lastStatus && absOff <= NO_CHANGE) {
            return;
        }

        lastHeight = offset;
        lastStatus = show;

        mListener.onInputChange(show, offset, visibleRect);
    }

    public interface OnInputListener {

        /**
         * 回调输入法弹起事件
         *
         * @param show
         * @param height
         * @param visibleRect
         */
        void onInputChange(boolean show, int height, Rect visibleRect);
    }

}
