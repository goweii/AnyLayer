package per.goweii.anylayer;

import android.animation.Animator;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CuiZhen
 * @date 2019/7/20
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public abstract class Layer implements ViewManager.OnLifeListener, ViewManager.OnKeyListener, ViewManager.OnPreDrawListener {

    protected final ViewManager mViewManager;
    protected final ListenerHolder mListenerHolder;

    private SparseArray<View> mViewCaches = null;
    private SparseArray<OnLayerClickListener> mOnLayerClickListeners = null;

    private boolean mInterceptKeyEvent = true;
    private boolean mCancelableOnKeyDown = true;

    private Animator mAnimatorIn = null;
    private Animator mAnimatorOut = null;

    public Layer() {
        mListenerHolder = new ListenerHolder();
        mViewManager = new ViewManager();
        mViewManager.setOnLifeListener(this);
        mViewManager.setOnPreDrawListener(this);
    }

    @NonNull
    protected abstract ViewGroup onGetParent();

    @NonNull
    protected abstract View onCreateLayer(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    @Nullable
    protected abstract Animator onCreateInAnimator(@NonNull View view);

    @Nullable
    protected abstract Animator onCreateOutAnimator(@NonNull View view);

    @Override
    public void onAttach() {
        bindListener();
        mListenerHolder.notifyVisibleChangeOnShow();
        mListenerHolder.notifyDataBinder();
    }

    @Override
    public void onPreDraw() {
        mListenerHolder.notifyLayerOnShowing();
        mAnimatorIn = onCreateInAnimator(mViewManager.getChild());
        if (mAnimatorIn != null) {
            mAnimatorIn.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    onShow();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            mAnimatorIn.start();
        } else {
            onShow();
        }
    }

    public void onShow() {
        mListenerHolder.notifyLayerOnShown();
        if (mAnimatorIn != null) {
            mAnimatorIn = null;
        }
    }

    public void onPerRemove() {
        mListenerHolder.notifyLayerOnDismissing();
        mAnimatorOut = onCreateOutAnimator(mViewManager.getChild());
        if (mAnimatorOut != null) {
            mAnimatorOut.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mViewManager.detach();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            mAnimatorOut.start();
        } else {
            mViewManager.detach();
        }
    }

    @Override
    public void onDetach() {
        mListenerHolder.notifyVisibleChangeOnDismiss();
        mListenerHolder.notifyLayerOnDismissed();
        if (mAnimatorOut != null) {
            mAnimatorOut = null;
        }
    }

    @Override
    public boolean onKey(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mCancelableOnKeyDown) {
                    dismiss();
                }
                return true;
            }
        }
        return false;
    }

    public void show() {
        ViewGroup parent = onGetParent();
        View child = onCreateLayer(LayoutInflater.from(parent.getContext()), parent);
        mViewManager.setParent(parent);
        mViewManager.setChild(child);
        mViewManager.setOnKeyListener(mInterceptKeyEvent ? this : null);
        mViewManager.attach();
    }

    public boolean isShow() {
        return mViewManager != null && mViewManager.isAttached();
    }

    public void dismiss() {
        onPerRemove();
    }

    @NonNull
    public ViewManager getViewManager() {
        return mViewManager;
    }

    @NonNull
    public ViewGroup getParent() {
        ViewGroup parent = mViewManager.getParent();
        if (parent == null) {
            throw new RuntimeException("You have to call it after the show method");
        }
        return parent;
    }

    @NonNull
    public View getChild() {
        View child = mViewManager.getChild();
        if (child == null) {
            throw new RuntimeException("You have to call it after the show method");
        }
        return child;
    }

    public void interceptKeyEvent(boolean interceptKeyEvent) {
        mInterceptKeyEvent = interceptKeyEvent;
    }

    public void cancelableOnKeyDown(boolean cancelable) {
        mCancelableOnKeyDown = cancelable;
    }

    @NonNull
    public <V extends View> V getView(@IdRes int viewId) {
        if (mViewCaches == null) {
            mViewCaches = new SparseArray<>();
        }
        if (mViewCaches.indexOfKey(viewId) < 0) {
            V view = getChild().findViewById(viewId);
            mViewCaches.put(viewId, view);
            return view;
        }
        return (V) mViewCaches.get(viewId);
    }

    /**
     * 绑定数据
     * 获取子控件ID为{@link #getView(int)}
     *
     * @param dataBinder 实现该接口进行数据绑定
     */
    public Layer bindData(DataBinder dataBinder) {
        mListenerHolder.addDataBinder(dataBinder);
        return this;
    }

    /**
     * 设置显示状态改变的监听
     *
     * @param onVisibleChangeListener OnVisibleChangeListener
     */
    public Layer onVisibleChangeListener(DecorLayer.OnVisibleChangeListener onVisibleChangeListener) {
        mListenerHolder.addOnVisibleChangeListener(onVisibleChangeListener);
        return this;
    }

    /**
     * 设置变更为显示状态监听
     *
     * @param onLayerShowListener OnLayerShowListener
     */
    public Layer onLayerShowListener(DecorLayer.OnLayerShowListener onLayerShowListener) {
        mListenerHolder.addOnLayerShowListener(onLayerShowListener);
        return this;
    }

    /**
     * 设置变更为隐藏状态监听
     *
     * @param onLayerDismissListener OnLayerDismissListener
     */
    public Layer onLayerDismissListener(DecorLayer.OnLayerDismissListener onLayerDismissListener) {
        mListenerHolder.addOnLayerDismissListener(onLayerDismissListener);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param listener 监听器
     * @param viewIds  控件ID
     */
    public Layer onClickToDismiss(OnLayerClickListener listener, @IdRes int... viewIds) {
        onClick(new OnLayerClickListener() {
            @Override
            public void onClick(Layer decorLayer, View v) {
                if (listener != null) {
                    listener.onClick(decorLayer, v);
                }
                dismiss();
            }
        }, viewIds);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewIds 控件ID
     */
    public Layer onClickToDismiss(@IdRes int... viewIds) {
        onClick(new OnLayerClickListener() {
            @Override
            public void onClick(Layer decorLayer, View v) {
                dismiss();
            }
        }, viewIds);
        return this;
    }

    public Layer onClick(OnLayerClickListener listener, @IdRes int... viewIds) {
        if (mOnLayerClickListeners == null) {
            mOnLayerClickListeners = new SparseArray<>();
        }
        if (viewIds != null && viewIds.length > 0) {
            for (int id : viewIds) {
                if (mOnLayerClickListeners.indexOfKey(id) < 0) {
                    mOnLayerClickListeners.put(id, listener);
                }
            }
        }
        return this;
    }

    private void bindListener() {
        if (mOnLayerClickListeners == null) {
            return;
        }
        for (int i = 0; i < mOnLayerClickListeners.size(); i++) {
            int viewId = mOnLayerClickListeners.keyAt(i);
            final OnLayerClickListener listener = mOnLayerClickListeners.valueAt(i);
            getView(viewId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(Layer.this, v);
                }
            });
        }
    }

    private final class ListenerHolder {

        private List<DataBinder> mDataBinders = null;
        private List<OnVisibleChangeListener> mOnVisibleChangeListeners = null;
        private List<OnLayerShowListener> mOnLayerShowListeners = null;
        private List<OnLayerDismissListener> mOnLayerDismissListeners = null;

        private void addDataBinder(DataBinder dataBinder) {
            if (mDataBinders == null) {
                mDataBinders = new ArrayList<>(1);
            }
            mDataBinders.add(dataBinder);
        }

        private void addOnVisibleChangeListener(OnVisibleChangeListener onVisibleChangeListener) {
            if (mOnVisibleChangeListeners == null) {
                mOnVisibleChangeListeners = new ArrayList<>(1);
            }
            mOnVisibleChangeListeners.add(onVisibleChangeListener);
        }

        private void addOnLayerShowListener(OnLayerShowListener onLayerShowListener) {
            if (mOnLayerShowListeners == null) {
                mOnLayerShowListeners = new ArrayList<>(1);
            }
            mOnLayerShowListeners.add(onLayerShowListener);
        }

        private void addOnLayerDismissListener(OnLayerDismissListener onLayerDismissListener) {
            if (mOnLayerDismissListeners == null) {
                mOnLayerDismissListeners = new ArrayList<>(1);
            }
            mOnLayerDismissListeners.add(onLayerDismissListener);
        }

        private void notifyDataBinder() {
            if (mDataBinders != null) {
                for (DataBinder dataBinder : mDataBinders) {
                    dataBinder.bindData(Layer.this);
                }
            }
        }

        private void notifyVisibleChangeOnShow() {
            if (mOnVisibleChangeListeners != null) {
                for (OnVisibleChangeListener onVisibleChangeListener : mOnVisibleChangeListeners) {
                    onVisibleChangeListener.onShow(Layer.this);
                }
            }
        }

        private void notifyVisibleChangeOnDismiss() {
            if (mOnVisibleChangeListeners != null) {
                for (OnVisibleChangeListener onVisibleChangeListener : mOnVisibleChangeListeners) {
                    onVisibleChangeListener.onDismiss(Layer.this);
                }
            }
        }

        private void notifyLayerOnShowing() {
            if (mOnLayerShowListeners != null) {
                for (OnLayerShowListener onLayerShowListener : mOnLayerShowListeners) {
                    onLayerShowListener.onShowing(Layer.this);
                }
            }
        }

        private void notifyLayerOnShown() {
            if (mOnLayerShowListeners != null) {
                for (OnLayerShowListener onLayerShowListener : mOnLayerShowListeners) {
                    onLayerShowListener.onShown(Layer.this);
                }
            }
        }

        private void notifyLayerOnDismissing() {
            if (mOnLayerDismissListeners != null) {
                for (OnLayerDismissListener onLayerDismissListener : mOnLayerDismissListeners) {
                    onLayerDismissListener.onDismissing(Layer.this);
                }
            }
        }

        private void notifyLayerOnDismissed() {
            if (mOnLayerDismissListeners != null) {
                for (OnLayerDismissListener onLayerDismissListener : mOnLayerDismissListeners) {
                    onLayerDismissListener.onDismissed(Layer.this);
                }
            }
        }
    }

    public interface AnimatorCreator {
        /**
         * 内容进入动画
         *
         * @param target 内容
         */
        @NonNull
        Animator createInAnimator(View target);

        /**
         * 内容消失动画
         *
         * @param target 内容
         */
        @NonNull
        Animator createOutAnimator(View target);
    }

    public interface DataBinder {
        /**
         * 绑定数据
         */
        void bindData(Layer layer);
    }

    public interface OnLayerClickListener {
        /**
         * 点击事件回调
         */
        void onClick(Layer layer, View v);
    }

    public interface OnLayerDismissListener {
        /**
         * 开始隐藏，动画刚开始执行
         */
        void onDismissing(Layer layer);

        /**
         * 已隐藏，浮层已被移除
         */
        void onDismissed(Layer layer);
    }

    public interface OnLayerShowListener {
        /**
         * 开始显示，动画刚开始执行
         */
        void onShowing(Layer layer);

        /**
         * 已显示，浮层已显示且动画结束
         */
        void onShown(Layer layer);
    }

    public interface OnVisibleChangeListener {
        /**
         * 浮层显示，刚被添加到父布局，进入动画未开始
         */
        void onShow(Layer layer);

        /**
         * 浮层隐藏，已被从父布局移除，隐藏动画已结束
         */
        void onDismiss(Layer layer);
    }
}
