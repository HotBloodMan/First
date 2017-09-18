package com.ljt.fastlivery.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.ljt.fastlivery.R;
import com.ljt.fastlivery.adapter.GuideAdapter;
import com.ljt.fastlivery.widget.CircleIndicator;

import java.io.File;

public class GuideActivity extends Activity {


    //注入adapter中的数组
    private String[] data = {"欢迎来到First,一款简约方便的生活应用",
            "让你不再为物流焦急等待,它将极大地提高你的生活品质", "First,努力让你的生活变的美好"
            };

    String videoPath;
    private VideoView videoView;
    private ImageView imgEntry;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private GuideAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        videoPath=getIntent().getStringExtra("VideoPath");
        initWidget();
        loadData();
    }

    private void initWidget() {
        videoView = (VideoView) this.findViewById(R.id.id_video);
        imgEntry = (ImageView) this.findViewById(R.id.id_imgEntry);
        imgEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuideActivity.this,ExpressActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                finish();
            }
        });
        viewPager= (ViewPager) this.findViewById(R.id.iv_viewPager);
        adapter = new GuideAdapter(getApplicationContext(), data);
        viewPager.setAdapter(adapter);
        System.out.println("aaa----->>>>1111 adapter= "+adapter.getCount());

        circleIndicator= (CircleIndicator) this.findViewById(R.id.id_circleIndicator);
        circleIndicator.setViewPager(viewPager);

        //让进入主界面的按钮是否显示
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                System.out.println("ccc------>>>>>>"+position);
                if(position==2){
                    imgEntry.setVisibility(View.VISIBLE);
                }else{
                    imgEntry.setVisibility(View.GONE);
                }

            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void loadData() {
        File file = new File(videoPath);
        if(!file.exists()){
            Log.d("GuideActivity","aaa----->>>视频文件不存在");
        }else{
            videoView.setVideoPath(file.getPath());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    //设置为填充父窗体
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            //设置布局
            videoView.setLayoutParams(layoutParams);
            //循环播放
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView!=null){
            //释放掉内存
            videoView.suspend();
        }
    }
}
