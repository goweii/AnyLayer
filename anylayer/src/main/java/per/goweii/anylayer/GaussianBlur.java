package per.goweii.anylayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.FloatRange;
import android.support.annotation.RequiresApi;

/**
 * @author CuiZhen
 * @date 2018/4/4
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
final class GaussianBlur {
    /**
     * 模糊
     * 采用系统自带的RenderScript
     * 输出图与原图参数相同
     * 模糊半径小于0则返回原图，大于25则进行缩放再模糊
     *
     * @param context        Context
     * @param originalBitmap 原图
     * @param radius         模糊半径（小于0则返回原图，大于25则进行缩放再模糊）
     * @return 模糊Bitmap
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static Bitmap blur(Context context, Bitmap originalBitmap, float radius) {
        if (radius <= 0) {
            return originalBitmap;
        }
        if (radius <= 25) {
            return blurIn25(context, originalBitmap, radius);
        }
        return blur(context, originalBitmap, 25, (radius / 25));
    }

    /**
     * 模糊
     * 采用系统自带的RenderScript
     * 输出图与原图参数相同
     *
     * @param context        Context
     * @param originalBitmap 原图
     * @param scaleFactor    缩放因子（>=1）
     * @param radius         模糊半径
     * @return 模糊Bitmap
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static Bitmap blur(Context context, Bitmap originalBitmap,
                              @FloatRange(fromInclusive = false, from = 0.0, to = 25.0) float radius,
                              @FloatRange(from = 1) float scaleFactor) {
        if (scaleFactor == 1){
            return blur(context, originalBitmap, radius);
        }
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        // 创建输入图片
        Bitmap input = BitmapUtils.scale(originalBitmap, (int) (width / scaleFactor), (int) (height / scaleFactor));
        Bitmap output = blur(context, input, radius);
        return BitmapUtils.scale(output, width, height);
    }

    /**
     * 高斯模糊
     * 采用系统自带的RenderScript
     * 图像越大耗时越长，测试时1280*680的图片耗时在30~60毫秒
     * 建议在子线程模糊通过Handler回调获取
     *
     * @param context        Context
     * @param originalBitmap 原图
     * @param radius         模糊半径
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    static Bitmap blurIn25(Context context, Bitmap originalBitmap,
                              @FloatRange(fromInclusive = false, from = 0.0, to = 25.0) float radius) {
        // 创建输出图片
        Bitmap blurBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), originalBitmap.getConfig());
        // 构建一个RenderScript对象
        RenderScript renderScript = RenderScript.create(context);
        // 创建高斯模糊脚本
        ScriptIntrinsicBlur gaussianBlue = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        // 开辟输入内存
        Allocation allIn = Allocation.createFromBitmap(renderScript, originalBitmap);
        // 开辟输出内存
        Allocation allOut = Allocation.createFromBitmap(renderScript, blurBitmap);
        // 设置模糊半径，范围0f<radius<=25f
        gaussianBlue.setRadius(radius);
        // 设置输入内存
        gaussianBlue.setInput(allIn);
        // 模糊编码，并将内存填入输出内存
        gaussianBlue.forEach(allOut);
        // 将输出内存编码为Bitmap，图片大小必须注意
        allOut.copyTo(blurBitmap);
        // 关闭RenderScript对象
        renderScript.destroy();
        return blurBitmap;
    }
}
