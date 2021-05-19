package per.goweii.anylayer.ktx

import android.view.View
import androidx.annotation.LayoutRes
import per.goweii.anylayer.floats.FloatLayer

fun <T : FloatLayer> T.floatView(@LayoutRes layoutId: Int) = this.apply {
    this.setFloatView(layoutId)
}

fun <T : FloatLayer> T.floatView(floatView: View) = this.apply {
    this.setFloatView(floatView)
}

fun <T : FloatLayer> T.defPercentX(p: Float) = this.apply {
    this.setDefPercentX(p)
}

fun <T : FloatLayer> T.defPercentY(p: Float) = this.apply {
    this.setDefPercentY(p)
}

fun <T : FloatLayer> T.defAlpha(alpha: Float) = this.apply {
    this.setDefAlpha(alpha)
}

fun <T : FloatLayer> T.defScale(scale: Float) = this.apply {
    this.setDefScale(scale)
}

fun <T : FloatLayer> T.normalAlpha(alpha: Float) = this.apply {
    this.setNormalAlpha(alpha)
}

fun <T : FloatLayer> T.normalScale(scale: Float) = this.apply {
    this.setNormalScale(scale)
}

fun <T : FloatLayer> T.lowProfileAlpha(alpha: Float) = this.apply {
    this.setLowProfileAlpha(alpha)
}

fun <T : FloatLayer> T.lowProfileScale(scale: Float) = this.apply {
    this.setLowProfileScale(scale)
}

fun <T : FloatLayer> T.lowProfileIndent(indent: Float) = this.apply {
    this.setLowProfileIndent(indent)
}

fun <T : FloatLayer> T.lowProfileDelay(delay: Long) = this.apply {
    this.setLowProfileDelay(delay)
}

fun <T : FloatLayer> T.snapEdge(edge: Int) = this.apply {
    this.setSnapEdge(edge)
}

fun <T : FloatLayer> T.pivotX(pivot: Float) = this.apply {
    this.setPivotX(pivot)
}

fun <T : FloatLayer> T.pivotY(pivot: Float) = this.apply {
    this.setPivotY(pivot)
}

fun <T : FloatLayer> T.outside(outside: Boolean) = this.apply {
    this.setOutside(outside)
}

fun <T : FloatLayer> T.marginLeft(margin: Int) = this.apply {
    this.setMarginLeft(margin)
}

fun <T : FloatLayer> T.marginTop(margin: Int) = this.apply {
    this.setMarginTop(margin)
}

fun <T : FloatLayer> T.marginRight(margin: Int) = this.apply {
    this.setMarginRight(margin)
}

fun <T : FloatLayer> T.marginBottom(margin: Int) = this.apply {
    this.setMarginBottom(margin)
}

fun <T : FloatLayer> T.paddingLeft(padding: Int) = this.apply {
    this.setPaddingLeft(padding)
}

fun <T : FloatLayer> T.paddingTop(padding: Int) = this.apply {
    this.setPaddingTop(padding)
}

fun <T : FloatLayer> T.paddingRight(padding: Int) = this.apply {
    this.setPaddingRight(padding)
}

fun <T : FloatLayer> T.paddingBottom(padding: Int) = this.apply {
    this.setPaddingBottom(padding)
}

fun <T : FloatLayer> T.doOnFloatClick(onFloatClick: T.(view: View) -> Unit) = this.apply {
    this.addOnFloatClickListener { _, view -> this.onFloatClick(view) }
}

fun <T : FloatLayer> T.onFloatLongClick(onFloatClick: T.(view: View) -> Boolean) = this.apply {
    this.setOnFloatLongClickListener { _, view -> this.onFloatClick(view) }
}