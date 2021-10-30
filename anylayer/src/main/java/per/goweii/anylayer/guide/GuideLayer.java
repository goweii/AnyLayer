package per.goweii.anylayer.guide;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.DecorLayer;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.Utils;

public class GuideLayer extends DecorLayer {

    private final int[] mLocationTemp = new int[2];
    private final Rect mTargetRect = new Rect();

    public GuideLayer(@NonNull Context context) {
        this(Utils.requireActivity(context));
    }

    public GuideLayer(@NonNull Activity activity) {
        super(activity);
    }

    @IntRange(from = 0)
    @Override
    protected int getLevel() {
        return Level.GUIDE;
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
    protected void onAttach() {
        super.onAttach();
        getViewHolder().getChild().setClickable(true);
        getViewHolder().getBackground().setOuterColor(getConfig().mBackgroundColor);
        for (Mapping mapping : getConfig().mMapping) {
            if (mapping.getGuideView() == null) {
                if (mapping.getGuideViewRes() > 0) {
                    View view = LayoutInflater.from(getActivity()).inflate(mapping.getGuideViewRes(), getViewHolder().getContentWrapper(), false);
                    mapping.setGuideView(view);
                }
            }
            if (mapping.getGuideView() != null) {
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mapping.getGuideView().getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = generateGuideDefaultLayoutParams();
                }
                getViewHolder().getContentWrapper().addView(mapping.getGuideView(), layoutParams);
            }
            mapping.bindOnClickListener(this);
        }
        Utils.onViewLayout(getViewHolder().getChild(), new Runnable() {
            @Override
            public void run() {
                updateLocation();
            }
        });
    }

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        final FrameLayout container = new FrameLayout(getActivity());

        final HoleView background = new HoleView(getActivity());
        getViewHolder().setBackground(background);
        background.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        container.addView(background);

        final FrameLayout contentWrapper = new FrameLayout(getActivity());
        getViewHolder().setContentWrapper(contentWrapper);
        contentWrapper.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        container.addView(contentWrapper);

