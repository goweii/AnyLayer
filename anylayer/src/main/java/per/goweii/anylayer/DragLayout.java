package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

/**
 * @author CuiZhen
 * @date 2019/12/1
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class DragLayout extends FrameLayout {

    private final ViewDragHelper mDragHelper;
    private DragStyle mDragStyle = DragStyle.None;
    private OnDragListener mOnDragListener = null;

    private List<View> mInnerScrollViews;
    private boolean mHandleDragEvent = false;
    private float mDownX;
    private float mDownY;
    private int mLeft;
    private int mTop;

    private float mDragFraction = 0F;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, new DragCallback());
    }

    public void setOnDragListener(OnDragListener onDragListener) {
        mOnDragListener = onDragListener;
    }

    public void setDragStyle(DragStyle dragStyle) {
        mDragStyle = dragStyle;
    }

    private boolean isEnable() {
        return mDragStyle != DragStyle.None;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mHandleDragEvent) {
                    float offX = ev.getRawX() - mDownX;
                    float offY = ev.getRawY() - mDownY;
                    boolean dragged = offX * offX + offY * offY > mDragHelper.getTouchSlop();
                    if (dragged) {
                        switch (mDragStyle) {
                            case Top:
                                if (offY < 0 && !DragCompat.canViewScrollDown(mInnerScrollViews, mDownX, mDownY, false)) {
                                    mHandleDragEvent = false;
                                    requestDisallowInterceptTouchEvent(false);
                                }
                                break;
                            case Bottom:
                                if (offY > 0 && !DragCompat.canViewScrollUp(mInnerScrollViews, mDownX, mDownY, false)) {
                                    mHandleDragEvent = false;
                                    requestDisallowInterceptTouchEvent(false);
                                }
                                break;
                            case Left:
                                if (offX < 0 && !DragCompat.canViewScrollRight(mInnerScrollViews, mDownX, mDownY, false)) {
                                    mHandleDragEvent = false;
                                    requestDisallowInterceptTouchEvent(false);
                                }
                                break;
                            case Right:
                                if (offX > 0 && !DragCompat.canViewScrollLeft(mInnerScrollViews, mDownX, mDownY, false)) {
                                    mHandleDragEvent = false;
                                    requestDisallowInterceptTouchEvent(false);
                                }
                                break;
                            case None:
                            default:
                                break;
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnable()) {
            mHandleDragEvent = false;
            return super.onInterceptTouchEvent(ev);
        }
        mHandleDragEvent = mDragHelper.shouldInterceptTouchEvent(ev);
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            default:
                break;
        }
        return mHandleDragEvent || super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEnable()) {
            mDragHelper.processTouchEvent(ev);
        }
        return mHandleDragEvent;
    }

    @Override
    public void computeScroll() {
        if (!isEnable()) {
            return;
        }
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mInnerScrollViews = DragCompat.findAllScrollViews(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            if (getChildCount() > 0) {
                mLeft = getChildAt(0).getLeft();
                mTop = getChildAt(0).getTop();
            }
        }
    }

    private class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return isEnable();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            switch (mDragStyle) {
                case Left:
                    return mLeft + child.getWidth();
                case Right:
                    return getWidth() - mLeft;
                case Top:
                case Bottom:
                case None:
                default:
                    return 0;
            }
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            switch (mDragStyle) {
                case Top:
                    return mTop + child.getHeight();
                case Bottom:
                    return getHeight() - mTop;
                case Left:
                case Right:
                case None:
                default:
                    return 0;
            }
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            mDragFraction = 0F;
            if (mOnDragListener != null) {
                mOnDragListener.onDragStart();
            }
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            switch (mDragStyle) {
                case Left:
                    if (DragCompat.canViewScrollRight(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mLeft;
                    }
                    if (left > mLeft) {
                        return mLeft;
                    }
                    final int l = mLeft + child.getWidth();
                    if (left < -l) {
                        return -l;
                    }
                    return left;
                case Right:
                    if (DragCompat.canViewScrollLeft(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mLeft;
                    }
                    if (left > getWidth()) {
                        return getWidth();
                    }
                    if (left < mLeft) {
                        return mLeft;
                    }
                    return left;
                case Top:
                case Bottom:
                case None:
                default:
                    return mLeft;
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            switch (mDragStyle) {
                case Top:
                    if (DragCompat.canViewScrollDown(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mTop;
                    }
                    if (top > mTop) {
                        return mTop;
                    }
                    final int t = mTop + child.getHeight();
                    if (top < -t) {
                        return -t;
                    }
                    return top;
                case Bottom:
                    if (DragCompat.canViewScrollUp(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mTop;
                    }
                    if (top > getHeight()) {
                        return getHeight();
                    }
                    if (top < mTop) {
                        return mTop;
                    }
                    return top;
                case Left:
                case Right:
                case None:
                default:
                    return mTop;
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            switch (mDragStyle) {
                case Left:
                case Right:
                    final float xoff = Math.abs(left - mLeft);
                    final float xmax = getViewHorizontalDragRange(changedView);
                    mDragFraction = xoff / xmax;
                    break;
                case Top:
                case Bottom:
                    final float yoff = Math.abs(top - mTop);
                    final float ymax = getViewVerticalDragRange(changedView);
                    mDragFraction = yoff / ymax;
                    break;
                case None:
                default:
                    break;
            }
            if (mDragFraction < 0) {
                mDragFraction = 0;
            } else if (mDragFraction > 1) {
                mDragFraction = 1;
            }
            if (mOnDragListener != null) {
                mOnDragListener.onDragging(mDragFraction);
                if (mDragFraction == 1) mOnDragListener.onDragEnd();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            float dismissFactor = 0.5f;
            boolean isDismiss = judgeDismissBySpeed(xvel, yvel) || mDragFraction >= dismissFactor;
            int l = mLeft;
            int t = mTop;
            if (isDismiss) {
                switch (mDragStyle) {
                    case Left:
                        l = -(mLeft + releasedChild.getWidth());
                        break;
                    case Right:
                        l = getWidth();
                        break;
                    case Top:
                        t = -(mTop + releasedChild.getHeight());
                        break;
                    case Bottom:
                        t = getHeight();
                        break;
                    case None:
                    default:
                        break;
                }
            }
            mDragHelper.settleCapturedViewAt(l, t);
            invalidate();
        }

        private boolean judgeDismissBySpeed(float xvel, float yvel) {
            float velocityLimit = 2000f;
            switch (mDragStyle) {
                case Left:
                    return xvel < -velocityLimit;
                case Right:
                    return xvel > velocityLimit;
                case Top:
                    return yvel < -velocityLimit;
                case Bottom:
                    return yvel > velocityLimit;
                case None:
                default:
                    break;
            }
            return false;
        }
    }

    public enum DragStyle {
        None, Left, Top, Right, Bottom,
    }

    public interface OnDragListener {
        void onDragStart();

        void onDragging(float f);

        void onDragEnd();
    }
}
