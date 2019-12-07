package per.goweii.anylayer;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class PopupLayer extends DialogLayer {

    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    public PopupLayer(Context context) {
        super(context);
    }

    public PopupLayer(Activity activity) {
        super(activity);
    }

    public PopupLayer(View targetView) {
        super(Utils.requireNonNull(targetView, "targetView == null").getContext());
        getViewHolder().setTarget(targetView);
    }

    @Override
    protected Level getLevel() {
        return Level.POPUP;
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
    protected View onCreateChild(LayoutInflater inflater, ViewGroup parent) {
        return super.onCreateChild(inflater, parent);
    }

    @Override
    protected Animator onCreateDefContentInAnimator(View view) {
        return AnimatorHelper.createTopInAnim(view);
    }

    @Override
    protected Animator onCreateDefContentOutAnimator(View view) {
        return AnimatorHelper.createTopOutAnim(view);
    }

    @Override
    public void onAttach() {
        super.onAttach();
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
    }

    @Override
    public void onShow() {
        super.onShow();
    }

    @Override
    public void onPreRemove() {
        super.onPreRemove();
    }

    @Override
    public void onDetach() {
        getViewHolder().getParent().getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        mOnScrollChangedListener = null;
        super.onDetach();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Utils.getViewSize(getViewHolder().getBackground(), new Runnable() {
            @Override
            public void run() {
                updateLocation();
            }
        });
    }

    @Override
    protected void initContainer() {
        super.initContainer();
        getViewHolder().getContentWrapper().setClipChildren(getConfig().mContentClip);
        getViewHolder().getChild().setClipChildren(getConfig().mContentClip);
        getViewHolder().getChild().setClipToPadding(false);
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
        Utils.getViewSize(getViewHolder().getChild(), new Runnable() {
            @Override
            public void run() {
                updateLocation();
            }
        });
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
        getViewHolder().getParent().getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);
    }

    private void initContentWrapperLocation(int targetX, int targetY, int targetWidth, int targetHeight) {
        final int[] lp = new int[2];
        getViewHolder().getChild().getLocationOnScreen(lp);
        int parentX = lp[0];
        int parentY = lp[1];
        int parentWidth = getViewHolder().getChild().getWidth();
        int parentHeight = getViewHolder().getChild().getHeight();
        int width = getViewHolder().getContentWrapper().getWidth();
        int height = getViewHolder().getContentWrapper().getHeight();
        FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) getViewHolder().getContent().getLayoutParams();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        float w = width;
        float h = height;
        float x = 0;
        float y = 0;
        switch (getConfig().mAlignHorizontal) {
            case CENTER:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
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
                    x = targetX - (width - targetWidth) / 2;
                }
                break;
            case TO_LEFT:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int l = targetX - parentX;
                    w = l;
                    x = 0;
                    w -= getConfig().mOffsetX;
                } else {
                    x = targetX - width;
                }
                break;
            case TO_RIGHT:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int r = parentX + parentWidth - (targetX + targetWidth);
                    x = targetX + targetWidth;
                    w = r;
                    w -= getConfig().mOffsetX;
                } else {
                    x = targetX + targetWidth;
                }
                break;
            case ALIGN_LEFT:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int l = targetX - parentX;
                    w = parentWidth - l;
                    x = targetX;
                    w -= getConfig().mOffsetX;
                } else {
                    x = targetX;
                }
                break;
            case ALIGN_RIGHT:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int l = targetX - parentX;
                    w = l + targetWidth;
                    x = 0;
                    w -= getConfig().mOffsetX;
                } else {
                    x = targetX - (width - targetWidth);
                }
                break;
            case ALIGN_PARENT_LEFT:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    x = 0;
                    w -= getConfig().mOffsetX;
                } else {
                    x = 0;
                }
                break;
            case ALIGN_PARENT_RIGHT:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    x = 0;
                    w -= getConfig().mOffsetX;
                } else {
                    x = parentX + parentWidth - width;
                }
                break;
            default:
                break;
        }
        switch (getConfig().mAlignVertical) {
            case CENTER:
                if (p.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int t = targetY - parentY;
                    int b = parentY + parentHeight - (targetY + targetHeight);
                    if (t < b) {
                        h = targetHeight + t * 2;
                        y = 0;
                    } else {
                        h = targetHeight + b * 2;
                        y = t - b;
                    }
                } else {
                    y = targetY - (height - targetHeight) / 2;
                }
                h -= getConfig().mOffsetY;
                break;
            case ABOVE:
                if (p.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int t = targetY - parentY;
                    h = t;
                    x = 0;
                    h -= getConfig().mOffsetY;
                } else {
                    y = targetY - height;
                }
                break;
            case BELOW:
                if (p.height == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int b = parentY + parentHeight - (targetY + targetHeight);
                    y = targetY + targetHeight;
                    h = b;
                    h -= getConfig().mOffsetY;
                } else {
                    y = targetY + targetHeight;
                }
                break;
            case ALIGN_TOP:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int t = targetY - parentY;
                    h = parentHeight - t;
                    y = targetY;
                    h -= getConfig().mOffsetY;
                } else {
                    y = targetY;
                }
                break;
            case ALIGN_BOTTOM:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    int t = targetY - parentY;
                    h = t + targetHeight;
                    h -= getConfig().mOffsetY;
                    y = 0;
                } else {
                    y = targetY - (height - targetHeight);
                }
                break;
            case ALIGN_PARENT_TOP:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    h -= getConfig().mOffsetY;
                    y = 0;
                } else {
                    y = 0;
                }
                break;
            case ALIGN_PARENT_BOTTOM:
                if (p.width == FrameLayout.LayoutParams.MATCH_PARENT) {
                    h -= getConfig().mOffsetY;
                    y = 0;
                } else {
                    y = parentY + parentHeight - height;
                }
                break;
            default:
                break;
        }
        boolean paramsChanged = false;
        if (width != w) {
            paramsChanged = true;
        }
        if (height != h) {
            paramsChanged = true;
        }
        if (paramsChanged) {
            params.width = (int) w;
            params.height = (int) h;
            getViewHolder().getContentWrapper().setLayoutParams(params);
        }
        if (getConfig().mUpdateLocationInterceptor != null) {
            float[] xy = new float[]{x, y};
            getConfig().mUpdateLocationInterceptor.interceptor(
                    xy, params.width, params.height,
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
    }

    private void initBackgroundLocation() {
        if (!getConfig().mBackgroundAlign) {
            getViewHolder().getBackground().setX(0);
            getViewHolder().getBackground().setY(0);
            return;
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getViewHolder().getBackground().getLayoutParams();
        int width = getViewHolder().getBackground().getWidth();
        int height = getViewHolder().getBackground().getHeight();
        int cww = getViewHolder().getContentWrapper().getWidth();
        int cwh = getViewHolder().getContentWrapper().getHeight();
        final float cwx = getViewHolder().getContentWrapper().getX();
        final float cwy = getViewHolder().getContentWrapper().getY();
        float x = 0, y = 0, w = params.width, h = params.height;
        int parentW = getViewHolder().getChild().getWidth();
        int parentH = getViewHolder().getChild().getHeight();
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
            getViewHolder().getBackground().setLayoutParams(params);
        }
        getViewHolder().getBackground().setX(x);
        getViewHolder().getBackground().setY(y);
    }

    @Override
    protected void initBackground() {
        super.initBackground();
    }

    @Override
    protected void initContent() {
        super.initContent();
        final FrameLayout.LayoutParams contentParams = (FrameLayout.LayoutParams) getViewHolder().getContent().getLayoutParams();
        contentParams.gravity = -1;
        getViewHolder().getContent().setLayoutParams(contentParams);
    }

    public void updateLocation() {
        final View target = getViewHolder().getTarget();
        final int[] locationTarget = new int[]{0, 0};
        if (target != null) {
            target.getLocationOnScreen(locationTarget);
        }
        final int[] locationRoot = new int[2];
        getViewHolder().getDecor().getLocationOnScreen(locationRoot);
        final int targetX = (locationTarget[0] - locationRoot[0]);
        final int targetY = (locationTarget[1] - locationRoot[1]);
        int targetWidth = 0;
        int targetHeight = 0;
        if (target != null) {
            targetWidth = target.getWidth();
            targetHeight = target.getHeight();
        }
        initContentWrapperLocation(targetX, targetY, targetWidth, targetHeight);
        initBackgroundLocation();
    }

    public PopupLayer updateLocationInterceptor(UpdateLocationInterceptor interceptor) {
        getConfig().mUpdateLocationInterceptor = interceptor;
        return this;
    }

    public PopupLayer onViewTreeScrollChangedListener(OnViewTreeScrollChangedListener listener) {
        getConfig().mOnViewTreeScrollChangedListener = listener;
        return this;
    }

    public PopupLayer scrollChangedToDismiss(boolean toDismiss) {
        getConfig().mViewTreeScrollChangedToDismiss = toDismiss;
        return this;
    }

    public PopupLayer targetView(View targetView) {
        getViewHolder().setTarget(targetView);
        updateLocation();
        return this;
    }

    /**
     * 是否裁剪contentView至包裹边界
     *
     * @param clip 是否裁剪contentView至包裹边界
     */
    public PopupLayer contentClip(boolean clip) {
        getConfig().mContentClip = clip;
        return this;
    }

    /**
     * 是否偏移背景对齐目标控件
     *
     * @param align 是否偏移背景对齐目标控件
     */
    public PopupLayer backgroundAlign(boolean align) {
        getConfig().mBackgroundAlign = align;
        return this;
    }

    /**
     * 背景应用offset设置
     *
     * @param offset 是否背景应用offset设置
     */
    public PopupLayer backgroundOffset(boolean offset) {
        getConfig().mBackgroundOffset = offset;
        return this;
    }

    /**
     * 背景重新调整尺寸
     * 在可移动时可能会有延迟，纯色背景不建议设置true
     *
     * @param resize 背景重新调整尺寸
     */
    public PopupLayer backgroundResize(boolean resize) {
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
    public PopupLayer align(Align.Direction direction,
                            Align.Horizontal horizontal,
                            Align.Vertical vertical,
                            boolean inside) {
        getConfig().mAlignDirection = Utils.requireNonNull(direction, "direction == null");
        getConfig().mAlignHorizontal = Utils.requireNonNull(horizontal, "horizontal == null");
        getConfig().mAlignVertical = Utils.requireNonNull(vertical, "vertical == null");
        getConfig().mInside = inside;
        return this;
    }

    /**
     * 指定浮层相对于参照View的对齐方式
     *
     * @param direction 主方向
     */
    public PopupLayer direction(Align.Direction direction) {
        getConfig().mAlignDirection = Utils.requireNonNull(direction, "direction == null");
        return this;
    }

    /**
     * 指定浮层相对于参照View的对齐方式
     *
     * @param horizontal 水平对齐方式
     */
    public PopupLayer horizontal(Align.Horizontal horizontal) {
        getConfig().mAlignHorizontal = Utils.requireNonNull(horizontal, "horizontal == null");
        return this;
    }

    /**
     * 指定浮层相对于参照View的对齐方式
     *
     * @param vertical 垂直对齐方式
     */
    public PopupLayer vertical(Align.Vertical vertical) {
        getConfig().mAlignVertical = Utils.requireNonNull(vertical, "vertical == null");
        return this;
    }

    /**
     * 指定浮层是否强制位于屏幕内部
     *
     * @param inside 是否强制位于屏幕内部
     */
    public PopupLayer inside(boolean inside) {
        getConfig().mInside = inside;
        return this;
    }

    /**
     * X轴偏移
     *
     * @param offsetX X轴偏移
     */
    public PopupLayer offsetX(float offsetX, int unit) {
        getConfig().mOffsetX = TypedValue.applyDimension(unit, offsetX, getActivity().getResources().getDisplayMetrics());
        return this;
    }

    /**
     * X轴偏移
     *
     * @param dp X轴偏移
     */
    public PopupLayer offsetXdp(float dp) {
        getConfig().mOffsetX = dp;
        return offsetX(dp, TypedValue.COMPLEX_UNIT_DIP);
    }

    /**
     * X轴偏移
     *
     * @param px X轴偏移
     */
    public PopupLayer offsetXpx(float px) {
        getConfig().mOffsetX = px;
        return offsetX(px, TypedValue.COMPLEX_UNIT_PX);
    }

    /**
     * Y轴偏移
     *
     * @param offsetY Y轴偏移
     */
    public PopupLayer offsetY(float offsetY, int unit) {
        getConfig().mOffsetY = TypedValue.applyDimension(unit, offsetY, getActivity().getResources().getDisplayMetrics());
        return this;
    }

    /**
     * Y轴偏移
     *
     * @param dp Y轴偏移
     */
    public PopupLayer offsetYdp(float dp) {
        getConfig().mOffsetY = dp;
        return offsetY(dp, TypedValue.COMPLEX_UNIT_DIP);
    }

    /**
     * Y轴偏移
     *
     * @param px Y轴偏移
     */
    public PopupLayer offsetYpY(float px) {
        getConfig().mOffsetY = px;
        return offsetY(px, TypedValue.COMPLEX_UNIT_PX);
    }

    public static class ViewHolder extends DialogLayer.ViewHolder {
        private View mTarget;

        public void setTarget(View target) {
            mTarget = target;
        }

        public View getTarget() {
            return mTarget;
        }
    }

    protected static class Config extends DialogLayer.Config {
        protected OnViewTreeScrollChangedListener mOnViewTreeScrollChangedListener = null;
        protected boolean mViewTreeScrollChangedToDismiss = false;
        protected UpdateLocationInterceptor mUpdateLocationInterceptor = null;
        protected boolean mContentClip = true;
        protected boolean mBackgroundAlign = true;
        protected boolean mBackgroundOffset = true;
        protected boolean mBackgroundResize = false;
        protected boolean mInside = true;
        protected Align.Direction mAlignDirection = Align.Direction.VERTICAL;
        protected Align.Horizontal mAlignHorizontal = Align.Horizontal.CENTER;
        protected Align.Vertical mAlignVertical = Align.Vertical.BELOW;
        protected float mOffsetX = 0F;
        protected float mOffsetY = 0F;
    }

    protected static class ListenerHolder extends DialogLayer.ListenerHolder {
    }

    public interface UpdateLocationInterceptor {
        void interceptor(float[] popupXY, int popupWidth, int popupHeight,
                         int targetX, int targetY, int targetWidth, int targetHeight,
                         int parentX, int parentY, int parentWidth, int parentHeight);
    }

    public interface OnViewTreeScrollChangedListener {
        void onScrollChanged();
    }
}
