package per.goweii.anylayer.ext;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.Layer;
import per.goweii.anylayer.utils.AnimatorHelper;

public class SimpleAnimatorCreator implements Layer.AnimatorCreator {
    private final AnimStyle animStyle;

    public SimpleAnimatorCreator(@Nullable AnimStyle animStyle) {
        this.animStyle = animStyle;
    }

    @Nullable
    @Override
    public Animator createInAnimator(@NonNull View target) {
        if (animStyle == null) return null;
        return animStyle.createInAnimator(target);
    }

    @Nullable
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        if (animStyle == null) return null;
        return animStyle.createOutAnimator(target);
    }

    public enum AnimStyle {
        NONE,
        ALPHA,
        ZOOM,
        ZOOM_ALPHA,
        BOTTOM,
        BOTTOM_ALPHA,
        TOP,
        TOP_ALPHA,
        LEFT,
        LEFT_ALPHA,
        RIGHT,
        RIGHT_ALPHA,
        BOTTOM_ZOOM_ALPHA,
        ;

        @Nullable
        public Animator createInAnimator(@NonNull View target) {
            switch (this) {
                default:
                case NONE:
                    return null;
                case ALPHA:
                    return AnimatorHelper.createAlphaInAnim(target);
                case ZOOM:
                    return AnimatorHelper.createZoomInAnim(target);
                case ZOOM_ALPHA:
                    return AnimatorHelper.createZoomAlphaInAnim(target);
                case BOTTOM:
                    return AnimatorHelper.createBottomInAnim(target);
                case BOTTOM_ALPHA:
                    return AnimatorHelper.createBottomAlphaInAnim(target);
                case TOP:
                    return AnimatorHelper.createTopInAnim(target);
                case TOP_ALPHA:
                    return AnimatorHelper.createTopAlphaInAnim(target);
                case LEFT:
                    return AnimatorHelper.createLeftInAnim(target);
                case LEFT_ALPHA:
                    return AnimatorHelper.createLeftAlphaInAnim(target);
                case RIGHT:
                    return AnimatorHelper.createRightInAnim(target);
                case RIGHT_ALPHA:
                    return AnimatorHelper.createRightAlphaInAnim(target);
                case BOTTOM_ZOOM_ALPHA:
                    AnimatorSet set = new AnimatorSet();
                    Animator a1 = AnimatorHelper.createBottomAlphaInAnim(target, 0.3F);
                    a1.setInterpolator(new DecelerateInterpolator(2.5f));
                    Animator a2 = AnimatorHelper.createZoomAlphaInAnim(target, 0.9F);
                    a2.setInterpolator(new DecelerateInterpolator(1.5f));
                    set.playTogether(a1, a2);
                    return set;
            }
        }

        @Nullable
        public Animator createOutAnimator(@NonNull View target) {
            switch (this) {
                default:
                case NONE:
                    return null;
                case ALPHA:
                    return AnimatorHelper.createAlphaOutAnim(target);
                case ZOOM:
                    return AnimatorHelper.createZoomOutAnim(target);
                case ZOOM_ALPHA:
                    return AnimatorHelper.createZoomAlphaOutAnim(target);
                case BOTTOM:
                    return AnimatorHelper.createBottomOutAnim(target);
                case BOTTOM_ALPHA:
                    return AnimatorHelper.createBottomAlphaOutAnim(target);
                case TOP:
                    return AnimatorHelper.createTopOutAnim(target);
                case TOP_ALPHA:
                    return AnimatorHelper.createTopAlphaOutAnim(target);
                case LEFT:
                    return AnimatorHelper.createLeftOutAnim(target);
                case LEFT_ALPHA:
                    return AnimatorHelper.createLeftAlphaOutAnim(target);
                case RIGHT:
                    return AnimatorHelper.createRightOutAnim(target);
                case RIGHT_ALPHA:
                    return AnimatorHelper.createRightAlphaOutAnim(target);
                case BOTTOM_ZOOM_ALPHA:
                    AnimatorSet set = new AnimatorSet();
                    Animator a1 = AnimatorHelper.createBottomAlphaOutAnim(target, 0.3F);
                    a1.setInterpolator(new DecelerateInterpolator(1.5f));
                    Animator a2 = AnimatorHelper.createZoomAlphaOutAnim(target, 0.9F);
                    a2.setInterpolator(new DecelerateInterpolator(2.5f));
                    set.playTogether(a1, a2);
                    return set;
            }
        }
    }
}
