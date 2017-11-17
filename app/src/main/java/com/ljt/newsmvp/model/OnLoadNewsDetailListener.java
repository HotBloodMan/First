package com.ljt.newsmvp.model;

import com.ljt.fastlivery.model.NewsDetailBean;

/**
 * Created by ${JT.L} on 2017/11/17.
 * Description : 新闻详情加载回调
 */

public interface OnLoadNewsDetailListener {
    void onSuccess(NewsDetailBean newsDetailBean);
    void onFailure(String msg,Exception e);
}
