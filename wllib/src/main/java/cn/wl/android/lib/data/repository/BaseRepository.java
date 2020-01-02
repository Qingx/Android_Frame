package cn.wl.android.lib.data.repository;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.NetworkUtils;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.wl.android.lib.core.IDataSource;
import cn.wl.android.lib.core.PageParam;
import cn.wl.android.lib.data.core.RetrofitHelper;
import cn.wl.android.lib.miss.BaseMiss;
import cn.wl.android.lib.miss.EmptyMiss;
import cn.wl.android.lib.miss.NetMiss;
import cn.wl.android.lib.utils.Gsons;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by JustBlue on 2019-08-24.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class BaseRepository<Ser> {

    static class TYPE {

        static MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    }

    private volatile Ser mService;

    private final Class<Ser> mServiceClazz;

    public BaseRepository() {
        Class<? extends BaseRepository> clazz = getClass();
        Type superclass = clazz.getGenericSuperclass();

        ParameterizedType type = (ParameterizedType) superclass;
        mServiceClazz = (Class<Ser>) type.getActualTypeArguments()[0];
    }

    /**
     * 获取接口服务
     *
     * @return
     */
    protected final Ser getService() {
        if (mService == null) {
            synchronized (BaseRepository.class) {
                if (mService == null) {
                    mService = createService(mServiceClazz);
                }
            }
        }
        return mService;
    }

    /**
     * 创建service
     *
     * @param clazz
     * @return
     */
    protected Ser createService(Class<Ser> clazz) {
        return RetrofitHelper.getIns().getService(clazz);
    }

    /**
     * 创建Json并封装构造器
     *
     * @param jsonCreator
     * @return
     */
    public RequestBody bodyByCall(Consumer<JSONObject> jsonCreator) {
        JSONObject object = new JSONObject();

        try {
            jsonCreator.accept(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyFromJson(object);
    }

    /**
     * 通过实体类创建{@link RequestBody}
     *
     * @return
     */
    protected RequestBody bodyFromBean(Object bean) {
        String json = Gsons.getGson().toJson(bean);

        return bodyFromString(json);
    }

    /**
     * 通过{@link JSONObject}对象创建{@link RequestBody}
     *
     * @param jsonObject
     * @return
     */
    protected RequestBody bodyFromJson(@NonNull JSONObject jsonObject) {
        return bodyFromString(jsonObject.toString());
    }

    /**
     * 通过json字符串创建{@link RequestBody}
     *
     * @return
     */
    protected RequestBody bodyFromString(@NonNull String jsonStr) {
        RequestBody body = RequestBody.create(TYPE.JSON_TYPE, jsonStr);

        return body;
    }

    /**
     * 通过创建器创建参数上传
     *
     * @param creator
     * @return
     */
    protected RequestBody bodyFromCreator(@NonNull Consumer<Map<String, Object>> creator) {
        HashMap<String, Object> map = new HashMap<>();

        try {
            creator.accept(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyFromBean(map);
    }

    /**
     * 通过创建器创建参数上传
     *
     * @param creator
     * @return
     */
    protected RequestBody bodyFromCreator(@NonNull PageParam param, @NonNull Consumer<Map<String, Object>> creator) {
        HashMap<String, Object> map = new HashMap<>();

        map.put("page", param.getPage());
        map.put("pageSize", param.getPageSize());

        List<PageParam.OrderBy> orderBy = param.getOrderBy();
        if (orderBy != null && !orderBy.isEmpty()) {
            map.put("orderBy", param.getOrderBy());
        }

        try {
            creator.accept(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bodyFromBean(map);
    }

    /**
     * 整合所有统一操作
     * 1、检查网络
     * 2、调度到子线程执行
     * 3、...
     *
     * @param <T>
     * @return
     */
    protected <T> ObservableTransformer<T, T> combine() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .compose(checkNetwork())
                .delay(500, TimeUnit.MILLISECONDS, true);
    }

    /**
     * 将坚持网络绑定到主流程
     *
     * @param <T>
     * @return
     */
    protected <T> ObservableTransformer<T, T> checkNetwork() {
        return upstream -> upstream.startWith(networkEvent());
    }

    /**
     * 判断是否有网络, 如没有抛出{@link NetMiss}异常
     *
     * @param <T>
     * @return
     */
    private <T> Observable<T> networkEvent() {
        return Observable.defer(() -> {
            if (NetworkUtils.isConnected()) {
                return Observable.empty();
            } else {
                return Observable.error(new NetMiss());
            }
        });
    }

    /**
     * 只判断接口是否返回成功, 一般用户提交数据并判断是否成功,
     * 没有特定的返回数据
     * <p>
     * 注: 此转换只会在onNext中接收的true, false会以异常的形式下发
     *
     * @return
     */
    protected ObservableTransformer<IDataSource<?>, Boolean> success() {
        return upstream -> upstream.flatMap((Function<IDataSource<?>, ObservableSource<Boolean>>) source -> {
            if (source.isSuccess()) {
                return Observable.just(true);
            } else {
                Throwable miss = resolveDataMiss(source);

                return Observable.error(miss);
            }
        });
    }

    /**
     * 将业务数据{@link IDataSource}去除外层的数据封装
     * 当成功时--> 返回IDataSource的实际类型(及 <T>)
     * 当失败时--> 封装自定义异常并下发
     * <p>
     * 注: {@link EmptyMiss}在业务数据code正常, 但返回的数据为空时出现
     *
     * @param <T> IDSource<T> 包装的实际数据类型
     * @return
     */
    protected <T> ObservableTransformer<IDataSource<T>, T> rebase() {
        return upstream -> upstream.flatMap((Function<IDataSource<T>, ObservableSource<T>>) source -> {
            if (source.isSuccess()) {
                if (source.isDataEmpty()) {
                    return Observable.error(new EmptyMiss());
                } else {
                    T data = source.getData();

                    return Observable.just(data);
                }
            } else {
                Throwable miss = resolveDataMiss(source);

                return Observable.error(miss);
            }
        });
    }

    /**
     * 通过指定的code转换本地异常
     * <p>
     * 注: 此时已确定该接口数据为异常
     *
     * @param source
     * @param <T>
     * @return
     */
    protected <T> Throwable resolveDataMiss(IDataSource<T> source) {
        // TODO: 添加默认异常
        // ...
        return new BaseMiss(source.getCode(), source.getMsg());
    }

}
