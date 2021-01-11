package per.goweii.anylayer.ktx

import android.support.annotation.LayoutRes
import android.view.View
import per.goweii.anylayer.floats.FloatLayer

fun <T : FloatLayer> T.setFloatView(@LayoutRes layoutId: Int) = this.apply {
    this.floatView(layoutId)
}

fun <T : FloatLayer> T.setFloatView(floatView: View) = this.apply {
    this.floatView(floatView)
}

fun <T : FloatLayer> T.setDefPercentX(p: Float) = this.apply {
    this.defPercentX(p)
}

fun <T : FloatLayer> T.setDefPercentY(p: Float) = this.apply {
    this.defPercentY(p)
}

fun <T : FloatLayer> T.setDefAlpha(alpha: Float) = this.apply {
    this.defAlpha(alpha)
}

fun <T : FloatLayer> T.setDefScale(scale: Float) = this.apply {
    this.defScale(scale)
}

fun <T : FloatLayer> T.setNormalAlpha(alpha: Float) = this.apply {
    this.normalAlpha(alpha)
}

fun <T : FloatLayer> T.setNormalScale(scale: Float) = this.apply {
    this.normalScale(scale)
}

fun <T : FloatLayer> T.setLowProfileAlpha(alpha: Float) = this.apply {
    this.lowProfileAlpha(alpha)
}

fun <T : FloatLayer> T.setLowProfileScale(scale: Float) = this.apply {
    this.lowProfileScale(scale)
}

fun <T : FloatLayer> T.setLowProfileIndent(indent: Float) = this.apply {
    this.lowProfileIndent(indent)
}

fun <T : FloatLayer> T.setLowProfileDelay(delay: Long) = this.apply {
    this.lowProfileDelay(delay)
}

fun <T : FloatLayer> T.setSnapEdge(edge: Int) = this.apply {
    this.snapEdge(edge)
}

fun <T : FloatLayer> T.setPivotX(pivot: Float) = this.apply {
    this.pivotX(pivot)
}

fun <T : FloatLayer> T.setPivotY(pivot: Float) = this.apply {
    this.pivotY(pivot)
}

fun <T : FloatLayer> T.setOutside(outside: Boolean) = this.apply {
    this.outside(outside)
}

fun <T : FloatLayer> T.setMarginLeft(margin: Int) = this.apply {
    this.marginLeft(margin)
}

fun <T : FloatLayer> T.setMarginTop(margin: Int) = this.apply {
    this.marginTop(margin)
}

fun <T : FloatLayer> T.setMarginRight(margin: Int) = this.apply {
    this.marginRight(margin)
}

fun <T : FloatLayer> T.setMarginBottom(margin: Int) = this.apply {
    this.marginBottom(margin)
}

fun <T : FloatLayer> T.setPaddingLeft(padding: Int) = this.apply {
    this.paddingLeft(padding)
}

fun <T : FloatLayer> T.setPaddingTop(padding: Int) = this.apply {
    this.paddingTop(padding)
}

fun <T : FloatLayer> T.setPaddingRight(padding: Int) = this.apply {
    this.paddingRight(padding)
}

fun <T : FloatLayer> T.setPaddingBottom(padding: Int) = this.apply {
    this.paddingBottom(padding)
}

fun <T : FloatLayer> T.doOnFloatClick(onFloatClick: T.(view: View) -> Unit) = this.apply {
    this.onFloatClick { _, view -> this.onFloatClick(view) }
}

fun <T : FloatLayer> T.doOnFloatLongClick(onFloatClick: T.(view: View) -> Boolean) = this.apply {
    this.onFloatLongClick { _, view -> this.onFloatClick(view) }
}