package per.goweii.anylayer.ktx

import android.animation.Animator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import per.goweii.anylayer.*
import per.goweii.anylayer.ext.DefaultOnDismissListener
import per.goweii.anylayer.ext.DefaultOnShowListener
import per.goweii.anylayer.ext.DefaultOnVisibleChangeListener

/**
 * @author CuiZhen
 * @date 2020/8/15
 */

// Layer

fun <T : Layer> T.doOnClick(@IdRes viewId: Int, onClickListener: T.(view: View) -> Unit) = this.apply {
    this.onClick(Layer.OnClickListener { _, v -> this.onClickListener(v) }, viewId)
}

fun <T : Layer> T.doOnClickToDismiss(@IdRes viewId: Int, onClickListener: (T.(view: View) -> Unit)? = null) = this.apply {
    this.onClickToDismiss(Layer.OnClickListener { _, v -> onClickListener?.invoke(this, v) }, viewId)
}

fun <T : Layer> T.doBindData(dataBinder: T.() -> Unit) = this.apply {
    this.bindData { this.dataBinder() }
}

fun <T : Layer> T.doInitialize(onInitialize: T.() -> Unit) = this.apply {
    this.onInitialize { this.onInitialize() }
}

fun <T : Layer> T.doOnShow(onShow: T.() -> Unit) = this.apply {
    this.onVisibleChangeListener(object : DefaultOnVisibleChangeListener() {
        override fun onShow(layer: Layer) {
            onShow.invoke(this@apply)
        }
    })
}

fun <T : Layer> T.doAfterShow(onAfterShow: T.() -> Unit) = this.apply {
    this.onShowListener(object : DefaultOnShowListener() {
        override fun onShown(layer: Layer) {
            this@apply.onAfterShow()
        }
    })
}

fun <T : Layer> T.doBeforeDismiss(onBeforeDismiss: T.() -> Unit) = this.apply {
    this.onDismissListener(object : DefaultOnDismissListener() {
        override fun onDismissing(layer: Layer) {
            this@apply.onBeforeDismiss()
        }
    })
}

fun <T : Layer> T.doOnDismiss(onDismiss: T.() -> Unit) = this.apply {
    this.onVisibleChangeListener(object : DefaultOnVisibleChangeListener() {
        override fun onDismiss(layer: Layer) {
            onDismiss.invoke(this@apply)
        }
    })
}

fun <T : Layer, R : Animator?> T.createAnimator(
        onIn: T.(target: View) -> R,
        onOut: T.(target: View) -> R
) = this.apply {
    this.animator(object : Layer.AnimatorCreator {
        override fun createInAnimator(target: View): Animator? {
            return onIn.invoke(this@apply, target)
        }

        override fun createOutAnimator(target: View): Animator? {
            return onOut.invoke(this@apply, target)
        }
    })
}

fun <T : Layer> T.setInterceptKeyEvent(enable: Boolean) = this.apply {
    this.interceptKeyEvent(enable)
}

fun <T : Layer> T.setCancelableOnClickKeyBack(enable: Boolean) = this.apply {
    this.cancelableOnKeyBack(enable)
}

// DialogLayer

fun <T : DialogLayer> T.setCancelableOnTouchOutside(enable: Boolean) = this.apply {
    this.cancelableOnTouchOutside(enable)
}

fun <T : DialogLayer> T.setContentView(contentView: View) = this.apply {
    this.contentView(contentView)
}

fun <T : DialogLayer> T.setContentView(@IdRes contentViewId: Int) = this.apply {
    this.contentView(contentViewId)
}

fun <T : DialogLayer> T.setAsStatusBar(@IdRes viewId: Int) = this.apply {
    this.asStatusBar(viewId)
}

fun <T : DialogLayer> T.setAvoidStatusBar(enable: Boolean) = this.apply {
    this.avoidStatusBar(enable)
}

fun <T : DialogLayer> T.setGravity(gravity: Int) = this.apply {
    this.gravity(gravity)
}

fun <T : DialogLayer> T.setDragStyle(dragStyle: DragLayout.DragStyle) = this.apply {
    this.dragDismiss(dragStyle)
}

fun <T : DialogLayer> T.setDragTransformer(dragTransformer: DialogLayer.DragTransformer) = this.apply {
    this.dragTransformer(dragTransformer)
}

fun <T : DialogLayer> T.setAnimStyle(animStyle: DialogLayer.AnimStyle?) = this.apply {
    this.animStyle(animStyle)
}

fun <T : DialogLayer, R : Animator?> T.createContentAnimator(
        onIn: T.(target: View) -> R,
        onOut: T.(target: View) -> R
) = this.apply {
    this.contentAnimator(object : Layer.AnimatorCreator {
        override fun createInAnimator(target: View): Animator? {
            return onIn.invoke(this@apply, target)
        }

        override fun createOutAnimator(target: View): Animator? {
            return onOut.invoke(this@apply, target)
        }
    })
}

fun <T : DialogLayer, R : Animator?> T.createBackgroundAnimator(
        onIn: T.(target: View) -> R,
        onOut: T.(target: View) -> R
) = this.apply {
    this.backgroundAnimator(object : Layer.AnimatorCreator {
        override fun createInAnimator(target: View): Animator? {
            return onIn.invoke(this@apply, target)
        }

        override fun createOutAnimator(target: View): Animator? {
            return onOut.invoke(this@apply, target)
        }
    })
}

fun <T : DialogLayer> T.setBackgroundBlurRadius(@FloatRange(from = 0.0) radius: Float) = this.apply {
    this.backgroundBlurRadius(radius)
}

fun <T : DialogLayer> T.setBackgroundBlurPercent(@FloatRange(from = 0.0) percent: Float) = this.apply {
    this.backgroundBlurPercent(percent)
}

fun <T : DialogLayer> T.setBackgroundBlurScale(@FloatRange(from = 1.0) scale: Float) = this.apply {
    this.backgroundBlurScale(scale)
}

fun <T : DialogLayer> T.setBackgroundBitmap(bitmap: Bitmap) = this.apply {
    this.backgroundBitmap(bitmap)
}

fun <T : DialogLayer> T.setBackgroundDimAmount(@FloatRange(from = 0.0, to = 1.0) dimAmount: Float) = this.apply {
    this.backgroundDimAmount(dimAmount)
}

fun <T : DialogLayer> T.setBackgroundDimDefault() = this.apply {
    this.backgroundDimDefault()
}

fun <T : DialogLayer> T.setBackgroundResource(@DrawableRes resource: Int) = this.apply {
    this.backgroundResource(resource)
}

fun <T : DialogLayer> T.setBackgroundDrawable(drawable: Drawable?) = this.apply {
    this.backgroundDrawable(drawable)
}

fun <T : DialogLayer> T.setBackgroundColorInt(@ColorInt colorInt: Int) = this.apply {
    this.backgroundColorInt(colorInt)
}

fun <T : DialogLayer> T.setBackgroundColorRes(@ColorRes colorRes: Int) = this.apply {
    this.backgroundColorRes(colorRes)
}

fun <T : DialogLayer> T.setOutsideInterceptTouchEvent(enable: Boolean) = this.apply {
    this.outsideInterceptTouchEvent(enable)
}

fun <T : DialogLayer> T.doOnOutsideTouched(onOutsideTouched: T.() -> Unit) = this.apply {
    this.outsideTouched { this@apply.onOutsideTouched() }
}

fun <T : DialogLayer> T.setOutsideTouchedToDismiss(enable: Boolean) = this.apply {
    this.outsideTouchedToDismiss(enable)
}

// PopupLayer

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