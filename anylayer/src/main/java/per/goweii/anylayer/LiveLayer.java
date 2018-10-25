package per.goweii.anylayer;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * 描述：管理view的动态添加和移除
 * 这里有几个生命周期的回调：
 * {@link #onAttach()}
 * {@link #onAnimIn(View)}
 * {@link #onShow()}
 * {@link #onRemove()}
 * {@link #onAnimOut(View)}
 * {@link #onDetach()}
 *
 * @author Cuizhen
 * @date 2018/10/25
 */
public class LiveLayer implements View.OnKeyListener, ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnPreDrawListener {
    private ViewGroup mParent;
    private View mChild;

    private View currentKeyView = null;
    private boolean mCancelableOnClickKeyBack = true;
    private boolean mShowing = false;
    private boolean mDismissing = false;

    protected void add(@NonNull ViewGroup parent, @NonNull View child) {
        if (mParent != null || mChild != null){
            return;
        }
        mParent = parent;
        mChild = child;
        onAttach();
    }

    protected void remove() {
        onRemove();
    }

    protected void setCancelableOnClickKeyBack(boolean cancelable) {
        mCancelableOnClickKeyBack = cancelable;
    }

    /**
     * 已添加到父View
     */
    protected void onAttach() {
        if (mChild.getParent() != null) {
            return;
        }
        if (mShowing) {
            return;
        }
        mShowing = true;
        mParent.addView(mChild);
        mChild.setFocusable(true);
        mChild.setFocusableInTouchMode(true);
        mChild.requestFocus();
        currentKeyView = mChild;
        currentKeyView.setOnKeyListener(this);
        mChild.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        mChild.getViewTreeObserver().addOnPreDrawListener(this);
    }

    /**
     * 进入动画开始
     */
    protected long onAnimIn(View view) {
        return 0;
    }

    /**
     * 进入动画结束
     */
    protected void onShow() {
    }

    /**
     * 移出动画开始
     */
    protected void onRemove() {
        if (mChild.getParent() == null) {
            return;
        }
        if (mDismissing) {
            return;
        }
        mDismissing = true;
        mChild.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDismissing = false;
                onDetach();
            }
        }, onAnimOut(mChild) + 16);
    }

    /**
     * 移出动画开始
     */
    protected long onAnimOut(View view) {
        return 0;
    }

    /**
     * 已从父View移除
     */
    protected void onDetach() {
        if (currentKeyView != null) {
            currentKeyView.setOnKeyListener(null);
        }
        mChild.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
        mParent.removeView(mChild);
        mParent = null;
        mChild = null;
    }

    @Override
    public boolean onPreDraw() {
        if (mChild.getViewTreeObserver().isAlive()) {
            mChild.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        mChild.postDelayed(new Runnable() {
            @Override
            public void run() {
                mShowing = false;
                onShow();
            }
        }, onAnimIn(mChild) + 16);
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (mChild.getParent() == null) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mCancelableOnClickKeyBack) {
                    remove();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        if (currentKeyView != null) {
            currentKeyView.setOnKeyListener(null);
        }
        if (oldFocus != null) {
            oldFocus.setOnKeyListener(null);
        }
        if (newFocus != null) {
            currentKeyView = newFocus;
            currentKeyView.setOnKeyListener(LiveLayer.this);
        }
    }
}