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
class LayerManager implements View.OnKeyListener, ViewTreeObserver.OnGlobalFocusChangeListener, ViewTreeObserver.OnPreDrawListener {

    private final ViewGroup mParent;
    private final View mChild;

    private LifeListener mLifeListener = null;
    private boolean mCancelableOnClickKeyBack = true;

    private View currentKeyView = null;

    private boolean mOnAnimIn = false;
    private boolean mOnAnimOut = false;

    LayerManager(@NonNull ViewGroup parent, @NonNull View child) {
        mParent = parent;
        mChild = child;
    }

    void add() {
        if (isAdded()) {
            return;
        }
        onAttach();
    }

    void remove() {
        if (!isAdded()) {
            return;
        }
        onRemove();
    }

    private boolean isAdded(){
        return mChild.getParent() != null;
    }

    void setLifeListener(LifeListener lifeListener) {
        mLifeListener = lifeListener;
    }

    void setCancelableOnClickKeyBack(boolean cancelable) {
        mCancelableOnClickKeyBack = cancelable;
    }

    @Override
    public boolean onPreDraw() {
        if (mChild.getViewTreeObserver().isAlive()) {
            mChild.getViewTreeObserver().removeOnPreDrawListener(this);
        }
        mChild.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOnAnimIn = false;
                onShow();
            }
        }, onAnimIn(mChild));
        return true;
    }

    /**
     * 已添加到父View
     */
    private void onAttach() {
        if (mOnAnimIn) {
            return;
        }
        mOnAnimIn = true;
        mChild.setFocusable(true);
        mChild.setFocusableInTouchMode(true);
        mChild.requestFocus();
        currentKeyView = mChild;
        currentKeyView.setOnKeyListener(this);
        if (mLifeListener != null){
            mLifeListener.onAttach();
        }
        mChild.getViewTreeObserver().addOnGlobalFocusChangeListener(this);
        mChild.getViewTreeObserver().addOnPreDrawListener(this);
        mParent.addView(mChild);
    }

    /**
     * 进入动画开始
     */
    private long onAnimIn(View view) {
        if (mLifeListener != null){
            return mLifeListener.onAnimIn(view);
        }
        return 0;
    }

    /**
     * 进入动画结束，处于显示状态
     */
    private void onShow() {
        if (mLifeListener != null){
            mLifeListener.onShow();
        }
    }

    /**
     * 开始移出
     */
    private void onRemove() {
        if (mOnAnimOut) {
            return;
        }
        mOnAnimOut = true;
        mChild.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOnAnimOut = false;
                onDetach();
            }
        }, onAnimOut(mChild));
        if (mLifeListener != null){
            mLifeListener.onRemove();
        }
    }

    /**
     * 移出动画开始
     */
    private long onAnimOut(View view) {
        if (mLifeListener != null){
           return mLifeListener.onAnimOut(view);
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
        if (mLifeListener != null){
            mLifeListener.onDetach();
        }
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

    public interface LifeListener {
        void onAttach();
        long onAnimIn(View view);
        void onShow();
        void onRemove();
        long onAnimOut(View view);
        void onDetach();
    }
}