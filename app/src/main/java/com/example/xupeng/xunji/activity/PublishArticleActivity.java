package com.example.xupeng.xunji.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.fragment.MainRecyclerPublishFragment;
import com.example.xupeng.xunji.fragment.RecordPublishFragment;
import com.example.xupeng.xunji.fragment.SkillCardPublishFragment;
import com.example.xupeng.xunji.fragment.TrackRecyclerPublishFragment;

/**
 * Created by xupeng on 2018/4/2.
 */

public class PublishArticleActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Spinner mPublishStyle;

    private Fragment mainRPFragment;
    private Fragment recordRPFragment;
    private Fragment skillCPFragment;
    private Fragment trackRPFragment;

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Integer userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_article);
        MyApplication myApplication=(MyApplication)getApplicationContext();
        userId=myApplication.getUserId();
        mToolbar = (Toolbar) findViewById(R.id.page_module_toolbar);
        mPublishStyle=(Spinner)findViewById(R.id.style_publish_article);

        mToolbar.setTitle(getResources().getString(R.string.publish_article));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState != null) {
            mainRPFragment = mFragmentManager.findFragmentByTag("MainRecyclerPublishFragment");
            recordRPFragment = mFragmentManager.findFragmentByTag("RecordPublishFragment");
            skillCPFragment = mFragmentManager.findFragmentByTag("SkillCardPublishFragment");
            trackRPFragment = mFragmentManager.findFragmentByTag("TrackRecyclerPublishFragment");
        }

        mPublishStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPublishStyle.setSelection(position,true);
                setSelectFragment(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSelectFragment(int position) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        hideFragment(mFragmentTransaction);

        switch (position) {
            case 0:
                if (mainRPFragment == null) {
                    mainRPFragment = new MainRecyclerPublishFragment();
                    mFragmentTransaction.add(R.id.article_fragment, mainRPFragment, "MainRecyclerPublishFragment");
                }
                mFragmentTransaction.show(mainRPFragment);
                break;
            case 1:
                if (recordRPFragment == null) {
                    recordRPFragment = new RecordPublishFragment();
                    mFragmentTransaction.add(R.id.article_fragment, recordRPFragment, "RecordPublishFragment");
                }
                mFragmentTransaction.show(recordRPFragment);
                break;
            case 2:
                if (skillCPFragment == null) {
                    skillCPFragment = new SkillCardPublishFragment();
                    mFragmentTransaction.add(R.id.article_fragment, skillCPFragment, "SkillCardPublishFragment");
                }
                mFragmentTransaction.show(skillCPFragment);
                break;
            case 3:
                if (trackRPFragment == null) {
                    trackRPFragment = new TrackRecyclerPublishFragment();
                    mFragmentTransaction.add(R.id.article_fragment, trackRPFragment, "TrackRecyclerPublishFragment");
                }
                mFragmentTransaction.show(trackRPFragment);
                break;
            default:
                break;
        }
        mFragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (mainRPFragment != null) {
            ft.hide(mainRPFragment);
        }
        if (recordRPFragment != null) {
            ft.hide(recordRPFragment);
        }
        if (skillCPFragment != null) {
            ft.hide(skillCPFragment);
        }
        if (trackRPFragment != null) {
            ft.hide(trackRPFragment);
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
