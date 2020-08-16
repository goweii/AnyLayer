package per.goweii.anylayer.ext;

import android.animation.Animator;
import android.view.View;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import per.goweii.anylayer.Layer;

/**
 * @author CuiZhen
 */
public class SimpleAnimatorCreator implements Layer.AnimatorCreator {
    private final AnimatorStyle animatorStyle;

    public SimpleAnimatorCreator(@Nullable AnimatorStyle animatorStyle) {
        this.animatorStyle = animatorStyle;
    }

    @Nullable
    @Override
    public Animator createInAnimator(@NonNull View target) {
        if (animatorStyle == null) return null;
        return animatorStyle.createInAnimator(target);
    }

    @Nullable
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        if (animatorStyle == null) return null;
        return animatorStyle.createOutAnimator(target);
    }
}
