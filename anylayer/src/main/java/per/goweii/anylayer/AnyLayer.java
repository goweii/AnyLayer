package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import android.support.annotation.NonNull;

import per.goweii.burred.Blurred;

/**
 * @author Cuizhen
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class AnyLayer {

    public static void initBlurred(@NonNull Context context) {
        Blurred.init(context);
    }

    public static void recycleBlurred() {
        Blurred.recycle();
    }

    public static void dialog(@NonNull LayerActivity.OnLayerCreatedCallback callback) {
        LayerActivity.start(ActivityHolder.getApplication(), callback);
    }

    @NonNull
    public static DialogLayer dialog() {
        Activity activity = ActivityHolder.getCurrentActivity();
        Utils.requireNonNull(activity, "请确保有已启动的Activity实例");
        return new DialogLayer(activity);
    }

    @NonNull
    public static DialogLayer dialog(@NonNull Class<Activity> clazz) {
        Activity activity = ActivityHolder.getCurrentActivity();
        Utils.requireNonNull(activity, "请确保有已启动的Activity实例");
        return new DialogLayer(activity);
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
        Activity activity = ActivityHolder.getCurrentActivity();
        Utils.requireNonNull(activity, "请确保有已启动的Activity实例");
        return new ToastLayer(activity);
    }

    @NonNull
    public static ToastLayer toast(@NonNull Context context) {
        return new ToastLayer(context);
    }
}
