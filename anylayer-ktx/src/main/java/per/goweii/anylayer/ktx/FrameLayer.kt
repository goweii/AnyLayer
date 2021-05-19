package per.goweii.anylayer.ktx

import per.goweii.anylayer.FrameLayer

fun <T : FrameLayer> T.level(level: Int) = this.apply {
    this.setLevel(level)
}

fun <T : FrameLayer> T.cancelableOnClickKeyBack(enable: Boolean) = this.apply {
    this.setCancelableOnClickKeyBack(enable)
}