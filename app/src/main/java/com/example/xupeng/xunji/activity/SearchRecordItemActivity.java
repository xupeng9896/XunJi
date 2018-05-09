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
import com.example.xupeng.xunji.bean.SearchRecordCard;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by xupeng on 2018/3/14.
 */

public class SearchRecordItemActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTitleTv;
    private TextView mLocationTv;
    private ImageView mImage;

    private String mArticleId;
    //private Integer mAuthorId;
    private String mTitle;
    private String mLocation;
    private String mImageUrl;
    private Intent mIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_item_content);

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
        mTitleTv=(TextView)findViewById(R.id.search_record_title);
        mLocationTv=(TextView)findViewById(R.id.search_record_location_tv);
        mImage=(ImageView)findViewById(R.id.search_record_card_image);

        mTitleTv.setText(mTitle);
        mLocationTv.setText(mLocation);
        Glide.with(this)
                .load(mImageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.mipmap.loading)
                        .error(R.mipmap.error))
                .into(mImage);
    }

    private void getIntentExtra(Intent intent){

        if(intent!=null){
            mArticleId=intent.getStringExtra("ArticleId");
            List<SearchRecordCard> cardList= DataSupport
                    .where("articleId=?",mArticleId).find(SearchRecordCard.class);
            for(int i=0;i<cardList.size();i++){
                //mAuthorId=cardList.get(i).getAuthorId();
                mTitle = cardList.get(i).getTitle();
                mImageUrl = cardList.get(i).getImageUrl();
                mLocation = cardList.get(i).getLocation();
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
