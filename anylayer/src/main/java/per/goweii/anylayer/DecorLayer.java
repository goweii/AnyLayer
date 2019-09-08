package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

    public DecorLayer(Activity activity) {
        super();
        Utils.requireNonNull(activity, "activity == null");
        mActivity = activity;
        getViewHolder().setDecor((FrameLayout) activity.getWindow().getDecorView());
    }

    protected Level getLevel() {
        return Level.DIALOG;
    }

    public Activity getActivity() {
        Utils.requireNonNull(mActivity, "activity == null");
        return mActivity;
    }

    @Override
    protected ViewHolder onCreateViewHolder() {
        return new ViewHolder();
    }

    @Override
    public ViewHolder getViewHolder() {
        return (ViewHolder) super.getViewHolder();
    }

    @Override
    protected Config onCreateConfig() {
        return new Config();
    }

    @Override
    public Config getConfig() {
        return (Config) super.getConfig();
    }

    @Override
    protected ListenerHolder onCreateListenerHolder() {
        return new ListenerHolder();
    }

    @Override
    public ListenerHolder getListenerHolder() {
        return (ListenerHolder) super.getListenerHolder();
    }

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
                } else if (getLevel().level() > levelLayout.getLevel().level()) {
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
    public void onPreRemove() {
        super.onPreRemove();
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
    protected enum Level {
        /**
         * 悬浮窗
         */
        FLOAT(1),
        /**
         * 吐司
         */
        TOAST(2),
        /**
         * 弹窗
         */
        DIALOG(3),
        /**
         * PopupWindow
         */
        POPUP(4),
        /**
         * 引导层
         */
        GUIDE(5);

        private final int level;

        Level(int level) {
            this.level = level;
        }

        protected int level() {
            return level;
        }
    }

    /**
     * 各个层级浮层的容器，直接添加进DecorView
     */
    @SuppressLint("ViewConstructor")
    public static class LayerLayout extends FrameLayout {
        public LayerLayout(Context context) {
            super(context);
        }
    }

    /**
     * 控制浮层上下层级的容器
     */
    @SuppressLint("ViewConstructor")
    public static class LevelLayout extends FrameLayout {
        private final Level mLevel;

        public LevelLayout(Context context, Level level) {
            super(context);
            mLevel = level;
        }

        public Level getLevel() {
            return mLevel;
        }
    }
}
