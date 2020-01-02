package cn.wl.android.lib.miss;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 网络连接异常
 */
public class NetMiss extends BaseMiss {

    public NetMiss() {
        super(0, "网络连接异常, 请检查您的网络设置后重试");
    }

}
