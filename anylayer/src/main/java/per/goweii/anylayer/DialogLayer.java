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
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import per.goweii.burred.Blurred;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class DialogLayer extends DecorLayer implements ViewManager.OnLifeListener, ViewManager.OnKeyListener, ViewManager.OnPreDrawListener {

    private final Config mConfig;
    private final ViewHolder mViewHolder;

    private AnimatorCreator mBackgroundAnimatorCreator = null;
    private AnimatorCreator mContentAnimatorCreator = null;

    @Nullable
    private SoftInputHelper mSoftInputHelper = null;

    public DialogLayer(@NonNull Activity activity) {
        super(activity);
        mConfig = new Config();
        mViewHolder = new ViewHolder();
        FrameLayout rootView = (FrameLayout) activity.getWindow().getDecorView();
        FrameLayout activityContentView = rootView.findViewById(android.R.id.content);
        mViewHolder.setRootView(rootView);
        mViewHolder.setActivityContentView(activityContentView);
    }

    public DialogLayer(@NonNull Context context) {
        this(Objects.requireNonNull(Utils.getActivity(context)));
    }

    public DialogLayer(@NonNull View targetView) {
        this(targetView.getContext());
        mViewHolder.setTargetView(targetView);
    }

    @Level
    @Override
    protected int getLevel() {
        return Level.DIALOG;
    }

    @NonNull
    @Override
    protected View onCreateLayer(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        FrameLayout container = (FrameLayout) inflater.inflate(R.layout.anylayer_dialog_layer, parent, false);
        if (mViewHolder.getContent() == null) {
            mViewHolder.setContent(LayoutInflater.from(mContext).inflate(mConfig.contentViewId, container, false));
        }
        mViewHolder.setContainer(container);
        return container;
    }

    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        Animator backgroundAnimator;
        if (mBackgroundAnimatorCreator != null) {
            backgroundAnimator = mBackgroundAnimatorCreator.createInAnimator(mViewHolder.getBackground());
        } else {
            backgroundAnimator = AnimatorHelper.createAlphaInAnim(mViewHolder.getBackground());
        }
        Animator contentAnimator;
        if (mContentAnimatorCreator != null) {
            contentAnimator = mContentAnimatorCreator.createInAnimator(mViewHolder.getContent());
        } else {
            contentAnimator = AnimatorHelper.createZoomAlphaInAnim(mViewHolder.getContent());
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        Animator backgroundAnimator;
        if (mBackgroundAnimatorCreator != null) {
            backgroundAnimator = mBackgroundAnimatorCreator.createOutAnimator(mViewHolder.getBackground());
        } else {
            backgroundAnimator = AnimatorHelper.createAlphaOutAnim(mViewHolder.getBackground());
        }
        Animator contentAnimator;
        if (mContentAnimatorCreator != null) {
            contentAnimator = mContentAnimatorCreator.createOutAnimator(mViewHolder.getContent());
        } else {
            contentAnimator = AnimatorHelper.createZoomAlphaOutAnim(mViewHolder.getContent());
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    @Override
    public void onAttach() {
        initContainer();
        initBackground();
        initContent();
        mViewHolder.bindListener();
        super.onAttach();
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
        mViewHolder.recycle();
    }

    private void initContainer() {
        if (mConfig.mOutsideInterceptTouchEvent) {
            mViewHolder.getContainer().setClickable(true);
            if (mConfig.mCancelableOnTouchOutside) {
                mViewHolder.getContainer().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            }
        } else {
            mViewHolder.getContainer().setClickable(false);
        }
        if (mViewHolder.getActivityContentView() != null) {
            // 非指定父布局的，添加到DecorView，此时mViewHolder.getRootView()为DecorView
            final int[] locationDecor = new int[2];
            mViewHolder.getRootView().getLocationOnScreen(locationDecor);
            final int[] locationActivityContent = new int[2];
            mViewHolder.getActivityContentView().getLocationOnScreen(locationActivityContent);
            FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) mViewHolder.getContainer().getLayoutParams();
            containerParams.leftMargin = locationActivityContent[0] - locationDecor[0];
            containerParams.topMargin = 0;
            containerParams.width = mViewHolder.getActivityContentView().getWidth();
            containerParams.height = mViewHolder.getActivityContentView().getHeight() + (locationActivityContent[1] - locationDecor[1]);
            mViewHolder.getContainer().setLayoutParams(containerParams);
        }
        if (mViewHolder.getTargetView() == null) {
            FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) mViewHolder.getContentWrapper().getLayoutParams();
            contentWrapperParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
            contentWrapperParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
            mViewHolder.getContentWrapper().setLayoutParams(contentWrapperParams);
        } else {
            initContainerWithTarget();
        }
    }

    private void initContainerWithTarget() {
        mViewHolder.getContainer().setClipToPadding(false);
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) mViewHolder.getContentWrapper().getLayoutParams();
        contentWrapperParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        contentWrapperParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        mViewHolder.getContentWrapper().setLayoutParams(contentWrapperParams);
        final int[] locationTarget = new int[2];
        mViewHolder.getTargetView().getLocationOnScreen(locationTarget);
        final int[] locationRoot = new int[2];
        mViewHolder.getRootView().getLocationOnScreen(locationRoot);
        final int targetX = (locationTarget[0] - locationRoot[0]);
        final int targetY = (locationTarget[1] - locationRoot[1]);
        final int targetWidth = mViewHolder.getTargetView().getWidth();
        final int targetHeight = mViewHolder.getTargetView().getHeight();
        final int[] padding = initContainerWithTargetPadding(targetX, targetY, targetWidth, targetHeight);
        mViewHolder.getContainer().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (mViewHolder.getContainer().getViewTreeObserver().isAlive()) {
                    mViewHolder.getContainer().getViewTreeObserver().removeOnPreDrawListener(this);
                }
                initContainerWithTargetMargin(targetX, targetY, targetWidth, targetHeight, padding[0], padding[1]);
                return false;
            }
        });
        if (!mConfig.mOutsideInterceptTouchEvent) {
            mViewHolder.getContainer().getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    final int[] locationTarget = new int[2];
                    mViewHolder.getTargetView().getLocationOnScreen(locationTarget);
                    final int[] locationRoot = new int[2];
                    mViewHolder.getRootView().getLocationOnScreen(locationRoot);
                    final int targetX = (locationTarget[0] - locationRoot[0]);
                    final int targetY = (locationTarget[1] - locationRoot[1]);
                    final int targetWidth = mViewHolder.getTargetView().getWidth();
                    final int targetHeight = mViewHolder.getTargetView().getHeight();
                    final int[] padding = initContainerWithTargetPadding(targetX, targetY, targetWidth, targetHeight);
                    initContainerWithTargetMargin(targetX, targetY, targetWidth, targetHeight, padding[0], padding[1]);
                }
            });
        }
    }

    private int[] initContainerWithTargetPadding(int targetX, int targetY, int targetWidth, int targetHeight) {
        final int[] padding = new int[]{0, 0, 0, 0};
        FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) mViewHolder.getContainer().getLayoutParams();
        if (mConfig.mAlignmentDirection == Alignment.Direction.HORIZONTAL) {
            if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
                padding[2] = containerParams.width - targetX;
            } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
                padding[0] = targetX + targetWidth;
            } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                padding[0] = targetX;
            } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
                padding[2] = containerParams.width - targetX - targetWidth;
            }
        } else if (mConfig.mAlignmentDirection == Alignment.Direction.VERTICAL) {
            if (mConfig.mAlignmentVertical == Alignment.Vertical.ABOVE) {
                padding[3] = containerParams.height - targetY;
            } else if (mConfig.mAlignmentVertical == Alignment.Vertical.BELOW) {
                padding[1] = targetY + targetHeight;
            } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                padding[1] = targetY;
            } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
                padding[3] = containerParams.height - targetY - targetHeight;
            }
        }
        mViewHolder.getContainer().setPadding(padding[0], padding[1], padding[2], padding[3]);
        return padding;
    }

    private void initContainerWithTargetMargin(int targetX, int targetY, int targetWidth, int targetHeight, int paddingLeft, int paddingTop) {
        final int width = mViewHolder.getContentWrapper().getWidth();
        final int height = mViewHolder.getContentWrapper().getHeight();
        int x = 0;
        int y = 0;
        if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.CENTER) {
            x = targetX - (width - targetWidth) / 2;
        } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
            x = targetX - width;
        } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
            x = targetX + targetWidth;
        } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
            x = targetX;
        } else if (mConfig.mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
            x = targetX - (width - targetWidth);
        }
        if (mConfig.mAlignmentVertical == Alignment.Vertical.CENTER) {
            y = targetY - (height - targetHeight) / 2;
        } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ABOVE) {
            y = targetY - height;
        } else if (mConfig.mAlignmentVertical == Alignment.Vertical.BELOW) {
            y = targetY + targetHeight;
        } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
            y = targetY;
        } else if (mConfig.mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
            y = targetY - (height - targetHeight);
        }
        x = x - paddingLeft;
        y = y - paddingTop;
        if (mConfig.mAlignmentInside) {
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
    }

    private void initBackground() {
        if (mConfig.mBackgroundBlurPercent > 0 || mConfig.mBackgroundBlurRadius > 0) {
            mViewHolder.getBackground().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mViewHolder.getBackground().getViewTreeObserver().removeOnPreDrawListener(this);
                    Bitmap snapshot = Utils.snapshot(mViewHolder.getRootView());
                    int[] locationRootView = new int[2];
                    mViewHolder.getRootView().getLocationOnScreen(locationRootView);
                    int[] locationBackground = new int[2];
                    mViewHolder.getBackground().getLocationOnScreen(locationBackground);
                    int x = locationBackground[0] - locationRootView[0];
                    int y = locationBackground[1] - locationRootView[1];
                    Bitmap original = Bitmap.createBitmap(snapshot, x, y, mViewHolder.getBackground().getWidth(), mViewHolder.getBackground().getHeight());
                    snapshot.recycle();
                    Blurred.init(mContext);
                    Blurred blurred = Blurred.with(original)
                            .recycleOriginal(true)
                            .keepSize(false)
                            .scale(mConfig.mBackgroundBlurScale);
                    if (mConfig.mBackgroundBlurPercent > 0) {
                        blurred.percent(mConfig.mBackgroundBlurPercent);
                    } else if (mConfig.mBackgroundBlurRadius > 0) {
                        blurred.radius(mConfig.mBackgroundBlurRadius);
                    }
                    Bitmap blurBitmap = blurred.blur();
                    mViewHolder.getBackground().setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mViewHolder.getBackground().setImageBitmap(blurBitmap);
                    mViewHolder.getBackground().setColorFilter(mConfig.mBackgroundColor);
                    return true;
                }
            });
        } else {
            if (mConfig.mBackgroundBitmap != null) {
                mViewHolder.getBackground().setImageBitmap(mConfig.mBackgroundBitmap);
                mViewHolder.getBackground().setColorFilter(mConfig.mBackgroundColor);
            } else if (mConfig.mBackgroundResource != -1) {
                mViewHolder.getBackground().setImageResource(mConfig.mBackgroundResource);
                mViewHolder.getBackground().setColorFilter(mConfig.mBackgroundColor);
            } else if (mConfig.mBackgroundDrawable != null) {
                mViewHolder.getBackground().setImageDrawable(mConfig.mBackgroundDrawable);
                mViewHolder.getBackground().setColorFilter(mConfig.mBackgroundColor);
            } else {
                mViewHolder.getBackground().setImageDrawable(new ColorDrawable(mConfig.mBackgroundColor));
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
            if (mViewHolder.getTargetView() == null && mConfig.mGravity != -1) {
                ViewGroup.LayoutParams params = mViewHolder.getContent().getLayoutParams();
                FrameLayout.LayoutParams contentParams;
                if (params == null) {
                    contentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                } else if (params instanceof FrameLayout.LayoutParams) {
                    contentParams = (FrameLayout.LayoutParams) params;
                } else {
                    contentParams = new FrameLayout.LayoutParams(params.width, params.height);
                }
                contentParams.gravity = mConfig.mGravity;
                mViewHolder.getContent().setLayoutParams(contentParams);
            }
            if (mConfig.mAsStatusBarViewId > 0) {
                View statusBar = mViewHolder.getContent().findViewById(mConfig.mAsStatusBarViewId);
                if (statusBar != null) {
                    ViewGroup.LayoutParams params = statusBar.getLayoutParams();
                    params.height = Utils.getStatusBarHeight(mContext);
                    statusBar.setLayoutParams(params);
                    statusBar.setVisibility(View.VISIBLE);
                }
            }
            mViewHolder.getContentWrapper().addView(mViewHolder.getContent());
        }
    }


    /**
     * 设置自自定义View
     *
     * @param contentView 自定以View
     */
    public DialogLayer contentView(@NonNull View contentView) {
        mViewHolder.setContent(contentView);
        return this;
    }

    /**
     * 设置自定义布局文件
     *
     * @param contentViewId 自定义布局ID
     */
    public DialogLayer contentView(@LayoutRes int contentViewId) {
        mConfig.contentViewId = contentViewId;
        return this;
    }

    /**
     * 设置自定义布局文件中状态栏的占位View
     * 该控件高度将设置为状态栏高度，可用来使布局整体下移，避免状态栏遮挡
     *
     * @param statusBarId 状态栏的占位View
     */
    public DialogLayer asStatusBar(@IdRes int statusBarId) {
        mConfig.mAsStatusBarViewId = statusBarId;
        return this;
    }

    /**
     * 设置子布局的gravity
     * 可直接在布局文件指定layout_gravity属性，作用相同
     *
     * @param gravity {@link Gravity}
     */
    public DialogLayer gravity(int gravity) {
        mConfig.mGravity = gravity;
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
        mConfig.mAlignmentDirection = direction;
        mConfig.mAlignmentHorizontal = horizontal;
        mConfig.mAlignmentVertical = vertical;
        mConfig.mAlignmentInside = inside;
        return this;
    }

    /**
     * 自定义浮层的进入和退出动画
     * 可使用工具类{@link AnimatorHelper}
     *
     * @param contentAnimatorCreator AnimatorCreator
     */
    public DialogLayer contentAnimator(AnimatorCreator contentAnimatorCreator) {
        mContentAnimatorCreator = contentAnimatorCreator;
        return this;
    }

    /**
     * 自定义背景的进入和退出动画
     * 可使用工具类{@link AnimatorHelper}
     *
     * @param backgroundAnimatorCreator AnimatorCreator
     */
    public DialogLayer backgroundAnimator(AnimatorCreator backgroundAnimatorCreator) {
        mBackgroundAnimatorCreator = backgroundAnimatorCreator;
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
        mConfig.mBackgroundBlurRadius = radius;
        return this;
    }

    public DialogLayer backgroundBlurPercent(@FloatRange(from = 0, fromInclusive = false) float percent) {
        mConfig.mBackgroundBlurPercent = percent;
        return this;
    }

    /**
     * 设置背景高斯模糊的缩小比例
     *
     * @param scale 缩小比例
     */
    public DialogLayer backgroundBlurScale(@FloatRange(from = 1) float scale) {
        mConfig.mBackgroundBlurScale = scale;
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param bitmap 图片
     */
    public DialogLayer backgroundBitmap(@NonNull Bitmap bitmap) {
        mConfig.mBackgroundBitmap = bitmap;
        return this;
    }

    /**
     * 设置背景资源
     *
     * @param resource 资源ID
     */
    public DialogLayer backgroundResource(@DrawableRes int resource) {
        mConfig.mBackgroundResource = resource;
        return this;
    }

    /**
     * 设置背景Drawable
     *
     * @param drawable Drawable
     */
    public DialogLayer backgroundDrawable(@NonNull Drawable drawable) {
        mConfig.mBackgroundDrawable = drawable;
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
        mConfig.mBackgroundColor = colorInt;
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
        mConfig.mBackgroundColor = ContextCompat.getColor(mContext, colorRes);
        return this;
    }

    /**
     * 设置浮层外部是否拦截触摸
     * 默认为true，false则事件有activityContent本身消费
     *
     * @param intercept 外部是否拦截触摸
     */
    public DialogLayer outsideInterceptTouchEvent(boolean intercept) {
        mConfig.mOutsideInterceptTouchEvent = intercept;
        return this;
    }

    /**
     * 设置点击浮层以外区域是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    public DialogLayer cancelableOnTouchOutside(boolean cancelable) {
        mConfig.mCancelableOnTouchOutside = cancelable;
        return this;
    }

    /**
     * 设置点击返回键是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    public DialogLayer cancelableOnClickKeyBack(boolean cancelable) {
        cancelableOnKeyDown(cancelable);
        return this;
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
     * 适配软键盘的弹出，布局自动上移
     * 在某几个EditText获取焦点时布局上移
     * 在{@link DialogLayer.OnVisibleChangeListener#onShow(DialogLayer)}中调用
     * 应该和{@link #removeSoftInput()}成对出现
     *
     * @param editText 焦点EditTexts
     */
    public void compatSoftInput(EditText... editText) {
        Activity activity = Utils.getActivity(mContext);
        if (activity != null) {
            mSoftInputHelper = SoftInputHelper.attach(activity)
                    .init(mViewHolder.getContentWrapper(), mViewHolder.getContent(), editText)
                    .moveWithTranslation();
        }
    }

    /**
     * 移除软键盘适配
     * 在{@link DialogLayer.OnVisibleChangeListener#onDismiss(DialogLayer)}中调用
     * 应该和{@link #compatSoftInput(EditText...)}成对出现
     */
    public void removeSoftInput() {
        if (mSoftInputHelper != null) {
            mSoftInputHelper.detach();
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
        void bindData(DialogLayer dialogLayer);
    }

    public interface OnLayerClickListener {
        /**
         * 点击事件回调
         */
        void onClick(DialogLayer dialogLayer, View v);
    }

    public interface OnLayerDismissListener {
        /**
         * 开始隐藏，动画刚开始执行
         */
        void onDismissing(DialogLayer dialogLayer);

        /**
         * 已隐藏，浮层已被移除
         */
        void onDismissed(DialogLayer dialogLayer);
    }

    public interface OnLayerShowListener {
        /**
         * 开始显示，动画刚开始执行
         */
        void onShowing(DialogLayer dialogLayer);

        /**
         * 已显示，浮层已显示且动画结束
         */
        void onShown(DialogLayer dialogLayer);
    }

    public interface OnVisibleChangeListener {
        /**
         * 浮层显示，刚被添加到父布局，进入动画未开始
         */
        void onShow(DialogLayer dialogLayer);

        /**
         * 浮层隐藏，已被从父布局移除，隐藏动画已结束
         */
        void onDismiss(DialogLayer dialogLayer);
    }

    final class ViewHolder {

        private ViewGroup mRootView;
        private FrameLayout mActivityContentView;
        private View mTargetView;
        private FrameLayout mContainer;

        private ImageView mBackground;
        private FrameLayout mContentWrapper;
        private View mContent;

        private SparseArray<View> views = null;
        private SparseArray<DialogLayer.OnLayerClickListener> onClickListeners = null;

        void recycle() {
            if (mBackground.getDrawable() instanceof BitmapDrawable) {
                BitmapDrawable bd = (BitmapDrawable) mBackground.getDrawable();
                bd.getBitmap().recycle();
            }
        }

        public void setRootView(ViewGroup rootView) {
            mRootView = rootView;
        }

        public void setActivityContentView(FrameLayout activityContentView) {
            mActivityContentView = activityContentView;
        }

        public void setTargetView(View targetView) {
            mTargetView = targetView;
        }

        public void setContainer(FrameLayout container) {
            mContainer = container;
            mContentWrapper = mContainer.findViewById(R.id.fl_content_wrapper);
            mBackground = mContainer.findViewById(R.id.iv_background);
        }

        public ViewGroup getRootView() {
            return mRootView;
        }

        public FrameLayout getActivityContentView() {
            return mActivityContentView;
        }

        public View getTargetView() {
            return mTargetView;
        }

        void setContent(View content) {
            mContent = content;
        }

        public View getContent() {
            return mContent;
        }

        public FrameLayout getContainer() {
            return mContainer;
        }

        public FrameLayout getContentWrapper() {
            return mContentWrapper;
        }

        public ImageView getBackground() {
            return mBackground;
        }

        void bindListener() {
            if (onClickListeners == null) {
                return;
            }
            for (int i = 0; i < onClickListeners.size(); i++) {
                int viewId = onClickListeners.keyAt(i);
                final DialogLayer.OnLayerClickListener listener = onClickListeners.valueAt(i);
                getView(viewId).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(DialogLayer.this, v);
                    }
                });
            }
        }

        public <V extends View> V getView(@IdRes int viewId) {
            if (views == null) {
                views = new SparseArray<>();
            }
            if (views.indexOfKey(viewId) < 0) {
                V view = mContent.findViewById(viewId);
                views.put(viewId, view);
                return view;
            }
            return (V) views.get(viewId);
        }

        void addOnClickListener(DialogLayer.OnLayerClickListener listener, @IdRes int viewId, @IdRes int... viewIds) {
            if (onClickListeners == null) {
                onClickListeners = new SparseArray<>();
            }
            if (onClickListeners.indexOfKey(viewId) < 0) {
                onClickListeners.put(viewId, listener);
            }
            if (viewIds != null && viewIds.length > 0) {
                for (int id : viewIds) {
                    if (onClickListeners.indexOfKey(id) < 0) {
                        onClickListeners.put(id, listener);
                    }
                }
            }
        }
    }

    private final class Config {
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

    private final class ListenerHolder {

        private List<DataBinder> mDataBinders = null;
        private List<DialogLayer.OnVisibleChangeListener> mOnVisibleChangeListeners = null;
        private List<DialogLayer.OnLayerShowListener> mOnLayerShowListeners = null;
        private List<DialogLayer.OnLayerDismissListener> mOnLayerDismissListeners = null;

        void addDataBinder(DataBinder dataBinder) {
            if (mDataBinders == null) {
                mDataBinders = new ArrayList<>(1);
            }
            mDataBinders.add(dataBinder);
        }

        void addOnVisibleChangeListener(DialogLayer.OnVisibleChangeListener onVisibleChangeListener) {
            if (mOnVisibleChangeListeners == null) {
                mOnVisibleChangeListeners = new ArrayList<>(1);
            }
            mOnVisibleChangeListeners.add(onVisibleChangeListener);
        }

        void addOnLayerShowListener(DialogLayer.OnLayerShowListener onLayerShowListener) {
            if (mOnLayerShowListeners == null) {
                mOnLayerShowListeners = new ArrayList<>(1);
            }
            mOnLayerShowListeners.add(onLayerShowListener);
        }

        void addOnLayerDismissListener(DialogLayer.OnLayerDismissListener onLayerDismissListener) {
            if (mOnLayerDismissListeners == null) {
                mOnLayerDismissListeners = new ArrayList<>(1);
            }
            mOnLayerDismissListeners.add(onLayerDismissListener);
        }

        void notifyDataBinder() {
            if (mDataBinders != null) {
                for (DataBinder dataBinder : mDataBinders) {
                    dataBinder.bindData(DialogLayer.this);
                }
            }
        }

        void notifyVisibleChangeOnShow() {
            if (mOnVisibleChangeListeners != null) {
                for (DialogLayer.OnVisibleChangeListener onVisibleChangeListener : mOnVisibleChangeListeners) {
                    onVisibleChangeListener.onShow(DialogLayer.this);
                }
            }
        }

        void notifyVisibleChangeOnDismiss() {
            if (mOnVisibleChangeListeners != null) {
                for (DialogLayer.OnVisibleChangeListener onVisibleChangeListener : mOnVisibleChangeListeners) {
                    onVisibleChangeListener.onDismiss(DialogLayer.this);
                }
            }
        }

        void notifyLayerOnShowing() {
            if (mOnLayerShowListeners != null) {
                for (DialogLayer.OnLayerShowListener onLayerShowListener : mOnLayerShowListeners) {
                    onLayerShowListener.onShowing(DialogLayer.this);
                }
            }
        }

        void notifyLayerOnShown() {
            if (mOnLayerShowListeners != null) {
                for (DialogLayer.OnLayerShowListener onLayerShowListener : mOnLayerShowListeners) {
                    onLayerShowListener.onShown(DialogLayer.this);
                }
            }
        }

        void notifyLayerOnDismissing() {
            if (mOnLayerDismissListeners != null) {
                for (DialogLayer.OnLayerDismissListener onLayerDismissListener : mOnLayerDismissListeners) {
                    onLayerDismissListener.onDismissing(DialogLayer.this);
                }
            }
        }

        void notifyLayerOnDismissed() {
            if (mOnLayerDismissListeners != null) {
                for (DialogLayer.OnLayerDismissListener onLayerDismissListener : mOnLayerDismissListeners) {
                    onLayerDismissListener.onDismissed(DialogLayer.this);
                }
            }
        }
    }
}
