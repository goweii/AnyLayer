package per.goweii.anylayer.ktx

import android.view.View
import per.goweii.anylayer.popup.PopupLayer
import per.goweii.anylayer.popup.PopupLayer.Align

/**
 * @author CuiZhen
 * @date 2020/8/15
 */

fun <T : PopupLayer> T.setUpdateLocationInterceptor(interceptor: PopupLayer.UpdateLocationInterceptor) = this.apply {
    this.updateLocationInterceptor(interceptor)
}

fun <T : PopupLayer> T.doOnViewTreeScrollChanged(onScrollChanged: T.() -> Unit) = this.apply {
    this.onViewTreeScrollChangedListener { this.onScrollChanged() }
}

fun <T : PopupLayer> T.setScrollChangedToDismiss(enable: Boolean) = this.apply {
    this.scrollChangedToDismiss(enable)
}

fun <T : PopupLayer> T.setTargetView(target: View) = this.apply {
    this.targetView(target)
}

fun <T : PopupLayer> T.setContentClip(enable: Boolean) = this.apply {
    this.contentClip(enable)
}

fun <T : PopupLayer> T.setBackgroundAlign(enable: Boolean) = this.apply {
    this.backgroundAlign(enable)
}

fun <T : PopupLayer> T.setBackgroundOffset(enable: Boolean) = this.apply {
    this.backgroundOffset(enable)
}

fun <T : PopupLayer> T.setBackgroundResize(enable: Boolean) = this.apply {
    this.backgroundResize(enable)
}

fun <T : PopupLayer> T.setAlign(
        direction: Align.Direction,
        horizontal: Align.Horizontal,
        vertical: Align.Vertical,
        inside: Boolean
) = this.apply {
    this.align(direction, horizontal, vertical, inside)
}

fun <T : PopupLayer> T.setDirection(direction: Align.Direction) = this.apply {
    this.direction(direction)
}

fun <T : PopupLayer> T.setHorizontal(horizontal: Align.Horizontal) = this.apply {
    this.horizontal(horizontal)
}

fun <T : PopupLayer> T.setVertical(vertical: Align.Vertical) = this.apply {
    this.vertical(vertical)
}

fun <T : PopupLayer> T.setInside(inside: Boolean) = this.apply {
    this.inside(inside)
}

fun <T : PopupLayer> T.setOffsetX(offsetX: Float, unit: Int) = this.apply {
    this.offsetX(offsetX, unit)
}

fun <T : PopupLayer> T.setOffsetXdp(offsetX: Float) = this.apply {
    this.offsetXdp(offsetX)
}

fun <T : PopupLayer> T.setOffsetXpx(offsetX: Float) = this.apply {
    this.offsetXpx(offsetX)
}

fun <T : PopupLayer> T.setOffsetY(offsetY: Float, unit: Int) = this.apply {
    this.offsetY(offsetY, unit)
}

fun <T : PopupLayer> T.setOffsetYdp(offsetY: Float) = this.apply {
    this.offsetYdp(offsetY)
}

fun <T : PopupLayer> T.setOffsetYpx(offsetY: Float) = this.apply {
    this.offsetYpx(offsetY)
}