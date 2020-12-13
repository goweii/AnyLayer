package per.goweii.anylayer.utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.ScrollingView;

public class ScrollCompat {

    public static boolean canScrollUp(@NonNull View view) {
        return ScrollCompat.canScrollVertically(view, -1);
    }

    public static boolean canScrollDown(@NonNull View view) {
        return ScrollCompat.canScrollVertically(view, 1);
    }

    public static boolean canScrollLeft(@NonNull View view) {
        return ScrollCompat.canScrollHorizontally(view, -1);
    }

    public static boolean canScrollRight(@NonNull View view) {
        return ScrollCompat.canScrollHorizontally(view, 1);
    }

    public static boolean canScrollHorizontally(@NonNull View v, int direction) {
        if (v instanceof ScrollingView) {
            return canScrollingViewScrollHorizontally((ScrollingView) v, direction);
        } else {
            return v.canScrollHorizontally(direction);
        }
    }

    public static boolean canScrollVertically(@NonNull View v, int direction) {
        if (v instanceof ScrollingView) {
            return canScrollingViewScrollVertically((ScrollingView) v, direction);
        } else {
            return v.canScrollVertically(direction);
        }
    }

    private static boolean canScrollingViewScrollHorizontally(@NonNull ScrollingView view, int direction) {
        final int offset = view.computeHorizontalScrollOffset();
        final int range = view.computeHorizontalScrollRange() - view.computeHorizontalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }

    private static boolean canScrollingViewScrollVertically(@NonNull ScrollingView view, int direction) {
        final int offset = view.computeVerticalScrollOffset();
        final int range = view.computeVerticalScrollRange() - view.computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }
}
