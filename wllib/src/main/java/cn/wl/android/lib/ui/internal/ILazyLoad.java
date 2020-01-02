package cn.wl.android.lib.ui.internal;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 懒加载适配
 */
public interface ILazyLoad {

    /**
     * 加载数据
     */
    void loadData();

    /**
     * 重试加载
     */
    void retryLoadData();

    /**
     * 判断是否加载过数据
     *
     * @return
     */
    boolean isLazyLoaded();

}
