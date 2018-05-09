package com.example.xupeng.xunji.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angel.view.SWImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.MainPageRecyclerItem;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by xupeng on 2018/3/17.
 */

public class MainPageRecyclerAdapter extends RecyclerView.Adapter<MainPageRecyclerAdapter.ViewHolder>
        implements View.OnClickListener{

    private OnRecyclerViewItemClickListener itemClickListener;
    private Context context;
    private List<MainPageRecyclerItem> dataList;
    private boolean loveState = false;

    static class ViewHolder extends RecyclerView.ViewHolder{
        SWImageView authorImage;
        TextView rvTitle;
        TextView rvContent;
        TextView rvTime;
        ImageView rvImage;
        ImageView love;

        public ViewHolder(View view) {
            super(view);
            authorImage = (SWImageView) view.findViewById(R.id.authorImage);
            rvTitle = (TextView) view.findViewById(R.id.textTitle);
            rvContent=(TextView)view.findViewById(R.id.textContent);
            rvTime=(TextView)view.findViewById(R.id.textTime);
            rvImage=(ImageView)view.findViewById(R.id.content_image);
            love=(ImageView)view.findViewById(R.id.main_love);
        }
    }

    public MainPageRecyclerAdapter(Context context, List<MainPageRecyclerItem> list){
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
    public void onBindViewHolder(final ViewHolder viewHolder,final int i) {

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()) {
            //payloads即有效负载，当首次加载或调用notifyDatasetChanged() ,notifyItemChange(int position)进行刷新时，payloads为empty 即空
            Integer id=dataList.get(position).getAuthorId();
            UserInfo user= DataSupport.find(UserInfo.class,id);
            if(user!=null){
                Glide.with(context).load(user.getImageUrl()).into(holder.authorImage);
            }else {
                holder.authorImage.setImageResource(R.mipmap.head_image2);
            }
            holder.rvTitle.setText(dataList.get(position).getTitle());
            holder.rvContent.setText(dataList.get(position).getContent());
            Glide.with(context)
                    .load(dataList.get(position).getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.loading)
                            .error(R.mipmap.error))
                    .into(holder.rvImage);
            holder.rvTime.setText(dataList.get(position).getTime().toString());

            boolean state=dataList.get(position).isLoveState();
            if(state){
                holder.love.setImageResource(R.mipmap.loved);
                loveState=true;
            }else {
                holder.love.setImageResource(R.mipmap.love);
                loveState=false;
            }

            if (itemClickListener != null) {
                holder.love.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("adapter", "clickLove1:" + position);
                        if (!loveState) {
                            //Log.d("adapter", "clickLove2:"+loveState);
                            holder.love.setImageResource(R.mipmap.loved);
                            itemClickListener.onLoveClickListener(position, true);
                            loveState = true;
                        } else {
                            Log.d("adapter", "clickLove3:" + loveState);
                            holder.love.setImageResource(R.mipmap.love);
                            itemClickListener.onLoveClickListener(position, false);
                            loveState = false;
                        }
                        notifyItemChanged(position, 0);
                    }
                });
            }
        }else {
            //当调用notifyItemChange(int position, Object payload)进行布局刷新时，payloads不会empty ，所以真正的布局刷新应该在这里实现 重点！
            if (itemClickListener != null) {
                holder.love.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("adapter", "clickLove4:" + position);
                        if (!loveState) {
                            Log.d("adapter", "clickLove5:"+loveState);
                            holder.love.setImageResource(R.mipmap.loved);
                            itemClickListener.onLoveClickListener(position, true);
                            loveState=true;
                        } else {
                            Log.d("adapter", "clickLove6:"+loveState);
                            holder.love.setImageResource(R.mipmap.love);
                            itemClickListener.onLoveClickListener(position, false);
                            loveState=false;
                        }
                        notifyItemChanged(position,0);
                    }
                });
            }
        }
        //给view设置tag以作为参数传递到监听回调方法中
        holder.itemView.setTag(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.main_rv_item,
                viewGroup,false);
        //为item设置点击监听器
        view.setOnClickListener(this);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateList(List<MainPageRecyclerItem> list){
        this.dataList=list;
        notifyDataSetChanged();
    }
    public void addData(MainPageRecyclerItem item,int position) {
        //在list中添加数据，并通知条目加入一条
        dataList.add(item);
        //添加动画
        notifyItemInserted(position);
    }
}
