package cn.wl.android.lib.utils;

import com.blankj.utilcode.util.TimeUtils;

import java.util.Date;

/**
 * Created by JustBlue on 2019-09-03.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 时间格式
 */
public class DateFormat {

    public static final String DATEFORMAT_yyyy_MM_dd_HHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String DATEFORMAT_HHmmss = "HH:mm:ss";
    public static final String DATEFORMAT_yyyy_MM_dd_HHmm = "yyyy-MM-dd HH:mm";
    public static final String DATEFORMAT_yyyyMMdd_HHmm = "yyyyMMdd HH:mm";
    public static final String DATEFORMAT_yyyyzMMzddz_HHmm = "yyyy年MM月dd日 HH:mm";
    public static final String DATEFORMAT_yyyyzMMzddz = "yyyy年MM月dd日";
    public static final String DATEFORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String DATEFORMAT_yyyyMMdd = "yyyyMMdd";
    public static final String DATEFORMAT_yyyyMMdd2 = "yyyy.MM.dd";
    public static final String DATEFORMAT_yMd_Hm = "yyyy.MM.dd HH:mm";
    public static final String DATEFORMAT_MM_dd_HHmm = "yyyy-MM-dd HH:mm";
    public static final String DATEFORMAT_MMz_ddz_HHmm = "MM月dd日 HH:mm";
    public static final String DATEFORMAT_MMdd = "MMdd";
    public static final String DATEFORMAT_yyyy = "yyyy";
    public static final String DATEFORMAT_MM = "MM";
    public static final String DATEFORMAT_dd = "dd";
    public static final String DATEFORMAT_HHmm = "HH:mm";
    public static final String DATEFORMAT_MM_dd = "MM-dd";
    public static final String DATEFORMAT_MM_dd_HH_mm = "MM-dd HH:mm";
    public static final String DATEFORMAT_MMz_ddz = "MM月dd日";


    public final static long SCEND_1 = 1000;// 1秒钟
    public final static long MINUTE_1 = 60 * 1000;// 1分钟
    public final static long MINUTE_5 = 5 * MINUTE_1;// 5分钟
    public final static long HOUR_1 = 60 * MINUTE_1;// 1小时
    public final static long DAY_1 = 24 * HOUR_1;// 1天
    public final static long WEEK_1 = 7 * DAY_1; // 1周
    public final static long MONTH_1 = 31 * DAY_1;// 月
    public final static long YEAR_1 = 12 * MONTH_1;// 年

    /**
     * 格式化时间为 yyyy-MM-dd HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String date2YY_MM_dd_HH_mm_ss(long time) {
        return TimeUtils.date2String(new Date(time),
                DATEFORMAT_yyyy_MM_dd_HHmmss);
    }

    public static String date2YY_MM_DD(long time){
        return TimeUtils.date2String(new Date(time),
                DATEFORMAT_yyyy_MM_dd);
    }

}
