package per.goweii.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
    protected Animator onCreateInAnimator(View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createInAnimator(getViewHolder().getBackground());
        } else {
            backgroundAnimator = AnimatorHelper.createAlphaInAnim(getViewHolder().getBackground());
        }
        Animator contentAnimator;
        if (getConfig().mContentAnimatorCreator != null) {
            contentAnimator = getConfig().mContentAnimatorCreator.createInAnimator(getViewHolder().getContent());
        } else {
            contentAnimator = AnimatorHelper.createTopInAnim(getViewHolder().getContent());
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
    }

    @Override
    protected Animator onCreateOutAnimator(View view) {
        Animator backgroundAnimator;
        if (getConfig().mBackgroundAnimatorCreator != null) {
            backgroundAnimator = getConfig().mBackgroundAnimatorCreator.createOutAnimator(getViewHolder().getBackground());
        } else {
            backgroundAnimator = AnimatorHelper.createAlphaOutAnim(getViewHolder().getBackground());
        }
        Animator contentAnimator;
        if (getConfig().mContentAnimatorCreator != null) {
            contentAnimator = getConfig().mContentAnimatorCreator.createOutAnimator(getViewHolder().getContent());
        } else {
            contentAnimator = AnimatorHelper.createTopOutAnim(getViewHolder().getContent());
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(backgroundAnimator, contentAnimator);
        return animatorSet;
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
        if (mOnScrollChangedListener != null) {
            getViewHolder().getParent().getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
            mOnScrollChangedListener = null;
        }
        super.onDetach();
    }

    @Override
    protected void initContainer() {
        super.initContainer();
        if (!getConfig().mOutsideInterceptTouchEvent) {
            getViewHolder().getChild().setOnClickListener(null);
            getViewHolder().getChild().setClickable(false);
        }
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
        getViewHolder().getChild().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (getViewHolder().getChild().getViewTreeObserver().isAlive()) {
                    getViewHolder().getChild().getViewTreeObserver().removeOnPreDrawListener(this);
                }
                initLocation();
                return false;
            }
        });
        if (!getConfig().mOutsideInterceptTouchEvent) {
            mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    initLocation();
                }
            };
            getViewHolder().getParent().getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);
        }
    }

    private void initLocation() {
        final int[] locationTarget = new int[2];
        getViewHolder().getTarget().getLocationOnScreen(locationTarget);
        final int[] locationRoot = new int[2];
        getViewHolder().getDecor().getLocationOnScreen(locationRoot);
        final int targetX = (locationTarget[0] - locationRoot[0]);
        final int targetY = (locationTarget[1] - locationRoot[1]);
        final int targetWidth = getViewHolder().getTarget().getWidth();
        final int targetHeight = getViewHolder().getTarget().getHeight();
        initContentWrapperLocation(targetX, targetY, targetWidth, targetHeight);
        if (getConfig().mBackgroundAlign) {
            initBackgroundLocation(targetX, targetY, targetWidth, targetHeight);
        }
    }

    private void initContentWrapperLocation(int targetX, int targetY, int targetWidth, int targetHeight) {
        final int width;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        if (params.width == FrameLayout.LayoutParams.MATCH_PARENT) {
            width = getViewHolder().getChild().getWidth();
        } else {
            width = getViewHolder().getContentWrapper().getWidth();
        }
        final int height;
        if (params.height == FrameLayout.LayoutParams.MATCH_PARENT) {
            height = getViewHolder().getChild().getHeight();
        } else {
            height = getViewHolder().getContentWrapper().getHeight();
        }
        int x = 0, y = 0;
        if (getConfig().mBackgroundOffset) {
            x += getConfig().mOffsetX;
            y += getConfig().mOffsetY;
        }
        switch (getConfig().mAlignHorizontal) {
            case CENTER:
                x += targetX - (width - targetWidth) / 2;
                break;
            case TO_LEFT:
                x += targetX - width;
                break;
            case TO_RIGHT:
                x += targetX + targetWidth;
                break;
            case ALIGN_LEFT:
                x += targetX;
                break;
            case ALIGN_RIGHT:
                x += targetX - (width - targetWidth);
                break;
            default:
                break;
        }
        switch (getConfig().mAlignVertical) {
            case CENTER:
                y += targetY - (height - targetHeight) / 2;
                break;
            case ABOVE:
                y += targetY - height;
                break;
            case BELOW:
                y += targetY + targetHeight;
                break;
            case ALIGN_TOP:
                y += targetY;
                break;
            case ALIGN_BOTTOM:
                y += targetY - (height - targetHeight);
                break;
            default:
                break;
        }
        boolean paramsChanged = false;
        if (params.width == FrameLayout.LayoutParams.MATCH_PARENT) {
            params.leftMargin = x;
            paramsChanged = true;
        } else {
            if (getConfig().mInside) {
                final int maxWidth = getViewHolder().getChild().getWidth();
                final int maxX = maxWidth - width;
                x = Utils.intRange(x, 0, maxX);
            }
            getViewHolder().getContentWrapper().setX(x);
        }
        if (params.height == FrameLayout.LayoutParams.MATCH_PARENT) {
            params.topMargin = y;
            paramsChanged = true;
        } else {
            if (getConfig().mInside) {
                final int maxHeight = getViewHolder().getChild().getHeight();
                final int maxY = maxHeight - height;
                y = Utils.intRange(y, 0, maxY);
            }
            getViewHolder().getContentWrapper().setY(y);
        }
        if (paramsChanged) {
            getViewHolder().getContentWrapper().setLayoutParams(params);
        }
    }

    private void initBackgroundLocation(int targetX, int targetY, int targetWidth, int targetHeight) {
        final int width = getViewHolder().getContentWrapper().getWidth();
        final int height = getViewHolder().getContentWrapper().getHeight();
        final int maxWidth = getViewHolder().getChild().getWidth();
        final int maxHeight = getViewHolder().getChild().getHeight();
        final int maxX = maxWidth - width;
        final int maxY = maxHeight - height;
        final FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) getViewHolder().getChild().getLayoutParams();
        int x = 0, y = 0;
        x += getConfig().mOffsetX;
        y += getConfig().mOffsetY;
        if (getConfig().mAlignDirection == Align.Direction.HORIZONTAL) {
            switch (getConfig().mAlignHorizontal) {
                case TO_LEFT:
                    x += -(containerParams.width - targetX);
                    if (getConfig().mInside) {
                        x = Utils.intRange(x, -maxX, 0);
                    }
                    break;
                case ALIGN_RIGHT:
                    x += -(containerParams.width - targetX - targetWidth);
                    if (getConfig().mInside) {
                        x = Utils.intRange(x, -maxX, 0);
                    }
                    break;
                case TO_RIGHT:
                    x += targetX + targetWidth;
                    if (getConfig().mInside) {
                        x = Utils.intRange(x, 0, maxX);
                    }
                    break;
                case ALIGN_LEFT:
                    x += targetX;
                    if (getConfig().mInside) {
                        x = Utils.intRange(x, 0, maxX);
                    }
                    break;
                case CENTER:
                default:
                    break;
            }
        } else if (getConfig().mAlignDirection == Align.Direction.VERTICAL) {
            switch (getConfig().mAlignVertical) {
                case ABOVE:
                    y += -(containerParams.height - targetY);
                    if (getConfig().mInside) {
                        y = Utils.intRange(y, -maxY, 0);
                    }
                    break;
                case ALIGN_BOTTOM:
                    y += -(containerParams.height - targetY - targetHeight);
                    if (getConfig().mInside) {
                        y = Utils.intRange(y, -maxY, 0);
                    }
                    break;
                case BELOW:
                    y += targetY + targetHeight;
                    if (getConfig().mInside) {
                        y = Utils.intRange(y, 0, maxY);
                    }
                    break;
                case ALIGN_TOP:
                    y += targetY;
                    if (getConfig().mInside) {
                        y = Utils.intRange(y, 0, maxY);
                    }
                    break;
                case CENTER:
                default:
                    break;
            }
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

    /**
     * 设置浮层外部是否拦截触摸
     * 默认为true，false则事件有activityContent本身消费
     *
     * @param intercept 外部是否拦截触摸
     */
    public PopupLayer outsideInterceptTouchEvent(boolean intercept) {
        getConfig().mOutsideInterceptTouchEvent = intercept;
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
        protected boolean mOutsideInterceptTouchEvent = true;

        protected boolean mContentClip = true;
        protected boolean mBackgroundAlign = true;
        protected boolean mBackgroundOffset = true;
        protected boolean mInside = true;
        protected Align.Direction mAlignDirection = Align.Direction.VERTICAL;
        protected Align.Horizontal mAlignHorizontal = Align.Horizontal.CENTER;
        protected Align.Vertical mAlignVertical = Align.Vertical.BELOW;
        protected float mOffsetX = 0F;
        protected float mOffsetY = 0F;
    }

    protected static class ListenerHolder extends DialogLayer.ListenerHolder {
    }
}
