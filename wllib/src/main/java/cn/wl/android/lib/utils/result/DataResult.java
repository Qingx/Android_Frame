package cn.wl.android.lib.utils.result;

import androidx.annotation.NonNull;

/**
 * Created by JustBlue on 2019-08-31.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 解析 {@link ActivityResult#getData()} 中封装
 * 的实际数据
 */
public class DataResult<T> {

    private final T mData;
    private final boolean isSuccess;

    /**
     * 发出一个取消事件
     *
     * @param <T>
     * @return
     */
    public static <T> DataResult<T> cancel() {
        return new DataResult(false, null);
    }

    /**
     * 发送一个正常数据的回调
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> DataResult<T> back(@NonNull T data) {
        if (data == null) throw new NullPointerException("data must be not null");

        return new DataResult<>(true, data);
    }

    public DataResult(boolean isSuccess, T data) {
        this.mData = data;
        this.isSuccess = isSuccess;
    }

    public T getData() {
        return mData;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    @Override
    public String toString() {
        return "DataResult{" +
                "mData=" + mData +
                ", isSuccess=" + isSuccess +
                '}';
    }
}
