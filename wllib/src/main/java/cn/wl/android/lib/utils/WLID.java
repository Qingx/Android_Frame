package cn.wl.android.lib.utils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.EncryptUtils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by JustBlue on 2019-09-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 生成全局唯一标识
 */
public final class WLID {

    private static AtomicLong mCount = new AtomicLong();

    /**
     * 获取全局唯一id
     *
     * @return
     */
    public static String getId() {
        long current = Times.current() + mCount.incrementAndGet();
        String deviceId = DeviceUtils.getUniqueDeviceId();

        return deviceId + current;
    }

}
