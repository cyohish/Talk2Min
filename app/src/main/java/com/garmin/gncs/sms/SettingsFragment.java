package com.garmin.gncs.sms;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SettingsFragment extends PreferenceFragment {

    private static final String TAG = "SettingsFragment";
//    public static final String IS_ENABLED = "is_enabled";
    public static final String GARMIN_DEVICE_NAME = "garmin_device_name";
    public static final String IS_ACCESS_NOTIFICATION_ENABLED = "is_access_notification_enabled";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_settings);

        SettingsActivity activity = (SettingsActivity) getActivity();
//        activity.setPreferenceChangeListener(findPreference(IS_ENABLED));
        activity.setPreferenceClickListener(findPreference(GARMIN_DEVICE_NAME));
        activity.setPreferenceChangeListener(findPreference(IS_ACCESS_NOTIFICATION_ENABLED));

//        SettingsActivity.bindPreferenceSummaryToValue(findPreference(IS_ENABLED));
//        SettingsActivity.bindPreferenceSummaryToValue(findPreference(GARMIN_DEVICE_NAME));
//        SettingsActivity.bindPreferenceSummaryToValue(findPreference(IS_ACCESS_NOTIFICATION_ENABLED));
    }
}
