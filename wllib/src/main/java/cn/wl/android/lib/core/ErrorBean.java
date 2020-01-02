package cn.wl.android.lib.core;

import androidx.annotation.IntDef;

import cn.wl.android.lib.miss.BaseMiss;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 异常数据封装
 */
public class ErrorBean {

    @IntDef({
            MODE_NONE,
            MODE_LIST,
            MODE_MORE
    })
    public @interface BindMode {
    }

    public static final int MODE_NONE = 0;
    public static final int MODE_LIST = 1;
    public static final int MODE_MORE = 2;

    private String msg = "服务异常, 请稍后再试";

    private int code = -1;
    @BindMode private int mode = MODE_NONE;
    private boolean loadMore = false;

    private Throwable error;

    public ErrorBean() {
        this.error = new BaseMiss(this.code, this.msg);
    }

    public ErrorBean(int code, String msg) {
        this.msg = msg;
        this.code = code;
        this.error = new BaseMiss(code, msg);
    }

    public ErrorBean(int code, String msg, boolean loadMore) {
        this.msg = msg;
        this.code = code;
        this.loadMore = loadMore;
        this.error = new BaseMiss(code, msg);
    }

    public ErrorBean(Throwable error) {
        this.error = error;

        if (error instanceof BaseMiss) {
            this.msg = ((BaseMiss) error).getMsg();
            this.code = ((BaseMiss) error).getCode();
        }
    }

    public ErrorBean(Throwable error, boolean loadMore) {
        this(error);

        this.loadMore = loadMore;
    }

    public ErrorBean(Throwable e, int code, String msg) {
        this.msg = msg;
        this.error = e;
        this.code = code;
    }

    @BindMode
    public int getMode() {
        return mode;
    }

    public void setMode(@BindMode int mode) {
        this.mode = mode;
    }

    public Throwable getError() {
        return error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isLoadMore() {
        return loadMore;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    @Override
    public String toString() {
        return "ErrorBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", loadMore=" + loadMore +
                '}';
    }
}
