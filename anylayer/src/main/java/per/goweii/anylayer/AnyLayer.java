package per.goweii.anylayer;

import android.animation.Animator;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Objects;

import per.goweii.burred.Blurred;

/**
 * @author Cuizhen
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class AnyLayer {

    private final LayerManager mLayerManager;
    private SoftInputHelper mSoftInputHelper = null;

    @Deprecated
    public static void init(@NonNull Application application) {
        ActivityHolder.init(application);
    }

    public static void initBlurred(@NonNull Context context) {
        Blurred.init(context);
    }

    public static void recycleBlurred() {
        Blurred.recycle();
    }

    /**
     * 此时不需要APP存在Activity实例
     * 会新启动一个Activity并向根布局添加一个浮层
     *
     * @param context 上下文
     */
    public static void with(@Nullable Context context, LayerActivity.OnLayerCreatedCallback callback) {
        final Context contextFinal;
        if (context == null) {
            contextFinal = ActivityHolder.getApplication();
        } else {
            contextFinal = context;
        }
        LayerActivity.start(contextFinal, callback);
    }

    /**
     * 向窗口根布局添加一个浮层
     */
    public static AnyLayer with() {
        return new AnyLayer();
    }

    /**
     * 向父布局viewGroup添加一个浮层
     *
     * @param viewGroup 浮层父布局
     */
    public static AnyLayer with(@NonNull ViewGroup viewGroup) {
        return new AnyLayer(viewGroup);
    }

    /**
     * 向窗口根布局添加一个浮层
     *
     * @param context 上下文，不能是ApplicationContext
     */
    public static AnyLayer with(@NonNull Context context) {
        return new AnyLayer(context);
    }

    /**
     * 向窗口根布局添加一个浮层，且位置参照targetView
     * 及类似PopupWindow效果
     *
     * @param targetView 位置参照View
     */
    public static AnyLayer target(@NonNull View targetView) {
        return new AnyLayer(targetView);
    }

    private AnyLayer() {
        Activity activity = ActivityHolder.getCurrentActivity();
        if (activity == null) {
            throw new RuntimeException("要是用全局弹窗必须先在Application中调用AnyLayer.init(Application)方法初始化");
        }
        FrameLayout rootView = (FrameLayout) activity.getWindow().getDecorView();
        FrameLayout activityContentView = rootView.findViewById(android.R.id.content);
        mLayerManager = new LayerManager(this, activity, rootView, null, activityContentView);
    }

    private AnyLayer(@NonNull ViewGroup viewGroup) {
        mLayerManager = new LayerManager(this, viewGroup.getContext(), viewGroup, null, null);
    }

    private AnyLayer(@NonNull Context context) {
        FrameLayout rootView = (FrameLayout) Objects.requireNonNull(Utils.getActivity(context)).getWindow().getDecorView();
        FrameLayout activityContentView = rootView.findViewById(android.R.id.content);
        mLayerManager = new LayerManager(this, context, rootView, null, activityContentView);
    }

    private AnyLayer(@NonNull View targetView) {
        Context context = targetView.getContext();
        FrameLayout rootView = (FrameLayout) Objects.requireNonNull(Utils.getActivity(context)).getWindow().getDecorView();
        FrameLayout activityContentView = rootView.findViewById(android.R.id.content);
        mLayerManager = new LayerManager(this, context, rootView, targetView, activityContentView);
    }

    /**
     * 设置自自定义View
     *
     * @param contentView 自定以View
     */
    public AnyLayer contentView(@NonNull View contentView) {
        mLayerManager.mViewHolder.setContent(contentView);
        return this;
    }

    /**
     * 设置自定义布局文件
     *
     * @param contentViewId 自定义布局ID
     */
    public AnyLayer contentView(@LayoutRes int contentViewId) {
        return contentView(mLayerManager.mLayoutInflater.inflate(contentViewId, mLayerManager.mViewHolder.getContainer(), false));
    }

    /**
     * 设置自定义布局文件中状态栏的占位View
     * 该控件高度将设置为状态栏高度，可用来使布局整体下移，避免状态栏遮挡
     *
     * @param statusBarId 状态栏的占位View
     */
    public AnyLayer asStatusBar(@IdRes int statusBarId) {
        mLayerManager.mConfig.mAsStatusBarViewId = statusBarId;
        return this;
    }

    /**
     * 绑定数据
     * 获取子控件ID为{@link #getView(int)}
     *
     * @param dataBinder 实现该接口进行数据绑定
     */
    public AnyLayer bindData(LayerManager.IDataBinder dataBinder) {
        mLayerManager.mListenerHolder.addDataBinder(dataBinder);
        return this;
    }

    /**
     * 设置显示状态改变的监听
     *
     * @param onVisibleChangeListener OnVisibleChangeListener
     */
    public AnyLayer onVisibleChangeListener(LayerManager.OnVisibleChangeListener onVisibleChangeListener) {
        mLayerManager.mListenerHolder.addOnVisibleChangeListener(onVisibleChangeListener);
        return this;
    }

    /**
     * 设置变更为显示状态监听
     *
     * @param onLayerShowListener OnLayerShowListener
     */
    public AnyLayer onLayerShowListener(LayerManager.OnLayerShowListener onLayerShowListener) {
        mLayerManager.mListenerHolder.addOnLayerShowListener(onLayerShowListener);
        return this;
    }

    /**
     * 设置变更为隐藏状态监听
     *
     * @param onLayerDismissListener OnLayerDismissListener
     */
    public AnyLayer onLayerDismissListener(LayerManager.OnLayerDismissListener onLayerDismissListener) {
        mLayerManager.mListenerHolder.addOnLayerDismissListener(onLayerDismissListener);
        return this;
    }

    /**
     * 设置子布局的gravity
     * 可直接在布局文件指定layout_gravity属性，作用相同
     *
     * @param gravity {@link Gravity}
     */
    public AnyLayer gravity(int gravity) {
        mLayerManager.mConfig.mGravity = gravity;
        return this;
    }

    /**
     * 当以target方式创建时为参照View位置显示
     * 可自己指定浮层相对于参照View的对齐方式
     *
     * @param direction  主方向
     * @param horizontal 水平对齐方式
     * @param vertical   垂直对齐方式
     * @param inside     是否强制位于屏幕内部
     */
    public AnyLayer alignment(@NonNull Alignment.Direction direction,
                              @NonNull Alignment.Horizontal horizontal,
                              @NonNull Alignment.Vertical vertical,
                              boolean inside) {
        mLayerManager.mConfig.mAlignmentDirection = direction;
        mLayerManager.mConfig.mAlignmentHorizontal = horizontal;
        mLayerManager.mConfig.mAlignmentVertical = vertical;
        mLayerManager.mConfig.mAlignmentInside = inside;
        return this;
    }

    /**
     * 自定义浮层的进入和退出动画
     * 可使用工具类{@link AnimHelper}
     *
     * @param contentAnim IAnim接口
     */
    public AnyLayer contentAnim(LayerManager.IAnim contentAnim) {
        if (contentAnim != null) {
            mLayerManager.mContentInAnimExecutor.setCreator(new AnimExecutor.Creator() {
                @Nullable
                @Override
                public Animator create(View target) {
                    return contentAnim.inAnim(target);
                }
            });
            mLayerManager.mContentOutAnimExecutor.setCreator(new AnimExecutor.Creator() {
                @Nullable
                @Override
                public Animator create(View target) {
                    return contentAnim.outAnim(target);
                }
            });
        }
        return this;
    }

    /**
     * 自定义浮层的进入动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer contentInAnim(@AnimRes int anim) {
        mLayerManager.mContentInAnimExecutor.setAnimation(AnimationUtils.loadAnimation(mLayerManager.mContext, anim));
        return this;
    }

    /**
     * 自定义浮层的进入动画
     *
     * @param anim Animation动画
     */
    public AnyLayer contentInAnim(@NonNull Animation anim) {
        mLayerManager.mContentInAnimExecutor.setAnimation(anim);
        return this;
    }

    /**
     * 自定义浮层的退出动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer contentOutAnim(@AnimRes int anim) {
        mLayerManager.mContentOutAnimExecutor.setAnimation(AnimationUtils.loadAnimation(mLayerManager.mContext, anim));
        return this;
    }

    /**
     * 自定义浮层的退出动画
     *
     * @param anim Animation动画
     */
    public AnyLayer contentOutAnim(@NonNull Animation anim) {
        mLayerManager.mContentOutAnimExecutor.setAnimation(anim);
        return this;
    }

    /**
     * 自定义背景的进入和退出动画
     * 可使用工具类{@link AnimHelper}
     *
     * @param backgroundAnim IAnim接口
     */
    public AnyLayer backgroundAnim(LayerManager.IAnim backgroundAnim) {
        if (backgroundAnim != null) {
            mLayerManager.mBackgroundInAnimExecutor.setCreator(new AnimExecutor.Creator() {
                @Nullable
                @Override
                public Animator create(View target) {
                    return backgroundAnim.inAnim(target);
                }
            });
            mLayerManager.mBackgroundOutAnimExecutor.setCreator(new AnimExecutor.Creator() {
                @Nullable
                @Override
                public Animator create(View target) {
                    return backgroundAnim.outAnim(target);
                }
            });
        }
        return this;
    }

    /**
     * 自定义背景的进入动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer backgroundInAnim(@AnimRes int anim) {
        mLayerManager.mBackgroundInAnimExecutor.setAnimation(AnimationUtils.loadAnimation(mLayerManager.mContext, anim));
        return this;
    }

    /**
     * 自定义背景的退出动画
     *
     * @param anim Animation动画
     */
    public AnyLayer backgroundInAnim(@NonNull Animation anim) {
        mLayerManager.mBackgroundInAnimExecutor.setAnimation(anim);
        return this;
    }

    /**
     * 自定义背景的退出动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer backgroundOutAnim(@AnimRes int anim) {
        mLayerManager.mBackgroundOutAnimExecutor.setAnimation(AnimationUtils.loadAnimation(mLayerManager.mContext, anim));
        return this;
    }

    /**
     * 自定义背景的退出动画
     *
     * @param anim Animation动画
     */
    public AnyLayer backgroundOutAnim(@NonNull Animation anim) {
        mLayerManager.mBackgroundOutAnimExecutor.setAnimation(anim);
        return this;
    }

    /**
     * 默认浮层进入和退出动画时长
     *
     * @param defaultAnimDuration 时长
     */
    public AnyLayer defaultContentAnimDuration(long defaultAnimDuration) {
        mLayerManager.mContentInAnimExecutor.setDuration(defaultAnimDuration);
        mLayerManager.mContentOutAnimExecutor.setDuration(defaultAnimDuration);
        return this;
    }

    /**
     * 默认浮层进入动画时长
     *
     * @param defaultInAnimDuration 时长
     */
    public AnyLayer defaultContentInAnimDuration(long defaultInAnimDuration) {
        mLayerManager.mContentInAnimExecutor.setDuration(defaultInAnimDuration);
        return this;
    }

    /**
     * 默认浮层退出动画时长
     *
     * @param defaultOutAnimDuration 时长
     */
    public AnyLayer defaultContentOutAnimDuration(long defaultOutAnimDuration) {
        mLayerManager.mContentOutAnimExecutor.setDuration(defaultOutAnimDuration);
        return this;
    }

    /**
     * 默认背景进入和退出动画时长
     *
     * @param defaultAnimDuration 时长
     */
    public AnyLayer defaultBackgroundAnimDuration(long defaultAnimDuration) {
        mLayerManager.mBackgroundInAnimExecutor.setDuration(defaultAnimDuration);
        mLayerManager.mBackgroundOutAnimExecutor.setDuration(defaultAnimDuration);
        return this;
    }

    /**
     * 默认背景进入动画时长
     *
     * @param defaultInAnimDuration 时长
     */
    public AnyLayer defaultBackgroundInAnimDuration(long defaultInAnimDuration) {
        mLayerManager.mBackgroundInAnimExecutor.setDuration(defaultInAnimDuration);
        return this;
    }

    /**
     * 默认背景退出动画时长
     *
     * @param defaultOutAnimDuration 时长
     */
    public AnyLayer defaultBackgroundOutAnimDuration(long defaultOutAnimDuration) {
        mLayerManager.mBackgroundOutAnimExecutor.setDuration(defaultOutAnimDuration);
        return this;
    }

    /**
     * 设置背景为当前activity的高斯模糊效果
     * 设置之后其他背景设置方法失效，仅{@link #backgroundColorInt(int)}生效
     * 且设置的backgroundColor值调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时的{@link #backgroundColorInt(int)}传入的为半透明颜色
     *
     * @param radius 模糊半径
     */
    public AnyLayer backgroundBlurRadius(@FloatRange(from = 0, fromInclusive = false, to = 25) float radius) {
        mLayerManager.mConfig.mBackgroundBlurRadius = radius;
        return this;
    }

    public AnyLayer backgroundBlurPercent(@FloatRange(from = 0, fromInclusive = false) float percent) {
        mLayerManager.mConfig.mBackgroundBlurPercent = percent;
        return this;
    }

    /**
     * 设置背景高斯模糊的缩小比例
     *
     * @param scale 缩小比例
     */
    public AnyLayer backgroundBlurScale(@FloatRange(from = 1) float scale) {
        mLayerManager.mConfig.mBackgroundBlurScale = scale;
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param bitmap 图片
     */
    public AnyLayer backgroundBitmap(@NonNull Bitmap bitmap) {
        mLayerManager.mConfig.mBackgroundBitmap = bitmap;
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param resource 资源ID
     */
    public AnyLayer backgroundResource(@DrawableRes int resource) {
        mLayerManager.mConfig.mBackgroundResource = resource;
        return this;
    }

    /**
     * 设置背景Drawable
     *
     * @param drawable Drawable
     */
    public AnyLayer backgroundDrawable(@NonNull Drawable drawable) {
        mLayerManager.mConfig.mBackgroundDrawable = drawable;
        return this;
    }

    /**
     * 设置背景颜色
     * 在调用了{@link #backgroundBitmap(Bitmap)}或者{@link #backgroundBlurRadius(float)}方法后
     * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时传入的颜色为半透明颜色
     *
     * @param colorInt 颜色值
     */
    public AnyLayer backgroundColorInt(@ColorInt int colorInt) {
        mLayerManager.mConfig.mBackgroundColor = colorInt;
        return this;
    }

    /**
     * 设置背景颜色
     * 在调用了{@link #backgroundBitmap(Bitmap)}或者{@link #backgroundBlurRadius(float)}方法后
     * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时传入的颜色为半透明颜色
     *
     * @param colorRes 颜色资源ID
     */
    public AnyLayer backgroundColorRes(@ColorRes int colorRes) {
        mLayerManager.mConfig.mBackgroundColor = ContextCompat.getColor(mLayerManager.mContext, colorRes);
        return this;
    }

    /**
     * 设置浮层外部是否拦截触摸
     * 默认为true，false则事件有activityContent本身消费
     *
     * @param intercept 外部是否拦截触摸
     */
    public AnyLayer outsideInterceptTouchEvent(boolean intercept) {
        mLayerManager.mConfig.mOutsideInterceptTouchEvent = intercept;
        return this;
    }

    /**
     * 设置点击浮层以外区域是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    public AnyLayer cancelableOnTouchOutside(boolean cancelable) {
        mLayerManager.mConfig.mCancelableOnTouchOutside = cancelable;
        return this;
    }

    /**
     * 设置点击返回键是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    public AnyLayer cancelableOnClickKeyBack(boolean cancelable) {
        mLayerManager.mConfig.mCancelableOnClickKeyBack = cancelable;
        return this;
    }

    /**
     * 对一个View绑定点击事件
     *
     * @param viewId   控件ID
     * @param listener 监听器
     */
    public AnyLayer onClick(@IdRes int viewId, LayerManager.OnLayerClickListener listener) {
        return onClick(listener, viewId, null);
    }

    /**
     * 对一个View绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewId   控件ID
     * @param listener 监听器
     */
    public AnyLayer onClickToDismiss(@IdRes int viewId, LayerManager.OnLayerClickListener listener) {
        return onClickToDismiss(listener, viewId, null);
    }

    /**
     * 对多个View绑定点击事件
     *
     * @param listener 监听器
     * @param viewId   控件ID
     * @param viewIds  控件ID
     */
    public AnyLayer onClick(LayerManager.OnLayerClickListener listener, @IdRes int viewId, @IdRes int... viewIds) {
        mLayerManager.mViewHolder.addOnClickListener(listener, viewId, viewIds);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param listener 监听器
     * @param viewId   控件ID
     * @param viewIds  控件ID
     */
    public AnyLayer onClickToDismiss(LayerManager.OnLayerClickListener listener, @IdRes int viewId, @IdRes int... viewIds) {
        mLayerManager.mViewHolder.addOnClickListener(new LayerManager.OnLayerClickListener() {
            @Override
            public void onClick(AnyLayer anyLayer, View v) {
                if (listener != null) {
                    listener.onClick(anyLayer, v);
                }
                dismiss();
            }
        }, viewId, viewIds);
        return this;
    }

    /**
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewId  控件ID
     * @param viewIds 控件ID
     */
    public AnyLayer onClickToDismiss(@IdRes int viewId, @IdRes int... viewIds) {
        return onClickToDismiss(null, viewId, viewIds);
    }

    /**
     * 显示
     */
    public void show() {
        mLayerManager.show();
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        mLayerManager.dismiss();
    }

    public Context getContext() {
        return mLayerManager.mContext;
    }

    /**
     * 获取布局子控件
     *
     * @param viewId 控件ID
     * @param <V>    子控件
     * @return 子控件
     */
    public <V extends View> V getView(@IdRes int viewId) {
        return mLayerManager.mViewHolder.getView(viewId);
    }

    /**
     * 获取View管理容器
     *
     * @return ViewHolder
     */
    public ViewHolder getViewHolder() {
        return mLayerManager.mViewHolder;
    }

    /**
     * 获取自定义的浮层控件
     *
     * @return View
     */
    public View getContentView() {
        return mLayerManager.mViewHolder.getContent();
    }

    /**
     * 获取背景图
     *
     * @return ImageView
     */
    public ImageView getBackground() {
        return mLayerManager.mViewHolder.getBackground();
    }

    /**
     * 获取浮层是否已显示
     *
     * @return boolean
     */
    public boolean isShow() {
        return mLayerManager.mViewManager.isAttached();
    }

    /**
     * 适配软键盘的弹出，布局自动上移
     * 在某几个EditText获取焦点时布局上移
     * 在{@link LayerManager.OnVisibleChangeListener#onShow(AnyLayer)}中调用
     * 应该和{@link #removeSoftInput()}成对出现
     *
     * @param editText 焦点EditTexts
     */
    public void compatSoftInput(EditText... editText) {
        Activity activity = Utils.getActivity(mLayerManager.mContext);
        if (activity != null) {
            SoftInputHelper.attach(activity)
                    .init(mLayerManager.mViewHolder.getContentWrapper(), mLayerManager.mViewHolder.getContent(), editText)
                    .moveWithTranslation();
        }
    }

    /**
     * 移除软键盘适配
     * 在{@link LayerManager.OnVisibleChangeListener#onDismiss(AnyLayer)}中调用
     * 应该和{@link #compatSoftInput(EditText...)}成对出现
     */
    public void removeSoftInput() {
        if (mSoftInputHelper != null) {
            mSoftInputHelper.detach();
        }
    }
}
