package cn.wl.android.lib.config;

import android.content.Context;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public final class WLConfig {

    private static String mBaseUrl; // 基础请求路径
    private static boolean isDebug; // 是否为开发环境
    private static UserProvider mProvider;
    private static Context mContext;// application context;

    private static AtomicBoolean isInit = new AtomicBoolean();

    /**
     * 初始化配置参数
     *
     * @param context
     * @param isDebug
     * @param baseUrl
     */
    public static void init(Context context, boolean isDebug, String baseUrl) {
        if (isInit.compareAndSet(false, true)) {
            WLConfig.isDebug = isDebug;
            WLConfig.mContext = context;
            WLConfig.mBaseUrl = baseUrl;

            Utils.init(context.getApplicationContext());
        }
    }

    /**
     * 是否是开发模式
     *
     * @return
     */
    public static boolean isDebug() {
        return WLConfig.isDebug;
    }

    /**
     * 获取项目主路径
     *
     * @return
     */
    public static String getBaseUrl() {
        return WLConfig.mBaseUrl;
    }

    /**
     * 获取全局Context
     *
     * @return
     */
    public static Context getContext() {
        return WLConfig.mContext;
    }

    /**
     * 获取{@link androidx.core.content.FileProvider} 授权码
     *
     * @return
     */
    public static String getAuthCode() {
        return mContext.getPackageName() + ".FileProvider";
    }

    /**
     * 判断是否联网
     *
     * @return
     */
    public static boolean isConnected() {
        return NetworkUtils.isConnected();
    }

    /**
     * 设置用户提供者
     *
     * @param provider
     */
    public static void setUserProvider(UserProvider provider) {
        mProvider = provider;
    }

    /**
     * 获取用户提供者
     *
     * @return
     */
    public static UserProvider getProvider() {
        return mProvider;
    }

    /**
     * 获取用户id
     *
     * @return
     */
    public static String getUserId() {
        UserProvider provider = getProvider();

        if (provider != null) {
            String userId = provider.userId();
            return userId == null ? "" : userId;
        }

        return "";
    }

    /**
     * 获取交接班id
     *
     * @return
     */
    public static String getCarryId() {
        UserProvider provider = getProvider();

        if (provider != null) {
            String carryId = provider.carryId();

            return carryId == null ? "" : carryId;
        }

        return "";
    }

    /**
     * 获取巡查id
     *
     * @return
     */
    public static String getPatrolId() {
        UserProvider provider = getProvider();

        if (provider != null) {
            String patrolId = provider.patrolId();

            return patrolId == null ? "" : patrolId;
        }

        return "";
    }

    /**
     * 获取巡查车辆信息
     *
     * @return
     */
    public static String getPatrolCarId() {
        UserProvider provider = getProvider();

        if (provider != null) {
            String patrolCarId = provider.patrolCarId();

            return patrolCarId == null ? "" : patrolCarId;
        }

        return "";
    }

    public interface UserProvider {

        // 当前用户id
        String userId() throws NullPointerException;

        // 当前部门id
        String deptId() throws NullPointerException;

        // 当前接班id
        String carryId() throws NullPointerException;

        // 当前巡查id
        String patrolId() throws NullPointerException;

        // 获取巡查车辆
        String patrolCarId() throws NullPointerException;

    }

}
