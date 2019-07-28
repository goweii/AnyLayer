package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2018/10/25
 */
final class Utils {

    static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 从当前上下文获取Activity
     */
    @Nullable
    static Activity getActivity(Context context) {
        if (context == null) {
            return null;
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            if (baseContext instanceof Activity) {
                return (Activity) baseContext;
            }
        }
        return null;
    }

    static Bitmap snapshot(FrameLayout decor, int level) {
        Bitmap bitmap = Bitmap.createBitmap(decor.getWidth(), decor.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        decor.getBackground().draw(canvas);
        for (int i = 0; i < decor.getChildCount(); i++) {
            View view = decor.getChildAt(i);
            if (view instanceof DecorLayer.LevelLayout) {
                DecorLayer.LevelLayout levelLayout = (DecorLayer.LevelLayout) view;
                if (levelLayout.getLevel() < level) {
                    break;
                }
            }
            view.draw(canvas);
        }
        return bitmap;
    }
}
