package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.customview.widget.ViewDragHelper;

/**
 * @author CuiZhen
 * @date 2019/12/1
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
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
        int finalLeft = view.getLeft();
        int finalTop = view.getTop();
        int currEdge = calcCurrEdge(view);
        if (currEdge == 0) {
            int centerX = view.getLeft() + view.getWidth() / 2;
            int centerY = view.getTop() + view.getHeight() / 2;
            boolean snapEdgeLeft = (mSnapEdge & Edge.LEFT) != 0;
            boolean snapEdgeRight = (mSnapEdge & Edge.RIGHT) != 0;
            boolean snapEdgeTop = (mSnapEdge & Edge.TOP) != 0;
            boolean snapEdgeBottom = (mSnapEdge & Edge.BOTTOM) != 0;
            if (snapEdgeLeft && snapEdgeRight) {
                if (centerX <= getWidth() / 2) {
                    finalLeft = 0;
                } else {
                    finalLeft = getWidth() - view.getWidth();
                }
            } else if (snapEdgeLeft) {
                finalLeft = 0;
            } else if (snapEdgeRight) {
                finalLeft = getWidth() - view.getWidth();
            } else {
                finalLeft = view.getLeft();
            }
            if (snapEdgeTop && snapEdgeBottom) {
                if (centerY <= getHeight() / 2) {
                    finalTop = 0;
                } else {
                    finalTop = getHeight() - view.getHeight();
                }
            } else if (snapEdgeTop) {
                finalTop = 0;
            } else if (snapEdgeBottom) {
                finalTop = getHeight() - view.getHeight();
            } else {
                finalTop = view.getTop();
            }
            if (finalLeft != view.getLeft() && finalTop != view.getTop()) {
                if (view.getLeft() < 0 || view.getRight() > getWidth()) {
                    finalTop = view.getTop();
                }
                if (view.getTop() < 0 || view.getBottom() > getHeight()) {
                    finalLeft = view.getLeft();
                }
                int moveLeft = Math.abs(finalLeft - view.getLeft());
                int moveTop = Math.abs(finalTop - view.getTop());
                if (moveLeft < moveTop) {
                    finalTop = view.getTop();
                } else {
                    finalLeft = view.getLeft();
                }
            }
            finalLeft = Utils.intRange(finalLeft, 0, getWidth() - view.getWidth());
            finalTop = Utils.intRange(finalTop, 0, getHeight() - view.getHeight());
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
        boolean onLeft = view.getLeft() == 0;
        boolean onRight = view.getRight() == getWidth();
        boolean onTop = view.getTop() == 0;
        boolean onBottom = view.getBottom() == getHeight();
        int currEdge = 0;
        if (canLeft && onLeft) currEdge |= Edge.LEFT;
        if (canRight && onRight) currEdge |= Edge.RIGHT;
        if (canTop && onTop) currEdge |= Edge.TOP;
        if (canBottom && onBottom) currEdge |= Edge.BOTTOM;
        return currEdge;
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
    }

    private void onStart(@NonNull View view) {
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

    private class DragCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return true;
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            if (mOutside) {
                return getWidth() + child.getWidth();
            } else {
                return getWidth() - child.getWidth();
            }
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            if (mOutside) {
                return getHeight() + child.getHeight();
            } else {
                return getHeight() - child.getHeight();
            }
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            onStart(capturedChild);
            mSettlingView = null;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (mOutside) {
                int min = -child.getWidth();
                int max = getWidth();
                return Utils.intRange(left, min, max);
            } else {
                int min = 0;
                int max = getWidth() - child.getWidth();
                return Utils.intRange(left, min, max);
            }
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (mOutside) {
                int min = -child.getHeight();
                int max = getHeight();
                return Utils.intRange(top, min, max);
            } else {
                int min = 0;
                int max = getHeight() - child.getHeight();
                return Utils.intRange(top, min, max);
            }
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
