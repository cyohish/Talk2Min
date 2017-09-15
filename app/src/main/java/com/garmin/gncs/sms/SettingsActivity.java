package com.garmin.gncs.sms;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceManager;
import android.util.Log;


public class SettingsActivity extends AppCompatPreferenceActivity implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    private static final String TAG = "SettingsActivity";

    private static final int REQUEST_CHOOSE_BLUETOOTH_DEVICE = 1;
    private static final int REQUEST_CHANGE_ACCESS_NOTIFICATION = 2;

    private Context context;
    private SharedPreferences preferences;

    private SwitchPreference enablePreference;
    private SwitchPreference accessNotificationPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate:");
        super.onCreate(savedInstanceState);
        setupActionBar();

        context = getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        addPreferencesFromResource(R.xml.pref_settings);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment(), null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult: requestCode = " + requestCode + ", resultCode = " + resultCode + ", data = " + data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHOOSE_BLUETOOTH_DEVICE: {
                if (resultCode == RESULT_OK) {

                }
                break;
            }
            case REQUEST_CHANGE_ACCESS_NOTIFICATION: {
                accessNotificationPreference.setChecked(Util.isNotificationListenerEnabled());

                break;
            }
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        final String key = preference.getKey();
        Log.v(TAG, "onPreferenceClick: key = " + key);
        switch (key) {
            case SettingsFragment.GARMIN_DEVICE_NAME: {
                showBluetoothDeviceListActivity();
                break;
            }

        }
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        Log.v(TAG, "onPreferenceChange: key = " + key + ", value = " + value.toString());
        switch (key) {
//            case SettingsFragment.IS_ENABLED: {
//                return true;
//            }
            case SettingsFragment.IS_ACCESS_NOTIFICATION_ENABLED: {
                showAccessNotificationActivity();
                break;
            }
        }
        return false;
    }

    protected void setPreferenceClickListener(Preference preference) {
        preference.setOnPreferenceClickListener(this);
        if (preference.getKey().equals(SettingsFragment.GARMIN_DEVICE_NAME)) {
//            if (preference.getTitle().length() < 1) {
//                Log.d(TAG, "GARMIN_DEVICE_NAME is empty");
//                preference.setTitle(R.string.pref_garmin_device_name_default);
//            }
            String name = Util.getGarminName();
            if (name.length() < 1) {
                preference.setTitle(R.string.pref_garmin_device_name_default);
            } else {
                preference.setTitle(name);
            }
        }
    }

    protected void setPreferenceChangeListener(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        if (preference.getKey().equals(SettingsFragment.IS_ACCESS_NOTIFICATION_ENABLED)) {
            accessNotificationPreference = (SwitchPreference) preference;
            accessNotificationPreference.setChecked(Util.isNotificationListenerEnabled());
        }
    }

    private void showBluetoothDeviceListActivity() {
        Log.v(TAG, "showBluetoothDeviceListActivity:");
        Intent activity = new Intent(context, BluetoothListActivity.class);
        startActivityForResult(activity, REQUEST_CHOOSE_BLUETOOTH_DEVICE);
    }

    private void showAccessNotificationActivity() {
        Log.v(TAG, "showAccessNotificationActivity:");
        Intent activity = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivityForResult(activity, REQUEST_CHANGE_ACCESS_NOTIFICATION);
    }


}
