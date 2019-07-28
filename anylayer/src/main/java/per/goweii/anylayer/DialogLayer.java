package per.goweii.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Objects;

import per.goweii.burred.Blurred;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class DialogLayer extends DecorLayer {

    @Nullable
    private SoftInputHelper mSoftInputHelper = null;

    public DialogLayer(@NonNull Activity activity) {
        super(activity);
        getViewHolder().setActivityContent(getViewHolder().getDecor().findViewById(android.R.id.content));
    }

    public DialogLayer(@NonNull Context context) {
        this(Objects.requireNonNull(Utils.getActivity(context)));
    }

    public DialogLayer(@NonNull View targetView) {
        this(targetView.getContext());
        getViewHolder().setTarget(targetView);
    }

    @Level
    @Override
    protected int getLevel() {
        return Level.DIALOG;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder() {
        return new ViewHolder();
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder() {
        return (ViewHolder) super.getViewHolder();
    }

    @NonNull
    @Override
    protected Config onCreateConfig() {
        return new Config();
    }

    @NonNull
    @Override
    public Config getConfig() {
        return (Config) super.getConfig();
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        FrameLayout container = (FrameLayout) inflater.inflate(R.layout.anylayer_dialog_layer, parent, false);
        if (getViewHolder().getContent() == null) {
            getViewHolder().setContent(inflater.inflate(getConfig().contentViewId, container, false));
        }
        return container;
    }

    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createInAnimator(getViewHolder().getBackground());
        } else {
            backgroundAnimator = AnimatorHelper.createAlphaInAnim(getViewHolder().getBackground());
        }
        Animator contentAnimator;
        if (getConfig().mContentAnimatorCreator != null) {
            contentAnimator = getConfig().mContentAnimatorCreator.createInAnimator(getViewHolder().getContent());
        } else {
            contentAnimator = AnimatorHelper.createZoomAlphaInAnim(getViewHolder().getContent());
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createOutAnimator(getViewHolder().getBackground());
        } else {
            backgroundAnimator = AnimatorHelper.createAlphaOutAnim(getViewHolder().getBackground());
        }
        Animator contentAnimator;
        if (getConfig().mContentAnimatorCreator != null) {
            contentAnimator = getConfig().mContentAnimatorCreator.createOutAnimator(getViewHolder().getContent());
        } else {
            contentAnimator = AnimatorHelper.createZoomAlphaOutAnim(getViewHolder().getContent());
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    @Override
    public void onAttach() {
        super.onAttach();
        initContainer();
        initBackground();
        initContent();
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
    }

    @Override
    public void onShow() {
        super.onShow();
    }

    @Override
    public void onPerRemove() {
        super.onPerRemove();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getViewHolder().recycle();
    }

    private void initContainer() {
        if (getConfig().mOutsideInterceptTouchEvent) {
            getViewHolder().getChild().setClickable(true);
            if (getConfig().mCancelableOnTouchOutside) {
                getViewHolder().getChild().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        } else {
            getViewHolder().getChild().setClickable(false);
        }
        if (getViewHolder().getActivityContent() != null) {
            // 非指定父布局的，添加到DecorView，此时getViewHolder().getRootView()为DecorView
            final int[] locationDecor = new int[2];
            getViewHolder().getDecor().getLocationOnScreen(locationDecor);
            final int[] locationActivityContent = new int[2];
            getViewHolder().getActivityContent().getLocationOnScreen(locationActivityContent);
            FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) getViewHolder().getChild().getLayoutParams();
            containerParams.leftMargin = locationActivityContent[0] - locationDecor[0];
            containerParams.topMargin = 0;
            containerParams.width = getViewHolder().getActivityContent().getWidth();
            containerParams.height = getViewHolder().getActivityContent().getHeight() + (locationActivityContent[1] - locationDecor[1]);
            getViewHolder().getChild().setLayoutParams(containerParams);
        }
        if (getViewHolder().getTarget() == null) {
            FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
            contentWrapperParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
            contentWrapperParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
            getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
        } else {
            initContainerWithTarget();
        }
    }

    private void initContainerWithTarget() {
        getViewHolder().getChild().setClipToPadding(false);
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        contentWrapperParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        contentWrapperParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
        final int[] locationTarget = new int[2];
        getViewHolder().getTarget().getLocationOnScreen(locationTarget);
        final int[] locationRoot = new int[2];
        getViewHolder().getDecor().getLocationOnScreen(locationRoot);
        final int targetX = (locationTarget[0] - locationRoot[0]);
        final int targetY = (locationTarget[1] - locationRoot[1]);
        final int targetWidth = getViewHolder().getTarget().getWidth();
        final int targetHeight = getViewHolder().getTarget().getHeight();
        final int[] padding = initContainerWithTargetPadding(targetX, targetY, targetWidth, targetHeight);
        getViewHolder().getChild().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (getViewHolder().getChild().getViewTreeObserver().isAlive()) {
                    getViewHolder().getChild().getViewTreeObserver().removeOnPreDrawListener(this);
                }
                initContainerWithTargetMargin(targetX, targetY, targetWidth, targetHeight, padding[0], padding[1]);
                return false;
            }
        });
        if (!getConfig().mOutsideInterceptTouchEvent) {
            getViewHolder().getChild().getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    final int[] locationTarget = new int[2];
                    getViewHolder().getTarget().getLocationOnScreen(locationTarget);
                    final int[] locationRoot = new int[2];
                    getViewHolder().getDecor().getLocationOnScreen(locationRoot);
                    final int targetX = (locationTarget[0] - locationRoot[0]);
                    final int targetY = (locationTarget[1] - locationRoot[1]);
                    final int targetWidth = getViewHolder().getTarget().getWidth();
                    final int targetHeight = getViewHolder().getTarget().getHeight();
                    final int[] padding = initContainerWithTargetPadding(targetX, targetY, targetWidth, targetHeight);
                    initContainerWithTargetMargin(targetX, targetY, targetWidth, targetHeight, padding[0], padding[1]);
                }
            });
        }
    }

    private int[] initContainerWithTargetPadding(int targetX, int targetY, int targetWidth, int targetHeight) {
        final int[] padding = new int[]{0, 0, 0, 0};
        FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) getViewHolder().getChild().getLayoutParams();
        if (getConfig().mAlignmentDirection == Alignment.Direction.HORIZONTAL) {
            if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
                padding[2] = containerParams.width - targetX;
            } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
                padding[0] = targetX + targetWidth;
            } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                padding[0] = targetX;
            } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
                padding[2] = containerParams.width - targetX - targetWidth;
            }
        } else if (getConfig().mAlignmentDirection == Alignment.Direction.VERTICAL) {
            if (getConfig().mAlignmentVertical == Alignment.Vertical.ABOVE) {
                padding[3] = containerParams.height - targetY;
            } else if (getConfig().mAlignmentVertical == Alignment.Vertical.BELOW) {
                padding[1] = targetY + targetHeight;
            } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                padding[1] = targetY;
            } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
                padding[3] = containerParams.height - targetY - targetHeight;
            }
        }
        getViewHolder().getChild().setPadding(padding[0], padding[1], padding[2], padding[3]);
        return padding;
    }

    private void initContainerWithTargetMargin(int targetX, int targetY, int targetWidth, int targetHeight, int paddingLeft, int paddingTop) {
        final int width = getViewHolder().getContentWrapper().getWidth();
        final int height = getViewHolder().getContentWrapper().getHeight();
        int x = 0;
        int y = 0;
        if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.CENTER) {
            x = targetX - (width - targetWidth) / 2;
        } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
            x = targetX - width;
        } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
            x = targetX + targetWidth;
        } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
            x = targetX;
        } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
            x = targetX - (width - targetWidth);
        }
        if (getConfig().mAlignmentVertical == Alignment.Vertical.CENTER) {
            y = targetY - (height - targetHeight) / 2;
        } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ABOVE) {
            y = targetY - height;
        } else if (getConfig().mAlignmentVertical == Alignment.Vertical.BELOW) {
            y = targetY + targetHeight;
        } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
            y = targetY;
        } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
            y = targetY - (height - targetHeight);
        }
        x = x - paddingLeft;
        y = y - paddingTop;
        if (getConfig().mAlignmentInside) {
            final int maxWidth = getViewHolder().getChild().getWidth() - getViewHolder().getChild().getPaddingLeft() - getViewHolder().getChild().getPaddingRight();
            final int maxHeight = getViewHolder().getChild().getHeight() - getViewHolder().getChild().getPaddingTop() - getViewHolder().getChild().getPaddingBottom();
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
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        contentWrapperParams.leftMargin = x;
        contentWrapperParams.topMargin = y;
        getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
    }

    private void initBackground() {
        if (getConfig().mBackgroundBlurPercent > 0 || getConfig().mBackgroundBlurRadius > 0) {
            getViewHolder().getBackground().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewHolder().getBackground().getViewTreeObserver().removeOnPreDrawListener(this);
                    Bitmap snapshot = Utils.snapshot(getViewHolder().getDecor(), getLevel());
                    int[] locationRootView = new int[2];
                    getViewHolder().getDecor().getLocationOnScreen(locationRootView);
                    int[] locationBackground = new int[2];
                    getViewHolder().getBackground().getLocationOnScreen(locationBackground);
                    int x = locationBackground[0] - locationRootView[0];
                    int y = locationBackground[1] - locationRootView[1];
                    Bitmap original = Bitmap.createBitmap(snapshot, x, y, getViewHolder().getBackground().getWidth(), getViewHolder().getBackground().getHeight());
                    snapshot.recycle();
                    Blurred.init(getActivity());
                    Blurred blurred = Blurred.with(original)
                            .recycleOriginal(true)
                            .keepSize(false)
                            .scale(getConfig().mBackgroundBlurScale);
                    if (getConfig().mBackgroundBlurPercent > 0) {
                        blurred.percent(getConfig().mBackgroundBlurPercent);
                    } else if (getConfig().mBackgroundBlurRadius > 0) {
                        blurred.radius(getConfig().mBackgroundBlurRadius);
                    }
                    Bitmap blurBitmap = blurred.blur();
                    getViewHolder().getBackground().setScaleType(ImageView.ScaleType.CENTER_CROP);
                    getViewHolder().getBackground().setImageBitmap(blurBitmap);
                    getViewHolder().getBackground().setColorFilter(getConfig().mBackgroundColor);
                    return true;
                }
            });
        } else {
            if (getConfig().mBackgroundBitmap != null) {
                getViewHolder().getBackground().setImageBitmap(getConfig().mBackgroundBitmap);
                getViewHolder().getBackground().setColorFilter(getConfig().mBackgroundColor);
            } else if (getConfig().mBackgroundResource != -1) {
                getViewHolder().getBackground().setImageResource(getConfig().mBackgroundResource);
                getViewHolder().getBackground().setColorFilter(getConfig().mBackgroundColor);
            } else if (getConfig().mBackgroundDrawable != null) {
                getViewHolder().getBackground().setImageDrawable(getConfig().mBackgroundDrawable);
                getViewHolder().getBackground().setColorFilter(getConfig().mBackgroundColor);
            } else {
                getViewHolder().getBackground().setImageDrawable(new ColorDrawable(getConfig().mBackgroundColor));
            }
        }
    }

    private void initContent() {
        if (getViewHolder().getContent() != null) {
            ViewGroup contentParent = (ViewGroup) getViewHolder().getContent().getParent();
            if (contentParent != null) {
                contentParent.removeView(getViewHolder().getContent());
            }
            getViewHolder().getContent().setClickable(true);
            if (getViewHolder().getTarget() == null && getConfig().mGravity != -1) {
                ViewGroup.LayoutParams params = getViewHolder().getContent().getLayoutParams();
                FrameLayout.LayoutParams contentParams;
                if (params == null) {
                    contentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else if (params instanceof FrameLayout.LayoutParams) {
                    contentParams = (FrameLayout.LayoutParams) params;
                } else {
                    contentParams = new FrameLayout.LayoutParams(params.width, params.height);
                }
                contentParams.gravity = getConfig().mGravity;
                getViewHolder().getContent().setLayoutParams(contentParams);
            }
            if (getConfig().mAsStatusBarViewId > 0) {
                View statusBar = getViewHolder().getContent().findViewById(getConfig().mAsStatusBarViewId);
                if (statusBar != null) {
                    ViewGroup.LayoutParams params = statusBar.getLayoutParams();
                    params.height = Utils.getStatusBarHeight(getActivity());
                    statusBar.setLayoutParams(params);
                    statusBar.setVisibility(View.VISIBLE);
                }
            }
            getViewHolder().getContentWrapper().addView(getViewHolder().getContent());
        }
    }


    /**
     * 设置自自定义View
     *
     * @param contentView 自定以View
     */
    public DialogLayer contentView(@NonNull View contentView) {
        getViewHolder().setContent(contentView);
        return this;
    }

    /**
     * 设置自定义布局文件
     *
     * @param contentViewId 自定义布局ID
     */
    public DialogLayer contentView(@LayoutRes int contentViewId) {
        getConfig().contentViewId = contentViewId;
        return this;
    }

    /**
     * 设置自定义布局文件中状态栏的占位View
     * 该控件高度将设置为状态栏高度，可用来使布局整体下移，避免状态栏遮挡
     *
     * @param statusBarId 状态栏的占位View
     */
    public DialogLayer asStatusBar(@IdRes int statusBarId) {
        getConfig().mAsStatusBarViewId = statusBarId;
        return this;
    }

    /**
     * 设置子布局的gravity
     * 可直接在布局文件指定layout_gravity属性，作用相同
     *
     * @param gravity {@link Gravity}
     */
    public DialogLayer gravity(int gravity) {
        getConfig().mGravity = gravity;
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
    public DialogLayer alignment(@NonNull Alignment.Direction direction,
                                 @NonNull Alignment.Horizontal horizontal,
                                 @NonNull Alignment.Vertical vertical,
                                 boolean inside) {
        getConfig().mAlignmentDirection = direction;
        getConfig().mAlignmentHorizontal = horizontal;
        getConfig().mAlignmentVertical = vertical;
        getConfig().mAlignmentInside = inside;
        return this;
    }

    /**
     * 自定义浮层的进入和退出动画
     * 可使用工具类{@link AnimatorHelper}
     *
     * @param contentAnimatorCreator AnimatorCreator
     */
    public DialogLayer contentAnimator(AnimatorCreator contentAnimatorCreator) {
        getConfig().mContentAnimatorCreator = contentAnimatorCreator;
        return this;
    }

    /**
     * 自定义背景的进入和退出动画
     * 可使用工具类{@link AnimatorHelper}
     *
     * @param backgroundAnimatorCreator AnimatorCreator
     */
    public DialogLayer backgroundAnimator(AnimatorCreator backgroundAnimatorCreator) {
        getConfig().mBackgroundAnimatorCreator = backgroundAnimatorCreator;
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
    public DialogLayer backgroundBlurRadius(@FloatRange(from = 0, fromInclusive = false, to = 25) float radius) {
        getConfig().mBackgroundBlurRadius = radius;
        return this;
    }

    public DialogLayer backgroundBlurPercent(@FloatRange(from = 0, fromInclusive = false) float percent) {
        getConfig().mBackgroundBlurPercent = percent;
        return this;
    }

    /**
     * 设置背景高斯模糊的缩小比例
     *
     * @param scale 缩小比例
     */
    public DialogLayer backgroundBlurScale(@FloatRange(from = 1) float scale) {
        getConfig().mBackgroundBlurScale = scale;
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param bitmap 图片
     */
    public DialogLayer backgroundBitmap(@NonNull Bitmap bitmap) {
        getConfig().mBackgroundBitmap = bitmap;
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param resource 资源ID
     */
    public DialogLayer backgroundResource(@DrawableRes int resource) {
        getConfig().mBackgroundResource = resource;
        return this;
    }

    /**
     * 设置背景Drawable
     *
     * @param drawable Drawable
     */
    public DialogLayer backgroundDrawable(@NonNull Drawable drawable) {
        getConfig().mBackgroundDrawable = drawable;
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
    public DialogLayer backgroundColorInt(@ColorInt int colorInt) {
        getConfig().mBackgroundColor = colorInt;
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
    public DialogLayer backgroundColorRes(@ColorRes int colorRes) {
        getConfig().mBackgroundColor = ContextCompat.getColor(getActivity(), colorRes);
        return this;
    }

    /**
     * 设置浮层外部是否拦截触摸
     * 默认为true，false则事件有activityContent本身消费
     *
     * @param intercept 外部是否拦截触摸
     */
    public DialogLayer outsideInterceptTouchEvent(boolean intercept) {
        getConfig().mOutsideInterceptTouchEvent = intercept;
        return this;
    }

    /**
     * 设置点击浮层以外区域是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    public DialogLayer cancelableOnTouchOutside(boolean cancelable) {
        getConfig().mCancelableOnTouchOutside = cancelable;
        return this;
    }

    /**
     * 设置点击返回键是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    @Override
    public DialogLayer cancelableOnClickKeyBack(boolean cancelable) {
        return (DialogLayer) super.cancelableOnClickKeyBack(cancelable);
    }

    /**
     * 获取自定义的浮层控件
     *
     * @return View
     */
    public View getContentView() {
        return getViewHolder().getContent();
    }

    /**
     * 获取背景图
     *
     * @return ImageView
     */
    public ImageView getBackground() {
        return getViewHolder().getBackground();
    }

    /**
     * 适配软键盘的弹出，布局自动上移
     * 在某几个EditText获取焦点时布局上移
     * 在{@link OnVisibleChangeListener#onShow(Layer)}中调用
     * 应该和{@link #removeSoftInput()}成对出现
     *
     * @param editText 焦点EditTexts
     */
    public void compatSoftInput(EditText... editText) {
        Activity activity = Utils.getActivity(getActivity());
        if (activity != null) {
            mSoftInputHelper = SoftInputHelper.attach(activity)
                    .init(getViewHolder().getContentWrapper(), getViewHolder().getContent(), editText)
                    .moveWithTranslation();
        }
    }

    /**
     * 移除软键盘适配
     * 在{@link OnVisibleChangeListener#onDismiss(Layer)}中调用
     * 应该和{@link #compatSoftInput(EditText...)}成对出现
     */
    public void removeSoftInput() {
        if (mSoftInputHelper != null) {
            mSoftInputHelper.detach();
        }
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {

        private FrameLayout mActivityContent;
        private View mTarget;

        private ImageView mBackground;
        private FrameLayout mContentWrapper;
        private View mContent;

        public void recycle() {
            if (mBackground.getDrawable() instanceof BitmapDrawable) {
                BitmapDrawable bd = (BitmapDrawable) mBackground.getDrawable();
                bd.getBitmap().recycle();
            }
        }

        public void setActivityContent(FrameLayout activityContent) {
            mActivityContent = activityContent;
        }

        public FrameLayout getActivityContent() {
            return mActivityContent;
        }

        public void setTarget(View target) {
            mTarget = target;
        }

        public View getTarget() {
            return mTarget;
        }

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
            mContentWrapper = getChild().findViewById(R.id.fl_content_wrapper);
            mBackground = getChild().findViewById(R.id.iv_background);
        }

        @Override
        public FrameLayout getChild() {
            return (FrameLayout) super.getChild();
        }

        void setContent(View content) {
            mContent = content;
        }

        public View getContent() {
            return mContent;
        }

        public FrameLayout getContentWrapper() {
            return mContentWrapper;
        }

        public ImageView getBackground() {
            return mBackground;
        }
    }

    protected static class Config extends DecorLayer.Config {
        private AnimatorCreator mBackgroundAnimatorCreator = null;
        private AnimatorCreator mContentAnimatorCreator = null;

        @LayoutRes
        private int contentViewId = 0;

        private boolean mOutsideInterceptTouchEvent = true;

        private boolean mCancelableOnTouchOutside = true;

        @IdRes
        private int mAsStatusBarViewId = 0;

        private int mGravity = Gravity.CENTER;
        private float mBackgroundBlurPercent = 0;
        private float mBackgroundBlurRadius = 0;
        private float mBackgroundBlurScale = 2;
        private Bitmap mBackgroundBitmap = null;
        private int mBackgroundResource = -1;
        private Drawable mBackgroundDrawable = null;
        @ColorInt
        private int mBackgroundColor = Color.TRANSPARENT;

        private boolean mAlignmentInside = false;
        private Alignment.Direction mAlignmentDirection = Alignment.Direction.VERTICAL;
        private Alignment.Horizontal mAlignmentHorizontal = Alignment.Horizontal.CENTER;
        private Alignment.Vertical mAlignmentVertical = Alignment.Vertical.BELOW;
    }

    protected static class ListenerHolder extends Layer.ListenerHolder {
    }
}
