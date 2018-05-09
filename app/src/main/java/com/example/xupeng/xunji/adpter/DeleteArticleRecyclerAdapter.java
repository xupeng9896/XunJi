package com.example.xupeng.xunji.adpter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.angel.view.SWImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.DeleteArticleRecyclerItem;
import com.example.xupeng.xunji.bean.MainPageRecyclerItem;
import com.example.xupeng.xunji.bean.SearchRecordCard;
import com.example.xupeng.xunji.bean.SearchSkillTopCard;
import com.example.xupeng.xunji.bean.SearchTrackRecyclerItem;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by xupeng on 2018/3/12.
 */

public class DeleteArticleRecyclerAdapter extends RecyclerView.Adapter<DeleteArticleRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<DeleteArticleRecyclerItem> dataList;

    private OnRecyclerViewItemClickListener itemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView style;
        Button delete;
        ImageView rvImage;
        SWImageView authorImage;
        TextView authorName;
        TextView title;
        TextView content;

        public ViewHolder(View view) {
            super(view);
            style = (TextView) view.findViewById(R.id.delete_article_style);
            delete = (Button) view.findViewById(R.id.delete_article_btn);
            rvImage = (ImageView) view.findViewById(R.id.delete_article_image);
            authorImage = (SWImageView) view.findViewById(R.id.delete_article_user_image);
            authorName = (TextView) view.findViewById(R.id.delete_article_username);
            title = (TextView) view.findViewById(R.id.delete_article_title);
            content = (TextView) view.findViewById(R.id.delete_article_content);
        }
    }

    public DeleteArticleRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setDataList(List<DeleteArticleRecyclerItem> list){
        this.dataList=list;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final String style = dataList.get(i).getStyle();
        viewHolder.style.setText(style);
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
            UserInfo user = DataSupport.find(UserInfo.class, id);
            if (user != null) {
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
        String content = dataList.get(i).getContent();
        if (content != null && !content.equals("")) {
            viewHolder.content.setText(content);
        } else {
            viewHolder.content.setText("内容");
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String articleId = dataList.get(i).getArticleId();
                Log.d("deleteArticle", "articleId:"+articleId);
                switch (style) {
                    case "首页列表文章":
                        DataSupport.deleteAll(MainPageRecyclerItem.class,"articleId=?",articleId);
                        break;

                    case "寻记列表卡片":
                        DataSupport.deleteAll(SearchRecordCard.class,"articleId=?",articleId);
                        break;

                    case "寻技上方卡片":
                        DataSupport.deleteAll(SearchSkillTopCard.class,"articleId=?",articleId);
                        break;

                    case "寻迹列表文章":
                        DataSupport.deleteAll(SearchTrackRecyclerItem.class,"articleId=?",articleId);
                        break;

                    default:
                        break;
                }
                removeData(i);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.delete_article_rv_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void updateData(List<DeleteArticleRecyclerItem> list) {
        this.dataList = list;
        notifyDataSetChanged();
    }

    //  删除数据
    private void removeData(int position) {
        dataList.remove(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
