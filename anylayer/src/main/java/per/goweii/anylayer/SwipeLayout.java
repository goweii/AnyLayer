package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ScrollingView;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import java.util.ArrayList;
import java.util.List;

public class SwipeLayout extends FrameLayout implements NestedScrollingParent3 {
    public static final int SWIPE_NONE = 0;
    public static final int SWIPE_LEFT = 1;
    public static final int SWIPE_TOP = 1 << 1;
    public static final int SWIPE_RIGHT = 1 << 2;
    public static final int SWIPE_BOTTOM = 1 << 3;


    private final ViewDragHelper mDragHelper;
    private final Scroller mScroller;
    private final NestedScrollingParentHelper mNestedHelper;

    private int mDismissDuration = 300;
    private float mDismissVelocity = 2000F;
    private float mDismissFraction = 0.5F;
    private int mSwipeDirection = SWIPE_NONE;
    private DragStyle mDragStyle = DragStyle.None;
    private OnSwipeListener mOnSwipeListener = null;

    private List<View> mInnerScrollViews = new ArrayList<>(0);
    private boolean mHandleTouchEvent = false;
    private float mDownX = 0F;
    private float mDownY = 0F;
    private int mLeft = 0;
    private int mTop = 0;

    @FloatRange(from = 0F, to = 1F)
    private float mSwipeFraction = 0F;

    private boolean mUsingNested = false;

