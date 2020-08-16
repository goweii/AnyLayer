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

    @NonNull
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

    @NonNull
    static ActivityHolder getInstance() {
        return Utils.requireNonNull(INSTANCE, "请先在Application中初始化");
    }

    @NonNull
    static Application getApplication() {
        return getInstance().mApplication;
    }

    @NonNull
    static Activity requireActivity(@NonNull Class<Activity> clazz) {
        Activity activity = ActivityHolder.getActivity(clazz);
        Utils.requireNonNull(activity, "请确保有已启动的Activity实例");
        return activity;
    }

    @Nullable
    static Activity getActivity(@NonNull Class<Activity> clazz) {
        if (getInstance().mActivityStack.isEmpty()) return null;
        final int size = getInstance().mActivityStack.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity activity = getInstance().mActivityStack.get(i);
            if (TextUtils.equals(clazz.getName(), activity.getClass().getName())) {
                return activity;
            }
        }
        return null;
    }

    @NonNull
    static Activity requireCurrentActivity() {
        Activity activity = ActivityHolder.getCurrentActivity();
        Utils.requireNonNull(activity, "请确保有已启动的Activity实例");
        return activity;
    }

    @Nullable
    static Activity getCurrentActivity() {
        if (getInstance().mActivityStack.isEmpty()) return null;
        return getInstance().mActivityStack.get(getInstance().mActivityStack.size() - 1);
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
