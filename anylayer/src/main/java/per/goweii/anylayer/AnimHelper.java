package per.goweii.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

/**
 * @author Cuizhen
 * @date 2018/5/20
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class AnimHelper {

    public static Animator createAlphaInAnim(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        alpha.setInterpolator(new DecelerateInterpolator());
        return alpha;
    }

    public static Animator createAlphaOutAnim(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        alpha.setInterpolator(new DecelerateInterpolator());
        return alpha;
    }

    public static Animator createZoomInAnim(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0.618f, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0.618f, 1);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createZoomOutAnim(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", target.getAlpha(), 0);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), 0.618f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), 0.618f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, scaleX, scaleY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createTopInAnim(final View target) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", -target.getBottom(), 0);
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createTopOutAnim(final View target) {
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), -target.getBottom());
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createTopAlphaInAnim(final View target) {
        float y = (1 - 0.618f) * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", -y, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createTopAlphaOutAnim(final View target) {
        float y = (1 - 0.618f) * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), -y);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createBottomInAnim(final View target) {
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", y, 0);
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createBottomOutAnim(final View target) {
        float y = ((ViewGroup) target.getParent()).getMeasuredHeight() - target.getTop();
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), y);
        translationY.setInterpolator(new DecelerateInterpolator());
        return translationY;
    }

    public static Animator createBottomAlphaInAnim(final View target) {
        float y = (1 - 0.618f) * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", y, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createBottomAlphaOutAnim(final View target) {
        float y = (1 - 0.618f) * target.getMeasuredHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), y);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationY);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createLeftInAnim(final View target) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -target.getRight(), 0);
        translationX.setInterpolator(new DecelerateInterpolator());
        translationX.start();
        return translationX;
    }

    public static Animator createLeftOutAnim(final View target) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), -target.getRight());
        translationX.setInterpolator(new DecelerateInterpolator());
        return translationX;
    }

    public static Animator createLeftAlphaInAnim(final View target) {
        float x = (1 - 0.618f) * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -x, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createLeftAlphaOutAnim(final View target) {
        float x = (1 - 0.618f) * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), -x);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createRightInAnim(final View target) {
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", x, 0);
        translationX.setInterpolator(new DecelerateInterpolator());
        return translationX;
    }

    public static Animator createRightOutAnim(final View target) {
        float x = ((ViewGroup) target.getParent()).getMeasuredWidth() - target.getLeft();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), x);
        translationX.setInterpolator(new DecelerateInterpolator());
        return translationX;
    }

    public static Animator createRightAlphaInAnim(final View target) {
        float x = (1 - 0.618f) * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", x, 0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    public static Animator createRightAlphaOutAnim(final View target) {
        float x = (1 - 0.618f) * target.getMeasuredWidth();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1, 0);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), x);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translationX);
        set.setInterpolator(new DecelerateInterpolator());
        return set;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealInAnim(View target, int centerX, int centerY) {
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, 0, r);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator createCircularRevealOutAnim(View target, int centerX, int centerY) {
        int x = target.getMeasuredWidth();
        int y = target.getMeasuredHeight();
        int r = (int) Math.sqrt(Math.pow(Math.max(centerX, x - centerX), 2) + Math.pow(Math.max(centerY, y - centerY), 2));
        Animator animator = ViewAnimationUtils.createCircularReveal(target, centerX, centerY, r, 0);
        animator.setInterpolator(new DecelerateInterpolator());
        return animator;
    }
}
