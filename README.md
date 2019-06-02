# AnyLayer

用于替代Android自带Dialog和PopupWindow

[GitHub主页](https://github.com/goweii/AnyLayer)

[Demo下载](https://github.com/goweii/AnyLayer/raw/master/app/release/app-release.apk)



# 简介

- 链式调用
- 可实现Dialog效果
- 可实现PopupWindow效果
- 可实现悬浮菜单效果
- 可自定义数据绑定
- 可自定义进出场动画
- 可自由控制显示大小和位置
- 可自定义背景变暗或者显示图片
- 可一行代码实现背景高斯模糊
- 占用区域不会超过当前Activity，避免导航栏遮挡问题
- PopupWindow效果时可跟随目标View移动
- 可全局弹出
- 可从ApplicationContext弹出



**从2.4.0版本开始，添加了一个通用弹窗库，里面会封装一些比较常见的弹窗，不定期更新，已有通用弹窗：**

- 提示弹窗
- 列表弹窗（可实现单选和菜单效果）
- 加载中弹窗
- 菜单弹窗（ToolBar右上角的更多菜单）



# 求职（2019-05-30~今）

感觉最近不是招聘旺季，大多数职位都停止招聘了。唉，离职时间不赶趟呀。

有没有大佬有内推的，欢迎联系。

QQ:302833254

E-mail:goweii@163.com




# 说明

可以看到我的GitHub上面有3个功能比较类似的可以实现弹窗的框架。分别为AnyDialog、AnyLayer和FloatingLayer。可能大家有点混淆，为什么同一个功能要写3个框架？为了骗点击数？在这里向大家说明下每个框架的针对点。

首先是AnyDialog，他继承自Dialog，目的是方便Dialog的创建和使用。

再者是FloatingLayer，他是AnyLayer的一个最初版，写他的目的是因为有时候Dialog会被导航栏遮挡，或者Dialog的出现消失动画会经过黑色的导航栏区域，看着很不爽，很别扭。所以就想如果Dialog的范围能和当前的Activity的显示范围一样就好了，因此写了一个FloatingLayer，让他依附于Activity，控制一个View的动态添加和移除。

最后是AnyLayer，随着FloatingLayer的完善，我重构了代码，分离出了3个关键类，让他不再仅为实现Dialog效果，还可以实现PopupWindow和悬浮菜单等效果。因此我觉得有必要给他一个新的名字了，也是为了和AnyDialog相呼应，因此定名为AnyLayer。

所以可以看出，FloatingLayer其实已经停止维护，AnyDialog还是建议用于普通的Dialog弹窗，而AnyLayer则是去替代一些浮动窗口的显示。



# 截图

截图效果较差且版本较老，建议[下载Demo](https://github.com/goweii/AnyDialog/raw/master/app/release/app-release.apk)体验最新功能

![anylayer.gif](https://github.com/goweii/AnyLayer/blob/master/picture/demo.gif?raw=true)



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
  从2.3.1版本开始，版本号前不加v，引用时需要注意。
```java
// build.gradle(Module:)
dependencies {
    // 完整引入
    implementation 'com.github.goweii:AnyLayer:2.5.0'
    
    // 基础库
    implementation 'com.github.goweii.AnyLayer:anylayer:2.5.0'
    // 通用弹窗（依赖基础库）
    implementation 'com.github.goweii.AnyLayer:anylayer-common:2.5.0'
}
```

## 调用

### Dialog效果

```java
AnyLayer.with(this)
        .contentView(R.layout.dialog_test_2)
        .bindData(new LayerManager.IDataBinder() {
            @Override
            public void bind(AnyLayer anyLayer) {
                // TODO 绑定数据
            }
        })
        .backgroundColorRes(R.color.dialog_bg)
        .onClickToDismiss(R.id.fl_dialog_yes, R.id.fl_dialog_no)
        .show();
```

### PopupWindow效果

```java
AnyLayer.target(targetView)
        .contentView(R.layout.dialog_test_4)
        .alignment(Alignment.Direction.VERTICAL, Alignment.Horizontal.CENTER, Alignment.Vertical.BELOW, true)
        .backgroundColorRes(R.color.dialog_bg)
        .contentAnim(new LayerManager.IAnim() {
            @Override
            public Animator inAnim(View content) {
                return AnimHelper.createTopInAnim(content);
            }

            @Override
            public Animator outAnim(View content) {
                return AnimHelper.createTopOutAnim(content);
            }
        })
        .show();
```



# API

## AnyLayer

构建一个弹窗

```java
/**
 * 如果需要全局弹窗，必须现在Application中初始化
 * 2.4.0之后不再需要初始化也可全局弹窗
 **/
@Deprecated
public static void init(@NonNull Application application)

/**
 * 有背景高斯模糊弹窗，建议提前调用该方法初始化
 * 不用在Application调用，可放到启动页中进行，避免影响APP启动速度
 * 也可在用到的Activity的onCreate方法中
 **/
public static void initBlurred(@NonNull Context context)

/**
 * 有背景高斯模糊弹窗，在用到的Activity的onCreate方法中初始化后可在onDestroy中回收
 **/
public static void recycleBlurred()
    
/**
 * 此时不需要APP存在Activity实例
 * 会新启动一个Activity并向根布局添加一个浮层
 */
public static void with(@Nullable Context context, LayerActivity.OnLayerCreatedCallback callback)

/**
 * 向窗口根布局添加一个浮层
 * 需要在Application中调用{@link AnyLayer#init(Application)}
 **/
public static AnyLayer with()

/**
 * 向父布局viewGroup添加一个浮层
 */
public static AnyLayer with(@NonNull ViewGroup viewGroup)

/**
 * 向窗口根布局添加一个浮层
 **/
public static AnyLayer with(@NonNull Context context)

/**
 * 向窗口根布局添加一个浮层，且位置参照targetView
 **/
public static AnyLayer target(@NonNull View targetView)

/**
 * 设置自定义View
 **/
public AnyLayer contentView(@NonNull View contentView)

/**
 * 设置自定义布局文件
 **/
public AnyLayer contentView(@LayoutRes int contentViewId)

/**
 * 设置自定义布局文件中状态栏的占位View
 * 该控件高度将设置为状态栏高度，可用来使布局整体下移，避免状态栏遮挡
 **/
public AnyLayer asStatusBar(@IdRes int statusBarId)

/**
 * 绑定数据
 **/
public AnyLayer bindData(LayerManager.IDataBinder dataBinder)

/**
 * 设置显示状态改变的监听
 **/
public AnyLayer onVisibleChangeListener(LayerManager.OnVisibleChangeListener mOnVisibleChangeListener)

/**
 * 设置变更为显示状态监听
 **/
public AnyLayer onLayerShowListener(LayerManager.OnLayerShowListener onLayerShowListener)

/**
 * 设置变更为隐藏状态监听
 **/
public AnyLayer onLayerDismissListener(LayerManager.OnLayerDismissListener onLayerDismissListener)

/**
 * 设置子布局的gravity，可直接在布局文件指定layout_gravity属性，作用相同
 **/
public AnyLayer gravity(int gravity)

/**
 * 当以target方式创建时为参照View位置显示
 * 可自己指定浮层相对于参照View的对齐方式
 *
 * @param direction  主方向
 * @param horizontal 水平对齐方式
 * @param vertical   垂直对齐方式
 * @param inside     是否强制位于屏幕内部
 **/
public AnyLayer alignment(@NonNull Alignment.Direction direction,
                          @NonNull Alignment.Horizontal horizontal,
                          @NonNull Alignment.Vertical vertical,
                          boolean inside)

/**
 * 自定义浮层的进入和退出动画，可使用工具类{@link AnimHelper}
 **/
public AnyLayer contentAnim(LayerManager.IAnim contentAnim)

/**
 * 自定义浮层的进入动画
 **/
public AnyLayer contentInAnim(@AnimRes int anim)

/**
 * 自定义浮层的进入动画
 **/
public AnyLayer contentInAnim(@NonNull Animation anim)

/**
 * 自定义浮层的退出动画
 **/
public AnyLayer contentOutAnim(@AnimRes int anim)

/**
 * 自定义浮层的退出动画
 **/
public AnyLayer contentOutAnim(@NonNull Animation anim)

/**
 * 自定义背景的进入和退出动画，可使用工具类{@link AnimHelper}
 **/
public AnyLayer backgroundAnim(LayerManager.IAnim backgroundAnim)

/**
 * 自定义背景的进入动画
 **/
public AnyLayer backgroundInAnim(@AnimRes int anim)

/**
 * 自定义背景的退出动画
 **/
public AnyLayer backgroundInAnim(@NonNull Animation anim)

/**
 * 自定义背景的退出动画
 **/
public AnyLayer backgroundOutAnim(@AnimRes int anim)

/**
 * 自定义背景的退出动画
 **/
public AnyLayer backgroundOutAnim(@NonNull Animation anim)

/**
 * 默认浮层进入和退出动画时长
 **/
public AnyLayer defaultContentAnimDuration(long defaultAnimDuration)

/**
 * 默认浮层进入动画时长
 **/
public AnyLayer defaultContentInAnimDuration(long defaultInAnimDuration)

/**
 * 默认浮层退出动画时长
 **/
public AnyLayer defaultContentOutAnimDuration(long defaultOutAnimDuration)

/**
 * 默认背景进入和退出动画时长
 **/
public AnyLayer defaultBackgroundAnimDuration(long defaultAnimDuration)

/**
 * 默认背景进入动画时长
 **/
public AnyLayer defaultBackgroundInAnimDuration(long defaultInAnimDuration)

/**
 * 默认背景退出动画时长
 **/
public AnyLayer defaultBackgroundOutAnimDuration(long defaultOutAnimDuration)

/**
 * 设置背景为当前activity的高斯模糊效果
 * 设置之后其他背景设置方法失效，仅{@link #backgroundColorInt(int)}生效
 * 且设置的backgroundColor值调用imageView.setColorFilter(backgroundColor)设置
 * 建议此时的{@link #backgroundColorInt(int)}传入的为半透明颜色
 *
 * @param radius 模糊半径
 **/
public AnyLayer backgroundBlurRadius(@FloatRange(from = 0, fromInclusive = false, to = 25) float radius)

/**
 * 设置背景为当前activity的高斯模糊效果
 *
 * @param radius 模糊的百分比(相较于短边)
 **/
public AnyLayer backgroundBlurPercent(@FloatRange(from = 0, fromInclusive = false) float percent)

/**
 * 设置背景高斯模糊的缩小比例
 **/
public AnyLayer backgroundBlurScale(@FloatRange(from = 1) float scale)

/**
 * 设置背景图片
 **/
public AnyLayer backgroundBitmap(@NonNull Bitmap bitmap)

/**
 * 设置背景资源
 **/
public AnyLayer backgroundResource(@DrawableRes int resource)

/**
 * 设置背景Drawable
 **/
public AnyLayer backgroundDrawable(@NonNull Drawable drawable)

/**
 * 设置背景颜色
 * 在调用了{@link #backgroundBitmap(Bitmap)}或者{@link #backgroundBlurRadius(float)}方法后
 * 该颜色值将调用imageView.setColorFilter(backgroundColor)设置
 * 建议此时传入的颜色为半透明颜色
 *
 * @param colorInt 颜色值
 **/
public AnyLayer backgroundColorInt(@ColorInt int colorInt)

/**
 * 设置背景颜色
 **/
public AnyLayer backgroundColorRes(@ColorRes int colorRes)

/**
 * 设置浮层外部是否拦截触摸
 * 默认为true，false则事件由activityContent本身消费
 **/
public AnyLayer outsideInterceptTouchEvent(boolean intercept)

/**
 * 设置点击浮层以外区域是否可关闭
 **/
public AnyLayer cancelableOnTouchOutside(boolean cancelable)

/**
 * 设置点击返回键是否可关闭
 **/
public AnyLayer cancelableOnClickKeyBack(boolean cancelable)

/**
 * 对一个View绑定点击事件
 **/
public AnyLayer onClick(@IdRes int viewId, 
                        LayerManager.OnLayerClickListener listener)

/**
 * 对一个View绑定点击事件，点击时直接隐藏浮层
 **/
public AnyLayer onClickToDismiss(@IdRes int viewId, 
                                 LayerManager.OnLayerClickListener listener)

/**
 * 对多个View绑定点击事件
 **/
public AnyLayer onClick(LayerManager.OnLayerClickListener listener, 
                        @IdRes int viewId, 
                        @IdRes int... viewIds)

/**
 * 对多个View绑定点击事件，点击时直接隐藏浮层
 **/
public AnyLayer onClickToDismiss(LayerManager.OnLayerClickListener listener, 
                                 @IdRes int viewId, 
                                 @IdRes int... viewIds)

/**
 * 绑定该控件点击时直接隐藏浮层
 **/
public AnyLayer onClickToDismiss(@IdRes int viewId, @IdRes int... viewIds)

/**
 * 获取浮层是否已显示
 **/
public boolean isShow()

/**
 * 显示
 **/
public void show()

/**
 * 隐藏
 **/
public void dismiss()

/**
 * 获取Context
 **/
public Context getContext()

/**
 * 获取布局子控件
 **/
public <V extends View> V getView(@IdRes int viewId)

/**
 * 获取View管理容器
 **/
public ViewHolder getViewHolder()

/**
 * 获取自定义的浮层控件
 **/
public View getContentView()

/**
 * 获取背景图
 **/
public ImageView getBackground()

/**
 * 适配软键盘的弹出，布局自动上移
 * 在某几个EditText获取焦点时布局上移
 * 在{@link LayerManager.OnVisibleChangeListener#onShow(AnyLayer)}中调用
 * 应该和{@link #removeSoftInput()}成对出现
 *
 * @param editText 焦点EditTexts
 **/
public void compatSoftInput(EditText... editText)

/**
 * 移除软键盘适配
 * 在{@link LayerManager.OnVisibleChangeListener#onDismiss(AnyLayer)}中调用
 * 应该和{@link #compatSoftInput(EditText...)}成对出现
 **/
public void removeSoftInput()
```

## BaseLayer

采用继承的方式创建一个浮层，为方便通用浮层的封装，可在次基础上实现MVP模式。

```java
/**
 * 获取布局ID
 **/
protected abstract int getLayoutId()

/**
 * 获取上下文
 **/
protected Context getContext()

/**
 * 获取父布局
 **/
protected ViewGroup getParent()

/**
 * 获取参照View
 **/
protected View getTarget()

/**
 * 创建时调用，可在此初始化
 **/
protected void onCreate()

/**
 * 销毁时调用，可在此释放资源
 **/
protected boolean onDestroy()

/**
 * 显示
 **/
public void show()

/**
 * 隐藏
 **/
public void dismiss()
```

