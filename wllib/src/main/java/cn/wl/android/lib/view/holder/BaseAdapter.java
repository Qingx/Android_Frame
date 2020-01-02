package cn.wl.android.lib.view.holder;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public abstract class BaseAdapter implements IAdapter {

    @Override
    public void onConvert(BaseHolder holder) {

    }

    @Override
    public void onCompleteShow(BaseHolder holder) {

    }

    @Override
    public void onCompleteHide(BaseHolder holder) {

    }

    @Override
    public void onDestroyHolder(BaseHolder holder) {

    }

    @Override
    public void onShowTransition(BaseHolder holder, float progress) {
        holder.getContentView().setAlpha(progress);
    }

    @Override
    public void onHideTransition(BaseHolder holder, float progress) {
        holder.getContentView().setAlpha(1 - progress);
    }

    @Override
    public void onHolderTransform(BaseHolder enter, BaseHolder exit, float progress) {
        exit.getContentView().setAlpha(1 - progress);
        enter.getContentView().setAlpha(progress);
    }
}
