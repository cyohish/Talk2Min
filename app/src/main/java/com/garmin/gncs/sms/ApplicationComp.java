package com.garmin.gncs.sms;

import android.app.Application;
import android.content.Context;

/**
 * Created by cyoh on 9/11/17.
 */

public class ApplicationComp extends Application {
    private static final String TAG = "ApplicationComp";
    private static final String PREF = "app_pref";

    public static Context context;


    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }
}
