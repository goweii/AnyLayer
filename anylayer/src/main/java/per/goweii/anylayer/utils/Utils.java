package per.goweii.anylayer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import per.goweii.anylayer.FrameLayer;
import per.goweii.anylayer.dialog.ContainerLayout;

public final class Utils {

    @NonNull
    public static <T> T requireNonNull(@Nullable T obj, String msg) {
        if (obj == null) {
            throw new NullPointerException(msg);
        }
        return obj;
    }

    @NonNull
    public static <T> T requireNonNull(@Nullable T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }

    public static float floatRange01(float value) {
        return floatRange(value, 0F, 1F);
    }

    public static float floatRange(float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static int intRange(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }

    public static int getStatusBarHeight(@NonNull Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 从当前上下文获取Activity
     */
    @NonNull
    public static Activity requireActivity(@NonNull Context context) {
        Activity activity = getActivity(context);
        requireNonNull(activity, "无法从Context获取Activity，请确保传入的不是ApplicationContext或Service等");
        return activity;
    }

    /**
     * 从当前上下文获取Activity
     */
    @Nullable
    public static Activity getActivity(@NonNull Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            Context baseContext = ((ContextWrapper) context).getBaseContext();
            if (baseContext instanceof Activity) {
                return (Activity) baseContext;
            }
        }
        return null;
    }

    @Nullable
    public static Bitmap snapshotSafely(@NonNull FrameLayout decorView,
                                        @NonNull ImageView inImageView,
                                        @FloatRange(from = 1F) float inSampleSize,
                                        @NonNull FrameLayer.LevelLayout currLevelLayout,
                                        @NonNull ContainerLayout currContainerLayout) {
        try {
            return snapshot(decorView, inImageView, inSampleSize, currLevelLayout, currContainerLayout);
        } catch (Throwable ignore) {
            return null;
        }
    }

    @NonNull
    public static Bitmap snapshot(@NonNull FrameLayout decorView,
                                  @NonNull ImageView inImageView,
                                  @FloatRange(from = 1F) float inSampleSize,
                                  @NonNull FrameLayer.LevelLayout currLevelLayout,
                                  @NonNull ContainerLayout currContainerLayout) {
        int w = inImageView.getWidth();
        int h = inImageView.getHeight();
        int oW = (int) (w / inSampleSize);
        int oH = (int) (h / inSampleSize);
        Bitmap bitmap = Bitmap.createBitmap(oW, oH, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        int[] locationRootView = new int[2];
        decorView.getLocationOnScreen(locationRootView);
        int[] locationBackground = new int[2];
        inImageView.getLocationOnScreen(locationBackground);
        int x = locationBackground[0] - locationRootView[0];
        int y = locationBackground[1] - locationRootView[1];
        canvas.scale(1 / inSampleSize, 1 / inSampleSize);
        canvas.translate(x / inSampleSize, y / inSampleSize);
        if (decorView.getBackground() != null) {
            decorView.getBackground().draw(canvas);
        }
        out:
        for (int i = 0; i < decorView.getChildCount(); i++) {
            View decorChildAt = decorView.getChildAt(i);
            if (decorChildAt instanceof FrameLayer.LayerLayout) {
                FrameLayer.LayerLayout layerLayout = (FrameLayer.LayerLayout) decorChildAt;
                for (int j = 0; j < layerLayout.getChildCount(); j++) {
                    View layerChildAt = layerLayout.getChildAt(j);
                    if (layerChildAt instanceof FrameLayer.LevelLayout) {
                        FrameLayer.LevelLayout levelLayout = (FrameLayer.LevelLayout) layerChildAt;
                        if (levelLayout == currLevelLayout) {
                            for (int k = 0; k < levelLayout.getChildCount(); k++) {
                                View containerLayout = levelLayout.getChildAt(k);
                                if (containerLayout == currContainerLayout) {
                                    break out;
                                } else {
                                    containerLayout.draw(canvas);
                                }
                            }
                            break out;
                        } else {
                            levelLayout.draw(canvas);
                        }
                    } else {
                        break out;
                    }
                }
                break;
            } else {
                decorChildAt.draw(canvas);
            }
        }
        canvas.restore();
        return bitmap;
    }

    public static void transparent(@NonNull Activity activity) {
        final Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static void onViewPreDraw(@NonNull final View view, @NonNull Runnable runnable) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (view.getViewTreeObserver().isAlive()) {
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }
                runnable.run();
                return true;
            }
        });
    }

    public static void onViewLayout(@NonNull final View view, @NonNull Runnable runnable) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (view.getViewTreeObserver().isAlive()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
                runnable.run();
            }
        });
    }

    public static void getViewSize(@NonNull final View view, @NonNull Runnable runnable) {
        onViewLayout(view, runnable);
    }

    public static int getViewMarginLeft(@NonNull View view) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return 0;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.leftMargin;
    }

    public static int getViewMarginRight(@NonNull View view) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return 0;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.rightMargin;
    }

    public static int getViewMarginTop(@NonNull View view) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return 0;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.topMargin;
    }

    public static int getViewMarginBottom(@NonNull View view) {
        if (!(view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) return 0;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        return params.bottomMargin;
    }

    public static void setViewPaddingLeft(@NonNull View view, int padding) {
        view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setViewPaddingTop(@NonNull View view, int padding) {
        view.setPadding(view.getPaddingLeft(), padding, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setViewPaddingRight(@NonNull View view, int padding) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding, view.getPaddingBottom());
    }

    public static void setViewPaddingBottom(@NonNull View view, int padding) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), padding);
    }
}
