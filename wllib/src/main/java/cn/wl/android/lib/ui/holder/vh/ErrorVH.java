package cn.wl.android.lib.ui.holder.vh;

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
public class ErrorVH extends WLHolder implements IAinmHolder {

    private final int mTransOffset;
    private View mTopView;
    private View mBotView;

    public ErrorVH() {
        super(StatusCodePool.ERROR_CODE);

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

        mTopView = getView(R.id.iv_holder_top);
        mBotView = getView(R.id.tv_holder_bottom);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_holder_error;
    }

    @Override
    public void onAnim(float fraction) {
        mTopView.setTranslationY(-mTransOffset * fraction);
        mBotView.setTranslationY(mTransOffset * fraction);

        getContentView().setAlpha(1-fraction);
    }
}
