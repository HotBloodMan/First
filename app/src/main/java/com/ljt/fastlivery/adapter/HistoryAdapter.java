/**
 * 2015-4-2
 */
package com.ljt.fastlivery.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ljt.fastlivery.R;
import com.ljt.fastlivery.database.History;
import com.ljt.fastlivery.http.HttpClient;
import com.ljt.fastlivery.utils.binding.Bind;
import com.ljt.fastlivery.utils.binding.ViewBinder;

import java.util.List;


/**
 * @author ljt
 */
public class HistoryAdapter extends BaseAdapter {
    private List<History> mHistoryList;

    public HistoryAdapter(List<History> historyList) {
        this.mHistoryList = historyList;
    }

    @Override
    public int getCount() {
        return mHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_holder_history, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(context)
                .load(HttpClient.urlForLogo(mHistoryList.get(position).getCompany_icon()))
                .dontAnimate()
                .placeholder(R.drawable.ic_default_logo)
                .into(holder.ivLogo);
        String isCheck = mHistoryList.get(position).getIs_check();
        int checkTextColor;
        if (TextUtils.equals(isCheck, "0")) {
            isCheck = context.getString(R.string.uncheck);
            checkTextColor = context.getResources().getColor(R.color.orange_700);
        } else {
            isCheck = context.getString(R.string.ischeck);
            checkTextColor = context.getResources().getColor(R.color.grey);
        }
        holder.tvIsCheck.setText(isCheck);
        holder.tvIsCheck.setTextColor(checkTextColor);
        String remark = mHistoryList.get(position).getRemark();
        if (TextUtils.isEmpty(remark)) {
            holder.tvName.setText(mHistoryList.get(position).getCompany_name());
            holder.tvPostId.setText(mHistoryList.get(position).getPost_id());
        } else {
            holder.tvName.setText(remark);
            holder.tvPostId.setText(mHistoryList.get(position).getCompany_name() +
                    " " + mHistoryList.get(position).getPost_id());
        }
        return convertView;
    }

    public  void remove(Context context,int index){
        if(index<0){
            Toast.makeText(context, "列表中并没有结果!!!", Toast.LENGTH_LONG).show();
            return;
        }else{
            mHistoryList.remove(index);
        }
        notifyDataSetChanged();
    }


    public static class ViewHolder {
        @Bind(R.id.iv_logo)
        public ImageView ivLogo;
        @Bind(R.id.tv_name)
        public TextView tvName;
        @Bind(R.id.tv_post_id)
        public TextView tvPostId;
        @Bind(R.id.tv_is_check)
        public TextView tvIsCheck;

        public ViewHolder(View view) {
            ViewBinder.bind(this, view);
        }
    }
}
