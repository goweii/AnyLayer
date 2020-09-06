package per.goweii.anylayer;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

/**
 * @author CuiZhen
 * @date 2020/8/16
 */
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
    public String notificationLabel = null;
    public Drawable notificationIcon = null;
    public String notificationTimePattern = null;
}
