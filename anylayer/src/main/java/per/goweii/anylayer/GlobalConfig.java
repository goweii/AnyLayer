package per.goweii.anylayer;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import per.goweii.anylayer.overlay.OverlayLayer;

public class GlobalConfig {
    private static final GlobalConfig INSTANCE = new GlobalConfig();

    @NonNull
    public static GlobalConfig get() {
        return INSTANCE;
    }

    // DialogLayer

    @IntRange(from = 0)
    public long dialogAnimDuration = 220L;
    @FloatRange(from = 0F, to = 1F)
    public float dialogDimAmount = 0.6F;
    @Nullable
    public Layer.AnimatorCreator dialogBackgroundAnimatorCreator = null;
    @Nullable
    public Layer.AnimatorCreator dialogContentAnimatorCreator = null;

    // ToastLayer

    @Nullable
    public Layer.AnimatorCreator toastAnimatorCreator = null;
    @IntRange(from = 0)
    public long toastAnimDuration = 220L;
    @IntRange(from = 0)
    public long toastDuration = 3000L;
    @DrawableRes
    public int toastBackgroundRes = -1;
    @ColorInt
    public int toastTextColorInt = Color.TRANSPARENT;
    @ColorRes
    public int toastTextColorRes = -1;
    @FloatRange(from = 0F, to = 1F)
    public float toastAlpha = 1;
    public int toastGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    public int toastMarginLeft = Integer.MIN_VALUE;
    public int toastMarginTop = Integer.MIN_VALUE;
    public int toastMarginRight = Integer.MIN_VALUE;
    public int toastMarginBottom = Integer.MIN_VALUE;

    // GuideLayer

    @ColorInt
    public int guideBackgroundInt = ColorUtils.setAlphaComponent(Color.BLACK, (int) (255 * 0.5));

    // NotificationLayer

    public long notificationDuration = 5000L;
    public int notificationMaxWidth = -1;
    public int notificationMaxHeight = -1;
    public CharSequence notificationLabel = null;
    public Drawable notificationIcon = null;
    public String notificationTimePattern = null;

    // OverlayLayer

    public boolean overlayOutside = true;
    public int overlaySnapEdge = OverlayLayer.Edge.HORIZONTAL;
    @FloatRange(from = -2F, to = 2F)
    public float overlayDefPercentX = 2F;
    @FloatRange(from = -2F, to = 2F)
    public float overlayDefPercentY = 0.236F;
    @FloatRange(from = 0F, to = 1F)
    public float overlayDefAlpha = 1F;
    public float overlayDefScale = 1F;
    public float overlayPivotX = 0.5F;
    public float overlayPivotY = 0.5F;
    @FloatRange(from = 0F, to = 1F)
    public float overlayNormalAlpha = 1F;
    public float overlayNormalScale = 1F;
    @IntRange(from = 0)
    public long overlayLowProfileDelay = 3000L;
    @FloatRange(from = 0F, to = 1F)
    public float overlayLowProfileAlpha = 0.8F;
    public float overlayLowProfileScale = 1F;
    @FloatRange(from = 0F, to = 1F)
    public float overlayLowProfileIndent = 0F;
    public int overlayMarginLeft = Integer.MIN_VALUE;
    public int overlayMarginTop = Integer.MIN_VALUE;
    public int overlayMarginRight = Integer.MIN_VALUE;
    public int overlayMarginBottom = Integer.MIN_VALUE;
    public int overlayPaddingLeft = 0;
    public int overlayPaddingTop = 0;
    public int overlayPaddingRight = 0;
    public int overlayPaddingBottom = 0;
}
