package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

public class FrameLayer extends Layer {
    private final LayerLayout.OnConfigurationChangedListener mOnConfigurationChangedListener = new OnConfigurationChangedListenerImpl();

    public FrameLayer(@NonNull FrameLayout frameLayout) {
        super();
        getViewHolder().setRoot(frameLayout);
    }

    @IntRange(from = 0)
    protected int getLevel() {
        return 0;
    }

    @IntRange(from = 0)
    protected int getRealLevel() {
        if (getConfig().mLevel >= 0) {
            return getConfig().mLevel;
        }
        return getLevel();
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
        LayerLayout group = findLayerLayoutFromRoot();
        if (group == null) {
            group = addNewLayerLayoutToRoot();
        }
        group.registerOnConfigurationChangedListener(mOnConfigurationChangedListener);
        LevelLayout parent = null;
        int lastIndex = -1;
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            lastIndex = i;
            View child = group.getChildAt(i);
            if (child instanceof LevelLayout) {
                LevelLayout levelLayout = (LevelLayout) child;
                if (getRealLevel() == levelLayout.getLevel()) {
                    parent = levelLayout;
                    break;
                } else if (Level.isFirstTopThanSecond(levelLayout.getLevel(), getRealLevel())) {
                    lastIndex--;
                    break;
                }
            }
        }
        if (parent == null) {
            parent = new LevelLayout(group.getContext(), getRealLevel());
            parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            group.addView(parent, lastIndex + 1);
        }
        return parent;
    }

    @Override
    protected void onResetParent() {
        super.onResetParent();
        final LayerLayout group = findLayerLayoutFromRoot();
        if (group == null) {
            return;
        }
        group.unregisterOnConfigurationChangedListener(mOnConfigurationChangedListener);
        final LevelLayout parent = findLevelLayoutFromGroup(group);
        if (parent == null) {
            return;
        }
        if (parent.getChildCount() == 0) {
            group.removeView(parent);
        }
        if (group.getChildCount() == 0) {
            removeLayerLayoutFromRoot(group);
        }
    }

    protected void onConfigurationChanged(@NonNull Configuration newConfig) {
    }

    @Override
    public void onGlobalLayout() {
        super.onGlobalLayout();
        final ViewGroup root = getViewHolder().getRoot();
        int count = root.getChildCount();
        if (count < 2) {
            return;
        }
        LayerLayout layerLayout = findLayerLayoutFromRoot();
        if (layerLayout == null) {
            return;
        }
        int index = root.indexOfChild(layerLayout);
        if (index < 0) {
            return;
        }
        if (index == count - 1) {
            return;
        }
        layerLayout.bringToFront();
    }

    @Nullable
    private LayerLayout findLayerLayoutFromRoot() {
        final ViewGroup root = getViewHolder().getRoot();
        LayerLayout layerLayout = null;
        final int count = root.getChildCount();
        for (int i = count; i >= 0; i--) {
            View child = root.getChildAt(i);
            if (child instanceof LayerLayout) {
                layerLayout = (LayerLayout) child;
                break;
            }
        }
        return layerLayout;
    }

    @Nullable
    private LevelLayout findLevelLayoutFromGroup(LayerLayout group) {
        LevelLayout parent = null;
        final int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = group.getChildAt(i);
            if (child instanceof LevelLayout) {
                LevelLayout levelLayout = (LevelLayout) child;
                if (getRealLevel() == levelLayout.getLevel()) {
                    parent = levelLayout;
                    break;
                }
            }
        }
        return parent;
    }

    @NonNull
    private LayerLayout addNewLayerLayoutToRoot() {
        final ViewGroup root = getViewHolder().getRoot();
        LayerLayout layerLayout = new LayerLayout(root.getContext());
        layerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(layerLayout, root.getChildCount());
        return layerLayout;
    }

    private void removeLayerLayoutFromRoot(LayerLayout layerLayout) {
        final ViewGroup root = getViewHolder().getRoot();
        root.removeView(layerLayout);
    }

    @NonNull
    public FrameLayer setLevel(int level) {
        getConfig().mLevel = level;
        return this;
    }

    @NonNull
    public FrameLayer setCancelableOnClickKeyBack(boolean cancelable) {
        setCancelableOnKeyBack(cancelable);
        return this;
    }

    protected class OnConfigurationChangedListenerImpl implements LayerLayout.OnConfigurationChangedListener {
        @Override
        public void onConfigurationChanged(@NonNull Configuration newConfig) {
            FrameLayer.this.onConfigurationChanged(newConfig);
        }
    }

    public static class ViewHolder extends Layer.ViewHolder {
        private FrameLayout mRoot;

        public void setRoot(@NonNull FrameLayout root) {
            mRoot = root;
        }

        @NonNull
        public FrameLayout getRoot() {
            return mRoot;
        }

        @NonNull
        @Override
        public LevelLayout getParent() {
            return (LevelLayout) super.getParent();
        }
    }

    protected static class Config extends Layer.Config {
        protected int mLevel = -1;
    }

    protected static class ListenerHolder extends Layer.ListenerHolder {
    }

    /**
     * 浮层层级
     * 数字越大层级越高，显示在越上层
     */
    protected static class Level {
        public static final int GUIDE = 1000;
        public static final int POPUP = 2000;
        public static final int DIALOG = 3000;
        public static final int OVERLAY = 4000;
        public static final int NOTIFICATION = 5000;
        public static final int TOAST = 6000;

        public static boolean isFirstTopThanSecond(int first, int second) {
            return first > second;
        }
    }

    /**
     * 各个层级浮层的容器，直接添加进DecorView
     */
    @SuppressLint("ViewConstructor")
    public static class LayerLayout extends FrameLayout {
        private final LinkedList<OnConfigurationChangedListener> mOnConfigurationChangedListeners = new LinkedList<>();

        public LayerLayout(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onConfigurationChanged(@NonNull Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            for (OnConfigurationChangedListener listener : mOnConfigurationChangedListeners) {
                listener.onConfigurationChanged(newConfig);
            }
        }

        protected void registerOnConfigurationChangedListener(@NonNull OnConfigurationChangedListener listener) {
            mOnConfigurationChangedListeners.add(listener);
        }

        protected void unregisterOnConfigurationChangedListener(@NonNull OnConfigurationChangedListener listener) {
            mOnConfigurationChangedListeners.remove(listener);
        }

        public interface OnConfigurationChangedListener {
            void onConfigurationChanged(@NonNull Configuration newConfig);
        }
    }

    /**
     * 控制浮层上下层级的容器
     */
    @SuppressLint("ViewConstructor")
    public static class LevelLayout extends FrameLayout {
        private final int mLevel;

        public LevelLayout(@NonNull Context context, int level) {
            super(context);
            mLevel = level;
        }

        public int getLevel() {
            return mLevel;
        }

        public boolean isTopThan(@NonNull LevelLayout other) {
            return Level.isFirstTopThanSecond(mLevel, other.mLevel);
        }
    }
}
