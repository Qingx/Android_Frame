package cn.wl.android.lib.ui.holder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.blankj.utilcode.util.ColorUtils;

import cn.wl.android.lib.R;
import cn.wl.android.lib.core.ErrorBean;
import cn.wl.android.lib.ui.internal.IStatusAction;
import cn.wl.android.lib.view.holder.BaseHolder;
import cn.wl.android.lib.view.holder.HolderView;

/**
 * Created by JustBlue on 2019-08-28.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public abstract class HolderProxy implements IStatusAction {

    private HolderView mHolder;
    private HolderAdapter mAdapter;

    public HolderProxy(View act) {
        mHolder = act.findViewById(R.id.hv_default_holder);

        if (mHolder != null) {
            initHolderView(mHolder);
        }
    }

    private void initHolderView(HolderView holder) {
        mAdapter = new HolderAdapter() {
            @Override
            BaseHolder createHolder(ViewGroup parent, int status) {
                return HolderProxy.this.createCustomHolder(parent, status);
            }

            @Override
            public void onHolder(int statusCode, Bundle data) {
                HolderProxy.this.onCustomHolder(statusCode, data);
            }

            @Override
            public void onClickRoot(int statusCode) {
                HolderProxy.this.onCustomClick(statusCode);
            }
        };
        holder.setAdapter(mAdapter);
    }

    /**
     * 添加点击界面的回调
     *
     * @param statusCode
     */
    protected abstract void onCustomClick(int statusCode);

    /**
     * 添加自定义事件处理
     *
     * @param statusCode
     * @param data
     */
    protected abstract void onCustomHolder(int statusCode, Bundle data);

    /**
     * 添加自定义holder
     *
     * @param parent
     * @param status
     * @return
     */
    protected abstract BaseHolder createCustomHolder(ViewGroup parent, int status);

    @Override
    public void showLoading() {
        if (mHolder != null) {
            mHolder.showStatus(StatusCodePool.LOADING_CODE);
        }
    }

    @Override
    public void showContent() {
        if (mHolder != null) {
            mHolder.showStatus(StatusCodePool.CONTENT_CODE);
        }
    }

    @Override
    public void showNetMiss(ErrorBean bean) {
        if (mHolder != null) {
            mHolder.showStatus(StatusCodePool.NONET_CODE);
        }
    }

    @Override
    public void showDataFail(ErrorBean bean) {
        if (mHolder != null) {
            int status = StatusCodePool.ERROR_CODE;

            mAdapter.setData(status, bean);
            mHolder.showStatus(status);
        }
    }

    @Override
    public void showEmptyData(ErrorBean bean) {
        if (mHolder != null) {
            mHolder.showStatus(StatusCodePool.EMPTY_CODE);
        }
    }

    @Override
    public void showViewByStatus(int status) {
        if (mHolder != null) {
            mHolder.showStatus(status);
        }
    }

    @Override
    public void dispatchDataMiss(ErrorBean error) {

    }

    @Override
    public boolean onInterceptDataMiss(ErrorBean error) {
        return false;
    }

    public void setBackgroundColor(@ColorRes int drawRes) {
        if (mHolder != null) {
            mHolder.setBackgroundColor(ColorUtils.getColor(drawRes));
        }
    }

    public void setBackgroundRes(@DrawableRes int drawRes) {
        if (mHolder != null) {
            mHolder.setBackgroundResource(drawRes);
        }
    }
}
