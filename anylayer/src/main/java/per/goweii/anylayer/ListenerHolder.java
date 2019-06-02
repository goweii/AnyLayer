package per.goweii.anylayer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
final class ListenerHolder {

    private List<LayerManager.IDataBinder> mIDataBinders = null;
    private List<LayerManager.OnVisibleChangeListener> mOnVisibleChangeListeners = null;
    private List<LayerManager.OnLayerShowListener> mOnLayerShowListeners = null;
    private List<LayerManager.OnLayerDismissListener> mOnLayerDismissListeners = null;

    void addDataBinder(LayerManager.IDataBinder dataBinder){
        if (mIDataBinders == null) {
            mIDataBinders = new ArrayList<>(1);
        }
        mIDataBinders.add(dataBinder);
    }

    void addOnVisibleChangeListener(LayerManager.OnVisibleChangeListener onVisibleChangeListener){
        if (mOnVisibleChangeListeners == null) {
            mOnVisibleChangeListeners = new ArrayList<>(1);
        }
        mOnVisibleChangeListeners.add(onVisibleChangeListener);
    }

    void addOnLayerShowListener(LayerManager.OnLayerShowListener onLayerShowListener){
        if (mOnLayerShowListeners == null) {
            mOnLayerShowListeners = new ArrayList<>(1);
        }
        mOnLayerShowListeners.add(onLayerShowListener);
    }

    void addOnLayerDismissListener(LayerManager.OnLayerDismissListener onLayerDismissListener){
        if (mOnLayerDismissListeners == null) {
            mOnLayerDismissListeners = new ArrayList<>(1);
        }
        mOnLayerDismissListeners.add(onLayerDismissListener);
    }

    void notifyDataBinder(AnyLayer anyLayer){
        if (mIDataBinders != null) {
            for (LayerManager.IDataBinder dataBinder : mIDataBinders) {
                dataBinder.bind(anyLayer);
            }
        }
    }

    void notifyVisibleChangeOnShow(AnyLayer anyLayer){
        if (mOnVisibleChangeListeners != null) {
            for (LayerManager.OnVisibleChangeListener onVisibleChangeListener : mOnVisibleChangeListeners) {
                onVisibleChangeListener.onShow(anyLayer);
            }
        }
    }

    void notifyVisibleChangeOnDismiss(AnyLayer anyLayer){
        if (mOnVisibleChangeListeners != null) {
            for (LayerManager.OnVisibleChangeListener onVisibleChangeListener : mOnVisibleChangeListeners) {
                onVisibleChangeListener.onDismiss(anyLayer);
            }
        }
    }

    void notifyLayerOnShowing(AnyLayer anyLayer){
        if (mOnLayerShowListeners != null) {
            for (LayerManager.OnLayerShowListener onLayerShowListener : mOnLayerShowListeners) {
                onLayerShowListener.onShowing(anyLayer);
            }
        }
    }

    void notifyLayerOnShown(AnyLayer anyLayer){
        if (mOnLayerShowListeners != null) {
            for (LayerManager.OnLayerShowListener onLayerShowListener : mOnLayerShowListeners) {
                onLayerShowListener.onShown(anyLayer);
            }
        }
    }

    void notifyLayerOnDismissing(AnyLayer anyLayer){
        if (mOnLayerDismissListeners != null) {
            for (LayerManager.OnLayerDismissListener onLayerDismissListener : mOnLayerDismissListeners) {
                onLayerDismissListener.onDismissing(anyLayer);
            }
        }
    }

    void notifyLayerOnDismissed(AnyLayer anyLayer){
        if (mOnLayerDismissListeners != null) {
            for (LayerManager.OnLayerDismissListener onLayerDismissListener : mOnLayerDismissListeners) {
                onLayerDismissListener.onDismissed(anyLayer);
            }
        }
    }
}
