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
    private int mSnapEdge = Edge.LEFT | Edge.TOP | Edge.RIGHT | Edge.BOTTOM;

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
        }

        @Override
        public void onViewReleased(@NonNull View child, float xvel, float yvel) {
            super.onViewReleased(child, xvel, yvel);
            int centerX = child.getLeft() + child.getWidth() / 2;
            int centerY = child.getTop() + child.getHeight() / 2;
            int finalLeft;
            int finalTop;
            boolean snapEdgeLeft = (mSnapEdge & Edge.LEFT) != 0;
            boolean snapEdgeRight = (mSnapEdge & Edge.RIGHT) != 0;
            boolean snapEdgeTop = (mSnapEdge & Edge.TOP) != 0;
            boolean snapEdgeBottom = (mSnapEdge & Edge.BOTTOM) != 0;
            if (snapEdgeLeft && snapEdgeRight) {
                if (centerX <= getWidth() / 2) {
                    finalLeft = 0;
                } else {
                    finalLeft = getWidth() - child.getWidth();
                }
            } else if (snapEdgeLeft) {
                finalLeft = 0;
            } else if (snapEdgeRight) {
                finalLeft = getWidth() - child.getWidth();
            } else {
                finalLeft = centerX;
            }
            if (snapEdgeTop && snapEdgeBottom) {
                if (centerY <= getHeight() / 2) {
                    finalTop = 0;
                } else {
                    finalTop = getHeight() - child.getHeight();
                }
            } else if (snapEdgeTop) {
                finalTop = 0;
            } else if (snapEdgeBottom) {
                finalTop = getHeight() - child.getHeight();
            } else {
                finalTop = centerY;
            }
            if (finalLeft != child.getLeft() && finalTop != child.getTop()) {
                if (child.getLeft() < 0 || child.getRight() > getWidth()) {
                    finalTop = child.getTop();
                }
                if (child.getTop() < 0 || child.getBottom() > getHeight()) {
                    finalLeft = child.getLeft();
                }
                int moveLeft = Math.abs(finalLeft - child.getLeft());
                int moveTop = Math.abs(finalTop - child.getTop());
                if (moveLeft < moveTop) {
                    finalTop = child.getTop();
                } else {
                    finalLeft = child.getLeft();
                }
            }
            finalLeft = Utils.intRange(finalLeft, 0, getWidth() - child.getWidth());
            finalTop = Utils.intRange(finalTop, 0, getHeight() - child.getHeight());
            mDragHelper.smoothSlideViewTo(child, finalLeft, finalTop);
            invalidate();
        }
    }
}
