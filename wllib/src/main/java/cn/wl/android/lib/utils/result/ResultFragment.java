package cn.wl.android.lib.utils.result;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by JustBlue on 2019-08-31.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 用于拦截 {@link Fragment#onActivityResult(int, int, Intent)}
 * 并用Rxjava做代理发布事件
 */
public class ResultFragment extends Fragment {

    /**
     * 判断当前Fragment是否已注销
     */
    private boolean isDestroy = false;

    /**
     * 数据发布器, 用于发布{@link #onActivityResult(int, int, Intent)}
     * 接收到的界面回传数据
     */
    private PublishSubject<ActivityResult> mResultSubject;

    /**
     * 获取数据发布Observable
     *
     * @return
     */
    public Observable<ActivityResult> getSubject() {
        if (mResultSubject == null) {
            mResultSubject = PublishSubject.create();
        }

        return mResultSubject;
    }

    /**
     * 执行界面跳转并返回{@link #mResultSubject}
     *
     * @param intent
     * @param requestCode
     * @return
     */
    public Observable<ActivityResult> request(Intent intent, int requestCode) {
        if (isDestroy) {
            return Observable.just(ActivityResult.getCancel(requestCode));
        }
        startActivityForResult(intent, requestCode);

        return getSubject();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 设置当前Fragment可以重用
         */
        setRetainInstance(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ActivityResult result = new ActivityResult(data, resultCode, requestCode);
        if (mResultSubject != null) {
            mResultSubject.onNext(result);
        }
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        if (mResultSubject != null) {
            mResultSubject.onComplete();
        }

        super.onDestroy();
    }
}
