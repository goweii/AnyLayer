package per.goweii.anylayer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.Stack;

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
    private final Stack<Activity> mActivityStack = new Stack<>();

    private ActivityHolder(Application application) {
        Utils.requireNonNull(application, "application == null");
        mApplication = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    static void init(Application application) {
        Utils.requireNonNull(application, "application == null");
        if (INSTANCE == null) {
            INSTANCE = new ActivityHolder(application);
        }
    }

    static Application getApplication() {
        if (INSTANCE == null) {
            return null;
        }
        return INSTANCE.mApplication;
    }

    static Activity getActivity(Class<Activity> clazz) {
        Utils.requireNonNull(clazz, "clazz == null");
        if (INSTANCE == null) {
            return null;
        }
        if (INSTANCE.mActivityStack.empty()) {
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

    static Activity getCurrentActivity() {
        if (INSTANCE == null) {
            return null;
        }
        if (INSTANCE.mActivityStack.empty()) {
            return null;
        }
        return INSTANCE.mActivityStack.peek();
    }

    // Application.ActivityLifecycleCallbacks

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityStack.push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityStack.pop();
    }
}
