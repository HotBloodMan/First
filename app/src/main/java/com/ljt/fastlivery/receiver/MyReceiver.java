package com.ljt.fastlivery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ${JT.L} on 2017/10/31.
 */

public class MyReceiver extends BroadcastReceiver {
    public static String TAG= MyReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);

         Log.d(TAG,TAG+"onReceive( ----MyReceiver->>> title= "+title.toString()+" content= "+content.toString());
        Toast.makeText(context,"收到推送消息...."+" title= "+title.toString()+" content= "+content.toString(),Toast.LENGTH_LONG).show();
    }
}
