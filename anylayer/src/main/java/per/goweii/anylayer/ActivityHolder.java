package per.goweii.anylayer;

import android.app.Activity;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    private ActivityHolder(@NonNull Application application) {
        mApplication = application;
        application.registerActivityLifecycleCallbacks(this);
    }

    static void init(@NonNull Application application) {
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

    @Nullable
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
