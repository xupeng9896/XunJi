package com.example.xupeng.xunji.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.adpter.DeleteArticleRecyclerAdapter;
import com.example.xupeng.xunji.bean.DeleteArticleRecyclerItem;
import com.example.xupeng.xunji.bean.MainPageRecyclerItem;
import com.example.xupeng.xunji.bean.SearchRecordCard;
import com.example.xupeng.xunji.bean.SearchSkillTopCard;
import com.example.xupeng.xunji.bean.SearchTrackRecyclerItem;
import com.example.xupeng.xunji.utils.SearchTrackRecyclerItemDecoration;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class ManagerDeleteArticleActivity extends AppCompatActivity {

    private List<DeleteArticleRecyclerItem> rvItemList;
    private Spinner spinner;
    private RecyclerView mRecyclerView;
    private DeleteArticleRecyclerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_article);
        spinner=(Spinner)findViewById(R.id.style_delete_article);
        mRecyclerView=(RecyclerView)findViewById(R.id.delete_article_rv);

        Toolbar toolbar=(Toolbar)findViewById(R.id.page_module_toolbar);

        toolbar.setTitle(this.getString(R.string.delete_article_title));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.addItemDecoration(new SearchTrackRecyclerItemDecoration(10));
        mRecyclerView.setLayoutManager(layoutManager);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position,true);
                setDeleteArticleList(ManagerDeleteArticleActivity.this
                        .getResources()
                        .getStringArray(R.array.publishArticleArray)[position]);
                if(rvItemList!=null) {
                    mAdapter = new DeleteArticleRecyclerAdapter(getBaseContext());
                    mAdapter.setDataList(rvItemList);
                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setDeleteArticleList(String style){
        /*<item>首页列表文章</item>
        <item>寻记列表卡片</item>
        <item>寻技上方卡片</item>
        <item>寻迹列表文章</item>*/
        switch (style){
            case "首页列表文章":
                rvItemList=new ArrayList<>();
                List<MainPageRecyclerItem> list= DataSupport.findAll(MainPageRecyclerItem.class);
                for(int i=0;i<list.size();i++){
                    DeleteArticleRecyclerItem item=new DeleteArticleRecyclerItem();
                    item.setArticleId(list.get(i).getArticleId());
                    item.setStyle("首页列表文章");
                    item.setImageUrl(list.get(i).getImageUrl());
                    item.setAuthorId(list.get(i).getAuthorId());
                    item.setTitle(list.get(i).getTitle());
                    item.setContent(list.get(i).getContent());
                    rvItemList.add(item);
                }
                break;

            case "寻记列表卡片":
                rvItemList=new ArrayList<>();
                List<SearchRecordCard> list1= DataSupport.findAll(SearchRecordCard.class);
                for(int i=0;i<list1.size();i++){
                    DeleteArticleRecyclerItem item=new DeleteArticleRecyclerItem();
                    item.setArticleId(list1.get(i).getArticleId());
                    item.setStyle("寻记列表卡片");
                    item.setImageUrl(list1.get(i).getImageUrl());
                    item.setAuthorId(list1.get(i).getAuthorId());
                    item.setTitle(list1.get(i).getTitle());
                    //item.setContent(list1.get(i).getContent());
                    rvItemList.add(item);
                }
                break;

            case "寻技上方卡片":
                rvItemList=new ArrayList<>();
                List<SearchSkillTopCard> list2= DataSupport.findAll(SearchSkillTopCard.class);
                for(int i=0;i<list2.size();i++){
                    DeleteArticleRecyclerItem item=new DeleteArticleRecyclerItem();
                    item.setArticleId(list2.get(i).getArticleId());
                    item.setStyle("寻技上方卡片");
                    item.setImageUrl(list2.get(i).getImageUrl());
                    item.setAuthorId(list2.get(i).getAuthorId());
                    item.setTitle(list2.get(i).getTitle());
                    item.setContent(list2.get(i).getContent());
                    rvItemList.add(item);
                }
                break;

            case "寻迹列表文章":
                rvItemList=new ArrayList<>();
                List<SearchTrackRecyclerItem> list3= DataSupport.findAll(SearchTrackRecyclerItem.class);
                for(int i=0;i<list3.size();i++){
                    DeleteArticleRecyclerItem item=new DeleteArticleRecyclerItem();
                    item.setArticleId(list3.get(i).getArticleId());
                    item.setStyle("寻迹列表文章");
                    item.setImageUrl(list3.get(i).getImageUrl());
                    item.setAuthorId(list3.get(i).getAuthorId());
                    item.setTitle(list3.get(i).getTitle());
                    //item.setContent(list3.get(i).getContent());
                    rvItemList.add(item);
                }
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
