package per.goweii.anylayer.guide;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import per.goweii.anylayer.DecorLayer;
import per.goweii.anylayer.GlobalConfig;
import per.goweii.anylayer.R;
import per.goweii.anylayer.utils.AnimatorHelper;
import per.goweii.anylayer.utils.Utils;

/**
 * @author CuiZhen
 * @date 2019/7/21
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
public class GuideLayer extends DecorLayer {

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

    @NonNull
    @Override
    protected View onCreateChild(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        if (getViewHolder().getChildNullable() == null) {
            FrameLayout container = (FrameLayout) inflater.inflate(R.layout.anylayer_guide_layer, parent, false);
            getViewHolder().setChild(container);
        }
        return getViewHolder().getChild();
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

    @NonNull
    protected FrameLayout.LayoutParams generateGuideDefaultLayoutParams() {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAttach() {
        super.onAttach();
        getViewHolder().getChild().setClickable(true);
        getViewHolder().getBackground().setOuterColor(getConfig().mBackgroundColor);
        for (Mapping mapping : getConfig().mMapping) {
            if (mapping.getGuideView() != null) {
                if (mapping.getGuideViewRes() > 0) {
                    View view = LayoutInflater.from(getActivity()).inflate(mapping.getGuideViewRes(), getViewHolder().getContentWrapper(), false);
                    mapping.guideView(view);
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
    }

    @Override
    public void onPreDraw() {
        super.onPreDraw();
        int[] childLocation = new int[2];
        getViewHolder().getChild().getLocationInWindow(childLocation);
        for (Mapping mapping : getConfig().mMapping) {
            final Rect targetRect = mapping.getTargetRect();
            if (!targetRect.isEmpty()) {
                final Rect holeRect = new Rect(targetRect);
                holeRect.offset(-childLocation[0], -childLocation[1]);
                holeRect.offset(mapping.getOffsetX(), mapping.getOffsetY());
                holeRect.set(holeRect.left - mapping.getPaddingLeft(),
                        holeRect.top - mapping.getPaddingTop(),
                        holeRect.right + mapping.getPaddingRight(),
                        holeRect.bottom + mapping.getPaddingBottom());
                getViewHolder().getBackground().addRect(holeRect, mapping.getCornerRadius());
                initLocation(holeRect, mapping);
            } else {
                Rect wrapperRect = new Rect(0, 0,
                        getViewHolder().getContentWrapper().getWidth(),
                        getViewHolder().getContentWrapper().getHeight());
                initLocation(wrapperRect, mapping);
            }
        }
    }

    private void initLocation(@NonNull Rect rect, @NonNull Mapping mapping) {
        final View view = mapping.getGuideView();
        if (view == null) return;
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
            case ALIGN_PARENT_LEFT:
                view.offsetLeftAndRight(mapping.getMarginLeft());
                break;
            case ALIGN_PARENT_RIGHT:
                view.offsetLeftAndRight(getViewHolder().getContentWrapper().getWidth() - view.getWidth() - mapping.getMarginRight());
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
            case ALIGN_PARENT_TOP:
                view.offsetTopAndBottom(mapping.getMarginTop());
                break;
            case ALIGN_PARENT_BOTTOM:
                view.offsetTopAndBottom(getViewHolder().getContentWrapper().getHeight() - view.getHeight() - mapping.getMarginBottom());
                break;
        }
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
        super.onDetach();
    }

    @NonNull
    public GuideLayer mapping(@NonNull Mapping mapping) {
        getConfig().mMapping.add(mapping);
        return this;
    }

    @NonNull
    public GuideLayer backgroundColorInt(@ColorInt int colorInt) {
        getConfig().mBackgroundColor = colorInt;
        return this;
    }

    @NonNull
    public GuideLayer backgroundColorRes(@ColorRes int colorRes) {
        getConfig().mBackgroundColor = getActivity().getResources().getColor(colorRes);
        return this;
    }

    public static class ViewHolder extends DecorLayer.ViewHolder {
        private HoleView mBackground;
        private FrameLayout mContentWrapper;

        @Override
        public void setChild(@NonNull View child) {
            super.setChild(child);
            mContentWrapper = getChild().findViewById(R.id.anylayler_fl_content_wrapper);
            mBackground = getChild().findViewById(R.id.anylayler_hv_background);
        }

        @NonNull
        @Override
        public FrameLayout getChild() {
            return (FrameLayout) super.getChild();
        }

        @Nullable
        @Override
        protected FrameLayout getChildNullable() {
            return (FrameLayout) super.getChildNullable();
        }

        @NonNull
        public FrameLayout getContentWrapper() {
            return mContentWrapper;
        }

        @NonNull
        public HoleView getBackground() {
            return mBackground;
        }
    }

    protected static class Config extends DecorLayer.Config {
        @ColorInt
        protected int mBackgroundColor = GlobalConfig.get().guideBackgroundInt;
        protected List<Mapping> mMapping = new ArrayList<>(1);
    }

    protected static class ListenerHolder extends DecorLayer.ListenerHolder {
    }

    public static class Mapping {
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

        private SparseArray<OnClickListener> mOnClickListeners = new SparseArray<>();

        private void bindOnClickListener(@NonNull GuideLayer layer) {
            if (mGuideView == null) return;
            for (int i = 0; i < mOnClickListeners.size(); i++) {
                int id = mOnClickListeners.keyAt(i);
                OnClickListener listener = mOnClickListeners.valueAt(i);
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

        public Mapping onClick(@NonNull OnClickListener listener, int... viewIds) {
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
        public Mapping targetRect(@NonNull Rect targetRect) {
            this.mTargetRect.set(targetRect);
            return this;
        }

        @NonNull
        public Mapping targetView(@Nullable View targetView) {
            this.mTargetView = targetView;
            return this;
        }

        @NonNull
        public Mapping guideView(@Nullable View guideView) {
            this.mGuideView = guideView;
            return this;
        }

        @NonNull
        public Mapping guideView(@LayoutRes int guideViewRes) {
            this.mGuideViewRes = guideViewRes;
            return this;
        }

        @NonNull
        public Mapping cornerRadius(float cornerRadius) {
            this.mCornerRadius = cornerRadius;
            return this;
        }

        @NonNull
        public Mapping offset(int offset) {
            this.mOffsetX = offset;
            this.mOffsetY = offset;
            return this;
        }

        @NonNull
        public Mapping offsetX(int offset) {
            this.mOffsetX = offset;
            return this;
        }

        @NonNull
        public Mapping offsetY(int offset) {
            this.mOffsetY = offset;
            return this;
        }

        @NonNull
        public Mapping padding(int padding) {
            this.mPaddingLeft = padding;
            this.mPaddingTop = padding;
            this.mPaddingRight = padding;
            this.mPaddingBottom = padding;
            return this;
        }

        @NonNull
        public Mapping paddingLeft(int padding) {
            this.mPaddingLeft = padding;
            return this;
        }

        @NonNull
        public Mapping paddingTop(int padding) {
            this.mPaddingTop = padding;
            return this;
        }

        @NonNull
        public Mapping paddingRight(int padding) {
            this.mPaddingRight = padding;
            return this;
        }

        @NonNull
        public Mapping paddingBottom(int padding) {
            this.mPaddingBottom = padding;
            return this;
        }

        @NonNull
        public Mapping margin(int margin) {
            this.mMarginLeft = margin;
            this.mMarginTop = margin;
            this.mMarginRight = margin;
            this.mMarginBottom = margin;
            return this;
        }

        @NonNull
        public Mapping marginLeft(int margin) {
            this.mMarginLeft = margin;
            return this;
        }

        @NonNull
        public Mapping marginTop(int margin) {
            this.mMarginTop = margin;
            return this;
        }

        @NonNull
        public Mapping marginRight(int margin) {
            this.mMarginRight = margin;
            return this;
        }

        @NonNull
        public Mapping marginBottom(int margin) {
            this.mMarginBottom = margin;
            return this;
        }

        @NonNull
        public Mapping horizontalAlign(@NonNull Align.Horizontal horizontalAlign) {
            mHorizontalAlign = horizontalAlign;
            return this;
        }

        @NonNull
        public Mapping verticalAlign(@NonNull Align.Vertical verticalAlign) {
            mVerticalAlign = verticalAlign;
            return this;
        }

        @NonNull
        public Rect getTargetRect() {
            if (mTargetRect.isEmpty()) {
                if (mTargetView != null) {
                    int[] location = new int[2];
                    mTargetView.getLocationInWindow(location);
                    mTargetRect.set(location[0], location[1],
                            location[0] + mTargetView.getWidth(),
                            location[1] + mTargetView.getHeight());
                }
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
            ALIGN_PARENT_TOP,
            ALIGN_PARENT_BOTTOM
        }
    }

}
