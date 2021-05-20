package per.goweii.anylayer.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ScrollingView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class DragCompat {

    @Deprecated
    public static boolean canViewScrollUp(@NonNull View view, float x, float y, boolean defaultValueForNull) {
        if (!contains(view, x, y)) {
            return defaultValueForNull;
        }
        return ScrollCompat.canScrollUp(view);
    }

    public static boolean canViewScrollUp(@NonNull List<View> views, float x, float y, boolean defaultValueForNull) {
        List<View> contains = contains(views, x, y);
        return canViewScrollUp(contains, defaultValueForNull);
    }

    public static boolean canViewScrollUp(@NonNull List<View> views, boolean defaultValueForNull) {
        boolean canViewScroll = false;
        for (int i = views.size() - 1; i >= 0; i--) {
            canViewScroll = ScrollCompat.canScrollUp(views.get(i));
            if (canViewScroll) {
                break;
            }
        }
        return canViewScroll;
    }

    @Nullable
    public static View canScrollUpView(@NonNull List<View> views, float x, float y) {
        List<View> contains = contains(views, x, y);
        for (int i = contains.size() - 1; i >= 0; i--) {
            View view = contains.get(i);
            if (ScrollCompat.canScrollUp(view)) {
                return view;
            }
        }
        return null;
    }

    @Deprecated
    public static boolean canViewScrollDown(@NonNull View view, float x, float y, boolean defaultValueForNull) {
        if (!contains(view, x, y)) {
            return defaultValueForNull;
        }
        return ScrollCompat.canScrollDown(view);
    }

    public static boolean canViewScrollDown(@NonNull List<View> views, float x, float y, boolean defaultValueForNull) {
        List<View> contains = contains(views, x, y);
        return canViewScrollDown(contains, defaultValueForNull);
    }

    public static boolean canViewScrollDown(@NonNull List<View> views, boolean defaultValueForNull) {
        boolean canViewScroll = false;
        for (int i = views.size() - 1; i >= 0; i--) {
            canViewScroll = ScrollCompat.canScrollDown(views.get(i));
            if (canViewScroll) {
                break;
            }
        }
        return canViewScroll;
    }

    @Nullable
    public static View canScrollDownView(@NonNull List<View> views, float x, float y) {
        List<View> contains = contains(views, x, y);
        for (int i = contains.size() - 1; i >= 0; i--) {
            View view = contains.get(i);
            if (ScrollCompat.canScrollDown(view)) {
                return view;
            }
        }
        return null;
    }

    @Deprecated
    public static boolean canViewScrollRight(@NonNull View view, float x, float y, boolean defaultValueForNull) {
        if (!contains(view, x, y)) {
            return defaultValueForNull;
        }
        return ScrollCompat.canScrollRight(view);
    }

    public static boolean canViewScrollRight(@NonNull List<View> views, float x, float y, boolean defaultValueForNull) {
        List<View> contains = contains(views, x, y);
        return canViewScrollRight(contains, defaultValueForNull);
    }

    public static boolean canViewScrollRight(@NonNull List<View> views, boolean defaultValueForNull) {
        boolean canViewScroll = false;
        for (int i = views.size() - 1; i >= 0; i--) {
            canViewScroll = ScrollCompat.canScrollRight(views.get(i));
            if (canViewScroll) {
                break;
            }
        }
        return canViewScroll;
    }

    @Nullable
    public static View canScrollRightView(@NonNull List<View> views, float x, float y) {
        List<View> contains = contains(views, x, y);
        for (int i = contains.size() - 1; i >= 0; i--) {
            View view = contains.get(i);
            if (ScrollCompat.canScrollRight(view)) {
                return view;
            }
        }
        return null;
    }

    @Deprecated
    public static boolean canViewScrollLeft(@NonNull View view, float x, float y, boolean defaultValueForNull) {
        if (!contains(view, x, y)) {
            return defaultValueForNull;
        }
        return ScrollCompat.canScrollLeft(view);
    }

    public static boolean canViewScrollLeft(@NonNull List<View> views, float x, float y, boolean defaultValueForNull) {
        List<View> contains = contains(views, x, y);
        return canViewScrollLeft(contains, defaultValueForNull);
    }

    public static boolean canViewScrollLeft(@NonNull List<View> views, boolean defaultValueForNull) {
        boolean canViewScroll = false;
        for (int i = views.size() - 1; i >= 0; i--) {
            canViewScroll = ScrollCompat.canScrollLeft(views.get(i));
            if (canViewScroll) {
                break;
            }
        }
        return canViewScroll;
    }

    @Nullable
    public static View canScrollLeftView(@NonNull List<View> views, float x, float y) {
        List<View> contains = contains(views, x, y);
        for (int i = contains.size() - 1; i >= 0; i--) {
            View view = contains.get(i);
            if (ScrollCompat.canScrollLeft(view)) {
                return view;
            }
        }
        return null;
    }

    @NonNull
    public static List<View> findAllScrollableView(@NonNull ViewGroup viewGroup) {
        List<View> views = new ArrayList<>();
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view.getVisibility() != View.VISIBLE) {
                continue;
            }
            if (isScrollableView(view)) {
                views.add(view);
            }
            if (view instanceof ViewGroup) {
                views.addAll(findAllScrollableView((ViewGroup) view));
            }
        }
        return views;
    }

    public static void findAllScrollableView(@NonNull ViewGroup viewGroup, @NonNull List<View> views) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            if (view.getVisibility() != View.VISIBLE) {
                continue;
            }
            if (isScrollableView(view)) {
                views.add(view);
            }
            if (view instanceof ViewGroup) {
                findAllScrollableView((ViewGroup) view, views);
            }
        }
    }

    public static boolean isScrollableView(@NonNull View view) {
        return view instanceof ScrollView
                || view instanceof HorizontalScrollView
                || view instanceof AbsListView
                || view instanceof ViewPager
                || view instanceof WebView
                || view instanceof ScrollingView
                ;
    }

    public static boolean contains(@NonNull View view, float x, float y) {
        Rect localRect = new Rect();
        view.getGlobalVisibleRect(localRect);
        return localRect.contains((int) x, (int) y);
    }

    @NonNull
    public static List<View> contains(@NonNull List<View> views, float x, float y) {
        List<View> contains = new ArrayList<>(views.size());
        for (int i = views.size() - 1; i >= 0; i--) {
            View v = views.get(i);
            Rect localRect = new Rect();
            int[] l = new int[2];
            v.getLocationInWindow(l);
            localRect.set(l[0], l[1], l[0] + v.getWidth(), l[1] + v.getHeight());
            if (localRect.contains((int) x, (int) y)) {
                contains.add(v);
            }
        }
        return contains;
    }
}
