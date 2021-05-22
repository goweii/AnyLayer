package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @CallSuper
    @Override
    protected void onPreShow() {
        super.onPreShow();
    }

    @CallSuper
    @Override
    protected void onPostShow() {
        super.onPostShow();
    }

    @CallSuper
    @Override
    protected void onPreDismiss() {
        super.onPreDismiss();
    }

    @CallSuper
    @Override
    protected void onPostDismiss() {
        super.onPostDismiss();
    }

    @CallSuper
    @Override
    protected void onDetach() {
        super.onDetach();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    protected void fitDecorInsidesToViewPadding(@NonNull View view) {
        Rect padding = getDecorPadding();
        Rect margin = getDecorMargin();
        padding.left += margin.left;
        padding.top += margin.top;
        padding.right += margin.right;
        padding.bottom += margin.bottom;
        setViewPaddingTo(view, margin);
        padding.setEmpty();
        margin.setEmpty();
    }

    protected void fitDecorInsidesToViewMargin(@NonNull View view) {
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

    protected void fitDecorInsidesTo(@NonNull View view) {
        fitDecorPaddingTo(view);
        fitDecorMarginTo(view);
    }

    protected void fitDecorPaddingTo(@NonNull View view) {
        Rect padding = getDecorPadding();
        setViewPaddingTo(view, padding);
        padding.setEmpty();
    }

    protected void fitDecorMarginTo(@NonNull View view) {
        Rect margin = getDecorMargin();
        setViewMarginTo(view, margin);
        margin.setEmpty();
    }

    protected void setViewPaddingTo(@NonNull View view, @NonNull Rect padding) {
        if (view.getPaddingLeft() != padding.left &&
                view.getPaddingTop() != padding.top &&
                view.getPaddingRight() != padding.right &&
                view.getPaddingBottom() != padding.bottom) {
            view.setPadding(padding.left, padding.top, padding.right, padding.bottom);
        }
    }

    protected void setViewMarginTo(@NonNull View view, @NonNull Rect margin) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        boolean needRequestLayout = false;
        if (params.leftMargin != margin.left) {
            params.leftMargin = margin.left;
            needRequestLayout = true;
        }
        if (params.topMargin != margin.top) {
            params.topMargin = margin.top;
            needRequestLayout = true;
        }
        if (params.rightMargin != margin.right) {
            params.rightMargin = margin.right;
            needRequestLayout = true;
        }
        if (params.bottomMargin != margin.bottom) {
            params.bottomMargin = margin.bottom;
            needRequestLayout = true;
        }
        if (needRequestLayout) {
            view.requestLayout();
        }
    }

    @NonNull
    protected Rect getDecorPadding() {
        View decorChild = getViewHolder().getDecorChild();
        mDecorPadding.left = decorChild.getPaddingLeft();
        mDecorPadding.top = decorChild.getPaddingTop();
        mDecorPadding.right = decorChild.getPaddingRight();
        mDecorPadding.bottom = decorChild.getPaddingBottom();
        return mDecorPadding;
    }

    @NonNull
    protected Rect getDecorMargin() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getViewHolder().getDecorChild().getLayoutParams();
        mDecorMargin.left = params.leftMargin;
        mDecorMargin.top = params.topMargin;
        mDecorMargin.right = params.rightMargin;
        mDecorMargin.bottom = params.bottomMargin;
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
    public void show(boolean withAnim) {
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
    public void dismiss() {
        if (mShowRunnable != null) {
            getViewHolder().getDecor().removeCallbacks(mShowRunnable);
            mShowRunnable = null;
        } else {
            super.dismiss();
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
