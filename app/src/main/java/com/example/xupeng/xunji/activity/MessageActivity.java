package com.example.xupeng.xunji.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.adpter.ViewPagerAdapter;
import com.example.xupeng.xunji.fragment.MessageCommFragment;
import com.example.xupeng.xunji.fragment.MessageNotifyFragment;

import java.util.ArrayList;

/**
 * Created by xupeng on 2018/3/10.
 */

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private Button mNotify;
    private Button mCommunicate;

    private MessageNotifyFragment mNotifyFragment;
    private MessageCommFragment mCommFragment;
    private ArrayList<Fragment> fragmentList;
    private ViewPagerAdapter mViewPagerAdapter;

    private Button[] buttons;
    private int currIndex=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_center);
        init();
    }

    private void init(){

        mToolbar=(Toolbar)findViewById(R.id.page_module_toolbar);
        mNotify=(Button)findViewById(R.id.notify);
        mCommunicate=(Button)findViewById(R.id.communicate);
        mViewPager=(ViewPager)findViewById(R.id.message_center_viewpager);

        mToolbar.setTitle(getResources().getString(R.string.message_center));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        buttons=new Button[]{mNotify,mCommunicate};

        mNotify.setOnClickListener(this);
        mCommunicate.setOnClickListener(this);

        //重置所有按钮背景颜色
        resetButtonColor();
        //将第一个按钮背景颜色设置为选中
        mNotify.setTextColor(getResources().getColor(R.color.darkOrange));

        fragmentList=new ArrayList<>();
        mNotifyFragment=new MessageNotifyFragment();
        mCommFragment=new MessageCommFragment();

        fragmentList.add(mNotifyFragment);
        fragmentList.add(mCommFragment);

        mViewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(),fragmentList);
        mViewPager.setCurrentItem(currIndex);
        mViewPager.setAdapter(mViewPagerAdapter);

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

        mNotify.setBackgroundColor(getResources().getColor(R.color.gray));
        mCommunicate.setBackgroundColor(getResources().getColor(R.color.gray));
        mNotify.setTextColor(getResources().getColor(R.color.black));
        mCommunicate.setTextColor(getResources().getColor(R.color.black));

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.notify:
                currIndex=0;
                mViewPager.setCurrentItem(currIndex);
                break;

            case R.id.communicate:
                currIndex=1;
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
