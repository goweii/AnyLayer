package per.goweii.anylayer.ktx

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import per.goweii.anylayer.toast.ToastLayer

fun <T : ToastLayer> T.contentView(contentView: View) = this.apply {
    this.setContentView(contentView)
}

fun <T : ToastLayer> T.contentView(@LayoutRes contentViewId: Int) = this.apply {
    this.setContentView(contentViewId)
}

fun <T : ToastLayer> T.textColorInt(@ColorInt colorInt: Int) = this.apply {
    this.setTextColorInt(colorInt)
}

fun <T : ToastLayer> T.textColorRes(@ColorRes colorRes: Int) = this.apply {
    this.setTextColorRes(colorRes)
}

fun <T : ToastLayer> T.removeOthers(removeOthers: Boolean) = this.apply {
    this.setRemoveOthers(removeOthers)
}

fun <T : ToastLayer> T.duration(duration: Long) = this.apply {
    this.setDuration(duration)
}

fun <T : ToastLayer> T.message(message: CharSequence) = this.apply {
    this.setMessage(message)
}

fun <T : ToastLayer> T.message(@StringRes message: Int) = this.apply {
    this.setMessage(message)
}

fun <T : ToastLayer> T.icon(@DrawableRes icon: Int) = this.apply {
    this.setIcon(icon)
}

fun <T : ToastLayer> T.gravity(gravity: Int) = this.apply {
    this.setGravity(gravity)
}

fun <T : ToastLayer> T.marginLeft(margin: Int?) = this.apply {
    this.setMarginLeft(margin)
}

fun <T : ToastLayer> T.marginTop(margin: Int?) = this.apply {
    this.setMarginTop(margin)
}

fun <T : ToastLayer> T.marginRight(margin: Int?) = this.apply {
    this.setMarginRight(margin)
}

fun <T : ToastLayer> T.marginBottom(margin: Int?) = this.apply {
    this.setMarginBottom(margin)
}

fun <T : ToastLayer> T.alpha(alpha: Float) = this.apply {
    this.setAlpha(alpha)
}

fun <T : ToastLayer> T.backgroundResource(@DrawableRes resource: Int) = this.apply {
    this.setBackgroundResource(resource)
}

fun <T : ToastLayer> T.backgroundDrawable(drawable: Drawable) = this.apply {
    this.setBackgroundDrawable(drawable)
}

fun <T : ToastLayer> T.backgroundColorInt(@ColorInt colorInt: Int) = this.apply {
    this.setBackgroundColorInt(colorInt)
}

fun <T : ToastLayer> T.backgroundColorRes(@ColorRes colorRes: Int) = this.apply {
    this.setBackgroundColorRes(colorRes)
}
