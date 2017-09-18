package com.ljt.fastlivery.application;

import android.app.Application;

/**
 * Created by ljt
 */
public class ExpressApplication extends Application {
    private static ExpressApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static ExpressApplication getInstance() {
        return sInstance;
    }
}