    public SwipeLayout(@NonNull Context context) {
        this(context, null);
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, new DragCallback());
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mNestedHelper = new NestedScrollingParentHelper(this);
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        mOnSwipeListener = onSwipeListener;
    }

    public void setSwipeDirection(int swipeDirection) {
        mSwipeDirection = swipeDirection;
    }

    public void setDragStyle(@NonNull DragStyle dragStyle) {
        mDragStyle = dragStyle;
    }

    public boolean isEnable() {
        return mDragStyle != DragStyle.None;
    }

    public void setDismissDuration(int dismissDuration) {
        this.mDismissDuration = dismissDuration;
    }

    public void setDismissFraction(float dismissFraction) {
        this.mDismissFraction = dismissFraction;
    }

    public void setDismissVelocity(float dismissVelocity) {
        this.mDismissVelocity = dismissVelocity;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
        if (!isEnable()) {
            mHandleTouchEvent = false;
            return super.onInterceptTouchEvent(ev);
        }
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDragHelper.abort();
                mScroller.abortAnimation();
                mUsingNested = false;
                mDownX = ev.getRawX();
                mDownY = ev.getRawY();
                break;
            default:
                break;
        }
        Log.d("DragLayout", "onInterceptTouchEvent->mDownY=$mDownY，mDownY=$mDownY");
        if (mUsingNested) {
            mHandleTouchEvent = false;
            return super.onInterceptTouchEvent(ev);
        }
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        mHandleTouchEvent = shouldIntercept;
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (!mHandleTouchEvent) {
                    switch (mDragStyle) {
                        case None:
                            break;
                        case Left:
                            if (!DragCompat.canViewScrollRight(mInnerScrollViews, mDownX, mDownY, false)) {
                                mHandleTouchEvent = true;
                            }
                            break;
                        case Right:
                            if (!DragCompat.canViewScrollLeft(mInnerScrollViews, mDownX, mDownY, false)) {
                                mHandleTouchEvent = true;
                            }
                            break;
                        case Top:
                            if (!DragCompat.canViewScrollDown(mInnerScrollViews, mDownX, mDownY, false)) {
                                mHandleTouchEvent = true;
                            }
                            break;
                        case Bottom:
                            if (!DragCompat.canViewScrollUp(mInnerScrollViews, mDownX, mDownY, false)) {
                                mHandleTouchEvent = true;
                            }
                            break;
                    }
                }
                break;
            default:
                break;
        }
        return shouldIntercept;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        if (!isEnable()) {
            return super.onTouchEvent(ev);
        }
        if (mUsingNested) {
            return super.onTouchEvent(ev);
        }
        mDragHelper.processTouchEvent(ev);
        return mHandleTouchEvent;
    }

    @Override
    public void computeScroll() {
        if (!isEnable()) {
            return;
        }
        if (mUsingNested) {
            if (mScroller.computeScrollOffset()) {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
                invalidate();
            }
        } else {
            if (mDragHelper.continueSettling(true)) {
                invalidate();
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
        if (getChildCount() != 1) {
            throw new IllegalStateException("只能设置一个子View");
        }
        mInnerScrollViews.clear();
        mInnerScrollViews.addAll(DragCompat.findAllScrollViews(this));
        mLeft = getChildAt(0).getLeft();
        mTop = getChildAt(0).getTop();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (!mUsingNested) {
            super.scrollTo(x, y);
            return;
        }
        int realx = 0;
        int realy = 0;
        switch (mDragStyle) {
            case Left:
                realx = Utils.intRange(x, 0, getWidth());
                break;
            case Right:
                realx = Utils.intRange(x, -getWidth(), 0);
                break;
            case Top:
                realy = Utils.intRange(y, 0, getHeight());
                break;
            case Bottom:
                realy = Utils.intRange(y, -getHeight(), 0);
                break;
            case None:
                break;
        }
        super.scrollTo(realx, realy);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!mUsingNested) return;
        switch (mDragStyle) {
            case Left:
            case Right:
                mSwipeFraction = Math.abs((float) getScaleX() / (float) getWidth());
                break;
            case Top:
            case Bottom:
                mSwipeFraction = Math.abs((float) getScaleY() / (float) getHeight());
                break;
            case None:
                mSwipeFraction = 0F;
                break;
        }
        if (mSwipeFraction < 0) {
            mSwipeFraction = 0f;
        } else if (mSwipeFraction > 1) {
            mSwipeFraction = 1f;
        }
        onDragging();
        if (mSwipeFraction == 1F) {
            onDragEnd();
        }
    }

    private void onDragStart() {
        if (mOnSwipeListener != null) {
            mOnSwipeListener.onStart();
        }
    }

    private void onDragging() {
        if (mOnSwipeListener != null) {
            mOnSwipeListener.onSwiping(mSwipeFraction);
        }
    }

    private void onDragEnd() {
        if (mOnSwipeListener != null) {
            mOnSwipeListener.onEnd();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        if (target instanceof NestedScrollingChild) {
            NestedScrollingChild nestedScrollingChild = (NestedScrollingChild) target;
            switch (mDragStyle) {
                case None:
                    mUsingNested = false;
                    break;
                case Left:
                case Right:
                    mUsingNested = target.canScrollHorizontally(-1) || target.canScrollHorizontally(1);
                    break;
                case Top:
                case Bottom:
                    mUsingNested = target.canScrollVertically(-1) || target.canScrollVertically(1);
                    break;
            }
        } else {
            mUsingNested = false;
        }
        Log.d("DragLayout", "onStartNestedScroll->usingNested=$usingNested");
        return mUsingNested;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.d("DragLayout", "onNestedScrollAccepted->type=$type");
        mNestedHelper.onNestedScrollAccepted(child, target, axes, type);
        if (type == ViewCompat.TYPE_TOUCH) {
            mScroller.abortAnimation();
            velocity = 0F;
            onDragStart();
        }
    }

    private float velocity = 0F;

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        switch (mDragStyle) {
            case None:
                velocity = 0F;
                break;
            case Left:
            case Right:
                velocity = velocityX;
                break;
            case Top:
            case Bottom:
                velocity = velocityY;
                break;
        }
        return super.onNestedPreFling(target, velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(@NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(target, velocityX, velocityY, consumed);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (target instanceof ScrollingView) {
            switch (mDragStyle) {
                case None:
                    break;
                case Left:
                    if (dx < 0) {
                        if (getScrollX() > 0) {
                            if (getScrollX() + dx < 0) {
                                consumed[0] = -getScrollX();
                            } else {
                                consumed[0] = dx;
                            }
                        } else {
                            consumed[0] = 0;
                        }
                    } else if (dx > 0) {
                        if (getScrollX() > 0) {
                            consumed[0] = dx;
                        } else {
                            if (target.canScrollHorizontally(1)) {
                                consumed[0] = 0;
                            } else {
                                if (type == ViewCompat.TYPE_NON_TOUCH) {
                                    if (getScrollX() + dx < 0) {
                                        consumed[0] = -getScrollX();
                                    } else {
                                        consumed[0] = 0;
                                    }
                                } else {
                                    consumed[0] = dx;
                                }
                            }
                        }
                    } else {
                        consumed[0] = 0;
                    }
                    consumed[1] = 0;
                    break;
                case Right:
                    if (dx > 0) {
                        if (getScrollX() < 0) {
                            if (getScrollX() + dx > 0) {
                                consumed[0] = -getScrollX();
                            } else {
                                consumed[0] = dx;
                            }
                        } else {
                            consumed[0] = 0;
                        }
                    } else if (dx < 0) {
                        if (getScrollX() < 0) {
                            consumed[0] = dx;
                        } else {
                            if (target.canScrollHorizontally(-1)) {
                                consumed[0] = 0;
                            } else {
                                if (type == ViewCompat.TYPE_NON_TOUCH) {
                                    if (getScrollX() + dx > 0) {
                                        consumed[0] = -getScrollX();
                                    } else {
                                        consumed[0] = 0;
                                    }
                                } else {
                                    consumed[0] = dx;
                                }
                            }
                        }
                    } else {
                        consumed[0] = 0;
                    }
                    consumed[1] = 0;
                    break;
                case Top:
                    consumed[0] = 0;
                    if (dy < 0) {
                        if (getScrollY() > 0) {
                            if (getScrollY() + dy < 0) {
                                consumed[1] = -getScrollY();
                            } else {
                                consumed[1] = dy;
                            }
                        } else {
                            consumed[1] = 0;
                        }
                    } else if (dy > 0) {
                        if (getScrollY() > 0) {
                            consumed[1] = dy;
                        } else {
                            if (target.canScrollVertically(1)) {
                                consumed[1] = 0;
                            } else {
                                if (type == ViewCompat.TYPE_NON_TOUCH) {
                                    if (getScrollY() + dy < 0) {
                                        consumed[1] = -getScrollY();
                                    } else {
                                        consumed[1] = 0;
                                    }
                                } else {
                                    consumed[1] = dy;
                                }
                            }
                        }
                    } else {
                        consumed[1] = 0;
                    }
                    break;
                case Bottom:
                    consumed[0] = 0;
                    if (dy > 0) {
                        if (getScrollY() < 0) {
                            if (getScrollY() + dy > 0) {
                                consumed[1] = -getScrollY();
                            } else {
                                consumed[1] = dy;
                            }
                        } else {
                            consumed[1] = 0;
                        }
                    } else if (dy < 0) {
                        if (getScrollY() < 0) {
                            consumed[1] = dy;
                        } else {
                            if (target.canScrollVertically(-1)) {
                                consumed[1] = 0;
                            } else {
                                if (type == ViewCompat.TYPE_NON_TOUCH) {
                                    if (getScrollY() + dy > 0) {
                                        consumed[1] = -getScrollY();
                                    } else {
                                        consumed[1] = 0;
                                    }
                                } else {
                                    consumed[1] = dy;
                                }
                            }
                        }
                    } else {
                        consumed[1] = 0;
                    }
                    break;
            }
            scrollBy(consumed[0], consumed[1]);
            Log.d("DragLayout", "onNestedPreScroll->consumed-y=${consumed[1]}");
        }
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        switch (mDragStyle) {
            case None:
                break;
            case Left:
                if (getScrollY() + dxUnconsumed < 0) {
                    consumed[0] = -getScrollX();
                } else {
                    if (type == ViewCompat.TYPE_NON_TOUCH) {
                        consumed[0] = 0;
                    } else {
                        consumed[0] = dxUnconsumed;
                    }
                }
                consumed[1] = 0;
                break;
            case Right:
                if (getScrollX() + dxUnconsumed > 0) {
                    consumed[0] = -getScrollX();
                } else {
                    if (type == ViewCompat.TYPE_NON_TOUCH) {
                        consumed[0] = 0;
                    } else {
                        consumed[0] = dxUnconsumed;
                    }
                }
                consumed[1] = 0;
                break;
            case Top:
                consumed[0] = 0;
                if (getScrollY() + dyUnconsumed < 0) {
                    consumed[1] = -getScrollY();
                } else {
                    if (type == ViewCompat.TYPE_NON_TOUCH) {
                        consumed[1] = 0;
                    } else {
                        consumed[1] = dyUnconsumed;
                    }
                }
                break;
            case Bottom:
                consumed[0] = 0;
                if (getScrollY() + dyUnconsumed > 0) {
                    consumed[1] = -getScrollY();
                } else {
                    if (type == ViewCompat.TYPE_NON_TOUCH) {
                        consumed[1] = 0;
                    } else {
                        consumed[1] = dyUnconsumed;
                    }
                }
                break;
        }
        scrollBy(consumed[0], consumed[1]);
        Log.d("DragLayout", "onNestedScroll->consumed-y=${consumed[1]}");
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mNestedHelper.onStopNestedScroll(target, type);
        Log.d("DragLayout", "onStopNestedScroll->type=$type");
        if (type == ViewCompat.TYPE_TOUCH) {
            Log.d("DragLayout", "onStopNestedScroll->velocity=$velocity");
            boolean dismiss = false;
            switch (mDragStyle) {
                case None:
                    break;
                case Left:
                    dismiss = getScrollX() > 0 && velocity > mDismissVelocity;
                    break;
                case Right:
                    dismiss = getScrollX() < 0 && -velocity > mDismissVelocity;
                    break;
                case Top:
                    dismiss = getScrollY() > 0 && velocity > mDismissVelocity;
                    break;
                case Bottom:
                    dismiss = getScrollY() < 0 && -velocity > mDismissVelocity;
                    break;
            }
            dismiss = dismiss || mSwipeFraction >= mDismissFraction;
            float f = Math.abs(velocity) / mDismissVelocity;
            int duration;
            if (f <= 1) {
                duration = mDismissDuration;
            } else {
                duration = Math.max((int) (mDismissDuration / f), 100);
            }
            Log.d("DragLayout", "onStopNestedScroll->dismiss=$dismiss");
            if (dismiss) {
                switch (mDragStyle) {
                    case None:
                        mScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getScrollY(), duration);
                        break;
                    case Left:
                        mScroller.startScroll(getScrollX(), getScrollY(), getWidth() - getScrollX(), -getScrollY(), duration);
                        break;
                    case Right:
                        mScroller.startScroll(getScrollX(), getScrollY(), -getWidth() - getScrollX(), -getScrollY(), duration);
                        break;
                    case Top:
                        mScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), getHeight() - getScrollY(), duration);
                        break;
                    case Bottom:
                        mScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getHeight() - getScrollY(), duration);
                        break;
                }
            } else {
                mScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getScrollY(), duration);
            }
            invalidate();
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedHelper.getNestedScrollAxes();
    }

    private class DragCallback extends ViewDragHelper.Callback {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            return isEnable();
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
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
        public int getViewVerticalDragRange(@NonNull View child) {
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
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            mSwipeFraction = 0F;
            if (mOnSwipeListener != null) {
                mOnSwipeListener.onStart();
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            switch (mDragStyle) {
                case Left:
                    if (DragCompat.canViewScrollRight(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mLeft;
                    }
                    if (left > mLeft) {
                        return mLeft;
                    }
                    final int l = mLeft + child.getWidth();
                    return Math.max(left, -l);
                case Right:
                    if (DragCompat.canViewScrollLeft(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mLeft;
                    }
                    if (left > getWidth()) {
                        return getWidth();
                    }
                    return Math.max(left, mLeft);
                case Top:
                case Bottom:
                case None:
                default:
                    return mLeft;
            }
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            switch (mDragStyle) {
                case Top:
                    if (DragCompat.canViewScrollDown(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mTop;
                    }
                    if (top > mTop) {
                        return mTop;
                    }
                    final int t = mTop + child.getHeight();
                    return Math.max(top, -t);
                case Bottom:
                    if (DragCompat.canViewScrollUp(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mTop;
                    }
                    if (top > getHeight()) {
                        return getHeight();
                    }
                    return Math.max(top, mTop);
                case Left:
                case Right:
                case None:
                default:
                    return mTop;
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            switch (mDragStyle) {
                case Left:
                case Right:
                    final float xoff = Math.abs(left - mLeft);
                    final float xmax = getViewHorizontalDragRange(changedView);
                    mSwipeFraction = xoff / xmax;
                    break;
                case Top:
                case Bottom:
                    final float yoff = Math.abs(top - mTop);
                    final float ymax = getViewVerticalDragRange(changedView);
                    mSwipeFraction = yoff / ymax;
                    break;
                case None:
                default:
                    break;
            }
            if (mSwipeFraction < 0) {
                mSwipeFraction = 0;
            } else if (mSwipeFraction > 1) {
                mSwipeFraction = 1;
            }
            if (mOnSwipeListener != null) {
                mOnSwipeListener.onSwiping(mSwipeFraction);
                if (mSwipeFraction == 1) mOnSwipeListener.onEnd();
            }
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            float dismissFactor = 0.5f;
            boolean isDismiss = judgeDismissBySpeed(xvel, yvel) || mSwipeFraction >= dismissFactor;
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

    public interface OnSwipeListener {
        void onStart();
        void onSwiping(@FloatRange(from = 0F, to = 1F) float f);
        void onEnd();
    }
}
