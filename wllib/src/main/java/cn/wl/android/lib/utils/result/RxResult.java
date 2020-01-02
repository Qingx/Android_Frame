package cn.wl.android.lib.utils.result;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.io.Serializable;
import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by JustBlue on 2019-08-31.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 使用RxJava代理 {@link androidx.fragment.app.Fragment#onActivityResult(int, int, Intent)}
 */
public final class RxResult {

    public static final String KEY = "RxResultData";
    public static final String TAG = RxResult.class.getSimpleName();

    private final Lazy<ResultFragment> mFragmentLazy;

    public RxResult(AppCompatActivity activity) {
        this.mFragmentLazy = this.createFragmentLazy(activity.getSupportFragmentManager());
    }

    public RxResult(Fragment fragment) {
        this.mFragmentLazy = this.createFragmentLazy(fragment.getChildFragmentManager());
    }

    /**
     * 创建一个懒加载对象, 用于在需要的时候创建{@link ResultFragment}
     *
     * @param manager
     * @return
     */
    private Lazy<ResultFragment> createFragmentLazy(FragmentManager manager) {
        return new Lazy<ResultFragment>() {
            private ResultFragment mFragment;

            @Override
            public ResultFragment get() {
                if (this.mFragment == null) {
                    this.mFragment = getResultFragment(manager);
                }
                return this.mFragment;
            }
        };
    }

    /**
     * 获取{@link ResultFragment} 防止多次创建相同实例
     *
     * @param manager
     * @return
     */
    private ResultFragment getResultFragment(FragmentManager manager) {
        ResultFragment resultFragment =
                (ResultFragment) manager.findFragmentByTag(TAG);

        boolean isNewInstance = resultFragment == null;
        if (isNewInstance) {
            resultFragment = new ResultFragment();

            manager.beginTransaction()
                    .add(resultFragment, TAG)
                    .commitNow();
        }
        return resultFragment;
    }

    /**
     * 获取代理的{@link ResultFragment#onActivityResult(int, int, Intent)}
     * 事件的fragment, 可以通过该Fragment发送界面跳转, 并发布界面回传的数据
     *
     * @return
     */
    public ResultFragment getFragment() {
        return mFragmentLazy.get();
    }

    /**
     * 获取{@link ResultFragment#mResultSubject}, 用于监听界面回传的数据
     *
     * @return
     */
    public Observable<ActivityResult> getResultEvents() {
        return mFragmentLazy.get().getSubject();
    }

    /**
     * 获取一个默认的取消事件
     *
     * @param requestCode
     * @return
     */
    private ResultObservable getCancelEvent(int requestCode) {
        Observable<ActivityResult> observable = Observable
                .just(ActivityResult.getCancel(requestCode));

        return new ResultObservable(observable, requestCode);
    }

    /**
     * 获取获取指定requestCode的接收事件
     *
     * @param requestCode
     * @return
     */
    public ResultObservable getResultEvent(int requestCode) {
        Observable<ActivityResult> events = getResultEvents();

        return new ResultObservable(events, requestCode);
    }

    /**
     * @param targetClazz
     * @param requestCode
     * @return
     */
    public ResultObservable start(Class<? extends Activity> targetClazz, int requestCode) {
        FragmentActivity activity = getFragment().getActivity();

        // 当界面为空, 此时可能界面已经被销毁, 直接返回取消事件
        if (activity == null) return getCancelEvent(requestCode);

        Intent intent = new Intent(activity, targetClazz);
        return start(intent, requestCode);
    }

    /**
     * 开始一个跳转事件
     *
     * @param intent
     * @param requestCode
     * @return
     */
    public ResultObservable start(Intent intent, int requestCode) {
        Observable<ActivityResult> request = getFragment()
                .request(intent, requestCode);

        return new ResultObservable(request, requestCode);
    }

    /**
     * 回退界面, 并携带参数
     *
     * @param act
     * @param data
     */
    private static void exitWithData(Activity act, Intent data) {
        act.setResult(Activity.RESULT_OK, data);
        act.finish();
    }

    /**
     * 回退界面, 并回传一个String类型的参数
     *
     * @param act
     * @param data
     */
    public static void exitWithString(Activity act, String data) {
        Intent intent = new Intent();
        intent.putExtra(KEY, data);

        exitWithData(act, intent);
    }

    /**
     * 回退界面, 并回传一个Float类型的参数
     *
     * @param act
     * @param data
     */
    public static void exitWithFloat(Activity act, float data) {
        Intent intent = new Intent();
        intent.putExtra(KEY, data);

        exitWithData(act, intent);
    }

    /**
     * 回退界面, 并回传一个Int类型的参数
     *
     * @param act
     * @param data
     */
    public static void exitWithInt(Activity act, int data) {
        Intent intent = new Intent();
        intent.putExtra(KEY, data);

        exitWithData(act, intent);
    }

    /**
     * 回退界面, 并回传一个String类型的参数
     *
     * @param act
     * @param data
     */
    public static void exitWithLong(Activity act, long data) {
        Intent intent = new Intent();
        intent.putExtra(KEY, data);

        exitWithData(act, intent);
    }

    /**
     * 回退界面, 并回传一个Serializable类型的参数
     *
     * @param act
     * @param data
     */
    public static void exitWithSerializable(Activity act, Serializable data) {
        Intent intent = new Intent();
        intent.putExtra(KEY, data);

        exitWithData(act, intent);
    }

    /**
     * 回退界面, 并回传一个Serializable类型的参数
     *
     * @param act
     * @param data
     */
    public static void exitWithSerializableArrayList(Activity act, ArrayList<Serializable> data) {
        Intent intent = new Intent();
        intent.putExtra(KEY, data);

        exitWithData(act, intent);
    }

    /**
     * 回退界面, 并回传一个Parcelable类型的参数
     *
     * @param act
     * @param data
     */
    public static void exitWithSerializable(Activity act, Parcelable data) {
        Intent intent = new Intent();
        intent.putExtra(KEY, data);

        exitWithData(act, intent);
    }

    // 懒加载适配
    interface Lazy<T> {

        T get();
    }

}
