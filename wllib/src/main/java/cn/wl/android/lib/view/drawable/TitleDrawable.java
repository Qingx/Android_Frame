package cn.wl.android.lib.view.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ConvertUtils;

import cn.wl.android.lib.R;

/**
 * Created by JustBlue on 2019-09-05.
 *
 * @email: bo.li@cdxzhi.com
 * @desc:
 */
public class TitleDrawable extends Drawable {

    private final Paint mPaint;

    private int mRadius;
    private Rect mDrawRect = new Rect();

    private Point c1Point = new Point();
    private Point c2Point = new Point();

    private static final int BG_COLOR = ColorUtils.getColor(R.color.md_blue_500);

    public TitleDrawable() {
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setColor(ColorUtils.getColor(R.color.white4));
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);

        mDrawRect.set(bounds);
        initCircleData(mDrawRect);
    }

    /**
     * 初始化圆的参数
     *
     * @param rect
     */
    private void initCircleData(Rect rect) {
        int width = rect.width();
        int radius = width / 4;

        int offset = ConvertUtils.dp2px(8);

        c1Point.x = width * 5 / 8 - offset;
        c1Point.y = rect.centerY() + offset;

        c2Point.x = width * 7 / 8 - offset;
        c2Point.y = rect.centerY() + radius - offset;

        mRadius = radius;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawColor(BG_COLOR);

        canvas.drawCircle(c1Point.x, c1Point.y, mRadius, mPaint);
        canvas.drawCircle(c2Point.x, c2Point.y, mRadius, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
