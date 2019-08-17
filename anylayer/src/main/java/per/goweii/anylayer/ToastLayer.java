package per.goweii.anylayer;

import android.animation.Animator;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class ToastLayer extends DecorLayer implements Runnable {

    public ToastLayer(Context context) {
        super(Utils.requireNonNull(Utils.getActivity(Utils.requireNonNull(context, "context == null"))));
        interceptKeyEvent(false);
        cancelableOnKeyBack(false);
    }

    @Override
    protected Level getLevel() {
        return Level.TOAST;
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
    public void show() {
        super.show();
    }

    public ToastLayer removeOthers(boolean removeOthers) {
        getConfig().mRemoveOthers = removeOthers;
        return this;
    }

    public ToastLayer duration(long duration) {
        getConfig().mDuration = duration;
        return this;
    }

    public ToastLayer message(CharSequence message) {
        Utils.requireNonNull(message, "message == null");
        getConfig().mMessage = message;
        return this;
    }

    public ToastLayer message(int message) {
        getConfig().mMessage = getActivity().getString(message);
        return this;
    }

    public ToastLayer icon(int icon) {
        getConfig().mIcon = icon;
        return this;
    }

    @Override
    protected View onCreateChild(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.anylayer_toast_layer, parent, false);
    }

    @Override
    protected Animator onCreateInAnimator(View view) {
        return AnimatorHelper.createLeftInAnim(view);
    }

    @Override
    protected Animator onCreateOutAnimator(View view) {
        return AnimatorHelper.createLeftOutAnim(view);
    }

    private void bindData() {
        if (getConfig().mIcon > 0) {
            getViewHolder().getIcon().setVisibility(View.VISIBLE);
            getViewHolder().getIcon().setImageResource(getConfig().mIcon);
        } else {
            getViewHolder().getIcon().setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(getConfig().mMessage)) {
            getViewHolder().getMessage().setVisibility(View.GONE);
            getViewHolder().getMessage().setText("");
        } else {
            getViewHolder().getMessage().setVisibility(View.VISIBLE);
            getViewHolder().getMessage().setText(getConfig().mMessage);
        }
    }

    @Override
    public void onAttach() {
        super.onAttach();
        if (getConfig().mRemoveOthers) {
            final ViewGroup parent = getParent();
            final int count = parent.getChildCount();
            if (count > 1) {
                parent.removeViews(0, count - 1);
            }
        }
        bindData();
    }

    @Override
    public void onShow() {
        super.onShow();
        if (getConfig().mDuration > 0) {
            getChild().postDelayed(this, getConfig().mDuration);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getChild().removeCallbacks(this);
    }

    @Override
    public void run() {
        dismiss();
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private ImageView mIcon;
        private TextView mMessage;

        @Override
        public void setChild(View child) {
            super.setChild(child);
            mIcon = child.findViewById(R.id.iv_icon);
            mMessage = child.findViewById(R.id.tv_msg);
        }

        public ImageView getIcon() {
            return mIcon;
        }

        public TextView getMessage() {
            return mMessage;
        }
    }

    protected static class Config extends DecorLayer.Config {
        private boolean mRemoveOthers = true;
        private long mDuration = 3000L;
        private CharSequence mMessage = "";
        private int mIcon = 0;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
    }
}
