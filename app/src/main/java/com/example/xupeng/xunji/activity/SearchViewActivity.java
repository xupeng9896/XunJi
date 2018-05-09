package com.example.xupeng.xunji.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.MainBannerItem;
import com.example.xupeng.xunji.bean.MainPageRecyclerItem;
import com.example.xupeng.xunji.bean.SearchRecordCard;
import com.example.xupeng.xunji.bean.SearchSkillRecyclerItem;
import com.example.xupeng.xunji.bean.SearchSkillTopCard;
import com.example.xupeng.xunji.bean.SearchTrackRecyclerItem;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xupeng on 2018/4/2.
 */

public class SearchViewActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private ListView mListView;
    private List<String> titleList;
    private List<String> articleList;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        mContext=SearchViewActivity.this;
        titleList=new ArrayList<>();
        articleList=new ArrayList<>();
        setTitleList();

        Toolbar toolbar=(Toolbar)findViewById(R.id.page_module_toolbar);
        mSearchView = (SearchView) findViewById(R.id.search);
        mListView=(ListView)findViewById(R.id.search_result_lv);

        toolbar.setTitle(this.getString(R.string.search_view));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,titleList));
        mListView.setTextFilterEnabled(true);

        setSearchView();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivityWithArticleId(position);
                //Toast.makeText(mContext,"搜索选项点击第"+position+"项",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSearchView() {
        //设置SearchView
        mSearchView.setIconifiedByDefault(false);//设置展开后图标的样式,这里只有两种,一种图标在搜索框外,一种在搜索框内
        mSearchView.onActionViewExpanded();// 写上此句后searchView初始是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能出现输入框,也就是设置为ToolBar的ActionView，默认展开
        mSearchView.requestFocus();//输入焦点
        //mSearchView.setSubmitButtonEnabled(true);//添加提交按钮，监听在OnQueryTextListener的onQueryTextSubmit响应
        mSearchView.setIconified(false);//输入框内icon不显示
        mSearchView.requestFocusFromTouch();//模拟焦点点击事件

        mSearchView.setFocusable(false);//禁止弹出输入法，在某些情况下有需要
        mSearchView.clearFocus();//禁止弹出输入法，在某些情况下有需要
    }

    private void setTitleList(){
        List<MainBannerItem> bannerItems= DataSupport.findAll(MainBannerItem.class);
        List<MainPageRecyclerItem> mainRvItems= DataSupport.findAll(MainPageRecyclerItem.class);
        List<SearchRecordCard> recordCards= DataSupport.findAll(SearchRecordCard.class);
        List<SearchSkillTopCard> skillTopCards= DataSupport.findAll(SearchSkillTopCard.class);
        List<SearchSkillRecyclerItem> skillRecyclerItems= DataSupport.findAll(SearchSkillRecyclerItem.class);
        List<SearchTrackRecyclerItem> trackRecyclerItems= DataSupport.findAll(SearchTrackRecyclerItem.class);
        for(int i=0;i<bannerItems.size();i++){
            titleList.add(bannerItems.get(i).getTitle());
            articleList.add(bannerItems.get(i).getArticleId());
        }
        for(int i=0;i<mainRvItems.size();i++){
            titleList.add(mainRvItems.get(i).getTitle());
            articleList.add(mainRvItems.get(i).getArticleId());
        }
        for(int i=0;i<recordCards.size();i++){
            titleList.add(recordCards.get(i).getTitle());
            articleList.add(recordCards.get(i).getArticleId());
        }
        for(int i=0;i<skillTopCards.size();i++){
            titleList.add(skillTopCards.get(i).getTitle());
            articleList.add(skillTopCards.get(i).getArticleId());
        }
        for(int i=0;i<skillRecyclerItems.size();i++){
            titleList.add(skillRecyclerItems.get(i).getTitle());
            articleList.add(skillRecyclerItems.get(i).getArticleId());
        }
        for(int i=0;i<trackRecyclerItems.size();i++){
            titleList.add(trackRecyclerItems.get(i).getTitle());
            articleList.add(trackRecyclerItems.get(i).getArticleId());
        }
    }

    private void startActivityWithArticleId(int position){
        String articleId=articleList.get(position);
        String first=String.valueOf(articleId.charAt(0));
        Intent intent;
        switch (first){
            case "b":
                intent=new Intent(mContext, MainRecyclerItemActivity.class);
                intent.putExtra("Mark", 0);
                intent.putExtra("ArticleId",articleId);
                startActivity(intent);
                break;

            case "m":
                intent=new Intent(mContext, MainRecyclerItemActivity.class);
                intent.putExtra("Mark", 1);
                intent.putExtra("ArticleId",articleId);
                startActivity(intent);
                break;

            case "r":
                intent=new Intent(mContext, SearchRecordItemActivity.class);
                intent.putExtra("ArticleId",articleId);
                startActivity(intent);
                break;

            case "v":
                intent=new Intent(mContext, SearchSkillItemActivity.class);
                intent.putExtra("Mark", 0);
                intent.putExtra("ArticleId",articleId);
                startActivity(intent);
                break;

            case "s":
                intent=new Intent(mContext, SearchSkillItemActivity.class);
                intent.putExtra("Mark", 1);
                intent.putExtra("ArticleId",articleId);
                startActivity(intent);
                break;

            case "t":
                intent=new Intent(mContext, SearchTrackItemActivity.class);
                intent.putExtra("ArticleId",articleId);
                startActivity(intent);
                break;

            default:
                    break;
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mSearchView.setFocusable(true);
        mSearchView.setFocusableInTouchMode(true);
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
