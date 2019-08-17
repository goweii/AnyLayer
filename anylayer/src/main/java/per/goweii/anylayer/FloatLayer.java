package per.goweii.anylayer;

import android.animation.Animator;
import android.content.Context;
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
public class FloatLayer extends DecorLayer {

    public FloatLayer(Context context) {
        super(Utils.getActivity(Utils.requireNonNull(context, "context == null")));
        interceptKeyEvent(false);
        cancelableOnKeyBack(false);
    }

    @Override
    protected Level getLevel() {
        return Level.FLOAT;
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
    public void show() {
        super.show();
    }

    @Override
    protected View onCreateChild(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    protected Animator onCreateInAnimator(View view) {
        return AnimatorHelper.createZoomAlphaInAnim(view);
    }

    @Override
    protected Animator onCreateOutAnimator(View view) {
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
