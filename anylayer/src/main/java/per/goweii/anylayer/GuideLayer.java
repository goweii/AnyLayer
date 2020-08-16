package per.goweii.anylayer;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class GuideLayer extends DecorLayer {

    public GuideLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public GuideLayer(@NonNull Activity activity) {
        super(activity);
        cancelableOnKeyBack(false);
    }

    @NonNull
    @Override
    protected Level getLevel() {
        return Level.GUIDE;
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
        throw new UnsupportedOperationException("未实现");
    }

    @Nullable
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        return AnimatorHelper.createZoomAlphaInAnim(view);
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        return AnimatorHelper.createZoomAlphaOutAnim(view);
    }

    private void bindData() {
    }

    @Override
    public void onAttach() {
        super.onAttach();
        bindData();
    }

    @Override
    public void onShow() {
        super.onShow();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
    }

    protected static class Config extends DecorLayer.Config {
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
    }
}
