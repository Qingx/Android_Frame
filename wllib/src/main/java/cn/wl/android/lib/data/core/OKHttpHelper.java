package cn.wl.android.lib.data.core;

import java.util.concurrent.TimeUnit;

import cn.wl.android.lib.config.WLConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: okHttp辅助创建类
 */
public class OKHttpHelper {

    public static final long UPLOAD_TIMEOUT = 60_000L;  // 文件上传超时时间
    public static final long DEFAULT_TIMEOUT = 16_000L; // 默认连接超时时间

    /**
     * 创建默认的{@link okhttp3.OkHttpClient.Builder}构造器
     *
     * @return
     */
    private static OkHttpClient.Builder getDefaultBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .callTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(getLoggerInterceptor())
                .retryOnConnectionFailure(true); // 开启失败后重试
    }

    /**
     * 获取接口日志拦截器
     *
     * @return
     */
    private static Interceptor getLoggerInterceptor() {
        HttpLoggingInterceptor interceptor =
                new HttpLoggingInterceptor();

        if (WLConfig.isDebug()) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return interceptor;
    }

    /**
     * 获取默认{@link OkHttpClient}
     * 注: 用于普通非上传资源文件的接口
     *
     * @return
     */
    public static OkHttpClient getDefault() {
        return getDefaultBuilder()
                .addInterceptor(new WLDataInterceptor())
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * 获取文件上传的{@link OkHttpClient}
     * 注: 用于上传、下载文件, 超时时间会更长
     *
     * @return
     */
    public static OkHttpClient getResource() {
        return getDefaultBuilder()
                .addInterceptor(new WLDataInterceptor())
                .writeTimeout(UPLOAD_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(UPLOAD_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

}
