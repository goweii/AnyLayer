package per.goweii.anylayer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * 显示相关帮助类
 * 1.获取屏幕宽度
 * 2.获取屏幕高度
 * 3.获取屏幕密度相关
 * 4.获取状态栏高度
 * 5.dp/px/sp相互转换
 *
 * @author damai
 * @date 2017/12/28
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class DisplayInfoUtils {
    private final DisplayMetrics mDisplayMetrics;
    private final Context mContext;

    private DisplayInfoUtils(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        mDisplayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(mDisplayMetrics);
        mContext = activity;
    }

    private DisplayInfoUtils(Context context) {
        Resources resources = context.getResources();
        mDisplayMetrics = resources.getDisplayMetrics();
        mContext = context;
    }

    private DisplayInfoUtils() {
        mDisplayMetrics = Resources.getSystem().getDisplayMetrics();
        mContext = null;
    }

    public static DisplayInfoUtils getInstance() {
        return new DisplayInfoUtils();
    }

    public static DisplayInfoUtils getInstance(Activity activity) {
        return new DisplayInfoUtils(activity);
    }

    public static DisplayInfoUtils getInstance(Context context) {
        return new DisplayInfoUtils(context);
    }

    /**
     * 第一种：判断手机是否有物理按键，有就没有导航栏，反之就有（这个有点问题，逻辑不严谨，4.0以上所有手机都可以显示NavigationBar，只是手机厂家屏蔽了）。
     *
     * @param activity
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何自己需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    /**
     * 获取屏幕宽度像素
     *
     * @return px
     */
    public int getWidthPixels() {
        return mDisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度像素
     *
     * @return px
     */
    public int getHeightPixels() {
        return mDisplayMetrics.heightPixels;
    }

    /**
     * 获取屏幕像素密度(每英寸多少像素)
     *
     * @return dpi
     */
    public int getDensityDpi() {
        return mDisplayMetrics.densityDpi;
    }

    /**
     * 获取屏幕密度(像素密度/160)
     *
     * @return float
     */
    public float getDensity() {
        return mDisplayMetrics.density;
    }

    /**
     * 字体缩放比例（一般和屏幕密度相等）
     *
     * @return float
     */
    public float getScaledDensity() {
        return mDisplayMetrics.scaledDensity;
    }

    /**
     * X方向的像素密度
     *
     * @return dpi
     */
    public float getXdpi() {
        return mDisplayMetrics.xdpi;
    }

    /**
     * Y方向的像素密度
     *
     * @return dpi
     */
    public float getYdpi() {
        return mDisplayMetrics.ydpi;
    }

    /**
     * 获取状态栏高度像素
     *
     * @return px
     */
    public int getStatusBarHeight() {
        if (mContext == null) {
            int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return Resources.getSystem().getDimensionPixelSize(resourceId);
            }
            return 0;
        } else {
            int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return mContext.getResources().getDimensionPixelSize(resourceId);
            }
            return 0;
        }
    }

    /**
     * dp: dip，Density-independent pixel(设备独立像素), 是安卓开发用的长度单位，1dp表示在屏幕像素点密度为160ppi时1px长度
     * px: pixel，像素，电子屏幕上组成一幅图画或照片的最基本单元
     * dp转px
     *
     * @param dp dp
     * @return px
     */
    public int dp2px(int dp) {
        return (int) (dp * getDensity() + 0.5f);
    }

    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mDisplayMetrics);
    }

    /**
     * px: pixel，像素，电子屏幕上组成一幅图画或照片的最基本单元
     * dp: dip，Density-independent pixel(设备独立像素), 是安卓开发用的长度单位，1dp表示在屏幕像素点密度为160ppi时1px长度
     * px转dp
     *
     * @param px px
     * @return dp
     */
    public int px2dp(int px) {
        return (int) (px / getDensity() + 0.5f);
    }

    /**
     * sp: scale-independent pixel，放大的像素，安卓开发用的字体大小单位
     * px: pixel，像素，电子屏幕上组成一幅图画或照片的最基本单元
     * sp转px
     *
     * @param sp sp
     * @return px
     */
    public int sp2px(int sp) {
        return (int) (sp * getScaledDensity() + 0.5f);
    }

    public int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mDisplayMetrics);
    }

    /**
     * px: pixel，像素，电子屏幕上组成一幅图画或照片的最基本单元
     * sp: scale-independent pixel，放大的像素，安卓开发用的字体大小单位
     * px转sp
     *
     * @param px px
     * @return sp
     */
    public int px2sp(int px) {
        return (int) (px / getScaledDensity() + 0.5f);
    }

    /**
     * dp: dip，Density-independent pixel(设备独立像素), 是安卓开发用的长度单位，1dp表示在屏幕像素点密度为160ppi时1px长度
     * sp: scale-independent pixel，放大的像素，安卓开发用的字体大小单位
     * dp转sp
     *
     * @param dp dp
     * @return sp
     */
    public int dp2sp(int dp) {
        return (int) (dp * getDensity() / getScaledDensity() + 0.5f);
    }

    /**
     * sp: scale-independent pixel，放大的像素，安卓开发用的字体大小单位
     * dp: dip，Density-independent pixel(设备独立像素), 是安卓开发用的长度单位，1dp表示在屏幕像素点密度为160ppi时1px长度
     * sp转dp
     *
     * @param sp sp
     * @return dp
     */
    public int sp2dp(int sp) {
        return (int) (sp * getScaledDensity() / getDensity() + 0.5f);
    }
}
