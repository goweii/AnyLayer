# AnyLayer

Android稳定高效的浮层创建管理框架。可取代系统自带Dialog/Popup/BottomSheet等弹窗，可实现特定Activity独用的Toast提示，可定制任意样式的Guide引导层。

[GitHub主页](https://github.com/goweii/AnyLayer)

[Demo下载](https://github.com/goweii/AnyLayer/raw/master/app/release/app-release.apk)





# 简介

- 链式调用
- 三大效果
  - Dialog效果（Dialog/Popup/BottomSheet等效果）
    - 占用区域不会超过当前Activity避免导航栏遮挡
    - 支持自由控制浮层大小和显示位置
    - 支持自定义数据绑定
    - 支持自定义进出场动画
    - 支持自定义背景颜色/图片/高斯模糊
    - 支持Popup效果时跟随目标View移动
    - 支持在Activity的onCreate生命周期弹出
  - Toast效果（依附Activity非全局）
    - 支持自定义图标和文字
    - 支持自定义显示时长
    - 待完善
  - Guide效果（引导层）
    - 待开发






# 说明

**详细原理和使用说明本人会在后续补上，但因个人开发，时间不定，请谅解。**

**或有大佬在看过源码后，愿意写下分析文章或使用说明来分享的，欢迎联系本人。写得比较详细的本人将会放到README中供大家查阅，提前谢过。**

当然也欢迎加好友共同探讨，共同进步。

QQ：302833254

QQ群：147715512（爱Android）





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
  
  引用时需注意版本号，从2.3.1版本开始，版本号前不加v。
  
  通用库为在2.4.0版本新增，有效引用为2.4.0~2.5.0之间版本。
  
  因框架重构，在3.0.0版本中暂时删除通用库，会在后续添加。
  
  从3.1.0版本开始移除support-v7依赖，可同时兼容support和androidx
  
  因重构代码变化较大，不建议使用较多的老项目升级，保持2.5.0版即可，在实现Dialog/Popup/BottomSheet等效果上无本质差别。
  
```groovy
// build.gradle(Module:)
dependencies {
    // 完整引入
    implementation 'com.github.goweii:AnyLayer:3.1.0'
    
    // 基础库
    // implementation 'com.github.goweii.AnyLayer:anylayer:2.5.0'
    
    // 通用弹窗（依赖基础库）
    // 从3.0.0版本暂时删除
    // implementation 'com.github.goweii.AnyLayer:anylayer-common:2.5.0'
}
```



## 使用

### Dialog效果

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

### Popup效果

```java
AnyLayer.popup(targetView)
        .contentView(R.layout.dialog_test_4)
        .outsideInterceptTouchEvent(false)	// 是否阻塞底部activity触摸事件
        .backgroundColorRes(R.color.dialog_bg)
        .alignment(Alignment.Direction.VERTICAL, Alignment.Horizontal.CENTER, Alignment.Vertical.BELOW, true)
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

### Toast效果

```java
AnyLayer.toast()
        .duration(3000)
        .icon(isSucc ? R.drawable.ic_success : R.drawable.ic_fail)
        .message(isSucc ? "哈哈，成功了" : "哎呀，失败了")
        .show();
```

### Guide效果

```java
// TODO 待实现
```



