package com.example.xupeng.xunji.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.activity.MainActivity;
import com.example.xupeng.xunji.activity.RecordGenealogyOrderActivity;
import com.example.xupeng.xunji.activity.RecordGenealogyShowActivity;
import com.example.xupeng.xunji.activity.SearchRecordItemActivity;
import com.example.xupeng.xunji.adpter.SearchRecordRecyclerAdapter;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.SearchRecordCard;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by XuPeng on 2017/11/30.
 */

public class SearchRecordFragment extends Fragment implements View.OnClickListener {

    private MyApplication myApplication;
    private RecyclerView mRecyclerView;
    private List<SearchRecordCard> srCardList;
    private SearchRecordRecyclerAdapter mAdapter;
    private LinearLayout mOrder,mShow;
    private Intent mIntent;
    private boolean isPause=false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myApplication=(MyApplication)context.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.activity_search_record,container,false);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.search_record_rv);
        mOrder=(LinearLayout)view.findViewById(R.id.genealogy_order);
        mShow=(LinearLayout)view.findViewById(R.id.genealogy_show);

        initCard();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        //列表在底部开始展示，反转后由上面开始展示
        layoutManager.setStackFromEnd(true);
        //列表翻转
        layoutManager.setReverseLayout(true);
        mAdapter=new SearchRecordRecyclerAdapter(getActivity(),srCardList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mShow.setOnClickListener(this);
        mOrder.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                mIntent=new Intent(getActivity(), SearchRecordItemActivity.class);
                mIntent.putExtra("ArticleId",srCardList.get(position).getArticleId());
                startActivity(mIntent);
            }

            @Override
            public void onLoveClickListener(int position, boolean state) {
                String articleId="r"+(position+1);
                SearchRecordCard card=srCardList.get(position);
                Log.d("searchRecord", "clickLove7: "+state);
                if(state){
                    card.setLoveState(state);
                }else {
                    card.setToDefault("loveState");
                }
                card.updateAll("articleId=?",articleId);
            }
        });

        return view;
    }

    private void initCard(){
        srCardList=DataSupport.findAll(SearchRecordCard.class);
        if(srCardList.size()==0){
            srCardList=new ArrayList<>();
            srCardList.add(new SearchRecordCard(1, "r"+1,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "village3.jpg",
                    "李鸿章后代李氏家训", "安徽合肥",false));
            srCardList.add(new SearchRecordCard(1, "r"+2,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "village4.jpg",
                    "张谷英村张氏祭祖", "湖南岳阳",false));
            srCardList.add(new SearchRecordCard(1, "r"+3,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "village5.jpg",
                    "鱼镇石门傍晚时分", "河北石家庄",false));
            DataSupport.saveAll(srCardList);
        }
    }

    private int getNumFromString(String string){

        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        int num=Integer.parseInt(m.replaceAll("").trim());
        return num;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.genealogy_order:
                mIntent=new Intent(getActivity(), RecordGenealogyOrderActivity.class);
                startActivity(mIntent);
                break;

            case R.id.genealogy_show:
                mIntent=new Intent(getActivity(), RecordGenealogyShowActivity.class);
                startActivity(mIntent);
                break;

            default:
                break;
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
            //获取收藏列表点击取消的Record card的id
            if(!myApplication.isRecordLove()){
                srCardList=DataSupport.findAll(SearchRecordCard.class);
                mAdapter.updateList(srCardList);
            }
            if(myApplication.getPublishIndex()==1) {
                SearchRecordCard card = DataSupport.findLast(SearchRecordCard.class);
                int index = getNumFromString(card.getArticleId());
                if (index > srCardList.size()) {
                    mAdapter.addData(card, index);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
