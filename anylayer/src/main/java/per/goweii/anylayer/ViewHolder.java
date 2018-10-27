package per.goweii.anylayer;

import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author Cuizhen
 * @date 2018/5/20
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
class ViewHolder {

    private final AnyLayer mAnyLayer;
    private FrameLayout mContainer;
    private FrameLayout mContentWrapper;
    private ImageView mBackground;

    private SparseArray<View> views = null;
    private SparseArray<AnyLayer.OnLayerClickListener> onClickListeners = null;

    ViewHolder(AnyLayer anyLayer, FrameLayout container) {
        this.mAnyLayer = anyLayer;
        this.mContainer = container;
        mContentWrapper = mContainer.findViewById(R.id.fl_content_wrapper);
        mBackground = mContainer.findViewById(R.id.iv_background);
    }

    void recycle(){
        if (mBackground.getDrawable() instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) mBackground.getDrawable();
            bd.getBitmap().recycle();
        }
    }

    FrameLayout getContainer() {
        return mContainer;
    }

    FrameLayout getContentWrapper() {
        return mContentWrapper;
    }

    ImageView getBackground() {
        return mBackground;
    }

    void bindListener() {
        if (onClickListeners == null) {
            return;
        }
        for (int i = 0; i < onClickListeners.size(); i++) {
            int viewId = onClickListeners.keyAt(i);
            final AnyLayer.OnLayerClickListener listener = onClickListeners.valueAt(i);
            getView(viewId).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(mAnyLayer, v);
                }
            });
        }
    }

    <V extends View> V getView(@IdRes int viewId) {
        if (views == null) {
            views = new SparseArray<>();
        }
        if (views.indexOfKey(viewId) < 0) {
            V view = mContainer.findViewById(viewId);
            views.put(viewId, view);
            return view;
        }
        return (V) views.get(viewId);
    }

    void addOnClickListener(AnyLayer.OnLayerClickListener listener, @IdRes int viewId, @IdRes int... viewIds) {
        if (onClickListeners == null) {
            onClickListeners = new SparseArray<>();
        }
        if (onClickListeners.indexOfKey(viewId) < 0) {
            onClickListeners.put(viewId, listener);
        }
        if (viewIds != null && viewIds.length > 0) {
            for (int id : viewIds) {
                if (onClickListeners.indexOfKey(id) < 0) {
                    onClickListeners.put(id, listener);
                }
            }
        }
    }
}