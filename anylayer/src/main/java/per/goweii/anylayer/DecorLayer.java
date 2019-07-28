package per.goweii.anylayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
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
        getViewHolder().mDecor = (FrameLayout) activity.getWindow().getDecorView();
    }

    public DecorLayer cancelableOnClickKeyBack(boolean cancelable) {
        cancelableOnKeyBack(cancelable);
        return this;
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
    protected ViewGroup onGetParent() {
        final ViewGroup decor = getViewHolder().mDecor;
        LevelLayout parent = null;
        int lastIndex = 0;
        final int count = decor.getChildCount();
        for (int i = 0; i < count; i++) {
            lastIndex = i;
            View child = decor.getChildAt(i);
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
            parent = new LevelLayout(decor.getContext(), getLevel());
            parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            decor.addView(parent, lastIndex + 1);
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
        final ViewGroup parent = getViewHolder().mDecor;
        LevelLayout container = null;
        final int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof LevelLayout) {
                LevelLayout levelLayout = (LevelLayout) child;
                if (getLevel() == levelLayout.getLevel()) {
                    container = levelLayout;
                    break;
                }
            }
        }
        if (container != null) {
            if (container.getChildCount() == 0) {
                parent.removeView(container);
            }
        }
    }

    protected static class Config extends Layer.Config {
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
