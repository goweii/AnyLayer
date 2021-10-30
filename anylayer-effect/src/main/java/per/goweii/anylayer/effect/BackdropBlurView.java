package per.goweii.anylayer.effect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.Arrays;

import per.goweii.visualeffect.blur.BlurEffect;
import per.goweii.visualeffect.blur.RSBlurEffect;
import per.goweii.visualeffect.view.BackdropVisualEffectFrameLayout;
import per.goweii.visualeffect.view.OutlineBuilder;

public class BackdropBlurView extends BackdropVisualEffectFrameLayout {
    private final RoundedOutlineBuilder mOutlineBuilder = new RoundedOutlineBuilder();

    private float mCornerRadius = 0F;
    private float mBlurRadius = 8F;
    private float mBlurPercent = 0F;

    public BackdropBlurView(Context context) {
        this(context, null);
    }

    public BackdropBlurView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BackdropBlurView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setShowDebugInfo(false);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float simple = getSimpleSize();
        float radius;
        if (mBlurPercent > 0) {
            radius = Math.min(getWidth(), getHeight()) * mBlurPercent;
            mBlurPercent = 0F;
        } else {
            radius = mBlurRadius;
        }
        if (radius > 25) {
            simple = simple * (radius / 25);
            radius = 25;
        } else if (radius < 0) {
            radius = 0;
        }
        if (mBlurRadius != radius) {
            mBlurRadius = radius;
        }
        if (getSimpleSize() != simple) {
            setSimpleSize(simple);
        }
        if (radius == 0) {
            if (getVisualEffect() != null) {
                setVisualEffect(null);
            }
        } else {
            if (!(getVisualEffect() instanceof BlurEffect) || ((BlurEffect) getVisualEffect()).getRadius() != radius) {
                setVisualEffect(new RSBlurEffect(getContext(), radius));
            }
        }
        super.draw(canvas);
    }

    public void setCornerRadius(float cornerRadius) {
        if (mCornerRadius != cornerRadius) {
            mCornerRadius = cornerRadius;
            if (mCornerRadius == 0) {
                if (getOutlineBuilder() != null) {
                    setOutlineBuilder(null);
                }
            } else {
                if (getOutlineBuilder() == null) {
                    setOutlineBuilder(mOutlineBuilder);
                } else {
                    mOutlineBuilder.invalidateOutline();
                }
            }
        }
    }

    public void setBlurPercent(float blurPercent) {
        if (mBlurPercent != blurPercent) {
            mBlurPercent = blurPercent;
            invalidate();
        }
    }

    public void setBlurRadius(float blurRadius) {
        if (mBlurRadius != blurRadius) {
            mBlurRadius = blurRadius;
            invalidate();
        }
    }

    private class RoundedOutlineBuilder extends OutlineBuilder {
        private final RectF rect = new RectF();
        private final float[] radii = new float[8];

        @Override
        public void buildOutline(@NonNull View view, @NonNull Path path) {
            float minSide = Math.min(view.getWidth(), view.getHeight());
            float r = Math.min(mCornerRadius, minSide / 2F);
            Arrays.fill(radii, r);
            rect.set(0, 0, view.getWidth(), view.getHeight());
            path.addRoundRect(rect, radii, Path.Direction.CW);
        }
    }
}
