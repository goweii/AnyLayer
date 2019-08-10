package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class DecorLayer extends Layer {

    private final Activity mActivity;

    public DecorLayer(@NonNull Activity activity) {
        super();
        mActivity = activity;
        getViewHolder().setDecor((FrameLayout) activity.getWindow().getDecorView());
    }

    @Level
    protected int getLevel() {
        return Level.DIALOG;
    }

    @NonNull
    public Activity getActivity() {
        return mActivity;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder() {
        return new ViewHolder();
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder() {
        return (ViewHolder) super.getViewHolder();
    }

    @NonNull
    @Override
    protected Config onCreateConfig() {
        return new Config();
    }

    @NonNull
    @Override
    public Config getConfig() {
        return (Config) super.getConfig();
    }

    @NonNull
    @Override
    protected ListenerHolder onCreateListenerHolder() {
        return new ListenerHolder();
    }

    @NonNull
    @Override
    public ListenerHolder getListenerHolder() {
        return (ListenerHolder) super.getListenerHolder();
    }

    @NonNull
    @Override
    protected ViewGroup onGetParent() {
        LayerLayout group = findLayerLayoutFromDecor();
        if (group == null) {
            group = addNewLayerLayoutToDecor();
        }
        LevelLayout parent = null;
        int lastIndex = -1;
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            lastIndex = i;
            View child = group.getChildAt(i);
            if (child instanceof LevelLayout) {
                LevelLayout levelLayout = (LevelLayout) child;
                if (getLevel() == levelLayout.getLevel()) {
                    parent = levelLayout;
                    break;
                } else if (getLevel() > levelLayout.getLevel()) {
                    lastIndex--;
                    break;
                }
            }
        }
        if (parent == null) {
            parent = new LevelLayout(group.getContext(), getLevel());
            parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            group.addView(parent, lastIndex + 1);
        }
        getViewHolder().setParent(parent);
        return parent;
    }

    @Override
    public void onAttach() {
        super.onAttach();
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
    }

    @Override
    public void onShow() {
        super.onShow();
    }

    @Override
    public void onPerRemove() {
        super.onPerRemove();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        final LayerLayout group = findLayerLayoutFromDecor();
        if (group == null) {
            return;
        }
        LevelLayout parent = null;
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = group.getChildAt(i);
            if (child instanceof LevelLayout) {
                LevelLayout levelLayout = (LevelLayout) child;
                if (getLevel() == levelLayout.getLevel()) {
                    parent = levelLayout;
                    break;
                }
            }
        }
        if (parent == null) {
            return;
        }
        if (parent.getChildCount() == 0) {
            group.removeView(parent);
        }
        if (group.getChildCount() == 0) {
            removeLayerLayoutFromDecor(group);
        }
    }

    @Nullable
    private LayerLayout findLayerLayoutFromDecor() {
        final ViewGroup decor = getViewHolder().mDecor;
        LayerLayout layerLayout = null;
        final int count = decor.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = decor.getChildAt(i);
            if (child instanceof LayerLayout) {
                layerLayout = (LayerLayout) child;
                break;
            }
        }
        return layerLayout;
    }

    @NonNull
    private LayerLayout addNewLayerLayoutToDecor() {
        final ViewGroup decor = getViewHolder().mDecor;
        LayerLayout layerLayout = new LayerLayout(decor.getContext());
        layerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        decor.addView(layerLayout, decor.getChildCount());
        return layerLayout;
    }

    private void removeLayerLayoutFromDecor(LayerLayout layerLayout) {
        final ViewGroup decor = getViewHolder().mDecor;
        decor.removeView(layerLayout);
    }

    public DecorLayer cancelableOnClickKeyBack(boolean cancelable) {
        cancelableOnKeyBack(cancelable);
        return this;
    }

    public static class ViewHolder extends Layer.ViewHolder {
        private FrameLayout mDecor;

        public void setDecor(FrameLayout decor) {
            mDecor = decor;
        }

        public FrameLayout getDecor() {
            return mDecor;
        }
    }

    protected static class Config extends Layer.Config {
    }

    protected static class ListenerHolder extends Layer.ListenerHolder {
    }

    /**
     * 浮层层级
     * 数字越小层级越高，显示在越上层
     */
    @IntDef({
            Level.TOAST,
            Level.DIALOG,
            Level.GUIDE
    })
    protected @interface Level {
        int TOAST = 1;
        int DIALOG = 2;
        int GUIDE = 3;
    }

    /**
     * 各个层级浮层的容器，直接添加进DecorView
     */
    @SuppressLint("ViewConstructor")
    public static class LayerLayout extends FrameLayout {
        public LayerLayout(@NonNull Context context) {
            super(context);
        }
    }

    /**
     * 控制浮层上下层级的容器
     */
    @SuppressLint("ViewConstructor")
    public static class LevelLayout extends FrameLayout {
        private final int mLevel;

        public LevelLayout(@NonNull Context context, @Level int level) {
            super(context);
            mLevel = level;
        }

        public int getLevel() {
            return mLevel;
        }
    }
}
