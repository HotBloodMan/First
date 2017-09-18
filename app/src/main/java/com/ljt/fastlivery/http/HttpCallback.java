package com.ljt.fastlivery.http;

import com.android.volley.VolleyError;

/**
 * Created by ljt
 */
public interface HttpCallback<T> {
    void onResponse(T t);

    void onError(VolleyError volleyError);
}
