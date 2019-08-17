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
        super.onDetach();
    }

    @Override
    protected void initContainer() {
        super.initContainer();
        if (!getConfig().mOutsideInterceptTouchEvent) {
            getViewHolder().getChild().setOnClickListener(null);
            getViewHolder().getChild().setClickable(false);
        }
        initContainerWithTarget();
    }

    private void initContainerWithTarget() {
        getViewHolder().getChild().setClipToPadding(false);
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        contentWrapperParams.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        contentWrapperParams.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
        final int[] locationTarget = new int[2];
        getViewHolder().getTarget().getLocationOnScreen(locationTarget);
        final int[] locationRoot = new int[2];
        getViewHolder().getDecor().getLocationOnScreen(locationRoot);
        final int targetX = (locationTarget[0] - locationRoot[0]);
        final int targetY = (locationTarget[1] - locationRoot[1]);
        final int targetWidth = getViewHolder().getTarget().getWidth();
        final int targetHeight = getViewHolder().getTarget().getHeight();
        final int[] padding = initContainerWithTargetPadding(targetX, targetY, targetWidth, targetHeight);
        getViewHolder().getChild().getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (getViewHolder().getChild().getViewTreeObserver().isAlive()) {
                    getViewHolder().getChild().getViewTreeObserver().removeOnPreDrawListener(this);
                }
                initContainerWithTargetMargin(targetX, targetY, targetWidth, targetHeight, padding[0], padding[1]);
                return false;
            }
        });
        if (!getConfig().mOutsideInterceptTouchEvent) {
            getViewHolder().getChild().getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    final int[] locationTarget = new int[2];
                    getViewHolder().getTarget().getLocationOnScreen(locationTarget);
                    final int[] locationRoot = new int[2];
                    getViewHolder().getDecor().getLocationOnScreen(locationRoot);
                    final int targetX = (locationTarget[0] - locationRoot[0]);
                    final int targetY = (locationTarget[1] - locationRoot[1]);
                    final int targetWidth = getViewHolder().getTarget().getWidth();
                    final int targetHeight = getViewHolder().getTarget().getHeight();
                    final int[] padding = initContainerWithTargetPadding(targetX, targetY, targetWidth, targetHeight);
                    initContainerWithTargetMargin(targetX, targetY, targetWidth, targetHeight, padding[0], padding[1]);
                }
            });
        }
    }

    private int[] initContainerWithTargetPadding(int targetX, int targetY, int targetWidth, int targetHeight) {
        final int[] padding = new int[]{0, 0, 0, 0};
        FrameLayout.LayoutParams containerParams = (FrameLayout.LayoutParams) getViewHolder().getChild().getLayoutParams();
        if (getConfig().mAlignmentDirection == Alignment.Direction.HORIZONTAL) {
            if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
                padding[2] = containerParams.width - targetX;
            } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
                padding[0] = targetX + targetWidth;
            } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
                padding[0] = targetX;
            } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
                padding[2] = containerParams.width - targetX - targetWidth;
            }
        } else if (getConfig().mAlignmentDirection == Alignment.Direction.VERTICAL) {
            if (getConfig().mAlignmentVertical == Alignment.Vertical.ABOVE) {
                padding[3] = containerParams.height - targetY;
            } else if (getConfig().mAlignmentVertical == Alignment.Vertical.BELOW) {
                padding[1] = targetY + targetHeight;
            } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
                padding[1] = targetY;
            } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
                padding[3] = containerParams.height - targetY - targetHeight;
            }
        }
        getViewHolder().getChild().setPadding(padding[0], padding[1], padding[2], padding[3]);
        return padding;
    }

    private void initContainerWithTargetMargin(int targetX, int targetY, int targetWidth, int targetHeight, int paddingLeft, int paddingTop) {
        final int width = getViewHolder().getContentWrapper().getWidth();
        final int height = getViewHolder().getContentWrapper().getHeight();
        int x = 0;
        int y = 0;
        if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.CENTER) {
            x = targetX - (width - targetWidth) / 2;
        } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.TO_LEFT) {
            x = targetX - width;
        } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.TO_RIGHT) {
            x = targetX + targetWidth;
        } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.ALIGN_LEFT) {
            x = targetX;
        } else if (getConfig().mAlignmentHorizontal == Alignment.Horizontal.ALIGN_RIGHT) {
            x = targetX - (width - targetWidth);
        }
        if (getConfig().mAlignmentVertical == Alignment.Vertical.CENTER) {
            y = targetY - (height - targetHeight) / 2;
        } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ABOVE) {
            y = targetY - height;
        } else if (getConfig().mAlignmentVertical == Alignment.Vertical.BELOW) {
            y = targetY + targetHeight;
        } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ALIGN_TOP) {
            y = targetY;
        } else if (getConfig().mAlignmentVertical == Alignment.Vertical.ALIGN_BOTTOM) {
            y = targetY - (height - targetHeight);
        }
        x = x - paddingLeft;
        y = y - paddingTop;
        if (getConfig().mAlignmentInside) {
            final int maxWidth = getViewHolder().getChild().getWidth() - getViewHolder().getChild().getPaddingLeft() - getViewHolder().getChild().getPaddingRight();
            final int maxHeight = getViewHolder().getChild().getHeight() - getViewHolder().getChild().getPaddingTop() - getViewHolder().getChild().getPaddingBottom();
            final int maxX = maxWidth - width;
            final int maxY = maxHeight - height;
            if (x < 0) {
                x = 0;
            } else if (x > maxX) {
                x = maxX;
            }
            if (y < 0) {
                y = 0;
            } else if (y > maxY) {
                y = maxY;
            }
        }
        FrameLayout.LayoutParams contentWrapperParams = (FrameLayout.LayoutParams) getViewHolder().getContentWrapper().getLayoutParams();
        contentWrapperParams.leftMargin = x;
        contentWrapperParams.topMargin = y;
        getViewHolder().getContentWrapper().setLayoutParams(contentWrapperParams);
    }

    @Override
    protected void initBackground() {
        super.initBackground();
    }

    @Override
    protected void initContent() {
        super.initContent();
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
    public PopupLayer alignment(Alignment.Direction direction,
                                Alignment.Horizontal horizontal,
                                Alignment.Vertical vertical,
                                boolean inside) {
        getConfig().mAlignmentDirection = Utils.requireNonNull(direction, "direction == null");
        getConfig().mAlignmentHorizontal = Utils.requireNonNull(horizontal, "horizontal == null");
        getConfig().mAlignmentVertical = Utils.requireNonNull(vertical, "vertical == null");
        getConfig().mAlignmentInside = inside;
        return this;
    }

    /**
     * 指定浮层是否强制位于屏幕内部
     *
     * @param inside 是否强制位于屏幕内部
     */
    public PopupLayer inside(boolean inside) {
        getConfig().mAlignmentInside = inside;
        return this;
    }

    /**
     * 指定浮层相对于参照View的对齐方式
     *
     * @param vertical 垂直对齐方式
     */
    public PopupLayer alignment(Alignment.Vertical vertical) {
        getConfig().mAlignmentVertical = Utils.requireNonNull(vertical, "vertical == null");
        return this;
    }

    /**
     * 指定浮层相对于参照View的对齐方式
     *
     * @param horizontal 水平对齐方式
     */
    public PopupLayer direction(Alignment.Horizontal horizontal) {
        getConfig().mAlignmentHorizontal = Utils.requireNonNull(horizontal, "horizontal == null");
        return this;
    }

    /**
     * 指定浮层相对于参照View的对齐方式
     *
     * @param direction 主方向
     */
    public PopupLayer direction(Alignment.Direction direction) {
        getConfig().mAlignmentDirection = Utils.requireNonNull(direction, "direction == null");
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

        protected boolean mAlignmentInside = false;
        protected Alignment.Direction mAlignmentDirection = Alignment.Direction.VERTICAL;
        protected Alignment.Horizontal mAlignmentHorizontal = Alignment.Horizontal.CENTER;
        protected Alignment.Vertical mAlignmentVertical = Alignment.Vertical.BELOW;
    }

    protected static class ListenerHolder extends DialogLayer.ListenerHolder {
    }
}
