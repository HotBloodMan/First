package com.ljt.fastlivery.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ljt.fastlivery.R;

/**
 * Created by 1 on 2017/5/23.
 */

public class GuideAdapter extends PagerAdapter {

    String[] objects;
    Context context;
    SparseArray<View> views=new SparseArray<>();

    public GuideAdapter(Context context, String[] objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.pageview_guidance, null);

        String[] textDescribe = objects[position].split(",");
        TextView txtUp = (TextView) itemView.findViewById(R.id.id_txtUp);
        TextView txtDown = (TextView) itemView.findViewById(R.id.id_txtDown);
        txtUp.setText(textDescribe[0]);
        txtDown.setText(textDescribe[1]);
        views.put(position,itemView);

        container.addView(itemView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        return itemView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
