package com.ljt.fastlivery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by ${JT.L} on 2017/10/31.
 */

public class ReceiveJpushReceiver extends JPushMessageReceiver {

    public static String TAG= ReceiveJpushReceiver.class.getSimpleName();
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.d(TAG,TAG+"onReceive( ----ReceiveJpushReceiver->>> ");
//        Toast.makeText(context,"收到推送消息....",Toast.LENGTH_LONG).show();
//    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        Log.d(TAG,TAG+"onReceive( ----ReceiveJpushReceiver->>> ");
        Toast.makeText(context,"收到推送消息....",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onCheckTagOperatorResult(context, jPushMessage);
        Log.d(TAG,TAG+"onReceive( ----ReceiveJpushReceiver->>> ");
        Toast.makeText(context,"收到推送消息..222..",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
        super.onTagOperatorResult(context, jPushMessage);
        Log.d(TAG,TAG+"onReceive( ----ReceiveJpushReceiver->>> ");
        Toast.makeText(context,"收到推送消息..333..",Toast.LENGTH_LONG).show();
    }
}
