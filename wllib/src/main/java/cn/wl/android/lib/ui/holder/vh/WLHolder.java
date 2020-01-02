package cn.wl.android.lib.ui.holder.vh;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import cn.wl.android.lib.core.ErrorBean;
import cn.wl.android.lib.ui.holder.OnHolderCallback;
import cn.wl.android.lib.view.holder.BaseHolder;

/**
 * Created by JustBlue on 2019-08-28.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public abstract class WLHolder extends BaseHolder {

    private OnHolderCallback mCallback;

    public WLHolder(int statusCode) {
        super(statusCode);
    }

    @Override
    protected View getContentView(Context context) {
        int layoutId = getLayoutId();

        return LayoutInflater.from(context)
                .inflate(layoutId, null);
    }

    /**
     * 获取layoutId
     *
     * @return
     */
    protected abstract int getLayoutId();

    public void setCallback(OnHolderCallback callback) {
        this.mCallback = callback;
    }

    public abstract void update(ErrorBean data);

    /**
     * 发布事件指定类型的事件
     *
     * @param data
     */
    protected void publishEvent(Bundle data) {
        OnHolderCallback callback = this.mCallback;

        if (callback != null) {
            callback.onHolder(getStatusCode(), data);
        }
    }

    /**
     * 当点击界面时回调
     */
    protected void publishClick() {
        OnHolderCallback callback = this.mCallback;

        if (callback != null) {
            callback.onClickRoot(getStatusCode());
        }
    }

}
