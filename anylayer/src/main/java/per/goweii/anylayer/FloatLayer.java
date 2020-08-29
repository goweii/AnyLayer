package per.goweii.anylayer;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class FloatLayer extends DecorLayer {

    public FloatLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public FloatLayer(@NonNull Activity activity) {
        super(activity);
        interceptKeyEvent(false);
        cancelableOnKeyBack(false);
    }

    @IntRange(from = 0)
    @Override
    protected int getLevel() {
        return Level.FLOAT;
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
            DragLayout container = (DragLayout) inflater.inflate(R.layout.anylayer_float_layer, parent, false);
            getViewHolder().setChild(container);
            getViewHolder().setFloat(onCreateFloat(inflater, getViewHolder().getChild()));
            ViewGroup.LayoutParams layoutParams = getViewHolder().getFloat().getLayoutParams();
            FrameLayout.LayoutParams floatParams;
            if (layoutParams == null) {
                floatParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            } else if (layoutParams instanceof FrameLayout.LayoutParams) {
                floatParams = (FrameLayout.LayoutParams) layoutParams;
            } else {
                floatParams = new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height);
            }
            getViewHolder().getFloat().setLayoutParams(floatParams);
            getViewHolder().getChild().addView(getViewHolder().getFloat());
        }
        return getViewHolder().getChild();
    }

    @NonNull
    protected View onCreateFloat(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getViewHolder().getFloatOrNull() == null) {
            getViewHolder().setFloat(inflater.inflate(getConfig().mFloatViewId, parent, false));
        } else {
            ViewGroup contentParent = (ViewGroup) getViewHolder().getFloat().getParent();
            if (contentParent != null) {
                contentParent.removeView(getViewHolder().getFloat());
            }
        }
        return getViewHolder().getFloat();
    }

    @Nullable
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        return AnimatorHelper.createZoomAlphaInAnim(view);
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        return AnimatorHelper.createZoomAlphaOutAnim(view);
    }

    @Override
    public void onAttach() {
        super.onAttach();
        initFloat();
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
    }

    @Override
    public void onShow() {
        super.onShow();
        getViewHolder().getChild().goEdge(getViewHolder().getFloat());
    }

    @Override
    public void onPreRemove() {
        super.onPreRemove();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initFloat() {
        final Config config = getConfig();
        final DragLayout dragLayout = getViewHolder().getChild();
        dragLayout.setOutside(config.mOutside);
        dragLayout.setSnapEdge(config.mSnapEdge);
        dragLayout.setOnDragListener(new FloatDragListener());
        final View floatView = getViewHolder().getFloat();
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) floatView.getLayoutParams();
        if (config.mGravity != -1) {
            params.gravity = config.mGravity;
        }
        if (config.mMarginLeft != Integer.MIN_VALUE) {
            params.leftMargin = config.mMarginLeft;
        }
        if (config.mMarginTop != Integer.MIN_VALUE) {
            params.topMargin = config.mMarginTop;
        }
        if (config.mMarginRight != Integer.MIN_VALUE) {
            params.rightMargin = config.mMarginRight;
        }
        if (config.mMarginBottom != Integer.MIN_VALUE) {
            params.bottomMargin = config.mMarginBottom;
        }
        getListenerHolder().bindFloatClickListener(this);
        getListenerHolder().bindFloatLongClickListener(this);
    }

    public FloatLayer floatView(int layoutId) {
        getConfig().mFloatViewId = layoutId;
        return this;
    }

    public FloatLayer floatView(View floatView) {
        getViewHolder().setFloat(floatView);
        return this;
    }

    public FloatLayer normalAlpha(float alpha) {
        getConfig().mNormalAlpha = alpha;
        return this;
    }

    public FloatLayer lowProfileAlpha(float alpha) {
        getConfig().mLowProfileAlpha = alpha;
        return this;
    }

    public FloatLayer lowProfileIndent(float indent) {
        getConfig().mLowProfileIndent = indent;
        return this;
    }

    public FloatLayer lowProfileDelay(int delay) {
        getConfig().mLowProfileDelay = delay;
        return this;
    }

    public FloatLayer snapEdge(int edge) {
        getConfig().mSnapEdge = edge;
        return this;
    }

    public FloatLayer outside(boolean outside) {
        getConfig().mOutside = outside;
        return this;
    }

    public FloatLayer gravity(int gravity) {
        getConfig().mGravity = gravity;
        return this;
    }

    public FloatLayer marginLeft(int margin) {
        getConfig().mMarginLeft = margin;
        return this;
    }

    public FloatLayer marginTop(int margin) {
        getConfig().mMarginTop = margin;
        return this;
    }

    public FloatLayer marginRight(int margin) {
        getConfig().mMarginRight = margin;
        return this;
    }

    public FloatLayer marginBottom(int margin) {
        getConfig().mMarginBottom = margin;
        return this;
    }

    public FloatLayer onFloatClick(@NonNull OnClickListener listener) {
        getListenerHolder().setOnFloatClickListener(listener);
        return this;
    }

    public FloatLayer onFloatLongClick(@NonNull OnLongClickListener listener) {
        getListenerHolder().setOnFloatLongClickListener(listener);
        return this;
    }

    private void doFloatNormal() {
        getViewHolder().getFloat().removeCallbacks(mFloatLowProfileRunnable);
        getViewHolder().getFloat().post(mFloatNormalRunnable);
    }

    private void doFloatLowProfile() {
        if (getConfig().mNormalAlpha != getConfig().mLowProfileAlpha) {
            getViewHolder().getFloat().postDelayed(mFloatLowProfileRunnable, getConfig().mLowProfileDelay);
        }
    }

    private final Runnable mFloatNormalRunnable = new Runnable() {
        @Override
        public void run() {
            getViewHolder().getFloat().animate()
                    .alpha(getConfig().mNormalAlpha)
                    .translationX(0F)
                    .translationY(0F)
                    .start();
        }
    };

    private final Runnable mFloatLowProfileRunnable = new Runnable() {
        @Override
        public void run() {
            float[] xy = calcIntentTranslation();
            getViewHolder().getFloat().animate()
                    .alpha(getConfig().mLowProfileAlpha)
                    .translationX(xy[0])
                    .translationY(xy[1])
                    .start();
        }
    };

    private float[] calcIntentTranslation() {
        float percent = getConfig().mLowProfileIndent;
        float x = 0F;
        float y = 0F;
        if (percent != 0) {
            final DragLayout dragLayout = getViewHolder().getChild();
            final View floatView = getViewHolder().getFloat();
            final int currEdge = dragLayout.calcCurrEdge(floatView);
            if ((currEdge & DragLayout.Edge.LEFT) != 0) {
                x = -floatView.getWidth() * percent;
            }
            if ((currEdge & DragLayout.Edge.RIGHT) != 0) {
                x = floatView.getWidth() * percent;
            }
            if ((currEdge & DragLayout.Edge.TOP) != 0) {
                y = -floatView.getHeight() * percent;
            }
            if ((currEdge & DragLayout.Edge.BOTTOM) != 0) {
                y = floatView.getHeight() * percent;
            }
        }
        return new float[]{x, y};
    }

    private class FloatDragListener implements DragLayout.OnDragListener {
        @Override
        public void onStart(@NonNull View view) {
            doFloatNormal();
        }

        @Override
        public void onDragging(@NonNull View view) {
        }

        @Override
        public void onRelease(@NonNull View view) {
        }

        @Override
        public void onStop(@NonNull View view) {
            doFloatLowProfile();
        }
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private View mFloat;

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
        }

        @NonNull
        @Override
        public DragLayout getChild() {
            return (DragLayout) super.getChild();
        }

        @Nullable
        @Override
        protected DragLayout getChildOrNull() {
            return (DragLayout) super.getChildOrNull();
        }

        void setFloat(@NonNull View floatView) {
            mFloat = floatView;
        }

        @Nullable
        protected View getFloatOrNull() {
            return mFloat;
        }

        @NonNull
        public View getFloat() {
            Utils.requireNonNull(mFloat, "必须在show方法后调用");
            return mFloat;
        }
    }

    protected static class Config extends DecorLayer.Config {
        protected int mFloatViewId = -1;

        private boolean mOutside = true;
        private int mSnapEdge = Edge.HORIZONTAL;

        @FloatRange(from = 0F, to = 1F)
        private float mNormalAlpha = 1F;
        @FloatRange(from = 0F, to = 1F)
        private float mLowProfileAlpha = 0.8F;
        @FloatRange(from = 0F, to = 1F)
        private float mLowProfileIndent = 0F;
        @IntRange(from = 0)
        private int mLowProfileDelay = 3000;

        @SuppressLint("RtlHardcoded")
        private int mGravity = -1;
        private int mMarginLeft = Integer.MIN_VALUE;
        private int mMarginTop = Integer.MIN_VALUE;
        private int mMarginRight = Integer.MIN_VALUE;
        private int mMarginBottom = Integer.MIN_VALUE;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
        private OnClickListener mOnFloatClickListener = null;
        private OnLongClickListener mOnFloatLongClickListener = null;

        private void bindFloatClickListener(@NonNull FloatLayer layer) {
            layer.getViewHolder().getFloat().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(@NonNull View v) {
                    if (mOnFloatClickListener != null) {
                        mOnFloatClickListener.onClick(layer, v);
                    }
                }
            });
        }

        private void bindFloatLongClickListener(@NonNull FloatLayer layer) {
            if (mOnFloatLongClickListener != null) {
                layer.getViewHolder().getFloat().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return mOnFloatLongClickListener.onLongClick(layer, v);
                    }
                });
            }
        }

        public void setOnFloatClickListener(@NonNull OnClickListener listener) {
            mOnFloatClickListener = listener;
        }

        public void setOnFloatLongClickListener(@NonNull OnLongClickListener listener) {
            mOnFloatLongClickListener = listener;
        }
    }

    public static class Edge {
        public static final int NONE = 0;

        public static final int LEFT = 1;
        public static final int TOP = 1 << 1;
        public static final int RIGHT = 1 << 2;
        public static final int BOTTOM = 1 << 3;

        public static final int HORIZONTAL = LEFT | RIGHT;
        public static final int VERTICAL = TOP | BOTTOM;

        public static final int ALL = HORIZONTAL | VERTICAL;
    }
}
