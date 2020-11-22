package per.goweii.anylayer.ktx

import per.goweii.anylayer.FrameLayer

fun <T : FrameLayer> T.setLevel(level: Int) = this.apply {
    this.level(level)
}

fun <T : FrameLayer> T.setCancelableOnClickKeyBack(enable: Boolean) = this.apply {
    this.cancelableOnClickKeyBack(enable)
}