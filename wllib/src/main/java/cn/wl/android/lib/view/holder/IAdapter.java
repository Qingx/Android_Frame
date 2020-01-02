package cn.wl.android.lib.view.holder;

import android.view.ViewGroup;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public interface IAdapter {

    BaseHolder onCreateHolder(ViewGroup parent, int status);

    void onConvert(BaseHolder holder);

    void onCompleteShow(BaseHolder holder);

    void onCompleteHide(BaseHolder holder);

    void onDestroyHolder(BaseHolder holder);

    void onShowTransition(BaseHolder holder, float progress);

    void onHideTransition(BaseHolder holder, float progress);

    void onHolderTransform(BaseHolder enter, BaseHolder exit, float progress);

}
