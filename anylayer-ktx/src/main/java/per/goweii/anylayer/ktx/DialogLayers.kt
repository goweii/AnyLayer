package per.goweii.anylayer.ktx

import android.animation.Animator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import per.goweii.anylayer.Layer
import per.goweii.anylayer.dialog.DialogLayer
import per.goweii.anylayer.ext.DefaultOnSwipeListener
import per.goweii.anylayer.widget.SwipeLayout

/**
 * @author CuiZhen
 * @date 2020/8/15
 */

fun <T : DialogLayer> T.setCancelableOnTouchOutside(enable: Boolean) = this.apply {
    this.cancelableOnTouchOutside(enable)
}

fun <T : DialogLayer> T.setContentView(contentView: View) = this.apply {
    this.contentView(contentView)
}

fun <T : DialogLayer> T.setContentView(@LayoutRes contentViewId: Int) = this.apply {
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

fun <T : DialogLayer> T.setSwipeDismiss(@SwipeLayout.Direction swipeDirection: Int) = this.apply {
    this.swipeDismiss(swipeDirection)
}

fun <T : DialogLayer> T.setSwipeTransformer(swipeTransformer: DialogLayer.SwipeTransformer) = this.apply {
    this.swipeTransformer(swipeTransformer)
}

fun <T : DialogLayer> T.doOnSwipeStart(onStart: T.() -> Unit) = this.apply {
    this.onSwipeListener(object : DefaultOnSwipeListener() {
        override fun onStart(layer: Layer) {
            this@apply.onStart()
        }
    })
}

fun <T : DialogLayer> T.doOnSwiping(onSwiping: T.(direction: Int, fraction: Float) -> Unit) = this.apply {
    this.onSwipeListener(object : DefaultOnSwipeListener() {
        override fun onSwiping(layer: Layer,
                               @SwipeLayout.Direction direction: Int,
                               @FloatRange(from = 0.0, to = 1.0) fraction: Float) {
            this@apply.onSwiping(direction, fraction)
        }
    })
}

fun <T : DialogLayer> T.doOnSwipeEnd(onEnd: T.(direction: Int) -> Unit) = this.apply {
    this.onSwipeListener(object : DefaultOnSwipeListener() {
        override fun onEnd(layer: Layer, @SwipeLayout.Direction direction: Int) {
            this@apply.onEnd(direction)
        }
    })
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

fun <T : DialogLayer> T.setContentAnimator(creator: Layer.AnimatorCreator) = this.apply {
    this.contentAnimator(creator)
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

fun <T : DialogLayer> T.setBackgroundAnimator(creator: Layer.AnimatorCreator) = this.apply {
    this.backgroundAnimator(creator)
}

fun <T : DialogLayer> T.setBackgroundBlurRadius(@FloatRange(from = 0.0) radius: Float) = this.apply {
    this.backgroundBlurRadius(radius)
}

fun <T : DialogLayer> T.setBackgroundBlurPercent(@FloatRange(from = 0.0) percent: Float) = this.apply {
    this.backgroundBlurPercent(percent)
}

fun <T : DialogLayer> T.setBackgroundBlurScale(@FloatRange(from = 1.0) scale: Float) = this.apply {
    this.backgroundBlurSimple(scale)
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