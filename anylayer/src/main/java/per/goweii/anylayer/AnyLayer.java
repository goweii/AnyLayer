package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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
public class AnyLayer implements LayerManager.LifeListener {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private final ViewGroup mRootView;
    private final FrameLayout mActivityContentView;
    private final View mTargetView;

    @IdRes
    private int mAsStatusBarViewId = 0;

    private ViewHolder mViewHolder;
    private LayerManager mLayerManager;

    private int mGravity = Gravity.CENTER;
    private float mBackgroundBlurPercent = 0;
    private float mBackgroundBlurRadius = 0;
    private float mBackgroundBlurScale = 2;
    private Bitmap mBackgroundBitmap = null;
    private int mBackgroundResource = -1;
    private Drawable mBackgroundDrawable = null;
    private int mBackgroundColor = Color.TRANSPARENT;

    private boolean mCancelableOnTouchOutside = true;

    private IAnim mBackgroundAnim = null;
    private Animation mBackgroundInAnim = null;
    private Animation mBackgroundOutAnim = null;
    private IAnim mContentAnim = null;
    private Animation mContentInAnim = null;
    private Animation mContentOutAnim = null;
    private long mBackgroundInAnimDuration = 300;
    private long mBackgroundOutAnimDuration = 300;
    private long mContentInAnimDuration = 300;
    private long mContentOutAnimDuration = 300;

    private IDataBinder mDataBinder = null;
    private OnVisibleChangeListener mOnVisibleChangeListener = null;
    private OnLayerShowListener mOnLayerShowListener = null;
    private OnLayerDismissListener mOnLayerDismissListener = null;

    private boolean mAlignmentInside = false;
    private Alignment.Direction mAlignmentDirection = Alignment.Direction.VERTICAL;
    private Alignment.Horizontal mAlignmentHorizontal = Alignment.Horizontal.CENTER;
    private Alignment.Vertical mAlignmentVertical = Alignment.Vertical.BELOW;

    private SoftInputHelper mSoftInputHelper = null;

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

    /**
     * 设置自自定义View
     *
     * @param contentView 自定以View
     */
    public AnyLayer contentView(@NonNull View contentView) {
        mViewHolder.setContent(contentView);
        return this;
    }

    /**
     * 设置自定义布局文件
     *
     * @param contentViewId 自定以布局ID
     */
    public AnyLayer contentView(@LayoutRes int contentViewId) {
        return contentView(mInflater.inflate(contentViewId, mViewHolder.getContainer(), false));
    }

    /**
     * 设置自定义布局文件中状态栏的占位View
     * 该控件高度将设置为状态栏高度，可用来使布局整体下移，避免状态栏遮挡
     *
     * @param statusBarId 状态栏的占位View
     */
    public AnyLayer asStatusBar(@IdRes int statusBarId) {
        mAsStatusBarViewId = statusBarId;
        return this;
    }

    /**
     * 绑定数据
     * 获取子控件ID为{@link #getView(int)}
     *
     * @param dataBinder 实现该接口进行数据绑定
     */
    public AnyLayer bindData(IDataBinder dataBinder) {
        mDataBinder = dataBinder;
        return this;
    }

    /**
     * 设置显示状态改变的监听
     *
     * @param mOnVisibleChangeListener OnVisibleChangeListener
     */
    public AnyLayer onVisibleChangeListener(OnVisibleChangeListener mOnVisibleChangeListener) {
        this.mOnVisibleChangeListener = mOnVisibleChangeListener;
        return this;
    }

    /**
     * 设置变更为显示状态监听
     *
     * @param onLayerShowListener OnLayerShowListener
     */
    public AnyLayer onLayerShowListener(OnLayerShowListener onLayerShowListener) {
        mOnLayerShowListener = onLayerShowListener;
        return this;
    }

    /**
     * 设置变更为隐藏状态监听
     *
     * @param onLayerDismissListener OnLayerDismissListener
     */
    public AnyLayer onLayerDismissListener(OnLayerDismissListener onLayerDismissListener) {
        mOnLayerDismissListener = onLayerDismissListener;
        return this;
    }

