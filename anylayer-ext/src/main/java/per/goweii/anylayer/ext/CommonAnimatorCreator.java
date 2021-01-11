package per.goweii.anylayer.ext;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.Layer;

/**
 * @author CuiZhen
 */
public class CommonAnimatorCreator implements Layer.AnimatorCreator {

    public interface Attr {
        @NonNull
        Animator createIn(@NonNull View target);

        @NonNull
        Animator createOut(@NonNull View target);
    }

    public static class AlphaAttr implements Attr {
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

        @NonNull
        @Override
        public Animator createIn(@NonNull View target) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", from, to);
            alpha.setInterpolator(inTimeInterpolator);
            return alpha;
        }

        @NonNull
        @Override
        public Animator createOut(@NonNull View target) {
            ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", from, to);
            alpha.setInterpolator(outTimeInterpolator);
            return alpha;
        }
    }

    public static class ScaleAttr implements Attr {
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

        @NonNull
        @Override
        public Animator createIn(@NonNull View target) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", fromX, toX);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", fromY, toY);
            target.setPivotX(getPivotX(target));
            target.setPivotY(getPivotY(target));
            scaleX.setInterpolator(xInTimeInterpolator);
            scaleY.setInterpolator(yInTimeInterpolator);
            set.playTogether(scaleX, scaleY);
            return set;
        }

        @NonNull
        @Override
        public Animator createOut(@NonNull View target) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", target.getScaleX(), fromX);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", target.getScaleY(), fromY);
            target.setPivotX(getPivotX(target));
            target.setPivotY(getPivotY(target));
            scaleX.setInterpolator(xOutTimeInterpolator);
            scaleY.setInterpolator(yOutTimeInterpolator);
            set.playTogether(scaleX, scaleY);
            return set;
        }
    }

    public static class TranslationAttr implements Attr {
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

        @NonNull
        @Override
        public Animator createIn(@NonNull View target) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", getFromX(target), getToX(target));
            ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", getFromY(target), getToY(target));
            translationX.setInterpolator(xInTimeInterpolator);
            translationY.setInterpolator(yInTimeInterpolator);
            set.playTogether(translationX, translationY);
            return set;
        }

        @NonNull
        @Override
        public Animator createOut(@NonNull View target) {
            AnimatorSet set = new AnimatorSet();
            ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", target.getTranslationX(), getFromX(target));
            ObjectAnimator translationY = ObjectAnimator.ofFloat(target, "translationY", target.getTranslationY(), getFromY(target));
            translationX.setInterpolator(xOutTimeInterpolator);
            translationY.setInterpolator(yOutTimeInterpolator);
            set.playTogether(translationX, translationY);
            return set;
        }
    }

    private List<Attr> attrs = new ArrayList<>();
    private TimeInterpolator inTimeInterpolator = null;
    private TimeInterpolator outTimeInterpolator = null;

    public CommonAnimatorCreator addAttr(Attr attr) {
        this.attrs.add(attr);
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
        List<Animator> animators = new ArrayList<>(attrs.size());
        for (Attr attr : attrs) {
            animators.add(attr.createIn(target));
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setInterpolator(inTimeInterpolator);
        return set;
    }

    @NonNull
    @Override
    public Animator createOutAnimator(@NonNull View target) {
        List<Animator> animators = new ArrayList<>(attrs.size());
        for (Attr attr : attrs) {
            animators.add(attr.createOut(target));
        }
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animators);
        set.setInterpolator(outTimeInterpolator);
        return set;
    }
}
