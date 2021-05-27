package per.goweii.anylayer.startup;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import java.util.Collections;
import java.util.List;

import per.goweii.anylayer.ActivityHolder;

public class AnyLayerInitializer implements Initializer<ActivityHolder> {
    @NonNull
    @Override
    public ActivityHolder create(@NonNull Context context) {
        ActivityHolder.init((Application) context.getApplicationContext());
        return ActivityHolder.getInstance();
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