    /**
     * 设置子布局的gravity
     * 可直接在布局文件指定layout_gravity属性，作用相同
     *
     * @param gravity {@link Gravity}
     */
    public AnyLayer gravity(int gravity) {
        mGravity = gravity;
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
        mAlignmentDirection = direction;
        mAlignmentHorizontal = horizontal;
        mAlignmentVertical = vertical;
        mAlignmentInside = inside;
        return this;
    }

    /**
     * 自定义浮层的进入和退出动画
     * 可使用工具类{@link AnimHelper}
     *
     * @param contentAnim IAnim接口
     */
    public AnyLayer contentAnim(IAnim contentAnim) {
        this.mContentAnim = contentAnim;
        return this;
    }

    /**
     * 自定义浮层的进入动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer contentInAnim(@AnimRes int anim) {
        contentInAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    /**
     * 自定义浮层的进入动画
     *
     * @param anim Animation动画
     */
    public AnyLayer contentInAnim(@NonNull Animation anim) {
        mContentInAnim = anim;
        mContentInAnimDuration = mContentInAnim.getDuration();
        return this;
    }

    /**
     * 自定义浮层的退出动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer contentOutAnim(@AnimRes int anim) {
        contentOutAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    /**
     * 自定义浮层的退出动画
     *
     * @param anim Animation动画
     */
    public AnyLayer contentOutAnim(@NonNull Animation anim) {
        mContentOutAnim = anim;
        mContentOutAnimDuration = mContentOutAnim.getDuration();
        return this;
    }

    /**
     * 自定义背景的进入和退出动画
     * 可使用工具类{@link AnimHelper}
     *
     * @param backgroundAnim IAnim接口
     */
    public AnyLayer backgroundAnim(IAnim backgroundAnim) {
        this.mBackgroundAnim = backgroundAnim;
        return this;
    }

    /**
     * 自定义背景的进入动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer backgroundInAnim(@AnimRes int anim) {
        backgroundInAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    /**
     * 自定义背景的退出动画
     *
     * @param anim Animation动画
     */
    public AnyLayer backgroundInAnim(@NonNull Animation anim) {
        mBackgroundInAnim = anim;
        mBackgroundInAnimDuration = mBackgroundInAnim.getDuration();
        return this;
    }

    /**
     * 自定义背景的退出动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer backgroundOutAnim(@AnimRes int anim) {
        backgroundOutAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    /**
     * 自定义背景的退出动画
     *
     * @param anim Animation动画
     */
    public AnyLayer backgroundOutAnim(@NonNull Animation anim) {
        mBackgroundOutAnim = anim;
        mBackgroundOutAnimDuration = mBackgroundOutAnim.getDuration();
        return this;
    }

    /**
     * 默认浮层进入和退出动画时长
     *
     * @param defaultAnimDuration 时长
     */
    public AnyLayer defaultContentAnimDuration(long defaultAnimDuration) {
        this.mContentInAnimDuration = defaultAnimDuration;
        this.mContentOutAnimDuration = defaultAnimDuration;
        return this;
    }

    /**
     * 默认浮层进入动画时长
     *
     * @param defaultInAnimDuration 时长
     */
    public AnyLayer defaultContentInAnimDuration(long defaultInAnimDuration) {
        this.mContentInAnimDuration = defaultInAnimDuration;
        return this;
    }

    /**
     * 默认浮层退出动画时长
     *
     * @param defaultOutAnimDuration 时长
     */
    public AnyLayer defaultContentOutAnimDuration(long defaultOutAnimDuration) {
        this.mContentOutAnimDuration = defaultOutAnimDuration;
        return this;
    }

    /**
     * 默认背景进入和退出动画时长
     *
     * @param defaultAnimDuration 时长
     */
    public AnyLayer defaultBackgroundAnimDuration(long defaultAnimDuration) {
        this.mBackgroundInAnimDuration = defaultAnimDuration;
        this.mBackgroundOutAnimDuration = defaultAnimDuration;
        return this;
    }

    /**
     * 默认背景进入动画时长
     *
     * @param defaultInAnimDuration 时长
     */
    public AnyLayer defaultBackgroundInAnimDuration(long defaultInAnimDuration) {
        this.mBackgroundInAnimDuration = defaultInAnimDuration;
        return this;
    }

