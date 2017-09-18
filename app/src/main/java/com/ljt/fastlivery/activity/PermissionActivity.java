package com.ljt.fastlivery.activity;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.ljt.fastlivery.utils.permission.PermissionReq;

/**
 * Created by ljt
 */
public class PermissionActivity extends AppCompatActivity {
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionReq.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
