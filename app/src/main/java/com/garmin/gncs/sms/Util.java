package com.garmin.gncs.sms;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * Created by cyoh on 8/19/17.
 */

public class Util {
    private static final String TAG = "Util";

    public static final boolean TEST_MODE = false;

    private static Context context = ApplicationComp.context;
    public static SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
    public static SharedPreferences.Editor editor = preferences.edit();

    static {
        Map<String, ?> prefsMap = preferences.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet()) {
            Log.v(TAG, entry.getKey() + ":" + entry.getValue().toString());
        }
    }

//    public static boolean isEnabled() {
//        boolean isEnabled = preferences.getBoolean(SettingsFragment.IS_ENABLED, false);
//        return isEnabled;
//    }
//
//    public static void setEnabled(boolean isEnabled) {
//        editor.putBoolean(SettingsFragment.IS_ENABLED, isEnabled);
//        editor.apply();
//    }

//    private static final String IS_PERMISSION_DIALOG_READ = "is_permission_dialog_read";
//    public static boolean isPermissionDialogRead() {
//        boolean isRead = preferences.getBoolean(IS_PERMISSION_DIALOG_READ, false);
//        return isRead;
//    }
//
//    public static void setPermissionDialogRead(boolean isRead) {
//        editor.putBoolean(IS_PERMISSION_DIALOG_READ, isRead);
//        editor.apply();
//    }

    private static final String GARMIN_NAME = SettingsFragment.GARMIN_DEVICE_NAME;

    public static String getGarminName() {
        String name = preferences.getString(GARMIN_NAME, "");
        return name;
    }

    public static void setGarminName(String name) {
        editor.putString(GARMIN_NAME, name);
        editor.apply();
    }

    public static void setAccessNotifiactionEnabled(boolean isEnabled) {
        editor.putBoolean(SettingsFragment.IS_ACCESS_NOTIFICATION_ENABLED, isEnabled);
        editor.apply();
    }


    private static final String GARMIN_CONNECT_PACKAGE_NAME = "com.garmin.android.apps.connectmobile";

    public static boolean isGarminConnectInstalled() {
        boolean isInstalled = isInstalled(GARMIN_CONNECT_PACKAGE_NAME);
        return isInstalled;
    }

    public static boolean isInstalled(String packageName) {
        boolean isInstalled = false;
        PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "isInstalled: " + packageName + " = " + isInstalled);
        return isInstalled;
    }

    public static boolean isGarminConnected() {
        boolean connected = false;

        String garminName = null;
        if (TEST_MODE) {
            garminName = "Edge 520";
        }
        if (garminName != null && garminName.length() > 0) {
            connected = isBluetoothConnected(garminName);
        }

        return connected;
    }

    public static Set<BluetoothDevice> getBondedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();

        return bluetoothDevices;
    }

    public static boolean isBluetoothConnected(String name) {
        boolean connected = false;

        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice bluetoothDevice = null;
        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : bluetoothDevices) {
            if (device.getName().equals(name)) {
                bluetoothDevice = device;
                break;
            }
        }

        if (bluetoothDevice != null) {
            try {
                Class<?> BluetoothDevice = Class.forName("android.bluetooth.BluetoothDevice");
                Class[] parameterTypes = {};
                Method isConnected = BluetoothDevice.getDeclaredMethod("isConnected", parameterTypes);
                connected = (boolean) isConnected.invoke(bluetoothDevice, (Object[]) null);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Log.v(TAG, "isBluetoothConnected() X, connected = " + connected);
        return connected;
    }

    public static boolean hasPhoneStatePermission() {
        boolean check = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
        Log.v(TAG, "hasPhoneStatePermission = " + check);
        return check;
    }

    public static boolean hasSnedSmsPermission() {
        boolean check = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        Log.v(TAG, "hasSnedSmsPermission = " + check);
        return check;
    }

    public static boolean isNotificationListenerEnabled() {
        ComponentName componentName = new ComponentName(context, NotificationListenerServiceComp.class);
        String flat = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(componentName.flattenToString());
        Log.v(TAG, "isNotificationListenerEnabled: enabled = " + enabled);

        return enabled && isConnectNotificationListenerEnabled();
    }

    private static boolean isConnectNotificationListenerEnabled() {
        ComponentName componentName = new ComponentName("com.garmin.android.apps.connectmobile", "com.garmin.android.gncs.GNCSListenerService");
        String flat = Settings.Secure.getString(ApplicationComp.context.getContentResolver(), "enabled_notification_listeners");
        final boolean enabled = flat != null && flat.contains(componentName.flattenToString());
        Log.v(TAG, "isConnectNotificationListenerEnabled: enabled = " + enabled);

        return enabled;
    }
}
