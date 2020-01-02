package cn.wl.android.lib.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by JustBlue on 2019-11-19.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 自定义activity栈
 */
public class ActStack {

    private static final class Holder {

        private static final ActStack mIns = new ActStack();
    }

    public static ActStack get() {
        return Holder.mIns;
    }

    private LinkedList<Activity> mActivityStack = new LinkedList<>();

    /**
     * 添加activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mActivityStack.add(activity);
    }

    /**
     * 删除activity
     *
     * @param activity
     */
    public void delActivity(Activity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 获取顶层Activity
     *
     * @return
     */
    public Activity getTopActivity() {
        try {
            return mActivityStack.getLast();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
