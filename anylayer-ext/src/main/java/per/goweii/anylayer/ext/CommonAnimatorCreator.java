package per.goweii.anylayer.ext;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.Layer;

/**
 * @author CuiZhen
 */
public class CommonAnimatorCreator implements Layer.AnimatorCreator {

    public static class AlphaAttr {
        private float from = 0F;
        private float to = 1F;

        public AlphaAttr from(float from) {
            this.from = from;
            return this;
        }

        public AlphaAttr to(float to) {
            this.to = to;
            return this;
        }

        private TimeInterpolator inTimeInterpolator = null;
        private TimeInterpolator outTimeInterpolator = null;

        public AlphaAttr timeInterpolator(TimeInterpolator timeInterpolator) {
            this.inTimeInterpolator = timeInterpolator;
            this.outTimeInterpolator = timeInterpolator;
            return this;
        }

        public AlphaAttr inTimeInterpolator(TimeInterpolator inTimeInterpolator) {
            this.inTimeInterpolator = inTimeInterpolator;
            return this;
        }

        public AlphaAttr outTimeInterpolator(TimeInterpolator outTimeInterpolator) {
            this.outTimeInterpolator = outTimeInterpolator;
            return this;
        }
    }

    public static class ScaleAttr {
        private float fromX = 0F;
        private float fromY = 0F;
        private float toX = 1F;
        private float toY = 1F;

        private float pivotX = 0F;
        private float pivotY = 0F;
        private float pivotPercentX = 0.5F;
        private float pivotPercentY = 0.5F;

        private boolean usePivotPercent = true;

        public ScaleAttr from(float from) {
            return from(from, from);
        }

        public ScaleAttr from(float fromX, float fromY) {
            this.fromX = fromX;
            this.fromY = fromY;
            return this;
        }

        public ScaleAttr to(float to) {
            return to(to, to);
        }

        public ScaleAttr to(float toX, float toY) {
            this.toX = toX;
            this.toY = toY;
            return this;
        }

        public ScaleAttr pivot(float pivot) {
            return pivot(pivot, pivot);
        }

        public ScaleAttr pivot(float pivotX, float pivotY) {
            this.usePivotPercent = false;
            this.pivotX = pivotX;
            this.pivotY = pivotY;
            return this;
        }

        public ScaleAttr pivotPercent(float pivotPercent) {
            return pivotPercent(pivotPercent, pivotPercent);
        }

        public ScaleAttr pivotPercent(float pivotPercentX, float pivotPercentY) {
            this.usePivotPercent = true;
            this.pivotPercentX = pivotPercentX;
            this.pivotPercentY = pivotPercentY;
            return this;
        }

        public float getPivotX(@NonNull View target) {
            float pivot;
            if (usePivotPercent) {
                pivot = target.getWidth() * pivotPercentX;
            } else {
                pivot = pivotX;
            }
            return pivot;
        }

        public float getPivotY(@NonNull View target) {
            float pivot;
            if (usePivotPercent) {
                pivot = target.getHeight() * pivotPercentY;
            } else {
                pivot = pivotY;
            }
            return pivot;
        }

        private TimeInterpolator xInTimeInterpolator = null;
        private TimeInterpolator yInTimeInterpolator = null;
        private TimeInterpolator xOutTimeInterpolator = null;
        private TimeInterpolator yOutTimeInterpolator = null;