    /**
     * 默认背景退出动画时长
     *
     * @param defaultOutAnimDuration 时长
     */
    public AnyLayer defaultBackgroundOutAnimDuration(long defaultOutAnimDuration) {
        this.mBackgroundOutAnimDuration = defaultOutAnimDuration;
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
        mBackgroundBlurRadius = radius;
        return this;
    }

    public AnyLayer backgroundBlurPercent(@FloatRange(from = 0, fromInclusive = false) float percent) {
        mBackgroundBlurPercent = percent;
        return this;
    }

    /**
     * 设置背景高斯模糊的缩小比例
     *
     * @param scale 缩小比例
     */
    public AnyLayer backgroundBlurScale(@FloatRange(from = 1) float scale) {
        mBackgroundBlurScale = scale;
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param bitmap 图片
     */
    public AnyLayer backgroundBitmap(@NonNull Bitmap bitmap) {
        mBackgroundBitmap = bitmap;
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param resource 资源ID
     */
    public AnyLayer backgroundResource(@DrawableRes int resource) {
        mBackgroundResource = resource;
        return this;
    }

    /**
     * 设置背景Drawable
     *
     * @param drawable Drawable
     */
    public AnyLayer backgroundDrawable(@NonNull Drawable drawable) {
        mBackgroundDrawable = drawable;
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
        mBackgroundColor = colorInt;
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
        mBackgroundColor = ContextCompat.getColor(mContext, colorRes);
        return this;
    }

    /**
     * 设置点击浮层以外区域是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    public AnyLayer cancelableOnTouchOutside(boolean cancelable) {
        mCancelableOnTouchOutside = cancelable;
        return this;
    }

    /**
     * 设置点击返回键是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    public AnyLayer cancelableOnClickKeyBack(boolean cancelable) {
        mLayerManager.setCancelableOnClickKeyBack(cancelable);
        return this;
    }

    /**
     * 对一个View绑定点击事件
     *
     * @param viewId   控件ID
     * @param listener 监听器
     */
    public AnyLayer onClick(@IdRes int viewId, OnLayerClickListener listener) {
        mViewHolder.addOnClickListener(listener, viewId, null);
        return this;
    }

    /**
     * 对多个View绑定点击事件
     *
     * @param listener 监听器
     * @param viewId   控件ID
     * @param viewIds  控件ID
     */
    public AnyLayer onClick(OnLayerClickListener listener, @IdRes int viewId, @IdRes int... viewIds) {
        mViewHolder.addOnClickListener(listener, viewId, viewIds);
        return this;
    }

    /**
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewId  控件ID
     * @param viewIds 控件ID
     */
    public AnyLayer onClickToDismiss(@IdRes int viewId, @IdRes int... viewIds) {
        mViewHolder.addOnClickListener(new OnLayerClickListener() {
            @Override
            public void onClick(AnyLayer anyLayer, View v) {
                dismiss();
            }
        }, viewId, viewIds);
        return this;
    }

    /**
     * 显示
     */
    public void show() {
        mLayerManager.add();
    }

    /**
     * 隐藏
     */
    public void dismiss() {
        mLayerManager.remove();
    }

    /**
     * 获取布局子控件
     *
     * @param viewId 控件ID
     * @param <V>    子控件
     * @return 子控件
     */
    public <V extends View> V getView(@IdRes int viewId) {
        return mViewHolder.getView(viewId);
    }

    /**
     * 获取View管理容器
     *
     * @return ViewHolder
     */
    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    /**
     * 获取自定义的浮层控件
     *
     * @return View
     */
    public View getContentView() {
        return mViewHolder.getContent();
    }

    /**
     * 获取背景图
     *
     * @return ImageView
     */
    public ImageView getBackground() {
        return mViewHolder.getBackground();
    }

    /**
     * 获取浮层是否已显示
     *
     * @return boolean
     */
    public boolean isShow() {
        return mViewHolder.getContainer().getParent() != null;
    }

    /**
     * 适配软键盘的弹出，布局自动上移
     * 在某几个EditText获取焦点时布局上移
     * 在{@link OnVisibleChangeListener#onShow(AnyLayer)}中调用
     * 应该和{@link #removeSoftInput()}成对出现
     *
     * @param editText 焦点EditTexts
     */
    public void compatSoftInput(EditText... editText) {
        Activity activity = getActivity();
        if (activity != null) {
            SoftInputHelper.attach(activity)
                    .init(mViewHolder.getContentWrapper(), mViewHolder.getContent(), editText)
                    .moveWithTranslation()
                    .duration(300);
        }
    }

    /**
     * 移除软键盘适配
     * 在{@link OnVisibleChangeListener#onDismiss(AnyLayer)}中调用
     * 应该和{@link #compatSoftInput(EditText...)}成对出现
     */
    public void removeSoftInput() {
        if (mSoftInputHelper != null) {
            mSoftInputHelper.detach();
        }
    }

    private AnyLayer(@NonNull ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        mInflater = LayoutInflater.from(mContext);
        mTargetView = null;
        mRootView = viewGroup;
        mActivityContentView = null;
        initView();
    }

    private AnyLayer(@NonNull Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mTargetView = null;
        mRootView = (FrameLayout) Objects.requireNonNull(Utils.getActivity(mContext)).getWindow().getDecorView();
        mActivityContentView = mRootView.findViewById(android.R.id.content);
        initView();
    }

    private AnyLayer(@NonNull View targetView) {
        mContext = targetView.getContext();
        mInflater = LayoutInflater.from(mContext);
        mTargetView = targetView;
        mRootView = (FrameLayout) Objects.requireNonNull(Utils.getActivity(mContext)).getWindow().getDecorView();
        mActivityContentView = mRootView.findViewById(android.R.id.content);
        initView();
    }

    private void initView() {
        Blurred.init(mContext);
        FrameLayout container = (FrameLayout) mInflater.inflate(R.layout.layout_any_layer, mRootView, false);
        mViewHolder = new ViewHolder(this, container);
        mLayerManager = new LayerManager(mRootView, container);
        mLayerManager.setLifeListener(this);
    }

    /**
     * 从当前上下文获取Activity
     */
    @Nullable
    private Activity getActivity() {
        if (mContext instanceof Activity) {
            return (Activity) mContext;
        }
        if (mContext instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) mContext).getBaseContext();
            if (baseContext instanceof Activity) {
                return (Activity) baseContext;
            }
        }
        return null;
    }

