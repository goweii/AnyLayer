package per.goweii.anylayer;

import android.view.Gravity;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @IntRange(from = 0)
    public long dialogAnimDuration = 220L;
    @FloatRange(from = 0F, to = 1F)
    public float dialogDimAmount = 0.6F;
    @Nullable
    public Layer.AnimatorCreator dialogBackgroundAnimatorCreator = null;
    @Nullable
    public Layer.AnimatorCreator dialogContentAnimatorCreator = null;

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
    public int marginLeft = Integer.MIN_VALUE;
    public int marginTop = Integer.MIN_VALUE;
    public int marginRight = Integer.MIN_VALUE;
    public int marginBottom = Integer.MIN_VALUE;
}
