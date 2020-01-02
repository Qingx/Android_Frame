package cn.wl.android.lib.ui.holder.vh;

import android.os.Bundle;
import android.view.View;

import cn.wl.android.lib.R;
import cn.wl.android.lib.core.ErrorBean;
import cn.wl.android.lib.ui.holder.StatusCodePool;
import cn.wl.android.lib.utils.WLClick;

/**
 * Created by JustBlue on 2019-08-29.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class TopNetVH extends WLHolder implements IAinmHolder {

    public TopNetVH() {
        super(StatusCodePool.TOP_NET_CODE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_holder_nonet_top;
    }

    @Override
    public void update(ErrorBean data) {

    }

    @Override
    protected void initContentView(View contentView) {
        getView(R.id.tv_holder_nonet).setOnClickListener(new WLClick(v -> {
            publishEvent(new Bundle());
        }));
    }

    @Override
    public boolean isTransparentBackground() {
        return true;
    }

    @Override
    public boolean interceptTouchEvent() {
        return false;
    }

    @Override
    public void onAnim(float fraction) {
        getView(R.id.tv_holder_nonet).setScaleY(1 - fraction);
//        ViewCompat.scal\\
    }
}
