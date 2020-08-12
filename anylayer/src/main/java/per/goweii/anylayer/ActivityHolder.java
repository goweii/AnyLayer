package per.goweii.anylayer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * @author CuiZhen
 * @date 2019/3/10
 * QQ: 302833254
 * E-mail: goweii@163.com
 * GitHub: https://github.com/goweii
 */
final class ActivityHolder implements Application.ActivityLifecycleCallbacks {

    private static ActivityHolder INSTANCE = null;

    private final Application mApplication;
    private final List<Activity> mActivityStack = new LinkedList<>();

    private ActivityHolder(@NonNull Application application) {
        mApplication = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    static void init(@NonNull Application application) {
        if (INSTANCE == null) {
            INSTANCE = new ActivityHolder(application);
        }
    }

    @Nullable
    static Application getApplication() {
        if (INSTANCE == null) {
            return null;
        }
        return INSTANCE.mApplication;
    }

    @Nullable
    static Activity getActivity(@NonNull Class<Activity> clazz) {
        if (INSTANCE == null) {
            return null;
        }
        if (INSTANCE.mActivityStack.isEmpty()) {
            return null;
        }
        final int size = INSTANCE.mActivityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity activity = INSTANCE.mActivityStack.get(i);
            if (TextUtils.equals(clazz.getName(), activity.getClass().getName())) {
                return activity;
            }
        }
        return null;
    }

    @Nullable
    static Activity getCurrentActivity() {
        if (INSTANCE == null) {
            return null;
        }
        if (INSTANCE.mActivityStack.isEmpty()) {
            return null;
        }
        return INSTANCE.mActivityStack.get(INSTANCE.mActivityStack.size() - 1);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        mActivityStack.add(activity);
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
        mActivityStack.remove(activity);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }
}
