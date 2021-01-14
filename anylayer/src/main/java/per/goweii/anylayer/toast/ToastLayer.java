package per.goweii.anylayer.toast;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import per.goweii.anylayer.DecorLayer;
import per.goweii.anylayer.GlobalConfig;
import per.goweii.anylayer.R;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.Utils;

public class ToastLayer extends DecorLayer {
    private final Runnable mDismissRunnable = new Runnable() {
        @Override
        public void run() {
            if (isShown()) {
                dismiss();
            }
        }
    };

    public ToastLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public ToastLayer(@NonNull Activity activity) {
        super(activity);
    }

    @IntRange(from = 0)
    @Override
    protected int getLevel() {
        return Level.TOAST;
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
            FrameLayout container = (FrameLayout) inflater.inflate(R.layout.anylayer_toast_layer, parent, false);
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
            getViewHolder().getChild().addView(getViewHolder().getContent());
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
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        Animator animator = super.onCreateInAnimator(view);
        if (animator == null) {
            if (GlobalConfig.get().toastAnimatorCreator != null) {
                animator = GlobalConfig.get().toastAnimatorCreator.createInAnimator(view);
            }
        }
        if (animator == null) {
            animator = AnimatorHelper.createZoomAlphaInAnim(view);
            animator.setDuration(GlobalConfig.get().toastAnimDuration);
        }
        return animator;
    }

