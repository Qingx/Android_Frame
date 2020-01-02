package cn.wl.android.lib.utils;

import android.view.View;

/**
 * Created by JustBlue on 2019-08-28.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: 屏蔽双重点击
 */
public class WLClick implements View.OnClickListener {

    private static final long TIME_VALID = 350;

    private long mLastClickTime;
    private final View.OnClickListener mLis;

    public WLClick(View.OnClickListener mLis) {
        this.mLis = mLis;
    }

    @Override
    public void onClick(View v) {
        long time = System.currentTimeMillis();

        long timeOffset = time - mLastClickTime;
        if (time < 0 || timeOffset > TIME_VALID) {
            mLastClickTime = time;

            mLis.onClick(v);
        }
    }

}
