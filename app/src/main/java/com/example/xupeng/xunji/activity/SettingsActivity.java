package com.example.xupeng.xunji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.adpter.SettingsRecyclerAdapter;
import com.example.xupeng.xunji.utils.SearchTrackRecyclerItemDecoration;
import com.example.xupeng.xunji.imp.OnRecyclerSettingsSwitchClickListener;
import com.example.xupeng.xunji.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;

import static com.example.xupeng.xunji.activity.MainActivity.CLICK_KEY;
import static com.example.xupeng.xunji.activity.MainActivity.FILE_KEY;
import static com.example.xupeng.xunji.activity.MainActivity.LOGIN_STATE;

/**
 * Created by xupeng on 2018/4/2.
 */

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private Button mExitBtn;
    private Button mExchangeBtn;
    private RecyclerView mRecyclerView;
    private SettingsRecyclerAdapter mAdapter;
    private List<String> settingTitleList;
    private List<Boolean> settingSwitchStateList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        initList();
        initView();
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initView(){
        mToolbar=(Toolbar)findViewById(R.id.page_module_toolbar);
        mRecyclerView=(RecyclerView)findViewById(R.id.settings_rv);
        mExitBtn=(Button)findViewById(R.id.exit_login_btn);
        mExchangeBtn=(Button)findViewById(R.id.exchange_login_btn);

        mToolbar.setTitle(this.getString(R.string.settings));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mAdapter=new SettingsRecyclerAdapter(this,settingTitleList);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SearchTrackRecyclerItemDecoration(10));

        mExitBtn.setOnClickListener(this);
        mExchangeBtn.setOnClickListener(this);

        mAdapter.setOnItemClickListener(new OnRecyclerSettingsSwitchClickListener() {
            @Override
            public void onSwitchClickListener(boolean isChecked) {
                settingSwitchStateList.add(isChecked);
            }
        });

    }

    private void initList(){
        settingTitleList=new ArrayList<>();
        settingSwitchStateList=new ArrayList<>();
        settingTitleList.add("设置标题1");
        settingTitleList.add("设置标题2");
        settingTitleList.add("设置标题3");
        settingTitleList.add("设置标题4");
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.exit_login_btn:
                SharedPreferencesUtil.saveObject(this,
                        FILE_KEY, LOGIN_STATE, false);
                SharedPreferencesUtil.saveObject(this,
                        FILE_KEY, CLICK_KEY, true);
                intent=new Intent(this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.exchange_login_btn:
                intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
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
