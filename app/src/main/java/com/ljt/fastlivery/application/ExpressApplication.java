package com.ljt.fastlivery.application;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ljt
 */
public class ExpressApplication extends Application {
    private static ExpressApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
    }

    public static ExpressApplication getInstance() {
        return sInstance;
    }
}