        public ScaleAttr timeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr inTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr outTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr xTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr yTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr xInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr xOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr yInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr yOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }
    }

    public static class TranslationAttr {
        private float fromX = 0F;
        private float fromY = 0F;
        private float toX = 0F;
        private float toY = Float.MAX_VALUE;
        private float fromPercentX = 0F;
        private float fromPercentY = 0F;
        private float toPercentX = 0F;
        private float toPercentY = 1F;

        private boolean useFromPercent = true;
        private boolean useToPercent = true;

        public TranslationAttr from(float from) {
            return from(from, from);
        }

        public TranslationAttr from(float fromX, float fromY) {
            this.useFromPercent = false;
            this.fromX = fromX;
            this.fromY = fromY;
            return this;
        }

        public TranslationAttr fromPercent(float fromPercent) {
            return fromPercent(fromPercent, fromPercent);
        }

        public TranslationAttr fromPercent(float fromPercentX, float fromPercentY) {
            this.useFromPercent = true;
            this.fromPercentX = fromPercentX;
            this.fromPercentY = fromPercentY;
            return this;
        }

        public TranslationAttr to(float to) {
            return to(to, to);
        }

        public TranslationAttr to(float toX, float toY) {
            this.useToPercent = false;
            this.toX = toX;
            this.toY = toY;
            return this;
        }

        public TranslationAttr toPercent(float toPercent) {
            return toPercent(toPercent, toPercent);
        }

        public TranslationAttr toPercent(float toPercentX, float toPercentY) {
            this.useToPercent = true;
            this.toPercentX = toPercentX;
            this.toPercentY = toPercentY;
            return this;
        }

        public float getFromX(@NonNull View target) {
            float from;
            if (useFromPercent) {
                from = target.getWidth() * fromPercentX;
            } else {
                from = fromX;
            }
            return from;
        }

        public float getFromY(@NonNull View target) {
            float from;
            if (useFromPercent) {
                from = target.getHeight() * fromPercentY;
            } else {
                from = fromY;
            }
            return from;
        }

        public float getToX(@NonNull View target) {
            float to;
            if (useToPercent) {
                to = target.getWidth() * toPercentX;
            } else {
                to = toX;
            }
            return to;
        }

        public float getToY(@NonNull View target) {
            float to;
            if (useToPercent) {
                to = target.getHeight() * toPercentY;
            } else {
                to = toY;
            }
            return to;
        }

        private TimeInterpolator xInTimeInterpolator = null;
        private TimeInterpolator yInTimeInterpolator = null;
        private TimeInterpolator xOutTimeInterpolator = null;
        private TimeInterpolator yOutTimeInterpolator = null;

        public TranslationAttr timeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr inTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr outTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr xTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr yTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr xInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr xOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr yInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr yOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }
    }

    private AlphaAttr alphaAttr = null;
    private ScaleAttr scaleAttr = null;
    private TranslationAttr translationAttr = null;
    private TimeInterpolator inTimeInterpolator = null;
    private TimeInterpolator outTimeInterpolator = null;

    public CommonAnimatorCreator alphaAttr(AlphaAttr alphaAttr) {
        this.alphaAttr = alphaAttr;
        return this;
    }

    public CommonAnimatorCreator scaleAttr(ScaleAttr scaleAttr) {
        this.scaleAttr = scaleAttr;
        return this;
    }

    public CommonAnimatorCreator translationAttr(TranslationAttr translationAttr) {
        this.translationAttr = translationAttr;
        return this;
    }

    public CommonAnimatorCreator timeInterpolator(TimeInterpolator timeInterpolator) {
        this.inTimeInterpolator = timeInterpolator;
        this.outTimeInterpolator = timeInterpolator;
        return this;
    }

    public CommonAnimatorCreator inTimeInterpolator(TimeInterpolator timeInterpolator) {
        this.inTimeInterpolator = timeInterpolator;
        return this;
    }

    public CommonAnimatorCreator outTimeInterpolator(TimeInterpolator timeInterpolator) {
        this.outTimeInterpolator = timeInterpolator;
        return this;
    }

    @NonNull
    @Override
    public Animator createInAnimator(@NonNull View target) {
        List<Animator> animators = new ArrayList<>(5);
        if (alphaAttr != null) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha",
                    alphaAttr.from, alphaAttr.to);
            alpha.setInterpolator(alphaAttr.inTimeInterpolator);
            animators.add(alpha);
        }
        if (scaleAttr != null) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX",
                    scaleAttr.fromX, scaleAttr.toX);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY",
                    scaleAttr.fromY, scaleAttr.toY);
            target.setPivotX(scaleAttr.getPivotX(target));
            target.setPivotY(scaleAttr.getPivotY(target));
            scaleX.setInterpolator(scaleAttr.xInTimeInterpolator);
            scaleY.setInterpolator(scaleAttr.yInTimeInterpolator);
            animators.add(scaleX);
            animators.add(scaleY);
        }
        if (translationAttr != null) {
            ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX",
                    translationAttr.getFromX(target), translationAttr.getToX(target));
            ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY",
                    translationAttr.getFromY(target), translationAttr.getToY(target));
            translationX.setInterpolator(translationAttr.xInTimeInterpolator);
            translationY.setInterpolator(translationAttr.yInTimeInterpolator);
            animators.add(translationX);
            animators.add(translationY);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setInterpolator(inTimeInterpolator);
        return set;
    }

    @NonNull
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        List<Animator> animators = new ArrayList<>(5);
        if (alphaAttr != null) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha",
                    target.getAlpha(), alphaAttr.from);
            alpha.setInterpolator(alphaAttr.inTimeInterpolator);
            animators.add(alpha);
        }
        if (scaleAttr != null) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX",
                    target.getScaleX(), scaleAttr.fromX);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY",
                    target.getScaleY(), scaleAttr.fromY);
            target.setPivotX(scaleAttr.getPivotX(target));
            target.setPivotY(scaleAttr.getPivotY(target));
            scaleX.setInterpolator(scaleAttr.xInTimeInterpolator);
            scaleY.setInterpolator(scaleAttr.yInTimeInterpolator);
            animators.add(scaleX);
            animators.add(scaleY);
        }
        if (translationAttr != null) {
            ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX",
                    target.getTranslationX(), translationAttr.getFromX(target));
            ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY",
                    target.getTranslationY(), translationAttr.getFromY(target));
            translationX.setInterpolator(translationAttr.xInTimeInterpolator);
            translationY.setInterpolator(translationAttr.yInTimeInterpolator);
            animators.add(translationX);
            animators.add(translationY);
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setInterpolator(outTimeInterpolator);
        return set;
    }
}
