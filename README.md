# AnyLayer

[![Android CI](https://github.com/goweii/AnyLayer/actions/workflows/android.yml/badge.svg)](https://github.com/goweii/AnyLayer/actions/workflows/android.yml)
[![GitHub license](https://img.shields.io/github/license/goweii/AnyLayer)](https://github.com/goweii/AnyLayer/blob/master/LICENSE)
[![](https://www.jitpack.io/v/goweii/AnyLayer.svg)](https://www.jitpack.io/#goweii/AnyLayer)
![Downloads Week](https://img.shields.io/badge/Downloads%20Week-1.4k-green)
![Downloads Month](https://img.shields.io/badge/Downloads%20Month-7.3K-blue)
![Demo](https://img.shields.io/badge/Demo-Apk-blue?link=https://github.com/goweii/AnyLayer/raw/master/simple/demo/demo.apk)

Android稳定高效的浮层创建管理框架。

浮层就是一个悬浮在其他View之上的View。通过使用代码动态控制View的添加和移除，可以实现例如Dialog/Popup/BottomSheet等弹窗，引导层，悬浮按钮，浮动通知，吐司等各种效果。

已实现的效果如下：

- Dialog/BottomSheet效果
- Popup效果
- Toast效果
- Guide效果
- Overlay效果
- Notification效果

# 如何集成

## 添加仓库

```groovy
maven { url "https://www.jitpack.io" }

// 使用4.1.5及以后版本必须添加，因高斯模糊用到的VisualEffect库暂时只发布在gitee仓库
// maven { url "https://gitee.com/goweii/maven-repository/raw/master/releases/" }
```

## 添加依赖

[点击查看更新历史](https://github.com/goweii/AnyLayer/releases)

```groovy
// 完整引入
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
```

## 问题建议

- 这几个依赖有啥区别？
    - anylayer：核心功能，所有浮层效果的实现
    - anylayer-ext：扩展功能，比如通用的动画实现和监听器的默认实现
    - anylayer-ktx：Kotlin扩展，方便再kt环境实现链式调用
    - anylayer-startup：Jetpack Startup实现
- anylayer-startup依赖于Jetpack Startup，需要自行引入。而且Jetpack Startup仅支持androidx，如果引用的是x.x.x-support需要自己在application初始化

# 使用说明

## 截图

| ![20210610_190449.gif](https://i.loli.net/2021/06/10/6jgVucdrE73S2pG.gif) | ![20210610_190537.gif](https://i.loli.net/2021/06/10/N617Xf2Kl5Woqd8.gif) | ![20210610_190654.gif](https://i.loli.net/2021/06/10/aVoWBmGqtE1HkUP.gif) | ![20210610_190715.gif](https://i.loli.net/2021/06/10/npHzPjwdqfKBDQt.gif) |
| --- | --- | --- | --- |
|     |     |     |     |

## 文档

[Wiki](https://github.com/goweii/AnyLayer/wiki)

# 使用该库的产品

如果你的产品正在使用AnyLayer，欢迎[留下相关信息](https://github.com/goweii/AnyLayer/issues/20)

**这些信息将用来帮助更多开发者关注并使用本框架，增加框架的活跃度。而高活跃度则意味着更多隐藏BUG被发现并修复，即活跃度等同于框架的健壮性。同时这也是我维护项目的最大动力，感谢。**

| APP名 | APP图标 | 公司名 |
| :--: | :--: | :--: |
| 玩安卓 | ![](https://user-images.githubusercontent.com/5456892/67614858-88c2e400-f7f6-11e9-868f-d6428a415e49.png) | 个人 |
| 熊猫淘学 | ![](https://user-images.githubusercontent.com/5456892/67614744-b9a21980-f7f4-11e9-9c2c-bcde1ff5a395.png) | 西安熊猫宝宝网络科技有限公司 |
| MBA大师 | ![](https://user-images.githubusercontent.com/5456892/67614972-806ba880-f7f8-11e9-81b4-c32ff1ef9d7e.png) | MBA大师 |

# 如何贡献

非常欢迎你的加入！提一个 [Issue](https://github.com/goweii/AnyLayer/issues) 或者提交一个 [Pull Request](https://github.com/goweii/AnyLayer/pulls)。

# 如何赞赏

如果你觉得还不错，就请我喝杯咖啡吧~

| ![wx_qrcode](https://gitee.com/goweii/WanAndroidServer/raw/master/about/wx_qrcode.png) |   |   |
|---|---|---|
|   |   |   |
