package com.ljt.fastlivery.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.ljt.fastlivery.application.ExpressApplication;


public class ToastUtil {

    private ToastUtil() {
    }

    public static void show(CharSequence text) {
        if (text.length() < 10) {
            Toast.makeText(ExpressApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ExpressApplication.getInstance(), text, Toast.LENGTH_LONG).show();
        }
    }

    public static void show(@StringRes int resId) {
        show(ExpressApplication.getInstance().getString(resId));
    }

}