package com.ljt.newsmvp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljt.fastlivery.R;
import com.ljt.fastlivery.model.NewsBean;
import com.ljt.fastlivery.utils.LoadImageUtils;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/13.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static String TAG= NewsAdapter.class.getSimpleName();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<NewsBean> mData;
    private boolean mShowFooter=true;
    private Context mContext;

    public NewsAdapter(Context context){
        this.mContext=context;
    }

    public void setmData(List<NewsBean> data){
        this.mData=data;
        this.notifyDataSetChanged();
    }

    private OnItemClickListener  onItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        //最后一个item设置为footerView
        if(!mShowFooter){
            return TYPE_ITEM;
        }
        if(position+1==getItemCount()){
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            return new FooterViewHolder(view);
        }
    }

    public class FooterViewHolder extends  RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  ItemViewHolder){
            NewsBean newsBean = mData.get(position);
            if(newsBean==null){
                return;
            }
            ((ItemViewHolder) holder).mTitle.setText(newsBean.getTitle());
            ((ItemViewHolder) holder).mDesc.setText(newsBean.getDigest());
            LoadImageUtils.display(mContext,((ItemViewHolder) holder).mNewsImg,newsBean.getImgsrc());
        }
    }

    @Override
    public int getItemCount() {
         Log.d(TAG,TAG+" ----->>>getItemCount() ");
        int begin=mShowFooter?1:0;
        if(mData==null){
            return begin;
        }
        return mData.size()+begin;
    }
    public NewsBean getItem(int position){
         Log.d(TAG,TAG+" ----->>>getItem(int position)= "+position);
        return mData==null?null:mData.get(position);
    }

    public void isShowFooter(boolean showFooter){
         Log.d(TAG,TAG+" ----->>isShowFooter(> showFooter= "+showFooter);
        this.mShowFooter=showFooter;
    }

    public boolean isShowFooter() {
         Log.d(TAG,TAG+" ----->>>isShowFooter() ");
        return this.mShowFooter;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTitle;
        public TextView mDesc;
        public ImageView mNewsImg;


        public ItemViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tvTitle);
            mDesc = (TextView) v.findViewById(R.id.tvDesc);
            mNewsImg = (ImageView) v.findViewById(R.id.ivNews);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(v,this.getPosition());
            }
        }
    }

}
