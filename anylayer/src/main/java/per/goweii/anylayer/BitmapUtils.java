package per.goweii.anylayer;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;

/**
 * 创建Bitmap 的帮助类
 *
 * @author CuiZhen
 * @date 2017/12/28
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
final class BitmapUtils {

    private BitmapUtils() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    static Bitmap scale(@NonNull Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        if (width != newWidth || height != newHeight) {
            float scaleWidth = newWidth / (float) width;
            float scaleHeight = newHeight / (float) height;
            matrix.setScale(scaleWidth, scaleHeight);
        }
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
