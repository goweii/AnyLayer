package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Create by cuizhen on {2019/12/3}
 */
public class ContainerLayout extends FrameLayout {

    private OnTouchedListener mOnTouchedListener = null;

    public ContainerLayout(Context context) {
        this(context, null);
    }

    public ContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContainerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void setOnTouchedListener(OnTouchedListener onTouchedListener) {
        this.mOnTouchedListener = onTouchedListener;
    }

    public interface OnTouchedListener {
        void onTouched();
    }
}
