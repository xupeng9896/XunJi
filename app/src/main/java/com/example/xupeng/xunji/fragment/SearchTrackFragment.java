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
import com.example.xupeng.xunji.activity.MainActivity;
import com.example.xupeng.xunji.activity.MapActivity;
import com.example.xupeng.xunji.activity.SearchTrackItemActivity;
import com.example.xupeng.xunji.activity.TrackVillageProtectActivity;
import com.example.xupeng.xunji.adpter.SearchTrackRecyclerAdapter;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.SearchTrackRecyclerItem;
import com.example.xupeng.xunji.utils.SearchTrackRecyclerItemDecoration;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XuPeng on 2017/11/30.
 */

public class SearchTrackFragment extends Fragment implements View.OnClickListener{

    private MyApplication myApplication;
    private RecyclerView mRecyclerView;
    private SearchTrackRecyclerAdapter mAdapter;

    private List<SearchTrackRecyclerItem> rvItemList;
    private boolean isPause=false;
    private Intent mIntent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myApplication=(MyApplication)context.getApplicationContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_search_track,container,false);

        LinearLayout nearVillage=(LinearLayout)view.findViewById(R.id.near_village);
        LinearLayout villageProtection=(LinearLayout)view.findViewById(R.id.village_protection);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.search_track_rv);

        initItemList();

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        //列表在底部开始展示，反转后由上面开始展示
        layoutManager.setStackFromEnd(true);
        //列表翻转
        layoutManager.setReverseLayout(true);
        mAdapter=new SearchTrackRecyclerAdapter(getActivity(),rvItemList);
        mRecyclerView.addItemDecoration(new SearchTrackRecyclerItemDecoration(8));
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                //Toast.makeText(getActivity(),"item序号："+position,Toast.LENGTH_SHORT).show();
                mIntent=new Intent(getActivity(), SearchTrackItemActivity.class);
                mIntent.putExtra("ArticleId",rvItemList.get(position).getArticleId());
                startActivity(mIntent);
            }

            @Override
            public void onLoveClickListener(int position, boolean state) {

            }
        });

        nearVillage.setOnClickListener(this);
        villageProtection.setOnClickListener(this);

        return view;
    }

    private void initItemList(){

        rvItemList=DataSupport.findAll(SearchTrackRecyclerItem.class);

        if(rvItemList.size()==0){
            rvItemList=new ArrayList<>();
            rvItemList.add(new SearchTrackRecyclerItem(1, "t"+1,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "village1.jpg", "金井镇"));
            rvItemList.add(new SearchTrackRecyclerItem(1, "t"+2,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "village3.jpg", "江永女书"));
            rvItemList.add(new SearchTrackRecyclerItem(1, "t"+3,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "village4.jpg", "浙水村"));
            rvItemList.add(new SearchTrackRecyclerItem(1, "t"+4,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "village5.jpg", "鱼镇石门"));
            DataSupport.saveAll(rvItemList);
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
    public void onPause() {
        super.onPause();
        isPause=true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPause) {
            isPause=false;
            if(myApplication.getPublishIndex()==3) {
                SearchTrackRecyclerItem item = DataSupport.findLast(SearchTrackRecyclerItem.class);
                int index = getNumFromString(item.getArticleId());
                if (index > rvItemList.size()) {
                    mAdapter.addData(item, index);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.near_village:
                mIntent=new Intent(getActivity(),MapActivity.class);
                startActivity(mIntent);
                break;

            case R.id.village_protection:
                mIntent=new Intent(getActivity(),TrackVillageProtectActivity.class);
                startActivity(mIntent);
                break;

            default:
                break;
        }
    }
}
