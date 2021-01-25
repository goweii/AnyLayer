package per.goweii.anylayer;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.widget.FrameLayout;

import per.goweii.anylayer.utils.Utils;

public class DecorLayer extends FrameLayer {
    private final ComponentCallbacks mActivityComponentCallbacks = new ComponentCallbacks() {
        @Override
        public void onConfigurationChanged(@NonNull Configuration newConfig) {
            DecorLayer.this.onActivityConfigChanged(newConfig);
        }

        @Override
        public void onLowMemory() {
        }
    };

    private final Activity mActivity;

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

    @CallSuper
    @Override
    protected void onAttach() {
        super.onAttach();
        getActivity().registerComponentCallbacks(mActivityComponentCallbacks);
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
        getActivity().unregisterComponentCallbacks(mActivityComponentCallbacks);
        super.onDetach();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onActivityConfigChanged(@NonNull Configuration newConfig) {
    }

    public static class ViewHolder extends FrameLayer.ViewHolder {
        private FrameLayout mActivityContent;

        @Override
        public void setRoot(@NonNull FrameLayout root) {
            super.setRoot(root);
            mActivityContent = root.findViewById(android.R.id.content);
        }

        @NonNull
        public FrameLayout getDecor() {
            return getRoot();
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
