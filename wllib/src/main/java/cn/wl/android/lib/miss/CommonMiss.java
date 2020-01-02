package cn.wl.android.lib.miss;

import io.reactivex.Observable;

/**
 * Created by JustBlue on 2019-11-15.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class CommonMiss extends BaseMiss {

    public CommonMiss() {
        super(-12, "服务连接异常, 请稍后再试");
    }

}
