package cn.wl.android.lib.data.core;

import cn.wl.android.lib.config.WLConfig;
import cn.wl.android.lib.utils.Gsons;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: {@link retrofit2.Retrofit} 管理类
 * 用于保存{@link retrofit2.Retrofit} 单例和创建
 * 接口service
 */
public class RetrofitHelper {

    private volatile static RetrofitHelper mInstance;

    private volatile Retrofit mDefaultRetrofit;
    private volatile Retrofit mResourceRetrofit;

    private RetrofitHelper() {
    }

    /**
     * 获取{@link RetrofitHelper}单例
     *
     * @return
     */
    public static RetrofitHelper getIns() {
        RetrofitHelper instance = RetrofitHelper.mInstance;

        if (instance == null) {
            synchronized (RetrofitHelper.class) {
                instance = RetrofitHelper.mInstance;
                if (instance == null) {
                    instance = new RetrofitHelper();
                    RetrofitHelper.mInstance = instance;
                }
            }
        }

        return instance;
    }

    private Retrofit getDefault() {
        if (mDefaultRetrofit == null) {
            synchronized (RetrofitHelper.class) {
                if (mDefaultRetrofit == null) {
                    mDefaultRetrofit = new Retrofit.Builder()
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(Gsons.getGson()))
                            .baseUrl(WLConfig.getBaseUrl())
                            .client(OKHttpHelper.getDefault())
                            .build();
                }
            }
        }

        return mDefaultRetrofit;
    }

    /**
     * 获取接口service代理对象
     *
     * @param clazz
     * @param <Ser>
     * @return
     */
    public <Ser> Ser getService(Class<Ser> clazz) {
        return getDefault().create(clazz);
    }


}
