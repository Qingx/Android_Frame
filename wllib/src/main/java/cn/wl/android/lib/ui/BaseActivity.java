package cn.wl.android.lib.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import cn.wl.android.lib.core.ErrorBean;
import cn.wl.android.lib.data.core.DefResult;
import cn.wl.android.lib.miss.EmptyMiss;
import cn.wl.android.lib.miss.NetMiss;
import cn.wl.android.lib.ui.holder.HolderProxy;
import cn.wl.android.lib.ui.holder.StatusCodePool;
import cn.wl.android.lib.ui.holder.TitleProxy;
import cn.wl.android.lib.utils.Toasts;
import cn.wl.android.lib.view.holder.BaseHolder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by JustBlue on 2019-08-26.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public abstract class BaseActivity extends BaseCommonActivity {

    private ProgressDialog mAlert;
    private TitleProxy mTitleProxy;
    private HolderProxy mHolderProxy;

    @Override
    protected void internalViewCreated() {
        super.internalViewCreated();

        View view = getWindow().getDecorView();
        mHolderProxy = new HolderProxy(view) {
            @Override
            protected void onCustomClick(int statusCode) {
                retryLoadData();
            }

            @Override
            protected void onCustomHolder(int statusCode, Bundle data) {
                if (statusCode == StatusCodePool.NONET_CODE || statusCode == StatusCodePool.TOP_NET_CODE) {
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            protected BaseHolder createCustomHolder(ViewGroup parent, int status) {
                return null;
            }
        };
    }

    @Override
    public void showLoadingAlert(String msg) {
        if (mAlert == null) {
            mAlert = ProgressDialog.show(mActivity, "", "");
        }
        mAlert.setMessage(msg);
        mAlert.show();
    }

    @Override
    public void hideLoadingAlert() {
        if (mAlert != null) {
            mAlert.dismiss();
        }
    }

    /**
     * 获取title bar
     *
     * @return
     */
    protected TitleProxy getTitleBar() {
        if (mTitleProxy == null) {
            mTitleProxy = new TitleProxy();
            mTitleProxy.initView(mActivity);
        }

        return mTitleProxy;
    }

    @Override
    public void showLoading() {
        mHolderProxy.showLoading();
    }

    @Override
    public void showContent() {
        mHolderProxy.showContent();
    }

    @Override
    public void showNetMiss(ErrorBean bean) {
        mHolderProxy.showNetMiss(bean);
    }

    @Override
    public void showDataFail(ErrorBean bean) {
        mHolderProxy.showDataFail(bean);
    }

    @Override
    public void showEmptyData(ErrorBean bean) {
        mHolderProxy.showEmptyData(bean);
    }

    @Override
    public void showViewByStatus(int status) {
        mHolderProxy.showViewByStatus(status);
    }

    protected void setHolderBackgroundColor(@ColorRes int colorRes) {
        mHolderProxy.setBackgroundColor(colorRes);
    }

    @Override
    public void dispatchDataMiss(ErrorBean error) {
        if (!onInterceptDataMiss(error)) {
            Throwable miss = error.getError();

            if (miss instanceof EmptyMiss) {
                showEmptyData(error);
            } else if (miss instanceof NetMiss) {
                showNetMiss(error);
            } else {
                showDataFail(error);
            }

            Toasts.show(error.getMsg());
        }
    }

    @Override
    public boolean onInterceptDataMiss(ErrorBean error) {
        return false;
    }

    /**
     * 处理默认的业务逻辑, 并实现界面销毁后, 事件自销毁
     *
     * @param event
     * @param doNext
     * @param <T>
     */
    protected <T> void bindSub(@NonNull Observable<T> event,
                               @NonNull final Consumer<T> doNext) {
        bindSub(event, doNext, null, null);
    }


    /**
     * 处理默认的业务逻辑, 并实现界面销毁后, 事件自销毁
     *
     * @param event
     * @param doNext
     * @param <T>
     */
    protected <T> void bindSub(@NonNull Observable<T> event,
                               @NonNull final Consumer<T> doNext,
                               @NonNull final Consumer<ErrorBean> doError) {
        bindSub(event, doNext, doError, null);
    }

    /**
     * 处理默认的业务逻辑, 并实现界面销毁后, 事件自销毁
     *
     * @param event
     * @param doNext
     * @param doFinish
     * @param <T>
     */
    protected <T> void bindSub(@NonNull Observable<T> event,
                               @NonNull final Consumer<T> doNext,
                               @NonNull final Consumer<ErrorBean> doError,
                               @Nullable final Consumer<Boolean> doFinish) {
        event.observeOn(AndroidSchedulers.mainThread())
                .compose(bindDestroy())
                .subscribe(new DefResult<T>() {

                    @Override
                    protected void doNext(T data) throws Exception {
                        showContent();

                        doNext.accept(data);
                    }

                    @Override
                    protected void doError(ErrorBean bean) throws Exception {
                        if (doError != null) {
                            doError.accept(bean);
                        } else {
                            dispatchDataMiss(bean);
                        }
                    }

                    @Override
                    protected void doFinally(boolean fromMiss) throws Exception {
                        if (doFinish != null) {
                            doFinish.accept(fromMiss);
                        } else {
                            hideLoadingAlert();
                        }
                    }
                });
    }

    /**
     * 默认的绑定事件
     *
     * @param source
     * @param doNext
     * @param doError
     * @param doFinish
     * @param <T>
     */
    protected <T> void defSub(@NonNull Observable<T> source,
                              @NonNull final Consumer<T> doNext,
                              @NonNull final Consumer<ErrorBean> doError,
                              @Nullable final Consumer<Boolean> doFinish) {
        source.compose(bindDestroy())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefResult<T>() {

                    @Override
                    protected void doNext(T data) throws Exception {

                        doNext.accept(data);
                    }

                    @Override
                    protected void doError(ErrorBean bean) throws Exception {
                        if (doError != null) {
                            doError.accept(bean);
                        } else {
                            Toasts.show(bean.getMsg());
                        }
                    }

                    @Override
                    protected void doFinally(boolean fromMiss) throws Exception {
                        if (doFinish != null) {
                            doFinish.accept(fromMiss);
                        } else {
                            hideLoadingAlert();
                        }
                    }
                });
    }

    /**
     * 判断接口请求结果, 成功提示提交的文字
     *
     * @param event
     * @param successMsg
     */
    protected void bindToast(@NonNull Observable<Boolean> event,
                             @NonNull final String successMsg) {
        bindToast(event, successMsg, null);
    }

    /**
     * 判断接口请求结果, 成功提示提交的文字
     *
     * @param event
     * @param successMsg // 当请求成功时, 提示的消息
     * @param doSuccess  // 成功时的回调
     */
    protected void bindToast(@NonNull Observable<Boolean> event,
                             @NonNull final String successMsg,
                             @Nullable final Runnable doSuccess) {
        bindToast(event, successMsg, true, doSuccess);
    }

    /**
     * 判断接口请求结果, 成功提示提交的文字
     *
     * @param event
     * @param successMsg // 当请求成功时, 提示的消息
     * @param doSuccess  // 成功时的回调
     */
    protected void bindToast(@NonNull Observable<Boolean> event,
                             @NonNull final String successMsg,
                             boolean isSuccessHideAlert,
                             @Nullable final Runnable doSuccess) {
        event.observeOn(AndroidSchedulers.mainThread())
                .compose(bindDestroy())
                .subscribe(new DefResult<Boolean>() {

                    @Override
                    protected void doNext(Boolean data) throws Exception {
                        Toasts.show(successMsg);

                        if (doSuccess != null) {
                            doSuccess.run();
                        }
                    }

                    @Override
                    protected void doError(ErrorBean bean) throws Exception {
                        Toasts.show(bean.getMsg());
                    }

                    @Override
                    protected void doFinally(boolean fromMiss) throws Exception {
                        if (fromMiss || isSuccessHideAlert) {
                            hideLoadingAlert();
                        }
                    }
                });
    }

    /**
     * 选择分页数
     *
     * @param vp
     * @param doSelected
     */
    protected void doPageSelected(ViewPager vp, Consumer<Integer> doSelected) {
        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                try {
                    doSelected.accept(position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        vp.addOnPageChangeListener(listener);

        doOnDestroy(() -> {
            vp.removeOnPageChangeListener(listener);
        });
    }

}
