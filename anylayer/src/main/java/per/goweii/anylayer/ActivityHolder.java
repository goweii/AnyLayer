package per.goweii.anylayer;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

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

    private ActivityHolder(Application application) {
        mApplication = Utils.requireNonNull(application, "application == null");
        application.registerActivityLifecycleCallbacks(this);
    }

    static void init(Application application) {
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
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityStack.add(activity);
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
    public void onActivityDestroyed(Activity activity) {
        mActivityStack.remove(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }
}
