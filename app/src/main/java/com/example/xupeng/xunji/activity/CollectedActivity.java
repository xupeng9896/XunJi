package com.example.xupeng.xunji.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.adpter.ViewPagerAdapter;
import com.example.xupeng.xunji.fragment.CollectedLoveFragment;
import com.example.xupeng.xunji.fragment.CollectedPublishFragment;

import java.util.ArrayList;

/**
 * Created by xupeng on 2018/4/2.
 */

public class CollectedActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private Button mPublish;
    private Button mLove;

    private Button[] buttons;
    private int currIndex=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collected);

        initView();

        mToolbar.setTitle(this.getString(R.string.collected));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initView(){

        mToolbar=(Toolbar)findViewById(R.id.page_module_toolbar);
        mPublish=(Button)findViewById(R.id.publish);
        mLove=(Button)findViewById(R.id.love);
        mViewPager=(ViewPager)findViewById(R.id.collected_viewpager);

        buttons=new Button[]{mPublish,mLove};

        mPublish.setOnClickListener(this);
        mLove.setOnClickListener(this);

        //重置所有按钮背景颜色
        resetButtonColor();
        //将第一个按钮背景颜色设置为选中
        mPublish.setTextColor(getResources().getColor(R.color.darkOrange));

        ArrayList<Fragment> fragmentList=new ArrayList<>();
        CollectedPublishFragment publishFragment=new CollectedPublishFragment();
        CollectedLoveFragment loveFragment=new CollectedLoveFragment();

        fragmentList.add(publishFragment);
        fragmentList.add(loveFragment);

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setCurrentItem(currIndex);
        mViewPager.setAdapter(viewPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                resetButtonColor();
                buttons[i].setTextColor(getResources().getColor(R.color.darkOrange));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void resetButtonColor(){

        mPublish.setBackgroundColor(getResources().getColor(R.color.gray));
        mLove.setBackgroundColor(getResources().getColor(R.color.gray));
        mPublish.setTextColor(getResources().getColor(R.color.black));
        mLove.setTextColor(getResources().getColor(R.color.black));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.publish:
                currIndex = 0;
                mViewPager.setCurrentItem(currIndex);
                break;

            case R.id.love:
                currIndex = 1;
                mViewPager.setCurrentItem(currIndex);
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
