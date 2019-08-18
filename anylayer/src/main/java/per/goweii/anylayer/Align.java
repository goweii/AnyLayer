package per.goweii.anylayer;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class Align {
    public enum Direction {
        /**
         * 主方向
         */
        HORIZONTAL,
        VERTICAL
    }

    public enum Horizontal {
        /**
         * 水平对齐方式
         */
        CENTER,
        TO_LEFT,
        TO_RIGHT,
        ALIGN_LEFT,
        ALIGN_RIGHT
    }

    public enum Vertical {
        /**
         * 垂直对齐方式
         */
        CENTER,
        ABOVE,
        BELOW,
        ALIGN_TOP,
        ALIGN_BOTTOM
    }
}
