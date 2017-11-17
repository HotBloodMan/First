package com.ljt.fastlivery.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.ljt.fastlivery.R;

/**
 * Created by Administrator on 2017/5/24/024.
 */

public class BannerView extends FrameLayout implements ViewPager.OnPageChangeListener {

    private String[] imgUrls;//存放网络图片Id的数组
    private int[] imageIds;//存放本地图片Id的数组
    private int mImaCount;//图片数量的大小
    private View[] imageViews;
    private ViewPager vP;
    private RadioGroup mRG;
    private RadioButton[] mNavigationViews;
    private int mCurrentItem=1;
    private boolean isScrolling;
    private int delayTime = 5000;//轮播间隔时间


    public BannerView(@NonNull Context context) {
        this(context,null);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

    //本地图片
    public void setImageForId(int[] urls){
        imageIds=null;
        this.imageIds=urls;
        mImaCount=urls.length;
        init();
    }
    //网络图片
    public void setImageForUrl(String[] urls){
        imageIds=null;
        this.imgUrls=urls;
        mImaCount=urls.length;
        init();
    }

    private void init() {
       //左右加两缓存 无限轮播
       imageViews=new View[mImaCount+2];
        initViews();
        //添加导航条
        addNavigetion();   
        //添加适配器
        setAdapter();
        //开启轮播
        startLoop();

    }
     private Handler handler=new Handler();
    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isScrolling){
                    if(mImaCount<=0){
                        return;
                    }else{
                        vP.setCurrentItem(mCurrentItem % (mImaCount+2));
                        mCurrentItem++;
                    }
                }
                startLoop();
            }
        },delayTime);

    }

    /**
     * 轮播时间跳转
     *
     * @param delayTime 单位ms
     */
    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    private void setAdapter() {
        BannerAdatper adatper = new BannerAdatper();
        vP.setAdapter(adatper);
        vP.setCurrentItem(1);
        vP.addOnPageChangeListener(this);
    }

    private void addNavigetion() {
        if(mImaCount<=0){
            return;
        }
        //设置一个和图片数量相同的指示器。
        mNavigationViews=new RadioButton[mImaCount];
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(20, 20);
        layoutParams.leftMargin=5;
        layoutParams.rightMargin=5;
        for(int i=0;i<mNavigationViews.length;i++){
            RadioButton radioButton = new RadioButton(getContext());
            ViewCompat.setBackground(radioButton,
                    getContext().getResources()
                            .getDrawable(R.drawable.selector_navigation_radio));
            radioButton.setButtonDrawable(new BitmapDrawable());
            radioButton.setLayoutParams(layoutParams);
            mRG.addView(radioButton);
            mNavigationViews[i]=radioButton;
        }
        mNavigationViews[0].setSelected(true);
    }

    private void initViews() {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.banner,this,true);
        vP = (ViewPager) view.findViewById(R.id.easy_banner_pager);
        mRG = (RadioGroup) view.findViewById(R.id.easy_banner_radio_bar);
        vP.removeAllViews();
        mRG.removeAllViews();
        initImageViews();
    }

    private void initImageViews() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        for(int i=0;i<imageViews.length;i++){
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //判断图片数据来自网络还是来自本地
            if(imgUrls!=null){
                String url;
                if(i==0){
                    url=imgUrls[mImaCount-1];
                }else if(i==imageViews.length-1){
                    url=imgUrls[0];
                }else{
                    url=imgUrls[i-1];
                }
                Glide.with(getContext()).load(url).into(imageView);
                imageView.setTag(R.id.tag,url);
            }else{
                int id;
                if(i==0){
                    id=imageIds[mImaCount-1];
                }else if(i==imageViews.length-1){
                    id=imageIds[0];
                }else{
                    id=imageIds[i-1];
                }
                System.out.println("aaa---->>id= "+id);
                imageView.setImageResource(id);
                imageView.setTag(R.id.tag,id);
            }
          imageViews[i]=imageView;
        }
    }

    private BannerItemClick mItemClickListener;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if(vP.getCurrentItem()==0){
            int i = mImaCount - 1;
            System.out.println("aaa---->>>i= "+i);
            mNavigationViews[mImaCount-1].setSelected(true);
        }else if(vP.getCurrentItem()==mImaCount+1){
            mNavigationViews[0].setSelected(true);
        }
    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0;i<mNavigationViews.length;i++){
            mNavigationViews[i].setSelected(i==position-1);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    switch (state){
        case ViewPager.SCROLL_STATE_DRAGGING://滑动中
            isScrolling=true;
            break;
        case ViewPager.SCROLL_STATE_SETTLING://手指从屏幕抬起来
            isScrolling=true;
            break;
        case ViewPager.SCROLL_STATE_IDLE:  //滑动完全结束
            isScrolling=false;
            //实现无限轮播 左右两边缓存
            if(vP.getCurrentItem()==0){
                vP.setCurrentItem(mImaCount,false);
            }else if(vP.getCurrentItem()==mImaCount+1){
                vP.setCurrentItem(1,false);
            }
            mCurrentItem=vP.getCurrentItem();
            System.out.println("aaa--->>>mCurrentItem= "+mCurrentItem);
            break;
    }
    }

    public interface BannerItemClick{
        void OnBannnerItemClick(Object o);
    }
    public void setItemClickListener(BannerItemClick itemClickListener){
       this.mItemClickListener=itemClickListener;
    }

    private class BannerAdatper extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //设置点击事件
            if(mItemClickListener!=null){
                imageViews[position].setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.OnBannnerItemClick(v.getTag(R.id.tag));
                    }
                });
            }
            container.addView(imageViews[position]);
            return imageViews[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews[position]);
        }
    }

}
