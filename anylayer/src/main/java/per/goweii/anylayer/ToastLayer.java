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

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class ToastLayer extends Layer {

    private long mDuration = 3000L;

    private final Context mContext;

    public ToastLayer(@NonNull Context context) {
        mContext = context;
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
        return null;
    }

    @Nullable
    @Override
    protected Animator onCreateViewInAnimator(@NonNull View view) {
        return null;
    }

    @Nullable
    @Override
    protected Animator onCreateViewOutAnimator(@NonNull View view) {
        return null;
    }
}
