package cn.wl.android.lib.ui.holder;

import android.util.SparseArray;
import android.view.ViewGroup;

import cn.wl.android.lib.core.ErrorBean;
import cn.wl.android.lib.ui.holder.vh.EmptyVH;
import cn.wl.android.lib.ui.holder.vh.ErrorVH;
import cn.wl.android.lib.ui.holder.vh.IAinmHolder;
import cn.wl.android.lib.ui.holder.vh.LoadingVH;
import cn.wl.android.lib.ui.holder.vh.NoNetVH;
import cn.wl.android.lib.ui.holder.vh.TopNetVH;
import cn.wl.android.lib.ui.holder.vh.WLHolder;
import cn.wl.android.lib.view.holder.BaseAdapter;
import cn.wl.android.lib.view.holder.BaseHolder;

/**
 * Created by JustBlue on 2019-08-28.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public abstract class HolderAdapter extends BaseAdapter implements OnHolderCallback {

    private SparseArray<ErrorBean> mErrorData = new SparseArray<>();

    @Override
    public BaseHolder onCreateHolder(ViewGroup parent, int status) {
        BaseHolder holder = createHolder(parent, status);

        if (holder != null) return holder;

        switch (status) {
            case StatusCodePool.EMPTY_CODE:
                return new EmptyVH();
            case StatusCodePool.ERROR_CODE:
                return new ErrorVH();
            case StatusCodePool.NONET_CODE:
                return new NoNetVH();
            case StatusCodePool.LOADING_CODE:
                return new LoadingVH();
            case StatusCodePool.TOP_NET_CODE:
                return new TopNetVH();
            default:
                return null;
        }
    }

    /**
     * 设置异常数据
     *
     * @param statusCode
     * @param bean
     */
    public void setData(int statusCode, ErrorBean bean) {
        mErrorData.put(statusCode, bean);
    }

    @Override
    public void onConvert(BaseHolder holder) {
        if (holder instanceof WLHolder) {
            ((WLHolder) holder).setCallback(this);

            ErrorBean bean = mErrorData
                    .get(holder.getStatusCode());
            ((WLHolder) holder).update(bean);
        }
    }

    @Override
    public void onCompleteHide(BaseHolder holder) {
        if (holder instanceof WLHolder) {
            ((WLHolder) holder).setCallback(this);
        }
    }

    @Override
    public void onHolderTransform(BaseHolder enter, BaseHolder exit, float progress) {
        onShowTransition(enter, progress);
        onHideTransition(exit, progress);
    }

    @Override
    public void onHideTransition(BaseHolder holder, float progress) {
        if (holder instanceof IAinmHolder) {
            ((IAinmHolder) holder).onAnim(progress);
        }
    }

    @Override
    public void onShowTransition(BaseHolder holder, float progress) {
        if (holder instanceof IAinmHolder) {
            ((IAinmHolder) holder).onAnim(1 - progress);
        }
    }

    /**
     * 创建自定义
     *
     * @param parent
     * @param status
     * @return
     */
    abstract BaseHolder createHolder(ViewGroup parent, int status);

}
