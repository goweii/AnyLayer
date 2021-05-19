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

        public ScaleAttr setFrom(float from) {
            return setFrom(from, from);
        }

        public ScaleAttr setFrom(float fromX, float fromY) {
            this.fromX = fromX;
            this.fromY = fromY;
            return this;
        }

        public ScaleAttr setTo(float to) {
            return setTo(to, to);
        }

        public ScaleAttr setTo(float toX, float toY) {
            this.toX = toX;
            this.toY = toY;
            return this;
        }

        public ScaleAttr setPivot(float pivot) {
            return setPivot(pivot, pivot);
        }

        public ScaleAttr setPivot(float pivotX, float pivotY) {
            this.usePivotPercent = false;
            this.pivotX = pivotX;
            this.pivotY = pivotY;
            return this;
        }

        public ScaleAttr setPivotPercent(float pivotPercent) {
            return setPivotPercent(pivotPercent, pivotPercent);
        }

        public ScaleAttr setPivotPercent(float pivotPercentX, float pivotPercentY) {
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

        public ScaleAttr setTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setXTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setYTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setXInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setXOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setYInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public ScaleAttr setYOutTimeInterpolator(TimeInterpolator timeInterpolator) {
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

        public TranslationAttr setFrom(float from) {
            return setFrom(from, from);
        }

        public TranslationAttr setFrom(float fromX, float fromY) {
            this.useFromPercent = false;
            this.fromX = fromX;
            this.fromY = fromY;
            return this;
        }

        public TranslationAttr setFromPercent(float fromPercent) {
            return setFromPercent(fromPercent, fromPercent);
        }

        public TranslationAttr setFromPercent(float fromPercentX, float fromPercentY) {
            this.useFromPercent = true;
            this.fromPercentX = fromPercentX;
            this.fromPercentY = fromPercentY;
            return this;
        }

        public TranslationAttr setTo(float to) {
            return setTo(to, to);
        }

        public TranslationAttr setTo(float toX, float toY) {
            this.useToPercent = false;
            this.toX = toX;
            this.toY = toY;
            return this;
        }

        public TranslationAttr setToPercent(float toPercent) {
            return setToPercent(toPercent, toPercent);
        }

        public TranslationAttr setToPercent(float toPercentX, float toPercentY) {
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

        public TranslationAttr setTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setXTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setYTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            this.yOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setXInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xInTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setXOutTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.xOutTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setYInTimeInterpolator(TimeInterpolator timeInterpolator) {
            this.yInTimeInterpolator = timeInterpolator;
            return this;
        }

        public TranslationAttr setYOutTimeInterpolator(TimeInterpolator timeInterpolator) {
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

    private final List<Attr> attrs = new ArrayList<>();
    private TimeInterpolator inTimeInterpolator = null;
    private TimeInterpolator outTimeInterpolator = null;

    public CommonAnimatorCreator addAttr(Attr attr) {
        this.attrs.add(attr);
        return this;
    }

    public CommonAnimatorCreator setTimeInterpolator(TimeInterpolator timeInterpolator) {
        this.inTimeInterpolator = timeInterpolator;
        this.outTimeInterpolator = timeInterpolator;
        return this;
    }

    public CommonAnimatorCreator setInTimeInterpolator(TimeInterpolator timeInterpolator) {
        this.inTimeInterpolator = timeInterpolator;
        return this;
    }

    public CommonAnimatorCreator setOutTimeInterpolator(TimeInterpolator timeInterpolator) {
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
