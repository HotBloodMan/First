package com.ljt.newsmvp.view;

import com.ljt.fastlivery.model.NewsBean;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/13.
 */

public interface NewsView {
    void showProgress();
    void addNews(List<NewsBean> newsList);
    void hideProgress();
    void showLoadFailMsg();
}
