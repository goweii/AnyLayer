package per.goweii.anylayer.floats;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.DecorLayer;
import per.goweii.anylayer.R;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.Utils;
import per.goweii.anylayer.widget.DragLayout;

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
        if (getViewHolder().getChildNullable() == null) {
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
        initDragLayout();
        initFloatView();
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
        initFloatViewDefConfig();
        toNormal();
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

    private void initDragLayout() {
        final Config config = getConfig();
        final DragLayout dragLayout = getViewHolder().getChild();
        dragLayout.setPadding(config.mPaddingLeft, config.mPaddingTop, config.mPaddingRight, config.mPaddingBottom);
        dragLayout.setOutside(config.mOutside);
        dragLayout.setSnapEdge(config.mSnapEdge);
        dragLayout.setOnDragListener(new FloatDragListener());
    }

    private void initFloatView() {
        final Config config = getConfig();
        final View floatView = getViewHolder().getFloat();
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) floatView.getLayoutParams();
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
        getListenerHolder().bindTouchListener(this);
    }

    private void initFloatViewDefConfig() {
        final Config config = getConfig();
        final DragLayout dragLayout = getViewHolder().getChild();
        final View floatView = getViewHolder().getFloat();
        final int minLeft = dragLayout.getViewLeftMinInside(floatView);
        final int maxLeft = dragLayout.getViewLeftMaxInside(floatView);
        final int minTop = dragLayout.getViewTopMinInside(floatView);
        final int maxTop = dragLayout.getViewTopMaxInside(floatView);
        final float layoutPercentX;
        final float floatPercentX;
        if (config.mDefPercentX < -1) {
            layoutPercentX = -1;
            floatPercentX = config.mDefPercentX + 1;
        } else if (config.mDefPercentX > 1) {
            layoutPercentX = 1;
            floatPercentX = config.mDefPercentX - 1;
        } else {
            layoutPercentX = config.mDefPercentX;
            floatPercentX = 0;
        }
        final float layoutPercentY;
        final float floatPercentY;
        if (config.mDefPercentY < -1) {
            layoutPercentY = -1;
            floatPercentY = config.mDefPercentY + 1;
        } else if (config.mDefPercentY > 1) {
            layoutPercentY = 1;
            floatPercentY = config.mDefPercentY - 1;
        } else {
            layoutPercentY = config.mDefPercentY;
            floatPercentY = 0;
        }
        final int halfRangeH = (maxLeft - minLeft) / 2;
        final int halfRangeV = (maxTop - minTop) / 2;
        final int centerLeft = minLeft + halfRangeH;
        final int centerTop = minTop + halfRangeV;
        final int floatWidthWithMargin = floatView.getWidth() + Utils.getViewMarginLeft(floatView) + Utils.getViewMarginRight(floatView);
        final int floatHeightWithMargin = floatView.getHeight() + Utils.getViewMarginTop(floatView) + Utils.getViewMarginBottom(floatView);
        final int defLeft = (int) (centerLeft + halfRangeH * layoutPercentX + floatWidthWithMargin * floatPercentX);
        final int defTop = (int) (centerTop + halfRangeV * layoutPercentY + floatHeightWithMargin * floatPercentY);
        floatView.offsetLeftAndRight(defLeft - floatView.getLeft());
        floatView.offsetTopAndBottom(defTop - floatView.getTop());
        floatView.setPivotX(floatView.getWidth() * config.mPivotX);
        floatView.setPivotY(floatView.getHeight() * config.mPivotY);
        floatView.setAlpha(config.mDefAlpha);
        floatView.setScaleX(config.mDefScale);
        floatView.setScaleY(config.mDefScale);
    }

    public FloatLayer floatView(int layoutId) {
        getConfig().mFloatViewId = layoutId;
        return this;
    }

    public FloatLayer floatView(View floatView) {
        getViewHolder().setFloat(floatView);
        return this;
    }

    public FloatLayer defPercentX(float p) {
        getConfig().mDefPercentX = p;
        return this;
    }

    public FloatLayer defPercentY(float p) {
        getConfig().mDefPercentY = p;
        return this;
    }

    public FloatLayer defAlpha(float alpha) {
        getConfig().mDefAlpha = alpha;
        return this;
    }

    public FloatLayer defScale(float scale) {
        getConfig().mDefScale = scale;
        return this;
    }

    public FloatLayer normalAlpha(float alpha) {
        getConfig().mNormalAlpha = alpha;
        return this;
    }

    public FloatLayer normalScale(float scale) {
        getConfig().mNormalScale = scale;
        return this;
    }

    public FloatLayer lowProfileAlpha(float alpha) {
        getConfig().mLowProfileAlpha = alpha;
        return this;
    }

    public FloatLayer lowProfileScale(float scale) {
        getConfig().mLowProfileScale = scale;
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

    public FloatLayer paddingLeft(int padding) {
        getConfig().mPaddingLeft = padding;
        return this;
    }

    public FloatLayer paddingTop(int padding) {
        getConfig().mPaddingTop = padding;
        return this;
    }

    public FloatLayer paddingRight(int padding) {
        getConfig().mPaddingRight = padding;
        return this;
    }

    public FloatLayer paddingBottom(int padding) {
        getConfig().mPaddingBottom = padding;
        return this;
    }

    public FloatLayer onFloatClick(@NonNull OnClickListener listener) {
        onClick(listener);
        return this;
    }

    public FloatLayer onFloatLongClick(@NonNull OnLongClickListener listener) {
        onLongClick(listener);
        return this;
    }

    public void toNormal() {
        getViewHolder().getFloat().removeCallbacks(mFloatLowProfileRunnable);
        getViewHolder().getFloat().post(mFloatNormalRunnable);
    }

    public void toLowProfile() {
        if (getConfig().mNormalAlpha != getConfig().mLowProfileAlpha) {
            getViewHolder().getFloat().removeCallbacks(mFloatLowProfileRunnable);
            getViewHolder().getFloat().postDelayed(mFloatLowProfileRunnable, getConfig().mLowProfileDelay);
        }
    }

    private final Runnable mFloatNormalRunnable = new Runnable() {
        @Override
        public void run() {
            getViewHolder().getFloat().animate()
                    .alpha(getConfig().mNormalAlpha)
                    .scaleX(getConfig().mNormalScale)
                    .scaleY(getConfig().mNormalScale)
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
                    .scaleX(getConfig().mLowProfileScale)
                    .scaleY(getConfig().mLowProfileScale)
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
            final int floatWidthWithMargin = floatView.getWidth() + Utils.getViewMarginLeft(floatView) + Utils.getViewMarginRight(floatView);
            final int floatHeightWithMargin = floatView.getHeight() + Utils.getViewMarginTop(floatView) + Utils.getViewMarginBottom(floatView);
            if ((currEdge & DragLayout.Edge.LEFT) != 0) {
                x = -floatWidthWithMargin * percent;
            }
            if ((currEdge & DragLayout.Edge.RIGHT) != 0) {
                x = floatWidthWithMargin * percent;
            }
            if ((currEdge & DragLayout.Edge.TOP) != 0) {
                y = -floatHeightWithMargin * percent;
            }
            if ((currEdge & DragLayout.Edge.BOTTOM) != 0) {
                y = floatHeightWithMargin * percent;
            }
        }
        return new float[]{x, y};
    }

    private class FloatDragListener implements DragLayout.OnDragListener {
        @Override
        public void onStart(@NonNull View view) {
            toNormal();
        }

        @Override
        public void onDragging(@NonNull View view) {
        }

        @Override
        public void onRelease(@NonNull View view) {
        }

        @Override
        public void onStop(@NonNull View view) {
            toLowProfile();
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
        protected DragLayout getChildNullable() {
            return (DragLayout) super.getChildNullable();
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

        @Nullable
        @Override
        protected View getNoIdClickView() {
            return mFloat;
        }
    }

    protected static class Config extends DecorLayer.Config {
        protected int mFloatViewId = -1;

        private boolean mOutside = true;
        private int mSnapEdge = Edge.HORIZONTAL;

        @FloatRange(from = -2F, to = 2F)
        private float mDefPercentX = 2F;
        @FloatRange(from = -2F, to = 2F)
        private float mDefPercentY = 0.236F;
        @FloatRange(from = 0F, to = 1F)
        private float mDefAlpha = 1F;
        private float mDefScale = 1F;

        private float mPivotX = 0.5F;
        private float mPivotY = 0.5F;

        @FloatRange(from = 0F, to = 1F)
        private float mNormalAlpha = 1F;
        private float mNormalScale = 1F;
        @IntRange(from = 0)
        private int mLowProfileDelay = 3000;
        @FloatRange(from = 0F, to = 1F)
        private float mLowProfileAlpha = 0.8F;
        private float mLowProfileScale = 1F;
        @FloatRange(from = 0F, to = 1F)
        private float mLowProfileIndent = 0F;

        private int mMarginLeft = Integer.MIN_VALUE;
        private int mMarginTop = Integer.MIN_VALUE;
        private int mMarginRight = Integer.MIN_VALUE;
        private int mMarginBottom = Integer.MIN_VALUE;

        private int mPaddingLeft = 0;
        private int mPaddingTop = 0;
        private int mPaddingRight = 0;
        private int mPaddingBottom = 0;
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
        private GestureDetector mGestureDetector = null;

        public void bindTouchListener(@NonNull FloatLayer layer) {
            final View floatView = layer.getViewHolder().getFloat();
            mGestureDetector = new GestureDetector(floatView.getContext(), new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    layer.toNormal();
                    return true;
                }

                @Override
                public void onShowPress(MotionEvent e) {
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    floatView.performClick();
                    layer.toLowProfile();
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    floatView.performLongClick();
                    layer.toLowProfile();
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }
            });
            floatView.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mGestureDetector.onTouchEvent(event);
                }
            });
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
