package com.example.xupeng.xunji.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angel.view.SWImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.SearchSkillRecyclerItem;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by xupeng on 2018/1/15.
 */

public class SearchSkillRecyclerAdapter extends RecyclerView.Adapter<SearchSkillRecyclerAdapter.ViewHolder>
            implements View.OnClickListener{

    private OnRecyclerViewItemClickListener itemClickListener;
    private Context context;
    private List<SearchSkillRecyclerItem> dataList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        SWImageView rvImage;
        TextView rvTitle;

        public ViewHolder(View view) {
            super(view);
            rvImage = (SWImageView) view.findViewById(R.id.search_skill_rv_item_image);
            rvTitle = (TextView) view.findViewById(R.id.search_skill_rv_item_title);
        }
    }

    public SearchSkillRecyclerAdapter(Context context, List<SearchSkillRecyclerItem> list){
          this.context=context;
          dataList=list;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        itemClickListener=listener;
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener!=null){
            itemClickListener.onItemClickListener(v,(int)v.getTag());
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        int position=getItemCount()-1-i;
        Glide.with(context)
                .load(dataList.get(position).getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.error))
                .into(viewHolder.rvImage);
        viewHolder.rvTitle.setText(dataList.get(position).getTitle());

         viewHolder.itemView.setTag(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_skill_rv_item,
                viewGroup,false);
        view.setOnClickListener(this);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void addData(SearchSkillRecyclerItem item,int position) {
        //在list中添加数据，并通知条目加入一条
        dataList.add(item);
        //添加动画
        notifyItemInserted(position);
    }
}
