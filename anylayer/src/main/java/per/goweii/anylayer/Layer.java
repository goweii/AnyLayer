package per.goweii.anylayer;

import android.animation.Animator;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
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
public class Layer implements ViewManager.OnLifeListener, ViewManager.OnKeyListener, ViewManager.OnPreDrawListener {

    private final ViewManager mViewManager;
    private final ViewHolder mViewHolder;
    private final ListenerHolder mListenerHolder;
    private final Config mConfig;

    private SparseArray<View> mViewCaches = null;

    private Animator mAnimatorIn = null;
    private Animator mAnimatorOut = null;

    public Layer() {
        mConfig = onCreateConfig();
        mViewHolder = onCreateViewHolder();
        mListenerHolder = onCreateListenerHolder();
        mViewManager = new ViewManager();
        mViewManager.setOnLifeListener(this);
        mViewManager.setOnPreDrawListener(this);
    }

    @NonNull
    protected ViewHolder onCreateViewHolder() {
        return new ViewHolder();
    }

    @NonNull
    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    @NonNull
    protected Config onCreateConfig() {
        return new Config();
    }

    @NonNull
    public Config getConfig() {
        return mConfig;
    }

    @NonNull
    protected ListenerHolder onCreateListenerHolder() {
        return new ListenerHolder();
    }

    @NonNull
    public ListenerHolder getListenerHolder() {
        return mListenerHolder;
    }

    @NonNull
    protected ViewGroup onGetParent() {
        return mViewHolder.getParent();
    }

