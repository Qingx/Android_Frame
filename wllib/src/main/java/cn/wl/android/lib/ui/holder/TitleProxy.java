package cn.wl.android.lib.ui.holder;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import cn.wl.android.lib.R;
import cn.wl.android.lib.utils.OnClick;
import cn.wl.android.lib.utils.WLClick;
import cn.wl.android.lib.view.drawable.TitleDrawable;

/**
 * Created by JustBlue on 2019-09-03.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class TitleProxy {

    private Space spacePosition;
    private TextView tvTitleName;
    private ImageView ivTitleBack;
    private TextView tvTitleRight;
    private ImageView ivTitleRight;

    private ConstraintLayout clTitleRoot;

    public void initView(Activity activity) {
        clTitleRoot = activity.findViewById(R.id.cl_title_root);

        if (clTitleRoot != null) {
            tvTitleName = clTitleRoot.findViewById(R.id.tv_title_name);
            ivTitleBack = clTitleRoot.findViewById(R.id.iv_title_back);
            tvTitleRight = clTitleRoot.findViewById(R.id.tv_title_right);
            ivTitleRight = clTitleRoot.findViewById(R.id.iv_title_right);
            spacePosition = clTitleRoot.findViewById(R.id.space_position);

            ivTitleBack.setOnClickListener(new OnClick() {
                @Override
                protected void doClick(View v) {
                    Context context = v.getContext();

                    if (context instanceof Activity) {
                        ((Activity) context).onBackPressed();
                    }
                }
            });

            clTitleRoot.setBackground(new TitleDrawable());
        }
    }

    /**
     * 设置标题栏
     *
     * @param text
     */
    public void setTitle(CharSequence text) {
        if (tvTitleName != null) {
            tvTitleName.setText(text);
        }
    }

    /**
     * 设置左边icon图标和点击事件
     *
     * @param drawableRes
     * @param listener
     */
    public void setLeftIcon(@DrawableRes int drawableRes, View.OnClickListener listener) {
        if (ivTitleBack != null) {
            ivTitleBack.setImageResource(drawableRes);
            ivTitleBack.setOnClickListener(new WLClick(listener));
        }
    }

    /**
     * 设置右边的icon图标和点击事件
     *
     * @param drawableRes
     * @param listener
     */
    public void setRightIcon(@DrawableRes int drawableRes, View.OnClickListener listener) {
        if (ivTitleRight != null) {
            ivTitleRight.setVisibility(View.VISIBLE);
            ivTitleRight.setImageResource(drawableRes);
            ivTitleRight.setOnClickListener(new WLClick(listener));
        }
    }

    /**
     * 设置右边文字按钮文本、点击事件
     *
     * @param text
     * @param listener
     */
    public void setRightText(CharSequence text, View.OnClickListener listener) {
        if (tvTitleRight != null) {
            tvTitleRight.setVisibility(View.VISIBLE);
            tvTitleRight.setText(text);
            tvTitleRight.setOnClickListener(new WLClick(listener));
        }
    }

    /**
     * 设置左边是否可见
     *
     * @param visible
     */
    public void setLeftVisible(boolean visible) {
        if (ivTitleBack != null) {
            ivTitleBack.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

}
