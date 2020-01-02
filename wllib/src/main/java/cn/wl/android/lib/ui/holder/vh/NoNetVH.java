package cn.wl.android.lib.ui.holder.vh;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;

import cn.wl.android.lib.R;
import cn.wl.android.lib.core.ErrorBean;
import cn.wl.android.lib.ui.holder.StatusCodePool;
import cn.wl.android.lib.utils.WLClick;

/**
 * Created by JustBlue on 2019-08-28.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class NoNetVH extends WLHolder implements IAinmHolder {

    private View topView;
    private View bottomView;

    private final int mTransOffset;

    public NoNetVH() {
        super(StatusCodePool.NONET_CODE);

        mTransOffset = ConvertUtils.dp2px(56);
    }

    @Override
    public void update(ErrorBean data) {

    }

    @Override
    protected void initContentView(View contentView) {
        contentView.setOnClickListener(new WLClick(v -> {
            publishClick();
        }));

        bottomView = getView(R.id.btn_goto_net);
        bottomView.setOnClickListener(new WLClick(v -> {
            publishEvent(new Bundle());
        }));

        topView = getView(R.id.iv_holder_top);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_holder_no_network;
    }

    @Override
    public void onAnim(float fraction) {
        topView.setTranslationY(-mTransOffset * fraction);
        bottomView.setTranslationY(mTransOffset * fraction);

        getContentView().setAlpha(1-fraction);
    }
}
