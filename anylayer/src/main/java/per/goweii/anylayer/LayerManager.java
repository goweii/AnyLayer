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
public class LayerManager implements View.OnKeyListener, ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnPreDrawListener {
    private final ViewGroup mParent;
    private final View mChild;

    private View currentKeyView = null;
    private boolean mCancelableOnClickKeyBack = true;
    private boolean mShowing = false;
    private boolean mDismissing = false;

    public LayerManager(@NonNull ViewGroup parent, @NonNull View child) {
        mParent = parent;
        mChild = child;
    }

    public void add() {
        if (mChild.getParent() != null) {
            return;
        }
        onAttach();
    }

    public void remove() {
        if (mChild.getParent() == null) {
            return;
        }
        onRemove();
    }

    public void setCancelableOnClickKeyBack(boolean cancelable) {
        mCancelableOnClickKeyBack = cancelable;
    }

    /**
     * 已添加到父View
     */
    private void onAttach() {
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
        if (mLiveListener != null){
            mLiveListener.onAttach();
        }
    }

    /**
     * 进入动画开始
     */
    private long onAnimIn(View view) {
        if (mLiveListener != null){
            return mLiveListener.onAnimIn(view);
        }
        return 0;
    }

    /**
     * 进入动画结束
     */
    private void onShow() {
        if (mLiveListener != null){
            mLiveListener.onShow();
        }
    }

    /**
     * 移出动画开始
     */
    private void onRemove() {
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
        if (mLiveListener != null){
            mLiveListener.onRemove();
        }
    }

    /**
     * 移出动画开始
     */
    private long onAnimOut(View view) {
        if (mLiveListener != null){
           return mLiveListener.onAnimOut(view);
        }
        return 0;
    }

    /**
     * 已从父View移除
     */
    private void onDetach() {
        if (currentKeyView != null) {
            currentKeyView.setOnKeyListener(null);
        }
        mChild.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
        mParent.removeView(mChild);
        if (mLiveListener != null){
            mLiveListener.onDetach();
        }
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

    private LiveListener mLiveListener = null;

    public void setLiveListener(LiveListener liveListener) {
        mLiveListener = liveListener;
    }

    public interface LiveListener {
        void onAttach();
        long onAnimIn(View view);
        void onShow();
        void onRemove();
        long onAnimOut(View view);
        void onDetach();
    }
}