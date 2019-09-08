package per.goweii.anylayer;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class ToastLayer extends DecorLayer implements Runnable {

    public ToastLayer(Context context) {
        super(Utils.getActivity(Utils.requireNonNull(context, "context == null")));
        interceptKeyEvent(false);
        cancelableOnKeyBack(false);
    }

    @Override
    protected Level getLevel() {
        return Level.TOAST;
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
    protected ListenerHolder onCreateListenerHolder() {
        return new ListenerHolder();
    }

    @Override
    public ListenerHolder getListenerHolder() {
        return (ListenerHolder) super.getListenerHolder();
    }

    @Override
    public void dismiss(boolean withAnim) {
        super.dismiss(withAnim);
    }

    public ToastLayer removeOthers(boolean removeOthers) {
        getConfig().mRemoveOthers = removeOthers;
        return this;
    }

    public ToastLayer duration(long duration) {
        getConfig().mDuration = duration;
        return this;
    }

    public ToastLayer message(CharSequence message) {
        Utils.requireNonNull(message, "message == null");
        getConfig().mMessage = message;
        return this;
    }

    public ToastLayer message(int message) {
        getConfig().mMessage = getActivity().getString(message);
        return this;
    }

    public ToastLayer icon(int icon) {
        getConfig().mIcon = icon;
        return this;
    }

    public ToastLayer gravity(int gravity) {
        getConfig().mGravity = gravity;
        return this;
    }

    public ToastLayer marginLeft(int marginLeft) {
        getConfig().mMarginLeft = marginLeft;
        return this;
    }

    public ToastLayer marginTop(int marginTop) {
        getConfig().mMarginTop = marginTop;
        return this;
    }

    public ToastLayer marginRight(int marginRight) {
        getConfig().mMarginRight = marginRight;
        return this;
    }

    public ToastLayer marginBottom(int marginBottom) {
        getConfig().mMarginBottom = marginBottom;
        return this;
    }

    public ToastLayer alpha(float alpha) {
        getConfig().mAlpha = alpha;
        return this;
    }

    public ToastLayer backgroundDrawable(Drawable drawable) {
        getConfig().mBackgroundDrawable = drawable;
        return this;
    }

    public ToastLayer backgroundDrawable(int drawableRes) {
        getConfig().mBackgroundDrawableRes = drawableRes;
        return this;
    }

    public ToastLayer backgroundColorInt(int colorInt) {
        getConfig().mBackgroundColor = colorInt;
        return this;
    }

    public ToastLayer backgroundColorRes(int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    @Override
    protected View onCreateChild(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.anylayer_toast_layer, parent, false);
    }

    @Override
    protected Animator onCreateInAnimator(View view) {
        Animator animator = super.onCreateInAnimator(view);
        if (animator == null) {
            animator = AnimatorHelper.createZoomAlphaInAnim(view);
        }
        return animator;
    }

    @Override
    protected Animator onCreateOutAnimator(View view) {
        Animator animator = super.onCreateOutAnimator(view);
        if (animator == null) {
            animator = AnimatorHelper.createZoomAlphaOutAnim(view);
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
        } else if (getConfig().mBackgroundDrawableRes != -1) {
            getChild().setBackgroundResource(getConfig().mBackgroundDrawableRes);
        } else {
            Drawable backgroundDrawable = getChild().getBackground();
            if (backgroundDrawable instanceof GradientDrawable) {
                backgroundDrawable.setColorFilter(getConfig().mBackgroundColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
        getChild().setAlpha(getConfig().mAlpha);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getChild().getLayoutParams();
        params.gravity = getConfig().mGravity;
        if (getConfig().mMarginLeft >= 0) {
            params.leftMargin = getConfig().mMarginLeft;
        }
        if (getConfig().mMarginTop >= 0) {
            params.topMargin = getConfig().mMarginTop;
        }
        if (getConfig().mMarginRight >= 0) {
            params.rightMargin = getConfig().mMarginRight;
        }
        if (getConfig().mMarginBottom >= 0) {
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
        if (isShow()) {
            dismiss();
        }
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private ImageView mIcon;
        private TextView mMessage;

        @Override
        public void setChild(View child) {
            super.setChild(child);
            mIcon = child.findViewById(R.id.iv_icon);
            mMessage = child.findViewById(R.id.tv_msg);
        }

        public ImageView getIcon() {
            return mIcon;
        }

        public TextView getMessage() {
            return mMessage;
        }
    }

    protected static class Config extends DecorLayer.Config {
        private boolean mRemoveOthers = true;
        private long mDuration = 3000L;
        private CharSequence mMessage = "";
        private int mIcon = 0;
        private int mBackgroundDrawableRes = -1;
        private Drawable mBackgroundDrawable = null;
        private int mBackgroundColor = Color.BLACK;
        private float mAlpha = 1F;
        private int mGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        private int mMarginLeft = -1;
        private int mMarginTop = -1;
        private int mMarginRight = -1;
        private int mMarginBottom = -1;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
    }
}
