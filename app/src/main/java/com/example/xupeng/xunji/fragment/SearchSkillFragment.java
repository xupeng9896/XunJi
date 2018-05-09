package com.example.xupeng.xunji.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.activity.MainActivity;
import com.example.xupeng.xunji.activity.SearchSkillItemActivity;
import com.example.xupeng.xunji.adpter.SearchSkillRecyclerAdapter;
import com.example.xupeng.xunji.adpter.SearchSkillTopCardPagerAdapter;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.SearchSkillRecyclerItem;
import com.example.xupeng.xunji.bean.SearchSkillTopCard;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.utils.SearchSkillRecyclerItemDecoration;
import com.example.xupeng.xunji.utils.SearchSkillTopShadowTransformer;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XuPeng on 2017/11/30.
 */

public class SearchSkillFragment extends Fragment implements View.OnClickListener {

    private MyApplication myApplication;
    private RecyclerView mRecyclerView;
    private SearchSkillTopCardPagerAdapter mTopCardAdapter;
    private SearchSkillTopShadowTransformer mShadowTransformer;
    private SearchSkillRecyclerAdapter mRecyclerAdapter;
    private List<SearchSkillRecyclerItem> rvItemList;
    private List<SearchSkillTopCard> topCardList;
    private List<UserInfo> userList;
    private ViewPager mTopViewPager;
    private ImageView mLeftCard;
    private ImageView mRightCard;
    private Intent mIntent;

    private int currItem = 0;
    private int itemNum = 0;
    private boolean isPause = false;
    private final int MARK_TOP_CARD = 0;
    private final int MARK_RECYCLER_CARD = 1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myApplication=(MyApplication)context.getApplicationContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList = DataSupport.findAll(UserInfo.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_skill, container, false);
        mTopCardAdapter = new SearchSkillTopCardPagerAdapter(getActivity());

        initList();
        //寻技上面部分布局及响应
        mTopViewPager = (ViewPager) view.findViewById(R.id.top_viewpager);
        mLeftCard = (ImageView) view.findViewById(R.id.search_skill_top_left_card);
        mRightCard = (ImageView) view.findViewById(R.id.search_skill_top_right_card);

        mShadowTransformer = new SearchSkillTopShadowTransformer(mTopViewPager, mTopCardAdapter);
        mShadowTransformer.enableScaling(true);
        mTopViewPager.setAdapter(mTopCardAdapter);
        mTopViewPager.setPageTransformer(false, mShadowTransformer);
        mTopViewPager.setPageMargin(3);

        mLeftCard.setOnClickListener(this);
        mRightCard.setOnClickListener(this);

        //寻技下面部分布局及响应
        mRecyclerView = (RecyclerView) view.findViewById(R.id.search_skill_rv);
        //设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new SearchSkillRecyclerItemDecoration(6));
        //设置适配器
        mRecyclerAdapter = new SearchSkillRecyclerAdapter(getContext(), rvItemList);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mTopCardAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                //Toast.makeText(getActivity(),"点击CardView:"+position,Toast.LENGTH_SHORT).show();
                mIntent = new Intent(getActivity(), SearchSkillItemActivity.class);
                mIntent.putExtra("Mark", MARK_TOP_CARD);
                mIntent.putExtra("ArticleId", topCardList.get(position).getArticleId());
                startActivity(mIntent);
            }

            @Override
            public void onLoveClickListener(int position, boolean state) {

            }
        });

        mRecyclerAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                //Toast.makeText(getActivity(), "item序号：" + position, Toast.LENGTH_SHORT).show();
                mIntent = new Intent(getActivity(), SearchSkillItemActivity.class);
                mIntent.putExtra("Mark", MARK_RECYCLER_CARD);
                mIntent.putExtra("ArticleId", rvItemList.get(position).getArticleId());
                startActivity(mIntent);
            }

            @Override
            public void onLoveClickListener(int position, boolean state) {

            }
        });

        return view;
    }

    private void initList() {
        topCardList = DataSupport.findAll(SearchSkillTopCard.class);
        rvItemList = DataSupport.findAll(SearchSkillRecyclerItem.class);
        if (topCardList.size() == 0) {
            topCardList = new ArrayList<>();
            topCardList.add(new SearchSkillTopCard(1, "v" + 1,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "skill_top_card_image.jpg",
                    "湘绣", "湖南刺绣"));
            topCardList.add(new SearchSkillTopCard(1, "v" + 2,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "skill_top_card_image1.jpg",
                    "茶叶罐", "百猴图陶瓷茶叶罐"));
            topCardList.add(new SearchSkillTopCard(1, "v" + 3,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "skill_top_card_image2.jpg",
                    "手账套装", "日中有乌旅行手账套装"));
            topCardList.add(new SearchSkillTopCard(1, "v" + 4,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "skill_top_card_image3.jpg",
                    "国品茅台", "贵州原生精品茅台"));
            DataSupport.saveAll(topCardList);
        }
        if (rvItemList.size() == 0) {
            rvItemList = new ArrayList<>();
            rvItemList.add(new SearchSkillRecyclerItem(1, "s" + 1,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "skill_top_card_image1.jpg",
                    "白猴图陶瓷茶叶罐"));
            rvItemList.add(new SearchSkillRecyclerItem(1, "s" + 2,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "skill_top_card_image2.jpg",
                    "日中有乌旅行手账套装"));
            rvItemList.add(new SearchSkillRecyclerItem(1, "s" + 3,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "skill_top_card_image3.jpg",
                    "贵州原生精品茅台"));
            DataSupport.saveAll(rvItemList);
        }
        for (int i = 0; i < topCardList.size(); i++) {
            mTopCardAdapter.addCardItem(topCardList.get(i));
        }

    }

    private int getNumFromString(String string) {

        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        int num = Integer.parseInt(m.replaceAll("").trim());
        return num;
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isPause) {
            isPause=false;
            if(myApplication.getPublishIndex()==2) {
                SearchSkillTopCard card = DataSupport.findLast(SearchSkillTopCard.class);
                int bannerIndex = getNumFromString(card.getArticleId());
                if (bannerIndex > topCardList.size()) {
                    mTopCardAdapter.addCardItem(card);
                }
                /*SearchSkillRecyclerItem item = DataSupport.findLast(SearchSkillRecyclerItem.class);
                int index = getNumFromString(item.getArticleId());
                if (index > rvItemList.size()) {
                    rvItemList.add(item);
                    mRecyclerAdapter.addData(index);
                }*/
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_skill_top_left_card:
                currItem = mTopViewPager.getCurrentItem();
                if (currItem != -1)
                    mTopViewPager.setCurrentItem(currItem - 1);
                break;

            case R.id.search_skill_top_right_card:
                currItem = mTopViewPager.getCurrentItem();
                itemNum = mTopCardAdapter.getCount();
                if (currItem != itemNum)
                    mTopViewPager.setCurrentItem(currItem + 1);
                break;

            default:
                break;
        }
    }
}
