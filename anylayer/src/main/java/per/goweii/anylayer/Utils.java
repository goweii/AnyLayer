package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2018/10/25
 */
final class Utils {

    static <T> T requireNonNull(T obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
        return obj;
    }

    static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    static float floatRange01(float value){
        return floatRange(value, 0F, 1F);
    }

    static float floatRange(float value, float min, float max){
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    static int intRange(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

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

    static Bitmap snapshot(FrameLayout decor, ImageView iv, float scale, DecorLayer.Level level) {
        int w = iv.getWidth();
        int h = iv.getHeight();
        int oW = (int) (w / scale);
        int oH = (int) (h / scale);
        Bitmap bitmap = Bitmap.createBitmap(oW, oH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.save();
        int[] locationRootView = new int[2];
        decor.getLocationOnScreen(locationRootView);
        int[] locationBackground = new int[2];
        iv.getLocationOnScreen(locationBackground);
        int x = locationBackground[0] - locationRootView[0];
        int y = locationBackground[1] - locationRootView[1];
        canvas.scale(1 / scale, 1 / scale);
        canvas.translate(x / scale, y / scale);

        decor.getBackground().draw(canvas);
        out:
        for (int i = 0; i < decor.getChildCount(); i++) {
            View view = decor.getChildAt(i);
            if (view instanceof DecorLayer.LayerLayout) {
                DecorLayer.LayerLayout layerLayout = (DecorLayer.LayerLayout) view;
                for (int j = 0; j < layerLayout.getChildCount(); j++) {
                    View item = layerLayout.getChildAt(j);
                    if (item instanceof DecorLayer.LevelLayout) {
                        DecorLayer.LevelLayout levelLayout = (DecorLayer.LevelLayout) item;
                        if (levelLayout.getLevel().level() < level.level()) {
                            break out;
                        }
                    }
                }
            }
            view.draw(canvas);
        }
        canvas.restore();

        return bitmap;
    }

    static void transparent(Activity activity) {
        final Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
