package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

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

    /**
     * 此时不需要APP存在Activity实例
     * 会新启动一个Activity并向根布局添加一个浮层
     */
    public static void with(LayerActivity.OnLayerCreatedCallback callback) {
        LayerActivity.start(ActivityHolder.getApplication(), callback);
    }

    /**
     * 向窗口根布局添加一个浮层
     */
    public static DialogLayer with() {
        Activity activity = ActivityHolder.getCurrentActivity();
        if (activity == null) {
            return null;
        }
        return new DialogLayer(activity);
    }

    /**
     * 向窗口根布局添加一个浮层
     */
    public static DialogLayer with(@NonNull Class<Activity> clazz) {
        Activity activity = ActivityHolder.getActivity(clazz);
        if (activity == null) {
            return null;
        }
        return new DialogLayer(activity);
    }

    /**
     * 向父布局viewGroup添加一个浮层
     *
     * @param viewGroup 浮层父布局
     */
    public static DialogLayer with(@NonNull ViewGroup viewGroup) {
        return new DialogLayer(viewGroup);
    }

    /**
     * 向窗口根布局添加一个浮层
     *
     * @param context 上下文，不能是ApplicationContext
     */
    public static DialogLayer with(@NonNull Context context) {
        return new DialogLayer(context);
    }

    /**
     * 向窗口根布局添加一个浮层，且位置参照targetView
     * 及类似PopupWindow效果
     *
     * @param targetView 位置参照View
     */
    public static DialogLayer target(@NonNull View targetView) {
        return new DialogLayer(targetView);
    }
}
