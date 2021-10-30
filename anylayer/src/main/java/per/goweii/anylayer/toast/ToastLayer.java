package per.goweii.anylayer.toast;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
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
import per.goweii.anylayer.R;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.Utils;

public class ToastLayer extends DecorLayer {
    private final long mAnimDurDef = 220L;
    private final Runnable mDismissRunnable = new DismissRunnable();

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
        if (getConfig().mMarginLeft != null) {
            params.leftMargin = getConfig().mMarginLeft;
        } else {
            params.leftMargin = getActivity().getResources().getDimensionPixelOffset(R.dimen.anylayer_toast_margin);
        }
        if (getConfig().mMarginTop != null) {
            params.topMargin = getConfig().mMarginTop;
        } else {
            params.topMargin = getActivity().getResources().getDimensionPixelOffset(R.dimen.anylayer_toast_margin);
        }
        if (getConfig().mMarginRight != null) {
            params.rightMargin = getConfig().mMarginRight;
        } else {
            params.rightMargin = getActivity().getResources().getDimensionPixelOffset(R.dimen.anylayer_toast_margin);
        }
        if (getConfig().mMarginBottom != null) {
            params.bottomMargin = getConfig().mMarginBottom;
        } else {
            params.bottomMargin = getActivity().getResources().getDimensionPixelOffset(R.dimen.anylayer_toast_margin);
        }
        getChild().setLayoutParams(params);
        bindDefaultContentData();
    }

    @CallSuper
    @Override
    protected void onPostShow() {
        super.onPostShow();
        if (getConfig().mDuration > 0) {
            getChild().postDelayed(mDismissRunnable, getConfig().mDuration);
        }
    }

    @CallSuper
    @Override
    protected void onPreDismiss() {
        getChild().removeCallbacks(mDismissRunnable);
        super.onPreDismiss();
    }

    @CallSuper
    @Override
    protected void onDetach() {
        getChild().setTag(null);
        super.onDetach();
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        FrameLayout container = new FrameLayout(getActivity());

        View content = onCreateContent(inflater, container);
        getViewHolder().setContent(content);

        ViewGroup.LayoutParams contentLayoutParams = content.getLayoutParams();
        FrameLayout.LayoutParams newContentLayoutParams;
        if (contentLayoutParams == null) {
            newContentLayoutParams = generateContentDefaultLayoutParams();
        } else if (contentLayoutParams instanceof FrameLayout.LayoutParams) {
            newContentLayoutParams = (FrameLayout.LayoutParams) contentLayoutParams;
        } else {
            newContentLayoutParams = new FrameLayout.LayoutParams(contentLayoutParams.width, contentLayoutParams.height);
        }
        content.setLayoutParams(newContentLayoutParams);
        container.addView(content);

        return container;
    }

    @NonNull
    protected View onCreateContent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getConfig().mContentView != null) {
            ViewGroup contentParent = (ViewGroup) getConfig().mContentView.getParent();
            if (contentParent != null) {
                contentParent.removeView(getConfig().mContentView);
            }
            return getConfig().mContentView;
        }
        if (getConfig().mContentViewId != View.NO_ID) {
            return inflater.inflate(getConfig().mContentViewId, parent, false);
        }
        throw new IllegalStateException("未设置contentView");
    }

    @Override
    protected void onDestroyChild() {
        getViewHolder().getChild().removeAllViews();
        if (!isViewCacheable()) {
            getViewHolder().setContent(null);
        }
        super.onDestroyChild();
    }

    @NonNull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
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
            animator = AnimatorHelper.createZoomAlphaInAnim(view);
            animator.setDuration(mAnimDurDef);
        }
        return animator;
    }

    @NonNull
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        Animator animator = super.onCreateOutAnimator(view);
        if (animator == null) {
            animator = AnimatorHelper.createZoomAlphaOutAnim(view);
            animator.setDuration(mAnimDurDef);
        }
        return animator;
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
    public ToastLayer setRemoveOthers(boolean removeOthers) {
        getConfig().mRemoveOthers = removeOthers;
        return this;
    }

    @NonNull
    public ToastLayer setContentView(@NonNull View contentView) {
        getConfig().mContentView = contentView;
        return this;
    }

    @NonNull
    public ToastLayer setContentView(@LayoutRes int contentView) {
        getConfig().mContentViewId = contentView;
        return this;
    }

    @NonNull
    public ToastLayer setDuration(long duration) {
        getConfig().mDuration = duration;
        return this;
    }

    @NonNull
    public ToastLayer setMessage(@NonNull CharSequence message) {
        getConfig().mMessage = message;
        return this;
    }

    @NonNull
    public ToastLayer setMessage(int message) {
        getConfig().mMessage = getActivity().getString(message);
        return this;
    }

    @NonNull
    public ToastLayer setIcon(@DrawableRes int icon) {
        getConfig().mIcon = icon;
        return this;
    }

    @NonNull
    public ToastLayer setGravity(int gravity) {
        getConfig().mGravity = gravity;
        return this;
    }

    @NonNull
    public ToastLayer setMarginLeft(@Nullable Integer marginLeft) {
        getConfig().mMarginLeft = marginLeft;
        return this;
    }

    @NonNull
    public ToastLayer setMarginTop(@Nullable Integer marginTop) {
        getConfig().mMarginTop = marginTop;
        return this;
    }

    @NonNull
    public ToastLayer setMarginRight(@Nullable Integer marginRight) {
        getConfig().mMarginRight = marginRight;
        return this;
    }

    @NonNull
    public ToastLayer setMarginBottom(@Nullable Integer marginBottom) {
        getConfig().mMarginBottom = marginBottom;
        return this;
    }

    @NonNull
    public ToastLayer setAlpha(float alpha) {
        getConfig().mAlpha = alpha;
        return this;
    }

    @NonNull
    public ToastLayer setBackgroundDrawable(@NonNull Drawable drawable) {
        getConfig().mBackgroundDrawable = drawable;
        return this;
    }

    @NonNull
    public ToastLayer setBackgroundResource(@DrawableRes int resource) {
        getConfig().mBackgroundResource = resource;
        return this;
    }

    @NonNull
    public ToastLayer setBackgroundColorInt(@ColorInt int colorInt) {
        getConfig().mBackgroundColor = colorInt;
        return this;
    }

    @NonNull
    public ToastLayer setBackgroundColorRes(@ColorRes int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @NonNull
    public ToastLayer setTextColorInt(@ColorInt int colorInt) {
        getConfig().mTextColorInt = colorInt;
        return this;
    }

    @NonNull
    public ToastLayer setTextColorRes(@ColorRes int colorRes) {
        getConfig().mTextColorRes = colorRes;
        return this;
    }

    private class DismissRunnable implements Runnable {
        @Override
        public void run() {
            if (isShown()) {
                dismiss();
            }
        }
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private View mContent;

        @NonNull
        @Override
        public FrameLayout getChild() {
            return (FrameLayout) super.getChild();
        }

        @Nullable
        @Override
        protected FrameLayout getChildOrNull() {
            return (FrameLayout) super.getChildOrNull();
        }

        protected void setContent(@Nullable View content) {
            mContent = content;
        }

        @Nullable
        protected View getContentOrNull() {
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
        @Nullable
        private View mContentView = null;
        @LayoutRes
        private int mContentViewId = R.layout.anylayer_toast_content;
        private boolean mRemoveOthers = true;
        private long mDuration = 3000L;
        @NonNull
        private CharSequence mMessage = "";
        private int mIcon = -1;
        @Nullable
        private Drawable mBackgroundDrawable = null;
        private int mBackgroundResource = -1;
        private int mBackgroundColor = Color.TRANSPARENT;
        @ColorInt
        private int mTextColorInt = Color.TRANSPARENT;
        @ColorRes
        private int mTextColorRes = -1;
        private float mAlpha = 1;
        private int mGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        private Integer mMarginLeft = null;
        private Integer mMarginTop = null;
        private Integer mMarginRight = null;
        private Integer mMarginBottom = null;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
    }
}
