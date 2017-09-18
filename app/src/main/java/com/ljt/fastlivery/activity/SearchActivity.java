package com.ljt.fastlivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.zxing.activity.CaptureActivity;
import com.ljt.fastlivery.R;
import com.ljt.fastlivery.adapter.SuggestionAdapter;
import com.ljt.fastlivery.constants.Extras;
import com.ljt.fastlivery.constants.RequestCode;
import com.ljt.fastlivery.http.HttpCallback;
import com.ljt.fastlivery.http.HttpClient;
import com.ljt.fastlivery.model.CompanyEntity;
import com.ljt.fastlivery.model.SearchInfo;
import com.ljt.fastlivery.model.SuggestionResult;
import com.ljt.fastlivery.utils.SnackbarUtils;
import com.ljt.fastlivery.utils.binding.Bind;
import com.ljt.fastlivery.utils.permission.PermissionReq;
import com.ljt.fastlivery.utils.permission.PermissionResult;
import com.ljt.fastlivery.utils.permission.Permissions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchActivity extends BaseActivity implements TextWatcher, View.OnClickListener,
        AdapterView.OnItemClickListener {
    @Bind(R.id.et_post_id)
    private EditText etPostId;
    @Bind(R.id.iv_scan)
    private ImageView ivScan;
    @Bind(R.id.iv_clear)
    private ImageView ivClear;
    @Bind(R.id.lv_suggestion)
    private ListView lvSuggestion;
    private Map<String, CompanyEntity> mCompanyMap = new HashMap<>();
    private List<CompanyEntity> mSuggestionList = new ArrayList<>();
    private SuggestionAdapter mSuggestionAdapter = new SuggestionAdapter(mSuggestionList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        readCompany();

        lvSuggestion.setAdapter(mSuggestionAdapter);
    }

    @Override
    protected void setListener() {
        lvSuggestion.setOnItemClickListener(this);
        etPostId.addTextChangedListener(this);
        ivScan.setOnClickListener(this);
        ivClear.setOnClickListener(this);
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
                if (!TextUtils.isEmpty(company.getCode())) {
                    mCompanyMap.put(company.getCode(), company);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(final Editable s) {
        if (s.length() > 0) {
            ivScan.setVisibility(View.INVISIBLE);
            ivClear.setVisibility(View.VISIBLE);
        } else {
            ivScan.setVisibility(View.VISIBLE);
            ivClear.setVisibility(View.INVISIBLE);
        }
        mSuggestionList.clear();
        mSuggestionAdapter.notifyDataSetChanged();
        if (s.length() >= 8) {
            getSuggestion(s.toString());
        }
    }

    private void getSuggestion(final String postId) {
        HttpClient.getSuggestion(postId, new HttpCallback<SuggestionResult>() {
            @Override
            public void onResponse(SuggestionResult suggestionResult) {
                if (!TextUtils.equals(etPostId.getText().toString(), postId)) {
                    return;
                }
                onSuggestion(suggestionResult);
            }

            @Override
            public void onError(VolleyError volleyError) {
                if (!TextUtils.equals(etPostId.getText().toString(), postId)) {
                    return;
                }
                onSuggestion(null);
            }
        });
    }

    private void onSuggestion(SuggestionResult response) {
        mSuggestionList.clear();
        if (response != null && response.getAuto() != null && !response.getAuto().isEmpty()) {
            for (SuggestionResult.AutoBean bean : response.getAuto()) {
                if (mCompanyMap.containsKey(bean.getComCode())) {
                    mSuggestionList.add(mCompanyMap.get(bean.getComCode()));
                }
            }
        }
        String label = "<font color='%1$s'>没有查到？</font> <font color='%2$s'>请选择快递公司</font>";
        String grey = String.format("#%06X", 0xFFFFFF & getResources().getColor(R.color.grey));
        String blue = String.format("#%06X", 0xFFFFFF & getResources().getColor(R.color.blue));
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.setName(String.format(label, grey, blue));
        mSuggestionList.add(companyEntity);
        mSuggestionAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_scan:
                startCaptureActivity();
                break;
            case R.id.iv_clear:
                etPostId.setText("");
                break;
        }
    }

    private void startCaptureActivity() {
        PermissionReq.with(this)
                .permissions(Permissions.CAMERA)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        CaptureActivity.start(SearchActivity.this, true, RequestCode.REQUEST_CAPTURE);
                    }

                    @Override
                    public void onDenied() {
                        SnackbarUtils.show(SearchActivity.this, getString(R.string.no_permission, Permissions.CAMERA_DESC, "扫描单号"));
                    }
                })
                .request();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == mSuggestionList.size() - 1) {
            startActivityForResult(new Intent(this, CompanyActivity.class), RequestCode.REQUEST_COMPANY);
            return;
        }
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setPost_id(etPostId.getText().toString());
        searchInfo.setCode(mSuggestionList.get(position).getCode());
        searchInfo.setName(mSuggestionList.get(position).getName());
        searchInfo.setLogo(mSuggestionList.get(position).getLogo());
        ResultActivity.start(this, searchInfo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }

        switch (requestCode) {
            case RequestCode.REQUEST_CAPTURE:
                // 处理扫描结果（在界面上显示）
                String resultStr = data.getStringExtra(Extras.SCAN_RESULT);
                etPostId.setText(resultStr);
                etPostId.setSelection(etPostId.length());
                break;
            case RequestCode.REQUEST_COMPANY:
                SearchInfo mSearchInfo = (SearchInfo) data.getSerializableExtra(Extras.SEARCH_INFO);
                mSearchInfo.setPost_id(etPostId.getText().toString());
                Intent intent = new Intent(this, ResultActivity.class);
                intent.putExtra(Extras.SEARCH_INFO, mSearchInfo);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
