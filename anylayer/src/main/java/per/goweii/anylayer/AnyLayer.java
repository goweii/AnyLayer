package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import per.goweii.anylayer.listener.IAnim;
import per.goweii.anylayer.listener.IDataBinder;
import per.goweii.anylayer.listener.OnLayerClickListener;
import per.goweii.anylayer.listener.OnLayerDismissListener;
import per.goweii.anylayer.listener.OnLayerShowListener;
import per.goweii.anylayer.listener.OnVisibleChangeListener;
import per.goweii.anylayer.utils.AnimHelper;
import per.goweii.anylayer.utils.Utils;
import per.goweii.anylayer.utils.blur.BlurUtils;

/**
 * @author Cuizhen
 */
public class AnyLayer implements View.OnKeyListener {

    private final Context mContext;
    private final LayoutInflater mInflater;

    private View mTargetView = null;
    private ViewGroup mRootView;
    private FrameLayout mContainer;
    private FrameLayout mContentWrapper;
    private ImageView mBackground;
    private View mContent;
    private ViewHolder mViewHolder;

    private boolean mShow = false;
    private boolean mDismissing = false;
    private boolean mShowing = false;

    private int mGravity = Gravity.CENTER;
    private float mBackgroundBlurRadius = 0;
    private float mBackgroundBlurScale = 0;
    private Bitmap mBackgroundBitmap = null;
    private int mBackgroundResource = -1;
    private Drawable mBackgroundDrawable = null;
    private int mBackgroundColor = Color.TRANSPARENT;

    private boolean mCancelableOnTouchOutside = true;
    private boolean mCancelableOnClickKeyBack = true;

    private IAnim mBackgroundAnim = null;
    private Animation mBackgroundInAnim = null;
    private Animation mBackgroundOutAnim = null;
    private IAnim mContentAnim = null;
    private Animation mContentInAnim = null;
    private Animation mContentOutAnim = null;
    private long mBackgroundAnimDuration = 200;
    private long mContentAnimDuration = 250;

    private View currentKeyView = null;

