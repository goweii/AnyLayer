package per.goweii.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import per.goweii.burred.Blurred;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class DialogLayer extends DecorLayer {

    private static final long ANIM_DUR_DEF = 220L;
    private static final float BG_DIM_AMOUNT_DEF = 0.6F;

    private SoftInputHelper mSoftInputHelper = null;

    public DialogLayer(Context context) {
        this(Utils.getActivity(Utils.requireNonNull(context, "context == null")));
    }

    public DialogLayer(Activity activity) {
        super(activity);
        getViewHolder().setActivityContent(getViewHolder().getDecor().findViewById(android.R.id.content));
    }

    @Override
    protected Level getLevel() {
        return Level.DIALOG;
    }

    @Override
    protected ViewHolder onCreateViewHolder() {
        return new ViewHolder();
    }

    @Override
    public ViewHolder getViewHolder() {
        return (ViewHolder) super.getViewHolder();
    }

    @Override
    protected Config onCreateConfig() {
        return new Config();
    }

    @Override
    public Config getConfig() {
        return (Config) super.getConfig();
    }

    @Override
    protected View onCreateChild(LayoutInflater inflater, ViewGroup parent) {
        ContainerLayout container = (ContainerLayout) inflater.inflate(R.layout.anylayer_dialog_layer, parent, false);
        getViewHolder().setChild(container);
        getViewHolder().setContent(onCreateContent(inflater, getViewHolder().getContentWrapper()));
        getViewHolder().getContentWrapper().addView(getViewHolder().getContent());
        return container;
    }

    protected View onCreateContent(LayoutInflater inflater, ViewGroup parent) {
        if (getViewHolder().getContent() != null) {
            ViewGroup contentParent = (ViewGroup) getViewHolder().getContent().getParent();
            if (contentParent != null) {
                contentParent.removeView(getViewHolder().getContent());
            }
            return getViewHolder().getContent();
        }
        return inflater.inflate(getConfig().mContentViewId, parent, false);
    }

    @Override
    protected Animator onCreateInAnimator(View view) {
        Animator backgroundAnimator = onCreateBackgroundInAnimator(getViewHolder().getBackground());
        Animator contentAnimator = onCreateContentInAnimator(getViewHolder().getContent());
        if (backgroundAnimator == null && contentAnimator == null) {
            return null;
        }
        if (backgroundAnimator == null) {
            return contentAnimator;
        }
        if (contentAnimator == null) {
            return backgroundAnimator;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    protected Animator onCreateBackgroundInAnimator(View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createInAnimator(view);
        } else {
            backgroundAnimator = onCreateDefBackgroundInAnimator(view);
        }
        return backgroundAnimator;
    }

    protected Animator onCreateDefBackgroundInAnimator(View view) {
        return AnimatorHelper.createAlphaInAnim(view);
    }

    protected Animator onCreateContentInAnimator(View view) {
        Animator contentAnimator;
        if (getConfig().mContentAnimatorCreator != null) {
            contentAnimator = getConfig().mContentAnimatorCreator.createInAnimator(view);
        } else {
            if (getConfig().mAnimStyle != null) {
                switch (getConfig().mAnimStyle) {
                    case ALPHA:
                        contentAnimator = AnimatorHelper.createAlphaInAnim(view);
                        break;
                    case ZOOM:
                        contentAnimator = AnimatorHelper.createZoomInAnim(view);
                        break;
                    case LEFT:
                        contentAnimator = AnimatorHelper.createLeftInAnim(view);
                        break;
                    case RIGHT:
                        contentAnimator = AnimatorHelper.createRightInAnim(view);
                        break;
                    case TOP:
                        contentAnimator = AnimatorHelper.createTopInAnim(view);
                        break;
                    case BOTTOM:
                        contentAnimator = AnimatorHelper.createBottomInAnim(view);
                        break;
                    default:
                        contentAnimator = onCreateDefContentInAnimator(view);
                        break;
                }
            } else {
                switch (getConfig().mDragStyle) {
                    case Left:
                        contentAnimator = AnimatorHelper.createLeftInAnim(view);
                        break;
                    case Top:
                        contentAnimator = AnimatorHelper.createTopInAnim(view);
                        break;
                    case Right:
                        contentAnimator = AnimatorHelper.createRightInAnim(view);
                        break;
                    case Bottom:
                        contentAnimator = AnimatorHelper.createBottomInAnim(view);
                        break;
                    case None:
                    default:
                        contentAnimator = onCreateDefContentInAnimator(view);
                        break;
                }
            }
            contentAnimator.setDuration(ANIM_DUR_DEF);
        }
        return contentAnimator;
    }

    protected Animator onCreateDefContentInAnimator(View view) {
        return AnimatorHelper.createZoomAlphaInAnim(view);
    }

    @Override
    protected Animator onCreateOutAnimator(View view) {
        Animator backgroundAnimator = onCreateBackgroundOutAnimator(getViewHolder().getBackground());
        Animator contentAnimator = onCreateContentOutAnimator(getViewHolder().getContent());
        if (backgroundAnimator == null && contentAnimator == null) {
            return null;
        }
        if (backgroundAnimator == null) {
            return contentAnimator;
        }
        if (contentAnimator == null) {
            return backgroundAnimator;
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    protected Animator onCreateBackgroundOutAnimator(View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createOutAnimator(view);
        } else {
            backgroundAnimator = onCreateDefBackgroundOutAnimator(view);
        }
        if (getConfig().mContentAnimatorCreator == null) {
            backgroundAnimator.setDuration(ANIM_DUR_DEF);
        }
        return backgroundAnimator;
    }

    protected Animator onCreateDefBackgroundOutAnimator(View view) {
        return AnimatorHelper.createAlphaOutAnim(view);
    }

    protected Animator onCreateContentOutAnimator(View view) {
        Animator contentAnimator;
        if (getConfig().mContentAnimatorCreator != null) {
            contentAnimator = getConfig().mContentAnimatorCreator.createOutAnimator(view);
        } else {
            if (getConfig().mAnimStyle != null) {
                switch (getConfig().mAnimStyle) {
                    case ALPHA:
                        contentAnimator = AnimatorHelper.createAlphaOutAnim(view);
                        break;
                    case ZOOM:
                        contentAnimator = AnimatorHelper.createZoomOutAnim(view);
                        break;
                    case LEFT:
                        contentAnimator = AnimatorHelper.createLeftOutAnim(view);
                        break;
                    case RIGHT:
                        contentAnimator = AnimatorHelper.createRightOutAnim(view);
                        break;
                    case TOP:
                        contentAnimator = AnimatorHelper.createTopOutAnim(view);
                        break;
                    case BOTTOM:
                        contentAnimator = AnimatorHelper.createBottomOutAnim(view);
                        break;
                    default:
                        contentAnimator = onCreateDefContentOutAnimator(view);
                        break;
                }
            } else {
                switch (getConfig().mDragStyle) {
                    case Left:
                        contentAnimator = AnimatorHelper.createLeftOutAnim(view);
                        break;
                    case Top:
                        contentAnimator = AnimatorHelper.createTopOutAnim(view);
                        break;
                    case Right:
                        contentAnimator = AnimatorHelper.createRightOutAnim(view);
                        break;
                    case Bottom:
                        contentAnimator = AnimatorHelper.createBottomOutAnim(view);
                        break;
                    case None:
                    default:
                        contentAnimator = onCreateDefContentOutAnimator(view);
                        break;
                }
            }
            contentAnimator.setDuration(ANIM_DUR_DEF);
        }
        return contentAnimator;
    }

    protected Animator onCreateDefContentOutAnimator(View view) {
        return AnimatorHelper.createZoomAlphaOutAnim(view);
    }

    @Override
    public void onAttach() {
        super.onAttach();
        initContent();
        initBackground();
        initContainer();
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
    public void onPreRemove() {
        super.onPreRemove();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getViewHolder().recycle();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.getViewSize(getViewHolder().getBackground(), new Runnable() {
            @Override
            public void run() {
                fitContainerToActivityContent();
            }
        });
    }

    protected void initContainer() {
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
            getViewHolder().getChild().setOnClickListener(null);
            getViewHolder().getChild().setClickable(false);
        }
        if (getConfig().mOutsideTouchedToDismiss || getConfig().mOutsideTouchedListener != null) {
            getViewHolder().getChild().setOnTouchedListener(new ContainerLayout.OnTouchedListener() {
                @Override
                public void onTouched() {
                    if (getConfig().mOutsideTouchedToDismiss) {
                        dismiss();
                    }
                    if (getConfig().mOutsideTouchedListener != null) {
                        getConfig().mOutsideTouchedListener.outsideTouched();
                    }
                }
            });
        }
        fitContainerToActivityContent();
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        contentWrapperParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        contentWrapperParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
        if (getConfig().mAvoidStatusBar) {
            getViewHolder().getContentWrapper().setPadding(0, Utils.getStatusBarHeight(getActivity()), 0, 0);
            getViewHolder().getContentWrapper().setClipToPadding(false);
        } else {
            getViewHolder().getContentWrapper().setPadding(0, 0, 0, 0);
            getViewHolder().getContentWrapper().setClipToPadding(true);
        }
        getViewHolder().getContentWrapper().setDragStyle(getConfig().mDragStyle);
        getViewHolder().getContentWrapper().setOnDragListener(new DragLayout.OnDragListener() {
            @Override
            public void onDragStart() {
                if (getConfig().mDragTransformer == null) {
                    getConfig().mDragTransformer = new DragTransformer() {
                        @Override
                        public void onDragging(View content, View background, float f) {
                            background.setAlpha(1F - f);
                        }
                    };
                }
            }

            @Override
            public void onDragging(float f) {
                if (getConfig().mDragTransformer != null) {
                    getConfig().mDragTransformer.onDragging(getViewHolder().getContent(), getViewHolder().getBackground(), f);
                }
            }

            @Override
            public void onDragEnd() {
                // 动画执行结束后不能直接removeView，要在下一个dispatchDraw周期移除
                // 否则会崩溃，因为viewGroup的childCount没有来得及-1，获取到的view为空
                getViewHolder().getContentWrapper().setVisibility(View.INVISIBLE);
                getViewHolder().getContentWrapper().post(new Runnable() {
                    @Override
                    public void run() {
                        dismiss(false);
                    }
                });
            }
        });
        getViewHolder().getContentWrapper().setVisibility(View.VISIBLE);
    }

    private void fitContainerToActivityContent() {
        final int dh = getViewHolder().getDecor().getHeight();
        final int dw = getViewHolder().getDecor().getWidth();
        final int[] dl = new int[2];
        getViewHolder().getDecor().getLocationOnScreen(dl);
        final int ach = getViewHolder().getActivityContent().getHeight();
        final int acw = getViewHolder().getActivityContent().getWidth();
        final int[] acl = new int[2];
        getViewHolder().getActivityContent().getLocationOnScreen(acl);
        FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) getViewHolder().getChild().getLayoutParams();
        containerParams.leftMargin = 0/*acl[0] - dl[0]*/;
        containerParams.topMargin = 0/*acl[1] - dl[1]*/;
        containerParams.rightMargin = dl[0] + dw - (acl[0] + acw);
        containerParams.bottomMargin = dl[1] + dh - (acl[1] + ach);
        getViewHolder().getChild().setLayoutParams(containerParams);
    }

    protected void initBackground() {
        if (getConfig().mBackgroundBlurPercent > 0 || getConfig().mBackgroundBlurRadius > 0) {
            Utils.getViewSize(getViewHolder().getBackground(), new Runnable() {
                @Override
                public void run() {
                    float radius = getConfig().mBackgroundBlurRadius;
                    if (getConfig().mBackgroundBlurPercent > 0) {
                        int w = getViewHolder().getBackground().getWidth();
                        int h = getViewHolder().getBackground().getHeight();
                        int min = Math.min(w, h);
                        radius = min * getConfig().mBackgroundBlurPercent;
                    }
                    float scale = getConfig().mBackgroundBlurScale;
                    if (radius > 25) {
                        scale = scale * (radius / 25);
                        radius = 25;
                    }
                    Bitmap snapshot = Utils.snapshot(getViewHolder().getDecor(),
                            getViewHolder().getBackground(),
                            scale,
                            getViewHolder().getParent(),
                            getViewHolder().getChild());
                    Blurred.init(getActivity());
                    Bitmap blurBitmap = Blurred.with(snapshot)
                            .recycleOriginal(true)
                            .keepSize(false)
                            .radius(radius)
                            .blur();
                    getViewHolder().getBackground().setScaleType(ImageView.ScaleType.CENTER_CROP);
                    getViewHolder().getBackground().setImageBitmap(blurBitmap);
                    getViewHolder().getBackground().setColorFilter(getConfig().mBackgroundColor);
                }
            });
        } else {
            if (getConfig().mBackgroundBitmap != null) {
                getViewHolder().getBackground().setImageBitmap(getConfig().mBackgroundBitmap);
                if (getConfig().mBackgroundColor != -1) {
                    getViewHolder().getBackground().setColorFilter(getConfig().mBackgroundColor);
                }
            } else if (getConfig().mBackgroundDrawable != null) {
                getViewHolder().getBackground().setImageDrawable(getConfig().mBackgroundDrawable);
                if (getConfig().mBackgroundColor != -1) {
                    getViewHolder().getBackground().setColorFilter(getConfig().mBackgroundColor);
                }
            } else if (getConfig().mBackgroundResource != -1) {
                getViewHolder().getBackground().setImageResource(getConfig().mBackgroundResource);
                if (getConfig().mBackgroundColor != -1) {
                    getViewHolder().getBackground().setColorFilter(getConfig().mBackgroundColor);
                }
            } else if (getConfig().mBackgroundColor != -1) {
                getViewHolder().getBackground().setImageDrawable(new ColorDrawable(getConfig().mBackgroundColor));
            } else if (getConfig().mBackgroundDimAmount != -1) {
                int color = Color.argb((int) (255 * Utils.floatRange01(getConfig().mBackgroundDimAmount)), 0, 0, 0);
                getViewHolder().getBackground().setImageDrawable(new ColorDrawable(color));
            } else {
                getViewHolder().getBackground().setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    protected void initContent() {
        getViewHolder().getContent().setClickable(true);
        ViewGroup.LayoutParams layoutParams = getViewHolder().getContent().getLayoutParams();
        FrameLayout.LayoutParams contentParams;
        if (layoutParams == null) {
            contentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
            contentParams = (FrameLayout.LayoutParams) layoutParams;
        } else {
            contentParams = new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height);
        }
        if (getConfig().mGravity != -1) {
            contentParams.gravity = getConfig().mGravity;
        }
        getViewHolder().getContent().setLayoutParams(contentParams);
        if (getConfig().mAsStatusBarViewId > 0) {
            View statusBar = getViewHolder().getContent().findViewById(getConfig().mAsStatusBarViewId);
            if (statusBar != null) {
                ViewGroup.LayoutParams params = statusBar.getLayoutParams();
                params.height = Utils.getStatusBarHeight(getActivity());
                statusBar.setLayoutParams(params);
                statusBar.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 设置自自定义View
     *
     * @param contentView 自定以View
     */
    public DialogLayer contentView(View contentView) {
        Utils.requireNonNull(contentView, "contentView == null");
        getViewHolder().setContent(contentView);
        return this;
    }

    /**
     * 设置自定义布局文件
     *
     * @param contentViewId 自定义布局ID
     */
    public DialogLayer contentView(int contentViewId) {
        getConfig().mContentViewId = contentViewId;
        return this;
    }

    /**
     * 设置自定义布局文件中状态栏的占位View
     * 该控件高度将设置为状态栏高度，可用来使布局整体下移，避免状态栏遮挡
     *
     * @param statusBarId 状态栏的占位View
     */
    public DialogLayer asStatusBar(int statusBarId) {
        getConfig().mAsStatusBarViewId = statusBarId;
        return this;
    }

    /**
     * 设置避开状态栏
     *
     * @param avoid 设置避开状态栏
     */
    public DialogLayer avoidStatusBar(boolean avoid) {
        getConfig().mAvoidStatusBar = avoid;
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
     * 自定义浮层的拖拽退出的方向
     *
     * @param dragStyle DragLayout.DragStyle
     */
    public DialogLayer dragDismiss(DragLayout.DragStyle dragStyle) {
        getConfig().mDragStyle = dragStyle;
        return this;
    }

    /**
     * 自定义浮层的拖拽退出时的动画
     *
     * @param dragTransformer DragTransformer
     */
    public DialogLayer dragTransformer(DragTransformer dragTransformer) {
        getConfig().mDragTransformer = dragTransformer;
        return this;
    }

    /**
     * 自定义浮层进入和退出动画样式
     *
     * @param animStyle AnimStyle
     */
    public DialogLayer animStyle(AnimStyle animStyle) {
        getConfig().mAnimStyle = animStyle;
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
    public DialogLayer backgroundBlurRadius(float radius) {
        getConfig().mBackgroundBlurRadius = radius;
        return this;
    }

    public DialogLayer backgroundBlurPercent(float percent) {
        getConfig().mBackgroundBlurPercent = percent;
        return this;
    }

    /**
     * 设置背景高斯模糊的缩放比例
     *
     * @param scale 缩放比例
     */
    public DialogLayer backgroundBlurScale(float scale) {
        getConfig().mBackgroundBlurScale = scale;
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param bitmap 图片
     */
    public DialogLayer backgroundBitmap(Bitmap bitmap) {
        Utils.requireNonNull(bitmap, "bitmap == null");
        getConfig().mBackgroundBitmap = bitmap;
        return this;
    }

    /**
     * 设置背景变暗程度
     *
     * @param dimAmount 变暗程度 0~1
     */
    public DialogLayer backgroundDimAmount(float dimAmount) {
        getConfig().mBackgroundDimAmount = Utils.floatRange01(dimAmount);
        return this;
    }

    /**
     * 设置背景变暗
     */
    public DialogLayer backgroundDimDefault() {
        return backgroundDimAmount(BG_DIM_AMOUNT_DEF);
    }

    /**
     * 设置背景资源
     *
     * @param resource 资源ID
     */
    public DialogLayer backgroundResource(int resource) {
        getConfig().mBackgroundResource = resource;
        return this;
    }

    /**
     * 设置背景Drawable
     *
     * @param drawable Drawable
     */
    public DialogLayer backgroundDrawable(Drawable drawable) {
        Utils.requireNonNull(drawable, "drawable == null");
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
    public DialogLayer backgroundColorInt(int colorInt) {
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
    public DialogLayer backgroundColorRes(int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
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
     * @param editTexts 焦点EditTexts
     */
    public DialogLayer compatSoftInput(boolean bottomToContentView, EditText... editTexts) {
        if (mSoftInputHelper == null) {
            mSoftInputHelper = SoftInputHelper.attach(getActivity())
                    .moveWithTranslation()
                    .moveBy(getViewHolder().getContentWrapper());
        }
        if (bottomToContentView) {
            mSoftInputHelper.moveWith(getViewHolder().getContent(), editTexts);
        } else {
            for (EditText editText : editTexts) {
                mSoftInputHelper.moveWith(editText, editText);
            }
        }
        return this;
    }

    public DialogLayer compatSoftInput(EditText... editTexts) {
        return compatSoftInput(true, editTexts);
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

    public DialogLayer outsideTouched(OutsideTouchedListener listener) {
        getConfig().mOutsideTouchedListener = listener;
        return this;
    }

    public DialogLayer outsideTouchedToDismiss(boolean toDismiss) {
        getConfig().mOutsideTouchedToDismiss = toDismiss;
        return this;
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private FrameLayout mActivityContent;
        private BackgroundView mBackground;
        private DragLayout mContentWrapper;
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

        @Override
        public void setChild(View child) {
            super.setChild(child);
            mContentWrapper = getChild().findViewById(R.id.fl_content_wrapper);
            mBackground = getChild().findViewById(R.id.iv_background);
        }

        @Override
        public ContainerLayout getChild() {
            return (ContainerLayout) super.getChild();
        }

        void setContent(View content) {
            mContent = content;
        }

        public View getContent() {
            return mContent;
        }

        public DragLayout getContentWrapper() {
            return mContentWrapper;
        }

        public BackgroundView getBackground() {
            return (BackgroundView) mBackground;
        }
    }

    protected static class Config extends DecorLayer.Config {
        protected boolean mOutsideInterceptTouchEvent = true;
        protected OutsideTouchedListener mOutsideTouchedListener = null;
        protected boolean mOutsideTouchedToDismiss = false;

        protected AnimatorCreator mBackgroundAnimatorCreator = null;
        protected AnimatorCreator mContentAnimatorCreator = null;
        protected AnimStyle mAnimStyle = null;

        protected int mContentViewId = 0;

        protected boolean mCancelableOnTouchOutside = true;

        protected int mAsStatusBarViewId = 0;
        protected boolean mAvoidStatusBar = false;

        protected int mGravity = Gravity.CENTER;
        protected float mBackgroundBlurPercent = 0F;
        protected float mBackgroundBlurRadius = 0F;
        protected float mBackgroundBlurScale = 2F;
        protected Bitmap mBackgroundBitmap = null;
        protected int mBackgroundResource = -1;
        protected Drawable mBackgroundDrawable = null;
        protected float mBackgroundDimAmount = -1;
        protected int mBackgroundColor = -1;

        protected DragLayout.DragStyle mDragStyle = DragLayout.DragStyle.None;
        protected DragTransformer mDragTransformer = null;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
    }

    public interface OutsideTouchedListener {
        void outsideTouched();
    }

    public interface DragTransformer {
        void onDragging(View content, View background, float f);
    }

    public enum AnimStyle {
        ALPHA,
        ZOOM,
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }
}
