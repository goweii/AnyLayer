package per.goweii.anylayer.ext;

import android.animation.Animator;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import per.goweii.anylayer.Layer;
import per.goweii.anylayer.utils.AnimatorHelper;

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
public class CircularRevealAnimatorCreator implements Layer.AnimatorCreator {
    @Nullable
    @Override
    public Animator createInAnimator(@NonNull View target) {
        return AnimatorHelper.createCircularRevealInAnim(target);
    }

    @Nullable
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        return AnimatorHelper.createCircularRevealOutAnim(target);
    }
}
