package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.Objects;

import per.goweii.burred.Blurred;

/**
 * @author Cuizhen
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class AnyLayer {

    public static void initBlurred(Context context) {
        Utils.requestNonNull(context, "context == null");
        Blurred.init(context);
    }

    public static void recycleBlurred() {
        Blurred.recycle();
    }

    /**
     * 此时不需要APP存在Activity实例
     * 会新启动一个Activity并向根布局添加一个浮层
     */
    public static void dialog(LayerActivity.OnLayerCreatedCallback callback) {
        LayerActivity.start(ActivityHolder.getApplication(), callback);
    }

    /**
     * 向窗口根布局添加一个浮层
     */
    public static DialogLayer dialog() {
        return new DialogLayer(Objects.requireNonNull(ActivityHolder.getCurrentActivity()));
    }

    /**
     * 向窗口根布局添加一个浮层
     */
    public static DialogLayer dialog(Class<Activity> clazz) {
        Utils.requestNonNull(clazz, "clazz == null");
        return new DialogLayer(Objects.requireNonNull(ActivityHolder.getActivity(clazz)));
    }

    /**
     * 向窗口根布局添加一个浮层
     *
     * @param context 上下文，不能是ApplicationContext
     */
    public static DialogLayer dialog(Context context) {
        Utils.requestNonNull(context, "context == null");
        return new DialogLayer(context);
    }

    /**
     * 向窗口根布局添加一个浮层，且位置参照targetView
     * 及类似PopupWindow效果
     *
     * @param targetView 位置参照View
     */
    public static DialogLayer popup(View targetView) {
        Utils.requestNonNull(targetView, "targetView == null");
        return new DialogLayer(targetView);
    }

    public static ToastLayer toast() {
        return new ToastLayer(Objects.requireNonNull(ActivityHolder.getCurrentActivity()));
    }

    public static ToastLayer toast(Context context) {
        Utils.requestNonNull(context, "context == null");
        return new ToastLayer(context);
    }
}
