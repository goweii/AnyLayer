package per.goweii.anylayer.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

import per.goweii.anylayer.R;
import per.goweii.anylayer.utils.Utils;

public class DragLayout extends FrameLayout {

    private final ViewDragHelper mDragHelper;
    private boolean mHandleTouchEvent = false;

    private boolean mOutside = true;
    private int mSnapEdge = Edge.LEFT | Edge.RIGHT;

    private OnDragListener mOnDragListener = null;

    private View mSettlingView = null;

    public static class Edge {
        public static final int LEFT = 1;
        public static final int TOP = 1 << 1;
        public static final int RIGHT = 1 << 2;
        public static final int BOTTOM = 1 << 3;
    }

    public DragLayout(@NonNull Context context) {
        this(context, null);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClipToPadding(false);
        mDragHelper = ViewDragHelper.create(this, new DragCallback());
    }

    public void setOutside(boolean outside) {
        mOutside = outside;
    }

    public void setSnapEdge(int snapEdge) {
        mSnapEdge = snapEdge;
    }

    public void setOnDragListener(OnDragListener listener) {
        mOnDragListener = listener;
    }

    public void goEdge(@NonNull View view) {
        final int currLeft = view.getLeft();
        final int currTop = view.getTop();
        int finalLeft = currLeft;
        int finalTop = currTop;
        int currEdge = calcCurrEdge(view);
        if (currEdge == 0) {
            boolean snapEdgeLeft = (mSnapEdge & Edge.LEFT) != 0;
            boolean snapEdgeRight = (mSnapEdge & Edge.RIGHT) != 0;
            boolean snapEdgeTop = (mSnapEdge & Edge.TOP) != 0;
            boolean snapEdgeBottom = (mSnapEdge & Edge.BOTTOM) != 0;
            final int minLeft = getViewLeftMinInside(view);
            final int maxLeft = getViewLeftMaxInside(view);
            final int minTop = getViewTopMinInside(view);
            final int maxTop = getViewTopMaxInside(view);
            if (snapEdgeLeft && snapEdgeRight) {
                if (currLeft < (maxLeft - minLeft) / 2) {
                    finalLeft = minLeft;
                } else {
                    finalLeft = maxLeft;
                }
            } else if (snapEdgeLeft) {
                finalLeft = minLeft;
            } else if (snapEdgeRight) {
                finalLeft = maxLeft;
            } else {
                finalLeft = currLeft;
            }
            if (snapEdgeTop && snapEdgeBottom) {
                if (currTop < (maxTop - minTop) / 2) {
                    finalTop = minTop;
                } else {
                    finalTop = maxTop;
                }
            } else if (snapEdgeTop) {
                finalTop = minTop;
            } else if (snapEdgeBottom) {
                finalTop = maxTop;
            } else {
                finalTop = currTop;
            }
            if (finalLeft != currLeft && finalTop != currTop) {
                if (currLeft < minLeft || currLeft > maxLeft) {
                    finalTop = currTop;
                }
                if (currTop < minTop || currTop > maxTop) {
                    finalLeft = currLeft;
                }
                int moveLeft = Math.abs(finalLeft - currLeft);
                int moveTop = Math.abs(finalTop - currTop);
                if (moveLeft < moveTop) {
                    finalTop = currTop;
                } else {
                    finalLeft = currLeft;
                }
            }
            finalLeft = Utils.intRange(finalLeft, minLeft, maxLeft);
            finalTop = Utils.intRange(finalTop, minTop, maxTop);
        }
        mSettlingView = view;
        mDragHelper.smoothSlideViewTo(view, finalLeft, finalTop);
        invalidate();
    }

    public int calcCurrEdge(@NonNull View view) {
        boolean canLeft = (mSnapEdge & Edge.LEFT) != 0;
        boolean canRight = (mSnapEdge & Edge.RIGHT) != 0;
        boolean canTop = (mSnapEdge & Edge.TOP) != 0;
        boolean canBottom = (mSnapEdge & Edge.BOTTOM) != 0;
        boolean onLeft = view.getLeft() == getViewLeftMinInside(view);
        boolean onRight = view.getLeft() == getViewLeftMaxInside(view);
        boolean onTop = view.getTop() == getViewTopMinInside(view);
        boolean onBottom = view.getTop() == getViewTopMaxInside(view);
        int currEdge = 0;
        if (canLeft && onLeft) currEdge |= Edge.LEFT;
        if (canRight && onRight) currEdge |= Edge.RIGHT;
        if (canTop && onTop) currEdge |= Edge.TOP;
        if (canBottom && onBottom) currEdge |= Edge.BOTTOM;
        return currEdge;
    }

    public int getViewLeftMinInside(@NonNull View view) {
        return getPaddingLeft() + Utils.getViewMarginLeft(view);
    }

    public int getViewLeftMin(@NonNull View view) {
        if (mOutside) {
            return -(view.getWidth() + Utils.getViewMarginRight(view));
        } else {
            return getViewLeftMinInside(view);
        }
    }

    public int getViewLeftMaxInside(@NonNull View view) {
        return getWidth() - getPaddingRight() - Utils.getViewMarginRight(view) - view.getWidth();
    }

