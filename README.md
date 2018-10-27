# AnyLayer

用于替代Android自带Dialog和PopupWindow

[GitHub主页](https://github.com/goweii/AnyLayer)

[Demo下载](https://github.com/goweii/AnyDialog/raw/master/app/release/app-release.apk)

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