        return container;
    }

    @Override
    protected void onDestroyChild() {
        getViewHolder().getBackground().clear();
        getViewHolder().setBackground(null);
        getViewHolder().getContentWrapper().removeAllViews();
        getViewHolder().setContentWrapper(null);
        super.onDestroyChild();
    }

    @NonNull
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @NonNull
    protected FrameLayout.LayoutParams generateGuideDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    protected Animator onCreateInAnimator(@NonNull View view) {
        return AnimatorHelper.createAlphaInAnim(view);
    }

    @Nullable
    @Override
    protected Animator onCreateOutAnimator(@NonNull View view) {
        return AnimatorHelper.createAlphaOutAnim(view);
    }

    @Override
    protected void fitDecorInsides() {
        fitDecorInsidesToViewPadding(getViewHolder().getContentWrapper());
        Utils.onViewLayout(getViewHolder().getChild(), new Runnable() {
            @Override
            public void run() {
                updateLocation();
            }
        });
    }

    @Override
    public void onGlobalLayout() {
        super.onGlobalLayout();
        updateLocation();
    }

    private void resetLocationTemp() {
        mLocationTemp[0] = 0;
        mLocationTemp[1] = 0;
    }

    public void updateLocation() {
        resetLocationTemp();
        final int[] location = mLocationTemp;
        getViewHolder().getBackground().clear();
        getViewHolder().getChild().getLocationInWindow(location);
        for (Mapping mapping : getConfig().mMapping) {
            mTargetRect.set(mapping.getTargetRect());
            if (!mTargetRect.isEmpty()) {
                mTargetRect.offset(-location[0], -location[1]);
                mTargetRect.offset(mapping.getOffsetX(), mapping.getOffsetY());
                mTargetRect.set(mTargetRect.left - mapping.getPaddingLeft(),
                        mTargetRect.top - mapping.getPaddingTop(),
                        mTargetRect.right + mapping.getPaddingRight(),
                        mTargetRect.bottom + mapping.getPaddingBottom());
                getViewHolder().getBackground().addRect(mTargetRect, mapping.getCornerRadius());
            } else {
                mTargetRect.set(
                        getViewHolder().getContentWrapper().getLeft(),
                        getViewHolder().getContentWrapper().getTop(),
                        getViewHolder().getContentWrapper().getRight(),
                        getViewHolder().getContentWrapper().getBottom()
                );
            }
            initLocation(mTargetRect, mapping);
        }
    }

    private void initLocation(@NonNull Rect rect, @NonNull Mapping mapping) {
        final View view = mapping.getGuideView();
        if (view == null) return;
        view.offsetLeftAndRight(-view.getLeft());
        view.offsetTopAndBottom(-view.getTop());
        FrameLayout parent = getViewHolder().getContentWrapper();
        final int viewWidthWithMargin = view.getWidth() + mapping.getMarginLeft() + mapping.getMarginRight();
        switch (mapping.getHorizontalAlign()) {
            case CENTER:
                view.offsetLeftAndRight(rect.left + (rect.width() - viewWidthWithMargin) / 2 + mapping.getMarginLeft());
                break;
            case TO_LEFT:
                view.offsetLeftAndRight(rect.left - view.getWidth() - mapping.getMarginRight());
                break;
            case TO_RIGHT:
                view.offsetLeftAndRight(rect.right + mapping.getMarginLeft());
                break;
            case ALIGN_LEFT:
                view.offsetLeftAndRight(rect.left + mapping.getMarginLeft());
                break;
            case ALIGN_RIGHT:
                view.offsetLeftAndRight(rect.right - view.getWidth() - mapping.getMarginRight());
                break;
            case CENTER_PARENT:
                view.offsetLeftAndRight((parent.getWidth() - viewWidthWithMargin) / 2 + mapping.getMarginLeft());
                break;
            case TO_PARENT_LEFT:
                view.offsetLeftAndRight(-view.getWidth() - mapping.getMarginRight());
                break;
            case TO_PARENT_RIGHT:
                view.offsetLeftAndRight(parent.getWidth() + mapping.getMarginLeft());
                break;
            case ALIGN_PARENT_LEFT:
                view.offsetLeftAndRight(parent.getPaddingLeft() + mapping.getMarginLeft());
                break;
            case ALIGN_PARENT_RIGHT:
                view.offsetLeftAndRight(parent.getWidth() - parent.getPaddingRight() - view.getWidth() - mapping.getMarginRight());
                break;
        }
        final int viewHeightWithMargin = view.getHeight() + mapping.getMarginTop() + mapping.getMarginBottom();
        switch (mapping.getVerticalAlign()) {
            case CENTER:
                view.offsetTopAndBottom(rect.top + (rect.height() - viewHeightWithMargin) / 2 + mapping.getMarginTop());
                break;
            case ABOVE:
                view.offsetTopAndBottom(rect.top - view.getHeight() - mapping.getMarginBottom());
                break;
            case BELOW:
                view.offsetTopAndBottom(rect.bottom + mapping.getMarginTop());
                break;
            case ALIGN_TOP:
                view.offsetTopAndBottom(rect.top + mapping.getMarginTop());
                break;
            case ALIGN_BOTTOM:
                view.offsetTopAndBottom(rect.bottom - view.getHeight() - mapping.getMarginBottom());
                break;
            case CENTER_PARENT:
                view.offsetTopAndBottom((parent.getHeight() - viewHeightWithMargin) / 2 + mapping.getMarginTop());
                break;
            case ABOVE_PARENT:
                view.offsetTopAndBottom(-view.getHeight() - mapping.getMarginBottom());
                break;
            case BELOW_PARENT:
                view.offsetTopAndBottom(parent.getHeight() + mapping.getMarginTop());
                break;
            case ALIGN_PARENT_TOP:
                view.offsetTopAndBottom(parent.getPaddingTop() + mapping.getMarginTop());
                break;
            case ALIGN_PARENT_BOTTOM:
                view.offsetTopAndBottom(parent.getHeight() - parent.getPaddingBottom() - view.getHeight() - mapping.getMarginBottom());
                break;
        }
    }

    @NonNull
    public GuideLayer addMapping(@NonNull Mapping mapping) {
        getConfig().mMapping.add(mapping);
        return this;
    }

    @NonNull
    public GuideLayer setBackgroundColorInt(@ColorInt int colorInt) {
        getConfig().mBackgroundColor = colorInt;
        return this;
    }

    @NonNull
    public GuideLayer setBackgroundColorRes(@ColorRes int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private HoleView mBackground;
        private FrameLayout mContentWrapper;

        @NonNull
        @Override
        public FrameLayout getChild() {
            return (FrameLayout) super.getChild();
        }

        @Nullable
        @Override
        protected FrameLayout getChildOrNull() {
            return (FrameLayout) super.getChildOrNull();
        }

        public void setBackground(@Nullable HoleView background) {
            mBackground = background;
        }

        public void setContentWrapper(@Nullable FrameLayout contentWrapper) {
            mContentWrapper = contentWrapper;
        }

        @NonNull
        public HoleView getBackground() {
            Utils.requireNonNull(mBackground, "必须在show方法后调用");
            return mBackground;
        }

        @NonNull
        public FrameLayout getContentWrapper() {
            Utils.requireNonNull(mContentWrapper, "必须在show方法后调用");
            return mContentWrapper;
        }
    }

    protected static class Config extends DecorLayer.Config {
        @ColorInt
        protected int mBackgroundColor = ColorUtils.setAlphaComponent(Color.BLACK, (int) (255 * 0.6));
        protected final List<Mapping> mMapping = new ArrayList<>(1);
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
    }

    public static class Mapping {
        private final int[] mTargetLocation = new int[2];
        private final Rect mTargetRect = new Rect();
        @Nullable
        private View mTargetView = null;
        @Nullable
        private View mGuideView = null;
        @LayoutRes
        private int mGuideViewRes = 0;
        private float mCornerRadius = 0F;
        private int mOffsetX = 0;
        private int mOffsetY = 0;
        private int mPaddingLeft = 0;
        private int mPaddingTop = 0;
        private int mPaddingRight = 0;
        private int mPaddingBottom = 0;
        private int mMarginLeft = 0;
        private int mMarginTop = 0;
        private int mMarginRight = 0;
        private int mMarginBottom = 0;
        private Align.Horizontal mHorizontalAlign = Align.Horizontal.CENTER;
        private Align.Vertical mVerticalAlign = Align.Vertical.BELOW;

        private final SparseArray<OnClickListener> mOnClickListeners = new SparseArray<>();

        private void bindOnClickListener(@NonNull final GuideLayer layer) {
            if (mGuideView == null) return;
            for (int i = 0; i < mOnClickListeners.size(); i++) {
                int id = mOnClickListeners.keyAt(i);
                final OnClickListener listener = mOnClickListeners.valueAt(i);
                View view = mGuideView.findViewById(id);
                if (view == null) view = mGuideView;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClick(layer, v);
                    }
                });
            }
        }

        public Mapping addOnClickListener(@NonNull OnClickListener listener, int... viewIds) {
            if (viewIds != null && viewIds.length > 0) {
                for (int id : viewIds) {
                    mOnClickListeners.put(id, listener);
                }
            } else {
                mOnClickListeners.put(View.NO_ID, listener);
            }
            return this;
        }

        @NonNull
        public Mapping setTargetRect(@NonNull Rect targetRect) {
            this.mTargetRect.set(targetRect);
            return this;
        }

        @NonNull
        public Mapping setTargetView(@Nullable View targetView) {
            this.mTargetView = targetView;
            return this;
        }

        @NonNull
        public Mapping setGuideView(@Nullable View guideView) {
            this.mGuideView = guideView;
            return this;
        }

        @NonNull
        public Mapping setGuideView(@LayoutRes int guideViewRes) {
            this.mGuideViewRes = guideViewRes;
            return this;
        }

        @NonNull
        public Mapping setCornerRadius(float cornerRadius) {
            this.mCornerRadius = cornerRadius;
            return this;
        }

        @NonNull
        public Mapping setOffset(int offset) {
            this.mOffsetX = offset;
            this.mOffsetY = offset;
            return this;
        }

        @NonNull
        public Mapping setOffsetX(int offset) {
            this.mOffsetX = offset;
            return this;
        }

        @NonNull
        public Mapping setOffsetY(int offset) {
            this.mOffsetY = offset;
            return this;
        }

        @NonNull
        public Mapping setPadding(int padding) {
            this.mPaddingLeft = padding;
            this.mPaddingTop = padding;
            this.mPaddingRight = padding;
            this.mPaddingBottom = padding;
            return this;
        }

        @NonNull
        public Mapping setPaddingLeft(int padding) {
            this.mPaddingLeft = padding;
            return this;
        }

        @NonNull
        public Mapping setPaddingTop(int padding) {
            this.mPaddingTop = padding;
            return this;
        }

        @NonNull
        public Mapping setPaddingRight(int padding) {
            this.mPaddingRight = padding;
            return this;
        }

        @NonNull
        public Mapping setPaddingBottom(int padding) {
            this.mPaddingBottom = padding;
            return this;
        }

        @NonNull
        public Mapping setMargin(int margin) {
            this.mMarginLeft = margin;
            this.mMarginTop = margin;
            this.mMarginRight = margin;
            this.mMarginBottom = margin;
            return this;
        }

        @NonNull
        public Mapping setMarginLeft(int margin) {
            this.mMarginLeft = margin;
            return this;
        }

        @NonNull
        public Mapping setMarginTop(int margin) {
            this.mMarginTop = margin;
            return this;
        }

        @NonNull
        public Mapping setMarginRight(int margin) {
            this.mMarginRight = margin;
            return this;
        }

        @NonNull
        public Mapping setMarginBottom(int margin) {
            this.mMarginBottom = margin;
            return this;
        }

        @NonNull
        public Mapping setHorizontalAlign(@NonNull Align.Horizontal horizontalAlign) {
            mHorizontalAlign = horizontalAlign;
            return this;
        }

        @NonNull
        public Mapping setVerticalAlign(@NonNull Align.Vertical verticalAlign) {
            mVerticalAlign = verticalAlign;
            return this;
        }

        @NonNull
        public Rect getTargetRect() {
            if (mTargetView != null) {
                final int[] location = mTargetLocation;
                mTargetView.getLocationInWindow(location);
                mTargetRect.set(
                        location[0],
                        location[1],
                        location[0] + mTargetView.getWidth(),
                        location[1] + mTargetView.getHeight()
                );
            }
            return mTargetRect;
        }

        @Nullable
        public View getGuideView() {
            return mGuideView;
        }

        public int getGuideViewRes() {
            return mGuideViewRes;
        }

        public float getCornerRadius() {
            return mCornerRadius;
        }

        public int getOffsetX() {
            return mOffsetX;
        }

        public int getOffsetY() {
            return mOffsetY;
        }

        public int getPaddingLeft() {
            return mPaddingLeft;
        }

        public int getPaddingTop() {
            return mPaddingTop;
        }

        public int getPaddingRight() {
            return mPaddingRight;
        }

        public int getPaddingBottom() {
            return mPaddingBottom;
        }

        public int getMarginLeft() {
            return mMarginLeft;
        }

        public int getMarginTop() {
            return mMarginTop;
        }

        public int getMarginRight() {
            return mMarginRight;
        }

        public int getMarginBottom() {
            return mMarginBottom;
        }

        public Align.Horizontal getHorizontalAlign() {
            return mHorizontalAlign;
        }

        public Align.Vertical getVerticalAlign() {
            return mVerticalAlign;
        }
    }

    public static final class Align {
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
