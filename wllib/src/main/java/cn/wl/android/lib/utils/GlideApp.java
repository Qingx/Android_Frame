package cn.wl.android.lib.utils;

import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.wl.android.lib.R;

/**
 * Created by JustBlue on 2019-08-30.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class GlideApp {

    @DrawableRes public static final int DRAW_DEFAULT = R.drawable.draw_default;
    @DrawableRes public static final int DRAW_FAILURE = R.drawable.draw_default;

    private static RequestOptions options;

    /**
     * 返回默认的{@link RequestOptions}
     *
     * @return
     */
    private static RequestOptions getOptions() {
        if (options == null) {
            options = new RequestOptions()
                    .placeholder(DRAW_DEFAULT)
                    .error(DRAW_FAILURE)
                    .lock();
        }

        return options;
    }

    /**
     * 显示图片
     *
     * @param resource
     * @param displayView
     */
    public static void display(Object resource, ImageView displayView, @DrawableRes int dRes) {
        RequestOptions options = getOptions().clone()
                .dontAnimate()
                .centerCrop()
                .placeholder(dRes)
                .error(dRes);

        displayByOptions(resource, displayView, options);
    }

    /**
     * 显示图片
     *
     * @param resource
     * @param displayView
     */
    public static void display(Object resource, ImageView displayView) {
        RequestOptions options = getOptions().clone()
                .dontAnimate()
                .centerCrop();

        displayByOptions(resource, displayView, options);
    }

    /**
     * 使用iamgeView自带的{@link android.widget.ImageView.ScaleType}显示
     *
     * @param resource
     * @param displayView
     */
    public static void displayNormal(Object resource, ImageView displayView) {
        RequestOptions options = getOptions().clone()
                .dontAnimate();

        displayByOptions(resource, displayView, options);
    }

    /**
     * 显示带缩略图的图片
     *
     * @param resource
     * @param displayView
     */
    public static void displayThumbnail(Object resource, ImageView displayView) {
        RequestOptions options = getOptions().clone()
                .dontAnimate()
                .centerCrop();

        Glide.with(displayView)
                .asBitmap()
                .thumbnail(0.4F)
                .load(resource)
                .apply(options)
                .into(displayView);
    }

    /**
     * 显示指定尺寸的图片
     *
     * @param resource
     * @param displayView
     * @param resize
     */
    public static void displayResize(Object resource, ImageView displayView, int resize) {
        RequestOptions options = getOptions().clone()
                .dontAnimate()
                .dontAnimate()
                .override(resize);

        displayByOptions(resource, displayView, options);
    }

    /**
     * 自定义{@link RequestOptions}参数的图片展示
     *
     * @param resource
     * @param displayView
     * @param options
     */
    public static void displayByOptions(Object resource, ImageView displayView, RequestOptions options) {
        Glide.with(displayView)
                .asBitmap()
                .load(resource)
                .apply(options)
                .into(displayView);
    }

    /**
     * 显示gif图
     *
     * @param resource
     * @param displayView
     */
    public static void displayGif(Object resource, ImageView displayView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(DRAW_FAILURE)
                .placeholder(DRAW_DEFAULT);

        Glide.with(displayView)
                .asGif()
                .load(resource)
                .apply(options)
                .into(displayView);
    }

}
