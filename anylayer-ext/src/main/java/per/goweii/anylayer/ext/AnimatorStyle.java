package per.goweii.anylayer.ext;

import android.animation.Animator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.AnimatorHelper;

/**
 * @author CuiZhen
 */

public enum AnimatorStyle {
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
        }
    }
}