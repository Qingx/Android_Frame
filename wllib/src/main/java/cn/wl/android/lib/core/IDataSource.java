package cn.wl.android.lib.core;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 接口数据状态判断接口
 */
public interface IDataSource<T> extends IDataEmpty {

    /**
     * 获取接口返回状态code
     *
     * @return
     */
    int getCode();

    /**
     * 获取接口返回信息
     *
     * @return
     */
    String getMsg();

    /**
     * 获取实际数据类型
     *
     * @return
     */
    T getData();

    /**
     * 判断接口是否成功
     *
     * @return
     */
    boolean isSuccess();

}
