package per.goweii.anylayer;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * 描述：管理view的动态添加和移除
 *
 * @author Cuizhen
 * @date 2018/10/25
 */
public class LayerManager implements View.OnKeyListener, ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnPreDrawListener {
    private final ViewGroup mParent;
    private final View mChild;

    private View currentKeyView = null;
    private boolean mCancelableOnClickKeyBack = true;
    private boolean mShowing = false;
    private boolean mDismissing = false;

    private LayerManager(@NonNull ViewGroup parent, @NonNull View child) {
        mParent = parent;
        mChild = child;
    }

    public static LayerManager with(@NonNull ViewGroup parent, @NonNull View child) {
        return new LayerManager(parent, child);
    }

    public void setCancelableOnClickKeyBack(boolean cancelable) {
        mCancelableOnClickKeyBack = cancelable;
    }

    public void add() {
        onAttach();
    }

    public void remove() {
        onRemove();
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
     * 移出动画结束
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
        mParent.removeView(mChild);
        mChild.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
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
            currentKeyView.setOnKeyListener(LayerManager.this);
        }
    }
}