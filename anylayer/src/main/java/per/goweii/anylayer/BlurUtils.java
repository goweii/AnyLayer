package per.goweii.anylayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

/**
 * @author CuiZhen
 * @date 2018/4/27
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
final class BlurUtils {

    static Bitmap blur(Context context, Bitmap originalBitmap, float radius){
        if (radius <= 0){
            return originalBitmap;
        }
        if (context == null){
            return FastBlur.blur(originalBitmap, (int) radius);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return GaussianBlur.blur(context, originalBitmap, radius);
        } else {
            return FastBlur.blur(originalBitmap, (int) radius);
        }
    }

    static Bitmap blur(Context context, Bitmap originalBitmap, float radius, float scaleFactor){
        if (radius <= 0){
            return originalBitmap;
        }
        if (scaleFactor <= 1){
           return blur(context, originalBitmap, radius);
        }
        if (context == null){
            return FastBlur.blur(originalBitmap, (int) radius, scaleFactor);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return GaussianBlur.blur(context, originalBitmap, radius, scaleFactor);
        } else {
            return FastBlur.blur(originalBitmap, (int) radius, scaleFactor);
        }
    }
}
