package cn.wl.android.lib.ui.internal;

import android.content.Context;

import com.tbruyelle.rxpermissions2.RxPermissions;

import cn.wl.android.lib.utils.result.RxResult;

/**
 * Created by JustBlue on 2019-08-31.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public interface IRxOption {

    RxResult getRxResult();

    Context getOptionContext();

    RxPermissions getRxPermission();

}
