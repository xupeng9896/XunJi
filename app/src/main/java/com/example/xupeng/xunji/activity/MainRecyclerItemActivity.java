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
import com.example.xupeng.xunji.bean.MainBannerItem;
import com.example.xupeng.xunji.bean.MainPageRecyclerItem;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by xupeng on 2018/4/2.
 */

public class MainRecyclerItemActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Intent mIntent;

    private String mImageUrl;
    private String mTitle;
    private String mContent;
    private String mTime;
    private String mArticleId;
    private Integer mAuthorId;

    private TextView mTitleTv;
    private TextView mAuthorTv;
    private TextView mTimeTv;
    private TextView mContentTv;
    private ImageView mImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_item_content);

        mIntent = getIntent();
        getIntentExtra(mIntent);
        initView();

        mToolbar.setTitle(getResources().getString(R.string.page_content));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.page_module_toolbar);
        mTitleTv = (TextView) findViewById(R.id.main_item_title);
        mAuthorTv = (TextView) findViewById(R.id.main_item_author);
        mTimeTv = (TextView) findViewById(R.id.main_item_time);
        mImage = (ImageView) findViewById(R.id.main_item_image);
        mContentTv = (TextView) findViewById(R.id.main_item_content);

        mTitleTv.setText(mTitle);
        mAuthorTv.setText("作者:"+mAuthorId);
        mTimeTv.setText(mTime);
        mContentTv.setText(mContent);
        Glide.with(this)
                .load(mImageUrl)
                .apply(new RequestOptions()
                      .placeholder(R.mipmap.loading)
                       .error(R.mipmap.error))
                .into(mImage);
    }

    private void getIntentExtra(Intent intent) {

        if (intent != null) {
            switch (intent.getIntExtra("Mark", -1)) {

                case 0:
                    mArticleId=intent.getStringExtra("ArticleId");
                    List<MainBannerItem> bannerItemList= DataSupport
                            .where("articleId=?",mArticleId).find(MainBannerItem.class);
                    for(int i=0;i<bannerItemList.size();i++){
                        mAuthorId=bannerItemList.get(i).getAuthorId();
                        mTitle = bannerItemList.get(i).getTitle();
                        mImageUrl = bannerItemList.get(i).getImageUrl();
                        mContent = bannerItemList.get(i).getContent();
                    }
                    break;

                case 1:
                    mArticleId=intent.getStringExtra("ArticleId");
                    List<MainPageRecyclerItem> rvItemList= DataSupport
                            .where("articleId=?",mArticleId).find(MainPageRecyclerItem.class);
                    for(int i=0;i<rvItemList.size();i++){
                        mAuthorId=rvItemList.get(i).getAuthorId();
                        mTitle = rvItemList.get(i).getTitle();
                        mImageUrl = rvItemList.get(i).getImageUrl();
                        mContent = rvItemList.get(i).getContent();
                        mTime=rvItemList.get(i).getTime();
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
