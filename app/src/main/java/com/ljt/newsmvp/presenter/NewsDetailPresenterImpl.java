package com.ljt.newsmvp.presenter;

import android.content.Context;
import android.util.Log;

import com.ljt.fastlivery.model.NewsDetailBean;
import com.ljt.newsmvp.model.NewsModel;
import com.ljt.newsmvp.model.NewsModelImpl;
import com.ljt.newsmvp.model.OnLoadNewsDetailListener;
import com.ljt.newsmvp.view.NewsDetailView;

/**
 * Created by ${JT.L} on 2017/11/17.
 */

public class NewsDetailPresenterImpl implements NewsDetailPresenter,OnLoadNewsDetailListener{

    public static String TAG= NewsDetailPresenterImpl.class.getSimpleName();
    private Context mContext;
    private NewsDetailView mNewsDetailView;
    private NewsModel mNewsModel;

    public NewsDetailPresenterImpl(Context mContext, NewsDetailView mNewsDetailView) {
        this.mContext = mContext;
        this.mNewsDetailView = mNewsDetailView;
        mNewsModel=new NewsModelImpl();
    }

    @Override
    public void onSuccess(NewsDetailBean newsDetailBean) {
         Log.d(TAG,TAG+" ----->>> onSuccess(");
        if(newsDetailBean!=null){
            mNewsDetailView.showNewsDetailContent(newsDetailBean.getBody());
        }
        mNewsDetailView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
         Log.d(TAG,TAG+" ----->>> onFailure(");
        mNewsDetailView.hideProgress();
    }
    @Override
    public void loadNewsDetail(String docId) {
         Log.d(TAG,TAG+" ----->>> loadNewsDetail(");
        mNewsDetailView.showProgress();
        mNewsModel.loadNewsDetail(docId,this);
    }
}
