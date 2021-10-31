package per.goweii.anylayer.effect;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PopupShadowLayout extends ShadowLayout {
    private final PopupShadowOutlineProvider mPopupShadowOutlineProvider;

    public PopupShadowLayout(@NonNull Context context) {
        this(context, null);
    }

    public PopupShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupShadowLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPopupShadowOutlineProvider = new PopupShadowOutlineProvider();
        setShadowOutlineProvider(mPopupShadowOutlineProvider);
        setClipToShadowOutline(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PopupShadowLayout);
        int cornerRadius = typedArray.getDimensionPixelSize(R.styleable.PopupShadowLayout_popupCornerRadius, 0);
        mPopupShadowOutlineProvider.setCornerRadius(cornerRadius);
        int arrowAlign = typedArray.getInt(R.styleable.PopupShadowLayout_popupArrowAlign, PopupShadowOutlineProvider.ARROW_ALIGN_CENTER);
        mPopupShadowOutlineProvider.setArrowAlign(arrowAlign);
        int arrowOffset = typedArray.getDimensionPixelSize(R.styleable.PopupShadowLayout_popupArrowOffset, 0);
        mPopupShadowOutlineProvider.setArrowOffset(arrowOffset);
        int arrowSide = typedArray.getInt(R.styleable.PopupShadowLayout_popupArrowSide, PopupShadowOutlineProvider.ARROW_SIDE_NONE);
        mPopupShadowOutlineProvider.setArrowSide(arrowSide);
        int arrowWidth = typedArray.getDimensionPixelOffset(R.styleable.PopupShadowLayout_popupArrowWidth, 0);
        mPopupShadowOutlineProvider.setArrowWidth(arrowWidth);
        int arrowHeight = typedArray.getDimensionPixelOffset(R.styleable.PopupShadowLayout_popupArrowHeight, 0);
        mPopupShadowOutlineProvider.setArrowHeight(arrowHeight);
        int arrowRadius = typedArray.getDimensionPixelOffset(R.styleable.PopupShadowLayout_popupArrowRadius, 0);
        mPopupShadowOutlineProvider.setArrowRadius(arrowRadius);
        typedArray.recycle();
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return mPopupShadowOutlineProvider.getMinimumWidth() + super.getSuggestedMinimumWidth();
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return mPopupShadowOutlineProvider.getMinimumHeight() + super.getSuggestedMinimumHeight();
    }

    @Override
    protected void onShadowInsetsChanged(@NonNull RectF shadowInsets) {
        super.onShadowInsetsChanged(shadowInsets);
        Rect arrowInsets = mPopupShadowOutlineProvider.calcArrowInsets();
        shadowInsets.left = Math.max(shadowInsets.left, arrowInsets.left);
        shadowInsets.top = Math.max(shadowInsets.top, arrowInsets.top);
        shadowInsets.right = Math.max(shadowInsets.right, arrowInsets.right);
        shadowInsets.bottom = Math.max(shadowInsets.bottom, arrowInsets.bottom);
    }

    public void setArrowSide(@PopupShadowOutlineProvider.ArrowSide int arrowSide) {
        mPopupShadowOutlineProvider.setArrowSide(arrowSide);
    }

    public void setArrowOffset(int arrowOffset) {
        mPopupShadowOutlineProvider.setArrowOffset(arrowOffset);
    }

    public void setArrowRadius(int arrowRadius) {
        mPopupShadowOutlineProvider.setArrowRadius(arrowRadius);
    }

    public void setCornerRadius(int cornerRadius) {
        mPopupShadowOutlineProvider.setCornerRadius(cornerRadius);
    }

    public void setArrowWidth(int arrowWidth) {
        mPopupShadowOutlineProvider.setArrowWidth(arrowWidth);
    }

    public void setArrowHeight(int arrowHeight) {
        mPopupShadowOutlineProvider.setArrowHeight(arrowHeight);
    }

    public float calcRealHalfArrowWidth() {
        return mPopupShadowOutlineProvider.calcRealHalfArrowWidth();
    }

    public double calcVertexDegrees() {
        return mPopupShadowOutlineProvider.calcVertexDegrees();
    }

    public float getHalfArrowWidth() {
        return mPopupShadowOutlineProvider.getHalfArrowWidth();
    }

    public float getRealArrowOffset() {
        return mPopupShadowOutlineProvider.getRealArrowOffset(this, getShadowInsets());
    }

    public static class PopupShadowOutlineProvider extends ShadowLayout.ShadowOutlineProvider {
        public static final int ARROW_ALIGN_CENTER = 0;
        public static final int ARROW_ALIGN_START = 1;
        public static final int ARROW_ALIGN_END = 2;

        public static final int ARROW_SIDE_NONE = 0;
        public static final int ARROW_SIDE_TOP = 1;
        public static final int ARROW_SIDE_LEFT = 2;
        public static final int ARROW_SIDE_RIGHT = 3;
        public static final int ARROW_SIDE_BOTTOM = 4;

        @ArrowSide
        private int mArrowSide = ARROW_SIDE_NONE;
        private int mArrowAlign = ARROW_ALIGN_CENTER;
        private int mArrowOffset = 0;
        private int mArrowRadius = 0;
        private int mArrowWidth = 0;
        private int mArrowHeight = 0;
        private int mCornerRadius = 0;

        private final Rect mArrowInset = new Rect();

        @Override
        public void buildShadowOutline(@NonNull ShadowLayout shadowLayout,
                                       @NonNull Path shadowOutline,
                                       @NonNull RectF shadowInsets) {
            switch (mArrowSide) {
                case ARROW_SIDE_LEFT:
                    buildLeftArrow(shadowLayout, shadowOutline, shadowInsets);
                    break;
                case ARROW_SIDE_TOP:
                    buildTopArrow(shadowLayout, shadowOutline, shadowInsets);
                    break;
                case ARROW_SIDE_RIGHT:
                    buildRightArrow(shadowLayout, shadowOutline, shadowInsets);
                    break;
                case ARROW_SIDE_BOTTOM:
                    buildBottomArrow(shadowLayout, shadowOutline, shadowInsets);
                    break;
                default:
                    buildNoneArrow(shadowLayout, shadowOutline, shadowInsets);
                    break;
            }
        }

        public int getMinimumWidth() {
            int minSize;
            switch (mArrowSide) {
                case ARROW_SIDE_LEFT:
                case ARROW_SIDE_RIGHT:
                    minSize = mCornerRadius * 2 + mArrowHeight;
                    break;
                case ARROW_SIDE_TOP:
                case ARROW_SIDE_BOTTOM:
                    minSize = (int) (mCornerRadius * 2 + calcRealHalfArrowWidth() * 2);
                    break;
                default:
                    minSize = mCornerRadius * 2;
                    break;
            }
            return minSize;
        }

        public int getMinimumHeight() {
            int minSize;
            switch (mArrowSide) {
                case ARROW_SIDE_LEFT:
                case ARROW_SIDE_RIGHT:
                    minSize = (int) (mCornerRadius * 2 + calcRealHalfArrowWidth() * 2);
                    break;
                case ARROW_SIDE_TOP:
                case ARROW_SIDE_BOTTOM:
                    minSize = mCornerRadius * 2 + mArrowHeight;
                    break;
                default:
                    minSize = mCornerRadius * 2;
                    break;
            }
            return minSize;
        }

        public void setArrowSide(@ArrowSide int arrowSide) {
            if (mArrowSide != arrowSide) {
                mArrowSide = arrowSide;
                invalidateShadowOutline();
            }
        }

        public void setArrowAlign(int arrowAlign) {
            if (mArrowAlign != arrowAlign) {
                mArrowAlign = arrowAlign;
                invalidateShadowOutline();
            }
        }

        public void setArrowOffset(int arrowOffset) {
            if (mArrowOffset != arrowOffset) {
                mArrowOffset = arrowOffset;
                invalidateShadowOutline();
            }
        }

        public void setArrowRadius(int arrowRadius) {
            if (mArrowRadius != arrowRadius) {
                mArrowRadius = arrowRadius;
                invalidateShadowOutline();
            }
        }

        public void setCornerRadius(int cornerRadius) {
            if (mCornerRadius != cornerRadius) {
                mCornerRadius = cornerRadius;
                invalidateShadowOutline();
            }
        }

        public void setArrowWidth(int arrowWidth) {
            if (mArrowWidth != arrowWidth) {
                mArrowWidth = arrowWidth;
                invalidateShadowOutline();
            }
        }

        public void setArrowHeight(int arrowHeight) {
            if (mArrowHeight != arrowHeight) {
                mArrowHeight = arrowHeight;
                invalidateShadowOutline();
            }
        }

        @SuppressWarnings("SuspiciousNameCombination")
        @NonNull
        private Rect calcArrowInsets() {
            mArrowInset.setEmpty();
            switch (mArrowSide) {
                case ARROW_SIDE_TOP:
                    mArrowInset.top = mArrowHeight;
                    break;
                case ARROW_SIDE_LEFT:
                    mArrowInset.left = mArrowHeight;
                    break;
                case ARROW_SIDE_RIGHT:
                    mArrowInset.right = mArrowHeight;
                    break;
                case ARROW_SIDE_BOTTOM:
                    mArrowInset.bottom = mArrowHeight;
                    break;
                default:
                    break;
            }
            return mArrowInset;
        }

        private void buildNoneArrow(@NonNull ShadowLayout shadowLayout,
                                    @NonNull Path shadowOutline,
                                    @NonNull RectF shadowInsets) {
            moveToTopLeft(shadowLayout, shadowOutline, shadowInsets);
            addTopLeftCorner(shadowLayout, shadowOutline, shadowInsets);
            lineToTopRight(shadowLayout, shadowOutline, shadowInsets);
            addTopRightCorner(shadowLayout, shadowOutline, shadowInsets);
            lineToBottomRight(shadowLayout, shadowOutline, shadowInsets);
            addBottomRightCorner(shadowLayout, shadowOutline, shadowInsets);
            lineToBottomLeft(shadowLayout, shadowOutline, shadowInsets);
            addBottomLeftCorner(shadowLayout, shadowOutline, shadowInsets);
            shadowOutline.close();
        }

        private void buildLeftArrow(@NonNull ShadowLayout shadowLayout,
                                    @NonNull Path shadowOutline,
                                    @NonNull RectF shadowInsets) {
            final float arrowRadius = mArrowRadius;
            final float arrowHeight = mArrowHeight;
            final float halfArrowWidth = getHalfArrowWidth();
            final float realArrowOffset = getRealArrowOffset(shadowLayout, shadowInsets);
            final float realHalfArrowWidth = calcRealHalfArrowWidth();
            final double vertexDegrees = calcVertexDegrees();

            final float a1 = (float) (arrowRadius * Math.sin(Math.toRadians(vertexDegrees)));
            final float b1 = (float) (arrowRadius * Math.cos(Math.toRadians(vertexDegrees)));
            final float a2 = b1;
            final float b2 = arrowHeight * a2 / halfArrowWidth;

            shadowOutline.moveTo(
                    shadowInsets.left,
                    realArrowOffset + realHalfArrowWidth
            );
            shadowOutline.quadTo(
                    shadowInsets.left,
                    realArrowOffset + halfArrowWidth,
                    shadowInsets.left - arrowRadius + a1,
                    realArrowOffset + realHalfArrowWidth - b1
            );
            shadowOutline.lineTo(
                    b2 + (shadowInsets.left - mArrowHeight),
                    realArrowOffset + a2
            );
            shadowOutline.quadTo(
                    (shadowInsets.left - mArrowHeight),
                    realArrowOffset,
                    b2 + (shadowInsets.left - mArrowHeight),
                    realArrowOffset - a2
            );
            shadowOutline.lineTo(
                    shadowInsets.left - arrowRadius + a1,
                    realArrowOffset - realHalfArrowWidth + b1
            );
            shadowOutline.quadTo(
                    shadowInsets.left,
                    realArrowOffset - halfArrowWidth,
                    shadowInsets.left,
                    realArrowOffset - realHalfArrowWidth
            );

            lineToTopLeft(shadowLayout, shadowOutline, shadowInsets);
            addTopLeftCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToTopRight(shadowLayout, shadowOutline, shadowInsets);
            addTopRightCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToBottomRight(shadowLayout, shadowOutline, shadowInsets);
            addBottomRightCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToBottomLeft(shadowLayout, shadowOutline, shadowInsets);
            addBottomLeftCorner(shadowLayout, shadowOutline, shadowInsets);

            shadowOutline.close();
        }

        private void buildTopArrow(@NonNull ShadowLayout shadowLayout,
                                   @NonNull Path shadowOutline,
                                   @NonNull RectF shadowInsets) {
            final float arrowRadius = mArrowRadius;
            final float arrowHeight = mArrowHeight;
            final float halfArrowWidth = getHalfArrowWidth();
            final float realArrowOffset = getRealArrowOffset(shadowLayout, shadowInsets);
            final float realHalfArrowWidth = calcRealHalfArrowWidth();
            final double vertexDegrees = calcVertexDegrees();

            final float a1 = (float) (arrowRadius * Math.sin(Math.toRadians(vertexDegrees)));
            final float b1 = (float) (arrowRadius * Math.cos(Math.toRadians(vertexDegrees)));
            final float a2 = b1;
            final float b2 = arrowHeight * a2 / halfArrowWidth;

            shadowOutline.moveTo(
                    realArrowOffset - realHalfArrowWidth,
                    shadowInsets.top
            );
            shadowOutline.quadTo(
                    realArrowOffset - halfArrowWidth,
                    shadowInsets.top,
                    realArrowOffset - realHalfArrowWidth + b1,
                    shadowInsets.top - arrowRadius + a1
            );
            shadowOutline.lineTo(
                    realArrowOffset - a2,
                    b2 + (shadowInsets.top - mArrowHeight)
            );
            shadowOutline.quadTo(
                    realArrowOffset,
                    (shadowInsets.top - mArrowHeight),
                    realArrowOffset + a2,
                    b2 + (shadowInsets.top - mArrowHeight)
            );
            shadowOutline.lineTo(
                    realArrowOffset + realHalfArrowWidth - b1,
                    shadowInsets.top - arrowRadius + a1
            );
            shadowOutline.quadTo(
                    realArrowOffset + halfArrowWidth,
                    shadowInsets.top,
                    realArrowOffset + realHalfArrowWidth,
                    shadowInsets.top
            );

            lineToTopRight(shadowLayout, shadowOutline, shadowInsets);
            addTopRightCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToBottomRight(shadowLayout, shadowOutline, shadowInsets);
            addBottomRightCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToBottomLeft(shadowLayout, shadowOutline, shadowInsets);
            addBottomLeftCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToTopLeft(shadowLayout, shadowOutline, shadowInsets);
            addTopLeftCorner(shadowLayout, shadowOutline, shadowInsets);

            shadowOutline.close();
        }

        private void buildRightArrow(@NonNull ShadowLayout shadowLayout,
                                     @NonNull Path shadowOutline,
                                     @NonNull RectF shadowInsets) {
            final float arrowRadius = mArrowRadius;
            final float arrowHeight = mArrowHeight;
            final float halfArrowWidth = getHalfArrowWidth();
            final float realArrowOffset = getRealArrowOffset(shadowLayout, shadowInsets);
            final float realHalfArrowWidth = calcRealHalfArrowWidth();
            final double vertexDegrees = calcVertexDegrees();

            final float a1 = (float) (arrowRadius * Math.sin(Math.toRadians(vertexDegrees)));
            final float b1 = (float) (arrowRadius * Math.cos(Math.toRadians(vertexDegrees)));
            final float a2 = b1;
            final float b2 = arrowHeight * a2 / halfArrowWidth;

            shadowOutline.moveTo(
                    shadowLayout.getWidth() - shadowInsets.right,
                    realArrowOffset + realHalfArrowWidth
            );
            shadowOutline.quadTo(
                    shadowLayout.getWidth() - shadowInsets.right,
                    realArrowOffset + halfArrowWidth,
                    shadowLayout.getWidth() - shadowInsets.right + arrowRadius - a1,
                    realArrowOffset + realHalfArrowWidth - b1
            );
            shadowOutline.lineTo(
                    shadowLayout.getWidth() - b2 - (shadowInsets.right - mArrowHeight),
                    realArrowOffset + a2
            );
            shadowOutline.quadTo(
                    shadowLayout.getWidth() - (shadowInsets.right - mArrowHeight),
                    realArrowOffset,
                    shadowLayout.getWidth() - b2 - (shadowInsets.right - mArrowHeight),
                    realArrowOffset - a2
            );
            shadowOutline.lineTo(
                    shadowLayout.getWidth() - shadowInsets.right + arrowRadius - a1,
                    realArrowOffset - realHalfArrowWidth + b1
            );
            shadowOutline.quadTo(
                    shadowLayout.getWidth() - shadowInsets.right,
                    realArrowOffset - halfArrowWidth,
                    shadowLayout.getWidth() - shadowInsets.right,
                    realArrowOffset - realHalfArrowWidth
            );

            lineToBottomRight(shadowLayout, shadowOutline, shadowInsets);
            addBottomRightCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToBottomLeft(shadowLayout, shadowOutline, shadowInsets);
            addBottomLeftCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToTopLeft(shadowLayout, shadowOutline, shadowInsets);
            addTopLeftCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToTopRight(shadowLayout, shadowOutline, shadowInsets);
            addTopRightCorner(shadowLayout, shadowOutline, shadowInsets);

            shadowOutline.close();
        }

        private void buildBottomArrow(@NonNull ShadowLayout shadowLayout,
                                      @NonNull Path shadowOutline,
                                      @NonNull RectF shadowInsets) {
            final float arrowRadius = mArrowRadius;
            final float arrowHeight = mArrowHeight;
            final float halfArrowWidth = getHalfArrowWidth();
            final float realArrowOffset = getRealArrowOffset(shadowLayout, shadowInsets);
            final float realHalfArrowWidth = calcRealHalfArrowWidth();
            final double vertexDegrees = calcVertexDegrees();

            final float a1 = (float) (arrowRadius * Math.sin(Math.toRadians(vertexDegrees)));
            final float b1 = (float) (arrowRadius * Math.cos(Math.toRadians(vertexDegrees)));
            final float a2 = b1;
            final float b2 = arrowHeight * a2 / halfArrowWidth;

            shadowOutline.moveTo(
                    realArrowOffset - realHalfArrowWidth,
                    shadowLayout.getHeight() - shadowInsets.bottom
            );
            shadowOutline.quadTo(
                    realArrowOffset - halfArrowWidth,
                    shadowLayout.getHeight() - shadowInsets.bottom,
                    realArrowOffset - realHalfArrowWidth + b1,
                    shadowLayout.getHeight() - shadowInsets.bottom + arrowRadius - a1
            );
            shadowOutline.lineTo(
                    realArrowOffset - a2,
                    shadowLayout.getHeight() - b2 - (shadowInsets.bottom - mArrowHeight)
            );
            shadowOutline.quadTo(
                    realArrowOffset,
                    shadowLayout.getHeight() - (shadowInsets.bottom - mArrowHeight),
                    realArrowOffset + a2,
                    shadowLayout.getHeight() - b2 - (shadowInsets.bottom - mArrowHeight)
            );
            shadowOutline.lineTo(
                    realArrowOffset + realHalfArrowWidth - b1,
                    shadowLayout.getHeight() - shadowInsets.bottom + arrowRadius - a1
            );
            shadowOutline.quadTo(
                    realArrowOffset + halfArrowWidth,
                    shadowLayout.getHeight() - shadowInsets.bottom,
                    realArrowOffset + realHalfArrowWidth,
                    shadowLayout.getHeight() - shadowInsets.bottom
            );

            lineToBottomLeft(shadowLayout, shadowOutline, shadowInsets);
            addBottomLeftCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToTopLeft(shadowLayout, shadowOutline, shadowInsets);
            addTopLeftCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToTopRight(shadowLayout, shadowOutline, shadowInsets);
            addTopRightCorner(shadowLayout, shadowOutline, shadowInsets);

            lineToBottomRight(shadowLayout, shadowOutline, shadowInsets);
            addBottomRightCorner(shadowLayout, shadowOutline, shadowInsets);

            shadowOutline.close();
        }

        private void moveToTopLeft(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.moveTo(
                    shadowInsets.left,
                    shadowInsets.top + mCornerRadius
            );
        }

        private void lineToTopLeft(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.lineTo(
                    shadowInsets.left,
                    shadowInsets.top + mCornerRadius
            );
        }

        private void addTopLeftCorner(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.quadTo(
                    shadowInsets.left,
                    shadowInsets.top,
                    shadowInsets.left + mCornerRadius,
                    shadowInsets.top
            );
        }

        private void moveToTopRight(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.moveTo(
                    shadowLayout.getWidth() - shadowInsets.right - mCornerRadius,
                    shadowInsets.top
            );
        }

        private void lineToTopRight(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.lineTo(
                    shadowLayout.getWidth() - shadowInsets.right - mCornerRadius,
                    shadowInsets.top
            );
        }

        private void addTopRightCorner(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.quadTo(
                    shadowLayout.getWidth() - shadowInsets.right,
                    shadowInsets.top,
                    shadowLayout.getWidth() - shadowInsets.right,
                    shadowInsets.top + mCornerRadius
            );
        }

        private void moveToBottomRight(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.moveTo(
                    shadowLayout.getWidth() - shadowInsets.right,
                    shadowLayout.getHeight() - shadowInsets.bottom - mCornerRadius
            );
        }

        private void lineToBottomRight(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.lineTo(
                    shadowLayout.getWidth() - shadowInsets.right,
                    shadowLayout.getHeight() - shadowInsets.bottom - mCornerRadius
            );
        }

        private void addBottomRightCorner(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.quadTo(
                    shadowLayout.getWidth() - shadowInsets.right,
                    shadowLayout.getHeight() - shadowInsets.bottom,
                    shadowLayout.getWidth() - shadowInsets.right - mCornerRadius,
                    shadowLayout.getHeight() - shadowInsets.bottom
            );
        }

        private void moveToBottomLeft(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.moveTo(
                    shadowInsets.left + mCornerRadius,
                    shadowLayout.getHeight() - shadowInsets.bottom
            );
        }

        private void lineToBottomLeft(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.lineTo(
                    shadowInsets.left + mCornerRadius,
                    shadowLayout.getHeight() - shadowInsets.bottom
            );
        }

        private void addBottomLeftCorner(@NonNull ShadowLayout shadowLayout, @NonNull Path shadowOutline, @NonNull RectF shadowInsets) {
            shadowOutline.quadTo(
                    shadowInsets.left,
                    shadowLayout.getHeight() - shadowInsets.bottom,
                    shadowInsets.left,
                    shadowLayout.getHeight() - shadowInsets.bottom - mCornerRadius
            );
        }

        private float calcRealHalfArrowWidth() {
            double vertexDegrees = calcVertexDegrees();
            double d = (90.0 - vertexDegrees) / 2.0;
            float increase = (float) (Math.tan(Math.toRadians(d)) * mArrowRadius);
            return getHalfArrowWidth() + increase;
        }

        private double calcVertexDegrees() {
            if (mArrowHeight <= 0) return 180.0;
            if (mArrowWidth <= 0) return 0.0;
            double tan = (mArrowWidth / 2.0) / mArrowHeight;
            double d = Math.atan(tan);
            return Math.toDegrees(d);
        }

        private float getHalfArrowWidth() {
            return mArrowWidth / 2F;
        }

        public float getRealArrowOffset(@NonNull ShadowLayout shadowLayout,
                                        @NonNull RectF shadowInsets) {
            final float minArrowPadding = mCornerRadius + calcRealHalfArrowWidth();
            final float minOffset;
            final float maxOffset;
            float realOffset;
            switch (mArrowSide) {
                case ARROW_SIDE_LEFT:
                case ARROW_SIDE_RIGHT:
                    minOffset = shadowInsets.top + minArrowPadding;
                    maxOffset = shadowLayout.getHeight() - shadowInsets.bottom - minArrowPadding;
                    switch (mArrowAlign) {
                        case ARROW_ALIGN_CENTER:
                            realOffset = (minOffset + maxOffset) / 2F;
                            break;
                        case ARROW_ALIGN_START:
                            realOffset = minOffset + mArrowOffset;
                            break;
                        case ARROW_ALIGN_END:
                            realOffset = maxOffset - mArrowOffset;
                            break;
                        default:
                            realOffset = 0;
                            break;
                    }
                    realOffset = Math.max(realOffset, minOffset);
                    realOffset = Math.min(realOffset, maxOffset);
                    break;
                case ARROW_SIDE_TOP:
                case ARROW_SIDE_BOTTOM:
                    minOffset = shadowInsets.left + minArrowPadding;
                    maxOffset = shadowLayout.getWidth() - shadowInsets.right - minArrowPadding;
                    switch (mArrowAlign) {
                        case ARROW_ALIGN_CENTER:
                            realOffset = (minOffset + maxOffset) / 2F;
                            break;
                        case ARROW_ALIGN_START:
                            realOffset = minOffset + mArrowOffset;
                            break;
                        case ARROW_ALIGN_END:
                            realOffset = maxOffset - mArrowOffset;
                            break;
                        default:
                            realOffset = 0;
                            break;
                    }
                    realOffset = Math.max(realOffset, minOffset);
                    realOffset = Math.min(realOffset, maxOffset);
                    break;
                default:
                    realOffset = 0;
                    break;
            }
            return realOffset;
        }

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({ARROW_SIDE_TOP, ARROW_SIDE_LEFT, ARROW_SIDE_RIGHT, ARROW_SIDE_BOTTOM})
        public @interface ArrowSide {
        }
    }
}
