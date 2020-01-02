package cn.wl.android.lib.utils;

import java.text.DecimalFormat;

/**
 * Created by JustBlue on 2019-11-16.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public final class FormatUtil {

    /**
     * 格式化数据
     *
     * @param num
     * @return
     */
    public static String num0_xx(double num) {
        try {
            return new DecimalFormat("0.##").format(num);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
