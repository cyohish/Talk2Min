package com.garmin.gncs.sms;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private static final String TAG = "SplashActivity";

    private static final int SPLASH_DISPLAY_LENGTH = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (TestActivity.IS_TEST) {
            startActivity(new Intent(SplashActivity.this, TestActivity.class));
            this.finish();
        } else {
            boolean isGarminConnectInstalled = Util.isGarminConnectInstalled();
            if (!isGarminConnectInstalled) {
                showAlerDialog(R.string.title_dialog_garmin_connect_not_installed, R.string.message_dialog_garmin_connect_not_installed);
                return;
            }

            final boolean hasNotificationListenerPermission = Util.isNotificationListenerEnabled();
            final String garminName = Util.getGarminName();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (hasNotificationListenerPermission) {
                        if (garminName != null && garminName.length() > 0) {
                            startActivity(new Intent(SplashActivity.this, SettingsActivity.class));
                        } else {
                            startActivity(new Intent(SplashActivity.this, BluetoothListActivity.class));
                        }
                    } else {
                        startActivity(new Intent(SplashActivity.this, AboutActivity.class));
                    }
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }




    private void showAlerDialog(final int titleId, int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (titleId == R.string.title_dialog_garmin_connect_not_installed) {
                    SplashActivity.this.finish();
                }
            }
        });
        AlertDialog dialog = builder.create();

        dialog.setOnDismissListener(this);
        dialog.setOnCancelListener(this);

        dialog.show();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        Log.v(TAG, "onDismiss: " + dialogInterface);
    }


    @Override
    public void onCancel(DialogInterface dialogInterface) {
        Log.v(TAG, "onCancel: " + dialogInterface);

        this.finish();
    }
}
