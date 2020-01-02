package cn.wl.android.lib.core;

import java.io.Serializable;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class WLData<T> implements Serializable, IDataSource<T> {

    private int status = 0;
    private String msg = "";
    private long timestamps = 0L;

    private T data;

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
    public T getData() {
        return this.data;
    }

    @Override
    public boolean isDataEmpty() {
        T data = this.data;

        if (data == null) {
            return true;
        }

        if (data instanceof IDataEmpty) {
            return ((IDataEmpty) data).isDataEmpty();
        }

        return false;
    }

    @Override
    public boolean isSuccess() {
        return WLDataHelper.isSuccess(this.status);
    }

    @Override
    public String toString() {
        return "WLData{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", timestamps=" + timestamps +
                ", data=" + data +
                '}';
    }
}
