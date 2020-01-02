package cn.wl.android.lib.core;

import java.io.Serializable;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 分页数据
 */
public class WLPage<T> implements Serializable, IDataSource<Page<T>> {

    private int status = 0;
    private String msg = "";
    private long timestamps = 0L;

    private Page<T> data = Page.empty();

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
    public Page<T> getData() {
        return this.data;
    }

    @Override
    public boolean isDataEmpty() {
        Page<T> data = this.data;

        if (data == null) {
            return true;
        }

        return data.isDataEmpty();
    }

    @Override
    public boolean isSuccess() {
        return WLDataHelper.isSuccess(this.status);
    }

    @Override
    public String toString() {
        return "WLPage{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", timestamps=" + timestamps +
                ", data=" + data +
                '}';
    }
}
