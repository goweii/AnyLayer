package per.goweii.anylayer.ext;

import androidx.annotation.NonNull;

import per.goweii.anylayer.notification.NotificationLayer;

public class DefaultNotificationOnSwipeListener implements NotificationLayer.OnSwipeListener {
    @Override
    public void onStart(@NonNull NotificationLayer layer) {
    }

    @Override
    public void onSwiping(@NonNull NotificationLayer layer, int direction, float fraction) {
    }

    @Override
    public void onEnd(@NonNull NotificationLayer layer, int direction) {
    }
}
