package com.ljt.newsmvp.model;

import com.ljt.fastlivery.model.NewsBean;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/13.
 */

public interface OnLoadNewsListListener {
    void onSuccess(List<NewsBean> list);
    void onFailure(String msg,Exception e);
}