    private void startContentInAnim() {
        if (mContentAnim != null) {
            mContentInAnimDuration = mContentAnim.inAnim(mViewHolder.getContent());
        } else {
            if (mContentInAnim != null) {
                mViewHolder.getContent().startAnimation(mContentInAnim);
            } else {
                AnimHelper.startZoomInAnim(mViewHolder.getContent(), mContentInAnimDuration);
            }
        }
    }

    private void startContentOutAnim() {
        if (mContentAnim != null) {
            mContentOutAnimDuration = mContentAnim.outAnim(mViewHolder.getContent());
        } else {
            if (mContentOutAnim != null) {
                mViewHolder.getContent().startAnimation(mContentOutAnim);
            } else {
                AnimHelper.startZoomOutAnim(mViewHolder.getContent(), mContentOutAnimDuration);
            }
        }
    }

    private void startBackgroundInAnim() {
        if (mBackgroundAnim != null) {
            mBackgroundInAnimDuration = mBackgroundAnim.inAnim(mViewHolder.getBackground());
        } else {
            if (mBackgroundInAnim != null) {
                mViewHolder.getBackground().startAnimation(mBackgroundInAnim);
            } else {
                AnimHelper.startAlphaInAnim(mViewHolder.getBackground(), mBackgroundInAnimDuration);
            }
        }
    }

    private void startBackgroundOutAnim() {
        if (mBackgroundAnim != null) {
            mBackgroundOutAnimDuration = mBackgroundAnim.outAnim(mViewHolder.getBackground());
        } else {
            if (mBackgroundOutAnim != null) {
                mViewHolder.getBackground().startAnimation(mBackgroundOutAnim);
            } else {
                AnimHelper.startAlphaOutAnim(mViewHolder.getBackground(), mBackgroundOutAnimDuration);
            }
        }
    }

    @Override
    public void onAttach() {
        initContainer();
        initBackground();
        initContent();
        mViewHolder.bindListener();
        if (mOnVisibleChangeListener != null) {
            mOnVisibleChangeListener.onShow(AnyLayer.this);
        }
        if (mDataBinder != null) {
            mDataBinder.bind(this);
        }
        if (mOnLayerShowListener != null) {
            mOnLayerShowListener.onShowing(AnyLayer.this);
        }
    }

