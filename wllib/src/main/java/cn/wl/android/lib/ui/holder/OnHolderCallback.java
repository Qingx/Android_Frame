package cn.wl.android.lib.ui.holder;

import android.os.Bundle;

/**
 * Created by JustBlue on 2019-08-28.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public interface OnHolderCallback {

    /**
     * 自定义回调
     *
     * @param statusCode
     * @param data
     */
    void onHolder(int statusCode, Bundle data);

    /**
     * 点击根View
     *
     * @param statusCode
     */
    void onClickRoot(int statusCode);

}
