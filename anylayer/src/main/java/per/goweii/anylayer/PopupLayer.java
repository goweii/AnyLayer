package per.goweii.anylayer;

import android.animation.Animator;
import android.animation.AnimatorSet;
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
    public void onPerRemove() {
        super.onPerRemove();
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
        initLocation();
    }

    private void initLocation() {
        getViewHolder().getChild().setClipToPadding(false);
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        contentWrapperParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        contentWrapperParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
        getViewHolder().getChild().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (getViewHolder().getChild().getViewTreeObserver().isAlive()) {
                    getViewHolder().getChild().getViewTreeObserver().removeOnPreDrawListener(this);
                }
                initLocationByTranslation();
                return false;
            }
        });
        if (!getConfig().mOutsideInterceptTouchEvent) {
            mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    initLocationByTranslation();
                }
            };
            getViewHolder().getParent().getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);
        }
    }

    private void initLocationByTranslation() {
        final int[] locationTarget = new int[2];
        getViewHolder().getTarget().getLocationOnScreen(locationTarget);
        final int[] locationRoot = new int[2];
        getViewHolder().getDecor().getLocationOnScreen(locationRoot);
        final int targetX = (locationTarget[0] - locationRoot[0]);
        final int targetY = (locationTarget[1] - locationRoot[1]);
        final int targetWidth = getViewHolder().getTarget().getWidth();
        final int targetHeight = getViewHolder().getTarget().getHeight();
        initContentWrapperTranslation(targetX, targetY, targetWidth, targetHeight);
        if (getConfig().mBackgroundAlign) {
            initBackgroundTranslation(targetX, targetY, targetWidth, targetHeight);
        }
    }

    private void initBackgroundTranslation(int targetX, int targetY, int targetWidth, int targetHeight) {
        final int width = getViewHolder().getContentWrapper().getWidth();
        final int height = getViewHolder().getContentWrapper().getHeight();
        final int maxWidth = getViewHolder().getChild().getWidth();
        final int maxHeight = getViewHolder().getChild().getHeight();
        final int maxX = maxWidth - width;
        final int maxY = maxHeight - height;
        final FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) getViewHolder().getChild().getLayoutParams();
        int x = 0, y = 0;
        if (getConfig().mAlignDirection == Align.Direction.HORIZONTAL) {
            switch (getConfig().mAlignHorizontal) {
                case TO_LEFT:
                    x = -(containerParams.width - targetX);
                    if (getConfig().mInside) x = Utils.intRange(x, -maxX, 0);
                    break;
                case ALIGN_RIGHT:
                    x = -(containerParams.width - targetX - targetWidth);
                    if (getConfig().mInside) x = Utils.intRange(x, -maxX, 0);
                    break;
                case TO_RIGHT:
                    x = targetX + targetWidth;
                    if (getConfig().mInside) x = Utils.intRange(x, 0, maxX);
                    break;
                case ALIGN_LEFT:
                    x = targetX;
                    if (getConfig().mInside) x = Utils.intRange(x, 0, maxX);
                    break;
                case CENTER:
                    break;
                default:
                    break;
            }
        } else if (getConfig().mAlignDirection == Align.Direction.VERTICAL) {
            switch (getConfig().mAlignVertical) {
                case ABOVE:
                    y = -(containerParams.height - targetY);
                    if (getConfig().mInside) y = Utils.intRange(y, -maxY, 0);
                    break;
                case ALIGN_BOTTOM:
                    y = -(containerParams.height - targetY - targetHeight);
                    if (getConfig().mInside) y = Utils.intRange(y, -maxY, 0);
                    break;
                case BELOW:
                    y = targetY + targetHeight;
                    if (getConfig().mInside) y = Utils.intRange(y, 0, maxY);
                    break;
                case ALIGN_TOP:
                    y = targetY;
                    if (getConfig().mInside) y = Utils.intRange(y, 0, maxY);
                    break;
                case CENTER:
                    break;
                default:
                    break;
            }
        }
        getViewHolder().getBackground().setTranslationX(x);
        getViewHolder().getBackground().setTranslationY(y);
    }

    private void initContentWrapperTranslation(int targetX, int targetY, int targetWidth, int targetHeight) {
        final int width = getViewHolder().getContentWrapper().getWidth();
        final int height = getViewHolder().getContentWrapper().getHeight();
        int x = 0, y = 0;
        switch (getConfig().mAlignHorizontal) {
            case CENTER:
                x = targetX - (width - targetWidth) / 2;
                break;
            case TO_LEFT:
                x = targetX - width;
                break;
            case TO_RIGHT:
                x = targetX + targetWidth;
                break;
            case ALIGN_LEFT:
                x = targetX;
                break;
            case ALIGN_RIGHT:
                x = targetX - (width - targetWidth);
                break;
            default:
                break;
        }
        switch (getConfig().mAlignVertical) {
            case CENTER:
                y = targetY - (height - targetHeight) / 2;
                break;
            case ABOVE:
                y = targetY - height;
                break;
            case BELOW:
                y = targetY + targetHeight;
                break;
            case ALIGN_TOP:
                y = targetY;
                break;
            case ALIGN_BOTTOM:
                y = targetY - (height - targetHeight);
                break;
            default:
                break;
        }
        if (getConfig().mInside) {
            final int maxWidth = getViewHolder().getChild().getWidth();
            final int maxHeight = getViewHolder().getChild().getHeight();
            final int maxX = maxWidth - width;
            final int maxY = maxHeight - height;
            x = Utils.intRange(x, 0, maxX);
            y = Utils.intRange(y, 0, maxY);
        }
        getViewHolder().getContentWrapper().setTranslationX(x);
        getViewHolder().getContentWrapper().setTranslationY(y);
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
     * 是否偏移背景对齐目标控件
     *
     * @param align 是否偏移背景对齐目标控件
     */
    public PopupLayer backgroundAlign(boolean align) {
        getConfig().mBackgroundAlign = align;
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
     * 指定浮层相对于参照View的对齐方式
     *
     * @param vertical 垂直对齐方式
     */
    public PopupLayer vertical(Align.Vertical vertical) {
        getConfig().mAlignVertical = Utils.requireNonNull(vertical, "vertical == null");
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
     * @param direction 主方向
     */
    public PopupLayer direction(Align.Direction direction) {
        getConfig().mAlignDirection = Utils.requireNonNull(direction, "direction == null");
        return this;
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

        protected boolean mBackgroundAlign = true;
        protected boolean mInside = true;
        protected Align.Direction mAlignDirection = Align.Direction.VERTICAL;
        protected Align.Horizontal mAlignHorizontal = Align.Horizontal.CENTER;
        protected Align.Vertical mAlignVertical = Align.Vertical.BELOW;
    }

    protected static class ListenerHolder extends DialogLayer.ListenerHolder {
    }
}
