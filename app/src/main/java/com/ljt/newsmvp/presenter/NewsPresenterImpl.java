package com.ljt.newsmvp.presenter;

import android.util.Log;

import com.ljt.fastlivery.model.NewsBean;
import com.ljt.fastlivery.utils.LogUtils;
import com.ljt.newsmvp.model.NewsModel;
import com.ljt.newsmvp.model.NewsModelImpl;
import com.ljt.newsmvp.model.OnLoadNewsListListener;
import com.ljt.newsmvp.view.NewsView;
import com.ljt.newsmvp.widget.NewsFragment;
import com.ljt.newsmvp.widget.NewsListFragment;
import com.ljt.newsmvp.widget.Urls;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/13.
 */

public class NewsPresenterImpl implements NewsPresenter,OnLoadNewsListListener{
    public static String TAG= NewsPresenterImpl.class.getSimpleName();

    private NewsView mNewsView;
    private NewsModel mNewsModel;

    public NewsPresenterImpl(NewsView mNewsView) {
         Log.d(TAG,TAG+" ----->>> NewsPresenterImpl( ");
        this.mNewsView = mNewsView;
        this.mNewsModel = new NewsModelImpl();
    }

    @Override
    public void loadNews(int type, int page) {
        String url = getUrl(type, page);
        LogUtils.d(TAG,url);
        //只有第一页的或者刷新的时候才显示刷新进度条
        if(page==0){
            mNewsView.showProgress();
        }
        mNewsModel.loadNews(url,type,this);
    }

    @Override
    public void onSuccess(List<NewsBean> list) {
        mNewsView.hideProgress();
        mNewsView.addNews(list);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.showLoadFailMsg();
    }
    /*
    * 根据类别和页面索引创建url
    * */
    private String getUrl(int type,int pageIndex){
        StringBuffer sb = new StringBuffer();
        switch (type){
            case NewsFragment.NEWS_TYPE_TOP:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }


}
