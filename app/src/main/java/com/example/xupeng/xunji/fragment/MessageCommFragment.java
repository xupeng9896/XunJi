package com.example.xupeng.xunji.fragment;

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
import com.example.xupeng.xunji.adpter.MessageCenterCommAdapter;
import com.example.xupeng.xunji.bean.MessageCenterCommCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2018/3/12.
 */

public class MessageCommFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private MessageCenterCommAdapter mAdapter;
    private List<MessageCenterCommCard> commCardList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.message_center_fragment,container,false);

        initList();
        mRecyclerView=(RecyclerView)view.findViewById(R.id.message_center_rv);
        mAdapter=new MessageCenterCommAdapter(getActivity(),commCardList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void initList(){

        commCardList=new ArrayList<>();

        commCardList.add(new MessageCenterCommCard("互动消息1","互动内容1"));
        commCardList.add(new MessageCenterCommCard("互动消息2","互动内容2"));
        commCardList.add(new MessageCenterCommCard("互动消息3","互动内容3"));
        commCardList.add(new MessageCenterCommCard("互动消息4","互动内容4"));
        commCardList.add(new MessageCenterCommCard("互动消息5","互动内容5"));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
