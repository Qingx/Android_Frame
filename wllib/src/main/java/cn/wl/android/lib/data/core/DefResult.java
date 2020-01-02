package cn.wl.android.lib.data.core;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.FileNotFoundException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import cn.wl.android.lib.BuildConfig;
import cn.wl.android.lib.config.BaseConfig;
import cn.wl.android.lib.core.ErrorBean;
import cn.wl.android.lib.miss.BaseMiss;
import cn.wl.android.lib.utils.Toasts;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: {@link io.reactivex.Observable} 默认处理过程
 */
public abstract class DefResult<T> implements Observer<T>, Disposable {

    private static final int MAX_CASE = 15;
    private AtomicReference<Disposable> mDis = new AtomicReference();

    @Override
    public void dispose() {
        DisposableHelper.dispose(mDis);
    }

    @Override
    public boolean isDisposed() {
        Disposable disposable = mDis.get();

        if (disposable == null) return true;
        return disposable.isDisposed();
    }

    @Override
    public  void onSubscribe(Disposable d) {
        DisposableHelper.setOnce(mDis, d);
    }

    @Override
    public final void onNext(T t) {
        try {
            doNext(t);
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }

    /**
     * 自定义数据处理
     *
     * @param data
     */
    protected abstract void doNext(T data) throws Exception;

    @Override
    public final void onError(Throwable e) {
        Throwable actual = getFinalCase(e);

        CM cm = getErrorDesc(actual);
        ErrorBean bean = resolveError(actual, cm);

        Throwable error = bean.getError();

        if (BuildConfig.DEBUG) {
            error.printStackTrace();
        }

        try {

            doError(bean);

            doFinally(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            RxJavaPlugins.onError(ex);
        }
    }

    /**
     * 处理异常数据
     *
     * @param bean
     */
    protected void doError(ErrorBean bean) throws Exception {
        Toasts.show(bean.getMsg());
    }

    protected ErrorBean resolveError(Throwable e, CM cm) {
        return new ErrorBean(e, cm.code, cm.msg);
    }

    @Override
    public final void onComplete() {
        try {
            doFinally(false);
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }

    /**
     * 终结函数，无论正常或异常都会调用，且在最后调用
     *
     * @param fromMiss
     */
    protected void doFinally(boolean fromMiss) throws Exception {

    }

    /**
     * 获取原始异常
     *
     * @param t
     * @return
     */
    private Throwable getFinalCase(Throwable t) {
        int count = 0;
        Throwable e = t;

        while (e.getCause() != null) {
            e = e.getCause();

            if (count++ > MAX_CASE) {
                return e;
            }
        }

        return e;
    }

    /**
     * 获取异常提示
     *
     * @param t
     * @return
     */
    private CM getErrorDesc(Throwable t) {
        int code = 0;
        String msg = "服务异常, 请稍后再试";

        if (t instanceof BaseMiss) {
            msg = ((BaseMiss) t).getMsg();
            code = ((BaseMiss) t).getCode();
        } else if (t instanceof SocketTimeoutException || t instanceof TimeoutException) {
            msg = "连接超时, 请稍后再试";
        } else if (t instanceof FileNotFoundException) {
            msg = "未找到指定文件";
        }

        return new CM(code, msg);
    }

    public static class CM {
        private final int code;
        private final String msg;

        public CM(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }
    }

}
