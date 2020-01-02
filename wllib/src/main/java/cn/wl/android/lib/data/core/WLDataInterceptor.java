package cn.wl.android.lib.data.core;

import android.util.Log;

import java.io.IOException;

import cn.wl.android.lib.config.WLConfig;
import cn.wl.android.lib.miss.CommonMiss;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 默认数据拦截器
 */
public class WLDataInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        return proceed(chain);
    }

    private Response proceed(Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();

        String sign = HttpConfig.getSign();
        String token = HttpConfig.getToken();

        if (WLConfig.isDebug()) {
            Log.e("OkHttp", " sign -> " + sign);
            Log.e("OkHttp", " token ->" + token);
        }

        requestBuilder.addHeader("Authorization", token)
                .addHeader("sign", sign);

        Request request = requestBuilder.build();
        Response response = chain.proceed(request);

//            int code = response.code();
////            if (code != 200) {
////                throw new CommonMiss();
////            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new CommonMiss();
//        }

        return response;
    }

}
