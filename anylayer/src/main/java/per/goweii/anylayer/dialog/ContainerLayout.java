package per.goweii.anylayer.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContainerLayout extends FrameLayout {
    private final GestureDetector mGestureDetector;

    private boolean mForceFocusInside = false;
    private boolean mHandleTouchEvent = false;

    private OnTouchedListener mOnTouchedListener = null;
    private OnTappedListener mOnTappedListener = null;

    public ContainerLayout(@NonNull Context context) {
        this(context, null);
    }

    public ContainerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mGestureDetector = new GestureDetector(context, new OnGestureListener());
    }

    public void setForceFocusInside(boolean forceFocusInside) {
        mForceFocusInside = forceFocusInside;
        if (forceFocusInside) {
            if (!hasFocus()) {
                requestFocus();
            }
        }
    }

    @Override
    public View focusSearch(View focused, int direction) {
        if (!mForceFocusInside) {
            return super.focusSearch(focused, direction);
        }
        FocusFinder focusFinder = FocusFinder.getInstance();
        View nextFocus = focusFinder.findNextFocus(this, focused, direction);
        if (nextFocus != null) {
            return nextFocus;
        }
        return this;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    public void setHandleTouchEvent(boolean handleTouchEvent) {
        this.mHandleTouchEvent = handleTouchEvent;
    }

    public void setOnTouchedListener(@Nullable OnTouchedListener onTouchedListener) {
        this.mOnTouchedListener = onTouchedListener;
    }

    public void setOnTappedListener(@Nullable OnTappedListener onTappedListener) {
        this.mOnTappedListener = onTappedListener;
    }

    private class OnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            if (mOnTouchedListener != null) {
                mOnTouchedListener.onTouched();
            }
            return mHandleTouchEvent;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mOnTappedListener != null) {
                mOnTappedListener.onTapped();
            }
            return true;
        }
    }

    public interface OnTouchedListener {
        void onTouched();
    }

    public interface OnTappedListener {
        void onTapped();
    }
}
