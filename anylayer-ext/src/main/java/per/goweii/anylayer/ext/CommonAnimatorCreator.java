package per.goweii.anylayer.ext;

import android.animation.Animator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.Layer;

public class CommonAnimatorCreator implements Layer.AnimatorCreator {
    private final AnimatorStyle animatorStyle;

    public CommonAnimatorCreator(@Nullable AnimatorStyle animatorStyle) {
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
