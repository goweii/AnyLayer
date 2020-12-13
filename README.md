AnyLayer

Android稳定高效的浮层创建管理框架。

可实现Dialog/Popup/BottomSheet等弹窗，引导层，悬浮按钮，浮动通知，吐司等效果。

[GitHub主页](https://github.com/goweii/AnyLayer)

[Demo下载](https://gitee.com/goweii/AnyLayer/raw/master/app/demo/demo.apk)





# 简介

- 同时兼容support和androidx
- 链式调用
- 支持自由扩展
- 实现几种常用效果
  - Dialog/BottomSheet效果
    - 占用区域不会超过当前Activity避免导航栏遮挡
    - 支持自定义大小和显示位置
    - 支持自定义数据绑定
    - 支持自定义进出场动画
    - 支持自定义背景颜色/图片/高斯模糊
    - 支持在Activity的onCreate生命周期弹出
    - 支持从ApplicationContext中弹出
    - 支持拖拽关闭
    - 支持不拦截外部事件
  - Popup效果
    - 拥有Dialog效果特性
    - 支持跟随目标View移动
  - Toast效果
    - 支持自定义图标和文字
    - 支持自定义显示时长
    - 支持自定义位置
    - 支持自定义背景资源和颜色
    - 支持自定义透明度
    - 支持自定义进出场动画
  - Guide效果
    - 详见demo
  - Float效果
    - 支持自定义吸附边
    - 支持自定义正常和低姿态2中模式
    - 支持自定义低姿态显示效果
  - Notification效果
    - 支持滑动关闭





# 说明

**详细原理和使用说明本人会在后续补上，但因个人开发，时间不定，请谅解。**

**或有大佬在看过源码后，愿意写下分析文章或使用说明来分享的，欢迎联系本人。写得比较详细的本人将会放到README中供大家查阅，提前谢过。**

当然也欢迎加群共同探讨，共同进步。

> QQ群：147715512（爱Android）





# 使用该库的产品

如果你的产品正在使用AnyLayer，欢迎[留下相关信息](https://github.com/goweii/AnyLayer/issues/20)

**这些信息将用来帮助更多开发者关注并使用本框架，增加框架的活跃度。而高活跃度则意味着更多隐藏BUG被发现并修复，即活跃度等同于框架的健壮性。同时这也是我维护项目的最大动力，感谢。**

| APP名 | APP图标 | 公司名 |
| :--: | :--: | :--: |
| 玩安卓 | ![](https://user-images.githubusercontent.com/5456892/67614858-88c2e400-f7f6-11e9-868f-d6428a415e49.png) | 个人 |
| 熊猫淘学 | ![](https://user-images.githubusercontent.com/5456892/67614744-b9a21980-f7f4-11e9-9c2c-bcde1ff5a395.png) | 西安熊猫宝宝网络科技有限公司 |
| MBA大师 | ![](https://user-images.githubusercontent.com/5456892/67614972-806ba880-f7f8-11e9-81b4-c32ff1ef9d7e.png) | MBA大师 |





# 截图

截图效果较差且版本较老，建议[下载Demo](https://gitee.com/goweii/AnyLayer/raw/master/app/demo/demo.apk)体验最新功能

![demo](https://gitee.com/goweii/AnyLayer/raw/master/app/demo/demo.gif)





# 使用说明



## 集成

![](https://img.shields.io/badge/Downloads%20Week-1.1k-green) ![](https://img.shields.io/badge/Downloads%20Month-5.2K-blue)

- ### 添加jitpack库

```groovy
// build.gradle(Project:)
allprojects {
    repositories {
        maven { url 'https://www.jitpack.io' }
    }
}
```

- ### 添加依赖

  [点击查看最新版本号](https://github.com/goweii/AnyLayer/releases)
```groovy
// build.gradle(Module:)
dependencies {
    // 完整引入，二选一
    implementation 'com.github.goweii:AnyLayer:4.1.0-androidx'
    implementation 'com.github.goweii:AnyLayer:4.1.0-support'
    
    // 基础库
    // implementation 'com.github.goweii.AnyLayer:anylayer:4.1.0-androidx'
    // 扩展库（依赖基础库）
    // implementation 'com.github.goweii.AnyLayer:anylayer-ext:4.1.0-androidx'
    // kotlin扩展库（依赖基础库和扩展库）
    // implementation 'com.github.goweii.AnyLayer:anylayer-ktx:4.1.0-androidx'
    // 通用弹窗（依赖基础库）
    // implementation 'com.github.goweii.AnyLayer:anylayer-common:2.5.0'
}
```



## 更新说明

[点击查看详细更新说明](https://github.com/goweii/AnyLayer/releases)

### 4.1.0

- 适配分屏小窗和横竖屏切换
- 新增设置通知最大宽高
- 新增3组对其方式
- 新增吐司自定义布局
- 吐司和通知默认布局适配暗色模式
- 修复一些代码错误

### 4.0.0

- 重构包结构
- 新增通知/悬浮按钮/引导浮层
- 支持自定义浮层层级

### 3.6.0

- 新增ext库，添加常用动画创建器
- 新增ktx库，方便kotlin调用

### 3.5.0

- 添加注解，避免kotlin调用变为可选型

### 3.3.0

- 拆分为support和androidx两个分支

### 3.1.0

- 移除对support-v7的依赖，可同时兼容support和androidx

### 3.0.0

- 大型重构，修改层级和继承结构，API改变很大
- 删除common库

### 2.5.0

- 新增popupWindow效果时可跟随targetView移动
- 可以从ApplicationContext中弹出
- 监听器支持添加多个

### 2.4.0

- 新增通用库



## 类间关系

- **[AnyLayer]()**（提供常用效果的调用入口）

- **[ViewManager]()**（管理View的动态添加移除和KeyEvent事件注册）

- **[Layer]()**（对ViewManager的包装，实现进出场动画逻辑和事件监听，规范接口形式，分离出ViewHolder/ListenerHolder/Config三大内部类）
  - **[FrameLayer]()**（限定父布局为FrameLayout，引入了Layer层级概念）
    - **[DecorLayer]()**（限定父布局为DecorView）
      - **[DialogLayer]()**（规范子布局层级，加入背景层，分离动画为背景动画和内容动画）
        - **[PopupLayer]()**（可依据锚点View定位）
      - **[ToastLayer]()**（吐司）
      - **[GuideLayer]()**（引导层）
      - **[FloatLayer]()**（悬浮按钮）
      - **[NotificationLayer]()**（通知）

- **[AnimatorHelper]()**（创建常用属性动画）



## 类说明

### AnyLayer

一个工厂效果的工具类，与Layer类族的关系就像Executors于Executor的关系这样。

只是提供了静态方法方便调用，不用new来new去的。

```java
/**
 * 初始化高斯模糊，可在用到高斯模糊背景的Activity的onCreate方法调用
 * 其实不调用也没关系，第一次显示的时候也会初始化的，只是这样第一次显示就会比后面显示稍微慢一点
 */
public static void initBlurred(Context context)

/**
 * 回收高斯模糊内存占用，可在用到高斯模糊背景的Activity的onDestroy方法调用，也可以在内存不足时调用
 */
public static void recycleBlurred()

/**
 * 创建一个DialogLayer
 * 这个Context不能是ApplicationContext
 */
public static DialogLayer dialog(Context context)

/**
 * 创建一个DialogLayer
 * 依附的是当前显示的Activity
 */
public static DialogLayer dialog()

/**
 * 创建一个DialogLayer
 * 依附的是特定Class的Activity实例，这个Activity必须是已经启动的
 */
public static DialogLayer dialog(Class<Activity> clazz)

/**
 * 创建一个DialogLayer
 * 会启动一个新的全透明Activity依附
 */
public static void dialog(LayerActivity.OnLayerCreatedCallback callback)

/**
 * 创建一个PopupLayer
 */
public static PopupLayer popup(View targetView)

/**
 * 创建一个ToastLayer
 * 依附的是当前显示的Activity
 */
public static ToastLayer toast()

/**
 * 创建一个ToastLayer
 * 这个Context不能是ApplicationContext
 */
public static ToastLayer toast(Context context)

/**
 * 创建一个FloatLayer
 * 这个Context不能是ApplicationContext
 */
public static FloatLayer floats(Context context)

/**
 * 创建一个GuideLayer
 * 这个Context不能是ApplicationContext
 */
public static GuideLayer guide(Context context)

/**
 * 创建一个NotificationLayer
 * 这个Context不能是ApplicationContext
 */
public static NotificationLayer notification(Context context)
```



### ViewManager

这个就不介绍了吧？一般用的时候也用不到，后面原理剖析的时候再说吧！



### Layer

上面类间关系简单描述了一下

> 对ViewManager的包装，实现进出场动画逻辑和事件监听，规范接口形式，分离出ViewHolder/ListenerHolder/Config三大内部类

可以看出这个类是所有效果的基类，有以下几个特定：

- #### 对ViewManager的包装

  可以自由指定父布局和子布局，可以通过继承在onGetParent和onCreateChild中返回，也可以通过parent()和child()方法设置。

  其次是几个生命周期回调方法

  - onAttach（View刚被添加到父布局）
  - onPreDraw（View开始绘制前，这里开始执行进场动画）
  - onShow（View显示，进场动画结束）
  - onPreRemove（View准备移除时，这里开始执行出场动画）
  - onDetach（View已被移除，出场动画结束）

- #### 实现进出场动画逻辑

  在onPreDraw和onPreRemove中实现了进出场动画的流程。可通过继承在onCreateInAnimator和onCreateOutAnimator中返回，也可以通过animator()方法设置。

  其中AnimatorCreator为创建自定义进出场动画的接口

- #### 事件监听

  有几个常用的事件监听，在ListenerHolder中统一管理

  - DataBinder（绑定数据）
  - OnClickListener（点击事件监听）
  - OnShowListener（显示动画开始和结束监听）
  - OnDismissListener（消失时动画开始和结束监听）
  - OnVisibleChangeListener（显示隐藏状态改变的监听）

- #### 分离出ViewHolder/ListenerHolder/Config三大内部类

  这个看名字应该就知道了，就不介绍了。

- #### 常用方法

  ```java
  /**
   * 一组控制显示隐藏的方法
   */
  public void show()
  public void show(boolean withAnim)
  public void dismiss()
  public void dismiss(boolean withAnim)
      
  /**
   * 判断当前显示隐藏状态
   */  
  public boolean isShow()
      
  /**
   * 根据ID获取对应View
   */  
  public <V extends View> V getView(int id)
      
  /**
   * 指定父布局
   */  
  public Layer parent(ViewGroup parent)
      
  /**
   * 指定子布局
   */  
  public Layer child(View child)
      
  /**
   * 自定义进出场动画
   */  
  public Layer animator(AnimatorCreator creator)
  
  /**
   * 是否拦截物理按键，为true时cancelableOnKeyBack才有效
   */  
  public Layer interceptKeyEvent(boolean intercept)
      
  /**
   * 是否可点击返回键关闭浮层，interceptKeyEvent为true才有效
   */  
  public Layer cancelableOnKeyBack(boolean cancelable)
      
  /**
   * 一组事件监听，见上面介绍
   */  
  public Layer bindData(DataBinder dataBinder)
  public Layer onVisibleChangeListener(OnVisibleChangeListener onVisibleChangeListener)
  public Layer onShowListener(OnShowListener onShowListener)
  public Layer onDismissListener(OnDismissListener onDismissListener)
      
  /**
   * 点击某些控件关闭浮层
   */  
  public Layer onClickToDismiss(OnClickListener listener, int... viewIds)
  public Layer onClickToDismiss(int... viewIds)
      
  /**
   * 对控件绑定点击事件
   */  
  public Layer onClick(OnClickListener listener, int... viewIds)
  ```



### DecorLayer

继承自Layer，强制父布局为DecorView。主要就是引入了Layer层级概念。

而这个层级就是由两个静态内部类实现的

- #### LayerLayout

  继承自FrameLayout，是各个层级浮层的容器，直接添加进DecorView。

- #### LevelLayout

  继承自FrameLayout，定义了Level概念，是每个浮层的父布局，也就是在外面包了一层，用来控制浮层上下层级的容器。这个是直接添加进LayerLayout的。

好了就这么多了。更多了细节还是看源码吧。



### DialogLayer

这个就是模仿传说中Dialog效果的浮层。用上面的描述就是：

> 规范子布局层级，加入背景层，分离动画为背景动画和内容动画

一个一个来看。

- #### 规范子布局层级，加入背景层

  从DecorLayer的介绍中可以知道LevelLayout是该类浮层的直接父布局。但它并不是DialogLayer中contentView的直接父布局。就是因为又加了一个ViewGroup把contextView和background包裹起来。布局文件是这样子的。

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
      android:id="@+id/fl_container"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:clickable="true">
  
      <ImageView
          android:id="@+id/iv_background"
          android:layout_width="match_parent"
          android:layout_height="match_parent"
          android:scaleType="centerCrop"/>
  
      <FrameLayout
          android:id="@+id/fl_content_wrapper"
          android:layout_width="match_parent"
          android:layout_height="match_parent" />
  
  </FrameLayout>
  ```

  contentView其实是直接添加至fl_content_wrapper中的。

- #### 分离动画为背景动画和内容动画

  因为有了contextView和background的概念，原来的动画就不好用了。所以从写了动画的创建逻辑，分离了背景和前景应用不同的动画。

最后看下常用的方法。

```java
/**
 * 设置自定义布局View/资源ID
 */
public DialogLayer contentView(View contentView)
public DialogLayer contentView(int contentViewId)

/**
 * 设置自定义布局文件中状态栏的占位View
 * 该控件高度将设置为状态栏高度，可用来使布局整体下移，避免状态栏遮挡
 */
public DialogLayer asStatusBar(int statusBarId)

/**
 * 设置避开状态栏
 */
public DialogLayer avoidStatusBar(boolean avoid)

/**
 * 设置子布局的gravity
 * 可直接在布局文件指定layout_gravity属性，作用相同
 */
public DialogLayer gravity(int gravity)

/**
 * 自定义浮层的进入和退出动画
 */
public DialogLayer contentAnimator(AnimatorCreator contentAnimatorCreator)

/**
 * 自定义背景的进入和退出动画
 */
public DialogLayer backgroundAnimator(AnimatorCreator backgroundAnimatorCreator)

/**
 * 设置背景高斯模糊/图片/颜色
 */
public DialogLayer backgroundBlurRadius(float radius)
public DialogLayer backgroundBlurPercent(float percent)
public DialogLayer backgroundBlurScale(float scale)
public DialogLayer backgroundBitmap(Bitmap bitmap)
public DialogLayer backgroundDimAmount(float dimAmount)
public DialogLayer backgroundResource(int resource)
public DialogLayer backgroundDrawable(Drawable drawable)
public DialogLayer backgroundColorInt(int colorInt)
public DialogLayer backgroundColorRes(int colorRes)

/**
 * 设置点击浮层以外区域是否可关闭
 */
public DialogLayer cancelableOnTouchOutside(boolean cancelable)

/**
 * 设置点击返回键是否可关闭
 */
public DialogLayer cancelableOnClickKeyBack(boolean cancelable)
```

算了，再看下调用代码凑凑字数把。

```java
AnyLayer.dialog(this)
        .contentView(R.layout.dialog_test_2)
        .backgroundColorRes(R.color.dialog_bg)
        .gravity(Gravity.CENTER)
        .cancelableOnTouchOutside(true)
        .cancelableOnClickKeyBack(true)
        .bindData(new Layer.DataBinder() {
            @Override
            public void bindData(Layer layer) {
                // TODO 绑定数据
            }
        })
        .onClickToDismiss(R.id.fl_dialog_no)
        .show();
```



### PopupLayer

因为继承自DialogLayer，所以可以使用定义过的方法（这不是废话吗）。

主要就是加入了可依据锚点View定位。

这个效果可能还要重构，暂时就不介绍了。

看下其他新增方法。

```java
/**
 * 设置浮层外部是否拦截触摸
 * 默认为true，false则事件有activityContent本身消费
 */
public PopupLayer outsideInterceptTouchEvent(boolean intercept)

/**
 * 是否裁剪contentView至包裹边界
 */
public PopupLayer contentClip(boolean clip)

/**
 * 是否偏移背景对齐目标控件
 */
public PopupLayer backgroundAlign(boolean align)

/**
 * 背景应用offset设置
 */
public PopupLayer backgroundOffset(boolean offset)

/**
 * 当以target方式创建时为参照View位置显示
 * 可自己指定浮层相对于参照View的对齐方式
 */
public PopupLayer align(Align.Direction direction,
                        Align.Horizontal horizontal,
                        Align.Vertical vertical,
                        boolean inside)

/**
 * 指定浮层相对于参照View的对齐方式
 */
public PopupLayer direction(Align.Direction direction)

/**
 * 指定浮层相对于参照View的对齐方式
 */
public PopupLayer horizontal(Align.Horizontal horizontal)

/**
 * 指定浮层相对于参照View的对齐方式
 */
public PopupLayer vertical(Align.Vertical vertical)

/**
 * 指定浮层是否强制位于屏幕内部
 */
public PopupLayer inside(boolean inside)

/**
 * X轴偏移
 */
public PopupLayer offsetX(float offsetX, int unit)
public PopupLayer offsetXdp(float dp)
public PopupLayer offsetXpx(float px)
/**
 * Y轴偏移
 */
public PopupLayer offsetY(float offsetY, int unit)
public PopupLayer offsetYdp(float dp)
public PopupLayer offsetYpY(float px)
```

国际惯例，最后看下调用代码凑凑字数。

```java
AnyLayer.popup(targetView)
        .contentView(R.layout.dialog_test_4)
        .backgroundColorRes(R.color.dialog_bg)
        .direction(Align.Direction.VERTICAL)
        .horizontal(Align.Horizontal.CENTER)
        .vertical(Align.Vertical.BELOW)
    	.inside(true)
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



### ToastLayer

累了，这个写简单点，就看下调用代码算了。

```java
AnyLayer.toast()
        .duration(3000)
        .icon(isSucc ? R.drawable.ic_success : R.drawable.ic_fail)
        .message(isSucc ? "哈哈，成功了" : "哎呀，失败了")
        .show();
```



### GuideLayer

> 已实现，文档待补充



### FloatLayer

> 已实现，文档待补充



### NotificationLayer

> 已实现，文档待补充



# 如果你觉得还不错，就请我喝杯咖啡吧~

| ![wx_qrcode](https://gitee.com/goweii/WanAndroidServer/raw/master/about/wx_qrcode.png) |
|---|
