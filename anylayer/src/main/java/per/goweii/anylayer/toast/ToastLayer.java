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

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.DecorLayer;
import per.goweii.anylayer.GlobalConfig;
import per.goweii.anylayer.R;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.Utils;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class ToastLayer extends DecorLayer implements Runnable {

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

    @Override
    public void dismiss(boolean withAnim) {
        super.dismiss(withAnim);
    }

    @NonNull
    public ToastLayer removeOthers(boolean removeOthers) {
        getConfig().mRemoveOthers = removeOthers;
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
    public ToastLayer icon(int icon) {
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
    public ToastLayer backgroundDrawable(Drawable drawable) {
        getConfig().mBackgroundDrawable = drawable;
        return this;
    }

    @NonNull
    public ToastLayer backgroundDrawable(int drawableRes) {
        getConfig().mBackgroundDrawableRes = drawableRes;
        return this;
    }

    @NonNull
    public ToastLayer backgroundColorInt(int colorInt) {
        getConfig().mBackgroundColor = colorInt;
        return this;
    }

    @NonNull
    public ToastLayer backgroundColorRes(int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.anylayer_toast_layer, parent, false);
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

    private void bindData() {
        if (getConfig().mIcon > 0) {
            getViewHolder().getIcon().setVisibility(View.VISIBLE);
            getViewHolder().getIcon().setImageResource(getConfig().mIcon);
        } else {
            getViewHolder().getIcon().setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(getConfig().mMessage)) {
            getViewHolder().getMessage().setVisibility(View.GONE);
            getViewHolder().getMessage().setText("");
        } else {
            getViewHolder().getMessage().setVisibility(View.VISIBLE);
            getViewHolder().getMessage().setText(getConfig().mMessage);
        }
        if (getConfig().mBackgroundDrawable != null) {
            getChild().setBackgroundDrawable(getConfig().mBackgroundDrawable);
        } else if (getConfig().mBackgroundDrawableRes > 0) {
            getChild().setBackgroundResource(getConfig().mBackgroundDrawableRes);
        }
        getChild().getBackground().setColorFilter(getConfig().mBackgroundColor, PorterDuff.Mode.SRC_ATOP);
        getChild().setAlpha(getConfig().mAlpha);
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
    }

    @Override
    public void onAttach() {
        super.onAttach();
        getChild().setTag(this);
        if (getConfig().mRemoveOthers) {
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
        bindData();
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
    }

    @Override
    public void onShow() {
        super.onShow();
        if (getConfig().mDuration > 0) {
            getChild().postDelayed(this, getConfig().mDuration);
        }
    }

    @Override
    public void onPreRemove() {
        getChild().removeCallbacks(this);
        super.onPreRemove();
    }

    @Override
    public void onDetach() {
        getChild().setTag(null);
        super.onDetach();
    }

    @Override
    public void run() {
        if (isShown()) {
            dismiss();
        }
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private ImageView mIcon;
        private TextView mMessage;

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
            mIcon = child.findViewById(R.id.anylayler_iv_icon);
            mMessage = child.findViewById(R.id.anylayler_tv_msg);
        }

        @NonNull
        public ImageView getIcon() {
            return mIcon;
        }

        @NonNull
        public TextView getMessage() {
            return mMessage;
        }
    }

    protected static class Config extends DecorLayer.Config {
        private boolean mRemoveOthers = true;
        private long mDuration = GlobalConfig.get().toastDuration;
        @NonNull
        private CharSequence mMessage = "";
        private int mIcon = 0;
        @Nullable
        private Drawable mBackgroundDrawable = null;
        private int mBackgroundDrawableRes = GlobalConfig.get().toastBackgroundRes;
        private int mBackgroundColor = Color.TRANSPARENT;
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
