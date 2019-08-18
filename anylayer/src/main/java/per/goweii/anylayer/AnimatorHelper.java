package per.goweii.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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

    public static Animator createAlphaInAnim(final View target) {
        if (target == null) return null;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        alpha.setInterpolator(new DecelerateInterpolator());
        return alpha;
    }

    public static Animator createAlphaOutAnim(final View target) {
        if (target == null) return null;
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        alpha.setInterpolator(new DecelerateInterpolator());
        return alpha;
    }

    public static Animator createZoomAlphaInAnim(final View target, int centerX, int centerY, float fromScale) {
        if (target == null) return null;
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", fromScale, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", fromScale, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createZoomAlphaInAnim(final View target, int centerX, int centerY) {
        return createZoomAlphaInAnim(target, centerX, centerY, 0.618f);
    }

    public static Animator createZoomAlphaOutAnim(final View target, int centerX, int centerY, float toScale) {
        if (target == null) return null;
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), toScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), toScale);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createZoomAlphaOutAnim(final View target, int centerX, int centerY) {
        return createZoomAlphaOutAnim(target, centerX, centerY, 0.618f);
    }

    public static Animator createZoomAlphaInAnim(final View target,
                                                 float centerPercentX,
                                                 float centerPercentY) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomAlphaInAnim(target, centerX, centerY);
    }

    public static Animator createZoomAlphaInAnim(final View target,
                                                 float centerPercentX,
                                                 float centerPercentY,
                                                 float fromScale) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomAlphaInAnim(target, centerX, centerY, fromScale);
    }

    public static Animator createZoomAlphaOutAnim(final View target,
                                                  float centerPercentX,
                                                  float centerPercentY) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomAlphaOutAnim(target, centerX, centerY);
    }

    public static Animator createZoomAlphaOutAnim(final View target,
                                                  float centerPercentX,
                                                  float centerPercentY,
                                                  float toScale) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomAlphaOutAnim(target, centerX, centerY, toScale);
    }

    public static Animator createZoomAlphaInAnim(final View target, float fromScale) {
        return createZoomAlphaInAnim(target, 0.5F, 0.5F, fromScale);
    }

    public static Animator createZoomAlphaInAnim(final View target) {
        if (target == null) return null;
        return createZoomAlphaInAnim(target, 0.5F, 0.5F);
    }

    public static Animator createZoomAlphaOutAnim(final View target, float toScale) {
        if (target == null) return null;
        return createZoomAlphaOutAnim(target, 0.5F, 0.5F, toScale);
    }

    public static Animator createZoomAlphaOutAnim(final View target) {
        if (target == null) return null;
        return createZoomAlphaOutAnim(target, 0.5F, 0.5F);
    }

    public static Animator createTopInAnim(final View target) {
        if (target == null) return null;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", -target.getBottom(), 0);
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createTopOutAnim(final View target) {
        if (target == null) return null;
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), -target.getBottom());
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createTopAlphaInAnim(final View target, final float percentTargetHeight) {
        if (target == null) return null;
        float y = percentTargetHeight * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", -y, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createTopAlphaInAnim(final View target) {
        return createTopAlphaInAnim(target, 1 - 0.618f);
    }

    public static Animator createTopAlphaOutAnim(final View target, final float percentTargetHeight) {
        if (target == null) return null;
        float y = percentTargetHeight * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), -y);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createTopAlphaOutAnim(final View target) {
        return createTopAlphaOutAnim(target, 1 - 0.618f);
    }

    public static Animator createBottomInAnim(final View target) {
        if (target == null) return null;
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", y, 0);
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createBottomOutAnim(final View target) {
        if (target == null) return null;
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), y);
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createBottomAlphaInAnim(final View target, final float percentTargetHeight) {
        if (target == null) return null;
        float y = percentTargetHeight * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", y, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createBottomAlphaInAnim(final View target) {
        return createBottomAlphaInAnim(target, 1 - 0.618f);
    }

    public static Animator createBottomAlphaOutAnim(final View target, final float percentTargetHeight) {
        if (target == null) return null;
        float y = percentTargetHeight * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), y);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createBottomAlphaOutAnim(final View target) {
        return createBottomAlphaOutAnim(target, 1 - 0.618f);
    }

    public static Animator createLeftInAnim(final View target) {
        if (target == null) return null;
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -target.getRight(), 0);
        translationX.setInterpolator(new DecelerateInterpolator());
        translationX.start();
        return translationX;
    }

    public static Animator createLeftOutAnim(final View target) {
        if (target == null) return null;
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), -target.getRight());
        translationX.setInterpolator(new DecelerateInterpolator());
        return translationX;
    }

    public static Animator createLeftAlphaInAnim(final View target, final float percentTargetWidth) {
        if (target == null) return null;
        float x = percentTargetWidth * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -x, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createLeftAlphaInAnim(final View target) {
        return createLeftAlphaInAnim(target, 1 - 0.618f);
    }

    public static Animator createLeftAlphaOutAnim(final View target, final float percentTargetWidth) {
        if (target == null) return null;
        float x = percentTargetWidth * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), -x);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createLeftAlphaOutAnim(final View target) {
        return createLeftAlphaOutAnim(target, 1 - 0.618f);
    }

    public static Animator createRightInAnim(final View target) {
        if (target == null) return null;
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", x, 0);
        translationX.setInterpolator(new DecelerateInterpolator());
        return translationX;
    }

    public static Animator createRightOutAnim(final View target) {
        if (target == null) return null;
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), x);
        translationX.setInterpolator(new DecelerateInterpolator());
        return translationX;
    }

    public static Animator createRightAlphaInAnim(final View target, final float percentTargetWidth) {
        if (target == null) return null;
        float x = percentTargetWidth * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", x, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createRightAlphaInAnim(final View target) {
        return createRightAlphaInAnim(target, 1 - 0.618f);
    }

    public static Animator createRightAlphaOutAnim(final View target, final float percentTargetWidth) {
        if (target == null) return null;
        float x = percentTargetWidth * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), x);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createRightAlphaOutAnim(final View target) {
        return createRightAlphaOutAnim(target, 1 - 0.618f);
    }

    // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealInAnim(final View target, int centerX, int centerY) {
        if (target == null) return null;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) return null;
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, 0, r);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealInAnim(final View target,
                                                      float centerPercentX,
                                                      float centerPercentY) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createCircularRevealInAnim(target, centerX, centerY);
    }

    // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealOutAnim(final View target, int centerX, int centerY) {
        if (target == null) return null;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) return null;
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, r, 0);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    // @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealOutAnim(final View target,
                                                       float centerPercentX,
                                                       float centerPercentY) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createCircularRevealOutAnim(target, centerX, centerY);
    }

    public static Animator createZoomInAnim(final View target, int centerX, int centerY) {
        if (target == null) return null;
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createZoomOutAnim(final View target, int centerX, int centerY) {
        if (target == null) return null;
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), 0.618f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), 0.618f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createZoomInAnim(final View target,
                                            float centerPercentX,
                                            float centerPercentY) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomInAnim(target, centerX, centerY);
    }

    public static Animator createZoomOutAnim(final View target,
                                             float centerPercentX,
                                             float centerPercentY) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createZoomOutAnim(target, centerX, centerY);
    }

    public static Animator createZoomInAnim(final View target) {
        if (target == null) return null;
        return createZoomInAnim(target, 0.5F, 0.5F);
    }

    public static Animator createZoomOutAnim(final View target) {
        if (target == null) return null;
        return createZoomOutAnim(target, 0.5F, 0.5F);
    }

    public static Animator createDelayedZoomInAnim(final View target, int centerX, int centerY) {
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
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
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
                        alphaChild.setStartDelay(18 * i);
                        alphaChild.setDuration(50);
                        childAnimators.add(alphaChild);
                    }
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(childAnimators);
                    set.setInterpolator(new DecelerateInterpolator());
                    set.start();
                }
            }
        });
        return set;
    }

    public static Animator createDelayedZoomOutAnim(final View target, int centerX, int centerY) {
        if (target == null) return null;
        if (!(target instanceof ViewGroup)) {
            return createZoomInAnim(target, centerX, centerY);
        }
        ViewGroup targetGroup = (ViewGroup) target;
        targetGroup.setPivotX(centerX);
        targetGroup.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(targetGroup, "scaleX", targetGroup.getScaleX(), 0);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(targetGroup, "scaleY", targetGroup.getScaleY(), 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
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
                        alphaChild.setStartDelay(18 * (targetGroup.getChildCount() - 1 - i));
                        alphaChild.setDuration(50);
                        childAnimators.add(alphaChild);
                    }
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(childAnimators);
                    set.setInterpolator(new DecelerateInterpolator());
                    set.start();
                }
            }
        });
        return set;
    }

    public static Animator createDelayedZoomInAnim(final View target,
                                                   float centerPercentX,
                                                   float centerPercentY) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createDelayedZoomInAnim(target, centerX, centerY);
    }

    public static Animator createDelayedZoomOutAnim(final View target,
                                                    float centerPercentX,
                                                    float centerPercentY) {
        if (target == null) return null;
        int centerX = (int) (target.getMeasuredWidth() * Utils.floatRange01(centerPercentX));
        int centerY = (int) (target.getMeasuredHeight() * Utils.floatRange01(centerPercentY));
        return createDelayedZoomOutAnim(target, centerX, centerY);
    }
}
