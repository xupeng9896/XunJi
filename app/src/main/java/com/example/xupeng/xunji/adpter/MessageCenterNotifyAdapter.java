package com.example.xupeng.xunji.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.MessageCenterNotifyCard;

import java.util.List;

/**
 * Created by xupeng on 2018/3/16.
 */

public class MessageCenterNotifyAdapter extends RecyclerView.Adapter<MessageCenterNotifyAdapter.ViewHolder>
        implements View.OnClickListener{

    private AdapterView.OnItemClickListener itemClickListener;
    private Context context;
    private List<MessageCenterNotifyCard> dataList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView title;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.notify_title);
            content = (TextView) view.findViewById(R.id.notify_content);
        }
    }

    public MessageCenterNotifyAdapter(Context context, List<MessageCenterNotifyCard> list){
        this.context=context;
        dataList=list;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        itemClickListener=listener;
    }

    @Override
    public void onClick(View v) {
        if(itemClickListener!=null){
            //itemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.title.setText(dataList.get(i).getTitle());
        viewHolder.content.setText(dataList.get(i).getContent());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.message_center_notify_item,
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
