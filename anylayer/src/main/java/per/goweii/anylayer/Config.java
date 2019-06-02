package per.goweii.anylayer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.view.Gravity;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
final class Config {
    boolean mOutsideInterceptTouchEvent = true;

    boolean mCancelableOnTouchOutside = true;
    boolean mCancelableOnClickKeyBack = true;

    @IdRes
    int mAsStatusBarViewId = 0;

    int mGravity = Gravity.CENTER;
    float mBackgroundBlurPercent = 0;
    float mBackgroundBlurRadius = 0;
    float mBackgroundBlurScale = 2;
    Bitmap mBackgroundBitmap = null;
    int mBackgroundResource = -1;
    Drawable mBackgroundDrawable = null;
    @ColorInt
    int mBackgroundColor = Color.TRANSPARENT;

    boolean mAlignmentInside = false;
    Alignment.Direction mAlignmentDirection = Alignment.Direction.VERTICAL;
    Alignment.Horizontal mAlignmentHorizontal = Alignment.Horizontal.CENTER;
    Alignment.Vertical mAlignmentVertical = Alignment.Vertical.BELOW;
}
