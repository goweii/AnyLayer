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

/**
 * @author Cuizhen
 */
public class AnyLayer implements LayerManager.LifeListener {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private View mTargetView;
    private ViewGroup mRootView;
    private View mContent;
    private ViewHolder mViewHolder;
    private LayerManager mLayerManager;

    private int mGravity = Gravity.CENTER;
    private float mBackgroundBlurRadius = 0;
    private float mBackgroundBlurScale = 0;
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

    private boolean mInsideAlignment = false;
    private Alignment.Direction mAlignmentDirection = Alignment.Direction.VERTICAL;
    private Alignment.Horizontal mAlignmentHorizontal = Alignment.Horizontal.CENTER;
    private Alignment.Vertical mAlignmentVertical = Alignment.Vertical.BELOW;

    private SoftInputHelper mSoftInputHelper = null;

    public static AnyLayer with(@NonNull Context context) {
        return new AnyLayer(context);
    }

    public static AnyLayer with(@NonNull ViewGroup viewGroup) {
        return new AnyLayer(viewGroup);
    }

    public static AnyLayer target(@NonNull View targetView) {
        return new AnyLayer(targetView);
    }

    private AnyLayer(@NonNull Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initRootView();
        initView();
    }

    private AnyLayer(@NonNull ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        mInflater = LayoutInflater.from(mContext);
        mRootView = viewGroup;
        initView();
    }

    private AnyLayer(@NonNull View targetView) {
        mContext = targetView.getContext();
        mInflater = LayoutInflater.from(mContext);
        mTargetView = targetView;
        initRootView();
        initView();
    }

