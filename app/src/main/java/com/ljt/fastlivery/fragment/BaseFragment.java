package com.ljt.fastlivery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljt.fastlivery.activity.BaseActivity;
import com.ljt.fastlivery.application.ExpressApplication;
import com.ljt.fastlivery.utils.ToastUtil;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by ${JT.L} on 2017/10/27.
 */

public abstract class BaseFragment extends Fragment {

    protected String TAG =this.getClass().getSimpleName();;
    protected BaseActivity mActivity;
    protected ExpressApplication mApp;
    protected boolean mIsLoadedData = false;
    protected  View mContentView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mApp = ExpressApplication.getInstance();
        mActivity= (BaseActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //避免多次从xml中加载布局文件
        if(mContentView==null){
             Log.d(TAG,TAG+"onCreateView( ----->>> ");
            initView(savedInstanceState);
            setListener();
            processLogic(savedInstanceState);
        }else{
         ViewGroup parent= (ViewGroup) mContentView.getParent();
            if(parent!=null){
                parent.removeView(mContentView);
            }
        }

        return mContentView;

    }

    protected void setContentView(int layoutResID){
        mContentView=LayoutInflater.from(mApp).inflate(layoutResID,null);
    }

    protected <VT extends View> VT getViewById(int id){
        return (VT) mContentView.findViewById(id);
    }

    /*
    *
    *处理业务逻辑，状态恢复等操作
    * */
    protected abstract void processLogic(Bundle savedInstanceState);

    /**
     * 给View控件添加事件监听器
     */
    protected abstract void setListener();

    protected abstract void initView(Bundle savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            handleOnVisibilityChangedToUser(isVisibleToUser);
        }
        onVisibleToUser();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            handleOnVisibilityChangedToUser(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(getUserVisibleHint()){
            handleOnVisibilityChangedToUser(false);
        }
    }

    private void handleOnVisibilityChangedToUser(boolean isVisibleToUser){
        if(isVisibleToUser){
            //
            if(!mIsLoadedData){
                mIsLoadedData=true;
                onLazyLoadOnce();
            }
            onVisibleToUser();
        }else{
            //对用户不可见
            onInvisibleToUser();
        }
    };

    /**
     * 懒加载一次。如果只想在对用户可见时才加载数据，并且只加载一次数据，在子类中重写该方法
     */
    protected void onLazyLoadOnce(){
    }
    /**
     * 对用户可见时触发该方法。如果只想在对用户可见时才加载数据，在子类中重写该方法
     */
    protected void onVisibleToUser(){
    }

    /**
     * 对用户不可见时触发该方法
     */
    protected void onInvisibleToUser() {
    }

    protected void showToast(String text) {
        ToastUtil.show(text);
    }
    protected void showLoadingDialog() {
        mActivity.showLoadingDialog();
    }

    protected void dismissLoadingDialog() {
        if(isVisible()){
            mActivity.dismissLoadingDialog();
        }
    }

    }
