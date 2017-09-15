package com.garmin.gncs.sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;


/**
 * Created by cyoh on 8/31/17.
 */

public class NotificationListenerServiceComp extends NotificationListenerService {
    private static final String TAG = "NLS";

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.d(TAG, "onListenerConnected");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.d(TAG, "onNotificationPosted");

        String packageName = sbn.getPackageName();
        Log.v(TAG, packageName);

        if (packageName.equals("com.garmin.gncs.sms")) {
            Notification notification = sbn.getNotification();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(NOTIFICATION_ID);
            return;
        }

        if (!packageName.equals("com.kakao.talk")) {
            Log.v(TAG, "not com.kakao.talk");
            return;
        }

        String garminName = Util.getGarminName();
        if (!Util.isBluetoothConnected(garminName)) {
            Log.e(TAG, "isGarminConnected == false");
            return;
        }

        Notification notification = sbn.getNotification();
        Uri sound = notification.sound;
        long[] vibrate = notification.vibrate;
        if (sound == null || (vibrate == null || vibrate.length < 1)) {
            Log.v(TAG, "sound == null || (vibrate == null || vibrate.length < 1)");
            return;
        }

        Bundle extras = notification.extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        CharSequence summaryText = extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT);

        Log.v(TAG, "title = " + title + ", text = " + text + ", subText = " + subText + ", summaryText = " + summaryText);

        String nTitle;
        if (summaryText == null) {
            nTitle = ApplicationComp.context.getResources().getString(R.string.kakaotalk) + ":" + title;
        } else {
            nTitle = summaryText.toString() + ":" + title;
        }
        if (text != null) {
            String nText = text.toString();
            if (!nText.equals("(이모티콘)")) {
                showNotifiaction(nTitle, nText);
            }
        }


//        String garminName = "Edge 520";
//        if (!BluetoothUtil.isBluetoothConnected(garminName)) {
//            Log.e(TAG, "isGarminConnected == false");
//            return;
//        }
//
//        String message = getKakaoTalkMessage(sbn);
//        if (message != null && !message.isEmpty()) {
//            sendSmsToMyself(message);
//        } else {
//            Log.w(TAG, "message is null");
//        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
        Log.d(TAG, "onNotificationRemoved");
    }

    private static final int NOTIFICATION_ID = 1;

    private void showNotifiaction(String title, String text) {
        Log.d(TAG, "showNotifiaction: title = " + title + ", text = " + text);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        long time = System.currentTimeMillis();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setVibrate(new long[]{0, 1000, 500, 1000})
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setSound(null)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVisibility(Notification.VISIBILITY_SECRET)
                .setContentTitle(title)
                .setContentText(text);
        Notification notification = builder.build();
        notification.flags = 0;

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private String getKakaoTalkMessage(StatusBarNotification sbn) {
        String message = "";

        Notification notification = sbn.getNotification();
        Uri sound = notification.sound;
        long[] vibrate = notification.vibrate;
        if (sound == null || (vibrate == null || vibrate.length < 1)) {
            Log.v(TAG, "sound == null || (vibrate == null || vibrate.length < 1)");
            return null;
        }

        Bundle extras = notification.extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        CharSequence summaryText = extras.getCharSequence(Notification.EXTRA_SUMMARY_TEXT);

        if (summaryText != null && summaryText.length() > 0) {
            message += summaryText + ":";
        }
        if (title != null && title.length() > 0) {
            message += title + "\n";
        }
        if (text != null && text.length() > 0 && !text.equals("(이모티콘)")) {
            message += text;
        }

        Log.d(TAG, "message = " + message);
        return message;
    }

    private void sendSmsToMyself(String message) {
        Context context = getApplicationContext();

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String phoneNumber = telephonyManager.getLine1Number();
            Log.v(TAG, "phoneNumber = " + phoneNumber);

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
