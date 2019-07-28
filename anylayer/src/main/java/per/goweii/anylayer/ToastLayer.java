package per.goweii.anylayer;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class ToastLayer extends DecorLayer implements Runnable {

    private boolean mRemoveOthers = true;
    private long mDuration = 3000L;
    private CharSequence mMessage = "";
    private int mIcon = 0;

    public ToastLayer(@NonNull Context context) {
        super(Objects.requireNonNull(Utils.getActivity(context)));
        interceptKeyEvent(false);
        cancelableOnKeyBack(false);
    }

    @Override
    public void show() {
        super.show();
    }

    @Level
    @Override
    protected int getLevel() {
        return Level.TOAST;
    }

    public ToastLayer removeOthers(boolean removeOthers) {
        mRemoveOthers = removeOthers;
        return this;
    }

    public ToastLayer duration(long duration) {
        mDuration = duration;
        return this;
    }

    public ToastLayer message(@NonNull CharSequence message) {
        mMessage = message;
        return this;
    }

    public ToastLayer message(@StringRes int message) {
        mMessage = getActivity().getString(message);
        return this;
    }

    public ToastLayer icon(@DrawableRes int icon) {
        mIcon = icon;
        return this;
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.anylayer_toast_layer, parent, false);
    }

    @Nullable
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        return AnimatorHelper.createLeftInAnim(view);
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        return AnimatorHelper.createLeftOutAnim(view);
    }

    @Override
    public void onAttach() {
        super.onAttach();
        if (mRemoveOthers) {
            final ViewGroup parent = getParent();
            final int count = parent.getChildCount();
            if (count > 1) {
                parent.removeViews(0, count - 1);
            }
        }
        final ImageView ivIcon = getView(R.id.iv_icon);
        if (mIcon > 0) {
            ivIcon.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(mIcon);
        } else {
            ivIcon.setVisibility(View.GONE);
        }
        final TextView tvMsg = getView(R.id.tv_msg);
        if (TextUtils.isEmpty(mMessage)) {
            tvMsg.setVisibility(View.GONE);
            tvMsg.setText("");
        } else {
            tvMsg.setVisibility(View.VISIBLE);
            tvMsg.setText(mMessage);
        }
    }

    @Override
    public void onShow() {
        super.onShow();
        if (mDuration > 0) {
            getChild().postDelayed(this, mDuration);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getChild().removeCallbacks(this);
    }

    @Override
    public void run() {
        dismiss();
    }
}
