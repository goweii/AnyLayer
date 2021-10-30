package per.goweii.anylayer.ktx

import android.view.View
import per.goweii.anylayer.popup.PopupLayer
import per.goweii.anylayer.popup.PopupLayer.Align

fun <T : PopupLayer> T.updateLocationInterceptor(interceptor: PopupLayer.UpdateLocationInterceptor) = this.apply {
    this.setUpdateLocationInterceptor(interceptor)
}

fun <T : PopupLayer> T.doOnViewTreeScrollChanged(onScrollChanged: T.() -> Unit) = this.apply {
    this.setOnViewTreeScrollChangedListener { this.onScrollChanged() }
}

fun <T : PopupLayer> T.dismissOnScrollChanged(enable: Boolean) = this.apply {
    this.setScrollChangedToDismiss(enable)
}

fun <T : PopupLayer> T.targetView(target: View) = this.apply {
    this.setTargetView(target)
}

fun <T : PopupLayer> T.contentClip(enable: Boolean) = this.apply {
    this.setContentClip(enable)
}

fun <T : PopupLayer> T.backgroundAlign(enable: Boolean) = this.apply {
    this.setBackgroundAlign(enable)
}

fun <T : PopupLayer> T.backgroundOffset(enable: Boolean) = this.apply {
    this.setBackgroundOffset(enable)
}

fun <T : PopupLayer> T.backgroundResize(enable: Boolean) = this.apply {
    this.setBackgroundResize(enable)
}

fun <T : PopupLayer> T.align(
        direction: Align.Direction,
        horizontal: Align.Horizontal,
        vertical: Align.Vertical,
        inside: Boolean
) = this.apply {
    this.setAlign(direction, horizontal, vertical, inside)
}

fun <T : PopupLayer> T.direction(direction: Align.Direction) = this.apply {
    this.setDirection(direction)
}

fun <T : PopupLayer> T.horizontal(horizontal: Align.Horizontal) = this.apply {
    this.setHorizontal(horizontal)
}

fun <T : PopupLayer> T.vertical(vertical: Align.Vertical) = this.apply {
    this.setVertical(vertical)
}

fun <T : PopupLayer> T.inside(inside: Boolean) = this.apply {
    this.setInside(inside)
}

fun <T : PopupLayer> T.offsetX(offsetX: Float, unit: Int) = this.apply {
    this.setOffsetX(offsetX, unit)
}

fun <T : PopupLayer> T.offsetXdp(offsetX: Float) = this.apply {
    this.setOffsetXdp(offsetX)
}

fun <T : PopupLayer> T.offsetXpx(offsetX: Float) = this.apply {
    this.setOffsetXpx(offsetX)
}

fun <T : PopupLayer> T.offsetY(offsetY: Float, unit: Int) = this.apply {
    this.setOffsetY(offsetY, unit)
}

fun <T : PopupLayer> T.offsetYdp(offsetY: Float) = this.apply {
    this.setOffsetYdp(offsetY)
}

fun <T : PopupLayer> T.offsetYpx(offsetY: Float) = this.apply {
    this.setOffsetYpx(offsetY)
}