    @Override
    public long onAnimIn(View view) {
        startContentInAnim();
        startBackgroundInAnim();
        return Math.max(mBackgroundInAnimDuration, mContentInAnimDuration);
    }

    @Override
    public long onAnimOut(View view) {
        startContentOutAnim();
        startBackgroundOutAnim();
        return Math.max(mBackgroundOutAnimDuration, mContentOutAnimDuration);
    }

    @Override
    public void onShow() {
        if (mOnLayerShowListener != null) {
            mOnLayerShowListener.onShown(AnyLayer.this);
        }
    }

    @Override
    public void onRemove() {
        if (mOnLayerDismissListener != null) {
            mOnLayerDismissListener.onDismissing(AnyLayer.this);
        }
    }

    @Override
    public void onDetach() {
        if (mOnVisibleChangeListener != null) {
            mOnVisibleChangeListener.onDismiss(AnyLayer.this);
        }
        if (mOnLayerDismissListener != null) {
            mOnLayerDismissListener.onDismissed(AnyLayer.this);
        }
        mViewHolder.recycle();
    }

    private int getStatusBarHeight() {
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    private void initContainer() {
        if (mCancelableOnTouchOutside) {
            mViewHolder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (mActivityContentView != null) {
            // 非指定父布局的，添加到DecorView，此时mRootView为DecorView
            final int[] locationDecor = new int[2];
            mRootView.getLocationOnScreen(locationDecor);
            final int[] locationActivityContent = new int[2];
            mActivityContentView.getLocationOnScreen(locationActivityContent);
            FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) mViewHolder.getContainer().getLayoutParams();
            containerParams.leftMargin = locationActivityContent[0] - locationDecor[0];
            containerParams.topMargin = 0;
            containerParams.width = mActivityContentView.getWidth();
            containerParams.height = mActivityContentView.getHeight() + (locationActivityContent[1] - locationDecor[1]);
            mViewHolder.getContainer().setLayoutParams(containerParams);
        }
        if (mTargetView == null) {
            FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) mViewHolder.getContentWrapper().getLayoutParams();
            contentWrapperParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
            contentWrapperParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
            mViewHolder.getContentWrapper().setLayoutParams(contentWrapperParams);
        } else {
            FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) mViewHolder.getContentWrapper().getLayoutParams();
            contentWrapperParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            contentWrapperParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            mViewHolder.getContentWrapper().setLayoutParams(contentWrapperParams);
            final int[] locationTarget = new int[2];
            mTargetView.getLocationOnScreen(locationTarget);
            final int[] locationRoot = new int[2];
            mRootView.getLocationOnScreen(locationRoot);
            final int targetX = (locationTarget[0] - locationRoot[0]);
            final int targetY = (locationTarget[1] - locationRoot[1]);
            final int targetWidth = mTargetView.getWidth();
            final int targetHeight = mTargetView.getHeight();
            int paddingTop = 0;
            int paddingBottom = 0;
            int paddingLeft = 0;
            int paddingRight = 0;
            FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) mViewHolder.getContainer().getLayoutParams();
            if (mAlignmentDirection == Alignment.Direction.HORIZONTAL) {
                if (mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
                    paddingRight = containerParams.width - targetX;
                } else if (mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
                    paddingLeft = targetX + targetWidth;
                } else if (mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                    paddingLeft = targetX;
                } else if (mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
                    paddingRight = containerParams.width - targetX - targetWidth;
                }
            } else if (mAlignmentDirection == Alignment.Direction.VERTICAL) {
                if (mAlignmentVertical == Alignment.Vertical.ABOVE) {
                    paddingBottom = containerParams.height - targetY;
                } else if (mAlignmentVertical == Alignment.Vertical.BELOW) {
                    paddingTop = targetY + targetHeight;
                } else if (mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                    paddingTop = targetY;
                } else if (mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
                    paddingBottom = containerParams.height - targetY - targetHeight;
                }
            }
            mViewHolder.getContainer().setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            int finalPaddingLeft = paddingLeft;
            int finalPaddingTop = paddingTop;
            mViewHolder.getContainer().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (mViewHolder.getContainer().getViewTreeObserver().isAlive()) {
                        mViewHolder.getContainer().getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    final int width = mViewHolder.getContentWrapper().getWidth();
                    final int height = mViewHolder.getContentWrapper().getHeight();
                    int x = 0;
                    int y = 0;
                    if (mAlignmentHorizontal == Alignment.Horizontal.CENTER) {
                        x = targetX - (width - targetWidth) / 2;
                    } else if (mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
                        x = targetX - width;
                    } else if (mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
                        x = targetX + targetWidth;
                    } else if (mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                        x = targetX;
                    } else if (mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
                        x = targetX - (width - targetWidth);
                    }
                    if (mAlignmentVertical == Alignment.Vertical.CENTER) {
                        y = targetY - (height - targetHeight) / 2;
                    } else if (mAlignmentVertical == Alignment.Vertical.ABOVE) {
                        y = targetY - height;
                    } else if (mAlignmentVertical == Alignment.Vertical.BELOW) {
                        y = targetY + targetHeight;
                    } else if (mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                        y = targetY;
                    } else if (mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
                        y = targetY - (height - targetHeight);
                    }
                    x = x - finalPaddingLeft;
                    y = y - finalPaddingTop;
                    if (mAlignmentInside) {
                        final int maxWidth = mViewHolder.getContainer().getWidth() - mViewHolder.getContainer().getPaddingLeft() - mViewHolder.getContainer().getPaddingRight();
                        final int maxHeight = mViewHolder.getContainer().getHeight() - mViewHolder.getContainer().getPaddingTop() - mViewHolder.getContainer().getPaddingBottom();
                        final int maxX = maxWidth - width;
                        final int maxY = maxHeight - height;
                        if (x < 0) {
                            x = 0;
                        } else if (x > maxX) {
                            x = maxX;
                        }
                        if (y < 0) {
                            y = 0;
                        } else if (y > maxY) {
                            y = maxY;
                        }
                    }
                    FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) mViewHolder.getContentWrapper().getLayoutParams();
                    contentWrapperParams.leftMargin = x;
                    contentWrapperParams.topMargin = y;
                    mViewHolder.getContentWrapper().setLayoutParams(contentWrapperParams);
                    return false;
                }
            });
        }
    }

    private void initBackground() {
        if (mBackgroundBlurPercent > 0 || mBackgroundBlurRadius > 0) {
            mViewHolder.getBackground().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mViewHolder.getBackground().getViewTreeObserver().removeOnPreDrawListener(this);
                    Bitmap snapshot = Utils.snapshot(mRootView);
                    int[] locationRootView = new int[2];
                    mRootView.getLocationOnScreen(locationRootView);
                    int[] locationBackground = new int[2];
                    mViewHolder.getBackground().getLocationOnScreen(locationBackground);
                    int x = locationBackground[0] - locationRootView[0];
                    int y = locationBackground[1] - locationRootView[1];
                    Bitmap original = Bitmap.createBitmap(snapshot, x, y, mViewHolder.getBackground().getWidth(), mViewHolder.getBackground().getHeight());
                    snapshot.recycle();
                    Blurred blurred = Blurred.with(original)
                            .recycleOriginal(true)
                            .keepSize(false)
                            .scale(mBackgroundBlurScale);
                    if (mBackgroundBlurPercent > 0) {
                        blurred.percent(mBackgroundBlurPercent);
                    } else if (mBackgroundBlurRadius > 0) {
                        blurred.radius(mBackgroundBlurRadius);
                    }
                    Bitmap blurBitmap = blurred.blur();
                    mViewHolder.getBackground().setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mViewHolder.getBackground().setImageBitmap(blurBitmap);
                    mViewHolder.getBackground().setColorFilter(mBackgroundColor);
                    return true;
                }
            });
        } else {
            if (mBackgroundBitmap != null) {
                mViewHolder.getBackground().setImageBitmap(mBackgroundBitmap);
                mViewHolder.getBackground().setColorFilter(mBackgroundColor);
            } else if (mBackgroundResource != -1) {
                mViewHolder.getBackground().setImageResource(mBackgroundResource);
                mViewHolder.getBackground().setColorFilter(mBackgroundColor);
            } else if (mBackgroundDrawable != null) {
                mViewHolder.getBackground().setImageDrawable(mBackgroundDrawable);
                mViewHolder.getBackground().setColorFilter(mBackgroundColor);
            } else {
                mViewHolder.getBackground().setImageDrawable(new ColorDrawable(mBackgroundColor));
            }
        }
    }

    private void initContent() {
        if (mViewHolder.getContent() != null) {
            ViewGroup contentParent = (ViewGroup) mViewHolder.getContent().getParent();
            if (contentParent != null) {
                contentParent.removeView(mViewHolder.getContent());
            }
            mViewHolder.getContent().setClickable(true);
            if (mTargetView == null && mGravity != -1) {
                ViewGroup.LayoutParams params = mViewHolder.getContent().getLayoutParams();
                FrameLayout.LayoutParams contentParams;
                if (params == null) {
                    contentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else if (params instanceof FrameLayout.LayoutParams) {
                    contentParams = (FrameLayout.LayoutParams) params;
                } else {
                    contentParams = new FrameLayout.LayoutParams(params.width, params.height);
                }
                contentParams.gravity = mGravity;
                mViewHolder.getContent().setLayoutParams(contentParams);
            }
            if (mAsStatusBarViewId > 0) {
                View statusBar = mViewHolder.getContent().findViewById(mAsStatusBarViewId);
                if (statusBar != null) {
                    ViewGroup.LayoutParams params = statusBar.getLayoutParams();
                    params.height = getStatusBarHeight();
                    statusBar.setLayoutParams(params);
                    statusBar.setVisibility(View.VISIBLE);
                }
            }
            mViewHolder.getContentWrapper().addView(mViewHolder.getContent());
        }
    }

    /**
     * 控制与目标控件的对齐方式
     */
    public static class Alignment {
        public enum Direction {
            /**
             * 主方向
             */
            HORIZONTAL,
            VERTICAL
        }

        public enum Horizontal {
            /**
             * 水平对齐方式
             */
            CENTER,
            TO_LEFT,
            TO_RIGHT,
            ALIGN_LEFT,
            ALIGN_RIGHT
        }

        public enum Vertical {
            /**
             * 垂直对齐方式
             */
            CENTER,
            ABOVE,
            BELOW,
            ALIGN_TOP,
            ALIGN_BOTTOM
        }
    }

    public interface IAnim {
        /**
         * 内容进入动画
         *
         * @param target 内容
         * @return 动画时长
         */
        long inAnim(View target);

        /**
         * 内容消失动画
         *
         * @param target 内容
         * @return 动画时长
         */
        long outAnim(View target);
    }

    public interface IDataBinder {
        /**
         * 绑定数据
         *
         * @param anyLayer AnyLayer
         */
        void bind(AnyLayer anyLayer);
    }

    public interface OnLayerClickListener {
        /**
         * 点击事件回调
         *
         * @param anyLayer 浮层
         * @param v        点击控件
         */
        void onClick(AnyLayer anyLayer, View v);
    }

    public interface OnLayerDismissListener {
        /**
         * 开始隐藏，动画刚开始执行
         *
         * @param anyLayer 浮层
         */
        void onDismissing(AnyLayer anyLayer);

        /**
         * 已隐藏，浮层已被移除
         *
         * @param anyLayer 浮层
         */
        void onDismissed(AnyLayer anyLayer);
    }

    public interface OnLayerShowListener {
        /**
         * 开始显示，动画刚开始执行
         *
         * @param anyLayer 浮层
         */
        void onShowing(AnyLayer anyLayer);

        /**
         * 已显示，浮层已显示且动画结束
         *
         * @param anyLayer 浮层
         */
        void onShown(AnyLayer anyLayer);
    }

    public interface OnVisibleChangeListener {
        /**
         * 浮层显示，刚被添加到父布局，进入动画未开始
         *
         * @param anyLayer 浮层
         */
        void onShow(AnyLayer anyLayer);

        /**
         * 浮层隐藏，已被从父布局移除，隐藏动画已结束
         *
         * @param anyLayer 浮层
         */
        void onDismiss(AnyLayer anyLayer);
    }
}
