package per.goweii.anylayer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MaxSizeFrameLayout extends FrameLayout {

    private int mMaxWidth = -1;
    private int mMaxHeight = -1;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int newWidthSize = widthSize;
        int newHeightSize = heightSize;
        int newWidthMode = widthMode;
        int newHeightMode = heightMode;
        if (mMaxWidth >= 0) {
            newWidthSize = Math.min(mMaxWidth, widthSize);
        }
        if (mMaxHeight >= 0) {
            newHeightSize = Math.min(mMaxHeight, heightSize);
        }
        int newWidthSpec = MeasureSpec.makeMeasureSpec(newWidthSize, newWidthMode);
        int newHeightSpec = MeasureSpec.makeMeasureSpec(newHeightSize, newHeightMode);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            final int childWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                    getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
            final int childHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                    getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin, lp.height);
            child.measure(childWidthSpec, childHeightSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
