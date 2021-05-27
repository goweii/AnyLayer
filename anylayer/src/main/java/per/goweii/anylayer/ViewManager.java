package per.goweii.anylayer;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.utils.Utils;

/**
 * 管理view的动态添加和移除
 * 这里有几个生命周期的回调：
 * {@link #onAttach()}
 * {@link #onDetach()}
 */
public final class ViewManager {
    private ViewGroup mParent = null;
    private View mChild = null;

    private LayerKeyListener mLayerKeyListener = null;
    private LayerGlobalFocusChangeListener mLayerGlobalFocusChangeListener = null;

    private View mCurrentKeyView = null;

    private OnLifeListener mOnLifeListener = null;
    private OnKeyListener mOnKeyListener = null;

    public ViewManager() {
    }

    public void setParent(@Nullable ViewGroup parent) {
        mParent = parent;
    }

    public void setChild(@Nullable View child) {
        mChild = child;
        checkChildParent();
    }

    @Nullable
    public ViewGroup getParent() {
        return mParent;
    }

    @Nullable
    public View getChild() {
        return mChild;
    }

    @NonNull
    public ViewGroup requireParent() {
        return Utils.requireNonNull(mParent, "还未设置parent");
    }

    @NonNull
    public View requireChild() {
        return Utils.requireNonNull(mChild, "还未设置child");
    }

    private void checkChildParent() {
        if (mChild != null) {
            ViewGroup parent = (ViewGroup) mChild.getParent();
            if (parent != null && parent != mParent) {
                parent.removeView(mChild);
            }
        }
    }

    public void attach() {
        if (!isAttached()) {
            onAttach();
        }
    }

    public void detach() {
        if (isAttached()) {
            onDetach();
        }
    }

    public boolean isAttached() {
        return mChild != null && mParent != null && mChild.getParent() == mParent;
    }

    public void setOnLifeListener(@Nullable OnLifeListener onLifeListener) {
        mOnLifeListener = onLifeListener;
    }

    public void setOnKeyListener(@Nullable OnKeyListener onKeyListener) {
        mOnKeyListener = onKeyListener;
        if (mOnKeyListener != null) {
            if (isAttached()) {
                registerKeyListener();
            }
        } else {
            unregisterKeyListener();
        }
    }

    private void registerKeyListener() {
        mChild.setFocusable(true);
        mChild.setFocusableInTouchMode(true);
        mLayerKeyListener = new LayerKeyListener();
        findFocusViewAndBindKeyListener();
        mLayerGlobalFocusChangeListener = new LayerGlobalFocusChangeListener();
        mParent.getViewTreeObserver().addOnGlobalFocusChangeListener(mLayerGlobalFocusChangeListener);
    }

    private void unregisterKeyListener() {
        if (mCurrentKeyView != null) {
            mCurrentKeyView.setOnKeyListener(null);
            mLayerKeyListener = null;
        }
        if (mLayerGlobalFocusChangeListener != null) {
            if (mParent.getViewTreeObserver().isAlive()) {
                mParent.getViewTreeObserver().removeOnGlobalFocusChangeListener(mLayerGlobalFocusChangeListener);
            }
            mLayerGlobalFocusChangeListener = null;
        }
    }

    /**
     * 添加到父View
     */
    private void onAttach() {
        mParent.addView(mChild);
        if (mOnKeyListener != null) {
            registerKeyListener();
        }
        if (mOnLifeListener != null) {
            mOnLifeListener.onAttach();
        }
    }

    /**
     * 从父View移除
     */
    private void onDetach() {
        unregisterKeyListener();
        mParent.removeView(mChild);
        if (mOnLifeListener != null) {
            mOnLifeListener.onDetach();
        }
    }

    private void findFocusViewAndBindKeyListener() {
        Utils.onViewLayout(mChild, new Runnable() {
            @Override
            public void run() {
                if (mCurrentKeyView != null) {
                    mCurrentKeyView.setOnKeyListener(null);
                    mCurrentKeyView = null;
                }
                if (isAttached()) {
                    mCurrentKeyView = mChild.findFocus();
                    if (mCurrentKeyView != null) {
                        mCurrentKeyView.setOnKeyListener(mLayerKeyListener);
                        return;
                    }
                    mChild.requestFocus();
                    mCurrentKeyView = mChild.findFocus();
                    if (mCurrentKeyView != null) {
                        mCurrentKeyView.setOnKeyListener(mLayerKeyListener);
                    }
                }
            }
        });
    }

    private final class LayerGlobalFocusChangeListener implements ViewTreeObserver.OnGlobalFocusChangeListener {
        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            if (mCurrentKeyView != null) {
                mCurrentKeyView.setOnKeyListener(null);
                mCurrentKeyView = null;
            }
            if (isAttached()) {
                mCurrentKeyView = mChild.findFocus();
                if (mCurrentKeyView != null) {
                    mCurrentKeyView.setOnKeyListener(mLayerKeyListener);
                    return;
                }
                View rootFocus = mChild.getRootView().findFocus();
                if (rootFocus != null) {
                    return;
                }
                mChild.requestFocus();
                mCurrentKeyView = mChild.findFocus();
                if (mCurrentKeyView != null) {
                    mCurrentKeyView.setOnKeyListener(mLayerKeyListener);
                }
            }
        }
    }

    private final class LayerKeyListener implements View.OnKeyListener {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (!isAttached()) {
                return false;
            }
            if (mOnKeyListener == null) {
                return false;
            }
            return mOnKeyListener.onKey(keyCode, event);
        }
    }

    public interface OnLifeListener {
        void onAttach();

        void onDetach();
    }

    public interface OnKeyListener {
        boolean onKey(int keyCode, KeyEvent event);
    }
}