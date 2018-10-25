package per.goweii.anylayer.listener;

import per.goweii.anylayer.AnyLayer;

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2018/10/24
 */
public interface OnLayerDismissListener {
    void onDismissing(AnyLayer anyLayer);
    void onDismissed(AnyLayer anyLayer);
}
