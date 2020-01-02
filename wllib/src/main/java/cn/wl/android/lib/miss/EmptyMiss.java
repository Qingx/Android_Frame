package cn.wl.android.lib.miss;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public final class EmptyMiss extends BaseMiss {

    public static final int CODE = -10001;

    public EmptyMiss() {
        super(CODE, "暂无数据");
    }

}
