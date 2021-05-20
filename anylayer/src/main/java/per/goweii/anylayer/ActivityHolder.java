package per.goweii.anylayer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import per.goweii.anylayer.utils.Utils;

public final class ActivityHolder implements Application.ActivityLifecycleCallbacks {
    private static ActivityHolder INSTANCE = null;

    @NonNull
    private final Application mApplication;
    private final List<WeakReference<Activity>> mActivityStack = new LinkedList<>();

    private ActivityHolder(@NonNull Application application) {
        mApplication = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    public static void init(@NonNull Application application) {
        if (INSTANCE == null) {
            INSTANCE = new ActivityHolder(application);
        }
    }

    @NonNull
    public static ActivityHolder getInstance() {
        return Utils.requireNonNull(INSTANCE, "需要先在Application中初始化");
    }

    @NonNull
    public static Application getApplication() {
        return getInstance().mApplication;
    }

    @NonNull
    public static Activity requireActivity(@NonNull Class<Activity> clazz) {
        Activity activity = ActivityHolder.getActivity(clazz);
        Utils.requireNonNull(activity, "请确保有已启动的Activity实例：" + clazz.getName());
        return activity;
    }

    @Nullable
    public static Activity getActivity(@NonNull Class<Activity> clazz) {
        final List<WeakReference<Activity>> stack = getInstance().mActivityStack;
        if (stack.isEmpty()) return null;
        final int size = stack.size();
        Activity find = null;
        for (int i = size - 1; i >= 0; i--) {
            WeakReference<Activity> ref = stack.get(i);
            final Activity activity = ref.get();
            if (activity == null) {
                ref.clear();
                stack.remove(i);
            } else {
                if (find == null) {
                    if (TextUtils.equals(clazz.getName(), activity.getClass().getName())) {
                        find = activity;
                    }
                }
            }
        }
        return find;
    }

    @NonNull
    public static Activity requireCurrentActivity() {
        Activity activity = ActivityHolder.getCurrentActivity();
        Utils.requireNonNull(activity, "请确保有已启动的Activity实例");
        return activity;
    }

    @Nullable
    public static Activity getCurrentActivity() {
        final List<WeakReference<Activity>> stack = getInstance().mActivityStack;
        if (stack.isEmpty()) return null;
        final int size = stack.size();
        for (int i = size - 1; i >= 0; i--) {
            WeakReference<Activity> ref = stack.get(i);
            if (ref.get() == null) {
                ref.clear();
                stack.remove(i);
            }
        }
        if (stack.isEmpty()) return null;
        return stack.get(stack.size() - 1).get();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        mActivityStack.add(new WeakReference<>(activity));
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        final int size = mActivityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            WeakReference<Activity> ref = mActivityStack.get(i);
            if (ref.get() == null || ref.get() == activity) {
                ref.clear();
                mActivityStack.remove(i);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }
}
