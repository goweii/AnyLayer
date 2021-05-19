package per.goweii.anylayer.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ContainerLayout extends FrameLayout {
    private final GestureDetector mGestureDetector;

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
