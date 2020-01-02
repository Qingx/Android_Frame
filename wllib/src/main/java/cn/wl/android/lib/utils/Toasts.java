package cn.wl.android.lib.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import static cn.wl.android.lib.config.WLConfig.getContext;

/**
 * toast工具
 * Created by liyong on 2018/3/12.
 */
public class Toasts {

    /**
     * 显示默认{@link Toast}
     *
     * @param message
     */
    public static void show(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        toastInstance(message).show();
    }

    /**
     * 显示默认{@link Toast}
     *
     * @param resId
     */
    public static void show(int resId) {
        String message = getContext()
                .getResources().getString(resId);
        show(message);
    }

    /**
     * 中心显示Toast
     *
     * @param message
     */
    public static void showCenter(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Toast toast = toastInstance(message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 中心显示toast
     *
     * @param resId
     */
    public static void showCenter(int resId) {
        String message = getContext()
                .getResources().getString(resId);
        showCenter(message);
    }

    /**
     * toast实例
     *
     * @param message
     * @return
     */
    private static Toast toastInstance(String message) {
        return Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
    }

}
