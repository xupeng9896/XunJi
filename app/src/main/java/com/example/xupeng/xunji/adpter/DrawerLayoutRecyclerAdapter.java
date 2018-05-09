package com.example.xupeng.xunji.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.DrawerLayoutRecyclerItem;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by xupeng on 2018/1/15.
 */

public class DrawerLayoutRecyclerAdapter extends RecyclerView.Adapter<DrawerLayoutRecyclerAdapter.ViewHolder>
            implements View.OnClickListener{

    private OnRecyclerViewItemClickListener itemClickListener;
    private Context context;
    private List<DrawerLayoutRecyclerItem> dataList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView rvImage;
        TextView rvTitle;

        public ViewHolder(View view) {
            super(view);
            rvImage = (ImageView) view.findViewById(R.id.drawer_layout_item_image);
            rvTitle = (TextView) view.findViewById(R.id.drawer_layout_item_title);
        }
    }

    public DrawerLayoutRecyclerAdapter(Context context, List<DrawerLayoutRecyclerItem> list){
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
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
         viewHolder.rvImage.setImageResource(dataList.get(i).getImageId());
         viewHolder.rvTitle.setText(dataList.get(i).getTitle());

         viewHolder.itemView.setTag(i);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.drawer_layout_rv_item,
                viewGroup,false);
        view.setOnClickListener(this);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
