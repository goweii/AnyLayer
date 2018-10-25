package per.goweii.anylayer.listener;

import android.view.View;

/**
 * @author damai
 * @date 2018/5/20
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public interface IAnim {
    /**
     * 内容进入动画
     *
     * @param target 内容
     * @return 动画时长
     */
    long inAnim(View target);

    /**
     * 内容消失动画
     *
     * @param target 内容
     * @return 动画时长
     */
    long outAnim(View target);
}
