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
import androidx.annotation.IntDef;
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

    @IntDef({SWIPE_LEFT, SWIPE_TOP, SWIPE_RIGHT, SWIPE_BOTTOM})
    @interface SwipeDirection {
    }

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
    private int mSwipeDirection = 0;
    private OnSwipeListener mOnSwipeListener = null;

    private List<View> mInnerScrollViews = new ArrayList<>(0);

    private boolean mUsingNested = false;
    private boolean mHandleTouchEvent = false;
    private float mDownX = 0F;
    private float mDownY = 0F;
    private int mLeft = 0;
    private int mTop = 0;
    @SwipeDirection
    private int mCurrSwipeDirection = 0;
    @FloatRange(from = 0F, to = 1F)
    private float mSwipeFraction = 0F;
    private float mVelocity = 0F;

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

    public boolean canSwipe() {
        return mSwipeDirection != 0;
    }

    public boolean canSwipeDirection(@SwipeDirection int swipeDirection) {
        return (mSwipeDirection & swipeDirection) != 0;
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
        if (!canSwipe()) {
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
        Log.d("SwipeLayout", "onInterceptTouchEvent->mDownY=$mDownY，mDownY=$mDownY");
        if (mUsingNested) {
            mHandleTouchEvent = false;
            return super.onInterceptTouchEvent(ev);
        }
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(ev);
        mHandleTouchEvent = shouldIntercept;
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (!mHandleTouchEvent) {
                    if (canSwipeDirection(SWIPE_LEFT)) {
                        if (!DragCompat.canViewScrollRight(mInnerScrollViews, mDownX, mDownY, false)) {
                            mHandleTouchEvent = true;
                        }
                    }
                    if (canSwipeDirection(SWIPE_RIGHT)) {
                        if (!DragCompat.canViewScrollLeft(mInnerScrollViews, mDownX, mDownY, false)) {
                            mHandleTouchEvent = true;
                        }
                    }
                    if (canSwipeDirection(SWIPE_TOP)) {
                        if (!DragCompat.canViewScrollDown(mInnerScrollViews, mDownX, mDownY, false)) {
                            mHandleTouchEvent = true;
                        }
                    }
                    if (canSwipeDirection(SWIPE_BOTTOM)) {
                        if (!DragCompat.canViewScrollUp(mInnerScrollViews, mDownX, mDownY, false)) {
                            mHandleTouchEvent = true;
                        }
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
        if (!canSwipe()) {
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
        if (!canSwipe()) {
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
        switch (mCurrSwipeDirection) {
            case SWIPE_LEFT:
                realx = Utils.intRange(x, 0, getWidth());
                break;
            case SWIPE_RIGHT:
                realx = Utils.intRange(x, -getWidth(), 0);
                break;
            case SWIPE_TOP:
                realy = Utils.intRange(y, 0, getHeight());
                break;
            case SWIPE_BOTTOM:
                realy = Utils.intRange(y, -getHeight(), 0);
                break;
            default:
                break;
        }
        super.scrollTo(realx, realy);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (!mUsingNested) return;
        switch (mCurrSwipeDirection) {
            case SWIPE_LEFT:
            case SWIPE_RIGHT:
                mSwipeFraction = Math.abs(getScaleX() / (float) getWidth());
                break;
            case SWIPE_TOP:
            case SWIPE_BOTTOM:
                mSwipeFraction = Math.abs(getScaleY() / (float) getHeight());
                break;
            default:
                mSwipeFraction = 0F;
                break;
        }
        if (mSwipeFraction < 0) {
            mSwipeFraction = 0f;
        } else if (mSwipeFraction > 1) {
            mSwipeFraction = 1f;
        }
        onSwiping();
        if (mSwipeFraction == 1F) {
            onSwipeEnd();
        }
    }

    private void onSwipeStart() {
        if (mOnSwipeListener != null) {
            mOnSwipeListener.onStart();
        }
    }

    private void onSwiping() {
        if (mOnSwipeListener != null) {
            mOnSwipeListener.onSwiping(mSwipeFraction);
        }
    }

    private void onSwipeEnd() {
        mCurrSwipeDirection = 0;
        if (mOnSwipeListener != null) {
            mOnSwipeListener.onEnd();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        if (target instanceof NestedScrollingChild) {
            NestedScrollingChild nestedScrollingChild = (NestedScrollingChild) target;
            switch (mCurrSwipeDirection) {
                default:
                    mUsingNested = false;
                    break;
                case SWIPE_LEFT:
                case SWIPE_RIGHT:
                    mUsingNested = target.canScrollHorizontally(-1) || target.canScrollHorizontally(1);
                    break;
                case SWIPE_TOP:
                case SWIPE_BOTTOM:
                    mUsingNested = target.canScrollVertically(-1) || target.canScrollVertically(1);
                    break;
            }
        } else {
            mUsingNested = false;
        }
        Log.d("SwipeLayout", "onStartNestedScroll->usingNested=$usingNested");
        return mUsingNested;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.d("SwipeLayout", "onNestedScrollAccepted->type=$type");
        mNestedHelper.onNestedScrollAccepted(child, target, axes, type);
        if (type == ViewCompat.TYPE_TOUCH) {
            mScroller.abortAnimation();
            mVelocity = 0F;
            onSwipeStart();
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        switch (mCurrSwipeDirection) {
            default:
                mVelocity = 0F;
                break;
            case SWIPE_LEFT:
            case SWIPE_RIGHT:
                mVelocity = velocityX;
                break;
            case SWIPE_TOP:
            case SWIPE_BOTTOM:
                mVelocity = velocityY;
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
        if (!(target instanceof ScrollingView)) {
            return;
        }
        switch (mCurrSwipeDirection) {
            default:
                break;
            case SWIPE_LEFT:
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
            case SWIPE_RIGHT:
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
            case SWIPE_TOP:
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
            case SWIPE_BOTTOM:
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
        Log.d("SwipeLayout", "onNestedPreScroll->consumed-y=${consumed[1]}");
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        switch (mCurrSwipeDirection) {
            default:
                break;
            case SWIPE_LEFT:
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
            case SWIPE_RIGHT:
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
            case SWIPE_TOP:
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
            case SWIPE_BOTTOM:
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
        Log.d("SwipeLayout", "onNestedScroll->consumed-y=${consumed[1]}");
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        mNestedHelper.onStopNestedScroll(target, type);
        Log.d("SwipeLayout", "onStopNestedScroll->type=$type");
        if (type == ViewCompat.TYPE_TOUCH) {
            Log.d("SwipeLayout", "onStopNestedScroll->velocity=$velocity");
            boolean dismiss = false;
            switch (mCurrSwipeDirection) {
                default:
                    break;
                case SWIPE_LEFT:
                    dismiss = getScrollX() > 0 && mVelocity > mDismissVelocity;
                    break;
                case SWIPE_RIGHT:
                    dismiss = getScrollX() < 0 && -mVelocity > mDismissVelocity;
                    break;
                case SWIPE_TOP:
                    dismiss = getScrollY() > 0 && mVelocity > mDismissVelocity;
                    break;
                case SWIPE_BOTTOM:
                    dismiss = getScrollY() < 0 && -mVelocity > mDismissVelocity;
                    break;
            }
            dismiss = dismiss || mSwipeFraction >= mDismissFraction;
            float f = Math.abs(mVelocity) / mDismissVelocity;
            int duration;
            if (f <= 1) {
                duration = mDismissDuration;
            } else {
                duration = Math.max((int) (mDismissDuration / f), 100);
            }
            Log.d("SwipeLayout", "onStopNestedScroll->dismiss=$dismiss");
            if (dismiss) {
                switch (mCurrSwipeDirection) {
                    default:
                        mScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getScrollY(), duration);
                        break;
                    case SWIPE_LEFT:
                        mScroller.startScroll(getScrollX(), getScrollY(), getWidth() - getScrollX(), -getScrollY(), duration);
                        break;
                    case SWIPE_RIGHT:
                        mScroller.startScroll(getScrollX(), getScrollY(), -getWidth() - getScrollX(), -getScrollY(), duration);
                        break;
                    case SWIPE_TOP:
                        mScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), getHeight() - getScrollY(), duration);
                        break;
                    case SWIPE_BOTTOM:
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
            return canSwipe();
        }

        @Override
        public int getViewHorizontalDragRange(@NonNull View child) {
            switch (mCurrSwipeDirection) {
                case SWIPE_LEFT:
                    return mLeft + child.getWidth();
                case SWIPE_RIGHT:
                    return getWidth() - mLeft;
                case SWIPE_TOP:
                case SWIPE_BOTTOM:
                default:
                    return 0;
            }
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            switch (mCurrSwipeDirection) {
                case SWIPE_TOP:
                    return mTop + child.getHeight();
                case SWIPE_BOTTOM:
                    return getHeight() - mTop;
                case SWIPE_LEFT:
                case SWIPE_RIGHT:
                default:
                    return 0;
            }
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            mSwipeFraction = 0F;
            mCurrSwipeDirection = 0;
            if (mOnSwipeListener != null) {
                mOnSwipeListener.onStart();
            }
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
            if (mCurrSwipeDirection == 0) {
                if (dx > 0) {
                    mCurrSwipeDirection = SWIPE_RIGHT;
                } else if (dx < 0) {
                    mCurrSwipeDirection = SWIPE_LEFT;
                }
            }
            switch (mCurrSwipeDirection) {
                case SWIPE_LEFT:
                    if (DragCompat.canViewScrollRight(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mLeft;
                    }
                    if (left > mLeft) {
                        return mLeft;
                    }
                    final int l = mLeft + child.getWidth();
                    return Math.max(left, -l);
                case SWIPE_RIGHT:
                    if (DragCompat.canViewScrollLeft(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mLeft;
                    }
                    if (left > getWidth()) {
                        return getWidth();
                    }
                    return Math.max(left, mLeft);
                case SWIPE_TOP:
                case SWIPE_BOTTOM:
                default:
                    return mLeft;
            }
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (mCurrSwipeDirection == 0) {
                if (dy > 0) {
                    mCurrSwipeDirection = SWIPE_BOTTOM;
                } else if (dy < 0) {
                    mCurrSwipeDirection = SWIPE_TOP;
                }
            }
            switch (mCurrSwipeDirection) {
                case SWIPE_TOP:
                    if (DragCompat.canViewScrollDown(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mTop;
                    }
                    if (top > mTop) {
                        return mTop;
                    }
                    final int t = mTop + child.getHeight();
                    return Math.max(top, -t);
                case SWIPE_BOTTOM:
                    if (DragCompat.canViewScrollUp(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mTop;
                    }
                    if (top > getHeight()) {
                        return getHeight();
                    }
                    return Math.max(top, mTop);
                case SWIPE_LEFT:
                case SWIPE_RIGHT:
                default:
                    return mTop;
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            switch (mCurrSwipeDirection) {
                case SWIPE_LEFT:
                case SWIPE_RIGHT:
                    final float xoff = Math.abs(left - mLeft);
                    final float xmax = getViewHorizontalDragRange(changedView);
                    mSwipeFraction = xoff / xmax;
                    break;
                case SWIPE_TOP:
                case SWIPE_BOTTOM:
                    final float yoff = Math.abs(top - mTop);
                    final float ymax = getViewVerticalDragRange(changedView);
                    mSwipeFraction = yoff / ymax;
                    break;
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
                switch (mCurrSwipeDirection) {
                    case SWIPE_LEFT:
                        l = -(mLeft + releasedChild.getWidth());
                        break;
                    case SWIPE_RIGHT:
                        l = getWidth();
                        break;
                    case SWIPE_TOP:
                        t = -(mTop + releasedChild.getHeight());
                        break;
                    case SWIPE_BOTTOM:
                        t = getHeight();
                        break;
                    default:
                        break;
                }
            }
            mDragHelper.settleCapturedViewAt(l, t);
            invalidate();
        }

        private boolean judgeDismissBySpeed(float xvel, float yvel) {
            float velocityLimit = 2000f;
            switch (mCurrSwipeDirection) {
                case SWIPE_LEFT:
                    return xvel < -velocityLimit;
                case SWIPE_RIGHT:
                    return xvel > velocityLimit;
                case SWIPE_TOP:
                    return yvel < -velocityLimit;
                case SWIPE_BOTTOM:
                    return yvel > velocityLimit;
                default:
                    break;
            }
            return false;
        }
    }

    public interface OnSwipeListener {
        void onStart();

        void onSwiping(@FloatRange(from = 0F, to = 1F) float f);

        void onEnd();
    }
}
