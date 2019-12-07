package per.goweii.anylayer;

import android.graphics.Rect;
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
    public static boolean canViewScrollUp(View view, float x, float y, boolean defaultValueForNull) {
        if (view == null || !contains(view, x, y)) {
            return defaultValueForNull;
        }
        return view.canScrollVertically(-1);
    }

    public static boolean canViewScrollUp(List<View> views, float x, float y, boolean defaultValueForNull) {
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

    public static View canScrollUpView(List<View> views, float x, float y) {
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
    public static boolean canViewScrollDown(View view, float x, float y, boolean defaultValueForNull) {
        if (view == null || !contains(view, x, y)) {
            return defaultValueForNull;
        }
        return view.canScrollVertically(1);
    }

    public static boolean canViewScrollDown(List<View> views, float x, float y, boolean defaultValueForNull) {
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

    public static View canScrollDownView(List<View> views, float x, float y) {
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
    public static boolean canViewScrollRight(View view, float x, float y, boolean defaultValueForNull) {
        if (view == null || !contains(view, x, y)) {
            return defaultValueForNull;
        }
        return view.canScrollHorizontally(-1);
    }

    public static boolean canViewScrollRight(List<View> views, float x, float y, boolean defaultValueForNull) {
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

    public static View canScrollRightView(List<View> views, float x, float y) {
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
    public static boolean canViewScrollLeft(View view, float x, float y, boolean defaultValueForNull) {
        if (view == null || !contains(view, x, y)) {
            return defaultValueForNull;
        }
        return view.canScrollHorizontally(1);
    }

    public static boolean canViewScrollLeft(List<View> views, float x, float y, boolean defaultValueForNull) {
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

    public static View canScrollLeftView(List<View> views, float x, float y) {
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

    public static List<View> findAllScrollViews(ViewGroup viewGroup) {
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

    public static boolean isScrollableView(View view) {
        return view instanceof ScrollView
                || view instanceof HorizontalScrollView
                || view instanceof AbsListView
                || view instanceof WebView;
    }

    public static boolean contains(View view, float x, float y) {
        Rect localRect = new Rect();
        view.getGlobalVisibleRect(localRect);
        return localRect.contains((int) x, (int) y);
    }

    public static List<View> contains(List<View> views, float x, float y) {
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
