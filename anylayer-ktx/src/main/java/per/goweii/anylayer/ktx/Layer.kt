package per.goweii.anylayer.ktx

import android.animation.Animator
import android.view.View
import androidx.annotation.IdRes
import per.goweii.anylayer.Layer
import per.goweii.anylayer.ext.DefaultOnDismissListener
import per.goweii.anylayer.ext.DefaultOnShowListener
import per.goweii.anylayer.ext.DefaultOnVisibleChangeListener

fun <T : Layer> T.doOnClick(@IdRes viewId: Int, onClickListener: T.(view: View) -> Unit) = this.apply {
    this.addOnClickListener(Layer.OnClickListener { _, v -> this.onClickListener(v) }, viewId)
}

fun <T : Layer> T.dismissOnClick(@IdRes viewId: Int, onClickListener: (T.(view: View) -> Unit)? = null) = this.apply {
    onClickListener?.let {
        this.addOnClickToDismissListener(Layer.OnClickListener { _, v -> this.it(v) }, viewId)
    } ?: addOnClickToDismissListener(null, viewId)
}

fun <T : Layer> T.doOnLongClick(@IdRes viewId: Int, onLongClickListener: T.(view: View) -> Boolean) = this.apply {
    this.addOnLongClickListener(Layer.OnLongClickListener { _, v -> this.onLongClickListener(v) }, viewId)
}

fun <T : Layer> T.dismissOnLongClick(@IdRes viewId: Int, onLongClickListener: (T.(view: View) -> Boolean)? = null) = this.apply {
    onLongClickListener?.let {
        this.addOnLongClickToDismissListener(Layer.OnLongClickListener { _, v -> this.it(v) }, viewId)
    } ?: addOnLongClickToDismissListener(null, viewId)
}

fun <T : Layer> T.doBindData(dataBinder: T.() -> Unit) = this.apply {
    this.addDataBindCallback { this.dataBinder() }
}

fun <T : Layer> T.doOnInitialize(onInitialize: T.() -> Unit) = this.apply {
    this.addOnInitializeListener { this.onInitialize() }
}

fun <T : Layer> T.doOnShow(onShow: T.() -> Unit) = this.apply {
    this.addOnVisibleChangeListener(object : DefaultOnVisibleChangeListener() {
        override fun onShow(layer: Layer) {
            onShow.invoke(this@apply)
        }
    })
}

fun <T : Layer> T.doOnDismiss(onDismiss: T.() -> Unit) = this.apply {
    this.addOnVisibleChangeListener(object : DefaultOnVisibleChangeListener() {
        override fun onDismiss(layer: Layer) {
            onDismiss.invoke(this@apply)
        }
    })
}

fun <T : Layer> T.doOnPreShow(onPreShow: T.() -> Unit) = this.apply {
    this.addOnShowListener(object : DefaultOnShowListener() {
        override fun onPreShow(layer: Layer) {
            this@apply.onPreShow()
        }
    })
}

fun <T : Layer> T.doOnPostShow(onPostShow: T.() -> Unit) = this.apply {
    this.addOnShowListener(object : DefaultOnShowListener() {
        override fun onPostShow(layer: Layer) {
            this@apply.onPostShow()
        }
    })
}

fun <T : Layer> T.doOnPreDismiss(onPreDismiss: T.() -> Unit) = this.apply {
    this.addOnDismissListener(object : DefaultOnDismissListener() {
        override fun onPreDismiss(layer: Layer) {
            this@apply.onPreDismiss()
        }
    })
}

fun <T : Layer> T.doOnPostDismiss(onPostDismiss: T.() -> Unit) = this.apply {
    this.addOnDismissListener(object : DefaultOnDismissListener() {
        override fun onPostDismiss(layer: Layer) {
            this@apply.onPostDismiss()
        }
    })
}

fun <T : Layer, R : Animator?> T.animator(
        onIn: T.(target: View) -> R,
        onOut: T.(target: View) -> R
) = this.apply {
    this.setAnimator(object : Layer.AnimatorCreator {
        override fun createInAnimator(target: View): Animator? {
            return onIn.invoke(this@apply, target)
        }

        override fun createOutAnimator(target: View): Animator? {
            return onOut.invoke(this@apply, target)
        }
    })
}

fun <T : Layer> T.animator(creator: Layer.AnimatorCreator) = this.apply {
    this.setAnimator(creator)
}

fun <T : Layer> T.interceptKeyEvent(enable: Boolean) = this.apply {
    this.setInterceptKeyEvent(enable)
}

fun <T : Layer> T.cancelableOnClickKeyBack(enable: Boolean) = this.apply {
    this.setCancelableOnKeyBack(enable)
}