    @NonNull
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (mViewHolder.getChild() == null) {
            mViewHolder.setChild(inflater.inflate(mConfig.mChildId, parent, false));
        }
        return mViewHolder.getChild();
    }

    @Nullable
    protected Animator onCreateInAnimator(@NonNull View view) {
        if (mConfig.mAnimatorCreator == null) {
            return null;
        }
        return mConfig.mAnimatorCreator.createInAnimator(view);
    }

    @Nullable
    protected Animator onCreateOutAnimator(@NonNull View view) {
        if (mConfig.mAnimatorCreator == null) {
            return null;
        }
        return mConfig.mAnimatorCreator.createOutAnimator(view);
    }

    @Override
    public void onAttach() {
        mListenerHolder.bindClickListeners(this);
        mListenerHolder.notifyVisibleChangeOnShow(this);
        mListenerHolder.notifyDataBinder(this);
    }

    @Override
    public void onPreDraw() {
        mListenerHolder.notifyLayerOnShowing(this);
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
        mListenerHolder.notifyLayerOnShown(this);
        if (mAnimatorIn != null) {
            mAnimatorIn = null;
        }
    }

    public void onPerRemove() {
        mListenerHolder.notifyLayerOnDismissing(this);
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
        mListenerHolder.notifyVisibleChangeOnDismiss(this);
        mListenerHolder.notifyLayerOnDismissed(this);
        if (mAnimatorOut != null) {
            mAnimatorOut = null;
        }
    }

    @Override
    public boolean onKey(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mConfig.mCancelableOnKeyBack) {
                    dismiss();
                }
                return true;
            }
        }
        return false;
    }

    public void show() {
        mViewHolder.setParent(onGetParent());
        mViewHolder.setChild(onCreateChild(LayoutInflater.from(mViewHolder.getParent().getContext()), mViewHolder.getParent()));
        mViewManager.setParent(mViewHolder.getParent());
        mViewManager.setChild(mViewHolder.getChild());
        mViewManager.setOnKeyListener(mConfig.mInterceptKeyEvent ? this : null);
        mViewManager.attach();
    }

    public void dismiss() {
        onPerRemove();
    }

    public boolean isShow() {
        return mViewManager.isAttached();
    }

    @NonNull
    public ViewManager getViewManager() {
        return mViewManager;
    }

    @NonNull
    public ViewGroup getParent() {
        if (mViewHolder.getParent() == null) {
            throw new RuntimeException("You have to call it after the show method");
        }
        return mViewHolder.getParent();
    }

    @NonNull
    public View getChild() {
        if (mViewHolder.getChild() == null) {
            throw new RuntimeException("You have to call it after the show method");
        }
        return mViewHolder.getChild();
    }

    @NonNull
    public <V extends View> V getView(@IdRes int id) {
        if (mViewCaches == null) {
            mViewCaches = new SparseArray<>();
        }
        if (mViewCaches.indexOfKey(id) < 0) {
            V view = getChild().findViewById(id);
            mViewCaches.put(id, view);
            return view;
        }
        return (V) mViewCaches.get(id);
    }

    public Layer parent(@NonNull ViewGroup parent) {
        mViewHolder.setParent(parent);
        return this;
    }

    public Layer child(@NonNull View child) {
        mViewHolder.setChild(child);
        return this;
    }

    public Layer child(@LayoutRes int child) {
        mConfig.mChildId = child;
        return this;
    }

    public Layer animator(AnimatorCreator creator) {
        mConfig.mAnimatorCreator = creator;
        return this;
    }

    public Layer interceptKeyEvent(boolean intercept) {
        mConfig.mInterceptKeyEvent = intercept;
        return this;
    }

    public Layer cancelableOnKeyBack(boolean cancelable) {
        mConfig.mCancelableOnKeyBack = cancelable;
        return this;
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
    public Layer onVisibleChangeListener(OnVisibleChangeListener onVisibleChangeListener) {
        mListenerHolder.addOnVisibleChangeListener(onVisibleChangeListener);
        return this;
    }

    /**
     * 设置变更为显示状态监听
     *
     * @param onShowListener OnShowListener
     */
    public Layer onShowListener(OnShowListener onShowListener) {
        mListenerHolder.addOnLayerShowListener(onShowListener);
        return this;
    }

    /**
     * 设置变更为隐藏状态监听
     *
     * @param onDismissListener OnDismissListener
     */
    public Layer onDismissListener(OnDismissListener onDismissListener) {
        mListenerHolder.addOnLayerDismissListener(onDismissListener);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param listener 监听器
     * @param viewIds  控件ID
     */
    public Layer onClickToDismiss(OnClickListener listener, @IdRes int... viewIds) {
        onClick(new OnClickListener() {
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
        onClick(new OnClickListener() {
            @Override
            public void onClick(Layer decorLayer, View v) {
                dismiss();
            }
        }, viewIds);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     *
     * @param listener 回调
     * @param viewIds  控件ID
     */
    public Layer onClick(OnClickListener listener, @IdRes int... viewIds) {
        mListenerHolder.addOnClickListener(listener, viewIds);
        return this;
    }

    protected static class Config {
        private int mChildId;

        private boolean mInterceptKeyEvent = true;
        private boolean mCancelableOnKeyBack = true;

        private AnimatorCreator mAnimatorCreator = null;
    }

    public static class ViewHolder {
        private ViewGroup mParent;
        private View mChild;

        public void setParent(@NonNull ViewGroup parent) {
            mParent = parent;
        }

        public ViewGroup getParent() {
            return mParent;
        }

        public void setChild(@NonNull View child) {
            mChild = child;
        }

        public View getChild() {
            return mChild;
        }
    }

    protected static class ListenerHolder {
        private SparseArray<OnClickListener> mOnClickListeners = null;
        private List<DataBinder> mDataBinders = null;
        private List<OnVisibleChangeListener> mOnVisibleChangeListeners = null;
        private List<OnShowListener> mOnShowListeners = null;
        private List<OnDismissListener> mOnDismissListeners = null;

        private void bindClickListeners(@NonNull Layer layer) {
            if (mOnClickListeners == null) {
                return;
            }
            for (int i = 0; i < mOnClickListeners.size(); i++) {
                int viewId = mOnClickListeners.keyAt(i);
                final OnClickListener listener = mOnClickListeners.valueAt(i);
                layer.getView(viewId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(layer, v);
                    }
                });
            }
        }

        public void addOnClickListener(OnClickListener listener, @IdRes int... viewIds) {
            if (mOnClickListeners == null) {
                mOnClickListeners = new SparseArray<>();
            }
            if (viewIds != null && viewIds.length > 0) {
                for (int id : viewIds) {
                    if (mOnClickListeners.indexOfKey(id) < 0) {
                        mOnClickListeners.put(id, listener);
                    }
                }
            }
        }

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

        private void addOnLayerShowListener(OnShowListener onShowListener) {
            if (mOnShowListeners == null) {
                mOnShowListeners = new ArrayList<>(1);
            }
            mOnShowListeners.add(onShowListener);
        }

        private void addOnLayerDismissListener(OnDismissListener onDismissListener) {
            if (mOnDismissListeners == null) {
                mOnDismissListeners = new ArrayList<>(1);
            }
            mOnDismissListeners.add(onDismissListener);
        }

        private void notifyDataBinder(@NonNull Layer layer) {
            if (mDataBinders != null) {
                for (DataBinder dataBinder : mDataBinders) {
                    dataBinder.bindData(layer);
                }
            }
        }

        private void notifyVisibleChangeOnShow(@NonNull Layer layer) {
            if (mOnVisibleChangeListeners != null) {
                for (OnVisibleChangeListener onVisibleChangeListener : mOnVisibleChangeListeners) {
                    onVisibleChangeListener.onShow(layer);
                }
            }
        }

        private void notifyVisibleChangeOnDismiss(@NonNull Layer layer) {
            if (mOnVisibleChangeListeners != null) {
                for (OnVisibleChangeListener onVisibleChangeListener : mOnVisibleChangeListeners) {
                    onVisibleChangeListener.onDismiss(layer);
                }
            }
        }

        private void notifyLayerOnShowing(@NonNull Layer layer) {
            if (mOnShowListeners != null) {
                for (OnShowListener onShowListener : mOnShowListeners) {
                    onShowListener.onShowing(layer);
                }
            }
        }

        private void notifyLayerOnShown(@NonNull Layer layer) {
            if (mOnShowListeners != null) {
                for (OnShowListener onShowListener : mOnShowListeners) {
                    onShowListener.onShown(layer);
                }
            }
        }

        private void notifyLayerOnDismissing(@NonNull Layer layer) {
            if (mOnDismissListeners != null) {
                for (OnDismissListener onDismissListener : mOnDismissListeners) {
                    onDismissListener.onDismissing(layer);
                }
            }
        }

        private void notifyLayerOnDismissed(@NonNull Layer layer) {
            if (mOnDismissListeners != null) {
                for (OnDismissListener onDismissListener : mOnDismissListeners) {
                    onDismissListener.onDismissed(layer);
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
        Animator createInAnimator(View target);

        /**
         * 内容消失动画
         *
         * @param target 内容
         */
        Animator createOutAnimator(View target);
    }

    public interface DataBinder {
        /**
         * 绑定数据
         */
        void bindData(Layer layer);
    }

    public interface OnClickListener {
        /**
         * 点击事件回调
         */
        void onClick(Layer layer, View v);
    }

    public interface OnDismissListener {
        /**
         * 开始隐藏，动画刚开始执行
         */
        void onDismissing(Layer layer);

        /**
         * 已隐藏，浮层已被移除
         */
        void onDismissed(Layer layer);
    }

    public interface OnShowListener {
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
