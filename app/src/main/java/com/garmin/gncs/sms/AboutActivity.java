package com.garmin.gncs.sms;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class AboutActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, DialogInterface.OnCancelListener {
    private static final String TAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onClick(View view) {
        startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), REQUEST_CHANGE_ACCESS_NOTIFICATION);
    }

    private static final int REQUEST_CHANGE_ACCESS_NOTIFICATION = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult: requestCode = " + requestCode + ", resultCode = " + resultCode + ", data = " + data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHANGE_ACCESS_NOTIFICATION: {
                if (Util.isNotificationListenerEnabled()) {
                    String garminName = Util.getGarminName();
                    if(garminName != null && garminName.length() > 0) {
                        startActivity(new Intent(this, SettingsActivity.class));
                    } else {
                        startActivity(new Intent(this, BluetoothListActivity.class));
                    }

                    this.finish();
                } else {
                    showAlerDialog(R.string.title_dialog_cannot_access_notification, R.string.message_dialog_cannot_access_notification);
                }
                break;
            }
        }
    }

    private void showAlerDialog(final int titleId, int messageId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titleId);
        builder.setMessage(messageId);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AboutActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.setOnDismissListener(this);
        dialog.setOnCancelListener(this);

        dialog.show();
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }
}
