# [求职](https://github.com/goweii/job-wanted)



# AnyLayer

Android稳定高效的浮层创建管理框架。

什么是浮层？浮层就是一个悬浮在其他View上方的View。但是2个View处于同一个Window。

可实现Dialog/Popup/BottomSheet等弹窗，引导层，悬浮按钮，浮动通知，吐司等效果。

[GitHub主页](https://github.com/goweii/AnyLayer)

[Demo下载](https://gitee.com/goweii/AnyLayer/raw/master/app/demo/demo.apk)



# 简介

如果你的App用的是单Activity架构，那么这个就是系统Dialog/PopupWindow/Toast的完美替代。而且可以实现悬浮按钮和引导层等一些列功能。

不是单Activity也不影响，上面这些功能一样可以实现，只是Toast无法跨页面，会随Activity一起关闭。

- 同时兼容support和androidx
- 链式调用
- 支持自由扩展
- 实现几种常用效果
  - Dialog/BottomSheet效果
  - Popup效果
  - Toast效果
  - Guide效果
  - Overlay效果
  - Notification效果

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

截图效果较差，建议[下载Demo](https://gitee.com/goweii/AnyLayer/raw/master/app/demo/demo.apk)体验最新功能

| ![20210610_190449.gif](https://i.loli.net/2021/06/10/6jgVucdrE73S2pG.gif) | ![20210610_190537.gif](https://i.loli.net/2021/06/10/N617Xf2Kl5Woqd8.gif) | ![20210610_190654.gif](https://i.loli.net/2021/06/10/aVoWBmGqtE1HkUP.gif) | ![20210610_190715.gif](https://i.loli.net/2021/06/10/npHzPjwdqfKBDQt.gif) |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
|                                                              |                                                              |                                                              |                                                              |

# 使用说明



## 集成

![](https://img.shields.io/badge/Downloads%20Week-1.4k-green)
![](https://img.shields.io/badge/Downloads%20Month-7.3K-blue)
[![](https://www.jitpack.io/v/goweii/AnyLayer.svg)](https://www.jitpack.io/#goweii/AnyLayer)

- ### 添加仓库

```groovy
// build.gradle(Project:)
allprojects {
    repositories {
        maven { url "https://www.jitpack.io" }
        
        // 使用4.1.5及以后版本必须添加，因高斯模糊用到的VisualEffect库暂时只发布在gitee仓库
        // maven { url "https://gitee.com/goweii/maven-repository/raw/master/releases/" }
    }
}
```

- ### 添加依赖

  [点击查看最新版本号](https://github.com/goweii/AnyLayer/releases)
```groovy
// build.gradle(Module:)
dependencies {
    // 完整引入，二选一
    implementation "com.github.goweii:AnyLayer:$version"
    
    // 按需引入
    // 基础库
    // implementation "com.github.goweii.AnyLayer:anylayer:$version"
    // 扩展库（依赖基础库）
    // implementation "com.github.goweii.AnyLayer:anylayer-ext:$version"
    // Kotlin扩展库（依赖基础库和扩展库）
    // implementation "com.github.goweii.AnyLayer:anylayer-ktx:$version"
    // 自动初始化（依赖基础库和Jetpack Startup）
    // implementation "com.github.goweii.AnyLayer:anylayer-startup:$version"
}
```

- ### 一些问题/建议

  - 建议按需引入
  anylayer-startup依赖于Jetpack Startup，需要自行引入。而且Jetpack Startup仅支持androidx，如果引用的是x.x.x-support需要自己在application初始化
  - 这几个依赖有啥区别？
    - anylayer：核心功能，所有浮层效果的实现
    - anylayer-ext：扩展功能，比如通用的动画实现和监听器的默认实现
    - anylayer-ktx：Kotlin扩展，方便再kt环境实现链式调用
    - anylayer-startup：Jetpack Startup实现



## 更新说明

[点击查看](https://github.com/goweii/AnyLayer/releases)



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
      - **[OverlayLayer]()**（悬浮按钮）
      - **[NotificationLayer]()**（通知）

- **[AnimatorHelper]()**（创建常用属性动画）



## 类说明

### AnyLayer

一个工厂效果的工具类，与Layer类族的关系就像Executors于Executor的关系这样。

只是提供了静态方法方便调用，不用new来new去的。

```java
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
 * 创建一个OverlayLayer
 * 这个Context不能是ApplicationContext
 */
public static OverlayLayer overlay(Context context)

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

  - DataBindCallback（绑定数据）
  - OnClickListener（点击事件监听）
  - OnShowListener（显示动画开始和结束监听）
  - OnDismissListener（消失时动画开始和结束监听）
  - OnVisibleChangeListener（显示隐藏状态改变的监听）

- #### 分离出ViewHolder/ListenerHolder/Config三大内部类

  这个看名字应该就知道了，就不介绍了。




### FrameLayer

继承自Layer，强制父布局为FrameLayout。主要就是引入了Layer层级概念。



### DecorLayer

继承自FrameLayer，强制父布局为DecorView。

而这个层级就是由两个静态内部类实现的

- #### LayerLayout

  继承自FrameLayout，是各个层级浮层的容器，直接添加进DecorView。

- #### LevelLayout

  继承自FrameLayout，定义了Level概念，是每个浮层的父布局，也就是在外面包了一层，用来控制浮层上下层级的容器。这个是直接添加进LayerLayout的。

好了就这么多了。更多了细节还是看源码吧。



### DialogLayer

Dialog效果的浮层。用上面的描述就是：

> 规范子布局层级，加入背景层，分离动画为背景动画和内容动画

一个一个来看。

- #### 规范子布局层级，加入背景层

  从DecorLayer的介绍中可以知道LevelLayout是该类浮层的直接父布局。但它并不是DialogLayer中contentView的直接父布局。就是因为又加了一个ViewGroup把contextView和background包裹起来。布局文件是这样子的。

  ```xml
  <?xml version="1.0" encoding="utf-8"?>
  <FrameLayout android:id="@+id/fl_container" >
      <ImageView android:id="@+id/iv_background" />
      <FrameLayout android:id="@+id/fl_content_wrapper" />
  </FrameLayout>
  ```
  
  contentView其实是直接添加至fl_content_wrapper中的。
  
- #### 分离动画为背景动画和内容动画

  因为有了contextView和background的概念，原来的动画就不好用了。所以从写了动画的创建逻辑，分离了背景和前景应用不同的动画。



### PopupLayer

继承自DialogLayer，加入了锚点定位。



### ToastLayer

吐司



### GuideLayer

引导层



### OverlayLayer

悬浮按钮



### NotificationLayer

通知



# 如果你觉得还不错，就请我喝杯咖啡吧~

| ![wx_qrcode](https://gitee.com/goweii/WanAndroidServer/raw/master/about/wx_qrcode.png) |
|---|
