package cn.wl.android.lib.miss;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class BaseMiss extends RuntimeException {

    private final int code;
    private final String msg;

    public BaseMiss(Throwable e) {
        super(e);
        this.code = -1;
        this.msg = "服务异常, 请稍后再试";
    }

    public BaseMiss(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
