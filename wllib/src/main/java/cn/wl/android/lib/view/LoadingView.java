package cn.wl.android.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.victor.loading.book.BookLoading;

/**
 * Created by JustBlue on 2019-08-28.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class LoadingView extends BookLoading {

    private boolean isStartLock = false;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

//        if (getVisibility() == VISIBLE) {
//            if (!isStartLock) {
//                isStartLock = true;
//
//                start();
//            }
//        } else {
//            if (isStartLock) {
//                isStartLock = false;
//
//                stop();
//            }
//        }
    }

}
