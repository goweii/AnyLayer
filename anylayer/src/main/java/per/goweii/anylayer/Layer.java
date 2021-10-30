package per.goweii.anylayer;

import android.animation.Animator;
import android.os.Build;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.utils.DefaultAnimatorListener;
import per.goweii.anylayer.utils.Utils;

public class Layer {
    private final ViewTreeObserver.OnPreDrawListener mOnGlobalPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            Layer.this.onGlobalPreDraw();
            return true;
        }
    };

    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Layer.this.onGlobalLayout();
        }
    };

    private final ViewManager.OnKeyListener mOnLayerKeyListener = new ViewManager.OnKeyListener() {
        @Override
        public boolean onKey(int keyCode, KeyEvent event) {
            return Layer.this.onKey(keyCode, event);
        }
    };

    private final Runnable mInAnimEndCallback = new Runnable() {
        @Override
        public void run() {
            Layer.this.handleInAnimEnd();
        }
    };

    private final Runnable mOutAnimEndCallback = new Runnable() {
        @Override
        public void run() {
            Layer.this.handleOutAnimEnd();
        }
    };

    private final ViewManager mViewManager;
    private final ViewHolder mViewHolder;
    private final ListenerHolder mListenerHolder;
    private final Config mConfig;

    private ViewTreeObserver.OnPreDrawListener mShowOnPreDrawListener = null;

    private boolean mShowWithAnim = false;
    private boolean mDismissWithAnim = false;

    private Animator mAnimatorIn = null;
    private Animator mAnimatorOut = null;

    private boolean mInitialized = false;

    public Layer() {
        mViewManager = new ViewManager();
        mConfig = onCreateConfig();
        mViewHolder = onCreateViewHolder();
        mListenerHolder = onCreateListenerHolder();
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
        if (mViewHolder.getChildOrNull() == null) {
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

    @CallSuper
    protected void onCreate() {
        mViewHolder.setParent(onGetParent());
        mViewHolder.setChild(onCreateChild(getLayoutInflater(), mViewHolder.getParent()));
        mViewManager.setParent(mViewHolder.getParent());
        mViewManager.setChild(mViewHolder.getChild());
        mViewManager.setOnKeyListener(mConfig.mInterceptKeyEvent ? mOnLayerKeyListener : null);
        if (!mInitialized) {
            mInitialized = true;
            mListenerHolder.notifyOnInitialize(this);
        }
    }

    @CallSuper
    protected void onAttach() {
        if (getViewTreeObserver().isAlive()) {
            getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
            getViewTreeObserver().addOnPreDrawListener(mOnGlobalPreDrawListener);
        }
        mListenerHolder.bindOnClickListeners(this);
        mListenerHolder.bindOnLongClickListeners(this);
        mListenerHolder.notifyVisibleChangeOnShow(this);
        mListenerHolder.notifyBindData(this);
    }

    @CallSuper
    protected void onPreShow() {
        mListenerHolder.notifyOnPreShow(this);
    }

    @CallSuper
    protected void onPostShow() {
        mListenerHolder.notifyOnPostShow(this);
    }

    @CallSuper
    protected void onPreDismiss() {
        mListenerHolder.notifyOnPreDismiss(this);
    }

    @CallSuper
    protected void onPostDismiss() {
        mListenerHolder.notifyOnPostDismiss(this);
    }

    @CallSuper
    protected void onDetach() {
        mListenerHolder.notifyVisibleChangeOnDismiss(this);
        if (getViewTreeObserver().isAlive()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            } else {
                getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
            }
            getViewTreeObserver().removeOnPreDrawListener(mOnGlobalPreDrawListener);
        }
    }

    @CallSuper
    protected void onDestroy() {
        mViewManager.setParent(null);
        mViewManager.setChild(null);
        mViewManager.setOnKeyListener(null);
    }

    protected boolean onKey(int keyCode, @NonNull KeyEvent event) {
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

    protected void onGlobalLayout() {
    }

    protected void onGlobalPreDraw() {
    }

    private void handleShow() {
        if (isShown()) {
            if (isOutAnimRunning()) {
                startAnimatorIn();
            }
            return;
        }
        onCreate();
        mViewManager.attach();
        onAttach();
        getViewHolder().getChild().setVisibility(View.VISIBLE);
        if (mShowOnPreDrawListener == null) {
            mShowOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mShowOnPreDrawListener = null;
                    if (getViewTreeObserver().isAlive()) {
                        getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    onPreShow();
                    startAnimatorIn();
                    return true;
                }
            };
        }
        getViewTreeObserver().addOnPreDrawListener(mShowOnPreDrawListener);
    }

    private void startAnimatorIn() {
        cancelAnimator();
        if (mShowWithAnim) {
            mAnimatorIn = onCreateInAnimator(mViewHolder.getChild());
            if (mAnimatorIn != null) {
                mAnimatorIn.addListener(new DefaultAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mAnimatorIn = null;
                    }

                    @Override
                    public void onAnimationEndNotCanceled(Animator animation) {
                        super.onAnimationEndNotCanceled(animation);
                        mInAnimEndCallback.run();
                    }
                });
                mAnimatorIn.start();
            } else {
                mInAnimEndCallback.run();
            }
        } else {
            mInAnimEndCallback.run();
        }
    }

    private void handleInAnimEnd() {
        onPostShow();
    }

    private void handleDismiss() {
        if (!isShown()) return;
        if (isOutAnimRunning()) return;
        if (mShowOnPreDrawListener != null) {
            mShowOnPreDrawListener = null;
            if (getViewTreeObserver().isAlive()) {
                getViewTreeObserver().removeOnPreDrawListener(mShowOnPreDrawListener);
            }
            mViewManager.detach();
            onDetach();
            onDestroy();
            return;
        }
        onPreDismiss();
        startAnimatorOut();
    }

    private void startAnimatorOut() {
        cancelAnimator();
        if (mDismissWithAnim) {
            mAnimatorOut = onCreateOutAnimator(mViewHolder.getChild());
            if (mAnimatorOut != null) {
                mAnimatorOut.addListener(new DefaultAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mAnimatorOut = null;
                    }

                    @Override
                    public void onAnimationEndNotCanceled(Animator animation) {
                        super.onAnimationEndNotCanceled(animation);
                        // 动画执行结束后不能直接removeView，要在下一个dispatchDraw周期移除
                        // 否则会崩溃，因为viewGroup的childCount没有来得及-1，获取到的view为空
                        getViewHolder().getChild().setVisibility(View.INVISIBLE);
                        getViewHolder().getParent().post(mOutAnimEndCallback);
                    }
                });
                mAnimatorOut.start();
            } else {
                getViewHolder().getChild().setVisibility(View.INVISIBLE);
                mOutAnimEndCallback.run();
            }
        } else {
            getViewHolder().getChild().setVisibility(View.INVISIBLE);
            mOutAnimEndCallback.run();
        }
    }

    private void handleOutAnimEnd() {
        onPostDismiss();
        mViewManager.detach();
        onDetach();
        onDestroy();
    }

    private ViewTreeObserver getViewTreeObserver() {
        return getViewHolder().getParent().getViewTreeObserver();
    }

    private void cancelAnimator() {
        getViewHolder().getParent().removeCallbacks(mInAnimEndCallback);
        getViewHolder().getParent().removeCallbacks(mOutAnimEndCallback);
        if (mAnimatorIn != null) {
            mAnimatorIn.removeAllListeners();
            mAnimatorIn.cancel();
            mAnimatorIn = null;
        }
        if (mAnimatorOut != null) {
            mAnimatorOut.removeAllListeners();
            mAnimatorOut.cancel();
            mAnimatorOut = null;
        }
    }

    public void show() {
        show(true);
    }

    public void show(boolean withAnim) {
        mShowWithAnim = withAnim;
        handleShow();
    }

    public void dismiss() {
        dismiss(true);
    }

    public void dismiss(boolean withAnim) {
        mDismissWithAnim = withAnim;
        handleDismiss();
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
    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mViewHolder.getParent().getContext());
    }

    @NonNull
    public ViewGroup getParent() {
        return mViewHolder.getParent();
    }

    @NonNull
    public View getChild() {
        return mViewHolder.getChild();
    }

    @NonNull
    public <V extends View> V requireViewById(@IdRes int id) {
        return Utils.requireNonNull(mViewHolder.findViewById(id));
    }

    @Nullable
    public <V extends View> V findViewById(@IdRes int id) {
        return mViewHolder.findViewById(id);
    }

    @NonNull
    public Layer setParent(@NonNull ViewGroup parent) {
        mViewHolder.setParent(parent);
        return this;
    }

    @NonNull
    public Layer setChild(@NonNull View child) {
        mViewHolder.setChild(child);
        return this;
    }

    @NonNull
    public Layer setChild(int child) {
        mConfig.mChildId = child;
        return this;
    }

    @NonNull
    public Layer setAnimator(@Nullable AnimatorCreator creator) {
        mConfig.mAnimatorCreator = creator;
        return this;
    }

    @NonNull
    public Layer setInterceptKeyEvent(boolean intercept) {
        mConfig.mInterceptKeyEvent = intercept;
        return this;
    }

    @NonNull
    public Layer setCancelableOnKeyBack(boolean cancelable) {
        if (cancelable) setInterceptKeyEvent(true);
        mConfig.mCancelableOnKeyBack = cancelable;
        return this;
    }

    /**
     * 绑定数据
     * 获取子控件ID为{@link #findViewById(int)}
     *
     * @param dataBindCallback 实现该接口进行数据绑定
     */
    @NonNull
    public Layer addDataBindCallback(@NonNull DataBindCallback dataBindCallback) {
        mListenerHolder.addDataBindCallback(dataBindCallback);
        return this;
    }

    /**
     * 初始化
     * 获取子控件ID为{@link #findViewById(int)}
     *
     * @param onInitializeListener 该接口仅在第一次加载时调用，可加载初始化数据
     */
    @NonNull
    public Layer addOnInitializeListener(@NonNull OnInitializeListener onInitializeListener) {
        mListenerHolder.addOnInitializeListener(onInitializeListener);
        return this;
    }

    /**
     * 设置显示状态改变的监听
     *
     * @param onVisibleChangeListener OnVisibleChangeListener
     */
    @NonNull
    public Layer addOnVisibleChangeListener(@NonNull OnVisibleChangeListener onVisibleChangeListener) {
        mListenerHolder.addOnVisibleChangeListener(onVisibleChangeListener);
        return this;
    }

    /**
     * 设置变更为显示状态监听
     *
     * @param onShowListener OnShowListener
     */
    @NonNull
    public Layer addOnShowListener(@NonNull OnShowListener onShowListener) {
        mListenerHolder.addOnShowListener(onShowListener);
        return this;
    }

    /**
     * 设置变更为隐藏状态监听
     *
     * @param onDismissListener OnDismissListener
     */
    @NonNull
    public Layer addOnDismissListener(@NonNull OnDismissListener onDismissListener) {
        mListenerHolder.addOnDismissListener(onDismissListener);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewIds 控件ID
     */
    @NonNull
    public Layer addOnClickToDismiss(@IdRes int... viewIds) {
        addOnClickToDismissListener(null, viewIds);
        return this;
    }

    /**
     * 对View绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewId   控件ID
     * @param listener 监听器
     */
    @NonNull
    public Layer addOnClickToDismissListener(@IdRes int viewId, @Nullable OnClickListener listener) {
        addOnClickToDismissListener(listener, viewId);
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
    public Layer addOnClickToDismissListener(@Nullable OnClickListener listener, @IdRes int... viewIds) {
        addOnClickListener(new OnClickListener() {
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
     * 对View绑定点击事件
     *
     * @param viewId   控件ID
     * @param listener 回调
     */
    @NonNull
    public Layer addOnClickListener(@IdRes int viewId, @NonNull OnClickListener listener) {
        addOnClickListener(listener, viewId);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     *
     * @param listener 回调
     * @param viewIds  控件ID
     */
    @NonNull
    public Layer addOnClickListener(@NonNull OnClickListener listener, @IdRes int... viewIds) {
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
    public Layer addOnLongClickToDismiss(@IdRes int... viewIds) {
        addOnLongClickToDismissListener(null, viewIds);
        return this;
    }

    /**
     * 对View绑定长按事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewId   控件ID
     * @param listener 监听器
     */
    @NonNull
    public Layer addOnLongClickToDismissListener(@IdRes int viewId, @Nullable OnLongClickListener listener) {
        addOnLongClickToDismissListener(listener, viewId);
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
    public Layer addOnLongClickToDismissListener(@Nullable OnLongClickListener listener, int... viewIds) {
        addOnLongClickListener(new OnLongClickListener() {
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
     * 对View绑定长按事件
     *
     * @param viewId   控件ID
     * @param listener 回调
     */
    @NonNull
    public Layer addOnLongClickListener(@IdRes int viewId, @NonNull OnLongClickListener listener) {
        addOnLongClickListener(listener, viewId);
        return this;
    }

    /**
     * 对多个View绑定长按事件
     *
     * @param listener 回调
     * @param viewIds  控件ID
     */
    @NonNull
    public Layer addOnLongClickListener(@NonNull OnLongClickListener listener, int... viewIds) {
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

        private SparseArray<View> mViewCaches = null;

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
        protected View getChildOrNull() {
            return mChild;
        }

        @Nullable
        protected View getNoIdClickView() {
            return null;
        }

        @SuppressWarnings("unchecked")
        @Nullable
        public final <V extends View> V findViewById(@IdRes int id) {
            if (mChild == null) {
                return null;
            }
            if (mViewCaches == null) {
                mViewCaches = new SparseArray<>();
            }
            View view = mViewCaches.get(id);
            if (view == null) {
                view = mChild.findViewById(id);
                if (view != null) {
                    mViewCaches.put(id, view);
                }
            }
            return (V) view;
        }
    }

    protected static class ListenerHolder {
        private SparseArray<OnClickListener> mOnClickListeners = null;
        private SparseArray<OnLongClickListener> mOnLongClickListeners = null;
        private List<OnInitializeListener> mOnInitializeListeners = null;
        private List<DataBindCallback> mDataBindCallbacks = null;
        private List<OnVisibleChangeListener> mOnVisibleChangeListeners = null;
        private List<OnShowListener> mOnShowListeners = null;
        private List<OnDismissListener> mOnDismissListeners = null;

        private void bindOnClickListeners(@NonNull Layer layer) {
            if (mOnClickListeners == null) return;
            for (int i = 0; i < mOnClickListeners.size(); i++) {
                final int viewId = mOnClickListeners.keyAt(i);
                final OnClickListener listener = mOnClickListeners.valueAt(i);
                final View view;
                if (viewId == View.NO_ID) {
                    view = layer.getViewHolder().getNoIdClickView();
                } else {
                    view = layer.findViewById(viewId);
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

        private void bindOnLongClickListeners(@NonNull Layer layer) {
            if (mOnLongClickListeners == null) return;
            for (int i = 0; i < mOnLongClickListeners.size(); i++) {
                final int viewId = mOnLongClickListeners.keyAt(i);
                final OnLongClickListener listener = mOnLongClickListeners.valueAt(i);
                final View view;
                if (viewId == View.NO_ID) {
                    view = layer.getViewHolder().getNoIdClickView();
                } else {
                    view = layer.findViewById(viewId);
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

        private void addDataBindCallback(@NonNull DataBindCallback dataBindCallback) {
            if (mDataBindCallbacks == null) {
                mDataBindCallbacks = new ArrayList<>(1);
            }
            mDataBindCallbacks.add(dataBindCallback);
        }

        private void addOnInitializeListener(@NonNull OnInitializeListener onInitializeListener) {
            if (mOnInitializeListeners == null) {
                mOnInitializeListeners = new ArrayList<>(1);
            }
            mOnInitializeListeners.add(onInitializeListener);
        }

        private void addOnVisibleChangeListener(@NonNull OnVisibleChangeListener onVisibleChangeListener) {
            if (mOnVisibleChangeListeners == null) {
                mOnVisibleChangeListeners = new ArrayList<>(1);
            }
            mOnVisibleChangeListeners.add(onVisibleChangeListener);
        }

        private void addOnShowListener(@NonNull OnShowListener onShowListener) {
            if (mOnShowListeners == null) {
                mOnShowListeners = new ArrayList<>(1);
            }
            mOnShowListeners.add(onShowListener);
        }

        private void addOnDismissListener(@NonNull OnDismissListener onDismissListener) {
            if (mOnDismissListeners == null) {
                mOnDismissListeners = new ArrayList<>(1);
            }
            mOnDismissListeners.add(onDismissListener);
        }

        private void notifyBindData(@NonNull Layer layer) {
            if (mDataBindCallbacks != null) {
                for (DataBindCallback dataBindCallback : mDataBindCallbacks) {
                    dataBindCallback.bindData(layer);
                }
            }
        }

        private void notifyOnInitialize(@NonNull Layer layer) {
            if (mOnInitializeListeners != null) {
                for (OnInitializeListener onInitializeListener : mOnInitializeListeners) {
                    onInitializeListener.onInitialize(layer);
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

        private void notifyOnPreShow(@NonNull Layer layer) {
            if (mOnShowListeners != null) {
                for (OnShowListener onShowListener : mOnShowListeners) {
                    onShowListener.onPreShow(layer);
                }
            }
        }

        private void notifyOnPostShow(@NonNull Layer layer) {
            if (mOnShowListeners != null) {
                for (OnShowListener onShowListener : mOnShowListeners) {
                    onShowListener.onPostShow(layer);
                }
            }
        }

        private void notifyOnPreDismiss(@NonNull Layer layer) {
            if (mOnDismissListeners != null) {
                for (OnDismissListener onDismissListener : mOnDismissListeners) {
                    onDismissListener.onPreDismiss(layer);
                }
            }
        }

        private void notifyOnPostDismiss(@NonNull Layer layer) {
            if (mOnDismissListeners != null) {
                for (OnDismissListener onDismissListener : mOnDismissListeners) {
                    onDismissListener.onPostDismiss(layer);
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

    public interface DataBindCallback {
        /**
         * 绑定数据
         */
        void bindData(@NonNull Layer layer);
    }

    public interface OnInitializeListener {
        /**
         * 首次加载
         */
        void onInitialize(@NonNull Layer layer);
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
        void onPreDismiss(@NonNull Layer layer);

        /**
         * 已隐藏，浮层已被移除
         */
        void onPostDismiss(@NonNull Layer layer);
    }

    public interface OnShowListener {
        /**
         * 开始显示，动画刚开始执行
         */
        void onPreShow(@NonNull Layer layer);

        /**
         * 已显示，浮层已显示且动画结束
         */
        void onPostShow(@NonNull Layer layer);
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
