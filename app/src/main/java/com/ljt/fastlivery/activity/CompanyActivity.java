package com.ljt.fastlivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ljt.fastlivery.R;
import com.ljt.fastlivery.adapter.CompanyAdapter;
import com.ljt.fastlivery.constants.Extras;
import com.ljt.fastlivery.model.CompanyEntity;
import com.ljt.fastlivery.model.SearchInfo;
import com.ljt.fastlivery.utils.binding.Bind;
import com.ljt.fastlivery.widget.IndexBar;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class CompanyActivity extends BaseActivity implements OnItemClickListener, IndexBar.OnIndexChangedListener {
    @Bind(R.id.lv_company)
    private ListView lvCompany;
    @Bind(R.id.ib_indicator)
    private IndexBar ibIndicator;
    @Bind(R.id.tv_indicator)
    private TextView tvIndicator;
    private List<CompanyEntity> mCompanyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        readCompany();
        lvCompany.setAdapter(new CompanyAdapter(mCompanyList));

//        Handler
//        Looper

    }

    @Override
    protected void setListener() {
        lvCompany.setOnItemClickListener(this);
        ibIndicator.setOnIndexChangedListener(this);
    }

    private void readCompany() {
        try {
            InputStream is = getAssets().open("company.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer);

            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray jArray = parser.parse(json).getAsJsonArray();
            for (JsonElement obj : jArray) {
                CompanyEntity company = gson.fromJson(obj, CompanyEntity.class);
                mCompanyList.add(company);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setName(mCompanyList.get(position).getName());
        searchInfo.setLogo(mCompanyList.get(position).getLogo());
        searchInfo.setCode(mCompanyList.get(position).getCode());
        Intent intent = new Intent();
        intent.putExtra(Extras.SEARCH_INFO, searchInfo);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onIndexChanged(String index, boolean isDown) {
        int position = -1;
        for (CompanyEntity company : mCompanyList) {
            if (TextUtils.equals(company.getName(), index)) {
                position = mCompanyList.indexOf(company);
                break;
            }
        }
        if (position != -1) {
            lvCompany.setSelection(position);
        }
        tvIndicator.setText(index);
        tvIndicator.setVisibility(isDown ? View.VISIBLE : View.GONE);
    }
}
