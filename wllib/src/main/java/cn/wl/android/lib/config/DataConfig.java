package cn.wl.android.lib.config;

import cn.wl.android.lib.data.core.HttpConfig;
import cn.wl.android.lib.utils.SPUtils;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 数据缓存
 */
public final class DataConfig {

    private static class SPHolder {
        private static final String KEY_TOKEN = "KEY_TOKEN";

        private static SPUtils spInstance = SPUtils.getInstance("sp-data-base");
    }

    /**
     * 保存token到偏好设置
     *
     * @param token
     */
    public static void saveToken(String token) {
        SPHolder.spInstance.put(SPHolder.KEY_TOKEN, token);
    }

    /**
     * 获取偏好设置中保存的token
     *
     * @return
     */
    public static String getToken() {
        return SPHolder.spInstance.getString(
                SPHolder.KEY_TOKEN, HttpConfig.EMPTY_TOKEN);
    }

}
