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
public abstract class DecorLayer extends Layer implements ViewManager.OnLifeListener, ViewManager.OnKeyListener, ViewManager.OnPreDrawListener {

    protected final Context mContext;
    protected final ViewGroup mDecor;

    public DecorLayer(@NonNull Activity activity) {
        super();
        mContext = activity;
        mDecor = (FrameLayout) activity.getWindow().getDecorView();
    }

    @Level
    protected abstract int getLevel();

    @Nullable
    public Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    protected ViewGroup onGetParent() {
        final ViewGroup parent = mDecor;
        LevelLayout container = null;
        int lastIndex = 0;
        final int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            lastIndex = i;
            View child = parent.getChildAt(i);
            if (child instanceof LevelLayout) {
                LevelLayout levelLayout = (LevelLayout) child;
                if (getLevel() == levelLayout.getLevel()) {
                    container = levelLayout;
                    break;
                } else if (getLevel() > levelLayout.getLevel()) {
                    lastIndex--;
                    break;
                }
            }
        }
        if (container == null) {
            container = new LevelLayout(parent.getContext(), getLevel());
            container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            parent.addView(container, lastIndex + 1);
        }
        return container;
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
        final ViewGroup parent = mDecor;
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

    @IntDef({
            Level.TOAST,
            Level.DIALOG,
            Level.POPUP,
            Level.GUIDE
    })
    public @interface Level {
        int TOAST = 1;
        int DIALOG = 2;
        int POPUP = 3;
        int GUIDE = 4;
    }

    @SuppressLint("ViewConstructor")
    private static class LevelLayout extends FrameLayout {

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
