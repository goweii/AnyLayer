package per.goweii.anylayer;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class NotificationLayer extends DecorLayer {

    public NotificationLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public NotificationLayer(@NonNull Activity activity) {
        super(activity);
        cancelableOnKeyBack(false);
    }

    @IntRange(from = 0)
    @Override
    protected int getLevel() {
        return Level.NOTIFICATION;
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
        if (getViewHolder().getChildOrNull() == null) {
            SwipeLayout container = (SwipeLayout) inflater.inflate(R.layout.anylayer_notification_layer, parent, false);
            getViewHolder().setChild(container);
        }
        return getViewHolder().getChild();
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

    @Override
    public void onAttach() {
        super.onAttach();
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
    }

    @Override
    public void onShow() {
        super.onShow();
    }

    @Override
    public void onPreRemove() {
        super.onPreRemove();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
        }

        @NonNull
        @Override
        public SwipeLayout getChild() {
            return (SwipeLayout) super.getChild();
        }

        @Nullable
        @Override
        protected SwipeLayout getChildOrNull() {
            return (SwipeLayout) super.getChildOrNull();
        }
    }

    protected static class Config extends DecorLayer.Config {
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
    }
}
