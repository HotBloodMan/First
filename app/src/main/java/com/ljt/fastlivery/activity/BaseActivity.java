package com.ljt.fastlivery.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleAdapter;

import com.ljt.fastlivery.R;
import com.ljt.fastlivery.application.ExpressApplication;
import com.ljt.fastlivery.utils.ToastUtil;
import com.ljt.fastlivery.utils.binding.ViewBinder;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.pedant.SweetAlert.SweetAlertDialog;


public abstract class BaseActivity extends PermissionActivity {
    protected Handler mHandler = new Handler(Looper.getMainLooper());

    protected String TAG;
    protected ExpressApplication mApp;
    private SweetAlertDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=this.getClass().getSimpleName();
        mApp = ExpressApplication.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            TypedValue typedValue = new TypedValue();
            getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            tintManager.setStatusBarTintResource(typedValue.resourceId);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
       ViewBinder.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setListener();
    }

    protected void setListener() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showToast(String text) {
        ToastUtil.show(text);
    }

    public void showLoadingDialog() {
        if(mLoadingDialog==null){
            mLoadingDialog=new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper().setBarColor(getResources()
                    .getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("数据加载中...");
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
         if(mLoadingDialog!=null){
             mLoadingDialog.dismiss();
         }
    }
}
