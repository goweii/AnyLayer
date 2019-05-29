package per.goweii.anylayer;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描述：为方便采用继承的方式创建一个浮层
 * 方便通用浮层的封装，可再次基础上实现MVP模式
 *
 * @author Cuizhen
 * @date 2018/12/10
 */
public abstract class BaseLayer implements LayerManager.OnVisibleChangeListener {

    protected AnyLayer mAnyLayer = null;

    /**
     * 获取布局资源文件
     */
    @LayoutRes
    protected abstract int getLayoutId();

    @Nullable
    protected Context getContext() {
        return null;
    }

    @Nullable
    protected ViewGroup getParent() {
        return null;
    }

    @Nullable
    protected View getTarget() {
        return null;
    }

    protected void onCreateLayer() {
        if (getTarget() != null) {
            mAnyLayer = AnyLayer.target(getTarget());
        } else if (getParent() != null) {
            mAnyLayer = AnyLayer.with(getParent());
        } else if (getContext() != null) {
            mAnyLayer = AnyLayer.with(getContext());
        } else {
            mAnyLayer = AnyLayer.with();
        }
        mAnyLayer.contentView(getLayoutId());
        mAnyLayer.onVisibleChangeListener(this);
    }

    /**
     * 浮层已创建，可在这里进行浮层的初始化和数据绑定
     */
    protected void onLayerCreated() {
    }

    /**
     * 浮层已销毁，可在这里进行资源释放
     *
     * @return 返回true表示销毁资源下次会重新创建浮层
     */
    protected boolean onDestroy() {
        return true;
    }

    @Override
    public void onShow(AnyLayer anyLayer) {
        onLayerCreated();
    }

    @Override
    public void onDismiss(AnyLayer anyLayer) {
        if (onDestroy()) {
            mAnyLayer = null;
        }
    }

    public void show() {
        if (mAnyLayer == null) {
            onCreateLayer();
        }
        mAnyLayer.show();
    }

    public void dismiss() {
        if (mAnyLayer != null) {
            mAnyLayer.dismiss();
        }
    }
}
