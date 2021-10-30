package per.goweii.anylayer.ktx

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import per.goweii.anylayer.Layer
import per.goweii.anylayer.guide.GuideLayer

fun <T : GuideLayer> T.backgroundColorInt(@ColorInt colorInt: Int) = this.apply {
    this.setBackgroundColorInt(colorInt)
}

fun <T : GuideLayer> T.backgroundColorRes(@ColorRes colorRes: Int) = this.apply {
    this.setBackgroundColorRes(colorRes)
}

fun <T : GuideLayer> T.mapping(mapping: GuideLayer.Mapping) = this.apply {
    this.addMapping(mapping)
}

fun <T : GuideLayer> T.mapping(init: GuideLayer.Mapping.() -> Unit) = this.apply {
    this.addMapping(GuideLayer.Mapping().apply { init() })
}

fun GuideLayer.Mapping.doOnClick(@IdRes viewId: Int, onClickListener: GuideLayer.Mapping.(view: View) -> Unit) = this.apply {
    this.addOnClickListener({ _, v -> this.onClickListener(v) }, viewId)
}