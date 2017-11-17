package com.ljt.fastlivery.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.ljt.fastlivery.R;
import com.ljt.fastlivery.adapter.HistoryAdapter;
import com.ljt.fastlivery.database.History;
import com.ljt.newsmvp.widget.NewsListFragment;
import com.ljt.fastlivery.model.SearchInfo;
import com.ljt.fastlivery.utils.DataManager;
import com.ljt.fastlivery.utils.SnackbarUtils;
import com.ljt.fastlivery.utils.binding.Bind;
import com.ljt.fastlivery.utils.binding.ViewBinder;
import com.ljt.fastlivery.utils.permission.PermissionReq;
import com.ljt.fastlivery.utils.permission.PermissionResult;
import com.ljt.fastlivery.utils.permission.Permissions;
import com.ljt.fastlivery.widget.BannerView;
import com.ljt.fastlivery.widget.RoundImageView;

import java.util.ArrayList;
import java.util.List;


public class ExpressActivity extends PermissionActivity implements OnClickListener, OnItemClickListener,
        NavigationView.OnNavigationItemSelectedListener, BannerView.BannerItemClick {
    @Bind(R.id.drawer_layout)
    private DrawerLayout drawerLayout;
    @Bind(R.id.navigation_view)
    private NavigationView navigationView;
    @Bind(R.id.tv_search)
    private TextView tvSearch;
    @Bind(R.id.tv_post)
    private TextView tvPost;
    @Bind(R.id.tv_sweep)
    private TextView tvSweep;
    @Bind(R.id.lv_un_check)
    private ListView lvUnCheck;
    @Bind(R.id.tv_empty)
    private TextView tvEmpty;
    @Bind(R.id.banner)
    private BannerView bv;

    @Bind(R.id.ll_content)
    private LinearLayout llContent;

    @Bind(R.id.ll_text)
    private LinearLayout llText;

    @Bind(R.id.fab_app)
    private FloatingActionButton floatingActionButton;
    View headerLayout;

//    @Bind(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;


    private List<History> mUnCheckList = new ArrayList<>();
    public HistoryAdapter mAdapter = new HistoryAdapter(mUnCheckList);
    private long mExitTime = 0;
    int[] ints = {R.mipmap.k91, R.mipmap.k92, R.mipmap.k93,R.mipmap.k95};
    private RoundImageView riv;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        ViewBinder.bind(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.inflateMenu(R.menu.base_toolbar_menu);
        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
//        }
        /*
        * 抽屉布局找Id
        * */
       headerLayout = navigationView.inflateHeaderView(R.layout.navigation_header);
       LinearLayout lNV= (LinearLayout) headerLayout.findViewById(R.id.navig_header);
       lNV.setVisibility(View.VISIBLE);

        CoordinatorLayout rootlayout= (CoordinatorLayout) findViewById(R.id.rootLayout);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsingToolbarLayout);
        collapsingToolbarLayout.setTitle("First");

        lvUnCheck.setAdapter(mAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.setNavigationItemSelectedListener(this);
        //圆形头像
        riv = (RoundImageView)headerLayout.findViewById(R.id.rIv_NH);

        riv.setOnClickListener(this);
        tvSearch.setOnClickListener(this);
        tvPost.setOnClickListener(this);
        tvSweep.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        lvUnCheck.setOnItemClickListener(this);
        lvUnCheck.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(ExpressActivity.this)
                        .setTitle("温馨提示")
                        .setMessage("客官，你真的要删除吗？^_^")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            mAdapter.remove(ExpressActivity.this,position);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            return;
                            }
                        }).setCancelable(false)
                        .create();
                    dialog.show();
                return true;
            }
        });

        bv.setImageForId(ints);
        bv.setDelayTime(1500);
        bv.setItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<History> unCheckList = DataManager.getInstance().getUnCheckList();
        mUnCheckList.clear();
        mUnCheckList.addAll(unCheckList);
        mAdapter.notifyDataSetChanged();
        tvEmpty.setVisibility(mUnCheckList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.tv_post:
                SnackbarUtils.show(this, "即将上线");
//                startActivity(new Intent(this, CompanyActivity.class));
                break;
            case R.id.tv_sweep:
                startCaptureActivity();
                break;
            case R.id.fab_app:
                startActivity(new Intent(this, AboutActivity.class));

            case R.id.rIv_NH:
//                startActivity(new Intent(this,LoginActivity.class));
                break;
            default:
                break;
        }
    }

    private void startCaptureActivity() {
        PermissionReq.with(this)
                .permissions(Permissions.CAMERA)
                .result(new PermissionResult() {
                    @Override
                    public void onGranted() {
                        CaptureActivity.start(ExpressActivity.this, true, 0);
                    }

                    @Override
                    public void onDenied() {
                        SnackbarUtils.show(ExpressActivity.this, getString(R.string.no_permission, Permissions.CAMERA_DESC, "打开扫一扫"));
                    }
                })
                .request();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        History history = mUnCheckList.get(position);
        SearchInfo searchInfo = new SearchInfo();
        searchInfo.setPost_id(history.getPost_id());
        searchInfo.setCode(history.getCompany_param());
        searchInfo.setName(history.getCompany_name());
        searchInfo.setLogo(history.getCompany_icon());
        ResultActivity.start(this, searchInfo);
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        drawerLayout.closeDrawers();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                item.setChecked(false);
            }
        }, 500);
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.action_history:
                intent.setClass(this, HistoryActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_qrcode:
                intent.setClass(this, QRCodeActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                share();
                return true;
            case R.id.action_news:
//                Intent intent1 = Utils.startActivity(this, NewsActivity.class);
//                startActivity(intent1);
                //news
                llContent.removeView(llText);
                getSupportFragmentManager().beginTransaction().replace(R.id.fl_express,new NewsListFragment()).commit();
                toolbar.setTitle("News");
                System.out.println("-------------->>> has add news");
                return true;
            case R.id.action_pictures:
                intent.setClass(this,PictureActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_about:
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        if (System.currentTimeMillis() - mExitTime > 2000) {
            mExitTime = System.currentTimeMillis();
            SnackbarUtils.show(this, R.string.click2exit);
        } else {
            finish();
        }
    }

    @Override
    public void OnBannnerItemClick(Object o) {
        return;
    }



}
