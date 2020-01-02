package cn.wl.android.lib.utils.result;

import android.content.Intent;
import android.os.Parcelable;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

/**
 * Created by JustBlue on 2019-08-31.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 自定义Observable, 添加系统内的转换方法
 */
public class ResultObservable extends Observable<ActivityResult> {

    private final int requestCode;
    private final Observable<ActivityResult> upstream;

    public ResultObservable(Observable<ActivityResult> upstream, int requestCode) {
        this.upstream = upstream;
        this.requestCode = requestCode;
    }

    @Override
    protected void subscribeActual(Observer<? super ActivityResult> observer) {
        this.upstream.subscribe(observer);
    }

    /**
     * 获取过滤{@link #requestCode}的事件流发射器
     * 并且在执行一次后自动销毁
     *
     * @return
     */
    private Observable<ActivityResult> getFilterUpstream() {
        return this.filter(t -> t.getRequestCode() == this.requestCode)
                .take(1);
    }

    /**
     * 将界面回传数据转换成String
     *
     * @return
     */
    public Observable<DataResult<String>> asString() {
        return getFilterUpstream()
                .map(new Mapper<>(t -> t.getStringExtra(RxResult.KEY)));
    }

    /**
     * 将界面回传数据转换成Int
     *
     * @param defValue 添加默认值
     * @return
     */
    public Observable<DataResult<Integer>> asInt(int defValue) {
        return getFilterUpstream()
                .map(new Mapper<>(t -> t.getIntExtra(RxResult.KEY, defValue)));
    }

    /**
     * 将界面回传数据转换成Long
     *
     * @param defValue
     * @return
     */
    public Observable<DataResult<Long>> asLong(long defValue) {
        return getFilterUpstream()
                .map(new Mapper<>(t -> t.getLongExtra(RxResult.KEY, defValue)));
    }

    /**
     * 将界面回传数据转换成float
     *
     * @param dafValue
     * @return
     */
    public Observable<DataResult<Float>> asFloat(float dafValue) {
        return getFilterUpstream()
                .map(new Mapper<>(t -> t.getFloatExtra(RxResult.KEY, dafValue)));
    }

    /**
     * 将界面回传的数据转换成{@link Serializable}的对象
     *
     * @param <T>
     * @return
     */
    public <T extends Serializable> Observable<DataResult<T>> asSerializable() {
        return getFilterUpstream()
                .map(new Mapper<>(t -> (T) t.getSerializableExtra(RxResult.KEY)));
    }

    /**
     * 将界面转换成{@link Parcelable}的对象
     *
     * @param <T>
     * @return
     */
    public <T extends Parcelable> Observable<DataResult<T>> asParcelable() {
        return getFilterUpstream()
                .map(new Mapper<>(t -> t.getParcelableExtra(RxResult.KEY)));
    }

    /**
     * 是否执行成功
     *
     * @return
     */
    public Observable<Boolean> checkSuccess() {
        return getFilterUpstream()
                .map(result -> result.isSuccess());
    }

    /**
     * 自定义转换器
     *
     * @param mapper
     * @param <T>
     * @return
     */
    public <T> Observable<DataResult<T>> mapBy(Function<Intent, T> mapper) {
        return getFilterUpstream()
                .map(result -> {
                    if (result.isSuccess()) {
                        T data = mapper.apply(result.getData());

                        if (data == null) return DataResult.cancel();

                        return DataResult.back(data);
                    } else {
                        return DataResult.cancel();
                    }
                });
    }

    // 自定义数据转换规则, 如果数据正常返回, 转换到指定类型, 如果失败发出一个取消事件
    private class Mapper<T> implements Function<ActivityResult, DataResult<T>> {

        private final Function<Intent, T> mapper;

        private Mapper(Function<Intent, T> mapper) {
            this.mapper = mapper;
        }

        @Override
        public DataResult<T> apply(ActivityResult result) throws Exception {
            if (result.isSuccess()) {
                T data = this.mapper.apply(result.getData());
                return DataResult.back(data);
            } else {
                return DataResult.cancel();
            }
        }
    }

}
