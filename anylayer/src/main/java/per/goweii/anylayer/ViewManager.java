package per.goweii.anylayer;

import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

/**
 * 描述：管理view的动态添加和移除
 * 这里有几个生命周期的回调：
 * {@link #onAttach()}
 * {@link #onDetach()}
 *
 * @author Cuizhen
 * @date 2018/10/25
 */
public final class ViewManager {

    private final ViewGroup mParent;
    private final View mChild;
    private final LayerKeyListener mLayerKeyListener;
    private final LayerGlobalFocusChangeListener mLayerGlobalFocusChangeListener;

    private View currentKeyView = null;

    private OnLifeListener mOnLifeListener = null;
    private OnPreDrawListener mOnPreDrawListener = null;
    private OnKeyListener mOnKeyListener = null;

    public ViewManager(@NonNull ViewGroup parent, @NonNull View child) {
        mParent = parent;
        mChild = child;
        checkChildParent();
        mLayerKeyListener = new LayerKeyListener();
        mLayerGlobalFocusChangeListener = new LayerGlobalFocusChangeListener();
    }

    /**
     * 确保子控件未被添加到非当前父布局，否则移除
     */
    private void checkChildParent() {
        ViewGroup parent = (ViewGroup) mChild.getParent();
        if (parent != null && parent != mParent) {
            parent.removeView(mChild);
        }
    }

    public void attach() {
        if (!isAttached()) {
            mParent.post(new Runnable() {
                @Override
                public void run() {
                    if (!isAttached()) {
                        onAttach();
                    }
                }
            });
        }
    }

    public void detach() {
        if (isAttached()) {
            onDetach();
        }
    }

    public boolean isAttached(){
        return mChild.getParent() != null;
    }

    public void setOnLifeListener(OnLifeListener onLifeListener) {
        mOnLifeListener = onLifeListener;
    }

    public void setOnPreDrawListener(OnPreDrawListener onPreDrawListener) {
        mOnPreDrawListener = onPreDrawListener;
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        mOnKeyListener = onKeyListener;
    }

    /**
     * 添加到父View
     */
    private void onAttach() {
        mChild.getViewTreeObserver().addOnGlobalFocusChangeListener(mLayerGlobalFocusChangeListener);
        mChild.getViewTreeObserver().addOnPreDrawListener(new LayerPreDrawListener());
        mChild.setFocusable(true);
        mChild.setFocusableInTouchMode(true);
        mChild.requestFocus();
        currentKeyView = mChild;
        currentKeyView.setOnKeyListener(mLayerKeyListener);
        mParent.addView(mChild);
        if (mOnLifeListener != null){
            mOnLifeListener.onAttach();
        }
    }

    /**
     * 从父View移除
     */
    private void onDetach() {
        if (currentKeyView != null) {
            currentKeyView.setOnKeyListener(null);
        }
        mChild.getViewTreeObserver().removeOnGlobalFocusChangeListener(mLayerGlobalFocusChangeListener);
        mParent.removeView(mChild);
        if (mOnLifeListener != null){
            mOnLifeListener.onDetach();
        }
    }

    private final class LayerPreDrawListener implements ViewTreeObserver.OnPreDrawListener{
        @Override
        public boolean onPreDraw() {
            if (mChild.getViewTreeObserver().isAlive()) {
                mChild.getViewTreeObserver().removeOnPreDrawListener(this);
            }
            if (mOnPreDrawListener != null) {
                mOnPreDrawListener.onPreDraw();
            }
            return true;
        }
    }

    private final class LayerGlobalFocusChangeListener implements ViewTreeObserver.OnGlobalFocusChangeListener {
        @Override
        public void onGlobalFocusChanged(android.view.View oldFocus, android.view.View newFocus) {
            if (currentKeyView != null) {
                currentKeyView.setOnKeyListener(null);
            }
            if (oldFocus != null) {
                oldFocus.setOnKeyListener(null);
            }
            if (newFocus != null) {
                currentKeyView = newFocus;
                currentKeyView.setOnKeyListener(mLayerKeyListener);
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

    public interface OnPreDrawListener {
        void onPreDraw();
    }

    public interface OnKeyListener {
        boolean onKey(int keyCode, KeyEvent event);
    }
}