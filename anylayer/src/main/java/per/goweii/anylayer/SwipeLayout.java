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

    @IntDef({
            Direction.Left,
            Direction.Top,
            Direction.Right,
            Direction.Bottom
    })
    public @interface Direction {
        int Left = 1;
        int Top = 1 << 1;
        int Right = 1 << 2;
        int Bottom = 1 << 3;
    }

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
    @Direction
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

    public boolean canSwipeDirection(int swipeDirection) {
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("SwipeLayout", "dispatchTouchEvent");
        switch (ev.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                if (!mHandleTouchEvent) {
                    float offX = ev.getRawX() - mDownX;
                    float offY = ev.getRawY() - mDownY;
                    boolean dragged = offX * offX + offY * offY > mDragHelper.getTouchSlop() * mDragHelper.getTouchSlop();
                    if (dragged) {
                        if (canSwipeDirection(Direction.Top)) {
                            if (offY < 0 && !DragCompat.canViewScrollDown(mInnerScrollViews, mDownX, mDownY, false)) {
                                mHandleTouchEvent = false;
                                requestDisallowInterceptTouchEvent(false);
                            }
                        } else if (canSwipeDirection(Direction.Bottom)) {
                            if (offY > 0 && !DragCompat.canViewScrollUp(mInnerScrollViews, mDownX, mDownY, false)) {
                                mHandleTouchEvent = false;
                                requestDisallowInterceptTouchEvent(false);
                            }
                        } else if (canSwipeDirection(Direction.Left)) {
                            if (offX < 0 && !DragCompat.canViewScrollRight(mInnerScrollViews, mDownX, mDownY, false)) {
                                mHandleTouchEvent = false;
                                requestDisallowInterceptTouchEvent(false);
                            }
                        } else if (canSwipeDirection(Direction.Right)) {
                            if (offX > 0 && !DragCompat.canViewScrollLeft(mInnerScrollViews, mDownX, mDownY, false)) {
                                mHandleTouchEvent = false;
                                requestDisallowInterceptTouchEvent(false);
                            }
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
                if (mSwipeFraction == 0F) {
                    mCurrSwipeDirection = 0;
                }
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
                    List<View> views = DragCompat.contains(mInnerScrollViews, mDownX, mDownY);
                    if (!views.isEmpty()) {
                        if (canSwipeDirection(Direction.Left)) {
                            if (!DragCompat.canViewScrollRight(views, false)) {
                                mHandleTouchEvent = true;
                            }
                        }
                        if (canSwipeDirection(Direction.Right)) {
                            if (!DragCompat.canViewScrollLeft(views, false)) {
                                mHandleTouchEvent = true;
                            }
                        }
                        if (canSwipeDirection(Direction.Top)) {
                            if (!DragCompat.canViewScrollDown(views, false)) {
                                mHandleTouchEvent = true;
                            }
                        }
                        if (canSwipeDirection(Direction.Bottom)) {
                            if (!DragCompat.canViewScrollUp(views, false)) {
                                mHandleTouchEvent = true;
                            }
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
        if (mHandleTouchEvent) {
            mDragHelper.processTouchEvent(ev);
        }
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
            case Direction.Left:
                realx = Utils.intRange(x, 0, getWidth());
                break;
            case Direction.Right:
                realx = Utils.intRange(x, -getWidth(), 0);
                break;
            case Direction.Top:
                realy = Utils.intRange(y, 0, getHeight());
                break;
            case Direction.Bottom:
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
            case Direction.Left:
            case Direction.Right:
                mSwipeFraction = Math.abs(getScrollX() / (float) getWidth());
                break;
            case Direction.Top:
            case Direction.Bottom:
                mSwipeFraction = Math.abs(getScrollY() / (float) getHeight());
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
        if (mSwipeFraction == 0F) {
            mCurrSwipeDirection = 0;
        } else if (mSwipeFraction == 1F) {
            mCurrSwipeDirection = 0;
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
        if (mOnSwipeListener != null) {
            mOnSwipeListener.onEnd();
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        if (target instanceof NestedScrollingChild) {
            NestedScrollingChild nestedScrollingChild = (NestedScrollingChild) target;
            if (canSwipeDirection(Direction.Left | Direction.Right)) {
                mUsingNested = target.canScrollHorizontally(-1) || target.canScrollHorizontally(1);
            } else if (canSwipeDirection(Direction.Top | Direction.Bottom)) {
                mUsingNested = target.canScrollVertically(-1) || target.canScrollVertically(1);
            } else {
                mUsingNested = false;
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
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (!(target instanceof ScrollingView)) {
            return;
        }
        if (mCurrSwipeDirection == 0) {
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    if (canSwipeDirection(Direction.Left))
                        mCurrSwipeDirection = Direction.Left;
                } else {
                    if (canSwipeDirection(Direction.Right))
                        mCurrSwipeDirection = Direction.Right;
                }
            } else {
                if (dy > 0) {
                    if (canSwipeDirection(Direction.Top))
                        mCurrSwipeDirection = Direction.Top;
                } else {
                    if (canSwipeDirection(Direction.Bottom))
                        mCurrSwipeDirection = Direction.Bottom;
                }
            }
        }
        switch (mCurrSwipeDirection) {
            default:
                break;
            case Direction.Left:
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
            case Direction.Right:
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
            case Direction.Top:
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
            case Direction.Bottom:
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
            case Direction.Left:
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
            case Direction.Right:
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
            case Direction.Top:
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
            case Direction.Bottom:
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
    public boolean onNestedPreFling(@NonNull View target, float velocityX, float velocityY) {
        switch (mCurrSwipeDirection) {
            default:
                mVelocity = 0F;
                break;
            case Direction.Left:
            case Direction.Right:
                mVelocity = velocityX;
                break;
            case Direction.Top:
            case Direction.Bottom:
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
    public void onStopNestedScroll(@NonNull View target, int type) {
        mNestedHelper.onStopNestedScroll(target, type);
        Log.d("SwipeLayout", "onStopNestedScroll->type=$type");
        if (type == ViewCompat.TYPE_TOUCH) {
            Log.d("SwipeLayout", "onStopNestedScroll->velocity=$velocity");
            boolean dismiss = false;
            switch (mCurrSwipeDirection) {
                default:
                    break;
                case Direction.Left:
                    dismiss = getScrollX() > 0 && mVelocity > mDismissVelocity;
                    break;
                case Direction.Right:
                    dismiss = getScrollX() < 0 && -mVelocity > mDismissVelocity;
                    break;
                case Direction.Top:
                    dismiss = getScrollY() > 0 && mVelocity > mDismissVelocity;
                    break;
                case Direction.Bottom:
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
                    case Direction.Left:
                        mScroller.startScroll(getScrollX(), getScrollY(), getWidth() - getScrollX(), -getScrollY(), duration);
                        break;
                    case Direction.Right:
                        mScroller.startScroll(getScrollX(), getScrollY(), -getWidth() - getScrollX(), -getScrollY(), duration);
                        break;
                    case Direction.Top:
                        mScroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), getHeight() - getScrollY(), duration);
                        break;
                    case Direction.Bottom:
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
            if (canSwipeDirection(Direction.Left)) {
                return mLeft + child.getWidth();
            } else if (canSwipeDirection(Direction.Right)) {
                return getWidth() - mLeft;
            } else {
                return 0;
            }
        }

        @Override
        public int getViewVerticalDragRange(@NonNull View child) {
            if (canSwipeDirection(Direction.Top)) {
                return mTop + child.getHeight();
            } else if (canSwipeDirection(Direction.Bottom)) {
                return getHeight() - mTop;
            } else {
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
                if (canSwipeDirection(Direction.Left) && dx < 0)
                    mCurrSwipeDirection = Direction.Left;
                if (canSwipeDirection(Direction.Right) && dx > 0)
                    mCurrSwipeDirection = Direction.Right;
            }
            switch (mCurrSwipeDirection) {
                case Direction.Left:
                    if (DragCompat.canViewScrollRight(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mLeft;
                    }
                    if (left > mLeft) {
                        return mLeft;
                    }
                    final int l = mLeft + child.getWidth();
                    return Math.max(left, -l);
                case Direction.Right:
                    if (DragCompat.canViewScrollLeft(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mLeft;
                    }
                    if (left > getWidth()) {
                        return getWidth();
                    }
                    return Math.max(left, mLeft);
                case Direction.Top:
                case Direction.Bottom:
                default:
                    return mLeft;
            }
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            if (mCurrSwipeDirection == 0) {
                if (canSwipeDirection(Direction.Top) && dy < 0)
                    mCurrSwipeDirection = Direction.Top;
                if (canSwipeDirection(Direction.Bottom) && dy > 0)
                    mCurrSwipeDirection = Direction.Bottom;
            }
            switch (mCurrSwipeDirection) {
                case Direction.Top:
                    if (DragCompat.canViewScrollDown(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mTop;
                    }
                    if (top > mTop) {
                        return mTop;
                    }
                    final int t = mTop + child.getHeight();
                    return Math.max(top, -t);
                case Direction.Bottom:
                    if (DragCompat.canViewScrollUp(mInnerScrollViews, mDownX, mDownY, false)) {
                        return mTop;
                    }
                    if (top > getHeight()) {
                        return getHeight();
                    }
                    return Math.max(top, mTop);
                case Direction.Left:
                case Direction.Right:
                default:
                    return mTop;
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            switch (mCurrSwipeDirection) {
                case Direction.Left:
                case Direction.Right:
                    final float xoff = Math.abs(left - mLeft);
                    final float xmax = getViewHorizontalDragRange(changedView);
                    mSwipeFraction = xoff / xmax;
                    break;
                case Direction.Top:
                case Direction.Bottom:
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
                    case Direction.Left:
                        l = -(mLeft + releasedChild.getWidth());
                        break;
                    case Direction.Right:
                        l = getWidth();
                        break;
                    case Direction.Top:
                        t = -(mTop + releasedChild.getHeight());
                        break;
                    case Direction.Bottom:
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
                case Direction.Left:
                    return xvel < -velocityLimit;
                case Direction.Right:
                    return xvel > velocityLimit;
                case Direction.Top:
                    return yvel < -velocityLimit;
                case Direction.Bottom:
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
