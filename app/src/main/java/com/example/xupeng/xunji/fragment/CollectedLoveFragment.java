package com.example.xupeng.xunji.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.activity.SearchRecordItemActivity;
import com.example.xupeng.xunji.adpter.CollectedLoveRecyclerAdapter;
import com.example.xupeng.xunji.bean.CollectedLoveRecyclerItem;
import com.example.xupeng.xunji.bean.MainPageRecyclerItem;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.SearchRecordCard;
import com.example.xupeng.xunji.utils.SearchTrackRecyclerItemDecoration;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2018/4/4.
 */

public class CollectedLoveFragment extends Fragment {

    private MyApplication myApplication;
    private List<CollectedLoveRecyclerItem> loveList;
    private List<SearchRecordCard> recordCardList;
    private List<MainPageRecyclerItem> mainItemList;
    private CollectedLoveRecyclerAdapter mAdapter;
    private boolean isPause=false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myApplication=(MyApplication)context.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collected_fragment, container, false);
        loveList=new ArrayList<>();

        initView(view);
        return view;
    }

    private void initView(View view) {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.collected_rv);

        getLoveTrueList();

        if (loveList != null) {
            //Log.d("test", "clickedLove:" + loveList.size());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayout.VERTICAL);
            mAdapter=new CollectedLoveRecyclerAdapter(getActivity(),loveList);
            recyclerView.addItemDecoration(new SearchTrackRecyclerItemDecoration(10));
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);

            mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    Intent intent = new Intent(getActivity(), SearchRecordItemActivity.class);
                    intent.putExtra("ArticleId",loveList.get(position).getArticleId());
                    startActivity(intent);
                }

                @Override
                public void onLoveClickListener(int position, boolean state) {
                    if(state){
                        String articleId =loveList.get(position).getArticleId();
                        List<MainPageRecyclerItem> itemList=DataSupport
                                .where("articleId=?", articleId)
                                .find(MainPageRecyclerItem.class);
                        if(itemList.size()>0) {
                            MainPageRecyclerItem item = itemList.get(0);
                            item.setToDefault("loveState");
                            item.updateAll("articleId=?", articleId);
                            mainItemList.remove(item);
                            myApplication.setMainLove(false);
                        }
                    }else {
                        String articleId=loveList.get(position).getArticleId();
                        List<SearchRecordCard> cardList=DataSupport
                                .where("articleId=?", articleId)
                                .find(SearchRecordCard.class);
                        if(cardList.size()>0) {
                            SearchRecordCard card = cardList.get(0);
                            card.setToDefault("loveState");
                            card.updateAll("articleId=?", articleId);
                            recordCardList.remove(card);
                            myApplication.setRecordLove(false);
                        }
                    }
                }
            });
        }

    }

    private void getLoveTrueList(){
        //获取寻记页面点击收藏的卡片列表
        recordCardList = DataSupport.select("articleId").where("loveState=?", "1").find(SearchRecordCard.class);
        //获取首页点击收藏的卡片列表
        mainItemList = DataSupport.select("articleId").where("loveState=?", "1").find(MainPageRecyclerItem.class);

        if(mainItemList.size()>0){
            for(int i=0;i<mainItemList.size();i++){
                initCollectedList("MainPageRecyclerItem",mainItemList.get(i).getArticleId());
            }
        }
        if(recordCardList.size()>0){
            for(int i=0;i<recordCardList.size();i++){
                initCollectedList("SearchRecordCard",recordCardList.get(i).getArticleId());
            }
        }
    }

    private void initCollectedList(String style,String articleId){

        if(style.equals("MainPageRecyclerItem")){
            List<MainPageRecyclerItem> list=DataSupport
                    .where("articleId=?",articleId)
                    .find(MainPageRecyclerItem.class);
            for(int i=0;i<list.size();i++){
                CollectedLoveRecyclerItem loveItem=new CollectedLoveRecyclerItem();
                loveItem.setArticleId(list.get(i).getArticleId());
                loveItem.setStyle("首页列表文章");
                loveItem.setLoveState(list.get(i).isLoveState());
                loveItem.setImageUrl(list.get(i).getImageUrl());
                loveItem.setAuthorId(list.get(i).getAuthorId());
                loveItem.setTitle(list.get(i).getTitle());
                loveItem.setContent(list.get(i).getContent());
                loveList.add(loveItem);
            }

        }
        if (style.equals("SearchRecordCard")){
            List<SearchRecordCard> list=DataSupport
                    .where("articleId=?",articleId)
                    .find(SearchRecordCard.class);
            for(int i=0;i<list.size();i++){
                CollectedLoveRecyclerItem loveItem=new CollectedLoveRecyclerItem();
                loveItem.setArticleId(list.get(i).getArticleId());
                loveItem.setStyle("寻记列表卡片");
                loveItem.setLoveState(list.get(i).isLoveState());
                loveItem.setImageUrl(list.get(i).getImageUrl());
                loveItem.setAuthorId(list.get(i).getAuthorId());
                loveItem.setTitle(list.get(i).getTitle());
                loveItem.setContent(list.get(i).getLocation());
                loveList.add(loveItem);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause=true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPause) {
            isPause=false;
            getLoveTrueList();
            mAdapter.updateData(loveList);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