    private IDataBinder mDataBinder = null;
    private OnVisibleChangeListener mOnVisibleChangeListener = null;
    private OnLayerShowListener mOnLayerShowListener = null;
    private OnLayerDismissListener mOnLayerDismissListener = null;
    private Direction mDirection = Direction.BOTTOM;

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
        Activity activity = Utils.getActivity(context);
        if (activity == null) {
            throw new NullPointerException();
        }
        mRootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
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
        Activity activity = Utils.getActivity(mContext);
        if (activity == null) {
            throw new NullPointerException();
        }
        mRootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        initView();
    }

    private void initView() {
        mContainer = (FrameLayout) mInflater.inflate(R.layout.layout_any_layer, mRootView, false);
        mContentWrapper = mContainer.findViewById(R.id.fl_content);
        mBackground = mContainer.findViewById(R.id.iv_background);
        mViewHolder = new ViewHolder(this, mContainer);
    }

    public void show() {
        if (isShow()) {
            return;
        }
        mShow = true;
        if (mShowing) {
            return;
        }
        onAttached();
    }

    public void dismiss() {
        if (mContainer.getParent() == null || !mShow) {
            return;
        }
        if (mDismissing) {
            return;
        }
        mDismissing = true;
        doOutAnim();
        mRootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                onDetached();
            }
        }, getDuration());
        if (mOnLayerDismissListener != null) {
            mOnLayerDismissListener.onDismissing(AnyLayer.this);
        }
    }

    private void onAttached() {
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

    private void initContainer() {
        if (mCancelableOnTouchOutside) {
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (mTargetView != null) {
            Activity activity = Utils.getActivity(mContext);
            if (activity == null) {
                throw new NullPointerException();
            }
            View decorView = activity.getWindow().getDecorView();
            mRootView = decorView.findViewById(android.R.id.content);
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
            mContainer.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
        mRootView.addView(mContainer);
        mContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (mContainer.getViewTreeObserver().isAlive()) {
                    mContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                mRootView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mShowing = false;
                        if (mOnLayerShowListener != null) {
                            mOnLayerShowListener.onShown(AnyLayer.this);
                        }
                    }
                }, getDuration());
                doInAnim();
                return true;
            }
        });
    }

    private void initBackground() {
        if (mBackgroundBlurRadius > 0) {
            mBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    Bitmap snapshot = Utils.snapshot(mRootView);
                    int[] location = new int[2];
                    mBackground.getLocationOnScreen(location);
                    Bitmap original = Bitmap.createBitmap(snapshot, location[0], location[1], mBackground.getWidth(), mBackground.getHeight());
                    snapshot.recycle();
                    Bitmap blur = BlurUtils.blur(mContext, original, mBackgroundBlurRadius, mBackgroundBlurScale);
                    original.recycle();
                    mBackground.setScaleType(ImageView.ScaleType.FIT_XY);
                    mBackground.setImageBitmap(blur);
                    mBackground.setBackgroundColor(mBackgroundColor);
                    return true;
                }
            });
        } else {
            if (mBackgroundBitmap != null) {
                mBackground.setImageBitmap(mBackgroundBitmap);
                mBackground.setColorFilter(mBackgroundColor);
            } else if (mBackgroundResource != -1) {
                mBackground.setImageResource(mBackgroundResource);
                mBackground.setColorFilter(mBackgroundColor);
            } else if (mBackgroundDrawable != null) {
                mBackground.setImageDrawable(mBackgroundDrawable);
                mBackground.setColorFilter(mBackgroundColor);
            } else if (mBackgroundColor != Color.TRANSPARENT) {
                mBackground.setImageDrawable(new ColorDrawable(mBackgroundColor));
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
                mContentWrapper.addView(mContent);
            }
            mContent.setFocusable(true);
            mContent.setFocusableInTouchMode(true);
            mContent.requestFocus();
            currentKeyView = mContent;
            currentKeyView.setOnKeyListener(this);
            ViewTreeObserver observer = mContent.getViewTreeObserver();
            observer.addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
                @Override
                public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                    if (currentKeyView != null) {
                        currentKeyView.setOnKeyListener(null);
                    }
                    if (oldFocus != null) {
                        oldFocus.setOnKeyListener(null);
                    }
                    if (newFocus != null) {
                        currentKeyView = newFocus;
                        currentKeyView.setOnKeyListener(AnyLayer.this);
                    }
                }
            });
        }
    }

    private void onDetached() {
        if (currentKeyView != null) {
            currentKeyView.setOnKeyListener(null);
        }
        mRootView.removeView(mContainer);
        mShow = false;
        mDismissing = false;
        if (mOnVisibleChangeListener != null) {
            mOnVisibleChangeListener.onDismiss(AnyLayer.this);
        }
        if (mOnLayerDismissListener != null) {
            mOnLayerDismissListener.onDismissed(AnyLayer.this);
        }
        mViewHolder = null;
        mContainer = null;
        mBackground = null;
        mTargetView = null;
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

    /**
     * 控制与targetView的对齐方式
     */
    public enum Alignment {
        TOP, BOTTOM, LEFT, RIGHT, CENTER
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
        mContent = mInflater.inflate(contentViewId, mContainer, false);
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
        mCancelableOnClickKeyBack = cancelable;
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
        return mContainer.getParent() != null || mShow;
    }

    private void doInAnim() {
        startContentInAnim();
        startBackgroundInAnim();
    }

    private void doOutAnim() {
        startContentOutAnim();
        startBackgroundOutAnim();
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
            mBackgroundAnimDuration = mBackgroundAnim.inAnim(mBackground);
        } else {
            if (mBackgroundInAnim != null) {
                mBackground.startAnimation(mBackgroundInAnim);
            } else {
                AnimHelper.startAlphaInAnim(mBackground, mBackgroundAnimDuration);
            }
        }
    }

    private void startBackgroundOutAnim() {
        if (mBackgroundAnim != null) {
            mBackgroundAnimDuration = mBackgroundAnim.outAnim(mBackground);
        } else {
            if (mBackgroundOutAnim != null) {
                mBackground.startAnimation(mBackgroundOutAnim);
            } else {
                AnimHelper.startAlphaOutAnim(mBackground, mBackgroundAnimDuration);
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (mContainer.getParent() == null || !mShow) {
            return false;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mCancelableOnClickKeyBack) {
                    dismiss();
                }
                return true;
            }
        }
        return false;
    }
}
