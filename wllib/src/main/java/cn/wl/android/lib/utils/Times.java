package cn.wl.android.lib.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by JustBlue on 2019-08-28.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 时间管理, 添加时间偏移
 */
public class Times {

    private static AtomicLong mTimeOffset = new AtomicLong();

    /**
     * 获取添加时间偏移后的系统时间
     *
     * @return
     */
    public static long current() {
        for (; ; ) {
            long offset = mTimeOffset.get();

            long time = System.currentTimeMillis();
            long actual = time + offset;

            if (offset == mTimeOffset.get()) {
                return actual;
            }
        }
    }

}
