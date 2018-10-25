package per.goweii.anylayer.utils.blur;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

import java.io.FileNotFoundException;

import per.goweii.anylayer.utils.DisplayInfoUtils;

/**
 * 创建Bitmap 的帮助类
 *
 * @author CuiZhen
 * @date 2017/12/28
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class BitmapUtils {

    private BitmapUtils() {
        throw new UnsupportedOperationException("Cannot be instantiated");
    }

    /**
     * 从 uri 创建图片
     *
     * @param context   context
     * @param uri       图片的uri
     * @param fitScreen 是否适应屏幕宽度，设为true时将缩放图片到临近屏幕宽度，false时返回原图
     * @return Bitmap
     */
    public static Bitmap createFromUri(Context context, Uri uri, boolean fitScreen) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (fitScreen) {
            options.inJustDecodeBounds = true;
            try {
                BitmapFactory.decodeStream(
                        context.getContentResolver().openInputStream(uri), null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            setInSampleSize(context, options);
            options.inJustDecodeBounds = false;
        }
        try {
            return BitmapFactory.decodeStream(
                    context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从路径获取图片
     *
     * @param pathName 路径
     * @return Bitmap
     */
    public static Bitmap createFromPath(String pathName) {
        return BitmapFactory.decodeFile(pathName);
    }

    /**
     * 从资源文件创建图片
     *
     * @param resId 资源id
     * @return Bitmap
     */
    public static Bitmap createFromRes(@DrawableRes int resId) {
        return BitmapFactory.decodeResource(Resources.getSystem(), resId);
    }

    /**
     * 从字节数组创建图片
     *
     * @param bytes 字节数组
     * @return Bitmap
     */
    public static Bitmap createFromBytes(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 修改 BitmapFactory.Options 的 inSampleSize 值，进行图片缩小
     * 缩小图片到临近屏幕宽高（取缩小倍数最大的）
     *
     * @param context
     * @param options BitmapFactory.Options
     */
    private static void setInSampleSize(Context context, BitmapFactory.Options options) {
        DisplayInfoUtils displayInfoUtils = DisplayInfoUtils.getInstance(context);
        int displayWidth = displayInfoUtils.getWidthPixels();
        int displayHeight = displayInfoUtils.getHeightPixels();
        int widthSize = options.outWidth / displayWidth;
        int heightSize = options.outHeight / displayHeight;
        options.inSampleSize = widthSize > heightSize ? widthSize : heightSize;
    }

    public static Bitmap scale(@NonNull Bitmap bitmap, int newWidth, int newHeight) {
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

    public static Bitmap scale(@NonNull Bitmap bitmap, float scale) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
