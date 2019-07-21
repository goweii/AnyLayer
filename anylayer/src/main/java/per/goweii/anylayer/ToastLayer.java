package per.goweii.anylayer;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class ToastLayer extends Layer {

    public ToastLayer(@NonNull ViewGroup parent) {
        super(parent);
    }

//    public ToastLayer(@Nullable Context context) {
//        Activity activity = Utils.getActivity(context);
//        if (activity == null) {
//            activity = ActivityHolder.getCurrentActivity();
//        }
//        FrameLayout rootView = (FrameLayout) activity.getWindow().getDecorView();
//        this(rootView);
//    }

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
