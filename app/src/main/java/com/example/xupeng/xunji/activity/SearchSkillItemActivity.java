package com.example.xupeng.xunji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.SearchSkillRecyclerItem;
import com.example.xupeng.xunji.bean.SearchSkillTopCard;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by xupeng on 2018/3/14.
 */

public class SearchSkillItemActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Intent mIntent;
    private TextView mTitleTv;
    private TextView mAuthorTv;
    private TextView mContentTv;
    private ImageView mImage;

    private String mArticleId;
    private String mImageUrl;
    private Integer mAuthorId;
    private String mTitle;
    private String mContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill_item_content);
        mIntent=getIntent();
        getIntentExtra(mIntent);
        initView();

        mToolbar.setTitle(getResources().getString(R.string.page_content));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView(){
        mToolbar=(Toolbar)findViewById(R.id.page_module_toolbar);
        mTitleTv=(TextView)findViewById(R.id.skill_item_title);
        mAuthorTv=(TextView)findViewById(R.id.skill_item_author);
        mContentTv=(TextView)findViewById(R.id.skill_item_content);
        mImage=(ImageView) findViewById(R.id.skill_item_image);

        mTitleTv.setText(mTitle);
        mAuthorTv.setText("作者"+mAuthorId);
        mContentTv.setText(mContent);
        //mImage.setImageResource(mImageId);
        Glide.with(this)
                .load(mImageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.error))
                .into(mImage);
    }

    private void getIntentExtra(Intent intent){
        if (intent != null) {
            switch (intent.getIntExtra("Mark", -1)) {

                case 0:
                    mArticleId=intent.getStringExtra("ArticleId");
                    List<SearchSkillTopCard> cardList= DataSupport
                            .where("articleId=?",mArticleId).find(SearchSkillTopCard.class);
                    for(int i=0;i<cardList.size();i++){
                        mAuthorId=cardList.get(i).getAuthorId();
                        mTitle = cardList.get(i).getTitle();
                        mImageUrl = cardList.get(i).getImageUrl();
                        mContent = cardList.get(i).getContent();
                    }
                    break;

                case 1:
                    mArticleId=intent.getStringExtra("ArticleId");
                    List<SearchSkillRecyclerItem> rvItemList= DataSupport
                            .where("articleId=?",mArticleId).find(SearchSkillRecyclerItem.class);
                    for(int i=0;i<rvItemList.size();i++){
                        mAuthorId=rvItemList.get(i).getAuthorId();
                        mTitle = rvItemList.get(i).getTitle();
                        mImageUrl = rvItemList.get(i).getImageUrl();
                        //mContent = rvItemList.get(i).getContent();
                    }
                    break;

                default:
                    break;
            }
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
