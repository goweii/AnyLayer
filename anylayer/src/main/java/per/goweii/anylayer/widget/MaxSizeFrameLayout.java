package per.goweii.anylayer.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.R;

public class MaxSizeFrameLayout extends FrameLayout {

    private int mMaxWidth = -1;
    private int mMaxHeight = -1;

    private OnDispatchTouchListener mOnDispatchTouchListener = null;

    public MaxSizeFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public MaxSizeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaxSizeFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MaxSizeFrameLayout);
        mMaxWidth = (int) array.getDimension(R.styleable.MaxSizeFrameLayout_android_maxWidth, mMaxWidth);
        mMaxHeight = (int) array.getDimension(R.styleable.MaxSizeFrameLayout_android_maxHeight, mMaxHeight);
        array.recycle();
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
        requestLayout();
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
        requestLayout();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (mOnDispatchTouchListener != null) {
            mOnDispatchTouchListener.onDispatch(e);
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int newWidthSize = widthSize;
        int newHeightSize = heightSize;
        if (mMaxWidth >= 0) {
            newWidthSize = Math.min(mMaxWidth, widthSize);
        }
        if (mMaxHeight >= 0) {
            newHeightSize = Math.min(mMaxHeight, heightSize);
        }
        int newWidthSpec = MeasureSpec.makeMeasureSpec(newWidthSize, widthMode);
        int newHeightSpec = MeasureSpec.makeMeasureSpec(newHeightSize, heightMode);
        super.onMeasure(newWidthSpec, newHeightSpec);
    }

    public void setOnDispatchTouchListener(@Nullable OnDispatchTouchListener onDispatchTouchListener) {
        this.mOnDispatchTouchListener = onDispatchTouchListener;
    }

    public interface OnDispatchTouchListener {
        void onDispatch(MotionEvent e);
    }
}
