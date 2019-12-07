package per.goweii.anylayer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Create by cuizhen on {2019/12/3}
 */
public class BackgroundView extends ImageView {

    private OnTouchedListener mOnTouchedListener = null;

    public BackgroundView(Context context) {
        this(context, null);
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

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
        return false;
    }

    public void setOnTouchedListener(OnTouchedListener onTouchedListener) {
        this.mOnTouchedListener = onTouchedListener;
    }

    public interface OnTouchedListener {
        void onTouched();
    }
}
