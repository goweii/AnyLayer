# AnyLayer

用于替代Android自带Dialog和PopupWindow

[GitHub主页](https://github.com/goweii/AnyLayer)

[Demo下载](https://github.com/goweii/AnyLayer/raw/master/app/release/app-release.apk)

# 简介

- 链式调用
- 可实现dialog效果
- 可实现popupWindow效果
- 可实现悬浮菜单效果
- 可自定义数据绑定
- 可自定义进出场动画
- 可自由控制显示大小和位置
- 可自定义背景变暗或者显示图片
- 可一行代码实现背景高斯模糊
- 占用区域不会超过当前Activity，避免导航栏遮挡问题



# 说明

可以看到我的GitHub上面有3个功能比较类似的可以实现弹窗的框架。分别为AnyDialog、AnyLayer和FloatingLayer。可能大家有点混淆，为什么同一个功能要写3个框架？为了骗点击数？在这里向大家说明下每个框架的针对点。

首先是AnyDialog，他继承自Dialog，目的是方便Dialog的创建和使用。

再者是FloatingLayer，他是AnyLayer的一个最初版，写他的目的是因为有时候Dialog会被导航栏遮挡，或者Dialog的出现消失动画会经过黑色的导航栏区域，看着很不爽，很别扭。所以就想如果Dialog的范围能和当前的Activity的显示范围一样就好了，因此写了一个FloatingLayer，让他依附于Activity，控制一个View的动态添加和移除。

最后是AnyLayer，随着FloatingLayer的完善，我重构了代码，分离出了3个关键类，让他不再仅为实现Dialog效果，还可以实现PopupWindow和悬浮菜单等效果。因此我觉得有必要给他一个新的名字了，也是为了和AnyDialog相呼应，因此定名为AnyLayer。

所以可以看出，FloatingLayer其实已经停止维护，AnyDialog还是建议用于普通的Dialog弹窗，而AnyLayer则是去替代一些浮动窗口的显示。



# 截图

截图效果较差，建议[下载Demo](https://github.com/goweii/AnyDialog/raw/master/app/release/app-release.apk)体验

![anylayer.gif](https://upload-images.jianshu.io/upload_images/9231307-9b9fdd9b345c3167.gif?imageMogr2/auto-orient/strip)


# 使用说明

## 集成

- ### 添加jitpack库

```java
// build.gradle(Project:)
allprojects {
    repositories {
        ...
            maven { url 'https://www.jitpack.io' }
    }
}
```

- ### 添加依赖

  [点击查看最新版本号](https://github.com/goweii/AnyLayer/releases)

```java
// build.gradle(Module:)
dependencies {
    implementation 'com.github.goweii:AnyLayer:最新版本号'
}
```

## 调用

### Dialog效果

```java
AnyLayer.with(MainActivity.this)
    .contentView(R.layout.dialog_test_3)
    .backgroundColorRes(R.color.dialog_bg)
    .gravity(Gravity.BOTTOM)
    .cancelableOnTouchOutside(true)
    .cancelableOnClickKeyBack(true)
    .contentAnim(new AnyLayer.IAnim() {
        @Override
        public long inAnim(View content) {
        AnimHelper.startBottomInAnim(content, ANIM_DURATION);
           return ANIM_DURATION;
        }

        @Override
        public long outAnim(View content) {
            AnimHelper.startBottomOutAnim(content, ANIM_DURATION);
            return ANIM_DURATION;
        }
    })
    .onClick(R.id.fl_dialog_no, new AnyLayer.OnLayerClickListener() {
        @Override
        public void onClick(AnyLayer AnyLayer, View v) {
            AnyLayer.dismiss();
        }
    })
    .show();
```

### PopupWindow效果

```java
AnyLayer.target(findViewById(R.id.tv_show_target_bottom))
        .direction(AnyLayer.Direction.BOTTOM)
        .contentView(R.layout.dialog_test_4)
        .backgroundColorRes(R.color.dialog_bg)
        .gravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL)
        .cancelableOnTouchOutside(true)
        .cancelableOnClickKeyBack(true)
        .contentAnim(new AnyLayer.IAnim() {
            @Override
            public long inAnim(View content) {
                AnimHelper.startTopInAnim(content, ANIM_DURATION);
                return ANIM_DURATION;
            }

            @Override
            public long outAnim(View content) {
                AnimHelper.startTopOutAnim(content, ANIM_DURATION);
                return ANIM_DURATION;
            }
        })
        .show();
```



# 常用方法

```java
	/**
     * 向父布局viewGroup添加一个浮层
     *
     * @param viewGroup 浮层父布局
     */
    public static AnyLayer with(@NonNull ViewGroup viewGroup)

    /**
     * 向窗口根布局添加一个浮层
     *
     * @param context 上下文，不能是ApplicationContext
     */
    public static AnyLayer with(@NonNull Context context)

    /**
     * 向窗口根布局添加一个浮层，且位置参照targetView
     * 及类似PopupWindow效果
     *
     * @param targetView 位置参照View
     */
    public static AnyLayer target(@NonNull View targetView)

    /**
     * 设置自自定义View
     *
     * @param contentView 自定以View
     */
    public AnyLayer contentView(@NonNull View contentView)

    /**
     * 设置自定义布局文件
     *
     * @param contentViewId 自定以布局ID
     */
    public AnyLayer contentView(@LayoutRes int contentViewId)

    /**
     * 设置自定义布局文件中状态栏的占位View
     * 该控件高度将设置为状态栏高度，可用来使布局整体下移，避免状态栏遮挡
     *
     * @param statusBarId 状态栏的占位View
     */
    public AnyLayer asStatusBar(@IdRes int statusBarId)

    /**
     * 绑定数据
     * 获取子控件ID为{@link #getView(int)}
     *
     * @param dataBinder 实现该接口进行数据绑定
     */
    public AnyLayer bindData(IDataBinder dataBinder)

    /**
     * 设置显示状态改变的监听
     *
     * @param mOnVisibleChangeListener OnVisibleChangeListener
     */
    public AnyLayer onVisibleChangeListener(OnVisibleChangeListener mOnVisibleChangeListener)

    /**
     * 设置变更为显示状态监听
     *
     * @param onLayerShowListener OnLayerShowListener
     */
    public AnyLayer onLayerShowListener(OnLayerShowListener onLayerShowListener)

    /**
     * 设置变更为隐藏状态监听
     *
     * @param onLayerDismissListener OnLayerDismissListener
     */
    public AnyLayer onLayerDismissListener(OnLayerDismissListener onLayerDismissListener)

    /**
     * 设置子布局的gravity
     * 可直接在布局文件指定layout_gravity属性，作用相同
     *
     * @param gravity {@link Gravity}
     */
    public AnyLayer gravity(int gravity)

    /**
     * 当以target方式创建时为参照View位置显示
     * 可自己指定浮层相对于参照View的对齐方式
     *
     * @param direction  主方向
     * @param horizontal 水平对齐方式
     * @param vertical   垂直对齐方式
     * @param inside     是否强制位于屏幕内部
     */
    public AnyLayer alignment(@NonNull Alignment.Direction direction,
                              @NonNull Alignment.Horizontal horizontal,
                              @NonNull Alignment.Vertical vertical,
                              boolean inside)

    /**
     * 自定义浮层的进入和退出动画
     * 可使用工具类{@link AnimHelper}
     *
     * @param contentAnim IAnim接口
     */
    public AnyLayer contentAnim(IAnim contentAnim)

    /**
     * 自定义浮层的进入动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer contentInAnim(@AnimRes int anim)

    /**
     * 自定义浮层的进入动画
     *
     * @param anim Animation动画
     */
    public AnyLayer contentInAnim(@NonNull Animation anim)

    /**
     * 自定义浮层的退出动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer contentOutAnim(@AnimRes int anim)
    /**
     * 自定义浮层的退出动画
     *
     * @param anim Animation动画
     */
    public AnyLayer contentOutAnim(@NonNull Animation anim)

    /**
     * 自定义背景的进入和退出动画
     * 可使用工具类{@link AnimHelper}
     *
     * @param backgroundAnim IAnim接口
     */
    public AnyLayer backgroundAnim(IAnim backgroundAnim)

    /**
     * 自定义背景的进入动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer backgroundInAnim(@AnimRes int anim)

    /**
     * 自定义背景的退出动画
     *
     * @param anim Animation动画
     */
    public AnyLayer backgroundInAnim(@NonNull Animation anim)

    /**
     * 自定义背景的退出动画
     *
     * @param anim 动画资源文件ID
     */
    public AnyLayer backgroundOutAnim(@AnimRes int anim)

    /**
     * 自定义背景的退出动画
     *
     * @param anim Animation动画
     */
    public AnyLayer backgroundOutAnim(@NonNull Animation anim)

    /**
     * 默认浮层进入和退出动画时长
     *
     * @param defaultAnimDuration 时长
     */
    public AnyLayer defaultContentAnimDuration(long defaultAnimDuration)

    /**
     * 默认浮层进入动画时长
     *
     * @param defaultInAnimDuration 时长
     */
    public AnyLayer defaultContentInAnimDuration(long defaultInAnimDuration)

    /**
     * 默认浮层退出动画时长
     *
     * @param defaultOutAnimDuration 时长
     */
    public AnyLayer defaultContentOutAnimDuration(long defaultOutAnimDuration)

    /**
     * 默认背景进入和退出动画时长
     *
     * @param defaultAnimDuration 时长
     */
    public AnyLayer defaultBackgroundAnimDuration(long defaultAnimDuration)

    /**
     * 默认背景进入动画时长
     *
     * @param defaultInAnimDuration 时长
     */
    public AnyLayer defaultBackgroundInAnimDuration(long defaultInAnimDuration)

    /**
     * 默认背景退出动画时长
     *
     * @param defaultOutAnimDuration 时长
     */
    public AnyLayer defaultBackgroundOutAnimDuration(long defaultOutAnimDuration)

    /**
     * 设置背景为当前activity的高斯模糊效果
     * 设置之后其他背景设置方法失效，仅{@link #backgroundColorInt(int)}生效
     * 且设置的backgroundColor值调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时的{@link #backgroundColorInt(int)}传入的为半透明颜色
     *
     * @param radius 模糊半径
     */
    public AnyLayer backgroundBlurRadius(@FloatRange(from = 0, fromInclusive = false, to = 25) float radius)

    /**
     * 设置背景高斯模糊的缩小比例
     *
     * @param scale 缩小比例
     */
    public AnyLayer backgroundBlurScale(@FloatRange(from = 1) float scale)

    /**
     * 设置背景图片
     *
     * @param bitmap 图片
     */
    public AnyLayer backgroundBitmap(@NonNull Bitmap bitmap)

    /**
     * 设置背景资源
     *
     * @param resource 资源ID
     */
    public AnyLayer backgroundResource(@DrawableRes int resource)

    /**
     * 设置背景Drawable
     *
     * @param drawable Drawable
     */
    public AnyLayer backgroundDrawable(@NonNull Drawable drawable)

    /**
     * 设置背景颜色
     * 在调用了{@link #backgroundBitmap(Bitmap)}或者{@link #backgroundBlurRadius(float)}方法后
     * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时传入的颜色为半透明颜色
     *
     * @param colorInt 颜色值
     */
    public AnyLayer backgroundColorInt(@ColorInt int colorInt)

    /**
     * 设置背景颜色
     * 在调用了{@link #backgroundBitmap(Bitmap)}或者{@link #backgroundBlurRadius(float)}方法后
     * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
     * 建议此时传入的颜色为半透明颜色
     *
     * @param colorRes 颜色资源ID
     */
    public AnyLayer backgroundColorRes(@ColorRes int colorRes)

    /**
     * 设置点击浮层以外区域是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    public AnyLayer cancelableOnTouchOutside(boolean cancelable)

    /**
     * 设置点击返回键是否可关闭
     *
     * @param cancelable 是否可关闭
     */
    public AnyLayer cancelableOnClickKeyBack(boolean cancelable)

    /**
     * 对一个View绑定点击事件
     *
     * @param viewId   控件ID
     * @param listener 监听器
     */
    public AnyLayer onClick(@IdRes int viewId, OnLayerClickListener listener)

    /**
     * 对多个View绑定点击事件
     *
     * @param listener 监听器
     * @param viewId   控件ID
     * @param viewIds  控件ID
     */
    public AnyLayer onClick(OnLayerClickListener listener, @IdRes int viewId, @IdRes int... viewIds)

    /**
     * 绑定该控件点击时直接隐藏浮层
     *
     * @param viewId  控件ID
     * @param viewIds 控件ID
     */
    public AnyLayer onClickToDismiss(@IdRes int viewId, @IdRes int... viewIds)

    /**
     * 显示
     */
    public void show()

    /**
     * 隐藏
     */
    public void dismiss()

    /**
     * 获取布局子控件
     *
     * @param viewId 控件ID
     * @param <V>    子控件
     * @return 子控件
     */
    public <V extends View> V getView(@IdRes int viewId)

    /**
     * 获取View管理容器
     *
     * @return ViewHolder
     */
    public ViewHolder getViewHolder()

    /**
     * 获取自定义的浮层控件
     *
     * @return View
     */
    public View getContentView() 

    /**
     * 获取背景图
     *
     * @return ImageView
     */
    public ImageView getBackground()

    /**
     * 获取浮层是否已显示
     *
     * @return boolean
     */
    public boolean isShow()

    /**
     * 适配软键盘的弹出，布局自动上移
     * 在某几个EditText获取焦点时布局上移
     * 在{@link OnVisibleChangeListener#onShow(AnyLayer)}中调用
     * 应该和{@link #removeSoftInput()}成对出现
     *
     * @param editText 焦点EditTexts
     */
    public void compatSoftInput(EditText... editText)

    /**
     * 移除软键盘适配
     * 在{@link OnVisibleChangeListener#onDismiss(AnyLayer)}中调用
     * 应该和{@link #compatSoftInput(EditText...)}成对出现
     */
    public void removeSoftInput()
```

### 