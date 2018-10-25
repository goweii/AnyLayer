package per.goweii.anylayer.listener;

import per.goweii.anylayer.AnyLayer;

/**
 * @author damai
 */
public interface OnVisibleChangeListener {
    void onShow(AnyLayer anyLayer);
    void onDismiss(AnyLayer anyLayer);
}
