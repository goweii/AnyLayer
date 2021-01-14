package per.goweii.anylayer.notification;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import per.goweii.anylayer.DecorLayer;
import per.goweii.anylayer.GlobalConfig;
import per.goweii.anylayer.R;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.Utils;
import per.goweii.anylayer.widget.SwipeLayout;

public class NotificationLayer extends DecorLayer {

    private Runnable mDismissRunnable = null;
    private boolean mSwiping = false;

    public NotificationLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public NotificationLayer(@NonNull Activity activity) {
        super(activity);
    }

    @IntRange(from = 0)
    @Override
    protected int getLevel() {
        return Level.NOTIFICATION;
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

    @Override
    public void show() {
        super.show();
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getViewHolder().getChildNullable() == null) {
            SwipeLayout container = (SwipeLayout) inflater.inflate(R.layout.anylayer_notification_layer, parent, false);
            getViewHolder().setChild(container);
            getViewHolder().setContent(onCreateContent(inflater, getViewHolder().getChild()));
            ViewGroup.LayoutParams layoutParams = getViewHolder().getContent().getLayoutParams();
            FrameLayout.LayoutParams contentParams;
            if (layoutParams == null) {
                contentParams = generateContentDefaultLayoutParams();
            } else if (layoutParams instanceof FrameLayout.LayoutParams) {
                contentParams = (FrameLayout.LayoutParams) layoutParams;
            } else {
                contentParams = new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height);
            }
            getViewHolder().getContent().setLayoutParams(contentParams);
            getViewHolder().getContentWrapper().addView(getViewHolder().getContent());
        }
        return getViewHolder().getChild();
    }

    @NonNull
    protected View onCreateContent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getViewHolder().getContentNullable() == null) {
            getViewHolder().setContent(inflater.inflate(getConfig().mContentViewId, parent, false));
        } else {
            ViewGroup contentParent = (ViewGroup) getViewHolder().getContent().getParent();
            if (contentParent != null) {
                contentParent.removeView(getViewHolder().getContent());
            }
        }
        return getViewHolder().getContent();
    }

    @NonNull
    protected FrameLayout.LayoutParams generateContentDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        return AnimatorHelper.createTopInAnim(getViewHolder().getContentWrapper());
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        return AnimatorHelper.createTopOutAnim(getViewHolder().getContentWrapper());
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        getViewHolder().getChild().setPadding(0, Utils.getStatusBarHeight(getActivity()), 0, 0);
        getViewHolder().getChild().setClipToPadding(false);
        getViewHolder().getChild().setSwipeDirection(
                SwipeLayout.Direction.TOP | SwipeLayout.Direction.LEFT | SwipeLayout.Direction.RIGHT
        );
        getViewHolder().getChild().setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onStart(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                mSwiping = true;
                autoDismiss(false);
                getListenerHolder().notifyOnSwipeStart(NotificationLayer.this);
            }

            @Override
            public void onSwiping(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                getListenerHolder().notifyOnSwiping(NotificationLayer.this, direction, fraction);
            }

            @Override
            public void onEnd(@SwipeLayout.Direction int direction, @FloatRange(from = 0F, to = 1F) float fraction) {
                mSwiping = false;
                if (fraction == 1F) {
                    getListenerHolder().notifyOnSwipeEnd(NotificationLayer.this, direction);
                    // 动画执行结束后不能直接removeView，要在下一个dispatchDraw周期移除
                    // 否则会崩溃，因为viewGroup的childCount没有来得及-1，获取到的view为空
                    getViewHolder().getContent().setVisibility(View.INVISIBLE);
                    getViewHolder().getContent().post(new Runnable() {
                        @Override
                        public void run() {
                            dismiss(false);
                        }
                    });
                } else if (fraction == 0F) {
                    autoDismiss(true);
                }
            }
        });
        if (getConfig().mMaxWidth >= 0) {
            getViewHolder().getContentWrapper().setMaxWidth(getConfig().mMaxWidth);
        }
        if (getConfig().mMaxHeight >= 0) {
            getViewHolder().getContentWrapper().setMaxHeight(getConfig().mMaxHeight);
        }
        getViewHolder().getContent().setVisibility(View.VISIBLE);
        getViewHolder().getContentWrapper().setOnDispatchTouchListener(new MaxSizeFrameLayout.OnDispatchTouchListener() {
            @Override
            public void onDispatch(MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        autoDismiss(false);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if (!mSwiping) {
                            autoDismiss(true);
                        }
                        break;
                }
            }
        });
        bindDefaultContentData();
    }

    @Override
    protected void onAppear() {
        super.onAppear();
    }

    @Override
    protected void onShow() {
        super.onShow();
        autoDismiss(true);
    }

    @Override
    protected void onDismiss() {
        if (mDismissRunnable != null) {
            getChild().removeCallbacks(mDismissRunnable);
        }
        super.onDismiss();
    }

    @Override
    protected void onDisappear() {
        super.onDisappear();
    }

    @Override
    protected void onDetach() {
        super.onDetach();
    }

    private void bindDefaultContentData() {
        if (getViewHolder().getTop() != null) {
            if (getViewHolder().getIcon() != null) {
                if (getConfig().mIcon != null) {
                    getViewHolder().getIcon().setVisibility(View.VISIBLE);
                    getViewHolder().getIcon().setImageDrawable(getConfig().mIcon);
                } else {
                    getViewHolder().getIcon().setVisibility(View.GONE);
                }
            }
            if (getViewHolder().getLabel() != null) {
                if (!TextUtils.isEmpty(getConfig().mLabel)) {
                    getViewHolder().getLabel().setVisibility(View.VISIBLE);
                    getViewHolder().getLabel().setText(getConfig().mLabel);
                } else {
                    getViewHolder().getLabel().setVisibility(View.GONE);
                }
            }
            if (getViewHolder().getTime() != null) {
                if (!TextUtils.isEmpty(getConfig().mTime)) {
                    getViewHolder().getTime().setVisibility(View.VISIBLE);
                    getViewHolder().getTime().setText(getConfig().mTime);
                } else {
                    if (!TextUtils.isEmpty(GlobalConfig.get().notificationTimePattern)) {
                        getViewHolder().getTime().setVisibility(View.VISIBLE);
                        String time = new SimpleDateFormat(GlobalConfig.get().notificationTimePattern, Locale.getDefault()).format(new Date());
                        getViewHolder().getTime().setText(time);
                    } else {
                        getViewHolder().getTime().setVisibility(View.GONE);
                    }
                }
            }
            LinearLayout topView = getViewHolder().getTop();
            topView.setVisibility(View.GONE);
            for (int i = 0; i < topView.getChildCount(); i++) {
                if (topView.getChildAt(i).getVisibility() == View.VISIBLE) {
                    topView.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
        if (getViewHolder().getTitle() != null) {
            if (!TextUtils.isEmpty(getConfig().mTitle)) {
                getViewHolder().getTitle().setVisibility(View.VISIBLE);
                getViewHolder().getTitle().setText(getConfig().mTitle);
            } else {
                getViewHolder().getTitle().setVisibility(View.GONE);
            }
        }
        if (getViewHolder().getDesc() != null) {
            if (!TextUtils.isEmpty(getConfig().mDesc)) {
                getViewHolder().getDesc().setVisibility(View.VISIBLE);
                getViewHolder().getDesc().setText(getConfig().mDesc);
            } else {
                getViewHolder().getDesc().setVisibility(View.GONE);
            }
        }
    }

    @NonNull
    public NotificationLayer contentView(@NonNull View contentView) {
        getViewHolder().setContent(contentView);
        return this;
    }

    @NonNull
    public NotificationLayer contentView(@LayoutRes int contentViewId) {
        getConfig().mContentViewId = contentViewId;
        return this;
    }

    @NonNull
    public NotificationLayer maxWidth(int maxWidth) {
        getConfig().mMaxWidth = maxWidth;
        return this;
    }

    @NonNull
    public NotificationLayer maxHeight(int maxHeight) {
        getConfig().mMaxHeight = maxHeight;
        return this;
    }

    @NonNull
    public NotificationLayer icon(@DrawableRes int drawableId) {
        getConfig().mIcon = ContextCompat.getDrawable(getActivity(), drawableId);
        return this;
    }

    @NonNull
    public NotificationLayer icon(@Nullable Drawable drawable) {
        getConfig().mIcon = drawable;
        return this;
    }

    @NonNull
    public NotificationLayer label(@Nullable CharSequence label) {
        getConfig().mLabel = label;
        return this;
    }

    @NonNull
    public NotificationLayer label(@StringRes int labelRes) {
        getConfig().mLabel = getActivity().getString(labelRes);
        return this;
    }

    @NonNull
    public NotificationLayer time(@Nullable CharSequence time) {
        getConfig().mTime = time;
        return this;
    }

    @NonNull
    public NotificationLayer title(@NonNull CharSequence title) {
        getConfig().mTitle = title;
        return this;
    }

    @NonNull
    public NotificationLayer title(@StringRes int titleRes) {
        getConfig().mTitle = getActivity().getString(titleRes);
        return this;
    }

    @NonNull
    public NotificationLayer desc(@NonNull CharSequence desc) {
        getConfig().mDesc = desc;
        return this;
    }

    @NonNull
    public NotificationLayer desc(@StringRes int descRes) {
        getConfig().mDesc = getActivity().getString(descRes);
        return this;
    }

    @NonNull
    public NotificationLayer duration(long duration) {
        getConfig().mDuration = duration;
        return this;
    }

    @NonNull
    public NotificationLayer onNotificationClick(@NonNull OnClickListener listener) {
        onClickToDismiss(listener);
        return this;
    }

    @NonNull
    public NotificationLayer onNotificationLongClick(@NonNull OnLongClickListener listener) {
        onLongClickToDismiss(listener);
        return this;
    }

    public void autoDismiss(boolean enable) {
        if (mDismissRunnable != null) {
            getChild().removeCallbacks(mDismissRunnable);
        }
        if (enable && isShown() && getConfig().mDuration > 0) {
            if (mDismissRunnable == null) {
                mDismissRunnable = new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                };
            }
            getChild().postDelayed(mDismissRunnable, getConfig().mDuration);
        }
    }

    @NonNull
    public NotificationLayer swipeTransformer(@Nullable NotificationLayer.SwipeTransformer swipeTransformer) {
        getConfig().mSwipeTransformer = swipeTransformer;
        return this;
    }

    /**
     * 浮层拖拽事件监听
     *
     * @param swipeListener OnSwipeListener
     */
    @NonNull
    public NotificationLayer onSwipeListener(@NonNull NotificationLayer.OnSwipeListener swipeListener) {
        getListenerHolder().addOnSwipeListener(swipeListener);
        return this;
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private MaxSizeFrameLayout mContentWrapper;
        private View mContent;

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
            mContentWrapper = getChild().findViewById(R.id.anylayler_notification_content_wrapper);
        }

        @NonNull
        @Override
        public SwipeLayout getChild() {
            return (SwipeLayout) super.getChild();
        }

        @Nullable
        @Override
        protected SwipeLayout getChildNullable() {
            return (SwipeLayout) super.getChildNullable();
        }

        @NonNull
        protected MaxSizeFrameLayout getContentWrapper() {
            return mContentWrapper;
        }

        protected void setContent(@NonNull View content) {
            mContent = content;
        }

        @Nullable
        protected View getContentNullable() {
            return mContent;
        }

        @NonNull
        public View getContent() {
            Utils.requireNonNull(mContent, "必须在show方法后调用");
            return mContent;
        }

        @Nullable
        @Override
        protected View getNoIdClickView() {
            return mContent;
        }

        @Nullable
        public LinearLayout getTop() {
            return mContent.findViewById(R.id.anylayler_notification_content_top);
        }

        @Nullable
        public ImageView getIcon() {
            return mContent.findViewById(R.id.anylayler_notification_content_icon);
        }

        @Nullable
        public TextView getLabel() {
            return mContent.findViewById(R.id.anylayler_notification_content_label);
        }

        @Nullable
        public TextView getTime() {
            return mContent.findViewById(R.id.anylayler_notification_content_time);
        }

        @Nullable
        public TextView getTitle() {
            return mContent.findViewById(R.id.anylayler_notification_content_title);
        }

        @Nullable
        public TextView getDesc() {
            return mContent.findViewById(R.id.anylayler_notification_content_desc);
        }
    }

    protected static class Config extends DecorLayer.Config {
        protected int mContentViewId = R.layout.anylayer_notification_content;

        protected long mDuration = GlobalConfig.get().notificationDuration;
        protected int mMaxWidth = GlobalConfig.get().notificationMaxWidth;
        protected int mMaxHeight = GlobalConfig.get().notificationMaxHeight;

        protected CharSequence mLabel = GlobalConfig.get().notificationLabel;
        protected Drawable mIcon = GlobalConfig.get().notificationIcon;
        protected CharSequence mTime = null;
        protected CharSequence mTitle = null;
        protected CharSequence mDesc = null;

        @Nullable
        protected SwipeTransformer mSwipeTransformer = null;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
        private List<OnSwipeListener> mOnSwipeListeners = null;

        private void addOnSwipeListener(@NonNull OnSwipeListener onSwipeListener) {
            if (mOnSwipeListeners == null) {
                mOnSwipeListeners = new ArrayList<>(1);
            }
            mOnSwipeListeners.add(onSwipeListener);
        }

        private void notifyOnSwipeStart(@NonNull NotificationLayer layer) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onStart(layer);
                }
            }
        }

        private void notifyOnSwiping(@NonNull NotificationLayer layer,
                                     @SwipeLayout.Direction int direction,
                                     @FloatRange(from = 0F, to = 1F) float fraction) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onSwiping(layer, direction, fraction);
                }
            }
        }

        private void notifyOnSwipeEnd(@NonNull NotificationLayer layer,
                                      @SwipeLayout.Direction int direction) {
            if (mOnSwipeListeners != null) {
                for (OnSwipeListener onSwipeListener : mOnSwipeListeners) {
                    onSwipeListener.onEnd(layer, direction);
                }
            }
        }
    }

    public interface SwipeTransformer {
        void onSwiping(@NonNull NotificationLayer layer,
                       @SwipeLayout.Direction int direction,
                       @FloatRange(from = 0F, to = 1F) float fraction);
    }

    public interface OnSwipeListener {
        void onStart(@NonNull NotificationLayer layer);

        void onSwiping(@NonNull NotificationLayer layer,
                       @SwipeLayout.Direction int direction,
                       @FloatRange(from = 0F, to = 1F) float fraction);

        void onEnd(@NonNull NotificationLayer layer,
                   @SwipeLayout.Direction int direction);
    }
}
