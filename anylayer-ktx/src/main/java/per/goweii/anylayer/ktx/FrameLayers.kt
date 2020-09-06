package per.goweii.anylayer.ktx

import per.goweii.anylayer.FrameLayer

/**
 * @author CuiZhen
 * @date 2020/9/6
 */

fun <T : FrameLayer> T.setLevel(level: Int) = this.apply {
    this.level(level)
}

fun <T : FrameLayer> T.setCancelableOnClickKeyBack(enable: Boolean) = this.apply {
    this.cancelableOnClickKeyBack(enable)
}