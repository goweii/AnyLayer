package per.goweii.anylayer.notification;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import per.goweii.anylayer.DecorLayer;
import per.goweii.anylayer.R;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.Utils;
import per.goweii.anylayer.widget.MaxSizeFrameLayout;
import per.goweii.anylayer.widget.SwipeLayout;
import per.goweii.visualeffect.blur.RSBlurEffect;
import per.goweii.visualeffect.core.VisualEffect;
import per.goweii.visualeffect.view.BackdropVisualEffectView;

public class NotificationLayer extends DecorLayer {

    private Runnable mDismissRunnable = null;
    private boolean mSwiping = false;

    public NotificationLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public NotificationLayer(@NonNull Activity activity) {
        super(activity);
    }

    @IntRange(from = 0)
    @Override
    protected int getLevel() {
        return Level.NOTIFICATION;
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

    @Override
    public void show() {
        super.show();
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getViewHolder().getChildNullable() == null) {
            SwipeLayout container = (SwipeLayout) inflater.inflate(R.layout.anylayer_notification_layer, parent, false);
            getViewHolder().setChild(container);
            getViewHolder().setContent(onCreateContent(inflater, getViewHolder().getChild()));
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
        return getViewHolder().getChild();
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

    @NonNull
    protected FrameLayout.LayoutParams generateContentDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        return AnimatorHelper.createTopInAnim(getViewHolder().getContentWrapper());
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        return AnimatorHelper.createTopOutAnim(getViewHolder().getContentWrapper());
    }

    @CallSuper
    @Override
    protected void onAttach() {
        onInitContent();
        super.onAttach();
        getViewHolder().getChild().setSwipeDirection(
                SwipeLayout.Direction.TOP | SwipeLayout.Direction.LEFT | SwipeLayout.Direction.RIGHT
        );
        getViewHolder().getChild().setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onStart(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                mSwiping = true;
                setAutoDismiss(false);
                getListenerHolder().notifyOnSwipeStart(NotificationLayer.this);
            }

            @Override
            public void onSwiping(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                getListenerHolder().notifyOnSwiping(NotificationLayer.this, direction, fraction);
            }

            @Override
            public void onEnd(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                mSwiping = false;
                if (fraction == 1F) {
                    getListenerHolder().notifyOnSwipeEnd(NotificationLayer.this, direction);
                    // 动画执行结束后不能直接removeView，要在下一个dispatchDraw周期移除
                    // 否则会崩溃，因为viewGroup的childCount没有来得及-1，获取到的view为空
                    getViewHolder().getContent().setVisibility(View.INVISIBLE);
                    getViewHolder().getContent().post(new Runnable() {
                        @Override
                        public void run() {
                            dismiss(false);
                        }
                    });
                } else if (fraction == 0F) {
                    setAutoDismiss(true);
                }
            }
        });
        if (getConfig().mMaxWidth >= 0) {
            getViewHolder().getContentWrapper().setMaxWidth(getConfig().mMaxWidth);
        }
        if (getConfig().mMaxHeight >= 0) {
            getViewHolder().getContentWrapper().setMaxHeight(getConfig().mMaxHeight);
        }
        getViewHolder().getContent().setVisibility(View.VISIBLE);
        getViewHolder().getContentWrapper().setOnDispatchTouchListener(new MaxSizeFrameLayout.OnDispatchTouchListener() {
            @Override
            public void onDispatch(MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setAutoDismiss(false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (!mSwiping) {
                            setAutoDismiss(true);
                        }
                        break;
                }
            }
        });
        bindDefaultContentData();
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
        setAutoDismiss(true);
    }

    @CallSuper
    @Override
    protected void onPreDismiss() {
        if (mDismissRunnable != null) {
            getChild().removeCallbacks(mDismissRunnable);
        }
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
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onInitContent() {
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
    }

    @Override
    protected void fitDecorInsides() {
        fitDecorInsidesToViewPadding(getViewHolder().getChild());
        getViewHolder().getChild().setClipToPadding(false);
    }

    private void bindDefaultContentData() {
        if (getViewHolder().getTop() != null) {
            if (getViewHolder().getIcon() != null) {
                if (getConfig().mIcon != null) {
                    getViewHolder().getIcon().setVisibility(View.VISIBLE);
                    getViewHolder().getIcon().setImageDrawable(getConfig().mIcon);
                } else {
                    getViewHolder().getIcon().setVisibility(View.GONE);
                }
            }
            if (getViewHolder().getLabel() != null) {
                if (!TextUtils.isEmpty(getConfig().mLabel)) {
                    getViewHolder().getLabel().setVisibility(View.VISIBLE);
                    getViewHolder().getLabel().setText(getConfig().mLabel);
                } else {
                    getViewHolder().getLabel().setVisibility(View.GONE);
                }
            }
            if (getViewHolder().getTime() != null) {
                if (!TextUtils.isEmpty(getConfig().mTime)) {
                    getViewHolder().getTime().setVisibility(View.VISIBLE);
                    getViewHolder().getTime().setText(getConfig().mTime);
                } else {
                    if (!TextUtils.isEmpty(getConfig().mTimePattern)) {
                        getViewHolder().getTime().setVisibility(View.VISIBLE);
                        String time = new SimpleDateFormat(getConfig().mTimePattern, Locale.getDefault()).format(new Date());
                        getViewHolder().getTime().setText(time);
                    } else {
                        getViewHolder().getTime().setVisibility(View.GONE);
                    }
                }
            }
            LinearLayout topView = getViewHolder().getTop();
            topView.setVisibility(View.GONE);
            for (int i = 0; i < topView.getChildCount(); i++) {
                if (topView.getChildAt(i).getVisibility() == View.VISIBLE) {
                    topView.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        if (getViewHolder().getTitle() != null) {
            if (!TextUtils.isEmpty(getConfig().mTitle)) {
                getViewHolder().getTitle().setVisibility(View.VISIBLE);
                getViewHolder().getTitle().setText(getConfig().mTitle);
            } else {
                getViewHolder().getTitle().setVisibility(View.GONE);
            }
        }
        if (getViewHolder().getDesc() != null) {
            if (!TextUtils.isEmpty(getConfig().mDesc)) {
                getViewHolder().getDesc().setVisibility(View.VISIBLE);
                getViewHolder().getDesc().setText(getConfig().mDesc);
            } else {
                getViewHolder().getDesc().setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    public NotificationLayer setContentView(@NonNull View contentView) {
        getViewHolder().setContent(contentView);
        return this;
    }

    @NonNull
    public NotificationLayer setContentView(@LayoutRes int contentViewId) {
        getConfig().mContentViewId = contentViewId;
        return this;
    }

    @NonNull
    public NotificationLayer setMaxWidth(int maxWidth) {
        getConfig().mMaxWidth = maxWidth;
        return this;
    }

    @NonNull
    public NotificationLayer setMaxHeight(int maxHeight) {
        getConfig().mMaxHeight = maxHeight;
        return this;
    }

    @NonNull
    public NotificationLayer setContentBlurRadius(@FloatRange(from = 0F) float radius) {
        getConfig().mContentBlurRadius = radius;
        return this;
    }

    @NonNull
    public NotificationLayer setContentBlurPercent(@FloatRange(from = 0F) float percent) {
        getConfig().mContentBlurPercent = percent;
        return this;
    }

    @NonNull
    public NotificationLayer setContentBlurSimple(@FloatRange(from = 1F) float simple) {
        getConfig().mContentBlurSimple = simple;
        return this;
    }

    @NonNull
    public NotificationLayer setContentBlurColorInt(@ColorInt int colorInt) {
        getConfig().mContentBlurColor = colorInt;
        return this;
    }

    @NonNull
    public NotificationLayer setContentBlurColorRes(@ColorRes int colorRes) {
        getConfig().mContentBlurColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @NonNull
    public NotificationLayer setContentBlurCornerRadius(float radius, int unit) {
        getConfig().mContentBlurCornerRadius = TypedValue.applyDimension(
                unit, radius, getActivity().getResources().getDisplayMetrics()
        );
        return this;
    }

    @NonNull
    public NotificationLayer setContentBlurCornerRadiusDp(float radius) {
        getConfig().mContentBlurCornerRadius = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, radius, getActivity().getResources().getDisplayMetrics()
        );
        return this;
    }

    @NonNull
    public NotificationLayer setContentBlurCornerRadiusPx(float radius) {
        getConfig().mContentBlurCornerRadius = radius;
        return this;
    }

    @NonNull
    public NotificationLayer setIcon(@DrawableRes int drawableId) {
        getConfig().mIcon = ContextCompat.getDrawable(getActivity(), drawableId);
        return this;
    }

    @NonNull
    public NotificationLayer setIcon(@Nullable Drawable drawable) {
        getConfig().mIcon = drawable;
        return this;
    }

    @NonNull
    public NotificationLayer setLabel(@Nullable CharSequence label) {
        getConfig().mLabel = label;
        return this;
    }

    @NonNull
    public NotificationLayer setLabel(@StringRes int labelRes) {
        getConfig().mLabel = getActivity().getString(labelRes);
        return this;
    }

    @NonNull
    public NotificationLayer setTime(@Nullable CharSequence time) {
        getConfig().mTime = time;
        return this;
    }

    @NonNull
    public NotificationLayer setTimePattern(@Nullable String pattern) {
        getConfig().mTimePattern = pattern;
        return this;
    }

    @NonNull
    public NotificationLayer setTitle(@NonNull CharSequence title) {
        getConfig().mTitle = title;
        return this;
    }

    @NonNull
    public NotificationLayer setTitle(@StringRes int titleRes) {
        getConfig().mTitle = getActivity().getString(titleRes);
        return this;
    }

    @NonNull
    public NotificationLayer setDesc(@NonNull CharSequence desc) {
        getConfig().mDesc = desc;
        return this;
    }

    @NonNull
    public NotificationLayer setDesc(@StringRes int descRes) {
        getConfig().mDesc = getActivity().getString(descRes);
        return this;
    }

    @NonNull
    public NotificationLayer setDuration(long duration) {
        getConfig().mDuration = duration;
        return this;
    }

    @NonNull
    public NotificationLayer setOnNotificationClickListener(@NonNull OnClickListener listener) {
        addOnClickToDismissListener(listener);
        return this;
    }

    @NonNull
    public NotificationLayer setOnNotificationLongClickListener(@NonNull OnLongClickListener listener) {
        addOnLongClickToDismissListener(listener);
        return this;
    }

    public void setAutoDismiss(boolean enable) {
        if (mDismissRunnable != null) {
            getChild().removeCallbacks(mDismissRunnable);
        }
        if (enable && isShown() && getConfig().mDuration > 0) {
            if (mDismissRunnable == null) {
                mDismissRunnable = new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                };
            }
            getChild().postDelayed(mDismissRunnable, getConfig().mDuration);
        }
    }

    @NonNull
    public NotificationLayer setSwipeTransformer(@Nullable NotificationLayer.SwipeTransformer swipeTransformer) {
        getConfig().mSwipeTransformer = swipeTransformer;
        return this;
    }

    /**
     * 浮层拖拽事件监听
     *
     * @param swipeListener OnSwipeListener
     */
    @NonNull
    public NotificationLayer addOnSwipeListener(@NonNull NotificationLayer.OnSwipeListener swipeListener) {
        getListenerHolder().addOnSwipeListener(swipeListener);
        return this;
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private MaxSizeFrameLayout mContentWrapper;
        private View mContent;

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
            mContentWrapper = getChild().findViewById(R.id.anylayler_notification_content_wrapper);
        }

        @NonNull
        @Override
        public SwipeLayout getChild() {
            return (SwipeLayout) super.getChild();
        }

        @Nullable
        @Override
        protected SwipeLayout getChildNullable() {
            return (SwipeLayout) super.getChildNullable();
        }

        @NonNull
        protected MaxSizeFrameLayout getContentWrapper() {
            return mContentWrapper;
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
            if (mContent instanceof CardView && mContent.getId() == R.id.anylayler_notification_content) {
                return (BackdropVisualEffectView) mContent.findViewById(R.id.anylayler_notification_content_effect);
            }
            final ViewGroup contentWrapper = mContentWrapper;
            final View realContent = mContent;
            final Context context = contentWrapper.getContext();
            if (realContent.getId() == View.NO_ID) {
                realContent.setId(R.id.anylayler_notification_content_really);
            }
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) realContent.getLayoutParams();

            int contentIndex = contentWrapper.indexOfChild(realContent);
            contentWrapper.removeViewAt(contentIndex);

            Drawable realContentBg = realContent.getBackground();
            if (realContentBg != null) {
                realContentBg.setAlpha(0);
            }

            BackdropVisualEffectView backdropVisualEffectView = new BackdropVisualEffectView(context);
            backdropVisualEffectView.setId(R.id.anylayler_notification_content_effect);
            backdropVisualEffectView.setShowDebugInfo(false);

            CardView cardView = new CardView(context);
            cardView.setId(R.id.anylayler_notification_content);
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

        @Nullable
        @Override
        protected View getNoIdClickView() {
            return mContent;
        }

        @Nullable
        public LinearLayout getTop() {
            return mContent.findViewById(R.id.anylayler_notification_content_top);
        }

        @Nullable
        public ImageView getIcon() {
            return mContent.findViewById(R.id.anylayler_notification_content_icon);
        }

        @Nullable
        public TextView getLabel() {
            return mContent.findViewById(R.id.anylayler_notification_content_label);
        }

        @Nullable
        public TextView getTime() {
            return mContent.findViewById(R.id.anylayler_notification_content_time);
        }

        @Nullable
        public TextView getTitle() {
            return mContent.findViewById(R.id.anylayler_notification_content_title);
        }

        @Nullable
        public TextView getDesc() {
            return mContent.findViewById(R.id.anylayler_notification_content_desc);
        }
    }

    protected static class Config extends DecorLayer.Config {
        protected int mContentViewId = R.layout.anylayer_notification_content;

        protected float mContentBlurPercent = 0F;
        protected float mContentBlurRadius = 0F;
        protected float mContentBlurSimple = 4F;
        @ColorInt
        protected int mContentBlurColor = Color.TRANSPARENT;
        protected float mContentBlurCornerRadius = 0F;

        protected long mDuration = 5000L;
        protected int mMaxWidth = -1;
        protected int mMaxHeight = -1;

        protected CharSequence mLabel = null;
        protected Drawable mIcon = null;
        protected CharSequence mTime = null;
        protected String mTimePattern = null;
        protected CharSequence mTitle = null;
        protected CharSequence mDesc = null;

        @Nullable
        protected SwipeTransformer mSwipeTransformer = null;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
        private List<OnSwipeListener> mOnSwipeListeners = null;

        private void addOnSwipeListener(@NonNull OnSwipeListener onSwipeListener) {
            if (mOnSwipeListeners == null) {
                mOnSwipeListeners = new ArrayList<>(1);
            }
            mOnSwipeListeners.add(onSwipeListener);
        }

        private void notifyOnSwipeStart(@NonNull NotificationLayer layer) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onStart(layer);
                }
            }
        }

        private void notifyOnSwiping(@NonNull NotificationLayer layer,
                                     @SwipeLayout.Direction int direction,
                                     @FloatRange(from = 0F, to = 1F) float fraction) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onSwiping(layer, direction, fraction);
                }
            }
        }

        private void notifyOnSwipeEnd(@NonNull NotificationLayer layer,
                                      @SwipeLayout.Direction int direction) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onEnd(layer, direction);
                }
            }
        }
    }

    public interface SwipeTransformer {
        void onSwiping(@NonNull NotificationLayer layer,
                       @SwipeLayout.Direction int direction,
                       @FloatRange(from = 0F, to = 1F) float fraction);
    }

    public interface OnSwipeListener {
        void onStart(@NonNull NotificationLayer layer);

        void onSwiping(@NonNull NotificationLayer layer,
                       @SwipeLayout.Direction int direction,
                       @FloatRange(from = 0F, to = 1F) float fraction);

        void onEnd(@NonNull NotificationLayer layer,
                   @SwipeLayout.Direction int direction);
    }
}
