package per.goweii.anylayer.ktx

import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.view.View
import per.goweii.anylayer.toast.ToastLayer

fun <T : ToastLayer> T.setContentView(contentView: View) = this.apply {
    this.contentView(contentView)
}

fun <T : ToastLayer> T.setContentView(@LayoutRes contentViewId: Int) = this.apply {
    this.contentView(contentViewId)
}

fun <T : ToastLayer> T.setTextColorInt(@ColorInt colorInt: Int) = this.apply {
    this.textColorInt(colorInt)
}

fun <T : ToastLayer> T.setTextColorRes(@ColorRes colorRes: Int) = this.apply {
    this.textColorRes(colorRes)
}

fun <T : ToastLayer> T.setRemoveOthers(removeOthers: Boolean) = this.apply {
    this.removeOthers(removeOthers)
}

fun <T : ToastLayer> T.setDuration(duration: Long) = this.apply {
    this.duration(duration)
}

fun <T : ToastLayer> T.setMessage(message: CharSequence) = this.apply {
    this.message(message)
}

fun <T : ToastLayer> T.setMessage(@StringRes message: Int) = this.apply {
    this.message(message)
}

fun <T : ToastLayer> T.setIcon(@DrawableRes icon: Int) = this.apply {
    this.icon(icon)
}

fun <T : ToastLayer> T.setGravity(gravity: Int) = this.apply {
    this.gravity(gravity)
}

fun <T : ToastLayer> T.setMarginLeft(margin: Int) = this.apply {
    this.marginLeft(margin)
}

fun <T : ToastLayer> T.setMarginTop(margin: Int) = this.apply {
    this.marginTop(margin)
}

fun <T : ToastLayer> T.setMarginRight(margin: Int) = this.apply {
    this.marginRight(margin)
}

fun <T : ToastLayer> T.setMarginBottom(margin: Int) = this.apply {
    this.marginBottom(margin)
}

fun <T : ToastLayer> T.setAlpha(alpha: Float) = this.apply {
    this.alpha(alpha)
}

fun <T : ToastLayer> T.setBackgroundResource(@DrawableRes resource: Int) = this.apply {
    this.backgroundResource(resource)
}

fun <T : ToastLayer> T.setBackgroundDrawable(drawable: Drawable) = this.apply {
    this.backgroundResource(drawable)
}

fun <T : ToastLayer> T.setBackgroundColorInt(@ColorInt colorInt: Int) = this.apply {
    this.backgroundColorInt(colorInt)
}

fun <T : ToastLayer> T.setBackgroundColorRes(@ColorRes colorRes: Int) = this.apply {
    this.backgroundColorRes(colorRes)
}
