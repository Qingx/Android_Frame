package cn.wl.android.lib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhihu.matisse.engine.ImageEngine;

/**
 * Created by JustBlue on 2019-08-31.
 *
 * @email: bo.li@cdxzhi.com
 * @desc: {@link com.zhihu.matisse.Matisse}图片展示适配
 */
public class GlideEngine implements ImageEngine {

    @Override
    public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        imageView.setScaleType(
                ImageView.ScaleType.CENTER_CROP);
        GlideApp.displayThumbnail(uri, imageView);
    }

    @Override
    public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
        imageView.setScaleType(
                ImageView.ScaleType.CENTER_CROP);
        GlideApp.displayGif(uri, imageView);
    }

    @Override
    public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        imageView.setScaleType(
                ImageView.ScaleType.CENTER_CROP);
        GlideApp.display(uri, imageView);
    }

    @Override
    public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
        imageView.setScaleType(
                ImageView.ScaleType.CENTER_CROP);
        GlideApp.displayGif(uri, imageView);
    }

    @Override
    public boolean supportAnimatedGif() {
        return false;
    }

}
