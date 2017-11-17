package com.ljt.newsmvp.widget;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljt.fastlivery.R;
import com.ljt.newsmvp.adapter.NewsAdapter;
import com.ljt.fastlivery.model.NewsBean;
import com.ljt.newsmvp.presenter.NewsPresenter;
import com.ljt.newsmvp.presenter.NewsPresenterImpl;
import com.ljt.newsmvp.view.NewsView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends Fragment implements NewsView,SwipeRefreshLayout.OnRefreshListener {

    public static String TAG= NewsListFragment.class.getSimpleName();
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private List<NewsBean> mData;
    private NewsPresenter mNewsPresenter;

    private int mType=NewsFragment.NEWS_TYPE_TOP;

    private int pageIndex=0;

    public NewsListFragment() {
        // Required empty public constructor
    }
public static NewsListFragment newsInstance(int type){
     Log.d(TAG,TAG+" ----->>>newsInstance( ");
    Bundle bundle = new Bundle();
    NewsListFragment fragment = new NewsListFragment();
    bundle.putInt("type",type);
    fragment.setArguments(bundle);
    return fragment;
}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         Log.d(TAG,TAG+" ----->>>onCreate( ");
        mNewsPresenter=new NewsPresenterImpl(this);
//        mType=getArguments().getInt("type");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         Log.d(TAG,TAG+" ----->>>onCreateView( ");
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        mSwipeRefreshWidget= (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary,
                R.color.primary_dark, R.color.primary_light,
                R.color.accent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter=new NewsAdapter(getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        onRefresh();
        return view;
    }

    private RecyclerView.OnScrollListener mOnScrollListener=new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(newState==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1==mAdapter.getItemCount())
            {
                //加载更多
                mNewsPresenter.loadNews(mType,pageIndex+Urls.PAZE_SIZE);
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem=mLayoutManager.findLastVisibleItemPosition();
        }
    };


    private NewsAdapter.OnItemClickListener mOnItemClickListener=new NewsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
         if(mData.size()<=0){
             return ;
         }
            NewsBean news = mAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("news",news);

            View transitionView = view.findViewById(R.id.ivNews);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                    transitionView, getString(R.string.transition_news_img));
            ActivityCompat.startActivity(getActivity(),intent,options.toBundle());

        }
    };


    @Override
    public void onRefresh() {
         Log.d(TAG,TAG+" ----->>>onRefresh() ");
         pageIndex=0;
        if(mData !=null){
            mData.clear();
        }

    }

    @Override
    public void showProgress() {
         Log.d(TAG,TAG+" ----->>>showProgress() ");
    mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void addNews(List<NewsBean> newsList) {
         Log.d(TAG,TAG+" ----->>>addNews( ");
         Log.d(TAG,TAG+" ----->>>addNews( newsList= "+newsList.size());
        mAdapter.isShowFooter(true);
        if(mData==null){
            mData=new ArrayList<NewsBean>();
        }
        mData.addAll(newsList);
        if(pageIndex==0){
            mAdapter.setmData(mData);
        }else{
            //如果没有更多数据，则隐藏footer布局
            if(newsList==null||newsList.size()==0){
                mAdapter.isShowFooter(false);
            }
            mAdapter.notifyDataSetChanged();
        }
        pageIndex+=Urls.PAZE_SIZE;
    }

    @Override
    public void hideProgress() {
         Log.d(TAG,TAG+" ----->>>hideProgress() ");
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
         Log.d(TAG,TAG+" ----->>>showLoadFailMsg() ");
        if(pageIndex==0){
            mAdapter.isShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
        View view=getActivity()==null?mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
    }
}
