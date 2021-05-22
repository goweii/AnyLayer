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
    private final ViewTreeObserver.OnPreDrawListener mOnGlobalPreDrawListener = new OnGlobalPreDrawListener();
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new OnGlobalLayoutListener();
    private final ViewManager.OnKeyListener mOnViewKeyListener = new OnViewKeyListener();

    private final ViewManager mViewManager;
    private final ViewHolder mViewHolder;
    private final ListenerHolder mListenerHolder;
    private final Config mConfig;

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

    @CallSuper
    protected void onCreate() {
        onGetParent();
        onCreateChild(getLayoutInflater(), mViewHolder.getParent());
        mViewManager.setParent(mViewHolder.getParent());
        mViewManager.setChild(mViewHolder.getChild());
        mViewManager.setOnKeyListener(mConfig.mInterceptKeyEvent ? mOnViewKeyListener : null);
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
        mListenerHolder.notifyOnVisibleChangeToShow(this);
        mListenerHolder.notifyOnBindData(this);
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
        mListenerHolder.notifyOnVisibleChangeToDismiss(this);
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

    protected boolean onKeyBack() {
        if (mConfig.mCancelableOnKeyBack) {
            dismiss();
        }
        return true;
    }

    protected boolean onKeyEvent(int keyCode, @NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return onKeyBack();
            }
        }
        return false;
    }

    protected void onGlobalLayout() {
    }

    protected boolean onGlobalPreDraw() {
        return true;
    }

    private void handleShow() {
        if (isShown()) {
            if (isOutAnimRunning()) {
                startAnimatorIn(new Runnable() {
                    @Override
                    public void run() {
                        onPostShow();
                    }
                });
            }
            return;
        }
        onCreate();
        mViewManager.attach();
        onAttach();
        getViewHolder().getChild().setVisibility(View.VISIBLE);
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (getViewTreeObserver().isAlive()) {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                }
                onPreShow();
                startAnimatorIn(new Runnable() {
                    @Override
                    public void run() {
                        onPostShow();
                    }
                });
                return true;
            }
        });
    }

    private void startAnimatorIn(@NonNull Runnable onEnd) {
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
                        onEnd.run();
                    }
                });
                mAnimatorIn.start();
            } else {
                onEnd.run();
            }
        } else {
            onEnd.run();
        }
    }

    private void handleDismiss() {
        if (!isShown()) return;
        if (isOutAnimRunning()) return;
        onPreDismiss();
        startAnimatorOut(new Runnable() {
            @Override
            public void run() {
                onPostDismiss();
                mViewManager.detach();
                onDetach();
                onDestroy();
            }
        });
    }

    private void startAnimatorOut(@NonNull final Runnable onEnd) {
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
                        getViewHolder().getChild().setVisibility(View.INVISIBLE);
                        getViewHolder().getParent().post(onEnd);
                    }
                });
                mAnimatorOut.start();
            } else {
                getViewHolder().getChild().setVisibility(View.INVISIBLE);
                onEnd.run();
            }
        } else {
            getViewHolder().getChild().setVisibility(View.INVISIBLE);
            onEnd.run();
        }
    }

    private ViewTreeObserver getViewTreeObserver() {
        return getViewHolder().getParent().getViewTreeObserver();
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
    public <V extends View> V requireView(@IdRes int id) {
        return Utils.requireNonNull(mViewHolder.findViewInChild(id));
    }

    @Nullable
    public <V extends View> V findView(@IdRes int id) {
        return mViewHolder.findViewInChild(id);
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
     * 获取子控件ID为{@link #findView(int)}
     *
     * @param onBindDataListener 实现该接口进行数据绑定
     */
    @NonNull
    public Layer addOnBindDataListener(@NonNull OnBindDataListener onBindDataListener) {
        mListenerHolder.addOnBindDataListener(onBindDataListener);
        return this;
    }

    /**
     * 初始化
     * 获取子控件ID为{@link #findView(int)}
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
     * @param onVisibleChangedListener OnVisibleChangeListener
     */
    @NonNull
    public Layer addOnVisibleChangeListener(@NonNull OnVisibleChangedListener onVisibleChangedListener) {
        mListenerHolder.addOnVisibleChangeListener(onVisibleChangedListener);
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
    public Layer addOnClickToDismissListener(int... viewIds) {
        addOnClickToDismissListener(null, viewIds);
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
    public Layer addOnClickToDismissListener(@Nullable OnClickListener listener, int... viewIds) {
        addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(@NonNull Layer layer, @NonNull View v) {
                layer.dismiss();
                if (listener != null) {
                    listener.onClick(layer, v);
                }
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
    public Layer addOnClickListener(@NonNull OnClickListener listener, int... viewIds) {
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
    public Layer addOnLongClickToDismissListener(int... viewIds) {
        addOnLongClickToDismissListener(null, viewIds);
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
                    dismiss();
                    return listener.onLongClick(layer, v);
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
        protected View getChildNullable() {
            return mChild;
        }

        @Nullable
        protected View getNoIdClickView() {
            return null;
        }

        @SuppressWarnings("unchecked")
        @Nullable
        public final <V extends View> V findViewInChild(@IdRes int id) {
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
        private List<OnBindDataListener> mOnBindDataListeners = null;
        private List<OnVisibleChangedListener> mOnVisibleChangedListeners = null;
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
                    view = layer.findView(viewId);
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
                    view = layer.findView(viewId);
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

        private void addOnBindDataListener(@NonNull OnBindDataListener onBindDataListener) {
            if (mOnBindDataListeners == null) {
                mOnBindDataListeners = new ArrayList<>(1);
            }
            mOnBindDataListeners.add(onBindDataListener);
        }

        private void addOnInitializeListener(@NonNull OnInitializeListener onInitializeListener) {
            if (mOnInitializeListeners == null) {
                mOnInitializeListeners = new ArrayList<>(1);
            }
            mOnInitializeListeners.add(onInitializeListener);
        }

        private void addOnVisibleChangeListener(@NonNull OnVisibleChangedListener onVisibleChangedListener) {
            if (mOnVisibleChangedListeners == null) {
                mOnVisibleChangedListeners = new ArrayList<>(1);
            }
            mOnVisibleChangedListeners.add(onVisibleChangedListener);
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

        private void notifyOnBindData(@NonNull Layer layer) {
            if (mOnBindDataListeners != null) {
                for (OnBindDataListener onBindDataListener : mOnBindDataListeners) {
                    onBindDataListener.onBindData(layer);
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

        private void notifyOnVisibleChangeToShow(@NonNull Layer layer) {
            if (mOnVisibleChangedListeners != null) {
                for (OnVisibleChangedListener onVisibleChangedListener : mOnVisibleChangedListeners) {
                    onVisibleChangedListener.onShow(layer);
                }
            }
        }

        private void notifyOnVisibleChangeToDismiss(@NonNull Layer layer) {
            if (mOnVisibleChangedListeners != null) {
                for (OnVisibleChangedListener onVisibleChangedListener : mOnVisibleChangedListeners) {
                    onVisibleChangedListener.onDismiss(layer);
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

    private class OnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @Override
        public void onGlobalLayout() {
            Layer.this.onGlobalLayout();
        }
    }

    private class OnGlobalPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        @Override
        public boolean onPreDraw() {
            return Layer.this.onGlobalPreDraw();
        }
    }

    private class OnViewKeyListener implements ViewManager.OnKeyListener {
        @Override
        public boolean onKey(int keyCode, KeyEvent keyEvent) {
            return Layer.this.onKeyEvent(keyCode, keyEvent);
        }
    }

    public interface AnimatorCreator {
        /**
         * 进入动画
         *
         * @param target 目标View
         */
        @Nullable
        Animator createInAnimator(@NonNull View target);

        /**
         * 消失动画
         *
         * @param target 目标View
         */
        @Nullable
        Animator createOutAnimator(@NonNull View target);
    }

    public interface OnInitializeListener {
        /**
         * 首次加载
         */
        void onInitialize(@NonNull Layer layer);
    }

    public interface OnBindDataListener {
        /**
         * 绑定数据
         */
        void onBindData(@NonNull Layer layer);
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

    public interface OnVisibleChangedListener {
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