    @NonNull
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        Animator animator = super.onCreateOutAnimator(view);
        if (animator == null) {
            if (GlobalConfig.get().toastAnimatorCreator != null) {
                animator = GlobalConfig.get().toastAnimatorCreator.createOutAnimator(view);
            }
        }
        if (animator == null) {
            animator = AnimatorHelper.createZoomAlphaOutAnim(view);
            animator.setDuration(GlobalConfig.get().toastAnimDuration);
        }
        return animator;
    }

    @CallSuper
    @Override
    protected void onAttach() {
        super.onAttach();
        getChild().setTag(this);
        if (getConfig().mRemoveOthers) {
            removeOthers();
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getChild().getLayoutParams();
        params.gravity = getConfig().mGravity;
        if (getConfig().mMarginLeft != Integer.MIN_VALUE) {
            params.leftMargin = getConfig().mMarginLeft;
        }
        if (getConfig().mMarginTop != Integer.MIN_VALUE) {
            params.topMargin = getConfig().mMarginTop;
        }
        if (getConfig().mMarginRight != Integer.MIN_VALUE) {
            params.rightMargin = getConfig().mMarginRight;
        }
        if (getConfig().mMarginBottom != Integer.MIN_VALUE) {
            params.bottomMargin = getConfig().mMarginBottom;
        }
        getChild().setLayoutParams(params);
        bindDefaultContentData();
    }

    @CallSuper
    @Override
    protected void onAppear() {
        super.onAppear();
    }

    @CallSuper
    @Override
    protected void onShow() {
        super.onShow();
        if (getConfig().mDuration > 0) {
            getChild().postDelayed(mDismissRunnable, getConfig().mDuration);
        }
    }

    @CallSuper
    @Override
    protected void onDismiss() {
        getChild().removeCallbacks(mDismissRunnable);
        super.onDismiss();
    }

    @CallSuper
    @Override
    protected void onDisappear() {
        super.onDisappear();
    }

    @CallSuper
    @Override
    protected void onDetach() {
        getChild().setTag(null);
        super.onDetach();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void removeOthers() {
        final ViewGroup parent = getParent();
        final int count = parent.getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);
            Object tag = child.getTag();
            if (tag instanceof ToastLayer) {
                ToastLayer toastLayer = (ToastLayer) tag;
                if (toastLayer != this) {
                    toastLayer.dismiss(false);
                }
            }
        }
    }

    private void bindDefaultContentData() {
        if (getConfig().mBackgroundDrawable != null) {
            getViewHolder().getContent().setBackgroundDrawable(getConfig().mBackgroundDrawable);
        } else if (getConfig().mBackgroundResource > 0) {
            getViewHolder().getContent().setBackgroundResource(getConfig().mBackgroundResource);
        }
        if (getViewHolder().getContent().getBackground() != null) {
            getViewHolder().getContent().getBackground().setColorFilter(getConfig().mBackgroundColor, PorterDuff.Mode.SRC_ATOP);
        }
        getViewHolder().getContent().setAlpha(getConfig().mAlpha);
        if (getViewHolder().getIcon() != null) {
            if (getConfig().mIcon > 0) {
                getViewHolder().getIcon().setVisibility(View.VISIBLE);
                getViewHolder().getIcon().setImageResource(getConfig().mIcon);
            } else {
                getViewHolder().getIcon().setVisibility(View.GONE);
            }
        }
        if (getViewHolder().getMessage() != null) {
            if (getConfig().mTextColorInt != Color.TRANSPARENT) {
                getViewHolder().getMessage().setTextColor(getConfig().mTextColorInt);
            } else if (getConfig().mTextColorRes != -1) {
                getViewHolder().getMessage().setTextColor(ContextCompat.getColor(getActivity(), getConfig().mTextColorRes));
            }
            if (TextUtils.isEmpty(getConfig().mMessage)) {
                getViewHolder().getMessage().setVisibility(View.GONE);
                getViewHolder().getMessage().setText("");
            } else {
                getViewHolder().getMessage().setVisibility(View.VISIBLE);
                getViewHolder().getMessage().setText(getConfig().mMessage);
            }
        }
    }

    @NonNull
    public ToastLayer removeOthers(boolean removeOthers) {
        getConfig().mRemoveOthers = removeOthers;
        return this;
    }

    @NonNull
    public ToastLayer contentView(View contentView) {
        getViewHolder().setContent(contentView);
        return this;
    }

    @NonNull
    public ToastLayer contentView(@LayoutRes int contentView) {
        getConfig().mContentViewId = contentView;
        return this;
    }

    @NonNull
    public ToastLayer duration(long duration) {
        getConfig().mDuration = duration;
        return this;
    }

    @NonNull
    public ToastLayer message(@NonNull CharSequence message) {
        getConfig().mMessage = message;
        return this;
    }

    @NonNull
    public ToastLayer message(int message) {
        getConfig().mMessage = getActivity().getString(message);
        return this;
    }

    @NonNull
    public ToastLayer icon(@DrawableRes int icon) {
        getConfig().mIcon = icon;
        return this;
    }

    @NonNull
    public ToastLayer gravity(int gravity) {
        getConfig().mGravity = gravity;
        return this;
    }

    @NonNull
    public ToastLayer marginLeft(int marginLeft) {
        getConfig().mMarginLeft = marginLeft;
        return this;
    }

    @NonNull
    public ToastLayer marginTop(int marginTop) {
        getConfig().mMarginTop = marginTop;
        return this;
    }

    @NonNull
    public ToastLayer marginRight(int marginRight) {
        getConfig().mMarginRight = marginRight;
        return this;
    }

    @NonNull
    public ToastLayer marginBottom(int marginBottom) {
        getConfig().mMarginBottom = marginBottom;
        return this;
    }

    @NonNull
    public ToastLayer alpha(float alpha) {
        getConfig().mAlpha = alpha;
        return this;
    }

    @NonNull
    public ToastLayer backgroundResource(@NonNull Drawable drawable) {
        getConfig().mBackgroundDrawable = drawable;
        return this;
    }

    @NonNull
    public ToastLayer backgroundResource(@DrawableRes int resource) {
        getConfig().mBackgroundResource = resource;
        return this;
    }

    @NonNull
    public ToastLayer backgroundColorInt(@ColorInt int colorInt) {
        getConfig().mBackgroundColor = colorInt;
        return this;
    }

    @NonNull
    public ToastLayer backgroundColorRes(@ColorRes int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @NonNull
    public ToastLayer textColorInt(@ColorInt int colorInt) {
        getConfig().mTextColorInt = colorInt;
        return this;
    }

    @NonNull
    public ToastLayer textColorRes(@ColorRes int colorRes) {
        getConfig().mTextColorRes = colorRes;
        return this;
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private View mContent;

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
        }

        @NonNull
        @Override
        public FrameLayout getChild() {
            return (FrameLayout) super.getChild();
        }

        @Nullable
        @Override
        protected FrameLayout getChildNullable() {
            return (FrameLayout) super.getChildNullable();
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

        @Nullable
        public ImageView getIcon() {
            return mContent.findViewById(R.id.anylayler_toast_content_icon);
        }

        @Nullable
        public TextView getMessage() {
            return mContent.findViewById(R.id.anylayler_toast_content_msg);
        }
    }

    protected static class Config extends DecorLayer.Config {
        private int mContentViewId = R.layout.anylayer_toast_content;
        private boolean mRemoveOthers = true;
        private long mDuration = GlobalConfig.get().toastDuration;
        @NonNull
        private CharSequence mMessage = "";
        private int mIcon = 0;
        @Nullable
        private Drawable mBackgroundDrawable = null;
        private int mBackgroundResource = GlobalConfig.get().toastBackgroundRes;
        private int mBackgroundColor = Color.TRANSPARENT;
        @ColorInt
        private int mTextColorInt = GlobalConfig.get().toastTextColorInt;
        @ColorRes
        private int mTextColorRes = GlobalConfig.get().toastTextColorRes;
        private float mAlpha = GlobalConfig.get().toastAlpha;
        private int mGravity = GlobalConfig.get().toastGravity;
        private int mMarginLeft = GlobalConfig.get().toastMarginLeft;
        private int mMarginTop = GlobalConfig.get().toastMarginTop;
        private int mMarginRight = GlobalConfig.get().toastMarginRight;
        private int mMarginBottom = GlobalConfig.get().toastMarginBottom;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
    }
}
