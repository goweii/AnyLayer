package per.goweii.anylayer.ktx

import android.view.View
import androidx.annotation.LayoutRes
import per.goweii.anylayer.overlay.OverlayLayer

fun <T : OverlayLayer> T.floatView(@LayoutRes layoutId: Int) = this.apply {
    this.setOverlayView(layoutId)
}

fun <T : OverlayLayer> T.floatView(floatView: View) = this.apply {
    this.setOverlayView(floatView)
}

fun <T : OverlayLayer> T.defPercentX(p: Float) = this.apply {
    this.setDefPercentX(p)
}

fun <T : OverlayLayer> T.defPercentY(p: Float) = this.apply {
    this.setDefPercentY(p)
}

fun <T : OverlayLayer> T.defAlpha(alpha: Float) = this.apply {
    this.setDefAlpha(alpha)
}

fun <T : OverlayLayer> T.defScale(scale: Float) = this.apply {
    this.setDefScale(scale)
}

fun <T : OverlayLayer> T.normalAlpha(alpha: Float) = this.apply {
    this.setNormalAlpha(alpha)
}

fun <T : OverlayLayer> T.normalScale(scale: Float) = this.apply {
    this.setNormalScale(scale)
}

fun <T : OverlayLayer> T.lowProfileAlpha(alpha: Float) = this.apply {
    this.setLowProfileAlpha(alpha)
}

fun <T : OverlayLayer> T.lowProfileScale(scale: Float) = this.apply {
    this.setLowProfileScale(scale)
}

fun <T : OverlayLayer> T.lowProfileIndent(indent: Float) = this.apply {
    this.setLowProfileIndent(indent)
}

fun <T : OverlayLayer> T.lowProfileDelay(delay: Long) = this.apply {
    this.setLowProfileDelay(delay)
}

fun <T : OverlayLayer> T.snapEdge(edge: Int) = this.apply {
    this.setSnapEdge(edge)
}

fun <T : OverlayLayer> T.pivotX(pivot: Float) = this.apply {
    this.setPivotX(pivot)
}

fun <T : OverlayLayer> T.pivotY(pivot: Float) = this.apply {
    this.setPivotY(pivot)
}

fun <T : OverlayLayer> T.outside(outside: Boolean) = this.apply {
    this.setOutside(outside)
}

fun <T : OverlayLayer> T.marginLeft(margin: Int?) = this.apply {
    this.setMarginLeft(margin)
}

fun <T : OverlayLayer> T.marginTop(margin: Int?) = this.apply {
    this.setMarginTop(margin)
}

fun <T : OverlayLayer> T.marginRight(margin: Int?) = this.apply {
    this.setMarginRight(margin)
}

fun <T : OverlayLayer> T.marginBottom(margin: Int?) = this.apply {
    this.setMarginBottom(margin)
}

fun <T : OverlayLayer> T.paddingLeft(padding: Int) = this.apply {
    this.setPaddingLeft(padding)
}

fun <T : OverlayLayer> T.paddingTop(padding: Int) = this.apply {
    this.setPaddingTop(padding)
}

fun <T : OverlayLayer> T.paddingRight(padding: Int) = this.apply {
    this.setPaddingRight(padding)
}

fun <T : OverlayLayer> T.paddingBottom(padding: Int) = this.apply {
    this.setPaddingBottom(padding)
}

fun <T : OverlayLayer> T.doOnOverlayClick(onFloatClick: T.(view: View) -> Unit) = this.apply {
    this.addOnOverlayClickListener { _, view -> this.onFloatClick(view) }
}

fun <T : OverlayLayer> T.doOnOverlayLongClick(onFloatClick: T.(view: View) -> Boolean) = this.apply {
    this.setOnOverlayLongClickListener { _, view -> this.onFloatClick(view) }
}