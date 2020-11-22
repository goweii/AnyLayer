package per.goweii.anylayer.ktx

import android.view.View
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.IdRes
import per.goweii.anylayer.Layer
import per.goweii.anylayer.guide.GuideLayer

fun <T : GuideLayer> T.setBackgroundColorInt(@ColorInt colorInt: Int) = this.apply {
    this.backgroundColorInt(colorInt)
}

fun <T : GuideLayer> T.setBackgroundColorRes(@ColorRes colorRes: Int) = this.apply {
    this.backgroundColorRes(colorRes)
}

fun <T : GuideLayer> T.addMapping(mapping: GuideLayer.Mapping) = this.apply {
    this.mapping(mapping)
}

fun <T : GuideLayer> T.addMapping(init: GuideLayer.Mapping.() -> Unit) = this.apply {
    this.mapping(GuideLayer.Mapping().apply { init() })
}

fun GuideLayer.Mapping.doOnClick(@IdRes viewId: Int, onClickListener: GuideLayer.Mapping.(view: View) -> Unit) = this.apply {
    this.onClick(Layer.OnClickListener { _, v -> this.onClickListener(v) }, viewId)
}