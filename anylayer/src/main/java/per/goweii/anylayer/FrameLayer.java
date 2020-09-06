package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class FrameLayer extends Layer implements ViewTreeObserver.OnGlobalLayoutListener {

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
        getViewHolder().setParent(parent);
        return parent;
    }

    @Override
    public void onAttach() {
        super.onAttach();
        getViewHolder().getRoot().getViewTreeObserver().addOnGlobalLayoutListener(this);
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
    public void onPreRemove() {
        super.onPreRemove();
    }

    @Override
    public void onDetach() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewHolder().getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewHolder().getRoot().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        super.onDetach();
        final LayerLayout group = findLayerLayoutFromRoot();
        if (group == null) {
            return;
        }
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

    @Override
    public void onGlobalLayout() {
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
    public FrameLayer level(int level) {
        getConfig().mLevel = level;
        return this;
    }

    @NonNull
    public FrameLayer cancelableOnClickKeyBack(boolean cancelable) {
        cancelableOnKeyBack(cancelable);
        return this;
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
        public static final int FLOAT = 4000;
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
