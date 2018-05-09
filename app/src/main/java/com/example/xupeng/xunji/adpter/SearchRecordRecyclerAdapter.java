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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.SearchRecordCard;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by xupeng on 2018/3/12.
 */

public class SearchRecordRecyclerAdapter extends RecyclerView.Adapter<SearchRecordRecyclerAdapter.ViewHolder>
        implements View.OnClickListener {

    private Context context;
    private List<SearchRecordCard> dataList;
    private boolean loveState = false;

    private OnRecyclerViewItemClickListener itemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardAuthorImage;
        TextView cardTitle;
        ImageView cardImage;
        ImageView cardShare;
        TextView cardLocationText;
        ImageView cardComment;
        ImageView cardLove;

        public ViewHolder(View view) {
            super(view);
            cardAuthorImage = (ImageView) view.findViewById(R.id.search_record_author_image);
            cardTitle = (TextView) view.findViewById(R.id.search_record_title);
            cardImage = (ImageView) view.findViewById(R.id.search_record_card_image);
            cardShare = (ImageView) view.findViewById(R.id.search_record_share);
            cardLocationText = (TextView) view.findViewById(R.id.search_record_location_tv);
            cardComment = (ImageView) view.findViewById(R.id.search_record_comment);
            cardLove = (ImageView) view.findViewById(R.id.search_record_love);
        }
    }

    public SearchRecordRecyclerAdapter(Context context, List<SearchRecordCard> list) {
        this.context = context;
        dataList = list;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        itemClickListener = listener;
    }

    @Override
    public void onClick(View v) {

        if (itemClickListener != null) {
            itemClickListener.onItemClickListener(v, (int) v.getTag());
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        //不使用 使用3个参数的重载函数
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            //payloads即有效负载，当首次加载或调用notifyDatasetChanged() ,notifyItemChange(int position)进行刷新时，payloads为empty 即空
            Integer id=dataList.get(position).getAuthorId();
            UserInfo user= DataSupport.find(UserInfo.class,id);
            if(user!=null){
                Glide.with(context).load(user.getImageUrl()).into(holder.cardAuthorImage);
            }else {
                holder.cardAuthorImage.setImageResource(R.mipmap.head_image2);
            }
            Glide.with(context)
                    .load(dataList.get(position).getImageUrl())
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.loading)
                            .error(R.mipmap.error))
                    .into(holder.cardImage);
            holder.cardTitle.setText(dataList.get(position).getTitle());
            holder.cardLocationText.setText(dataList.get(position).getLocation());
            boolean state=dataList.get(position).isLoveState();
            if(state){
                holder.cardLove.setImageResource(R.mipmap.loved);
                loveState=true;
            }else {
                holder.cardLove.setImageResource(R.mipmap.love);
                loveState=false;
            }

            holder.cardShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "点击Share:" + position, Toast.LENGTH_SHORT).show();
                    showShare();
                }
            });
            holder.cardComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("adapter", "onClickComment: " + position);
                }
            });
            if (itemClickListener != null) {
                holder.cardLove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("adapter", "clickLove1:" + position);
                        if (!loveState) {
                            Log.d("adapter", "clickLove2:"+loveState);
                            holder.cardLove.setImageResource(R.mipmap.loved);
                            itemClickListener.onLoveClickListener(position, true);
                            loveState=true;
                        } else {
                            Log.d("adapter", "clickLove3:"+loveState);
                            holder.cardLove.setImageResource(R.mipmap.love);
                            itemClickListener.onLoveClickListener(position, false);
                            loveState=false;
                        }
                        notifyItemChanged(position,0);
                    }
                });
            }
        }else {
            //当调用notifyItemChange(int position, Object payload)进行布局刷新时，payloads不会empty ，所以真正的布局刷新应该在这里实现 重点！
            if (itemClickListener != null) {
                holder.cardLove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("adapter", "clickLove4:" + position);
                        if (!loveState) {
                            Log.d("adapter", "clickLove5:"+loveState);
                            holder.cardLove.setImageResource(R.mipmap.loved);
                            itemClickListener.onLoveClickListener(position, true);
                            loveState=true;
                        } else {
                            Log.d("adapter", "clickLove6:"+loveState);
                            holder.cardLove.setImageResource(R.mipmap.love);
                            itemClickListener.onLoveClickListener(position, false);
                            loveState=false;
                        }
                        notifyItemChanged(position,0);
                    }
                });
            }
        }
        holder.itemView.setTag(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_record_item,
                viewGroup, false);
        view.setOnClickListener(this);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private void showShare() {

        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        //oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(context);
    }

    public void updateList(List<SearchRecordCard> list){
        this.dataList=list;
        notifyDataSetChanged();
    }

    public void addData(SearchRecordCard card,int position) {
        //在list中添加数据，并通知条目加入一条
        dataList.add(card);
        //添加动画
        notifyItemInserted(position);
    }

    //  删除数据
    public void removeData(int position) {
        dataList.remove(position);
        //删除动画
        notifyItemRemoved(position);
        //notifyDataSetChanged();
    }
}
