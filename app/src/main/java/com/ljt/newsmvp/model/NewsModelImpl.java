package com.ljt.newsmvp.model;

import android.util.Log;

import com.ljt.fastlivery.model.NewsBean;
import com.ljt.fastlivery.model.NewsDetailBean;
import com.ljt.fastlivery.utils.OkHttpUtils;
import com.ljt.newsmvp.widget.NewsFragment;
import com.ljt.newsmvp.widget.NewsJsonUtils;
import com.ljt.newsmvp.widget.Urls;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/13.
 */

public class NewsModelImpl implements NewsModel{

    public static String TAG= NewsModelImpl.class.getSimpleName();

/*
加载列表
* */
    @Override
    public void loadNews(String url, final int type, final OnLoadNewsListListener listener) {
         Log.d(TAG,TAG+" ------------------>>>loadNews( ");
        OkHttpUtils.ResultCallback<String> loadNewsCallback=new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<NewsBean> newsBeenList = NewsJsonUtils.readJsonNewsBeans(response, getID(type));
                 Log.d(TAG,TAG+" ----------->>>newsBeenList=  "+newsBeenList.size());
                listener.onSuccess(newsBeenList);
            }
            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };
        OkHttpUtils.get(url,loadNewsCallback);
    }

    /*
    * 加载新闻详情
    * */
    @Override
    public void loadNewsDetail(final String docid, final OnLoadNewsDetailListener listener) {

        String url = getDetailUrl(docid);
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {

            @Override
            public void onSuccess(String response) {
                NewsDetailBean newsDetailBean = NewsJsonUtils.readJsonNewsDetailBeans(response, docid);
                listener.onSuccess(newsDetailBean);
            }
            @Override
            public void onFailure(Exception e) {
              listener.onFailure("load news detail info failure",e);
            }
        };
        OkHttpUtils.get(url,loadNewsCallback);
    }


    //获取ID
    private String getID(int type){
        String id;
        switch (type){
            case NewsFragment.NEWS_TYPE_TOP:
                id= Urls.TOP_ID;
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                id=Urls.NBA_ID;
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                id=Urls.CAR_ID;
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                id=Urls.JOKE_ID;
                break;
            default:
                id=Urls.TOP_ID;
                break;
        }
        return id;
    }

    private String getDetailUrl(String docId){
        StringBuffer sb=new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docId).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }


}
