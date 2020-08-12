package per.goweii.anylayer;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class DragCompat {

    @Deprecated
    public static boolean canViewScrollUp(@Nullable View view, float x, float y, boolean defaultValueForNull) {
        if (view == null || !contains(view, x, y)) {
            return defaultValueForNull;
        }
        return view.canScrollVertically(-1);
    }

    public static boolean canViewScrollUp(@Nullable List<View> views, float x, float y, boolean defaultValueForNull) {
        if (views == null) {
            return defaultValueForNull;
        }
        List<View> contains = contains(views, x, y);
        if (contains == null) {
            return defaultValueForNull;
        }
        boolean canViewScroll = false;
        for (int i = contains.size() - 1; i >= 0; i--) {
            canViewScroll = ScrollCompat.canScrollVertically(contains.get(i), -1);
            if (canViewScroll) {
                break;
            }
        }
        return canViewScroll;
    }

    @Nullable
    public static View canScrollUpView(@Nullable List<View> views, float x, float y) {
        if (views == null) {
            return null;
        }
        List<View> contains = contains(views, x, y);
        if (contains == null) {
            return null;
        }
        for (int i = contains.size() - 1; i >= 0; i--) {
            View view = contains.get(i);
            if (ScrollCompat.canScrollVertically(view, -1)) {
                return view;
            }
        }
        return null;
    }

    @Deprecated
    public static boolean canViewScrollDown(@Nullable View view, float x, float y, boolean defaultValueForNull) {
        if (view == null || !contains(view, x, y)) {
            return defaultValueForNull;
        }
        return view.canScrollVertically(1);
    }

    public static boolean canViewScrollDown(@Nullable List<View> views, float x, float y, boolean defaultValueForNull) {
        if (views == null) {
            return defaultValueForNull;
        }
        List<View> contains = contains(views, x, y);
        if (contains == null) {
            return defaultValueForNull;
        }
        boolean canViewScroll = false;
        for (int i = contains.size() - 1; i >= 0; i--) {
            canViewScroll = ScrollCompat.canScrollVertically(contains.get(i), 1);
            if (canViewScroll) {
                break;
            }
        }
        return canViewScroll;
    }

    @Nullable
    public static View canScrollDownView(@Nullable List<View> views, float x, float y) {
        if (views == null) {
            return null;
        }
        List<View> contains = contains(views, x, y);
        if (contains == null) {
            return null;
        }
        for (int i = contains.size() - 1; i >= 0; i--) {
            View view = contains.get(i);
            if (ScrollCompat.canScrollVertically(view, 1)) {
                return view;
            }
        }
        return null;
    }

    @Deprecated
    public static boolean canViewScrollRight(@Nullable View view, float x, float y, boolean defaultValueForNull) {
        if (view == null || !contains(view, x, y)) {
            return defaultValueForNull;
        }
        return view.canScrollHorizontally(-1);
    }

    public static boolean canViewScrollRight(@Nullable List<View> views, float x, float y, boolean defaultValueForNull) {
        if (views == null) {
            return defaultValueForNull;
        }
        List<View> contains = contains(views, x, y);
        if (contains == null) {
            return defaultValueForNull;
        }
        boolean canViewScroll = false;
        for (int i = contains.size() - 1; i >= 0; i--) {
            canViewScroll = ScrollCompat.canScrollHorizontally(contains.get(i), 1);
            if (canViewScroll) {
                break;
            }
        }
        return canViewScroll;
    }

    @Nullable
    public static View canScrollRightView(@Nullable List<View> views, float x, float y) {
        if (views == null) {
            return null;
        }
        List<View> contains = contains(views, x, y);
        if (contains == null) {
            return null;
        }
        for (int i = contains.size() - 1; i >= 0; i--) {
            View view = contains.get(i);
            if (ScrollCompat.canScrollHorizontally(view, 1)) {
                return view;
            }
        }
        return null;
    }

    @Deprecated
    public static boolean canViewScrollLeft(@Nullable View view, float x, float y, boolean defaultValueForNull) {
        if (view == null || !contains(view, x, y)) {
            return defaultValueForNull;
        }
        return view.canScrollHorizontally(1);
    }

    public static boolean canViewScrollLeft(@Nullable List<View> views, float x, float y, boolean defaultValueForNull) {
        if (views == null) {
            return defaultValueForNull;
        }
        List<View> contains = contains(views, x, y);
        if (contains == null) {
            return defaultValueForNull;
        }
        boolean canViewScroll = false;
        for (int i = contains.size() - 1; i >= 0; i--) {
            canViewScroll = ScrollCompat.canScrollHorizontally(contains.get(i), -1);
            if (canViewScroll) {
                break;
            }
        }
        return canViewScroll;
    }

    @Nullable
    public static View canScrollLeftView(@Nullable List<View> views, float x, float y) {
        if (views == null) {
            return null;
        }
        List<View> contains = contains(views, x, y);
        if (contains == null) {
            return null;
        }
        for (int i = contains.size() - 1; i >= 0; i--) {
            View view = contains.get(i);
            if (ScrollCompat.canScrollHorizontally(view, -1)) {
                return view;
            }
        }
        return null;
    }

    public static List<View> findAllScrollViews(@NonNull ViewGroup viewGroup) {
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
                views.addAll(findAllScrollViews((ViewGroup) view));
            }
        }
        return views;
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

    @Nullable
    public static List<View> contains(@Nullable List<View> views, float x, float y) {
        if (views == null) {
            return null;
        }
        List<View> contains = new ArrayList<>(views.size());
        for (int i = views.size() - 1; i >= 0; i--) {
            View v = views.get(i);
            Rect localRect = new Rect();
            int[] l = new int[2];
            v.getLocationOnScreen(l);
            localRect.set(l[0], l[1], l[0] + v.getWidth(), l[1] + v.getHeight());
            if (localRect.contains((int) x, (int) y)) {
                contains.add(v);
            }
        }
        return contains;
    }
}
