package cn.wl.android.lib.ui.internal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import io.reactivex.ObservableTransformer;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 界面生命周期管理
 */
public interface ILifeCycle {

    int CYC_CREATE = 0;     /** {@link Activity#onCreate(Bundle)} */
    int CYC_START_ = 1;     /** {@link Activity#onStart()} */
    int CYC_RESUME = 2;     /** {@link Activity#onResume()} */
    int CYC_PASUE_ = 3;     /** {@link Activity#onPause()} */
    int CYC_STOP_X = 4;     /** {@link Activity#onStop()} */
    int CYC_DESTROY = 5;    /** {@link Activity#onDestroy()} */

    int FYC_ATTACH = 6;     /** 对应 {@link Fragment#onAttach(Context)} */
    int FYC_DETTCH = 7;     /** 对应 {@link Fragment#onDetach(Context)} */

    /**
     * 将当前{@link io.reactivex.Observable}事件流绑定
     * 生命周期{@link #CYC_DESTROY}, 自动注销
     *
     * @param <T>
     * @return
     */
    <T> ObservableTransformer<T, T> bindDestroy();

    /**
     * 界面注销是调用
     * @param runDestroy
     */
    void doOnDestroy(Runnable runDestroy);

}
