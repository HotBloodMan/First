package com.ljt.newsmvp.model;

/**
 * Created by ${JT.L} on 2017/11/13.
 */

public interface NewsModel {
    void loadNews(String url,int type,OnLoadNewsListListener listener);
    void loadNewsDetail(String docid,OnLoadNewsDetailListener listener);
}
