package per.goweii.anylayer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.guide.GuideLayer;
import per.goweii.anylayer.notification.NotificationLayer;
import per.goweii.anylayer.overlay.OverlayLayer;
import per.goweii.anylayer.popup.PopupLayer;
import per.goweii.anylayer.toast.ToastLayer;

public final class AnyLayer {

    public static void init(@NonNull Application application) {
        ActivityHolder.init(application);
    }

    public static void dialog(@NonNull LayerActivity.OnLayerCreatedCallback callback) {
        LayerActivity.start(ActivityHolder.getApplication(), callback);
    }

    @NonNull
    public static DialogLayer dialog() {
        return new DialogLayer(ActivityHolder.requireCurrentActivity());
    }

    @NonNull
    public static DialogLayer dialog(@NonNull Class<Activity> clazz) {
        return new DialogLayer(ActivityHolder.requireActivity(clazz));
    }

    @NonNull
    public static DialogLayer dialog(@NonNull Context context) {
        return new DialogLayer(context);
    }

    @NonNull
    public static PopupLayer popup(@NonNull Context context) {
        return new PopupLayer(context);
    }

    @NonNull
    public static PopupLayer popup(@NonNull View targetView) {
        return new PopupLayer(targetView);
    }

    @NonNull
    public static ToastLayer toast() {
        return new ToastLayer(ActivityHolder.requireCurrentActivity());
    }

    @NonNull
    public static ToastLayer toast(@NonNull Context context) {
        return new ToastLayer(context);
    }

    @NonNull
    public static OverlayLayer overlay(@NonNull Context context) {
        return new OverlayLayer(context);
    }

    @NonNull
    public static GuideLayer guide(@NonNull Context context) {
        return new GuideLayer(context);
    }

    @NonNull
    public static NotificationLayer notification(@NonNull Context context) {
        return new NotificationLayer(context);
    }

}
