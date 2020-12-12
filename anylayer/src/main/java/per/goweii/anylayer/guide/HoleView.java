package per.goweii.anylayer.guide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;

public class HoleView extends View {
    private final Paint mPaint;
    private final Path mTempPath = new Path();
    private final Path mHolePath = new Path();
    @ColorInt
    private int mOuterColor = ColorUtils.setAlphaComponent(Color.BLACK, (int) (255 * 0.5));

    public HoleView(Context context) {
        this(context, null);
    }

    public HoleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HoleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void setOuterColor(@ColorInt int colorInt) {
        mOuterColor = colorInt;
    }

    public void clear() {
        mTempPath.reset();
        mTempPath.rewind();
    }

    public void addCircle(float centerX, float centerY, float radius) {
        mTempPath.addCircle(centerX, centerY, radius, Path.Direction.CW);
    }

    public void addRect(float left, float top, float right, float bottom, float radii) {
        addRect(new RectF(left, top, right, bottom), radii);
    }

    public void addRect(@NonNull Rect rect, float radii) {
        addRect(new RectF(rect), radii);
    }

    public void addRect(@NonNull RectF rectF, float radii) {
        mTempPath.addRoundRect(
                rectF,
                new float[]{radii, radii, radii, radii, radii, radii, radii, radii},
                Path.Direction.CW
        );
    }

    public void addPath(@NonNull Path path) {
        mTempPath.addPath(path);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        mPaint.setColor(mOuterColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mHolePath.reset();
            mHolePath.rewind();
            mHolePath.addRect(0F, 0F, getWidth(), getHeight(), Path.Direction.CW);
            mHolePath.op(mHolePath, mTempPath, Path.Op.DIFFERENCE);
            canvas.drawPath(mHolePath, mPaint);
        } else {
            canvas.clipPath(mTempPath, Region.Op.DIFFERENCE);
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        }
        canvas.restore();
    }
}
