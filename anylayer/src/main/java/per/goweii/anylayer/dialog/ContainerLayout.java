package per.goweii.anylayer.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ContainerLayout extends FrameLayout {

    private OnTouchedListener mOnTouchedListener = null;

    public ContainerLayout(@NonNull Context context) {
        this(context, null);
    }

    public ContainerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mOnTouchedListener != null) {
                    mOnTouchedListener.onTouched();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void setOnTouchedListener(@Nullable OnTouchedListener onTouchedListener) {
        this.mOnTouchedListener = onTouchedListener;
    }

    public interface OnTouchedListener {
        void onTouched();
    }
}
