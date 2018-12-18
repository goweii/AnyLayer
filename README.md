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

