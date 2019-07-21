package per.goweii.anylayer;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class ToastLayer extends Layer implements Runnable {

    private final Context mContext;

    private long mDuration = 3000L;
    private CharSequence message = "";

    public ToastLayer(@NonNull Context context) {
        mContext = context;
        interceptKeyEvent(false);
        cancelableOnKeyDown(false);
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
    protected ViewGroup onGetParent() {
        Activity activity = Utils.getActivity(mContext);
        if (activity == null) {
            activity = ActivityHolder.getCurrentActivity();
        }
        if (activity == null) {
            throw new RuntimeException("");
        }
        return (FrameLayout) activity.getWindow().getDecorView();
    }

    @NonNull
    @Override
    protected View onCreateView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.anylayer_toast_layer, parent, false);
    }

    @Nullable
    @Override
    protected Animator onCreateViewInAnimator(@NonNull View view) {
        return AnimatorHelper.createLeftInAnim(view);
    }

    @Nullable
    @Override
    protected Animator onCreateViewOutAnimator(@NonNull View view) {
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
