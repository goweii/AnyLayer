package per.goweii.anylayer;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public final class Align {
    /**
     * 主方向
     */
    public enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * 水平对齐方式
     */
    public enum Horizontal {
        CENTER,
        TO_LEFT,
        TO_RIGHT,
        ALIGN_LEFT,
        ALIGN_RIGHT,
        ALIGN_PARENT_LEFT,
        ALIGN_PARENT_RIGHT
    }

    /**
     * 垂直对齐方式
     */
    public enum Vertical {
        CENTER,
        ABOVE,
        BELOW,
        ALIGN_TOP,
        ALIGN_BOTTOM,
        ALIGN_PARENT_TOP,
        ALIGN_PARENT_BOTTOM
    }
}
