package per.goweii.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
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
public class AnimHelper {

    public static Animator createAlphaInAnim(@NonNull final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        alpha.setInterpolator(new DecelerateInterpolator());
        return alpha;
    }

    public static Animator createAlphaOutAnim(@NonNull final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        alpha.setInterpolator(new DecelerateInterpolator());
        return alpha;
    }

    public static Animator createZoomAlphaInAnim(@NonNull final View target, int centerX, int centerY){
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0.618f, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0.618f, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createZoomAlphaOutAnim(@NonNull final View target, int centerX, int centerY){
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), 0.618f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), 0.618f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createZoomAlphaInAnim(@NonNull final View target,
                                                 @FloatRange(from = 0, to = 1) float centerPercentX,
                                                 @FloatRange(from = 0, to = 1) float centerPercentY){
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createZoomAlphaInAnim(target, centerX, centerY);
    }

    public static Animator createZoomAlphaOutAnim(@NonNull final View target,
                                                  @FloatRange(from = 0, to = 1) float centerPercentX,
                                                  @FloatRange(from = 0, to = 1) float centerPercentY){
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createZoomAlphaOutAnim(target, centerX, centerY);
    }

    public static Animator createZoomAlphaInAnim(@NonNull final View target) {
        return createZoomAlphaInAnim(target, 0.5F, 0.5F);
    }

    public static Animator createZoomAlphaOutAnim(@NonNull final View target) {
        return createZoomAlphaOutAnim(target, 0.5F, 0.5F);
    }

    public static Animator createTopInAnim(@NonNull final View target) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", -target.getBottom(), 0);
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createTopOutAnim(@NonNull final View target) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), -target.getBottom());
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createTopAlphaInAnim(@NonNull final View target) {
        float y = (1 - 0.618f) * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", -y, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createTopAlphaOutAnim(@NonNull final View target) {
        float y = (1 - 0.618f) * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), -y);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createBottomInAnim(@NonNull final View target) {
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", y, 0);
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createBottomOutAnim(@NonNull final View target) {
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), y);
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createBottomAlphaInAnim(@NonNull final View target) {
        float y = (1 - 0.618f) * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", y, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createBottomAlphaOutAnim(@NonNull final View target) {
        float y = (1 - 0.618f) * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), y);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createLeftInAnim(@NonNull final View target) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -target.getRight(), 0);
        translationX.setInterpolator(new DecelerateInterpolator());
        translationX.start();
        return translationX;
    }

    public static Animator createLeftOutAnim(@NonNull final View target) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), -target.getRight());
        translationX.setInterpolator(new DecelerateInterpolator());
        return translationX;
    }

    public static Animator createLeftAlphaInAnim(@NonNull final View target) {
        float x = (1 - 0.618f) * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -x, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createLeftAlphaOutAnim(@NonNull final View target) {
        float x = (1 - 0.618f) * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), -x);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createRightInAnim(@NonNull final View target) {
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", x, 0);
        translationX.setInterpolator(new DecelerateInterpolator());
        return translationX;
    }

    public static Animator createRightOutAnim(@NonNull final View target) {
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), x);
        translationX.setInterpolator(new DecelerateInterpolator());
        return translationX;
    }

    public static Animator createRightAlphaInAnim(@NonNull final View target) {
        float x = (1 - 0.618f) * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", x, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createRightAlphaOutAnim(@NonNull final View target) {
        float x = (1 - 0.618f) * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), x);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealInAnim(@NonNull final View target, int centerX, int centerY) {
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, 0, r);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealInAnim(@NonNull final View target,
                                                      @FloatRange(from = 0, to = 1) float centerPercentX,
                                                      @FloatRange(from = 0, to = 1) float centerPercentY) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createCircularRevealInAnim(target, centerX, centerY);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealOutAnim(@NonNull final View target, int centerX, int centerY) {
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, r, 0);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealOutAnim(@NonNull final View target,
                                                       @FloatRange(from = 0, to = 1) float centerPercentX,
                                                       @FloatRange(from = 0, to = 1) float centerPercentY) {
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createCircularRevealOutAnim(target, centerX, centerY);
    }

    public static Animator createZoomInAnim(@NonNull final View target, int centerX, int centerY){
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createZoomOutAnim(@NonNull final View target, int centerX, int centerY){
        target.setPivotX(centerX);
        target.setPivotY(centerY);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), 0.618f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), 0.618f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createZoomInAnim(@NonNull final View target,
                                                 @FloatRange(from = 0, to = 1) float centerPercentX,
                                                 @FloatRange(from = 0, to = 1) float centerPercentY){
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createZoomAlphaInAnim(target, centerX, centerY);
    }

    public static Animator createZoomOutAnim(@NonNull final View target,
                                                  @FloatRange(from = 0, to = 1) float centerPercentX,
                                                  @FloatRange(from = 0, to = 1) float centerPercentY){
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createZoomAlphaOutAnim(target, centerX, centerY);
    }

    public static Animator createZoomInAnim(@NonNull final View target) {
        return createZoomAlphaInAnim(target, 0.5F, 0.5F);
    }

    public static Animator createZoomOutAnim(@NonNull final View target) {
        return createZoomAlphaOutAnim(target, 0.5F, 0.5F);
    }

    public static Animator createDelayedZoomInAnim(@NonNull final View target, int centerX, int centerY){
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

    public static Animator createDelayedZoomOutAnim(@NonNull final View target, int centerX, int centerY){
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
                    for (int i = targetGroup.getChildCount() - 1; i >= 0 ; i--) {
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

    public static Animator createDelayedZoomInAnim(@NonNull final View target,
                                                   @FloatRange(from = 0, to = 1) float centerPercentX,
                                                   @FloatRange(from = 0, to = 1) float centerPercentY){
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createDelayedZoomInAnim(target, centerX, centerY);
    }

    public static Animator createDelayedZoomOutAnim(@NonNull final View target,
                                                   @FloatRange(from = 0, to = 1) float centerPercentX,
                                                   @FloatRange(from = 0, to = 1) float centerPercentY){
        int centerX = (int) (target.getMeasuredWidth() * centerPercentX);
        int centerY = (int) (target.getMeasuredHeight() * centerPercentY);
        return createDelayedZoomOutAnim(target, centerX, centerY);
    }
}
