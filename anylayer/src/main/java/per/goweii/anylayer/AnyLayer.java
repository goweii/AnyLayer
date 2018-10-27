package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
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
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author Cuizhen
 */
public class AnyLayer implements LayerManager.LiveListener {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private View mTargetView;
    private ViewGroup mRootView;
    private View mContent;
    private ViewHolder mViewHolder;

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
    private long mBackgroundAnimDuration = 300;
    private long mContentAnimDuration = 300;

    private IDataBinder mDataBinder = null;
    private OnVisibleChangeListener mOnVisibleChangeListener = null;
    private OnLayerShowListener mOnLayerShowListener = null;
    private OnLayerDismissListener mOnLayerDismissListener = null;

    private Direction mDirection = Direction.BOTTOM;
    private LayerManager mLayerManager;

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

    public AnyLayer(@NonNull ViewGroup viewGroup) {
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

    private void initRootView(){
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
        mLayerManager.setLiveListener(this);
    }

    public void show() {
        mLayerManager.add();
    }

    public void dismiss() {
        mLayerManager.remove();
    }

    @Override
    public void onAttach() {
        initContainer();
        initBackground();
        initContent();
        mViewHolder.bindListener();
        if (mDataBinder != null) {
            mDataBinder.bind(this);
        }
        if (mOnVisibleChangeListener != null) {
            mOnVisibleChangeListener.onShow(AnyLayer.this);
        }
        if (mOnLayerShowListener != null) {
            mOnLayerShowListener.onShowing(AnyLayer.this);
        }
    }

    @Override
    public long onAnimIn(View view) {
        startContentInAnim();
        startBackgroundInAnim();
        return getDuration();
    }

    @Override
    public long onAnimOut(View view) {
        startContentOutAnim();
        startBackgroundOutAnim();
        return getDuration();
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
        mViewHolder = null;
        mTargetView = null;
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
        if (mTargetView != null) {
            int[] locationTarget = new int[2];
            mTargetView.getLocationOnScreen(locationTarget);
            int[] locationRoot = new int[2];
            mRootView.getLocationOnScreen(locationRoot);
            int paddingTop = 0;
            int paddingBottom = 0;
            int paddingLeft = 0;
            int paddingRight = 0;
            if (mDirection == Direction.TOP) {
                paddingBottom = mRootView.getHeight() - locationTarget[1];
            } else if (mDirection == Direction.BOTTOM){
                paddingTop = locationTarget[1] - locationRoot[1] + mTargetView.getHeight();
            } else if (mDirection == Direction.LEFT){
                paddingRight = mRootView.getWidth() - locationTarget[0];
            } else if (mDirection == Direction.RIGHT){
                paddingLeft = locationTarget[0] - locationRoot[0] + mTargetView.getWidth();
            }
            mViewHolder.getContainer().setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    private void initBackground() {
        if (mBackgroundBlurRadius > 0) {
            mViewHolder.getBackground().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mViewHolder.getBackground().getViewTreeObserver().removeOnPreDrawListener(this);
                    Bitmap snapshot = Utils.snapshot(mRootView);
                    int[] location = new int[2];
                    mViewHolder.getBackground().getLocationOnScreen(location);
                    Bitmap original = Bitmap.createBitmap(snapshot, location[0], location[1], mViewHolder.getBackground().getWidth(), mViewHolder.getBackground().getHeight());
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
            if (mContent.getParent() == null) {
                mContent.setClickable(true);
                if (mGravity != -1) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mContent.getLayoutParams();
                    params.gravity = mGravity;
                    mContent.setLayoutParams(params);
                }
                mViewHolder.getContentWrapper().addView(mContent);
            }
        }
    }

    public AnyLayer setOnVisibleChangeListener(OnVisibleChangeListener mOnVisibleChangeListener) {
        this.mOnVisibleChangeListener = mOnVisibleChangeListener;
        return this;
    }

    public AnyLayer onOnLayerShowListener(OnLayerShowListener onLayerShowListener) {
        mOnLayerShowListener = onLayerShowListener;
        return this;
    }

    public AnyLayer onOnLayerDismissListener(OnLayerDismissListener onLayerDismissListener) {
        mOnLayerDismissListener = onLayerDismissListener;
        return this;
    }

    public AnyLayer direction(Direction direction) {
        mDirection = direction;
        return this;
    }

    /**
     * 控制出现的方向
     */
    public enum Direction {
        TOP, BOTTOM, LEFT, RIGHT
    }

    public AnyLayer gravity(int gravity) {
        mGravity = gravity;
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
        mContentAnimDuration = Math.max(mContentAnimDuration, mContentInAnim.getDuration());
        return this;
    }

    public AnyLayer contentOutAnim(@AnimRes int anim) {
        contentOutAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    public AnyLayer contentOutAnim(@NonNull Animation anim) {
        mContentOutAnim = anim;
        mContentAnimDuration = Math.max(mContentAnimDuration, mContentOutAnim.getDuration());
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
        mBackgroundAnimDuration = Math.max(mBackgroundAnimDuration, mBackgroundInAnim.getDuration());
        return this;
    }

    public AnyLayer backgroundOutAnim(@AnimRes int anim) {
        backgroundOutAnim(AnimationUtils.loadAnimation(mContext, anim));
        return this;
    }

    public AnyLayer backgroundOutAnim(@NonNull Animation anim) {
        mBackgroundOutAnim = anim;
        mBackgroundAnimDuration = Math.max(mBackgroundAnimDuration, mBackgroundOutAnim.getDuration());
        return this;
    }

    public AnyLayer defaultContentAnimDuration(long defaultAnimDuration) {
        this.mContentAnimDuration = defaultAnimDuration;
        return this;
    }

    public AnyLayer defaultBackgroundAnimDuration(long defaultAnimDuration) {
        this.mBackgroundAnimDuration = defaultAnimDuration;
        return this;
    }

    public AnyLayer contentView(@NonNull View contentView) {
        mContent = contentView;
        return this;
    }

    public AnyLayer contentView(@LayoutRes int contentViewId) {
        mContent = mInflater.inflate(contentViewId, mViewHolder.getContainer(), false);
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

    public <V extends View> V getView(@IdRes int viewId) {
        return mViewHolder.getView(viewId);
    }

    public ViewHolder getViewHolder() {
        return mViewHolder;
    }

    public AnyLayer bindData(IDataBinder dataBinder) {
        mDataBinder = dataBinder;
        return this;
    }

    public View getContentView() {
        return mContent;
    }

    private long getDuration() {
        return Math.max(mBackgroundAnimDuration, mContentAnimDuration);
    }

    public boolean isShow() {
        return mViewHolder.getContainer().getParent() != null;
    }

    private void startContentInAnim() {
        if (mContentAnim != null) {
            mContentAnimDuration = mContentAnim.inAnim(mContent);
        } else {
            if (mContentInAnim != null) {
                mContent.startAnimation(mContentInAnim);
            } else {
                AnimHelper.startZoomInAnim(mContent, mContentAnimDuration);
            }
        }
    }

    private void startContentOutAnim() {
        if (mContentAnim != null) {
            mContentAnimDuration = mContentAnim.outAnim(mContent);
        } else {
            if (mContentOutAnim != null) {
                mContent.startAnimation(mContentOutAnim);
            } else {
                AnimHelper.startZoomOutAnim(mContent, mContentAnimDuration);
            }
        }
    }

    private void startBackgroundInAnim() {
        if (mBackgroundAnim != null) {
            mBackgroundAnimDuration = mBackgroundAnim.inAnim(mViewHolder.getBackground());
        } else {
            if (mBackgroundInAnim != null) {
                mViewHolder.getBackground().startAnimation(mBackgroundInAnim);
            } else {
                AnimHelper.startAlphaInAnim(mViewHolder.getBackground(), mBackgroundAnimDuration);
            }
        }
    }

    private void startBackgroundOutAnim() {
        if (mBackgroundAnim != null) {
            mBackgroundAnimDuration = mBackgroundAnim.outAnim(mViewHolder.getBackground());
        } else {
            if (mBackgroundOutAnim != null) {
                mViewHolder.getBackground().startAnimation(mBackgroundOutAnim);
            } else {
                AnimHelper.startAlphaOutAnim(mViewHolder.getBackground(), mBackgroundAnimDuration);
            }
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
