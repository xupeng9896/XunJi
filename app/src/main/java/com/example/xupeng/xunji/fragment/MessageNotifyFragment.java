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
import com.example.xupeng.xunji.adpter.MessageCenterNotifyAdapter;
import com.example.xupeng.xunji.bean.MessageCenterNotifyCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2018/3/12.
 */

public class MessageNotifyFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MessageCenterNotifyAdapter mAdapter;
    private List<MessageCenterNotifyCard> notifyCardList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.message_center_fragment,container,false);

        initList();
        mRecyclerView=(RecyclerView)view.findViewById(R.id.message_center_rv);
        mAdapter=new MessageCenterNotifyAdapter(getActivity(),notifyCardList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void initList(){

        notifyCardList=new ArrayList<>();

        notifyCardList.add(new MessageCenterNotifyCard("通知消息1","通知内容1"));
        notifyCardList.add(new MessageCenterNotifyCard("通知消息2","通知内容2"));
        notifyCardList.add(new MessageCenterNotifyCard("通知消息3","通知内容3"));
        notifyCardList.add(new MessageCenterNotifyCard("通知消息4","通知内容4"));
        notifyCardList.add(new MessageCenterNotifyCard("通知消息5","通知内容5"));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
