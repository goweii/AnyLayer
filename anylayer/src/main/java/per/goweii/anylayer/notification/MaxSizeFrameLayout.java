package per.goweii.anylayer.notification;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.R;

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
}
