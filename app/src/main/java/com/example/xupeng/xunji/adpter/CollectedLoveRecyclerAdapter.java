package com.example.xupeng.xunji.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.angel.view.SWImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.CollectedLoveRecyclerItem;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by xupeng on 2018/3/12.
 */

public class CollectedLoveRecyclerAdapter extends RecyclerView.Adapter<CollectedLoveRecyclerAdapter.ViewHolder>
        implements View.OnClickListener {

    private Context context;
    private List<CollectedLoveRecyclerItem> dataList;

    private OnRecyclerViewItemClickListener itemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView style;
        ImageView love;
        ImageView comment;
        ImageView share;
        ImageView rvImage;
        SWImageView authorImage;
        TextView authorName;
        TextView title;
        TextView content;

        public ViewHolder(View view) {
            super(view);
            style = (TextView) view.findViewById(R.id.collected_style);
            love = (ImageView) view.findViewById(R.id.collected_love);
            comment = (ImageView) view.findViewById(R.id.collected_comment);
            share = (ImageView) view.findViewById(R.id.collected_share);
            rvImage = (ImageView) view.findViewById(R.id.collected_image);
            authorImage = (SWImageView) view.findViewById(R.id.collected_user_image);
            authorName = (TextView) view.findViewById(R.id.collected_username);
            title = (TextView) view.findViewById(R.id.collected_title);
            content = (TextView) view.findViewById(R.id.collected_content);
        }
    }

    public CollectedLoveRecyclerAdapter(Context context, List<CollectedLoveRecyclerItem> list) {
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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final String style = dataList.get(i).getStyle();
        viewHolder.style.setText(style);
        viewHolder.love.setImageResource(R.mipmap.loved);
        Glide.with(context)
                .load(dataList.get(i).getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.error))
                .into(viewHolder.rvImage);
        //获取文章用户Id
        Integer id = dataList.get(i).getAuthorId();
        String userImageUrl = "";
        String username = "";
        //如果Id不为null，则获取该用户的头像和昵称
        if (id != null) {
            //Log.d("cloveAdapter", "id:"+id);
            UserInfo user=DataSupport.find(UserInfo.class,id);
            if(user!=null) {
                userImageUrl = user.getImageUrl();
                //Log.d("cloveAdapter", "imageUrl:" + userImageUrl);
                username = user.getUsername();
                //Log.d("cloveAdapter", "username:" + username);
            }
        }
        if (!userImageUrl.equals("")) {
            Glide.with(context)
                    .load(userImageUrl)
                    .apply(new RequestOptions()
                            .placeholder(R.mipmap.loading)
                            .error(R.mipmap.error))
                    .into(viewHolder.authorImage);
        }
        viewHolder.authorName.setText(username);
        viewHolder.title.setText(dataList.get(i).getTitle());
        viewHolder.content.setText(dataList.get(i).getContent());

        if (itemClickListener != null) {
            viewHolder.love.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (style.equals("首页列表文章")) {
                        itemClickListener.onLoveClickListener(i, true);
                    } else if (style.equals("寻记列表卡片")) {
                        itemClickListener.onLoveClickListener(i, false);
                    }
                    removeData(i);
                }
            });
        }

        viewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        viewHolder.itemView.setTag(i);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.collected_love_item,
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

    public void updateData(List<CollectedLoveRecyclerItem> list){
        this.dataList=list;
        notifyDataSetChanged();
    }

    public void addData(Object object,int position) {
//      在list中添加数据，并通知条目加入一条
        //dataList.add(card);
        //添加动画
        notifyItemInserted(position);
    }
    //  删除数据
    private void removeData(int position) {
        dataList.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
