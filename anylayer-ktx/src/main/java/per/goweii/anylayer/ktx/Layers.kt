package per.goweii.anylayer.ktx

import android.animation.Animator
import android.view.View
import android.support.annotation.IdRes
import per.goweii.anylayer.Layer
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

fun <T : Layer> T.setAnimator(creator: Layer.AnimatorCreator) = this.apply {
    this.animator(creator)
}

fun <T : Layer> T.setInterceptKeyEvent(enable: Boolean) = this.apply {
    this.interceptKeyEvent(enable)
}

fun <T : Layer> T.setCancelableOnClickKeyBack(enable: Boolean) = this.apply {
    this.cancelableOnKeyBack(enable)
}