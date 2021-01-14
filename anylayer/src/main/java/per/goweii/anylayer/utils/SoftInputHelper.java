package per.goweii.anylayer.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

import java.util.HashMap;
import java.util.Map;

/**
 * 监听软键盘的打开和隐藏
 * 打开时滚动布局，可设置仅在某几个EditText获取焦点时开启
 */
public final class SoftInputHelper implements ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnGlobalFocusChangeListener {

    private final Window window;
    private final View rootView;
    private final int oldSoftInputMode;

    private final Rect windowVisibleDisplayFrame = new Rect();
    private final int[] viewInWindowLocation = new int[2];

    private boolean isOpened = false;

    private long duration = 200;
    private View moveView = null;
    private final Map<View, View> focusBottomMap = new HashMap<>(0);
    private View focusBottomView = null;
    private OnSoftInputListener onSoftInputListener = null;

    private Animator moveAnim = null;

    private final Runnable moveRunnable = new Runnable() {
        @Override
        public void run() {
            calcMove();
        }
    };

    public static SoftInputHelper attach(@NonNull Activity activity) {
        return new SoftInputHelper(activity);
    }

    private SoftInputHelper(@NonNull Activity activity) {
        this.window = activity.getWindow();
        this.rootView = window.getDecorView().getRootView();
        ViewTreeObserver observer = rootView.getViewTreeObserver();
        if (observer.isAlive()) {
            observer.addOnGlobalLayoutListener(this);
            observer.addOnGlobalFocusChangeListener(this);
        }
        oldSoftInputMode = window.getAttributes().softInputMode;
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
    }

    public void detach() {
        rootView.removeCallbacks(moveRunnable);
        if (moveAnim != null) {
            moveAnim.cancel();
        }
        ViewTreeObserver observer = rootView.getViewTreeObserver();
        if (observer.isAlive()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                observer.removeOnGlobalLayoutListener(this);
            } else {
                observer.removeGlobalOnLayoutListener(this);
            }
            observer.removeOnGlobalFocusChangeListener(this);
        }
        window.setSoftInputMode(oldSoftInputMode);
    }

    @NonNull
    public SoftInputHelper move(@NonNull View moveView) {
        this.moveView = moveView;
        return this;
    }

    @NonNull
    public SoftInputHelper follow(@Nullable View bottomView, View... focusViews) {
        if (focusViews != null && focusViews.length > 0) {
            for (View focusView : focusViews) {
                if (focusView != null) {
                    if (bottomView != null) {
                        focusBottomMap.put(focusView, bottomView);
                    } else {
                        focusBottomMap.put(focusView, focusView);
                    }
                }
            }
        } else {
            this.focusBottomView = bottomView;
        }
        return this;
    }

    @NonNull
    public SoftInputHelper clear() {
        this.moveView = null;
        this.focusBottomMap.clear();
        this.focusBottomView = null;
        return this;
    }

    @NonNull
    public SoftInputHelper listener(@Nullable OnSoftInputListener onSoftInputListener) {
        this.onSoftInputListener = onSoftInputListener;
        return this;
    }

    @NonNull
    public SoftInputHelper duration(long duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public void onGlobalLayout() {
        final boolean isOpen = calcIsOpen();
        if (isOpened == isOpen) return;
        isOpened = isOpen;
        notifyListener();
        startMove();
    }

    private void notifyListener() {
        if (onSoftInputListener != null) {
            if (isOpened) {
                onSoftInputListener.onOpen();
            } else {
                onSoftInputListener.onClose();
            }
        }
    }

    private void startMove() {
        if (moveView != null) {
            rootView.removeCallbacks(moveRunnable);
            calcMove();
        }
    }

    private void calcMove() {
        if (!isOpened) {
            moveTo(0);
            return;
        }
        final View focusView = window.getCurrentFocus();
        if (focusView == null) {
            moveTo(0);
            return;
        }
        if (focusBottomMap.containsKey(focusView)) {
            View bottomView = focusBottomMap.get(focusView);
            if (bottomView != null) {
                calcMoveToBottom(bottomView);
            }
        } else {
            if (isFocusInMove(focusView)) {
                if (focusBottomView != null) {
                    calcMoveToBottom(focusBottomView);
                } else {
                    calcMoveToBottom(focusView);
                }
            }
        }
    }

    private boolean isFocusInMove(@NonNull View focusView) {
        if (moveView == null) return false;
        return moveView.findFocus() == focusView;
    }

    private void calcMoveToBottom(@NonNull View bottomView) {
        final Rect rect = getWindowVisibleDisplayFrame();
        int bottomViewY = getBottomViewY(bottomView);
        int offHeight = bottomViewY - rect.bottom;
        if (offHeight > 0) {
            moveBy(-offHeight);
        } else if (offHeight < 0) {
            moveBy(-offHeight);
        }
    }

    private void moveBy(float off) {
        if (moveView == null) return;
        float from = moveView.getTranslationY();
        moveTo(from + off);
    }

    private void moveTo(float to) {
        if (moveView == null) return;
        translationTo(moveView, Math.min(to, 0));
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        if (isOpened) {
            if (moveView != null) {
                rootView.postDelayed(moveRunnable, 100);
            }
        }
    }

    private int getBottomViewY(@NonNull View bottomView) {
        bottomView.getLocationInWindow(viewInWindowLocation);
        return viewInWindowLocation[1] + bottomView.getHeight();
    }

    @NonNull
    private Rect getWindowVisibleDisplayFrame() {
        rootView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
        return windowVisibleDisplayFrame;
    }

    private void translationTo(@NonNull View view, float to) {
        if (moveAnim != null) moveAnim.cancel();
        float from = moveView.getTranslationY();
        if (from == to) return;
        moveAnim = ObjectAnimator.ofFloat(view, "translationY", from, to);
        moveAnim.setInterpolator(new DecelerateInterpolator());
        moveAnim.setDuration(duration);
        moveAnim.start();
    }

    private boolean calcIsOpen() {
        Rect rect = getWindowVisibleDisplayFrame();
        int usableHeightNow = rect.height();
        int usableHeightSansKeyboard = rootView.getHeight();
        int heightDifference = usableHeightSansKeyboard - usableHeightNow;
        return heightDifference > (usableHeightSansKeyboard / 4);
    }

    @Nullable
    private View currFocusView() {
        View focusView = window.getCurrentFocus();
        for (View view : focusBottomMap.keySet()) {
            if (focusView == view) {
                return view;
            }
        }
        return null;
    }

    public interface OnSoftInputListener {
        void onOpen();

        void onClose();
    }
}
