package per.goweii.anylayer.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.DecorLayer;
import per.goweii.anylayer.R;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.SoftInputHelper;
import per.goweii.anylayer.utils.Utils;
import per.goweii.anylayer.widget.SwipeLayout;
import per.goweii.visualeffect.blur.RSBlurEffect;
import per.goweii.visualeffect.core.VisualEffect;
import per.goweii.visualeffect.view.BackdropVisualEffectView;

public class DialogLayer extends DecorLayer {
    private final long mAnimDurDef = 220L;
    private final float mDimAmountDef = 0.6F;

    private SoftInputHelper mSoftInputHelper = null;

    public DialogLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public DialogLayer(@NonNull Activity activity) {
        super(activity);
        setCancelableOnKeyBack(true);
    }

    @IntRange(from = 0)
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
    protected ListenerHolder onCreateListenerHolder() {
        return new ListenerHolder();
    }

    @NonNull
    @Override
    public ListenerHolder getListenerHolder() {
        return (ListenerHolder) super.getListenerHolder();
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getViewHolder().getChildNullable() == null) {
            ContainerLayout container = (ContainerLayout) inflater.inflate(R.layout.anylayer_dialog_layer, parent, false);
            getViewHolder().setChild(container);
            getViewHolder().setContent(onCreateContent(inflater, getViewHolder().getContentWrapper()));
            ViewGroup.LayoutParams layoutParams = getViewHolder().getContent().getLayoutParams();
            FrameLayout.LayoutParams contentParams;
            if (layoutParams == null) {
                contentParams = generateContentDefaultLayoutParams();
            } else if (layoutParams instanceof FrameLayout.LayoutParams) {
                contentParams = (FrameLayout.LayoutParams) layoutParams;
            } else {
                contentParams = new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height);
            }
            getViewHolder().getContent().setLayoutParams(contentParams);
            getViewHolder().getContentWrapper().addView(getViewHolder().getContent());
        }
        return getViewHolder().getContainer();
    }

    @NonNull
    protected View onCreateContent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getViewHolder().getContentNullable() == null) {
            getViewHolder().setContent(inflater.inflate(getConfig().mContentViewId, parent, false));
        } else {
            ViewGroup contentParent = (ViewGroup) getViewHolder().getContent().getParent();
            if (contentParent != null) {
                contentParent.removeView(getViewHolder().getContent());
            }
        }
        return getViewHolder().getContent();
    }

    @Nullable
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        Animator backgroundAnimator = onCreateBackgroundInAnimator(getViewHolder().getBackground());
        Animator contentAnimator = onCreateContentInAnimator(getViewHolder().getContent());
        if (backgroundAnimator == null && contentAnimator == null) return null;
        if (backgroundAnimator == null) return contentAnimator;
        if (contentAnimator == null) return backgroundAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    @Nullable
    protected Animator onCreateBackgroundInAnimator(@NonNull View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createInAnimator(view);
        } else {
            backgroundAnimator = onCreateDefBackgroundInAnimator(view);
        }
        return backgroundAnimator;
    }

    @NonNull
    protected Animator onCreateDefBackgroundInAnimator(@NonNull View view) {
        Animator animator = AnimatorHelper.createAlphaInAnim(view);
        animator.setDuration(mAnimDurDef);
        return animator;
    }

    @Nullable
    protected Animator onCreateContentInAnimator(@NonNull View view) {
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
                int swipeDirection = getConfig().mSwipeDirection;
                if ((swipeDirection & SwipeLayout.Direction.LEFT) != 0) {
                    contentAnimator = AnimatorHelper.createLeftInAnim(view);
                } else if ((swipeDirection & SwipeLayout.Direction.TOP) != 0) {
                    contentAnimator = AnimatorHelper.createTopInAnim(view);
                } else if ((swipeDirection & SwipeLayout.Direction.RIGHT) != 0) {
                    contentAnimator = AnimatorHelper.createRightInAnim(view);
                } else if ((swipeDirection & SwipeLayout.Direction.BOTTOM) != 0) {
                    contentAnimator = AnimatorHelper.createBottomInAnim(view);
                } else {
                    contentAnimator = onCreateDefContentInAnimator(view);
                }
            }
            contentAnimator.setDuration(mAnimDurDef);
        }
        return contentAnimator;
    }

    @NonNull
    protected Animator onCreateDefContentInAnimator(@NonNull View view) {
        Animator animator = AnimatorHelper.createZoomAlphaInAnim(view);
        animator.setDuration(mAnimDurDef);
        return animator;
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        Animator backgroundAnimator = onCreateBackgroundOutAnimator(getViewHolder().getBackground());
        Animator contentAnimator = onCreateContentOutAnimator(getViewHolder().getContent());
        if (backgroundAnimator == null && contentAnimator == null) return null;
        if (backgroundAnimator == null) return contentAnimator;
        if (contentAnimator == null) return backgroundAnimator;
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    @Nullable
    protected Animator onCreateBackgroundOutAnimator(@NonNull View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createOutAnimator(view);
        } else {
            backgroundAnimator = onCreateDefBackgroundOutAnimator(view);
        }
        return backgroundAnimator;
    }

    @NonNull
    protected Animator onCreateDefBackgroundOutAnimator(@NonNull View view) {
        Animator animator = AnimatorHelper.createAlphaOutAnim(view);
        animator.setDuration(mAnimDurDef);
        return animator;
    }

    @Nullable
    protected Animator onCreateContentOutAnimator(@NonNull View view) {
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
                int swipeDirection = getConfig().mSwipeDirection;
                if ((swipeDirection & SwipeLayout.Direction.LEFT) != 0) {
                    contentAnimator = AnimatorHelper.createLeftOutAnim(view);
                } else if ((swipeDirection & SwipeLayout.Direction.TOP) != 0) {
                    contentAnimator = AnimatorHelper.createTopOutAnim(view);
                } else if ((swipeDirection & SwipeLayout.Direction.RIGHT) != 0) {
                    contentAnimator = AnimatorHelper.createRightOutAnim(view);
                } else if ((swipeDirection & SwipeLayout.Direction.BOTTOM) != 0) {
                    contentAnimator = AnimatorHelper.createBottomOutAnim(view);
                } else {
                    contentAnimator = onCreateDefContentOutAnimator(view);
                }
            }
            contentAnimator.setDuration(mAnimDurDef);
        }
        return contentAnimator;
    }

    @NonNull
    protected Animator onCreateDefContentOutAnimator(@NonNull View view) {
        Animator animator = AnimatorHelper.createZoomAlphaOutAnim(view);
        animator.setDuration(mAnimDurDef);
        return animator;
    }

    @NonNull
    protected FrameLayout.LayoutParams generateContentDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @CallSuper
    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @CallSuper
    @Override
    protected void onAttach() {
        onInitContent();
        onInitBackground();
        onInitContainer();
        super.onAttach();
        registerSoftInputCompat();
    }

    @CallSuper
    @Override
    protected void onPreShow() {
        super.onPreShow();
    }

    @CallSuper
    @Override
    protected void onPostShow() {
        super.onPostShow();
    }

    @CallSuper
    @Override
    protected void onPreDismiss() {
        super.onPreDismiss();
    }

    @CallSuper
    @Override
    protected void onPostDismiss() {
        super.onPostDismiss();
    }

    @CallSuper
    @Override
    protected void onDetach() {
        super.onDetach();
        unregisterSoftInputCompat();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void fitDecorInsides() {
        fitDecorInsidesToViewPadding(getViewHolder().getContentWrapper());
        if (getConfig().mAvoidStatusBar) {
            int paddingTop = getViewHolder().getContentWrapper().getPaddingTop();
            int statusBarHeight = Utils.getStatusBarHeight(getActivity());
            Utils.setViewPaddingTop(getViewHolder().getContentWrapper(), Math.max(paddingTop, statusBarHeight));
        }
        getViewHolder().getContentWrapper().setClipToPadding(false);
        getViewHolder().getContentWrapper().setClipChildren(false);
    }

    protected void onInitContainer() {
        if (getConfig().mOutsideInterceptTouchEvent) {
            getViewHolder().getContainer().setHandleTouchEvent(true);
            if (getConfig().mCancelableOnTouchOutside) {
                getViewHolder().getContainer().setOnTappedListener(new ContainerLayout.OnTappedListener() {
                    @Override
                    public void onTapped() {
                        dismiss();
                    }
                });
            }
        } else {
            getViewHolder().getContainer().setOnTappedListener(null);
            getViewHolder().getContainer().setHandleTouchEvent(false);
        }
        if (getConfig().mOutsideTouchedToDismiss || getConfig().mOnOutsideTouchListener != null) {
            getViewHolder().getContainer().setOnTouchedListener(new ContainerLayout.OnTouchedListener() {
                @Override
                public void onTouched() {
                    if (getConfig().mOutsideTouchedToDismiss) {
                        dismiss();
                    }
                    if (getConfig().mOnOutsideTouchListener != null) {
                        getConfig().mOnOutsideTouchListener.outsideTouched();
                    }
                }
            });
        }
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        contentWrapperParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        contentWrapperParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
        getViewHolder().getContentWrapper().setSwipeDirection(getConfig().mSwipeDirection);
        getViewHolder().getContentWrapper().setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onStart(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                if (getConfig().mSwipeTransformer == null) {
                    getConfig().mSwipeTransformer = new SwipeTransformer() {
                        @Override
                        public void onSwiping(@NonNull DialogLayer layer, @SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                            layer.getViewHolder().getBackground().setAlpha(1F - fraction);
                        }
                    };
                }
                getListenerHolder().notifyOnSwipeStart(DialogLayer.this);
            }

            @Override
            public void onSwiping(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                if (getConfig().mSwipeTransformer != null) {
                    getConfig().mSwipeTransformer.onSwiping(DialogLayer.this, direction, fraction);
                }
                getListenerHolder().notifyOnSwiping(DialogLayer.this, direction, fraction);
            }

            @Override
            public void onEnd(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                if (fraction == 1F) {
                    getListenerHolder().notifyOnSwipeEnd(DialogLayer.this, direction);
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
            }
        });
        getViewHolder().getContentWrapper().setVisibility(View.VISIBLE);
    }

    protected void onInitBackground() {
        if (getConfig().mBackgroundBlurPercent > 0) {
            final BackdropVisualEffectView backdropVisualEffectView = getViewHolder().replaceBackgroundToVisualEffectView();
            backdropVisualEffectView.setOverlayColor(getConfig().mBackgroundColor);
            Utils.onViewLayout(backdropVisualEffectView, new Runnable() {
                @Override
                public void run() {
                    int w = backdropVisualEffectView.getWidth();
                    int h = backdropVisualEffectView.getHeight();
                    float radius = Math.min(w, h) * getConfig().mBackgroundBlurPercent;
                    float simple = getConfig().mBackgroundBlurSimple;
                    if (radius > 25) {
                        simple = simple * (radius / 25);
                        radius = 25;
                    }
                    backdropVisualEffectView.setSimpleSize(simple);
                    VisualEffect visualEffect = new RSBlurEffect(getActivity(), radius);
                    backdropVisualEffectView.setVisualEffect(visualEffect);
                }
            });
        } else if (getConfig().mBackgroundBlurRadius > 0) {
            BackdropVisualEffectView backdropVisualEffectView = getViewHolder().replaceBackgroundToVisualEffectView();
            backdropVisualEffectView.setOverlayColor(getConfig().mBackgroundColor);
            float radius = getConfig().mBackgroundBlurRadius;
            float simple = getConfig().mBackgroundBlurSimple;
            if (radius > 25) {
                simple = simple * (radius / 25);
                radius = 25;
            }
            backdropVisualEffectView.setSimpleSize(simple);
            VisualEffect visualEffect = new RSBlurEffect(getActivity(), radius);
            backdropVisualEffectView.setVisualEffect(visualEffect);
        } else {
            ImageView backgroundView = (ImageView) getViewHolder().getBackground();
            if (getConfig().mBackgroundBitmap != null) {
                backgroundView.setImageBitmap(getConfig().mBackgroundBitmap);
                if (getConfig().mBackgroundColor != Color.TRANSPARENT) {
                    backgroundView.setColorFilter(getConfig().mBackgroundColor);
                }
            } else if (getConfig().mBackgroundDrawable != null) {
                backgroundView.setImageDrawable(getConfig().mBackgroundDrawable);
                if (getConfig().mBackgroundColor != Color.TRANSPARENT) {
                    backgroundView.setColorFilter(getConfig().mBackgroundColor);
                }
            } else if (getConfig().mBackgroundResource != -1) {
                backgroundView.setImageResource(getConfig().mBackgroundResource);
                if (getConfig().mBackgroundColor != Color.TRANSPARENT) {
                    backgroundView.setColorFilter(getConfig().mBackgroundColor);
                }
            } else if (getConfig().mBackgroundColor != Color.TRANSPARENT) {
                backgroundView.setImageDrawable(new ColorDrawable(getConfig().mBackgroundColor));
            } else if (getConfig().mBackgroundDimAmount != -1) {
                int color = Color.argb((int) (255 * Utils.floatRange01(getConfig().mBackgroundDimAmount)), 0, 0, 0);
                backgroundView.setImageDrawable(new ColorDrawable(color));
            } else {
                backgroundView.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    protected void onInitContent() {
        getViewHolder().getContent().setClickable(true);
        if (getConfig().mContentBlurPercent > 0) {
            final BackdropVisualEffectView backdropVisualEffectView = getViewHolder()
                    .replaceContentToBackdropVisualEffectView(getConfig().mContentBlurCornerRadius);
            backdropVisualEffectView.setOverlayColor(getConfig().mContentBlurColor);
            Utils.onViewLayout(backdropVisualEffectView, new Runnable() {
                @Override
                public void run() {
                    int w = backdropVisualEffectView.getWidth();
                    int h = backdropVisualEffectView.getHeight();
                    float radius = Math.min(w, h) * getConfig().mContentBlurPercent;
                    float simple = getConfig().mContentBlurSimple;
                    if (radius > 25) {
                        simple = simple * (radius / 25);
                        radius = 25;
                    }
                    backdropVisualEffectView.setSimpleSize(simple);
                    VisualEffect visualEffect = new RSBlurEffect(getActivity(), radius);
                    backdropVisualEffectView.setVisualEffect(visualEffect);
                }
            });
        } else if (getConfig().mContentBlurRadius > 0) {
            BackdropVisualEffectView backdropVisualEffectView = getViewHolder()
                    .replaceContentToBackdropVisualEffectView(getConfig().mContentBlurCornerRadius);
            backdropVisualEffectView.setOverlayColor(getConfig().mContentBlurColor);
            float radius = getConfig().mContentBlurRadius;
            float simple = getConfig().mContentBlurSimple;
            if (radius > 25) {
                simple = simple * (radius / 25);
                radius = 25;
            }
            backdropVisualEffectView.setSimpleSize(simple);
            VisualEffect visualEffect = new RSBlurEffect(getActivity(), radius);
            backdropVisualEffectView.setVisualEffect(visualEffect);
        }
        FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) getViewHolder().getContent().getLayoutParams();
        if (getConfig().mGravity != -1) {
            contentParams.gravity = getConfig().mGravity;
        }
        getViewHolder().getContent().setLayoutParams(contentParams);
    }

    private void registerSoftInputCompat() {
        final SparseBooleanArray mapping = getConfig().mSoftInputMapping;
        if (mapping == null || mapping.size() == 0) {
            return;
        }
        if (mSoftInputHelper == null) {
            mSoftInputHelper = SoftInputHelper.attach(getActivity());
        } else {
            mSoftInputHelper.clear();
        }
        mSoftInputHelper.setListener(new SoftInputHelper.OnSoftInputListener() {
            @Override
            public void onOpen(int height) {
                getListenerHolder().notifyOnSoftInputOpen(DialogLayer.this, height);
            }

            @Override
            public void onClose(int height) {
                getListenerHolder().notifyOnSoftInputClose(DialogLayer.this, height);
            }

            @Override
            public void onHeightChange(int height) {
                getListenerHolder().notifyOnSoftInputHeightChange(DialogLayer.this, height);
            }
        });
        mSoftInputHelper.setMoveView(getViewHolder().getContentWrapper());
        for (int i = 0; i < mapping.size(); i++) {
            boolean alignToContentOrFocus = mapping.valueAt(i);
            int focusId = mapping.keyAt(i);
            if (focusId == View.NO_ID) {
                if (alignToContentOrFocus) {
                    mSoftInputHelper.setFollowViews(getViewHolder().getContent());
                }
            } else {
                if (alignToContentOrFocus) {
                    mSoftInputHelper.setFollowViews(getViewHolder().getContent(), findView(focusId));
                } else {
                    mSoftInputHelper.setFollowViews(null, findView(focusId));
                }
            }
        }
    }

    private void unregisterSoftInputCompat() {
        if (mSoftInputHelper != null) {
            mSoftInputHelper.setListener(null);
            mSoftInputHelper.clear();
            mSoftInputHelper.detach();
            mSoftInputHelper = null;
        }
    }

    /**
     * 设置自自定义View
     *
     * @param contentView 自定以View
     */
    @NonNull
    public DialogLayer setContentView(@NonNull View contentView) {
        getViewHolder().setContent(contentView);
        return this;
    }

    /**
     * 设置自定义布局文件
     *
     * @param contentViewId 自定义布局ID
     */
    @NonNull
    public DialogLayer setContentView(@LayoutRes int contentViewId) {
        getConfig().mContentViewId = contentViewId;
        return this;
    }

    /**
     * 设置避开状态栏
     *
     * @param avoid 设置避开状态栏
     */
    @NonNull
    public DialogLayer setAvoidStatusBar(boolean avoid) {
        getConfig().mAvoidStatusBar = avoid;
        return this;
    }

    /**
     * 设置子布局的gravity
     * 可直接在布局文件指定layout_gravity属性，作用相同
     *
     * @param gravity {@link Gravity}
     */
    @NonNull
    public DialogLayer setGravity(int gravity) {
        getConfig().mGravity = gravity;
        return this;
    }

    /**
     * 自定义浮层的拖拽退出的方向
     *
     * @param swipeDirection {@link SwipeLayout.Direction}
     */
    @NonNull
    public DialogLayer setSwipeDismiss(int swipeDirection) {
        getConfig().mSwipeDirection = swipeDirection;
        return this;
    }

    /**
     * 自定义浮层的拖拽退出时的动画
     *
     * @param swipeTransformer SwipeTransformer
     */
    @NonNull
    public DialogLayer setSwipeTransformer(@Nullable SwipeTransformer swipeTransformer) {
        getConfig().mSwipeTransformer = swipeTransformer;
        return this;
    }

    /**
     * 浮层拖拽事件监听
     *
     * @param swipeListener OnSwipeListener
     */
    @NonNull
    public DialogLayer addOnSwipeListener(@NonNull OnSwipeListener swipeListener) {
        getListenerHolder().addOnSwipeListener(swipeListener);
        return this;
    }

    /**
     * 自定义浮层进入和退出动画样式
     *
     * @param animStyle AnimStyle
     */
    @NonNull
    public DialogLayer setAnimStyle(@Nullable AnimStyle animStyle) {
        getConfig().mAnimStyle = animStyle;
        return this;
    }

    /**
     * 自定义浮层的进入和退出动画
     * 可使用工具类{@link AnimatorHelper}
     *
     * @param contentAnimatorCreator AnimatorCreator
     */
    @NonNull
    public DialogLayer setContentAnimator(@Nullable AnimatorCreator contentAnimatorCreator) {
        getConfig().mContentAnimatorCreator = contentAnimatorCreator;
        return this;
    }

    /**
     * 自定义背景的进入和退出动画
     * 可使用工具类{@link AnimatorHelper}
     *
     * @param backgroundAnimatorCreator AnimatorCreator
     */
    @NonNull
    public DialogLayer setBackgroundAnimator(@Nullable AnimatorCreator backgroundAnimatorCreator) {
        getConfig().mBackgroundAnimatorCreator = backgroundAnimatorCreator;
        return this;
    }

    @NonNull
    public DialogLayer setContentBlurRadius(@FloatRange(from = 0F) float radius) {
        getConfig().mContentBlurRadius = radius;
        return this;
    }

    @NonNull
    public DialogLayer setContentBlurPercent(@FloatRange(from = 0F) float percent) {
        getConfig().mContentBlurPercent = percent;
        return this;
    }

    @NonNull
    public DialogLayer setContentBlurSimple(@FloatRange(from = 1F) float simple) {
        getConfig().mContentBlurSimple = simple;
        return this;
    }

    @NonNull
    public DialogLayer setContentBlurColorInt(@ColorInt int colorInt) {
        getConfig().mContentBlurColor = colorInt;
        return this;
    }

    @NonNull
    public DialogLayer setContentBlurColorRes(@ColorRes int colorRes) {
        getConfig().mContentBlurColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @NonNull
    public DialogLayer setContentBlurCornerRadius(float radius, int unit) {
        getConfig().mContentBlurCornerRadius = TypedValue.applyDimension(
                unit, radius, getActivity().getResources().getDisplayMetrics()
        );
        return this;
    }

    @NonNull
    public DialogLayer setContentBlurCornerRadiusDp(float radius) {
        getConfig().mContentBlurCornerRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, radius, getActivity().getResources().getDisplayMetrics()
        );
        return this;
    }

    @NonNull
    public DialogLayer setContentBlurCornerRadiusPx(float radius) {
        getConfig().mContentBlurCornerRadius = radius;
        return this;
    }

    /**
     * 设置背景为当前activity的高斯模糊效果
     * 设置之后其他背景设置方法失效，仅{@link #setBackgroundColorInt(int)}生效
     * 且设置的backgroundColor值调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时的{@link #setBackgroundColorInt(int)}传入的为半透明颜色
     *
     * @param radius 模糊半径
     */
    @NonNull
    public DialogLayer setBackgroundBlurRadius(@FloatRange(from = 0F) float radius) {
        getConfig().mBackgroundBlurRadius = radius;
        return this;
    }

    @NonNull
    public DialogLayer setBackgroundBlurPercent(@FloatRange(from = 0F) float percent) {
        getConfig().mBackgroundBlurPercent = percent;
        return this;
    }

    /**
     * 设置背景高斯模糊的采样比例
     *
     * @param simple 采样比例
     */
    @NonNull
    public DialogLayer setBackgroundBlurSimple(@FloatRange(from = 1F) float simple) {
        getConfig().mBackgroundBlurSimple = simple;
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param bitmap 图片
     */
    @NonNull
    public DialogLayer setBackgroundBitmap(@Nullable Bitmap bitmap) {
        getConfig().mBackgroundBitmap = bitmap;
        return this;
    }

    /**
     * 设置背景变暗程度
     *
     * @param dimAmount 变暗程度 0~1
     */
    @NonNull
    public DialogLayer setBackgroundDimAmount(@FloatRange(from = 0F, to = 1F) float dimAmount) {
        getConfig().mBackgroundDimAmount = Utils.floatRange01(dimAmount);
        return this;
    }

    /**
     * 设置背景变暗
     */
    @NonNull
    public DialogLayer setBackgroundDimDefault() {
        return setBackgroundDimAmount(mDimAmountDef);
    }

    /**
     * 设置背景资源
     *
     * @param resource 资源ID
     */
    @NonNull
    public DialogLayer setBackgroundResource(@DrawableRes int resource) {
        getConfig().mBackgroundResource = resource;
        return this;
    }

    /**
     * 设置背景Drawable
     *
     * @param drawable Drawable
     */
    @NonNull
    public DialogLayer setBackgroundDrawable(@Nullable Drawable drawable) {
        getConfig().mBackgroundDrawable = drawable;
        return this;
    }

    /**
     * 设置背景颜色
     * 在调用了{@link #setBackgroundBitmap(Bitmap)}或者{@link #setBackgroundBlurRadius(float)}方法后
     * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时传入的颜色为半透明颜色
     *
     * @param colorInt 颜色值
     */
    @NonNull
    public DialogLayer setBackgroundColorInt(@ColorInt int colorInt) {
        getConfig().mBackgroundColor = colorInt;
        return this;
    }

    /**
     * 设置背景颜色
     * 在调用了{@link #setBackgroundBitmap(Bitmap)}或者{@link #setBackgroundBlurRadius(float)}方法后
     * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时传入的颜色为半透明颜色
     *
     * @param colorRes 颜色资源ID
     */
    @NonNull
    public DialogLayer setBackgroundColorRes(@ColorRes int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    /**
     * 设置点击浮层以外区域是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    @NonNull
    public DialogLayer setCancelableOnTouchOutside(boolean cancelable) {
        getConfig().mCancelableOnTouchOutside = cancelable;
        return this;
    }

    /**
     * 设置点击返回键是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    @NonNull
    @Override
    public DialogLayer setCancelableOnClickKeyBack(boolean cancelable) {
        return (DialogLayer) super.setCancelableOnClickKeyBack(cancelable);
    }

    /**
     * 适配软键盘的弹出，布局自动上移
     * 在某几个View获取焦点时布局上移
     *
     * @param alignToContentOrFocus true为对齐到contentView，false为对齐到focusView自身
     * @param focusIds              焦点View
     */
    @NonNull
    public DialogLayer addSoftInputCompat(boolean alignToContentOrFocus, @Nullable int... focusIds) {
        if (getConfig().mSoftInputMapping == null) {
            getConfig().mSoftInputMapping = new SparseBooleanArray(1);
        }
        if (focusIds != null && focusIds.length > 0) {
            for (int focusId : focusIds) {
                getConfig().mSoftInputMapping.append(focusId, alignToContentOrFocus);
            }
        } else {
            getConfig().mSoftInputMapping.append(View.NO_ID, alignToContentOrFocus);
        }
        return this;
    }

    public DialogLayer addOnSoftInputListener(@NonNull OnSoftInputListener onSoftInputListener) {
        getListenerHolder().addOnSoftInputListener(onSoftInputListener);
        return this;
    }

    /**
     * 设置浮层外部是否拦截触摸
     * 默认为true，false则事件有activityContent本身消费
     *
     * @param intercept 外部是否拦截触摸
     */
    @NonNull
    public DialogLayer setOutsideInterceptTouchEvent(boolean intercept) {
        getConfig().mOutsideInterceptTouchEvent = intercept;
        return this;
    }

    @NonNull
    public DialogLayer setOnOutsideTouchListener(OnOutsideTouchListener listener) {
        getConfig().mOnOutsideTouchListener = listener;
        return this;
    }

    @NonNull
    public DialogLayer setOutsideTouchToDismiss(boolean toDismiss) {
        getConfig().mOutsideTouchedToDismiss = toDismiss;
        return this;
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private View mBackground;
        private SwipeLayout mContentWrapper;
        private View mContent;

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
            mContentWrapper = getChild().findViewById(R.id.anylayler_dialog_content_wrapper);
            mBackground = getChild().findViewById(R.id.anylayler_dialog_background);
        }

        @NonNull
        public ContainerLayout getContainer() {
            return getChild();
        }

        @NonNull
        @Override
        public ContainerLayout getChild() {
            return (ContainerLayout) super.getChild();
        }

        @Nullable
        @Override
        protected ContainerLayout getChildNullable() {
            return (ContainerLayout) super.getChildNullable();
        }

        protected void setContent(@NonNull View content) {
            mContent = content;
        }

        @Nullable
        protected View getContentNullable() {
            return mContent;
        }

        @NonNull
        public View getContent() {
            Utils.requireNonNull(mContent, "必须在show方法后调用");
            return mContent;
        }

        public BackdropVisualEffectView replaceContentToBackdropVisualEffectView(float cornerRadius) {
            if (mContent instanceof CardView && mContent.getId() == R.id.anylayer_dialog_content) {
                return (BackdropVisualEffectView) mContent.findViewById(R.id.anylayer_dialog_content_effect);
            }
            final ViewGroup contentWrapper = mContentWrapper;
            final View realContent = mContent;
            final Context context = contentWrapper.getContext();
            if (realContent.getId() == View.NO_ID) {
                realContent.setId(R.id.anylayer_dialog_content_really);
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) realContent.getLayoutParams();

            int contentIndex = contentWrapper.indexOfChild(realContent);
            contentWrapper.removeViewAt(contentIndex);

            BackdropVisualEffectView backdropVisualEffectView = new BackdropVisualEffectView(context);
            backdropVisualEffectView.setId(R.id.anylayer_dialog_content_effect);
            backdropVisualEffectView.setShowDebugInfo(false);

            CardView cardView = new CardView(context);
            cardView.setId(R.id.anylayer_dialog_content);
            cardView.setCardBackgroundColor(Color.TRANSPARENT);
            cardView.setMaxCardElevation(0);
            cardView.setCardElevation(0);
            cardView.setRadius(cornerRadius);

            RelativeLayout relativeLayout = new RelativeLayout(context);

            RelativeLayout.LayoutParams effectViewLP = new RelativeLayout.LayoutParams(0, 0);
            effectViewLP.addRule(RelativeLayout.ALIGN_TOP, realContent.getId());
            effectViewLP.addRule(RelativeLayout.ALIGN_BOTTOM, realContent.getId());
            effectViewLP.addRule(RelativeLayout.ALIGN_LEFT, realContent.getId());
            effectViewLP.addRule(RelativeLayout.ALIGN_RIGHT, realContent.getId());
            relativeLayout.addView(backdropVisualEffectView, effectViewLP);

            RelativeLayout.LayoutParams contentViewLP = new RelativeLayout.LayoutParams(
                    layoutParams.width, layoutParams.height
            );
            relativeLayout.addView(realContent, contentViewLP);

            cardView.addView(relativeLayout, new CardView.LayoutParams(buildWrapInnerLayoutParams(layoutParams)));

            FrameLayout.LayoutParams cardLP = new FrameLayout.LayoutParams(buildWrapInnerLayoutParams(layoutParams));
            cardLP.leftMargin = layoutParams.leftMargin;
            cardLP.topMargin = layoutParams.topMargin;
            cardLP.rightMargin = layoutParams.rightMargin;
            cardLP.bottomMargin = layoutParams.bottomMargin;
            cardLP.gravity = layoutParams.gravity;
            contentWrapper.addView(cardView, contentIndex, cardLP);

            mContent = cardView;
            return backdropVisualEffectView;
        }

        @NonNull
        private ViewGroup.LayoutParams buildWrapInnerLayoutParams(@NonNull ViewGroup.LayoutParams layoutParams) {
            final int lpw, lph;
            if (layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                lpw = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lpw = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                lph = ViewGroup.LayoutParams.MATCH_PARENT;
            } else {
                lph = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            return new ViewGroup.LayoutParams(lpw, lph);
        }

        @NonNull
        public SwipeLayout getContentWrapper() {
            return mContentWrapper;
        }

        @NonNull
        public View getBackground() {
            return mBackground;
        }

        public BackdropVisualEffectView replaceBackgroundToVisualEffectView() {
            if (mBackground instanceof BackdropVisualEffectView) {
                return (BackdropVisualEffectView) mBackground;
            }
            ViewGroup.LayoutParams layoutParams = mBackground.getLayoutParams();
            int index = getChild().indexOfChild(mBackground);
            getChild().removeViewAt(index);
            BackdropVisualEffectView backdropVisualEffectView = new BackdropVisualEffectView(getChild().getContext());
            backdropVisualEffectView.setShowDebugInfo(false);
            backdropVisualEffectView.setId(R.id.anylayler_dialog_background);
            getChild().addView(backdropVisualEffectView, index, new ViewGroup.LayoutParams(layoutParams));
            mBackground = backdropVisualEffectView;
            return backdropVisualEffectView;
        }
    }

    protected static class Config extends DecorLayer.Config {
        protected boolean mOutsideInterceptTouchEvent = true;
        @Nullable
        protected OnOutsideTouchListener mOnOutsideTouchListener = null;
        protected boolean mOutsideTouchedToDismiss = false;

        @Nullable
        protected AnimatorCreator mBackgroundAnimatorCreator = null;
        @Nullable
        protected AnimatorCreator mContentAnimatorCreator = null;
        @Nullable
        protected AnimStyle mAnimStyle = null;

        protected int mContentViewId = -1;

        protected boolean mCancelableOnTouchOutside = true;

        protected boolean mAvoidStatusBar = false;

        protected int mGravity = Gravity.CENTER;
        protected float mBackgroundBlurPercent = 0F;
        protected float mBackgroundBlurRadius = 0F;
        protected float mBackgroundBlurSimple = 4F;
        @Nullable
        protected Bitmap mBackgroundBitmap = null;
        protected int mBackgroundResource = -1;
        @Nullable
        protected Drawable mBackgroundDrawable = null;
        protected float mBackgroundDimAmount = -1;
        @ColorInt
        protected int mBackgroundColor = Color.TRANSPARENT;

        protected float mContentBlurPercent = 0F;
        protected float mContentBlurRadius = 0F;
        protected float mContentBlurSimple = 4F;
        @ColorInt
        protected int mContentBlurColor = Color.TRANSPARENT;
        protected float mContentBlurCornerRadius = 0F;

        @SwipeLayout.Direction
        protected int mSwipeDirection = 0;
        @Nullable
        protected SwipeTransformer mSwipeTransformer = null;

        protected SparseBooleanArray mSoftInputMapping = null;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
        private List<OnSwipeListener> mOnSwipeListeners = null;
        private List<OnSoftInputListener> mOnSoftInputListeners = null;

        private void addOnSwipeListener(@NonNull OnSwipeListener onSwipeListener) {
            if (mOnSwipeListeners == null) {
                mOnSwipeListeners = new ArrayList<>(1);
            }
            mOnSwipeListeners.add(onSwipeListener);
        }

        private void addOnSoftInputListener(@NonNull OnSoftInputListener onSoftInputListener) {
            if (mOnSoftInputListeners == null) {
                mOnSoftInputListeners = new ArrayList<>(1);
            }
            mOnSoftInputListeners.add(onSoftInputListener);
        }

        private void notifyOnSwipeStart(@NonNull DialogLayer layer) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onStart(layer);
                }
            }
        }

        private void notifyOnSwiping(@NonNull DialogLayer layer,
                                     @SwipeLayout.Direction int direction,
                                     @FloatRange(from = 0F, to = 1F) float fraction) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onSwiping(layer, direction, fraction);
                }
            }
        }

        private void notifyOnSwipeEnd(@NonNull DialogLayer layer,
                                      @SwipeLayout.Direction int direction) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onEnd(layer, direction);
                }
            }
        }

        private void notifyOnSoftInputOpen(@NonNull DialogLayer layer, @Px int height) {
            if (mOnSoftInputListeners != null) {
                for (OnSoftInputListener onSoftInputListener : mOnSoftInputListeners) {
                    onSoftInputListener.onOpen(layer, height);
                }
            }
        }

        private void notifyOnSoftInputClose(@NonNull DialogLayer layer, @Px int height) {
            if (mOnSoftInputListeners != null) {
                for (OnSoftInputListener onSoftInputListener : mOnSoftInputListeners) {
                    onSoftInputListener.onClose(layer, height);
                }
            }
        }

        private void notifyOnSoftInputHeightChange(@NonNull DialogLayer layer, @Px int height) {
            if (mOnSoftInputListeners != null) {
                for (OnSoftInputListener onSoftInputListener : mOnSoftInputListeners) {
                    onSoftInputListener.onHeightChange(layer, height);
                }
            }
        }
    }

    public interface OnOutsideTouchListener {
        void outsideTouched();
    }

    public interface SwipeTransformer {
        void onSwiping(@NonNull DialogLayer layer,
                       @SwipeLayout.Direction int direction,
                       @FloatRange(from = 0F, to = 1F) float fraction);
    }

    public interface OnSwipeListener {
        void onStart(@NonNull DialogLayer layer);

        void onSwiping(@NonNull DialogLayer layer,
                       @SwipeLayout.Direction int direction,
                       @FloatRange(from = 0F, to = 1F) float fraction);

        void onEnd(@NonNull DialogLayer layer,
                   @SwipeLayout.Direction int direction);
    }

    public interface OnSoftInputListener {
        void onOpen(@NonNull DialogLayer layer, @Px int height);

        void onClose(@NonNull DialogLayer layer, @Px int height);

        void onHeightChange(@NonNull DialogLayer layer, @Px int height);
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
