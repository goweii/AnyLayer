package per.goweii.anylayer.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

import java.util.HashMap;
import java.util.Map;

/**
 * 监听软键盘的打开和隐藏
 * 打开时滚动布局，可设置仅在某几个EditText获取焦点时开启
 */
public final class InputMethodCompat implements ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnGlobalFocusChangeListener {

    private final Window mActivityWindow;
    private final View mActivityDecorView;
    private final PopupWindow mPopupWindow;
    private final View mPopupRootView;
    private final int mOldSoftInputMode;

    private final Rect mWindowVisibleDisplayFrame = new Rect();
    private final int[] mViewInWindowLocation = new int[2];

    private final int mKeyboardMinHeight;

    private int mKeyBoardHeight = 0;

    private long mDuration = 200;
    private View mMoveView = null;
    private final Map<View, View> mFocusBottomMap = new HashMap<>(0);
    private View mFocusBottomView = null;
    private OnInputMethodListener mOnInputMethodListener = null;

    private Animator mMoveAnim = null;

    private final Runnable mMoveRunnable = new Runnable() {
        @Override
        public void run() {
            calcMove();
        }
    };

    public static InputMethodCompat attach(@NonNull Activity activity) {
        return new InputMethodCompat(activity);
    }

    private InputMethodCompat(@NonNull Activity activity) {
        mActivityWindow = activity.getWindow();
        mActivityDecorView = mActivityWindow.getDecorView();
        mKeyboardMinHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                200f,
                activity.getResources().getDisplayMetrics()
        );
        mOldSoftInputMode = mActivityWindow.getAttributes().softInputMode;
        mActivityWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        mPopupWindow = new PopupWindow(activity);
        View popupView = new View(activity);
        mPopupWindow.setContentView(popupView);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setTouchable(false);
        mPopupWindow.setWidth(0);
        mPopupWindow.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setAnimationStyle(0);
        mPopupWindow.showAsDropDown(mActivityDecorView);
        mPopupRootView = popupView.getRootView();
        if (mPopupRootView.getViewTreeObserver().isAlive()) {
            mPopupRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
        if (mActivityDecorView.getViewTreeObserver().isAlive()) {
            mActivityDecorView.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        }
    }

    public void detach() {
        if (mPopupRootView.getViewTreeObserver().isAlive()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mPopupRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            } else {
                mPopupRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        }
        if (mActivityDecorView.getViewTreeObserver().isAlive()) {
            mActivityDecorView.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
        }
        mPopupWindow.dismiss();
        mPopupRootView.removeCallbacks(mMoveRunnable);
        if (mMoveAnim != null) {
            mMoveAnim.cancel();
        }
        mActivityWindow.setSoftInputMode(mOldSoftInputMode);
    }

    @NonNull
    public InputMethodCompat setMoveView(@NonNull View moveView) {
        this.mMoveView = moveView;
        return this;
    }

    @NonNull
    public InputMethodCompat setFollowViews(@Nullable View bottomView, View... focusViews) {
        if (focusViews != null && focusViews.length > 0) {
            for (View focusView : focusViews) {
                if (focusView != null) {
                    if (bottomView != null) {
                        mFocusBottomMap.put(focusView, bottomView);
                    } else {
                        mFocusBottomMap.put(focusView, focusView);
                    }
                }
            }
        } else {
            this.mFocusBottomView = bottomView;
        }
        return this;
    }

    @NonNull
    public InputMethodCompat clear() {
        this.mMoveView = null;
        this.mFocusBottomMap.clear();
        this.mFocusBottomView = null;
        return this;
    }

    @NonNull
    public InputMethodCompat setListener(@Nullable OnInputMethodListener onInputMethodListener) {
        this.mOnInputMethodListener = onInputMethodListener;
        return this;
    }

    @NonNull
    public InputMethodCompat setDuration(long duration) {
        this.mDuration = duration;
        return this;
    }

    public boolean isOpened() {
        return mKeyBoardHeight > 0;
    }

