package cn.wl.android.lib.ui.internal;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 对话框操作接口
 */
public interface IAlertAction {

    /**
     * 显示加载对话框
     *
     * @param msg
     */
    void showLoadingAlert(String msg);

    /**
     * 隐藏加载对话框
     */
    void hideLoadingAlert();

}
