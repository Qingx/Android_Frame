package cn.wl.android.lib.core;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 接口集合类型数据
 */
public class WLList<T> implements Serializable, IDataSource<List<T>> {

    private int status = 0;
    private String msg = "";

    private long timestamps = 0L;

    private List<T> data;

    public long getTimestamps() {
        return timestamps;
    }

    @Override
    public int getCode() {
        return this.status;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public List<T> getData() {
        return this.data;
    }

    @Override
    public boolean isDataEmpty() {
        List<T> data = this.data;

        return WLDataHelper.isEmpty(data);
    }

    @Override
    public boolean isSuccess() {
        return WLDataHelper.isSuccess(this.status);
    }

    @Override
    public String toString() {
        return "WLList{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", timestamps=" + timestamps +
                ", data=" + data +
                '}';
    }
}
