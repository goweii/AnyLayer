package per.goweii.anylayer.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class AnimatorHelper {

    public static final float ZOOM_PERCENT = 0.9F;
    public static final float MOVE_PERCENT = 0.9F;
    public static final float INTERPOLATOR_FACTOR_1 = 1.5F;
    public static final float INTERPOLATOR_FACTOR_2 = 2.5F;

    @NonNull
    private static TimeInterpolator createDefInterpolator(float factor) {
        return new DecelerateInterpolator(factor);
    }

    @NonNull
    private static TimeInterpolator createDefInterpolator1() {
        return createDefInterpolator(INTERPOLATOR_FACTOR_1);
    }

    @NonNull
    private static TimeInterpolator createDefInterpolator2() {
        return createDefInterpolator(INTERPOLATOR_FACTOR_2);
    }

    // AlphaIn

    @NonNull
    public static Animator createAlphaInAnim(@NonNull final View target) {
        return createAlphaInAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createAlphaInAnim(@NonNull final View target,
                                             @Nullable TimeInterpolator alphaInterpolator) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        return alpha;
    }

    // AlphaOut

    @NonNull
    public static Animator createAlphaOutAnim(@NonNull final View target) {
        return createAlphaOutAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createAlphaOutAnim(@NonNull final View target,
                                              @Nullable TimeInterpolator alphaInterpolator) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        return alpha;
    }

    // ZoomIn

    @NonNull
    public static Animator createZoomInAnim(@NonNull final View target) {
        return createZoomInAnim(target, null);
    }

    @NonNull
    public static Animator createZoomInAnim(@NonNull final View target,
                                            @Nullable TimeInterpolator zoomInterpolator) {
        return createZoomInAnim(target, 0.5F, 0.5F, zoomInterpolator);
    }

    @NonNull
    public static Animator createZoomInAnim(@NonNull final View target,
                                            float centerPercentX,
                                            float centerPercentY) {
        return createZoomInAnim(target, centerPercentX, centerPercentY, createDefInterpolator1());
    }

    @NonNull
    public static Animator createZoomInAnim(@NonNull final View target,
                                            float centerPercentX,
                                            float centerPercentY,
                                            @Nullable TimeInterpolator zoomInterpolator) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createZoomInAnim(target, centerX, centerY, zoomInterpolator);
    }

    @NonNull
    public static Animator createZoomInAnim(@NonNull final View target,
                                            int centerX,
                                            int centerY) {
        return createZoomInAnim(target, centerX, centerY, null);
    }

    @NonNull
    public static Animator createZoomInAnim(@NonNull final View target,
                                            int centerX,
                                            int centerY,
                                            @Nullable TimeInterpolator zoomInterpolator) {
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0, 1);
        if (zoomInterpolator != null) {
            scaleX.setInterpolator(zoomInterpolator);
            scaleY.setInterpolator(zoomInterpolator);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        return set;
    }

    // ZoomOut

    @NonNull
    public static Animator createZoomOutAnim(@NonNull final View target) {
        return createZoomOutAnim(target, null);
    }

    @NonNull
    public static Animator createZoomOutAnim(@NonNull final View target,
                                             @Nullable TimeInterpolator zoomInterpolator) {
        return createZoomOutAnim(target, 0.5F, 0.5F, zoomInterpolator);
    }

    @NonNull
    public static Animator createZoomOutAnim(@NonNull final View target,
                                             float centerPercentX,
                                             float centerPercentY) {
        return createZoomOutAnim(target, centerPercentX, centerPercentY, createDefInterpolator1());
    }

    @NonNull
    public static Animator createZoomOutAnim(@NonNull final View target,
                                             float centerPercentX,
                                             float centerPercentY,
                                             @Nullable TimeInterpolator zoomInterpolator) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createZoomOutAnim(target, centerX, centerY, zoomInterpolator);
    }

    @NonNull
    public static Animator createZoomOutAnim(@NonNull final View target,
                                             int centerX,
                                             int centerY,
                                             @Nullable TimeInterpolator zoomInterpolator) {
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), 0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), 0);
        if (zoomInterpolator != null) {
            scaleX.setInterpolator(zoomInterpolator);
            scaleY.setInterpolator(zoomInterpolator);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        return set;
    }

    @NonNull
    public static Animator createZoomOutAnim(@NonNull final View target,
                                             int centerX,
                                             int centerY) {
        return createZoomOutAnim(target, centerX, centerY, null);
    }

    // ZoomAlphaIn

    @NonNull
    public static Animator createZoomAlphaInAnim(@NonNull final View target) {
        return createZoomAlphaInAnim(target, ZOOM_PERCENT);
    }

    @NonNull
    public static Animator createZoomAlphaInAnim(@NonNull final View target,
                                                 float fromScale) {
        return createZoomAlphaInAnim(target, 0.5F, 0.5F, fromScale);
    }

    @NonNull
    public static Animator createZoomAlphaInAnim(@NonNull final View target,
                                                 float centerPercentX,
                                                 float centerPercentY) {
        return createZoomAlphaInAnim(target, centerPercentX, centerPercentY, ZOOM_PERCENT);
    }

    @NonNull
    public static Animator createZoomAlphaInAnim(@NonNull final View target,
                                                 int centerX,
                                                 int centerY) {
        return createZoomAlphaInAnim(target, centerX, centerY, ZOOM_PERCENT);
    }

    @NonNull
    public static Animator createZoomAlphaInAnim(@NonNull final View target,
                                                 float centerPercentX,
                                                 float centerPercentY,
                                                 float fromScale) {
        return createZoomAlphaInAnim(target, centerPercentX, centerPercentY, fromScale,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createZoomAlphaInAnim(@NonNull final View target,
                                                 int centerX,
                                                 int centerY,
                                                 float fromScale) {
        return createZoomAlphaInAnim(target, centerX, centerY, fromScale,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createZoomAlphaInAnim(@NonNull final View target,
                                                 float centerPercentX,
                                                 float centerPercentY,
                                                 float fromScale,
                                                 @Nullable TimeInterpolator zoomInterpolator,
                                                 @Nullable TimeInterpolator alphaInterpolator) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createZoomAlphaInAnim(target, centerX, centerY, fromScale, zoomInterpolator, alphaInterpolator);
    }

    @NonNull
    public static Animator createZoomAlphaInAnim(@NonNull final View target,
                                                 int centerX,
                                                 int centerY,
                                                 float fromScale,
                                                 @Nullable TimeInterpolator zoomInterpolator,
                                                 @Nullable TimeInterpolator alphaInterpolator) {
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", fromScale, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", fromScale, 1);
        if (zoomInterpolator != null) {
            scaleX.setInterpolator(zoomInterpolator);
            scaleY.setInterpolator(zoomInterpolator);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        return set;
    }

    // ZoomAlphaOut

    @NonNull
    public static Animator createZoomAlphaOutAnim(@NonNull final View target) {
        return createZoomAlphaOutAnim(target, ZOOM_PERCENT);
    }

    @NonNull
    public static Animator createZoomAlphaOutAnim(@NonNull final View target,
                                                  float toScale) {
        return createZoomAlphaOutAnim(target, 0.5F, 0.5F, toScale);
    }

    @NonNull
    public static Animator createZoomAlphaOutAnim(@NonNull final View target,
                                                  float centerPercentX,
                                                  float centerPercentY) {
        return createZoomAlphaOutAnim(target, centerPercentX, centerPercentY, ZOOM_PERCENT);
    }

    @NonNull
    public static Animator createZoomAlphaOutAnim(@NonNull final View target,
                                                  int centerX,
                                                  int centerY) {
        return createZoomAlphaOutAnim(target, centerX, centerY, ZOOM_PERCENT);
    }

    @NonNull
    public static Animator createZoomAlphaOutAnim(@NonNull final View target,
                                                  float centerPercentX,
                                                  float centerPercentY,
                                                  float toScale) {
        return createZoomAlphaOutAnim(target, centerPercentX, centerPercentY, toScale,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createZoomAlphaOutAnim(@NonNull final View target,
                                                  int centerX,
                                                  int centerY,
                                                  float toScale) {
        return createZoomAlphaOutAnim(target, centerX, centerY, toScale,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createZoomAlphaOutAnim(@NonNull final View target,
                                                  float centerPercentX,
                                                  float centerPercentY,
                                                  float toScale,
                                                  @Nullable TimeInterpolator zoomInterpolator,
                                                  @Nullable TimeInterpolator alphaInterpolator) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createZoomAlphaOutAnim(target, centerX, centerY, toScale, zoomInterpolator, alphaInterpolator);
    }

    @NonNull
    public static Animator createZoomAlphaOutAnim(@NonNull final View target,
                                                  int centerX,
                                                  int centerY,
                                                  float toScale,
                                                  @Nullable TimeInterpolator zoomInterpolator,
                                                  @Nullable TimeInterpolator alphaInterpolator) {
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), toScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), toScale);
        if (zoomInterpolator != null) {
            scaleX.setInterpolator(zoomInterpolator);
            scaleY.setInterpolator(zoomInterpolator);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        return set;
    }

    // TopIn

    @NonNull
    public static Animator createTopInAnim(@NonNull final View target) {
        return createTopInAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createTopInAnim(@NonNull final View target,
                                           @Nullable TimeInterpolator topInterpolator) {
        ObjectAnimator top = ObjectAnimator.ofFloat(target, "translationY", -target.getBottom(), 0);
        if (topInterpolator != null) top.setInterpolator(topInterpolator);
        return top;
    }

    // TopOut

    @NonNull
    public static Animator createTopOutAnim(@NonNull final View target) {
        return createTopOutAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createTopOutAnim(@NonNull final View target, TimeInterpolator topInterpolator) {
        ObjectAnimator top = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), -target.getBottom());
        if (topInterpolator != null) top.setInterpolator(topInterpolator);
        return top;
    }

    // TopAlphaIn

    @NonNull
    public static Animator createTopAlphaInAnim(@NonNull final View target) {
        return createTopAlphaInAnim(target, 1 - MOVE_PERCENT);
    }

    @NonNull
    public static Animator createTopAlphaInAnim(@NonNull final View target,
                                                float percentTargetHeight) {
        return createTopAlphaInAnim(target, percentTargetHeight,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createTopAlphaInAnim(@NonNull final View target,
                                                float percentTargetHeight,
                                                @Nullable TimeInterpolator yInterpolator,
                                                @Nullable TimeInterpolator alphaInterpolator) {
        float y = percentTargetHeight * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", -y, 0);
        if (yInterpolator != null) translationY.setInterpolator(yInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        return set;
    }

    // TopAlphaOut

    @NonNull
    public static Animator createTopAlphaOutAnim(@NonNull final View target) {
        return createTopAlphaOutAnim(target, 1 - MOVE_PERCENT);
    }

    @NonNull
    public static Animator createTopAlphaOutAnim(@NonNull final View target,
                                                 float percentTargetHeight) {
        return createTopAlphaOutAnim(target, percentTargetHeight,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createTopAlphaOutAnim(@NonNull final View target,
                                                 float percentTargetHeight,
                                                 @Nullable TimeInterpolator yInterpolator,
                                                 @Nullable TimeInterpolator alphaInterpolator) {
        float y = percentTargetHeight * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), -y);
        if (yInterpolator != null) translationY.setInterpolator(yInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        return set;
    }

    // BottomIn

    @NonNull
    public static Animator createBottomInAnim(@NonNull final View target) {
        return createBottomInAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createBottomInAnim(@NonNull final View target,
                                              @Nullable TimeInterpolator yInterpolator) {
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", y, 0);
        if (yInterpolator != null) translationY.setInterpolator(yInterpolator);
        return translationY;
    }

    // BottomAlphaIn

    @NonNull
    public static Animator createBottomAlphaInAnim(@NonNull final View target) {
        return createBottomAlphaInAnim(target, 1 - MOVE_PERCENT);
    }

    @NonNull
    public static Animator createBottomAlphaInAnim(@NonNull final View target,
                                                   float percentTargetHeight) {
        return createBottomAlphaInAnim(target, percentTargetHeight,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createBottomAlphaInAnim(@NonNull final View target,
                                                   float percentTargetHeight,
                                                   @Nullable TimeInterpolator yInterpolator,
                                                   @Nullable TimeInterpolator alphaInterpolator) {
        float y = percentTargetHeight * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", y, 0);
        if (yInterpolator != null) translationY.setInterpolator(yInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        return set;
    }

    // BottomOut

    @NonNull
    public static Animator createBottomOutAnim(@NonNull final View target) {
        return createBottomOutAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createBottomOutAnim(@NonNull final View target,
                                               @Nullable TimeInterpolator yInterpolator) {
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), y);
        if (yInterpolator != null) translationY.setInterpolator(yInterpolator);
        return translationY;
    }

    // BottomAlphaOut

    @NonNull
    public static Animator createBottomAlphaOutAnim(@NonNull final View target) {
        return createBottomAlphaOutAnim(target, 1 - MOVE_PERCENT);
    }

    @NonNull
    public static Animator createBottomAlphaOutAnim(@NonNull final View target,
                                                    float percentTargetHeight) {
        return createBottomAlphaOutAnim(target, percentTargetHeight,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createBottomAlphaOutAnim(@NonNull final View target,
                                                    float percentTargetHeight,
                                                    @Nullable TimeInterpolator yInterpolator,
                                                    @Nullable TimeInterpolator alphaInterpolator) {
        float y = percentTargetHeight * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), y);
        if (yInterpolator != null) translationY.setInterpolator(yInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        return set;
    }

    // LeftIn

    @NonNull
    public static Animator createLeftInAnim(@NonNull final View target) {
        return createLeftInAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createLeftInAnim(@NonNull final View target,
                                            @Nullable TimeInterpolator xInterpolator) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -target.getRight(), 0);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        return translationX;
    }

    // LeftOut

    @NonNull
    public static Animator createLeftOutAnim(@NonNull final View target) {
        return createLeftOutAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createLeftOutAnim(@NonNull final View target,
                                             @Nullable TimeInterpolator xInterpolator) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), -target.getRight());
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        return translationX;
    }

    // LeftAlphaIn

    @NonNull
    public static Animator createLeftAlphaInAnim(@NonNull final View target) {
        return createLeftAlphaInAnim(target, 1 - MOVE_PERCENT);
    }

    @NonNull
    public static Animator createLeftAlphaInAnim(@NonNull final View target,
                                                 float percentTargetWidth) {
        return createLeftAlphaInAnim(target, percentTargetWidth,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createLeftAlphaInAnim(@NonNull final View target,
                                                 float percentTargetWidth,
                                                 @Nullable TimeInterpolator xInterpolator,
                                                 @Nullable TimeInterpolator alphaInterpolator) {
        float x = percentTargetWidth * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -x, 0);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        return set;
    }

    // LeftAlphaOut

    @NonNull
    public static Animator createLeftAlphaOutAnim(@NonNull final View target) {
        return createLeftAlphaOutAnim(target, 1 - MOVE_PERCENT);
    }

    @NonNull
    public static Animator createLeftAlphaOutAnim(@NonNull final View target,
                                                  float percentTargetWidth) {
        return createLeftAlphaOutAnim(target, percentTargetWidth,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createLeftAlphaOutAnim(@NonNull final View target,
                                                  float percentTargetWidth,
                                                  @Nullable TimeInterpolator xInterpolator,
                                                  @Nullable TimeInterpolator alphaInterpolator) {
        float x = percentTargetWidth * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), -x);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        return set;
    }

    // RightIn

    @NonNull
    public static Animator createRightInAnim(@NonNull final View target) {
        return createRightInAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createRightInAnim(@NonNull final View target,
                                             @Nullable TimeInterpolator xInterpolator) {
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", x, 0);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        return translationX;
    }

    // RightOut

    @NonNull
    public static Animator createRightOutAnim(@NonNull final View target) {
        return createRightOutAnim(target, createDefInterpolator1());
    }

    @NonNull
    public static Animator createRightOutAnim(@NonNull final View target,
                                              @Nullable TimeInterpolator xInterpolator) {
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), x);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        return translationX;
    }

    // RightAlphaIn

    @NonNull
    public static Animator createRightAlphaInAnim(@NonNull final View target) {
        return createRightAlphaInAnim(target, 1 - MOVE_PERCENT);
    }

    @NonNull
    public static Animator createRightAlphaInAnim(@NonNull final View target,
                                                  float percentTargetWidth) {
        return createRightAlphaInAnim(target, percentTargetWidth,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createRightAlphaInAnim(@NonNull final View target,
                                                  float percentTargetWidth,
                                                  @Nullable TimeInterpolator xInterpolator,
                                                  @Nullable TimeInterpolator alphaInterpolator) {
        float x = percentTargetWidth * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", x, 0);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        return set;
    }

    // RightAlphaOut

    @NonNull
    public static Animator createRightAlphaOutAnim(@NonNull final View target) {
        return createRightAlphaOutAnim(target, 1 - MOVE_PERCENT);
    }

    @NonNull
    public static Animator createRightAlphaOutAnim(@NonNull final View target,
                                                   float percentTargetWidth) {
        return createRightAlphaOutAnim(target, percentTargetWidth,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createRightAlphaOutAnim(@NonNull final View target,
                                                   float percentTargetWidth,
                                                   @Nullable TimeInterpolator xInterpolator,
                                                   @Nullable TimeInterpolator alphaInterpolator) {
        float x = percentTargetWidth * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), x);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        return set;
    }

    // CircularRevealIn

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealInAnim(@NonNull final View target) {
        return createCircularRevealInAnim(target, 0.5F, 0.5F);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealInAnim(@NonNull final View target,
                                                      float centerPercentX,
                                                      float centerPercentY) {
        return createCircularRevealInAnim(target, centerPercentX, centerPercentY, createDefInterpolator1());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealInAnim(@NonNull final View target,
                                                      int centerX,
                                                      int centerY) {
        return createCircularRevealInAnim(target, centerX, centerY, createDefInterpolator1());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealInAnim(@NonNull final View target,
                                                      float centerPercentX,
                                                      float centerPercentY,
                                                      @Nullable TimeInterpolator timeInterpolator) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createCircularRevealInAnim(target, centerX, centerY, timeInterpolator);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealInAnim(@NonNull final View target,
                                                      int centerX,
                                                      int centerY,
                                                      @Nullable TimeInterpolator timeInterpolator) {
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, 0, r);
        if (timeInterpolator != null) animator.setInterpolator(timeInterpolator);
        return animator;
    }

    // CircularRevealOut

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealOutAnim(@NonNull final View target) {
        return createCircularRevealOutAnim(target, 0.5F, 0.5F);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealOutAnim(@NonNull final View target,
                                                       float centerPercentX,
                                                       float centerPercentY) {
        return createCircularRevealOutAnim(target, centerPercentX, centerPercentY, createDefInterpolator1());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealOutAnim(@NonNull final View target,
                                                       int centerX,
                                                       int centerY) {
        return createCircularRevealOutAnim(target, centerX, centerY, createDefInterpolator1());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealOutAnim(@NonNull final View target,
                                                       float centerPercentX,
                                                       float centerPercentY,
                                                       @Nullable TimeInterpolator timeInterpolator) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createCircularRevealOutAnim(target, centerX, centerY, timeInterpolator);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static Animator createCircularRevealOutAnim(@NonNull final View target,
                                                       int centerX,
                                                       int centerY,
                                                       @Nullable TimeInterpolator timeInterpolator) {
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, r, 0);
        if (timeInterpolator != null) animator.setInterpolator(timeInterpolator);
        return animator;
    }

    // DelayedZoomIn

    @NonNull
    public static Animator createDelayedZoomInAnim(@NonNull final View target) {
        return createDelayedZoomInAnim(target, 0.5F, 0.5F);
    }

    @NonNull
    public static Animator createDelayedZoomInAnim(@NonNull final View target,
                                                   int centerX,
                                                   int centerY) {
        return createDelayedZoomInAnim(target, centerX, centerY,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createDelayedZoomInAnim(@NonNull final View target,
                                                   float centerPercentX,
                                                   float centerPercentY) {
        return createDelayedZoomInAnim(target, centerPercentX, centerPercentY,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createDelayedZoomInAnim(@NonNull final View target,
                                                   float centerPercentX,
                                                   float centerPercentY,
                                                   @Nullable TimeInterpolator zoomInterpolator,
                                                   @Nullable TimeInterpolator alphaInterpolator) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createDelayedZoomInAnim(target, centerX, centerY, zoomInterpolator, alphaInterpolator);
    }

    @NonNull
    public static Animator createDelayedZoomInAnim(@NonNull final View target,
                                                   int centerX,
                                                   int centerY,
                                                   @Nullable TimeInterpolator zoomInterpolator,
                                                   @Nullable final TimeInterpolator alphaInterpolator) {
        if (!(target instanceof ViewGroup)) {
            return createZoomInAnim(target, centerX, centerY);
        }
        final ViewGroup targetGroup = (ViewGroup) target;
        for (int i = 0; i < targetGroup.getChildCount(); i++) {
            View targetChild = targetGroup.getChildAt(i);
            targetChild.setAlpha(0);
        }
        targetGroup.setPivotX(centerX);
        targetGroup.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(targetGroup, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(targetGroup, "scaleY", 0, 1);
        if (zoomInterpolator != null) {
            scaleX.setInterpolator(zoomInterpolator);
            scaleY.setInterpolator(zoomInterpolator);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        scaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private boolean isChildAnimStart = false;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = animation.getAnimatedFraction();
                if (!isChildAnimStart && f > 0.618F) {
                    isChildAnimStart = true;
                    final List<Animator> childAnimators = new ArrayList<>(targetGroup.getChildCount());
                    for (int i = 0; i < targetGroup.getChildCount(); i++) {
                        View targetChild = targetGroup.getChildAt(i);
                        ObjectAnimator alphaChild = ObjectAnimator.ofFloat(targetChild, "alpha", 0, 1);
                        if (alphaInterpolator != null)
                            alphaChild.setInterpolator(alphaInterpolator);
                        alphaChild.setStartDelay(18 * i);
                        alphaChild.setDuration(50);
                        childAnimators.add(alphaChild);
                    }
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(childAnimators);
                    set.start();
                }
            }
        });
        return set;
    }

    // DelayedZoomOut

    @NonNull
    public static Animator createDelayedZoomOutAnim(@NonNull final View target) {
        return createDelayedZoomOutAnim(target, 0.5F, 0.5F);
    }

    @NonNull
    public static Animator createDelayedZoomOutAnim(@NonNull final View target,
                                                    int centerX,
                                                    int centerY) {
        return createDelayedZoomOutAnim(target, centerX, centerY,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createDelayedZoomOutAnim(@NonNull final View target,
                                                    float centerPercentX,
                                                    float centerPercentY) {
        return createDelayedZoomOutAnim(target, centerPercentX, centerPercentY,
                createDefInterpolator1(), createDefInterpolator2());
    }

    @NonNull
    public static Animator createDelayedZoomOutAnim(@NonNull final View target,
                                                    float centerPercentX,
                                                    float centerPercentY,
                                                    @Nullable TimeInterpolator zoomInterpolator,
                                                    @Nullable TimeInterpolator alphaInterpolator) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createDelayedZoomOutAnim(target, centerX, centerY, zoomInterpolator, alphaInterpolator);
    }

    @NonNull
    public static Animator createDelayedZoomOutAnim(@NonNull final View target,
                                                    int centerX,
                                                    int centerY,
                                                    @Nullable TimeInterpolator zoomInterpolator,
                                                    @Nullable final TimeInterpolator alphaInterpolator) {
        if (!(target instanceof ViewGroup)) {
            return createZoomInAnim(target, centerX, centerY);
        }
        final ViewGroup targetGroup = (ViewGroup) target;
        targetGroup.setPivotX(centerX);
        targetGroup.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(targetGroup, "scaleX", targetGroup.getScaleX(), 0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(targetGroup, "scaleY", targetGroup.getScaleY(), 0);
        if (zoomInterpolator != null) {
            scaleX.setInterpolator(zoomInterpolator);
            scaleY.setInterpolator(zoomInterpolator);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        scaleX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private boolean isChildAnimStart = false;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!isChildAnimStart) {
                    isChildAnimStart = true;
                    final List<Animator> childAnimators = new ArrayList<>(targetGroup.getChildCount());
                    for (int i = targetGroup.getChildCount() - 1; i >= 0; i--) {
                        View targetChild = targetGroup.getChildAt(i);
                        ObjectAnimator alphaChild = ObjectAnimator.ofFloat(targetChild, "alpha", targetChild.getAlpha(), 0);
                        if (alphaInterpolator != null)
                            alphaChild.setInterpolator(alphaInterpolator);
                        alphaChild.setStartDelay(18 * (targetGroup.getChildCount() - 1 - i));
                        alphaChild.setDuration(50);
                        childAnimators.add(alphaChild);
                    }
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(childAnimators);
                    set.start();
                }
            }
        });
        return set;
    }
}
