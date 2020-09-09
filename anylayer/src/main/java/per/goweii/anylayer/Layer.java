package per.goweii.anylayer;

import android.animation.Animator;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.utils.Utils;

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

    private boolean mShowWithAnim = false;
    private boolean mDismissWithAnim = false;

    private Animator mAnimatorIn = null;
    private Animator mAnimatorOut = null;

    private boolean mInitialized = false;

    public Layer() {
        mConfig = onCreateConfig();
        mViewHolder = onCreateViewHolder();
        mListenerHolder = onCreateListenerHolder();
        mViewManager = new ViewManager();
        mViewManager.setOnLifeListener(this);
        mViewManager.setOnPreDrawListener(this);
    }

    @NonNull
    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    @NonNull
    public Config getConfig() {
        return mConfig;
    }

    @NonNull
    public ListenerHolder getListenerHolder() {
        return mListenerHolder;
    }

    @NonNull
    protected Config onCreateConfig() {
        return new Config();
    }

    @NonNull
    protected ViewHolder onCreateViewHolder() {
        return new ViewHolder();
    }

    @NonNull
    protected ListenerHolder onCreateListenerHolder() {
        return new ListenerHolder();
    }

    @NonNull
    protected ViewGroup onGetParent() {
        return mViewHolder.getParent();
    }

    @NonNull
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (mViewHolder.getChildNullable() == null) {
            mViewHolder.setChild(inflater.inflate(mConfig.mChildId, parent, false));
        }
        return mViewHolder.getChild();
    }

    @Nullable
    protected Animator onCreateInAnimator(@NonNull View view) {
        if (mConfig.mAnimatorCreator != null) {
            return mConfig.mAnimatorCreator.createInAnimator(view);
        }
        return null;
    }

    @Nullable
    protected Animator onCreateOutAnimator(@NonNull View view) {
        if (mConfig.mAnimatorCreator != null) {
            return mConfig.mAnimatorCreator.createOutAnimator(view);
        }
        return null;
    }

    @Override
    public void onAttach() {
        getViewHolder().getChild().setVisibility(View.VISIBLE);
        mListenerHolder.bindClickListeners(this);
        mListenerHolder.bindLongClickListeners(this);
        mListenerHolder.notifyVisibleChangeOnShow(this);
        if (!mInitialized) {
            mInitialized = true;
            mListenerHolder.notifyOnInitialize(this);
        }
        mListenerHolder.notifyDataBinder(this);
    }

    @Override
    public void onPreDraw() {
        mListenerHolder.notifyLayerOnShowing(this);
        cancelAnimator();
        if (mShowWithAnim) {
            mAnimatorIn = onCreateInAnimator(mViewManager.requireChild());
            if (mAnimatorIn != null) {
                mAnimatorIn.addListener(new Animator.AnimatorListener() {
                    private boolean beenCanceled = false;

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!beenCanceled) {
                            onShow();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        beenCanceled = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                mAnimatorIn.start();
            } else {
                onShow();
            }
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

    public void onPreRemove() {
        mListenerHolder.notifyLayerOnDismissing(this);
        cancelAnimator();
        if (mDismissWithAnim) {
            mAnimatorOut = onCreateOutAnimator(mViewManager.requireChild());
            if (mAnimatorOut != null) {
                mAnimatorOut.addListener(new Animator.AnimatorListener() {
                    private boolean beenCanceled = false;

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!beenCanceled) {
                            // 动画执行结束后不能直接removeView，要在下一个dispatchDraw周期移除
                            // 否则会崩溃，因为viewGroup的childCount没有来得及-1，获取到的view为空
                            getViewHolder().getChild().setVisibility(View.INVISIBLE);
                            getViewHolder().getChild().post(new Runnable() {
                                @Override
                                public void run() {
                                    mViewManager.detach();
                                }
                            });
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        beenCanceled = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                mAnimatorOut.start();
            } else {
                mViewManager.detach();
            }
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

    private void cancelAnimator() {
        if (mAnimatorIn != null) {
            mAnimatorIn.cancel();
            mAnimatorIn = null;
        }
        if (mAnimatorOut != null) {
            mAnimatorOut.cancel();
            mAnimatorOut = null;
        }
    }

    @Override
    public boolean onKey(int keyCode, @NonNull KeyEvent event) {
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
        show(true);
    }

    public void show(boolean withAnim) {
        if (isShown()) return;
        if (isInAnimRunning()) return;
        mShowWithAnim = withAnim;
        mViewHolder.setParent(onGetParent());
        mViewHolder.setChild(onCreateChild(getLayoutInflater(), mViewHolder.getParent()));
        mViewManager.setParent(mViewHolder.getParent());
        mViewManager.setChild(mViewHolder.getChild());
        mViewManager.setOnKeyListener(mConfig.mInterceptKeyEvent ? this : null);
        mViewManager.attach();
    }

    public void dismiss() {
        dismiss(true);
    }

    public void dismiss(boolean withAnim) {
        if (!isShown()) return;
        if (isOutAnimRunning()) return;
        mDismissWithAnim = withAnim;
        onPreRemove();
    }

    public boolean isShown() {
        return mViewManager.isAttached();
    }

    public boolean isInAnimRunning() {
        return mAnimatorIn != null && mAnimatorIn.isStarted();
    }

    public boolean isOutAnimRunning() {
        return mAnimatorOut != null && mAnimatorOut.isStarted();
    }

    @NonNull
    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mViewHolder.getParent().getContext());
    }

    @NonNull
    public ViewManager getViewManager() {
        return mViewManager;
    }

    @NonNull
    public ViewGroup getParent() {
        return mViewHolder.getParent();
    }

    @NonNull
    public View getChild() {
        return mViewHolder.getChild();
    }

    @SuppressWarnings("unchecked")
    @Nullable
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

    @NonNull
    public Layer parent(@NonNull ViewGroup parent) {
        mViewHolder.setParent(parent);
        return this;
    }

    @NonNull
    public Layer child(@NonNull View child) {
        mViewHolder.setChild(child);
        return this;
    }

    @NonNull
    public Layer child(int child) {
        mConfig.mChildId = child;
        return this;
    }

    @NonNull
    public Layer animator(@Nullable AnimatorCreator creator) {
        mConfig.mAnimatorCreator = creator;
        return this;
    }

    @NonNull
    public Layer interceptKeyEvent(boolean intercept) {
        mConfig.mInterceptKeyEvent = intercept;
        return this;
    }

    @NonNull
    public Layer cancelableOnKeyBack(boolean cancelable) {
        if (cancelable) interceptKeyEvent(true);
        mConfig.mCancelableOnKeyBack = cancelable;
        return this;
    }

    /**
     * 绑定数据
     * 获取子控件ID为{@link #getView(int)}
     *
     * @param dataBinder 实现该接口进行数据绑定
     */
    @NonNull
    public Layer bindData(@NonNull DataBinder dataBinder) {
        mListenerHolder.addDataBinder(dataBinder);
        return this;
    }

    /**
     * 初始化
     * 获取子控件ID为{@link #getView(int)}
     *
     * @param onInitialize 该接口仅在第一次加载时调用，可加载初始化数据
     */
    @NonNull
    public Layer onInitialize(@NonNull OnInitialize onInitialize) {
        mListenerHolder.addOnInitialize(onInitialize);
        return this;
    }

    /**
     * 设置显示状态改变的监听
     *
     * @param onVisibleChangeListener OnVisibleChangeListener
     */
    @NonNull
    public Layer onVisibleChangeListener(@NonNull OnVisibleChangeListener onVisibleChangeListener) {
        mListenerHolder.addOnVisibleChangeListener(onVisibleChangeListener);
        return this;
    }

    /**
     * 设置变更为显示状态监听
     *
     * @param onShowListener OnShowListener
     */
    @NonNull
    public Layer onShowListener(@NonNull OnShowListener onShowListener) {
        mListenerHolder.addOnLayerShowListener(onShowListener);
        return this;
    }

    /**
     * 设置变更为隐藏状态监听
     *
     * @param onDismissListener OnDismissListener
     */
    @NonNull
    public Layer onDismissListener(@NonNull OnDismissListener onDismissListener) {
        mListenerHolder.addOnLayerDismissListener(onDismissListener);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewIds 控件ID
     */
    @NonNull
    public Layer onClickToDismiss(int... viewIds) {
        onClickToDismiss(null, viewIds);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param listener 监听器
     * @param viewIds  控件ID
     */
    @NonNull
    public Layer onClickToDismiss(@Nullable OnClickListener listener, int... viewIds) {
        onClick(new OnClickListener() {
            @Override
            public void onClick(@NonNull Layer decorLayer, @NonNull View v) {
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
     *
     * @param listener 回调
     * @param viewIds  控件ID
     */
    @NonNull
    public Layer onClick(@NonNull OnClickListener listener, int... viewIds) {
        mListenerHolder.addOnClickListener(listener, viewIds);
        return this;
    }

    /**
     * 对多个View绑定长按事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewIds 控件ID
     */
    @NonNull
    public Layer onLongClickToDismiss(int... viewIds) {
        onLongClickToDismiss(null, viewIds);
        return this;
    }

    /**
     * 对多个View绑定长按事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param listener 监听器
     * @param viewIds  控件ID
     */
    @NonNull
    public Layer onLongClickToDismiss(@Nullable OnLongClickListener listener, int... viewIds) {
        onLongClick(new OnLongClickListener() {
            @Override
            public boolean onLongClick(@NonNull Layer layer, @NonNull View v) {
                if (listener == null) {
                    dismiss();
                    return true;
                } else {
                    boolean result = listener.onLongClick(layer, v);
                    dismiss();
                    return result;
                }
            }
        }, viewIds);
        return this;
    }

    /**
     * 对多个View绑定长按事件
     *
     * @param listener 回调
     * @param viewIds  控件ID
     */
    @NonNull
    public Layer onLongClick(@NonNull OnLongClickListener listener, int... viewIds) {
        mListenerHolder.addOnLongClickListener(listener, viewIds);
        return this;
    }

    protected static class Config {
        private int mChildId;

        private boolean mInterceptKeyEvent = false;
        private boolean mCancelableOnKeyBack = false;

        private AnimatorCreator mAnimatorCreator = null;
    }

    public static class ViewHolder {
        private ViewGroup mParent;
        private View mChild;

        public void setParent(@NonNull ViewGroup parent) {
            mParent = parent;
        }

        @NonNull
        public ViewGroup getParent() {
            return Utils.requireNonNull(mParent, "parent未创建");
        }

        @Nullable
        protected ViewGroup getParentNullable() {
            return mParent;
        }

        public void setChild(@NonNull View child) {
            mChild = child;
        }

        @NonNull
        public View getChild() {
            return Utils.requireNonNull(mChild, "child未创建");
        }

        @Nullable
        protected View getChildNullable() {
            return mChild;
        }

        @Nullable
        protected View getNoIdClickView() {
            return null;
        }
    }

    protected static class ListenerHolder {
        private SparseArray<OnClickListener> mOnClickListeners = null;
        private SparseArray<OnLongClickListener> mOnLongClickListeners = null;
        private List<OnInitialize> mOnInitializes = null;
        private List<DataBinder> mDataBinders = null;
        private List<OnVisibleChangeListener> mOnVisibleChangeListeners = null;
        private List<OnShowListener> mOnShowListeners = null;
        private List<OnDismissListener> mOnDismissListeners = null;

        private void bindClickListeners(@NonNull Layer layer) {
            if (mOnClickListeners == null) return;
            for (int i = 0; i < mOnClickListeners.size(); i++) {
                final int viewId = mOnClickListeners.keyAt(i);
                final OnClickListener listener = mOnClickListeners.valueAt(i);
                final View view;
                if (viewId == View.NO_ID) {
                    view = layer.getViewHolder().getNoIdClickView();
                } else {
                    view = layer.getView(viewId);
                }
                if (view != null) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(@NonNull View v) {
                            listener.onClick(layer, v);
                        }
                    });
                }
            }
        }

        private void bindLongClickListeners(@NonNull Layer layer) {
            if (mOnLongClickListeners == null) return;
            for (int i = 0; i < mOnLongClickListeners.size(); i++) {
                final int viewId = mOnLongClickListeners.keyAt(i);
                final OnLongClickListener listener = mOnLongClickListeners.valueAt(i);
                final View view;
                if (viewId == View.NO_ID) {
                    view = layer.getViewHolder().getNoIdClickView();
                } else {
                    view = layer.getView(viewId);
                }
                if (view != null) {
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            return listener.onLongClick(layer, v);
                        }
                    });
                }
            }
        }

        public void addOnClickListener(@NonNull OnClickListener listener, int... viewIds) {
            if (mOnClickListeners == null) {
                mOnClickListeners = new SparseArray<>();
            }
            if (viewIds != null && viewIds.length > 0) {
                for (int id : viewIds) {
                    mOnClickListeners.put(id, listener);
                }
            } else {
                mOnClickListeners.put(View.NO_ID, listener);
            }
        }

        public void addOnLongClickListener(@NonNull OnLongClickListener listener, int... viewIds) {
            if (mOnLongClickListeners == null) {
                mOnLongClickListeners = new SparseArray<>();
            }
            if (viewIds != null && viewIds.length > 0) {
                for (int id : viewIds) {
                    mOnLongClickListeners.put(id, listener);
                }
            } else {
                mOnLongClickListeners.put(View.NO_ID, listener);
            }
        }

        private void addDataBinder(@NonNull DataBinder dataBinder) {
            if (mDataBinders == null) {
                mDataBinders = new ArrayList<>(1);
            }
            mDataBinders.add(dataBinder);
        }

        private void addOnInitialize(@NonNull OnInitialize onInitialize) {
            if (mOnInitializes == null) {
                mOnInitializes = new ArrayList<>(1);
            }
            mOnInitializes.add(onInitialize);
        }

        private void addOnVisibleChangeListener(@NonNull OnVisibleChangeListener onVisibleChangeListener) {
            if (mOnVisibleChangeListeners == null) {
                mOnVisibleChangeListeners = new ArrayList<>(1);
            }
            mOnVisibleChangeListeners.add(onVisibleChangeListener);
        }

        private void addOnLayerShowListener(@NonNull OnShowListener onShowListener) {
            if (mOnShowListeners == null) {
                mOnShowListeners = new ArrayList<>(1);
            }
            mOnShowListeners.add(onShowListener);
        }

        private void addOnLayerDismissListener(@NonNull OnDismissListener onDismissListener) {
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

        private void notifyOnInitialize(@NonNull Layer layer) {
            if (mOnInitializes != null) {
                for (OnInitialize onInitialize : mOnInitializes) {
                    onInitialize.onInit(layer);
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
        @Nullable
        Animator createInAnimator(@NonNull View target);

        /**
         * 内容消失动画
         *
         * @param target 内容
         */
        @Nullable
        Animator createOutAnimator(@NonNull View target);
    }

    public interface DataBinder {
        /**
         * 绑定数据
         */
        void bindData(@NonNull Layer layer);
    }

    public interface OnInitialize {
        /**
         * 首次加载
         */
        void onInit(@NonNull Layer layer);
    }

    public interface OnClickListener {
        /**
         * 点击事件回调
         */
        void onClick(@NonNull Layer layer, @NonNull View view);
    }

    public interface OnLongClickListener {
        /**
         * 长按事件回调
         */
        boolean onLongClick(@NonNull Layer layer, @NonNull View view);
    }

    public interface OnDismissListener {
        /**
         * 开始隐藏，动画刚开始执行
         */
        void onDismissing(@NonNull Layer layer);

        /**
         * 已隐藏，浮层已被移除
         */
        void onDismissed(@NonNull Layer layer);
    }

    public interface OnShowListener {
        /**
         * 开始显示，动画刚开始执行
         */
        void onShowing(@NonNull Layer layer);

        /**
         * 已显示，浮层已显示且动画结束
         */
        void onShown(@NonNull Layer layer);
    }

    public interface OnVisibleChangeListener {
        /**
         * 浮层显示，刚被添加到父布局，进入动画未开始
         */
        void onShow(@NonNull Layer layer);

        /**
         * 浮层隐藏，已被从父布局移除，隐藏动画已结束
         */
        void onDismiss(@NonNull Layer layer);
    }
}
