package com.garmin.gncs.sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    public static final boolean IS_TEST = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    private static final int NOTIFICATION_ID = 1;

    public void onClick(View view) {
        isNotificationListenerEnabled();

        sendNotification();
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        long time = System.currentTimeMillis();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setVibrate(new long[]{0, 1000, 500, 1000})
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setSound(null)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_SECRET)
                .setContentTitle("aaa")
                .setContentText("Content Text " + time);
        Notification notification = builder.build();
        notification.flags = 0;

        notificationManager.notify(NOTIFICATION_ID, notification);


//        notificationManager.cancel(NOTIFICATION_ID);
    }

    private void isNotificationListenerEnabled() {
        ComponentName componentName = new ComponentName("com.garmin.android.apps.connectmobile", "com.garmin.android.gncs.GNCSListenerService");
        String flat = Settings.Secure.getString(ApplicationComp.context.getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(componentName.flattenToString());
        Log.v(TAG, "isNotificationListenerEnabled: enabled = " + enabled);
    }
}
