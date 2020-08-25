package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class DecorLayer extends Layer implements ComponentCallbacks, ViewTreeObserver.OnGlobalLayoutListener {

    private final Activity mActivity;

    public DecorLayer(@NonNull Activity activity) {
        super();
        mActivity = activity;
        getViewHolder().setDecor((FrameLayout) activity.getWindow().getDecorView());
    }

    @NonNull
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
                } else if (Level.isATopThanB(levelLayout.getLevel(), getLevel())) {
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
        getActivity().registerComponentCallbacks(this);
        getViewHolder().getDecor().getViewTreeObserver().addOnGlobalLayoutListener(this);
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
            getViewHolder().getDecor().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewHolder().getDecor().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        getActivity().unregisterComponentCallbacks(this);
        super.onDetach();
        final LayerLayout group = findLayerLayoutFromDecor();
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
            removeLayerLayoutFromDecor(group);
        }
    }

    @Override
    public void onGlobalLayout() {
        final ViewGroup decor = getViewHolder().getDecor();
        int count = decor.getChildCount();
        if (count < 2) {
            return;
        }
        LayerLayout layerLayout = findLayerLayoutFromDecor();
        if (layerLayout == null) {
            return;
        }
        int index = decor.indexOfChild(layerLayout);
        if (index < 0) {
            return;
        }
        if (index == count - 1) {
            return;
        }
        layerLayout.bringToFront();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
    }

    @Override
    public void onLowMemory() {
    }

    @Nullable
    private LayerLayout findLayerLayoutFromDecor() {
        final ViewGroup decor = getViewHolder().getDecor();
        LayerLayout layerLayout = null;
        final int count = decor.getChildCount();
        for (int i = count; i >= 0; i--) {
            View child = decor.getChildAt(i);
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
                if (getLevel() == levelLayout.getLevel()) {
                    parent = levelLayout;
                    break;
                }
            }
        }
        return parent;
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

    @NonNull
    public DecorLayer cancelableOnClickKeyBack(boolean cancelable) {
        cancelableOnKeyBack(cancelable);
        return this;
    }

    public static class ViewHolder extends Layer.ViewHolder {
        private FrameLayout mDecor;

        public void setDecor(@NonNull FrameLayout decor) {
            mDecor = decor;
        }

        @NonNull
        public FrameLayout getDecor() {
            return mDecor;
        }

        @NonNull
        @Override
        public LevelLayout getParent() {
            return (LevelLayout) super.getParent();
        }
    }

    protected static class Config extends Layer.Config {
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
        public static final int TOAST = 5000;
        public static final int NOTIFICATION = 6000;

        public static boolean isATopThanB(int a, int b) {
            return a > b;
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
            return Level.isATopThanB(mLevel, other.mLevel);
        }
    }
}
