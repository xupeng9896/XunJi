package com.example.xupeng.xunji.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.adpter.MainPageRecyclerAdapter;
import com.example.xupeng.xunji.bean.MainPageRecyclerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2018/4/4.
 */

public class CollectedPublishFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MainPageRecyclerAdapter mAdapter;

    private Intent mIntent;
    private List<MainPageRecyclerItem> rvItemList;
    private final int RECYCLER_MARK=1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.collected_fragment,container,false);
        initItemList();
        initView(view);
        return view;
    }

    private void initView(View view){

        mRecyclerView=(RecyclerView)view.findViewById(R.id.collected_rv);

        //配置RecyclerView
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        /*mRecyclerView.addItemDecoration(new SearchTrackRecyclerItemDecoration(10));
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter=new MainPageRecyclerAdapter(getContext(),rvItemList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Toast.makeText(getActivity(),"item序号："+position,Toast.LENGTH_SHORT).show();
                mIntent=new Intent(getActivity(), MainRecyclerItemActivity.class);
                mIntent.putExtra("Mark",RECYCLER_MARK);
                mIntent.putExtra("Title",rvItemList.get(position).getTitle());
                *//*mIntent.putExtra("UserImageId",rvItemList.get(position).getUserImageId());
                mIntent.putExtra("ContentImageId",rvItemList.get(position).getContentImageId());*//*
                mIntent.putExtra("Content",rvItemList.get(position).getContent());
                mIntent.putExtra("Time",rvItemList.get(position).getTime());
                startActivity(mIntent);
            }

            @Override
            public void onWidgetClickListener(int position) {

            }
        });*/
    }

    private void initItemList(){
        rvItemList=new ArrayList<>();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
