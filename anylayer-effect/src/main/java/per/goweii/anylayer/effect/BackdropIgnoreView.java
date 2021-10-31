package per.goweii.anylayer.effect;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BackdropIgnoreView extends FrameLayout {
    private final List<WeakReference<BackdropBlurView>> mBackdropBlurViews = new LinkedList<>();

    public BackdropIgnoreView(@NonNull Context context) {
        super(context);
    }

    public BackdropIgnoreView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BackdropIgnoreView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void clearBackdropBlurViews() {
        mBackdropBlurViews.clear();
    }

    public boolean containBackdropBlurView(@Nullable BackdropBlurView backdropBlurView) {
        Iterator<WeakReference<BackdropBlurView>> iterator = mBackdropBlurViews.iterator();
        boolean contain = false;
        while (iterator.hasNext()) {
            WeakReference<BackdropBlurView> reference = iterator.next();
            if (reference.get() == null) {
                iterator.remove();
            } else {
                if (reference.get() == backdropBlurView) {
                    contain = true;
                }
            }
        }
        return contain;
    }

    public void addBackdropBlurView(@Nullable BackdropBlurView backdropBlurView) {
        if (containBackdropBlurView(backdropBlurView)) {
            return;
        }
        if (backdropBlurView != null) {
            mBackdropBlurViews.add(new WeakReference<>(backdropBlurView));
        }
    }

    @Override
    public void draw(Canvas canvas) {
        boolean hasRendering = false;
        Iterator<WeakReference<BackdropBlurView>> iterator = mBackdropBlurViews.iterator();
        while (iterator.hasNext()) {
            WeakReference<BackdropBlurView> reference = iterator.next();
            if (reference.get() == null) {
                iterator.remove();
            } else {
                if (!hasRendering && reference.get().isRendering()) {
                    hasRendering = true;
                }
            }
        }
        if (!hasRendering) {
            super.draw(canvas);
        }
    }
}