    @Override
    public void onGlobalLayout() {
        final int keyboardHeight = calcKeyboardHeight();
        final int lastHeight = mKeyBoardHeight;
        final boolean lastOpened = mKeyBoardHeight > 0;
        final boolean nowOpened = keyboardHeight > 0;
        mKeyBoardHeight = keyboardHeight;
        if (lastOpened != nowOpened) {
            notifyOpenOrClose();
        } else {
            if (lastHeight != keyboardHeight) {
                notifyHeightChanged();
            }
        }
        startMove();
    }

    private void notifyOpenOrClose() {
        if (mOnInputMethodListener != null) {
            if (isOpened()) {
                mOnInputMethodListener.onOpen(mKeyBoardHeight);
            } else {
                mOnInputMethodListener.onClose(mKeyBoardHeight);
            }
        }
    }

    private void notifyHeightChanged() {
        if (mOnInputMethodListener != null) {
            mOnInputMethodListener.onHeightChange(mKeyBoardHeight);
        }
    }

    private void startMove() {
        if (mMoveView != null) {
            mPopupRootView.removeCallbacks(mMoveRunnable);
            calcMove();
        }
    }

    private void calcMove() {
        if (!isOpened()) {
            moveTo(0);
            return;
        }
        final View focusView = mActivityWindow.getCurrentFocus();
        if (focusView == null) {
            moveTo(0);
            return;
        }
        if (mFocusBottomMap.containsKey(focusView)) {
            View bottomView = mFocusBottomMap.get(focusView);
            if (bottomView != null) {
                calcMoveToBottom(bottomView);
            }
        } else {
            if (isFocusInMove(focusView)) {
                if (mFocusBottomView != null) {
                    calcMoveToBottom(mFocusBottomView);
                } else {
                    calcMoveToBottom(focusView);
                }
            }
        }
    }

    private boolean isFocusInMove(@NonNull View focusView) {
        if (mMoveView == null) return false;
        return mMoveView.findFocus() == focusView;
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
        if (mMoveView == null) return;
        float from = mMoveView.getTranslationY();
        moveTo(from + off);
    }

    private void moveTo(float to) {
        if (mMoveView == null) return;
        translationTo(mMoveView, Math.min(to, 0));
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        if (isOpened()) {
            if (mMoveView != null) {
                mPopupRootView.postDelayed(mMoveRunnable, 100);
            }
        }
    }

    private int getBottomViewY(@NonNull View bottomView) {
        bottomView.getLocationInWindow(mViewInWindowLocation);
        return mViewInWindowLocation[1] + bottomView.getHeight();
    }

    @NonNull
    private Rect getWindowVisibleDisplayFrame() {
        mPopupRootView.getWindowVisibleDisplayFrame(mWindowVisibleDisplayFrame);
        return mWindowVisibleDisplayFrame;
    }

    private void translationTo(@NonNull View view, float to) {
        if (mMoveAnim != null) mMoveAnim.cancel();
        float from = mMoveView.getTranslationY();
        if (from == to) return;
        mMoveAnim = ObjectAnimator.ofFloat(view, "translationY", from, to);
        mMoveAnim.setInterpolator(new DecelerateInterpolator());
        mMoveAnim.setDuration(mDuration);
        mMoveAnim.start();
    }

    private int calcKeyboardHeight() {
        Rect rect = getWindowVisibleDisplayFrame();
        int usableHeightNow = rect.height();
        int usableHeightSansKeyboard = mPopupRootView.getHeight();
        int heightDifference = usableHeightSansKeyboard - usableHeightNow;
        if (heightDifference > (usableHeightSansKeyboard / 4) || heightDifference > mKeyboardMinHeight) {
            return heightDifference;
        } else {
            return 0;
        }
    }

    @Nullable
    private View currFocusViewInMap() {
        View focusView = mActivityWindow.getCurrentFocus();
        for (View view : mFocusBottomMap.keySet()) {
            if (focusView == view) {
                return view;
            }
        }
        return null;
    }

    public interface OnInputMethodListener {
        void onOpen(@Px int height);

        void onClose(@Px int height);

        void onHeightChange(@Px int height);
    }
}
