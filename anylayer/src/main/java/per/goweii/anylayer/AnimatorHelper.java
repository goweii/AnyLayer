package per.goweii.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cuizhen
 * @date 2018/5/20
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class AnimatorHelper {

    private static final float ZOOM_PERCENT = 0.9F;
    private static final float MOVE_PERCENT = 0.9F;
    private static final float INTERPOLATOR_FACTOR_1 = 1.5F;
    private static final float INTERPOLATOR_FACTOR_2 = 2.5F;

    private static TimeInterpolator createDefInterpolator(float factor) {
        return new DecelerateInterpolator(factor);
    }

    private static TimeInterpolator createDefInterpolator1() {
        return createDefInterpolator(INTERPOLATOR_FACTOR_1);
    }

    private static TimeInterpolator createDefInterpolator2() {
        return createDefInterpolator(INTERPOLATOR_FACTOR_2);
    }

    // AlphaIn

    public static Animator createAlphaInAnim(final View target) {
        return createAlphaInAnim(target, createDefInterpolator1());
    }

    public static Animator createAlphaInAnim(final View target,
                                             TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        return alpha;
    }

    // AlphaOut

    public static Animator createAlphaOutAnim(final View target) {
        return createAlphaOutAnim(target, createDefInterpolator1());
    }

    public static Animator createAlphaOutAnim(final View target,
                                              TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        if (alphaInterpolator != null) alpha.setInterpolator(alphaInterpolator);
        return alpha;
    }

    // ZoomIn

    public static Animator createZoomInAnim(final View target) {
        return createZoomInAnim(target, 0.5F, 0.5F);
    }

    public static Animator createZoomInAnim(final View target,
                                            float centerPercentX,
                                            float centerPercentY) {
        return createZoomInAnim(target, centerPercentX, centerPercentY, createDefInterpolator1());
    }

    public static Animator createZoomInAnim(final View target,
                                            float centerPercentX,
                                            float centerPercentY,
                                            TimeInterpolator zoomInterpolator) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomInAnim(target, centerX, centerY, zoomInterpolator);
    }

    public static Animator createZoomInAnim(final View target,
                                            int centerX,
                                            int centerY,
                                            TimeInterpolator zoomInterpolator) {
        if (target == null) return null;
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

    public static Animator createZoomOutAnim(final View target) {
        return createZoomOutAnim(target, 0.5F, 0.5F);
    }

    public static Animator createZoomOutAnim(final View target,
                                             float centerPercentX,
                                             float centerPercentY) {
        return createZoomOutAnim(target, centerPercentX, centerPercentY, createDefInterpolator1());
    }

    public static Animator createZoomOutAnim(final View target,
                                             float centerPercentX,
                                             float centerPercentY,
                                             TimeInterpolator zoomInterpolator) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomOutAnim(target, centerX, centerY, zoomInterpolator);
    }

    public static Animator createZoomOutAnim(final View target,
                                             int centerX,
                                             int centerY,
                                             TimeInterpolator zoomInterpolator) {
        if (target == null) return null;
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

    // ZoomAlphaIn

    public static Animator createZoomAlphaInAnim(final View target) {
        return createZoomAlphaInAnim(target, ZOOM_PERCENT);
    }

    public static Animator createZoomAlphaInAnim(final View target,
                                                 float fromScale) {
        return createZoomAlphaInAnim(target, 0.5F, 0.5F, fromScale);
    }

    public static Animator createZoomAlphaInAnim(final View target,
                                                 float centerPercentX,
                                                 float centerPercentY) {
        return createZoomAlphaInAnim(target, centerPercentX, centerPercentY, ZOOM_PERCENT);
    }

    public static Animator createZoomAlphaInAnim(final View target,
                                                 int centerX,
                                                 int centerY) {
        return createZoomAlphaInAnim(target, centerX, centerY, ZOOM_PERCENT);
    }

    public static Animator createZoomAlphaInAnim(final View target,
                                                 float centerPercentX,
                                                 float centerPercentY,
                                                 float fromScale) {
        return createZoomAlphaInAnim(target, centerPercentX, centerPercentY, fromScale,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createZoomAlphaInAnim(final View target,
                                                 int centerX,
                                                 int centerY,
                                                 float fromScale) {
        return createZoomAlphaInAnim(target, centerX, centerY, fromScale,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createZoomAlphaInAnim(final View target,
                                                 float centerPercentX,
                                                 float centerPercentY,
                                                 float fromScale,
                                                 TimeInterpolator zoomInterpolator,
                                                 TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomAlphaInAnim(target, centerX, centerY, fromScale, zoomInterpolator, alphaInterpolator);
    }

    public static Animator createZoomAlphaInAnim(final View target,
                                                 int centerX,
                                                 int centerY,
                                                 float fromScale,
                                                 TimeInterpolator zoomInterpolator,
                                                 TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createZoomAlphaOutAnim(final View target) {
        return createZoomAlphaOutAnim(target, ZOOM_PERCENT);
    }

    public static Animator createZoomAlphaOutAnim(final View target,
                                                  float toScale) {
        return createZoomAlphaOutAnim(target, 0.5F, 0.5F, toScale);
    }

    public static Animator createZoomAlphaOutAnim(final View target,
                                                  float centerPercentX,
                                                  float centerPercentY) {
        return createZoomAlphaOutAnim(target, centerPercentX, centerPercentY, ZOOM_PERCENT);
    }

    public static Animator createZoomAlphaOutAnim(final View target,
                                                  int centerX,
                                                  int centerY) {
        return createZoomAlphaOutAnim(target, centerX, centerY, ZOOM_PERCENT);
    }

    public static Animator createZoomAlphaOutAnim(final View target,
                                                  float centerPercentX,
                                                  float centerPercentY,
                                                  float toScale) {
        return createZoomAlphaOutAnim(target, centerPercentX, centerPercentY, toScale,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createZoomAlphaOutAnim(final View target,
                                                  int centerX,
                                                  int centerY,
                                                  float toScale) {
        return createZoomAlphaOutAnim(target, centerX, centerY, toScale,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createZoomAlphaOutAnim(final View target,
                                                  float centerPercentX,
                                                  float centerPercentY,
                                                  float toScale,
                                                  TimeInterpolator zoomInterpolator,
                                                  TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomAlphaOutAnim(target, centerX, centerY, toScale, zoomInterpolator, alphaInterpolator);
    }

    public static Animator createZoomAlphaOutAnim(final View target,
                                                  int centerX,
                                                  int centerY,
                                                  float toScale,
                                                  TimeInterpolator zoomInterpolator,
                                                  TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createTopInAnim(final View target) {
        return createTopInAnim(target, createDefInterpolator1());
    }

    public static Animator createTopInAnim(final View target,
                                           TimeInterpolator topInterpolator) {
        if (target == null) return null;
        ObjectAnimator top = ObjectAnimator.ofFloat(target, "translationY", -target.getBottom(), 0);
        if (topInterpolator != null) top.setInterpolator(topInterpolator);
        return top;
    }

    // TopOut

    public static Animator createTopOutAnim(final View target) {
        return createTopOutAnim(target, createDefInterpolator1());
    }

    public static Animator createTopOutAnim(final View target, TimeInterpolator topInterpolator) {
        if (target == null) return null;
        ObjectAnimator top = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), -target.getBottom());
        if (topInterpolator != null) top.setInterpolator(topInterpolator);
        return top;
    }

    // TopAlphaIn

    public static Animator createTopAlphaInAnim(final View target) {
        return createTopAlphaInAnim(target, 1 - MOVE_PERCENT);
    }

    public static Animator createTopAlphaInAnim(final View target,
                                                float percentTargetHeight) {
        return createTopAlphaInAnim(target, percentTargetHeight,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createTopAlphaInAnim(final View target,
                                                float percentTargetHeight,
                                                TimeInterpolator yInterpolator,
                                                TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createTopAlphaOutAnim(final View target) {
        return createTopAlphaOutAnim(target, 1 - MOVE_PERCENT);
    }

    public static Animator createTopAlphaOutAnim(final View target,
                                                 float percentTargetHeight) {
        return createTopAlphaOutAnim(target, percentTargetHeight,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createTopAlphaOutAnim(final View target,
                                                 float percentTargetHeight,
                                                 TimeInterpolator yInterpolator,
                                                 TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createBottomInAnim(final View target) {
        return createBottomInAnim(target, createDefInterpolator1());
    }

    public static Animator createBottomInAnim(final View target,
                                              TimeInterpolator yInterpolator) {
        if (target == null) return null;
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", y, 0);
        if (yInterpolator != null) translationY.setInterpolator(yInterpolator);
        return translationY;
    }

    // BottomAlphaIn

    public static Animator createBottomAlphaInAnim(final View target) {
        return createBottomAlphaInAnim(target, 1 - MOVE_PERCENT);
    }

    public static Animator createBottomAlphaInAnim(final View target,
                                                   float percentTargetHeight) {
        return createBottomAlphaInAnim(target, percentTargetHeight,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createBottomAlphaInAnim(final View target,
                                                   float percentTargetHeight,
                                                   TimeInterpolator yInterpolator,
                                                   TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createBottomOutAnim(final View target) {
        return createBottomOutAnim(target, createDefInterpolator1());
    }

    public static Animator createBottomOutAnim(final View target,
                                               TimeInterpolator yInterpolator) {
        if (target == null) return null;
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), y);
        if (yInterpolator != null) translationY.setInterpolator(yInterpolator);
        return translationY;
    }

    // BottomAlphaOut

    public static Animator createBottomAlphaOutAnim(final View target) {
        return createBottomAlphaOutAnim(target, 1 - MOVE_PERCENT);
    }

    public static Animator createBottomAlphaOutAnim(final View target,
                                                    float percentTargetHeight) {
        return createBottomAlphaOutAnim(target, percentTargetHeight,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createBottomAlphaOutAnim(final View target,
                                                    float percentTargetHeight,
                                                    TimeInterpolator yInterpolator,
                                                    TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createLeftInAnim(final View target) {
        return createLeftInAnim(target, createDefInterpolator1());
    }

    public static Animator createLeftInAnim(final View target,
                                            TimeInterpolator xInterpolator) {
        if (target == null) return null;
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -target.getRight(), 0);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        return translationX;
    }

    // LeftOut

    public static Animator createLeftOutAnim(final View target) {
        return createLeftOutAnim(target, createDefInterpolator1());
    }

    public static Animator createLeftOutAnim(final View target,
                                             TimeInterpolator xInterpolator) {
        if (target == null) return null;
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), -target.getRight());
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        return translationX;
    }

    // LeftAlphaIn

    public static Animator createLeftAlphaInAnim(final View target) {
        return createLeftAlphaInAnim(target, 1 - MOVE_PERCENT);
    }

    public static Animator createLeftAlphaInAnim(final View target,
                                                 float percentTargetWidth) {
        return createLeftAlphaInAnim(target, percentTargetWidth,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createLeftAlphaInAnim(final View target,
                                                 float percentTargetWidth,
                                                 TimeInterpolator xInterpolator,
                                                 TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createLeftAlphaOutAnim(final View target) {
        return createLeftAlphaOutAnim(target, 1 - MOVE_PERCENT);
    }

    public static Animator createLeftAlphaOutAnim(final View target,
                                                  float percentTargetWidth) {
        return createLeftAlphaOutAnim(target, percentTargetWidth,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createLeftAlphaOutAnim(final View target,
                                                  float percentTargetWidth,
                                                  TimeInterpolator xInterpolator,
                                                  TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createRightInAnim(final View target) {
        return createRightInAnim(target, createDefInterpolator1());
    }

    public static Animator createRightInAnim(final View target,
                                             TimeInterpolator xInterpolator) {
        if (target == null) return null;
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", x, 0);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        return translationX;
    }

    // RightOut

    public static Animator createRightOutAnim(final View target) {
        return createRightOutAnim(target, createDefInterpolator1());
    }

    public static Animator createRightOutAnim(final View target,
                                              TimeInterpolator xInterpolator) {
        if (target == null) return null;
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), x);
        if (xInterpolator != null) translationX.setInterpolator(xInterpolator);
        return translationX;
    }

    // RightAlphaIn

    public static Animator createRightAlphaInAnim(final View target) {
        return createRightAlphaInAnim(target, 1 - MOVE_PERCENT);
    }

    public static Animator createRightAlphaInAnim(final View target,
                                                  float percentTargetWidth) {
        return createRightAlphaInAnim(target, percentTargetWidth,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createRightAlphaInAnim(final View target,
                                                  float percentTargetWidth,
                                                  TimeInterpolator xInterpolator,
                                                  TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createRightAlphaOutAnim(final View target) {
        return createRightAlphaOutAnim(target, 1 - MOVE_PERCENT);
    }

    public static Animator createRightAlphaOutAnim(final View target,
                                                   float percentTargetWidth) {
        return createRightAlphaOutAnim(target, percentTargetWidth,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createRightAlphaOutAnim(final View target,
                                                   float percentTargetWidth,
                                                   TimeInterpolator xInterpolator,
                                                   TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
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

    public static Animator createCircularRevealInAnim(final View target) {
        return createCircularRevealInAnim(target, 0.5F, 0.5F);
    }

    public static Animator createCircularRevealInAnim(final View target,
                                                      float centerPercentX,
                                                      float centerPercentY) {
        return createCircularRevealInAnim(target, centerPercentX, centerPercentY, createDefInterpolator1());
    }

    public static Animator createCircularRevealInAnim(final View target,
                                                      int centerX,
                                                      int centerY) {
        return createCircularRevealInAnim(target, centerX, centerY, createDefInterpolator1());
    }

    public static Animator createCircularRevealInAnim(final View target,
                                                      float centerPercentX,
                                                      float centerPercentY,
                                                      TimeInterpolator timeInterpolator) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createCircularRevealInAnim(target, centerX, centerY, timeInterpolator);
    }

    public static Animator createCircularRevealInAnim(final View target,
                                                      int centerX,
                                                      int centerY,
                                                      TimeInterpolator timeInterpolator) {
        if (target == null) return null;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) return null;
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, 0, r);
        if (timeInterpolator != null) animator.setInterpolator(timeInterpolator);
        return animator;
    }

    // CircularRevealOut

    public static Animator createCircularRevealOutAnim(final View target) {
        return createCircularRevealOutAnim(target, 0.5F, 0.5F);
    }

    public static Animator createCircularRevealOutAnim(final View target,
                                                       float centerPercentX,
                                                       float centerPercentY) {
        return createCircularRevealOutAnim(target, centerPercentX, centerPercentY, createDefInterpolator1());
    }

    public static Animator createCircularRevealOutAnim(final View target,
                                                       int centerX,
                                                       int centerY) {
        return createCircularRevealOutAnim(target, centerX, centerY, createDefInterpolator1());
    }

    public static Animator createCircularRevealOutAnim(final View target,
                                                       float centerPercentX,
                                                       float centerPercentY,
                                                       TimeInterpolator timeInterpolator) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createCircularRevealOutAnim(target, centerX, centerY, timeInterpolator);
    }

    public static Animator createCircularRevealOutAnim(final View target,
                                                       int centerX,
                                                       int centerY,
                                                       TimeInterpolator timeInterpolator) {
        if (target == null) return null;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) return null;
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, r, 0);
        if (timeInterpolator != null) animator.setInterpolator(timeInterpolator);
        return animator;
    }

    // DelayedZoomIn

    public static Animator createDelayedZoomInAnim(final View target) {
        return createDelayedZoomInAnim(target, 0.5F, 0.5F);
    }

    public static Animator createDelayedZoomInAnim(final View target,
                                                   int centerX,
                                                   int centerY) {
        return createDelayedZoomInAnim(target, centerX, centerY,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createDelayedZoomInAnim(final View target,
                                                   float centerPercentX,
                                                   float centerPercentY) {
        return createDelayedZoomInAnim(target, centerPercentX, centerPercentY,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createDelayedZoomInAnim(final View target,
                                                   float centerPercentX,
                                                   float centerPercentY,
                                                   TimeInterpolator zoomInterpolator,
                                                   TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createDelayedZoomInAnim(target, centerX, centerY, zoomInterpolator, alphaInterpolator);
    }

    public static Animator createDelayedZoomInAnim(final View target,
                                                   int centerX,
                                                   int centerY,
                                                   TimeInterpolator zoomInterpolator,
                                                   TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
        if (!(target instanceof ViewGroup)) {
            return createZoomInAnim(target, centerX, centerY);
        }
        ViewGroup targetGroup = (ViewGroup) target;
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

    public static Animator createDelayedZoomOutAnim(final View target) {
        return createDelayedZoomOutAnim(target, 0.5F, 0.5F);
    }

    public static Animator createDelayedZoomOutAnim(final View target,
                                                    int centerX,
                                                    int centerY) {
        return createDelayedZoomOutAnim(target, centerX, centerY,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createDelayedZoomOutAnim(final View target,
                                                    float centerPercentX,
                                                    float centerPercentY) {
        return createDelayedZoomOutAnim(target, centerPercentX, centerPercentY,
                createDefInterpolator1(), createDefInterpolator2());
    }

    public static Animator createDelayedZoomOutAnim(final View target,
                                                    float centerPercentX,
                                                    float centerPercentY,
                                                    TimeInterpolator zoomInterpolator,
                                                    TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createDelayedZoomOutAnim(target, centerX, centerY, zoomInterpolator, alphaInterpolator);
    }

    public static Animator createDelayedZoomOutAnim(final View target,
                                                    int centerX,
                                                    int centerY,
                                                    TimeInterpolator zoomInterpolator,
                                                    TimeInterpolator alphaInterpolator) {
        if (target == null) return null;
        if (!(target instanceof ViewGroup)) {
            return createZoomInAnim(target, centerX, centerY);
        }
        ViewGroup targetGroup = (ViewGroup) target;
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