    public int getViewLeftMax(@NonNull View view) {
        if (mOutside) {
            return getWidth() + Utils.getViewMarginLeft(view);
        } else {
            return getViewLeftMaxInside(view);
        }
    }

    public int getViewTopMinInside(@NonNull View view) {
        return getPaddingTop() + Utils.getViewMarginTop(view);
    }

    public int getViewTopMin(@NonNull View view) {
        if (mOutside) {
            return -(view.getHeight() + Utils.getViewMarginBottom(view));
        } else {
            return getViewTopMinInside(view);
        }
    }

    public int getViewTopMaxInside(@NonNull View view) {
        return getHeight() - getPaddingBottom() - Utils.getViewMarginBottom(view) - view.getHeight();
    }

    public int getViewTopMax(@NonNull View view) {
        if (mOutside) {
            return getHeight() + Utils.getViewMarginTop(view);
        } else {
            return getViewTopMaxInside(view);
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        mHandleTouchEvent = shouldIntercept;
        return shouldIntercept;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        if (mHandleTouchEvent) {
            mDragHelper.processTouchEvent(ev);
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        } else {
            if (mSettlingView != null) {
                onStop(mSettlingView);
                mSettlingView = null;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            updateViewByPositionX(view);
            updateViewByPositionY(view);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            updateViewPositionTag(view);
        }
    }

    public void onStart(@NonNull View view) {
        if (mOnDragListener != null) {
            mOnDragListener.onStart(view);
        }
    }

    private void onDragging(@NonNull View view) {
        if (mOnDragListener != null) {
            mOnDragListener.onDragging(view);
        }
    }

    private void onRelease(@NonNull View view) {
        goEdge(view);
        if (mOnDragListener != null) {
            mOnDragListener.onRelease(view);
        }
    }

    private void onStop(@NonNull View view) {
        if (mOnDragListener != null) {
            mOnDragListener.onStop(view);
        }
    }

    private void updateViewPositionTag(@NonNull View view) {
        PointF position = (PointF) view.getTag(R.id.anylayer_overlay_position);
        if (position == null) {
            position = new PointF();
            view.setTag(R.id.anylayer_overlay_position, position);
        }
        float px = calcViewPositionX(view);
        float py = calcViewPositionY(view);
        position.set(px, py);
    }

    private float calcViewPositionX(@NonNull View view) {
        float minX = getViewLeftMinInside(view) + view.getWidth() / 2F;
        float maxX = getViewLeftMaxInside(view) + view.getWidth() / 2F;
        float currX = view.getLeft() + view.getWidth() / 2F;
        return (currX - minX) / (maxX - minX);
    }

    private float calcViewPositionY(@NonNull View view) {
        float minY = getViewTopMinInside(view) + view.getHeight() / 2F;
        float maxY = getViewTopMaxInside(view) + view.getHeight() / 2F;
        float currY = view.getTop() + view.getHeight() / 2F;
        return (currY - minY) / (maxY - minY);
    }

    private void updateViewByPositionX(@NonNull View view) {
        PointF position = (PointF) view.getTag(R.id.anylayer_overlay_position);
        if (position != null) {
            if (position.x != calcViewPositionX(view)) {
                float currX = view.getLeft() + view.getWidth() / 2F;
                float minX = getViewLeftMinInside(view) + view.getWidth() / 2F;
                float maxX = getViewLeftMaxInside(view) + view.getWidth() / 2F;
                float toX = position.x * (maxX - minX) + minX;
                view.offsetLeftAndRight((int) (toX - currX));
            }
        }
    }

    private void updateViewByPositionY(@NonNull View view) {
        PointF position = (PointF) view.getTag(R.id.anylayer_overlay_position);
        if (position != null) {
            if (position.y != calcViewPositionY(view)) {
                float currY = view.getTop() + view.getHeight() / 2F;
                float minY = getViewTopMinInside(view) + view.getHeight() / 2F;
                float maxY = getViewTopMaxInside(view) + view.getHeight() / 2F;
                float toY = position.y * (maxY - minY) + minY;
                view.offsetTopAndBottom((int) (toY - currY));
            }
        }
    }

    private class DragCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            int min = getViewLeftMin(child);
            int max = getViewLeftMax(child);
            return max - min;
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            int min = getViewTopMin(child);
            int max = getViewTopMax(child);
            return max - min;
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            onStart(capturedChild);
            mSettlingView = null;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            int min = getViewLeftMin(child);
            int max = getViewLeftMax(child);
            return Utils.intRange(left, min, max);
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            int min = getViewTopMin(child);
            int max = getViewTopMax(child);
            return Utils.intRange(top, min, max);
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            onDragging(changedView);
        }

        @Override
        public void onViewReleased(@NonNull View child, float xvel, float yvel) {
            super.onViewReleased(child, xvel, yvel);
            onRelease(child);
        }
    }

    public interface OnDragListener {
        void onStart(@NonNull View view);

        void onDragging(@NonNull View view);

        void onRelease(@NonNull View view);

        void onStop(@NonNull View view);
    }
}