    private void initRootView() {
        Activity activity = Utils.getActivity(mContext);
        if (activity == null) {
            throw new NullPointerException();
        }
        mRootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    private void initView() {
        FrameLayout container = (FrameLayout) mInflater.inflate(R.layout.layout_any_layer, mRootView, false);
        mViewHolder = new ViewHolder(this, container);
        mLayerManager = new LayerManager(mRootView, container);
        mLayerManager.setLifeListener(this);
    }

    public void show() {
        mLayerManager.add();
    }

    public void dismiss() {
        mLayerManager.remove();
    }

    public <V extends View> V getView(@IdRes int viewId) {
        return mViewHolder.getView(viewId);
    }

    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    public View getContentView() {
        return mContent;
    }

    public ImageView getBackground() {
        return mViewHolder.getBackground();
    }

    public AnyLayer contentView(@NonNull View contentView) {
        mContent = contentView;
        return this;
    }

    public AnyLayer contentView(@LayoutRes int contentViewId) {
        mContent = mInflater.inflate(contentViewId, mViewHolder.getContainer(), false);
        return this;
    }

    public AnyLayer bindData(IDataBinder dataBinder) {
        mDataBinder = dataBinder;
        return this;
    }

    public AnyLayer onVisibleChangeListener(OnVisibleChangeListener mOnVisibleChangeListener) {
        this.mOnVisibleChangeListener = mOnVisibleChangeListener;
        return this;
    }

    public AnyLayer onLayerShowListener(OnLayerShowListener onLayerShowListener) {
        mOnLayerShowListener = onLayerShowListener;
        return this;
    }

    public AnyLayer onLayerDismissListener(OnLayerDismissListener onLayerDismissListener) {
        mOnLayerDismissListener = onLayerDismissListener;
        return this;
    }

    public AnyLayer gravity(int gravity) {
        mGravity = gravity;
        return this;
    }

    public AnyLayer alignment(@NonNull Alignment.Direction direction,
                              @NonNull Alignment.Horizontal horizontal,
                              @NonNull Alignment.Vertical vertical,
                              boolean inside) {
        mAlignmentDirection = direction;
        mAlignmentHorizontal = horizontal;
        mAlignmentVertical = vertical;
        mInsideAlignment = inside;
        return this;
    }

    public AnyLayer contentAnim(IAnim contentAnim) {
        this.mContentAnim = contentAnim;
        return this;
    }

    public AnyLayer contentInAnim(@AnimRes int anim) {
        contentInAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    public AnyLayer contentInAnim(@NonNull Animation anim) {
        mContentInAnim = anim;
        mContentInAnimDuration = mContentInAnim.getDuration();
        return this;
    }

    public AnyLayer contentOutAnim(@AnimRes int anim) {
        contentOutAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    public AnyLayer contentOutAnim(@NonNull Animation anim) {
        mContentOutAnim = anim;
        mContentOutAnimDuration = mContentOutAnim.getDuration();
        return this;
    }

    public AnyLayer backgroundAnim(IAnim backgroundAnim) {
        this.mBackgroundAnim = backgroundAnim;
        return this;
    }

    public AnyLayer backgroundInAnim(@AnimRes int anim) {
        backgroundInAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    public AnyLayer backgroundInAnim(@NonNull Animation anim) {
        mBackgroundInAnim = anim;
        mBackgroundInAnimDuration = mBackgroundInAnim.getDuration();
        return this;
    }

    public AnyLayer backgroundOutAnim(@AnimRes int anim) {
        backgroundOutAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    public AnyLayer backgroundOutAnim(@NonNull Animation anim) {
        mBackgroundOutAnim = anim;
        mBackgroundOutAnimDuration = mBackgroundOutAnim.getDuration();
        return this;
    }

    public AnyLayer defaultContentAnimDuration(long defaultAnimDuration) {
        this.mContentInAnimDuration = defaultAnimDuration;
        this.mContentOutAnimDuration = defaultAnimDuration;
        return this;
    }

    public AnyLayer defaultContentInAnimDuration(long defaultInAnimDuration) {
        this.mContentInAnimDuration = defaultInAnimDuration;
        return this;
    }

    public AnyLayer defaultContentOutAnimDuration(long defaultOutAnimDuration) {
        this.mContentOutAnimDuration = defaultOutAnimDuration;
        return this;
    }

    public AnyLayer defaultBackgroundAnimDuration(long defaultAnimDuration) {
        this.mBackgroundInAnimDuration = defaultAnimDuration;
        this.mBackgroundOutAnimDuration = defaultAnimDuration;
        return this;
    }

    public AnyLayer defaultBackgroundInAnimDuration(long defaultInAnimDuration) {
        this.mBackgroundInAnimDuration = defaultInAnimDuration;
        return this;
    }

    public AnyLayer defaultBackgroundOutAnimDuration(long defaultOutAnimDuration) {
        this.mBackgroundOutAnimDuration = defaultOutAnimDuration;
        return this;
    }

    /**
     * 设置背景为当前activity的高斯模糊效果
     * 设置之后其他背景设置方法失效，仅{@link #backgroundColorInt(int)}生效
     * 且设置的backgroundColor值调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时的{@link #backgroundColorInt(int)}为半透明颜色
     *
     * @param radius 模糊半径
     * @return PopupView
     */
    public AnyLayer backgroundBlurRadius(@FloatRange(from = 0, fromInclusive = false, to = 25) float radius) {
        mBackgroundBlurRadius = radius;
        return this;
    }

    public AnyLayer backgroundBlurScale(@FloatRange(from = 1) float scale) {
        mBackgroundBlurScale = scale;
        return this;
    }

    public AnyLayer backgroundBitmap(@NonNull Bitmap bitmap) {
        mBackgroundBitmap = bitmap;
        return this;
    }

    public AnyLayer backgroundResource(@DrawableRes int resource) {
        mBackgroundResource = resource;
        return this;
    }

    public AnyLayer backgroundDrawable(@NonNull Drawable drawable) {
        mBackgroundDrawable = drawable;
        return this;
    }

    /**
     * 在调用了{@link #backgroundBitmap(Bitmap)}或者{@link #backgroundBlurRadius(float)}方法后
     * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时传入的颜色为半透明颜色
     *
     * @param colorInt ColorInt
     * @return PopupView
     */
    public AnyLayer backgroundColorInt(@ColorInt int colorInt) {
        mBackgroundColor = colorInt;
        return this;
    }

    public AnyLayer backgroundColorRes(@ColorRes int colorRes) {
        mBackgroundColor = ContextCompat.getColor(mContext, colorRes);
        return this;
    }

    public AnyLayer cancelableOnTouchOutside(boolean cancelable) {
        mCancelableOnTouchOutside = cancelable;
        return this;
    }

    public AnyLayer cancelableOnClickKeyBack(boolean cancelable) {
        mLayerManager.setCancelableOnClickKeyBack(cancelable);
        return this;
    }

    public AnyLayer onClick(@IdRes int viewId, OnLayerClickListener listener) {
        mViewHolder.addOnClickListener(listener, viewId, null);
        return this;
    }

    public AnyLayer onClick(OnLayerClickListener listener, @IdRes int viewId, @IdRes int... viewIds) {
        mViewHolder.addOnClickListener(listener, viewId, viewIds);
        return this;
    }

    public AnyLayer onClickToDismiss(@IdRes int viewId, @IdRes int... viewIds) {
        mViewHolder.addOnClickListener(new OnLayerClickListener() {
            @Override
            public void onClick(AnyLayer anyLayer, View v) {
                dismiss();
            }
        }, viewId, viewIds);
        return this;
    }

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
                    .init(mViewHolder.getContentWrapper(), mContent, editText)
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
            mContentInAnimDuration = mContentAnim.inAnim(mContent);
        } else {
            if (mContentInAnim != null) {
                mContent.startAnimation(mContentInAnim);
            } else {
                AnimHelper.startZoomInAnim(mContent, mContentInAnimDuration);
            }
        }
    }

    private void startContentOutAnim() {
        if (mContentAnim != null) {
            mContentOutAnimDuration = mContentAnim.outAnim(mContent);
        } else {
            if (mContentOutAnim != null) {
                mContent.startAnimation(mContentOutAnim);
            } else {
                AnimHelper.startZoomOutAnim(mContent, mContentOutAnimDuration);
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

    private void initContainer() {
        if (mCancelableOnTouchOutside) {
            mViewHolder.getContainer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (mTargetView == null) {
            FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) mViewHolder.getContentWrapper().getLayoutParams();
            contentWrapperParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
            contentWrapperParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
            mViewHolder.getContentWrapper().setLayoutParams(contentWrapperParams);
            if (mGravity != -1) {
                FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) mContent.getLayoutParams();
                contentParams.gravity = mGravity;
                mContent.setLayoutParams(contentParams);
            }
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
            if (mAlignmentDirection == Alignment.Direction.HORIZONTAL) {
                if (mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
                    paddingRight = mRootView.getWidth() - targetX;
                } else if (mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
                    paddingLeft = targetX + targetWidth;
                } else if (mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                    paddingLeft = targetX;
                } else if (mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
                    paddingRight = mRootView.getWidth() - targetX - targetWidth;
                }
            } else if (mAlignmentDirection == Alignment.Direction.VERTICAL) {
                if (mAlignmentVertical == Alignment.Vertical.ABOVE) {
                    paddingBottom = mRootView.getHeight() - targetY;
                } else if (mAlignmentVertical == Alignment.Vertical.BELOW) {
                    paddingTop = targetY + targetHeight;
                } else if (mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                    paddingTop = targetY;
                } else if (mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
                    paddingBottom = mRootView.getHeight() - targetY - targetHeight;
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
                    if (mInsideAlignment) {
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
        if (mBackgroundBlurRadius > 0) {
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
                    Bitmap blur = BlurUtils.blur(mContext, original, mBackgroundBlurRadius, mBackgroundBlurScale);
                    original.recycle();
                    mViewHolder.getBackground().setScaleType(ImageView.ScaleType.FIT_XY);
                    mViewHolder.getBackground().setImageBitmap(blur);
                    mViewHolder.getBackground().setBackgroundColor(mBackgroundColor);
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
            } else if (mBackgroundColor != Color.TRANSPARENT) {
                mViewHolder.getBackground().setImageDrawable(new ColorDrawable(mBackgroundColor));
            }
        }
    }

    private void initContent() {
        if (mContent != null) {
            ViewGroup contentParent = (ViewGroup) mContent.getParent();
            if (contentParent != null) {
                contentParent.removeView(mContent);
            }
            mContent.setClickable(true);
            mViewHolder.getContentWrapper().addView(mContent);
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
        void onClick(AnyLayer anyLayer, View v);
    }

    public interface OnLayerDismissListener {
        void onDismissing(AnyLayer anyLayer);

        void onDismissed(AnyLayer anyLayer);
    }

    public interface OnLayerShowListener {
        void onShowing(AnyLayer anyLayer);

        void onShown(AnyLayer anyLayer);
    }

    public interface OnVisibleChangeListener {
        void onShow(AnyLayer anyLayer);

        void onDismiss(AnyLayer anyLayer);
    }
}
