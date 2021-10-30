package per.goweii.anylayer.popup;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.anylayer.dialog.DialogLayer;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.Utils;

public class PopupLayer extends DialogLayer {

    private final int[] mLocationTemp = new int[2];

    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = null;

    public PopupLayer(@NonNull Context context) {
        super(context);
    }

    public PopupLayer(@NonNull Activity activity) {
        super(activity);
    }

    public PopupLayer(@NonNull View targetView) {
        super(targetView.getContext());
        getViewHolder().setTarget(targetView);
    }

    @IntRange(from = 0)
    @Override
    protected int getLevel() {
        return Level.POPUP;
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
    protected void onDetach() {
        if (mOnScrollChangedListener != null) {
            ViewTreeObserver viewTreeObserver = getViewHolder().getParent().getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener);
            }
            mOnScrollChangedListener = null;
        }
        super.onDetach();
    }

    @Override
    protected void onInitContainer() {
        super.onInitContainer();
        getViewHolder().getContentWrapper().setClipChildren(getConfig().mContentClip);
        getViewHolder().getContainer().setClipChildren(getConfig().mContentClip);
        getViewHolder().getContainer().setClipToPadding(true);
        FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) getViewHolder().getContent().getLayoutParams();
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        if (contentParams.width == FrameLayout.LayoutParams.MATCH_PARENT) {
            contentWrapperParams.width = FrameLayout.LayoutParams.MATCH_PARENT;
        } else {
            contentWrapperParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        }
        if (contentParams.height == FrameLayout.LayoutParams.MATCH_PARENT) {
            contentWrapperParams.height = FrameLayout.LayoutParams.MATCH_PARENT;
        } else {
            contentWrapperParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        }
        getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
        Utils.getViewSize(getViewHolder().getContainer(), new Runnable() {
            @Override
            public void run() {
                updateLocation();
            }
        });
        ViewTreeObserver viewTreeObserver = getViewHolder().getParent().getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (getConfig().mViewTreeScrollChangedToDismiss) {
                        dismiss();
                    }
                    if (getConfig().mOnViewTreeScrollChangedListener != null) {
                        getConfig().mOnViewTreeScrollChangedListener.onScrollChanged();
                    }
                    updateLocation();
                }
            };
            viewTreeObserver.addOnScrollChangedListener(mOnScrollChangedListener);
        }
    }

    @Override
    protected void onInitBackground() {
        super.onInitBackground();
    }

    @Override
    protected void onInitContent() {
        super.onInitContent();
        final FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) getViewHolder().getContent().getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contentParams.gravity = FrameLayout.LayoutParams.UNSPECIFIED_GRAVITY;
        } else {
            contentParams.gravity = -1;
        }
        getViewHolder().getContent().setLayoutParams(contentParams);
    }

    @NonNull
    @Override
    protected Animator onCreateDefContentInAnimator(@NonNull View view) {
        return AnimatorHelper.createTopInAnim(view);
    }

    @NonNull
    @Override
    protected Animator onCreateDefContentOutAnimator(@NonNull View view) {
        return AnimatorHelper.createTopOutAnim(view);
    }

    @Override
    protected void fitDecorInsides() {
        fitDecorMarginTo(getViewHolder().getContainer());
        Utils.onViewLayout(getViewHolder().getDecor(), new Runnable() {
            @Override
            public void run() {
                updateLocation();
            }
        });
    }

    private void initContentLocation(int targetX, int targetY, int targetWidth, int targetHeight) {
        resetLocationTemp();
        final int[] location = mLocationTemp;
        getViewHolder().getContainer().getLocationInWindow(location);
        int parentX = location[0];
        int parentY = location[1];
        int parentWidth = getViewHolder().getContainer().getWidth();
        int parentHeight = getViewHolder().getContainer().getHeight();
        int width = getViewHolder().getContentWrapper().getWidth();
        int height = getViewHolder().getContentWrapper().getHeight();
        FrameLayout.LayoutParams clp = (FrameLayout.LayoutParams) getViewHolder().getContent().getLayoutParams();
        FrameLayout.LayoutParams cwlp = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        float w = width;
        float h = height;
        float x = 0;
        float y = 0;
        switch (getConfig().mAlignHorizontal) {
            case CENTER:
                if (clp.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int l = targetX - parentX;
                    int r = parentX + parentWidth - (targetX + targetWidth);
                    if (l < r) {
                        w = targetWidth + l * 2;
                        x = 0;
                    } else {
                        w = targetWidth + r * 2;
                        x = l - r;
                    }
                    w -= getConfig().mOffsetX;
                } else {
                    x = targetX - parentX - (width - targetWidth) / 2F;
                }
                break;
            case TO_LEFT:
                if (clp.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    w = targetX - parentX;
                    w -= getConfig().mOffsetX;
                    x = 0;
                } else {
                    x = targetX - parentX - width;
                }
                break;
            case TO_RIGHT:
                if (clp.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    w = parentX + parentWidth - (targetX + targetWidth);
                    w -= getConfig().mOffsetX;
                }
                x = targetX - parentX + targetWidth;
                break;
            case ALIGN_LEFT:
                if (clp.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    w = parentWidth - (targetX - parentX);
                    w -= getConfig().mOffsetX;
                }
                x = targetX - parentX;
                break;
            case ALIGN_RIGHT:
                if (clp.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    w = targetX - parentX + targetWidth;
                    w -= getConfig().mOffsetX;
                    x = 0;
                } else {
                    x = targetX - parentX - (width - targetWidth);
                }
                break;
            case CENTER_PARENT:
                if (clp.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    w -= getConfig().mOffsetX;
                    x = 0;
                } else {
                    x = (parentWidth - width) / 2F;
                }
                break;
            case TO_PARENT_LEFT:
                x = -width;
                break;
            case TO_PARENT_RIGHT:
                x = parentWidth;
                break;
            case ALIGN_PARENT_LEFT:
                if (clp.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    w -= getConfig().mOffsetX;
                }
                x = 0;
                break;
            case ALIGN_PARENT_RIGHT:
                if (clp.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    w -= getConfig().mOffsetX;
                    x = 0;
                } else {
                    x = parentX + parentWidth - width;
                }
                break;
            default:
                break;
        }
        switch (getConfig().mAlignVertical) {
            case CENTER:
                if (clp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int t = targetY - parentY;
                    int b = parentY + parentHeight - (targetY + targetHeight);
                    if (t < b) {
                        h = targetHeight + t * 2;
                        y = 0;
                    } else {
                        h = targetHeight + b * 2;
                        y = t - b;
                    }
                    h -= getConfig().mOffsetY;
                } else {
                    y = targetY - parentY - (height - targetHeight) / 2F;
                }
                break;
            case ABOVE:
                if (clp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    h = targetY - parentY;
                    h -= getConfig().mOffsetY;
                    y = 0;
                } else {
                    y = targetY - parentY - height;
                }
                break;
            case BELOW:
                if (clp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    h = parentY + parentHeight - (targetY + targetHeight);
                    h -= getConfig().mOffsetY;
                }
                y = targetY - parentY + targetHeight;
                break;
            case ALIGN_TOP:
                if (clp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    h = parentHeight - (targetY - parentY);
                    h -= getConfig().mOffsetY;
                }
                y = targetY - parentY;
                break;
            case ALIGN_BOTTOM:
                if (clp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    h = targetY - parentY + targetHeight;
                    h -= getConfig().mOffsetY;
                    y = 0;
                } else {
                    y = targetY - parentY - (height - targetHeight);
                }
                break;
            case CENTER_PARENT:
                if (clp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    h -= getConfig().mOffsetY;
                    y = 0;
                } else {
                    y = (parentHeight - height) / 2F;
                }
                break;
            case ABOVE_PARENT:
                y = -height;
                break;
            case BELOW_PARENT:
                y = parentHeight;
                break;
            case ALIGN_PARENT_TOP:
                if (clp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    h -= getConfig().mOffsetY;
                }
                y = 0;
                break;
            case ALIGN_PARENT_BOTTOM:
                if (clp.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    h -= getConfig().mOffsetY;
                    y = 0;
                } else {
                    y = parentY + parentHeight - height;
                }
                break;
            default:
                break;
        }
        if (getConfig().mUpdateLocationInterceptor != null) {
            float[] xy = new float[]{x, y};
            getConfig().mUpdateLocationInterceptor.interceptor(
                    xy, (int) w, (int) h,
                    targetX, targetY, targetWidth, targetHeight,
                    parentX, parentY, parentWidth, parentHeight
            );
            x = xy[0];
            y = xy[1];
        }
        if (getConfig().mOffsetX != 0) {
            x += getConfig().mOffsetX;
        }
        if (getConfig().mOffsetY != 0) {
            y += getConfig().mOffsetY;
        }
        if (getConfig().mInside) {
            x = Utils.floatRange(x, 0, parentWidth - w);
            y = Utils.floatRange(y, 0, parentHeight - h);
        }
        getViewHolder().getContentWrapper().setX(x);
        getViewHolder().getContentWrapper().setY(y);
        boolean paramsChanged = false;
        if (width != w) {
            paramsChanged = true;
        }
        if (height != h) {
            paramsChanged = true;
        }
        if (paramsChanged) {
            cwlp.width = (int) w;
            cwlp.height = (int) h;
            getViewHolder().getContentWrapper().setLayoutParams(cwlp);
            Utils.onViewLayout(getViewHolder().getContentWrapper(), new Runnable() {
                @Override
                public void run() {
                    updateLocation();
                }
            });
        }
    }

    private void initBackgroundLocation() {
        View background = getViewHolder().getBackground();
        if (background == null) {
            return;
        }
        if (!getConfig().mBackgroundAlign) {
            background.setX(0);
            background.setY(0);
            return;
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) background.getLayoutParams();
        int width = background.getWidth();
        int height = background.getHeight();
        int cww = getViewHolder().getContentWrapper().getWidth();
        int cwh = getViewHolder().getContentWrapper().getHeight();
        final float cwx = getViewHolder().getContentWrapper().getX();
        final float cwy = getViewHolder().getContentWrapper().getY();
        float x = 0, y = 0, w = params.width, h = params.height;
        int parentW = getViewHolder().getContainer().getWidth();
        int parentH = getViewHolder().getContainer().getHeight();
        if (getConfig().mAlignDirection == Align.Direction.HORIZONTAL) {
            switch (getConfig().mAlignHorizontal) {
                case TO_RIGHT:
                case ALIGN_LEFT:
                case ALIGN_PARENT_LEFT:
                    x = cwx;
                    if (getConfig().mBackgroundResize) {
                        w = (int) (parentW - cwx);
                    }
                    break;
                case TO_LEFT:
                case ALIGN_RIGHT:
                case ALIGN_PARENT_RIGHT:
                    x = -(width - (cwx + cww));
                    if (getConfig().mBackgroundResize) {
                        w = cwx + cww;
                        x = 0;
                    }
                    break;
                case CENTER:
                default:
                    break;
            }
        } else if (getConfig().mAlignDirection == Align.Direction.VERTICAL) {
            switch (getConfig().mAlignVertical) {
                case BELOW:
                case ALIGN_TOP:
                case ALIGN_PARENT_TOP:
                    y = cwy;
                    if (getConfig().mBackgroundResize) {
                        h = (int) (parentH - cwy);
                    }
                    break;
                case ABOVE:
                case ALIGN_BOTTOM:
                case ALIGN_PARENT_BOTTOM:
                    y = -(height - (cwy + cwh));
                    if (getConfig().mBackgroundResize) {
                        h = cwy + cwh;
                        y = 0;
                    }
                    break;
                case CENTER:
                default:
                    break;
            }
        }
        background.setX(x);
        background.setY(y);
        boolean changed = false;
        if (params.width != w) {
            changed = true;
        }
        if (params.height != h) {
            changed = true;
        }
        if (changed) {
            params.width = (int) w;
            params.height = (int) h;
            background.setLayoutParams(params);
            Utils.onViewLayout(background, new Runnable() {
                @Override
                public void run() {
                    updateLocation();
                }
            });
        }
    }

    private void resetLocationTemp() {
        mLocationTemp[0] = 0;
        mLocationTemp[1] = 0;
    }

    public void updateLocation() {
        final View target = getViewHolder().getTarget();
        resetLocationTemp();
        final int[] location = mLocationTemp;
        if (target != null) {
            target.getLocationInWindow(location);
        }
        final int[] locationRoot = new int[2];
        getViewHolder().getDecor().getLocationInWindow(locationRoot);
        final int targetX = (location[0] - locationRoot[0]);
        final int targetY = (location[1] - locationRoot[1]);
        int targetWidth = 0;
        int targetHeight = 0;
        if (target != null) {
            targetWidth = target.getWidth();
            targetHeight = target.getHeight();
        }
        initContentLocation(targetX, targetY, targetWidth, targetHeight);
        initBackgroundLocation();
    }

    @NonNull
    public PopupLayer setUpdateLocationInterceptor(@Nullable UpdateLocationInterceptor interceptor) {
        getConfig().mUpdateLocationInterceptor = interceptor;
        return this;
    }

    @NonNull
    public PopupLayer setOnViewTreeScrollChangedListener(@Nullable OnViewTreeScrollChangedListener listener) {
        getConfig().mOnViewTreeScrollChangedListener = listener;
        return this;
    }

    @NonNull
    public PopupLayer setScrollChangedToDismiss(boolean toDismiss) {
        getConfig().mViewTreeScrollChangedToDismiss = toDismiss;
        return this;
    }

    @NonNull
    public PopupLayer setTargetView(@Nullable View targetView) {
        getViewHolder().setTarget(targetView);
        updateLocation();
        return this;
    }

    /**
     * 是否裁剪contentView至包裹边界
     *
     * @param clip 是否裁剪contentView至包裹边界
     */
    @NonNull
    public PopupLayer setContentClip(boolean clip) {
        getConfig().mContentClip = clip;
        return this;
    }

    /**
     * 是否偏移背景对齐目标控件
     *
     * @param align 是否偏移背景对齐目标控件
     */
    @NonNull
    public PopupLayer setBackgroundAlign(boolean align) {
        getConfig().mBackgroundAlign = align;
        return this;
    }

    /**
     * 背景应用offset设置
     *
     * @param offset 是否背景应用offset设置
     */
    @NonNull
    public PopupLayer setBackgroundOffset(boolean offset) {
        getConfig().mBackgroundOffset = offset;
        return this;
    }

    /**
     * 背景重新调整尺寸
     * 在可移动时可能会有延迟，纯色背景不建议设置true
     *
     * @param resize 背景重新调整尺寸
     */
    @NonNull
    public PopupLayer setBackgroundResize(boolean resize) {
        getConfig().mBackgroundResize = resize;
        return this;
    }

    /**
     * 当以target方式创建时为参照View位置显示
     * 可自己指定浮层相对于参照View的对齐方式
     *
     * @param direction  主方向
     * @param horizontal 水平对齐方式
     * @param vertical   垂直对齐方式
     * @param inside     是否强制位于屏幕内部
     */
    @NonNull
    public PopupLayer setAlign(@NonNull Align.Direction direction,
                               @NonNull Align.Horizontal horizontal,
                               @NonNull Align.Vertical vertical,
                               boolean inside) {
        getConfig().mAlignDirection = direction;
        getConfig().mAlignHorizontal = horizontal;
        getConfig().mAlignVertical = vertical;
        getConfig().mInside = inside;
        return this;
    }

    /**
     * 指定浮层相对于参照View的对齐方式
     *
     * @param direction 主方向
     */
    @NonNull
    public PopupLayer setDirection(@NonNull Align.Direction direction) {
        getConfig().mAlignDirection = direction;
        return this;
    }

    /**
     * 指定浮层相对于参照View的对齐方式
     *
     * @param horizontal 水平对齐方式
     */
    @NonNull
    public PopupLayer setHorizontal(@NonNull Align.Horizontal horizontal) {
        getConfig().mAlignHorizontal = horizontal;
        return this;
    }

    /**
     * 指定浮层相对于参照View的对齐方式
     *
     * @param vertical 垂直对齐方式
     */
    @NonNull
    public PopupLayer setVertical(@NonNull Align.Vertical vertical) {
        getConfig().mAlignVertical = vertical;
        return this;
    }

    /**
     * 指定浮层是否强制位于屏幕内部
     *
     * @param inside 是否强制位于屏幕内部
     */
    @NonNull
    public PopupLayer setInside(boolean inside) {
        getConfig().mInside = inside;
        return this;
    }

    /**
     * X轴偏移
     *
     * @param offsetX X轴偏移
     */
    @NonNull
    public PopupLayer setOffsetX(float offsetX, int unit) {
        getConfig().mOffsetX = TypedValue.applyDimension(unit, offsetX, getActivity().getResources().getDisplayMetrics());
        return this;
    }

    /**
     * X轴偏移
     *
     * @param dp X轴偏移
     */
    @NonNull
    public PopupLayer setOffsetXdp(float dp) {
        return setOffsetX(dp, TypedValue.COMPLEX_UNIT_DIP);
    }

    /**
     * X轴偏移
     *
     * @param px X轴偏移
     */
    @NonNull
    public PopupLayer setOffsetXpx(float px) {
        return setOffsetX(px, TypedValue.COMPLEX_UNIT_PX);
    }

    /**
     * Y轴偏移
     *
     * @param offsetY Y轴偏移
     */
    @NonNull
    public PopupLayer setOffsetY(float offsetY, int unit) {
        getConfig().mOffsetY = TypedValue.applyDimension(unit, offsetY, getActivity().getResources().getDisplayMetrics());
        return this;
    }

    /**
     * Y轴偏移
     *
     * @param dp Y轴偏移
     */
    @NonNull
    public PopupLayer setOffsetYdp(float dp) {
        return setOffsetY(dp, TypedValue.COMPLEX_UNIT_DIP);
    }

    /**
     * Y轴偏移
     *
     * @param px Y轴偏移
     */
    @NonNull
    public PopupLayer setOffsetYpx(float px) {
        return setOffsetY(px, TypedValue.COMPLEX_UNIT_PX);
    }

    public static class ViewHolder extends DialogLayer.ViewHolder {
        @Nullable
        private View mTarget;

        public void setTarget(@Nullable View target) {
            mTarget = target;
        }

        @Nullable
        public View getTarget() {
            return mTarget;
        }
    }

    protected static class Config extends DialogLayer.Config {
        @Nullable
        protected OnViewTreeScrollChangedListener mOnViewTreeScrollChangedListener = null;
        protected boolean mViewTreeScrollChangedToDismiss = false;
        @Nullable
        protected UpdateLocationInterceptor mUpdateLocationInterceptor = null;
        protected boolean mContentClip = true;
        protected boolean mBackgroundAlign = true;
        protected boolean mBackgroundOffset = true;
        protected boolean mBackgroundResize = false;
        protected boolean mInside = true;
        @NonNull
        protected Align.Direction mAlignDirection = Align.Direction.VERTICAL;
        @NonNull
        protected Align.Horizontal mAlignHorizontal = Align.Horizontal.CENTER;
        @NonNull
        protected Align.Vertical mAlignVertical = Align.Vertical.BELOW;
        protected float mOffsetX = 0F;
        protected float mOffsetY = 0F;
    }

    protected static class ListenerHolder extends DialogLayer.ListenerHolder {
    }

    public interface UpdateLocationInterceptor {
        void interceptor(@NonNull float[] popupXY, int popupWidth, int popupHeight,
                         int targetX, int targetY, int targetWidth, int targetHeight,
                         int parentX, int parentY, int parentWidth, int parentHeight);
    }

    public interface OnViewTreeScrollChangedListener {
        void onScrollChanged();
    }

    public static final class Align {
        /**
         * 主方向
         */
        public enum Direction {
            HORIZONTAL,
            VERTICAL
        }

        /**
         * 水平对齐方式
         */
        public enum Horizontal {
            CENTER,
            TO_LEFT,
            TO_RIGHT,
            ALIGN_LEFT,
            ALIGN_RIGHT,
            CENTER_PARENT,
            TO_PARENT_LEFT,
            TO_PARENT_RIGHT,
            ALIGN_PARENT_LEFT,
            ALIGN_PARENT_RIGHT
        }

        /**
         * 垂直对齐方式
         */
        public enum Vertical {
            CENTER,
            ABOVE,
            BELOW,
            ALIGN_TOP,
            ALIGN_BOTTOM,
            CENTER_PARENT,
            ABOVE_PARENT,
            BELOW_PARENT,
            ALIGN_PARENT_TOP,
            ALIGN_PARENT_BOTTOM
        }
    }
}
