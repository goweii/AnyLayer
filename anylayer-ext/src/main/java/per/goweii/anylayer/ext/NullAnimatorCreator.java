package per.goweii.anylayer.ext;

import android.animation.Animator;
import android.view.View;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import per.goweii.anylayer.Layer;

/**
 * @author CuiZhen
 */
public class NullAnimatorCreator implements Layer.AnimatorCreator {
    @Nullable
    @Override
    public Animator createInAnimator(@NonNull View target) {
        return null;
    }

    @Nullable
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        return null;
    }
}
