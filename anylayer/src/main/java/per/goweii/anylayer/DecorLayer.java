package per.goweii.anylayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Size;

import per.goweii.anylayer.utils.Utils;

public class DecorLayer extends FrameLayer {
    private final Activity mActivity;
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
    protected void onAppear() {
        super.onAppear();
    }

    @CallSuper
    @Override
    protected void onShow() {
        super.onShow();
    }

    @CallSuper
    @Override
    protected void onDismiss() {
        super.onDismiss();
    }

    @CallSuper
    @Override
    protected void onDisappear() {
        super.onDisappear();
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
        final int[] padding = getDecorPadding();
        final int[] margin = getDecorMargin();
        view.setPadding(
                padding[0] + margin[0],
                padding[1] + margin[1],
                padding[2] + margin[2],
                padding[3] + margin[3]
        );
    }

    protected void fitDecorInsidesToViewMargin(@NonNull View view) {
        final int[] padding = getDecorPadding();
        final int[] margin = getDecorMargin();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.leftMargin = padding[0] + margin[0];
        params.topMargin = padding[1] + margin[1];
        params.rightMargin = padding[2] + margin[2];
        params.bottomMargin = padding[3] + margin[3];
    }

    protected void fitDecorInsidesTo(@NonNull View view) {
        fitDecorPaddingTo(view);
        fitDecorMarginTo(view);
    }

    protected void fitDecorPaddingTo(@NonNull View view) {
        final int[] padding = getDecorPadding();
        view.setPadding(padding[0], padding[1], padding[2], padding[3]);
    }

    protected void fitDecorMarginTo(@NonNull View view) {
        final int[] margin = getDecorMargin();
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        params.leftMargin = margin[0];
        params.topMargin = margin[1];
        params.rightMargin = margin[2];
        params.bottomMargin = margin[3];
    }

    @NonNull
    @Size(4)
    protected int[] getDecorPadding() {
        return new int[]{
                getViewHolder().getDecorChild().getPaddingLeft(),
                getViewHolder().getDecorChild().getPaddingTop(),
                getViewHolder().getDecorChild().getPaddingRight(),
                getViewHolder().getDecorChild().getPaddingBottom()
        };
    }

    @NonNull
    @Size(4)
    protected int[] getDecorMargin() {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) getViewHolder().getDecorChild().getLayoutParams();
        return new int[]{
                params.leftMargin,
                params.topMargin,
                params.rightMargin,
                params.bottomMargin
        };
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
