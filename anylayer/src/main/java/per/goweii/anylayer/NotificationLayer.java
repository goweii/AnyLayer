package per.goweii.anylayer;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class NotificationLayer extends DecorLayer {

    private Runnable mDismissRunnable = null;

    public NotificationLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public NotificationLayer(@NonNull Activity activity) {
        super(activity);
        interceptKeyEvent(false);
        cancelableOnKeyBack(false);
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
        if (getViewHolder().getChildOrNull() == null) {
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
            getViewHolder().getChild().addView(getViewHolder().getContent());
        }
        return getViewHolder().getChild();
    }

    @NonNull
    protected View onCreateContent(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getViewHolder().getContentOrNull() == null) {
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
        return AnimatorHelper.createTopInAnim(getViewHolder().getContent());
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        return AnimatorHelper.createTopOutAnim(getViewHolder().getContent());
    }

    @Override
    public void onAttach() {
        super.onAttach();
        getListenerHolder().setOnTouchListener(new OnTouchListener() {
            @Override
            public void onDown() {
                if (mDismissRunnable != null) {
                    getChild().removeCallbacks(mDismissRunnable);
                }
            }
        });
        getListenerHolder().bindListeners(this);
        getViewHolder().getChild().setPadding(0, Utils.getStatusBarHeight(getActivity()), 0, 0);
        getViewHolder().getChild().setClipToPadding(false);
        getViewHolder().getChild().setSwipeDirection(
                SwipeLayout.Direction.TOP | SwipeLayout.Direction.LEFT | SwipeLayout.Direction.RIGHT
        );
        getViewHolder().getChild().setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSwiping(int direction, float fraction) {
            }

            @Override
            public void onEnd(int direction) {
                dismiss(false);
            }
        });
        if (getConfig().mIcon != null) {
            getViewHolder().getTop().setVisibility(View.VISIBLE);
            getViewHolder().getIcon().setVisibility(View.VISIBLE);
            getViewHolder().getIcon().setImageDrawable(getConfig().mIcon);
        }
        if (!TextUtils.isEmpty(getConfig().mLabel)) {
            getViewHolder().getTop().setVisibility(View.VISIBLE);
            getViewHolder().getLabel().setVisibility(View.VISIBLE);
            getViewHolder().getLabel().setText(getConfig().mLabel);
        }
        if (!TextUtils.isEmpty(getConfig().mTime)) {
            getViewHolder().getTop().setVisibility(View.VISIBLE);
            getViewHolder().getTime().setVisibility(View.VISIBLE);
            getViewHolder().getTime().setText(getConfig().mTime);
        }
        if (!TextUtils.isEmpty(getConfig().mTitle)) {
            getViewHolder().getTitle().setVisibility(View.VISIBLE);
            getViewHolder().getTitle().setText(getConfig().mTitle);
        }
        if (!TextUtils.isEmpty(getConfig().mDesc)) {
            getViewHolder().getDesc().setVisibility(View.VISIBLE);
            getViewHolder().getDesc().setText(getConfig().mDesc);
        }
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
    }

    @Override
    public void onShow() {
        super.onShow();
        if (getConfig().mDuration > 0) {
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

    @Override
    public void onPreRemove() {
        if (mDismissRunnable != null) {
            getChild().removeCallbacks(mDismissRunnable);
        }
        super.onPreRemove();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public NotificationLayer label(@Nullable String label) {
        getConfig().mLabel = label;
        return this;
    }

    @NonNull
    public NotificationLayer label(@StringRes int labelRes) {
        getConfig().mLabel = getActivity().getString(labelRes);
        return this;
    }

    @NonNull
    public NotificationLayer time(@Nullable String time) {
        getConfig().mTime = time;
        return this;
    }

    @NonNull
    public NotificationLayer title(@NonNull String title) {
        getConfig().mTitle = title;
        return this;
    }

    @NonNull
    public NotificationLayer title(@StringRes int titleRes) {
        getConfig().mTitle = getActivity().getString(titleRes);
        return this;
    }

    @NonNull
    public NotificationLayer desc(@NonNull String desc) {
        getConfig().mDesc = desc;
        return this;
    }

    @NonNull
    public NotificationLayer desc(@StringRes int descRes) {
        getConfig().mDesc = getActivity().getString(descRes);
        return this;
    }

    @NonNull
    public NotificationLayer duration(int duration) {
        getConfig().mDuration = duration;
        return this;
    }

    public NotificationLayer onNotificationClick(@Nullable OnClickListener listener) {
        getListenerHolder().setOnClickListener(listener);
        return this;
    }

    public NotificationLayer onNotificationLongClick(@Nullable OnLongClickListener listener) {
        getListenerHolder().setOnLongClickListener(listener);
        return this;
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private View mContent;

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
        }

        @NonNull
        @Override
        public SwipeLayout getChild() {
            return (SwipeLayout) super.getChild();
        }

        @Nullable
        @Override
        protected SwipeLayout getChildOrNull() {
            return (SwipeLayout) super.getChildOrNull();
        }

        void setContent(@NonNull View content) {
            mContent = content;
        }

        @Nullable
        protected View getContentOrNull() {
            return mContent;
        }

        @NonNull
        public View getContent() {
            Utils.requireNonNull(mContent, "必须在show方法后调用");
            return mContent;
        }

        public LinearLayout getTop() {
            return mContent.findViewById(R.id.anylayler_ll_top);
        }

        public ImageView getIcon() {
            return mContent.findViewById(R.id.anylayler_iv_icon);
        }

        public TextView getLabel() {
            return mContent.findViewById(R.id.anylayler_tv_label);
        }

        public TextView getTime() {
            return mContent.findViewById(R.id.anylayler_tv_time);
        }

        public TextView getTitle() {
            return mContent.findViewById(R.id.anylayler_tv_title);
        }

        public TextView getDesc() {
            return mContent.findViewById(R.id.anylayler_tv_desc);
        }
    }

    protected static class Config extends DecorLayer.Config {
        protected int mContentViewId = R.layout.anylayer_notification_content;

        protected int mDuration = 10000;

        protected String mLabel;
        protected Drawable mIcon;
        protected String mTime;
        protected String mTitle;
        protected String mDesc;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
        private GestureDetector mGestureDetector = null;

        private OnTouchListener mOnTouchListener = null;
        private OnClickListener mOnClickListener = null;
        private OnLongClickListener mOnLongClickListener = null;

        private void bindListeners(@NonNull NotificationLayer layer) {
            final View contentView = layer.getViewHolder().getContent();
            if (mGestureDetector == null) {
                mGestureDetector = new GestureDetector(contentView.getContext(), new GestureDetector.OnGestureListener() {
                    @Override
                    public boolean onDown(MotionEvent e) {
                        mOnTouchListener.onDown();
                        return true;
                    }

                    @Override
                    public void onShowPress(MotionEvent e) {
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        if (mOnClickListener != null) {
                            mOnClickListener.onClick(layer, contentView);
                        }
                        layer.dismiss();
                        return true;
                    }

                    @Override
                    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                        return false;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        if (mOnLongClickListener != null) {
                            mOnLongClickListener.onLongClick(layer, contentView);
                        }
                        layer.dismiss();
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        return false;
                    }
                });
            }
            layer.getViewHolder().getContent().setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mGestureDetector.onTouchEvent(event);
                }
            });
        }

        private void setOnTouchListener(@NonNull OnTouchListener listener) {
            mOnTouchListener = listener;
        }

        private void setOnClickListener(@Nullable OnClickListener listener) {
            mOnClickListener = listener;
        }

        private void setOnLongClickListener(@Nullable OnLongClickListener listener) {
            mOnLongClickListener = listener;
        }
    }

    private interface OnTouchListener {
        void onDown();
    }
}
