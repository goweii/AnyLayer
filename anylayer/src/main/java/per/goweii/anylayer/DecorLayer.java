package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;

import per.goweii.anylayer.utils.Utils;

public class DecorLayer extends FrameLayer {
    private final Activity mActivity;

    private final Rect mDecorMargin = new Rect();
    private final Rect mDecorPadding = new Rect();

    private Runnable mShowRunnable = null;

    public DecorLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public DecorLayer(@NonNull Activity activity) {
        super((FrameLayout) activity.getWindow().getDecorView());
        mActivity = activity;
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
    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(mActivity);
    }

    @CallSuper
    @Override
    protected void onAttach() {
        super.onAttach();
        fitDecorInsides();
    }

    @Override
    protected void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Utils.onViewLayout(getViewHolder().getChild(), new Runnable() {
            @Override
            public void run() {
                fitDecorInsides();
            }
        });
    }

    protected void fitDecorInsides() {
        fitDecorInsidesToViewPadding(getViewHolder().getParent());
        getViewHolder().getParent().setClipToPadding(false);
        getViewHolder().getParent().setClipChildren(false);
    }

    protected final void fitDecorInsidesToViewPadding(@NonNull View view) {
        Rect padding = getDecorPadding();
        Rect margin = getDecorMargin();
        padding.left += margin.left;
        padding.top += margin.top;
        padding.right += margin.right;
        padding.bottom += margin.bottom;
        setViewPaddingTo(view, padding);
        padding.setEmpty();
        margin.setEmpty();
    }

    protected final void fitDecorInsidesToViewMargin(@NonNull View view) {
        Rect padding = getDecorPadding();
        Rect margin = getDecorMargin();
        margin.left += padding.left;
        margin.top += padding.top;
        margin.right += padding.right;
        margin.bottom += padding.bottom;
        setViewMarginTo(view, margin);
        padding.setEmpty();
        margin.setEmpty();
    }

    protected final void fitDecorInsidesTo(@NonNull View view) {
        fitDecorPaddingTo(view);
        fitDecorMarginTo(view);
    }

    protected final void fitDecorPaddingTo(@NonNull View view) {
        Rect padding = getDecorPadding();
        setViewPaddingTo(view, padding);
        padding.setEmpty();
    }

    protected final void fitDecorMarginTo(@NonNull View view) {
        Rect margin = getDecorMargin();
        setViewMarginTo(view, margin);
        margin.setEmpty();
    }

    protected final void setViewPaddingTo(@NonNull View view, @NonNull Rect padding) {
        if (view.getPaddingLeft() != padding.left ||
                view.getPaddingTop() != padding.top ||
                view.getPaddingRight() != padding.right ||
                view.getPaddingBottom() != padding.bottom) {
            view.setPadding(padding.left, padding.top, padding.right, padding.bottom);
        }
    }

    protected final void setViewMarginTo(@NonNull View view, @NonNull Rect margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        boolean changed = false;
        if (params.leftMargin != margin.left) {
            params.leftMargin = margin.left;
            changed = true;
        }
        if (params.topMargin != margin.top) {
            params.topMargin = margin.top;
            changed = true;
        }
        if (params.rightMargin != margin.right) {
            params.rightMargin = margin.right;
            changed = true;
        }
        if (params.bottomMargin != margin.bottom) {
            params.bottomMargin = margin.bottom;
            changed = true;
        }
        if (changed) {
            view.requestLayout();
        }
    }

    @NonNull
    protected final Rect getDecorPadding() {
        mDecorPadding.setEmpty();
        Utils.getViewPadding(getViewHolder().getDecorChild(), mDecorPadding);
        return mDecorPadding;
    }

    @NonNull
    protected final Rect getDecorMargin() {
        mDecorMargin.setEmpty();
        View view = getViewHolder().getDecorChild();
        while (true) {
            Utils.getViewMargin(view, mDecorMargin);
            ViewParent viewParent = view.getParent();
            if (!(viewParent instanceof ViewGroup)) {
                break;
            }
            view = (ViewGroup) viewParent;
            Utils.getViewPadding(view, mDecorMargin);
            if (view == getViewHolder().getDecor()) {
                break;
            }
        }
        return mDecorMargin;
    }

    public void showImmediately(boolean withAnim) {
        if (mShowRunnable != null) {
            getViewHolder().getDecor().removeCallbacks(mShowRunnable);
            mShowRunnable = null;
        }
        super.show(withAnim);
    }

    @Override
    public void show(final boolean withAnim) {
        if (mShowRunnable == null) {
            mShowRunnable = new Runnable() {
                @Override
                public void run() {
                    mShowRunnable = null;
                    DecorLayer.super.show(withAnim);
                }
            };
            getViewHolder().getDecor().post(mShowRunnable);
        }
    }

    @Override
    public void dismiss(boolean withAnim) {
        if (mShowRunnable != null) {
            getViewHolder().getDecor().removeCallbacks(mShowRunnable);
            mShowRunnable = null;
        } else {
            super.dismiss(withAnim);
        }
    }

    public static class ViewHolder extends FrameLayer.ViewHolder {
        private FrameLayout mActivityContent;
        private View mDecorChild;

        @Override
        public void setRoot(@NonNull FrameLayout root) {
            super.setRoot(root);
            mDecorChild = root.getChildAt(0);
            mActivityContent = root.findViewById(android.R.id.content);
        }

        @NonNull
        public FrameLayout getDecor() {
            return getRoot();
        }

        @NonNull
        public View getDecorChild() {
            return mDecorChild;
        }

        @NonNull
        public FrameLayout getActivityContent() {
            return mActivityContent;
        }
    }

    protected static class Config extends FrameLayer.Config {
    }

    protected static class ListenerHolder extends FrameLayer.ListenerHolder {
    }
}
