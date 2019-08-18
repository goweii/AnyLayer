package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import per.goweii.burred.Blurred;

/**
 * @author Cuizhen
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class AnyLayer {

    public static void initBlurred(Context context) {
        Blurred.init(context);
    }

    public static void recycleBlurred() {
        Blurred.recycle();
    }

    public static void dialog(LayerActivity.OnLayerCreatedCallback callback) {
        LayerActivity.start(ActivityHolder.getApplication(), callback);
    }

    public static DialogLayer dialog() {
        return new DialogLayer(ActivityHolder.getCurrentActivity());
    }

    public static DialogLayer dialog(Class<Activity> clazz) {
        return new DialogLayer(ActivityHolder.getActivity(clazz));
    }

    public static DialogLayer dialog(Context context) {
        return new DialogLayer(context);
    }

    public static PopupLayer popup(View targetView) {
        return new PopupLayer(targetView);
    }

    public static ToastLayer toast() {
        return new ToastLayer(ActivityHolder.getCurrentActivity());
    }

    public static ToastLayer toast(Context context) {
        return new ToastLayer(context);
    }
}
