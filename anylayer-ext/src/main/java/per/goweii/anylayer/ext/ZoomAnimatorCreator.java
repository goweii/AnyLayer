package per.goweii.anylayer.ext;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.Layer;

public class ZoomAnimatorCreator implements Layer.AnimatorCreator {

    private Point center = null;
    private PointF centerPercent = null;
    private TimeInterpolator interpolator = null;

    public ZoomAnimatorCreator() {
    }

    public ZoomAnimatorCreator center(int x, int y) {
        if (center == null) {
            center = new Point();
        }
        center.set(x, y);
        return this;
    }

    public ZoomAnimatorCreator center(float x, float y) {
        if (centerPercent == null) {
            centerPercent = new PointF();
        }
        centerPercent.set(x, y);
        return this;
    }

    public ZoomAnimatorCreator setInterpolator(@Nullable TimeInterpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }

    @Nullable
    @Override
    public Animator createInAnimator(@NonNull View target) {
        if (center != null) {
            return AnimatorHelper.createZoomInAnim(target, center.x, center.y, interpolator);
        } else if (centerPercent != null) {
            return AnimatorHelper.createZoomInAnim(target, centerPercent.x, centerPercent.y, interpolator);
        } else {
            return AnimatorHelper.createZoomInAnim(target, interpolator);
        }
    }

    @Nullable
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        if (center != null) {
            return AnimatorHelper.createZoomOutAnim(target, center.x, center.y, interpolator);
        } else if (centerPercent != null) {
            return AnimatorHelper.createZoomOutAnim(target, centerPercent.x, centerPercent.y, interpolator);
        } else {
            return AnimatorHelper.createZoomOutAnim(target, interpolator);
        }
    }
}
