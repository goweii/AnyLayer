package per.goweii.anylayer;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private long mDuration = 3000L;
    private CharSequence message = "";

    public ToastLayer(@NonNull Context context) {
        super(Objects.requireNonNull(Utils.getActivity(context)));
        interceptKeyEvent(false);
        cancelableOnKeyDown(false);
    }

    @Level
    @Override
    protected int getLevel() {
        return Level.TOAST;
    }

    public ToastLayer duration(long duration) {
        mDuration = duration;
        return this;
    }

    public ToastLayer message(@NonNull CharSequence message) {
        this.message = message;
        return this;
    }

    @NonNull
    @Override
    protected View onCreateLayer(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
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
        TextView tvMsg = getViewManager().getChild().findViewById(R.id.tv_msg);
        tvMsg.setText(message);
    }

    @Override
    public void onShow() {
        super.onShow();
        if (mDuration > 0) {
            getViewManager().getChild().postDelayed(this, mDuration);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getViewManager().getChild().removeCallbacks(this);
    }

    @Override
    public void run() {
        dismiss();
    }
}
