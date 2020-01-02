package cn.wl.android.lib.ui.internal;

import cn.wl.android.lib.core.ErrorBean;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 界面UI状态
 */
public interface IStatusAction {

    /**
     * 显示数据加载界面
     */
    void showLoading();

    /**
     * 显示正常数据界面
     */
    void showContent();

    /**
     * 显示网络异常界面
     *
     * @param bean
     */
    void showNetMiss(ErrorBean bean);

    /**
     * 显示数据加载失败界面
     *
     * @param bean
     */
    void showDataFail(ErrorBean bean);

    /**
     * 显示空数据界面
     *
     * @param bean
     */
    void showEmptyData(ErrorBean bean);

    /**
     * 显示界面通过状态
     *
     * @param status
     */
    void showViewByStatus(int status);

    /**
     * 下发异常到界面
     *
     * @param error
     */
    void dispatchDataMiss(ErrorBean error);

    /**
     * 拦截业务异常
     *
     * @param error
     * @return
     */
    boolean onInterceptDataMiss(ErrorBean error);

}
