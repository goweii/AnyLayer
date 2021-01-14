package per.goweii.anylayer.utils;

import android.animation.Animator;

public class AnimatorListener implements Animator.AnimatorListener {
    private boolean beenCanceled = false;

    @Override
    public void onAnimationStart(Animator animation) {
        beenCanceled = false;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!beenCanceled) {
            onAnimationEndWithoutCancel(animation);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        beenCanceled = true;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationEndWithoutCancel(Animator animation){
    }
}
