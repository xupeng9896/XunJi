package com.example.xupeng.xunji.adpter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.SearchSkillTopCard;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;
import com.example.xupeng.xunji.imp.SearchSkillTopCardAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2018/3/14.
 */

public class SearchSkillTopCardPagerAdapter extends PagerAdapter
        implements SearchSkillTopCardAdapter,View.OnClickListener{

    private OnRecyclerViewItemClickListener itemClickListener;
    private List<CardView> mCardViews;
    private List<SearchSkillTopCard> mCardItems;
    private Context context;
    private float mBaseElevation;


    public SearchSkillTopCardPagerAdapter(Context context){
        mCardViews=new ArrayList<>();
        mCardItems=new ArrayList<>();
        this.context=context;
    }

    public void addCardItem(SearchSkillTopCard searchSkillTopCard){
        mCardViews.add(null);
        mCardItems.add(searchSkillTopCard);
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

    public void bind(SearchSkillTopCard searchRecordCard, View view){

        ImageView topCardImage=(ImageView)view.findViewById(R.id.search_skill_top_image);
        TextView topCardAuthor=(TextView)view.findViewById(R.id.search_skill_top_author);
        TextView topCardTitle=(TextView)view.findViewById(R.id.search_skill_top_title);
        TextView topCardContent=(TextView)view.findViewById(R.id.search_skill_top_content);

        Glide.with(context)
                .load(searchRecordCard.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.error))
                .into(topCardImage);
        topCardTitle.setText(searchRecordCard.getTitle());
        topCardAuthor.setText("作者:"+searchRecordCard.getAuthorId());
        topCardContent.setText(searchRecordCard.getContent());


    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view= LayoutInflater.from(context).inflate(R.layout.search_skill_top_item,
                container,false);
        container.addView(view);
        bind(mCardItems.get(position),view);
        CardView cardView=(CardView)view.findViewById(R.id.search_skill_top_cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mCardViews.set(position,cardView);

        cardView.setTag(position);
        cardView.setOnClickListener(this);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
        mCardViews.set(position,null);

    }

    @Override
    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view==o;
    }

    @Override
    public int getCount() {
        return mCardItems.size();
    }


    @Override
    public CardView getCardViewAt(int position) {
        return mCardViews.get(position);
    }

}
