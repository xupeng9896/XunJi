package com.example.xupeng.xunji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.utils.SharedPreferencesUtil;

import org.litepal.crud.DataSupport;

import static com.example.xupeng.xunji.activity.MainActivity.FILE_KEY;
import static com.example.xupeng.xunji.activity.MainActivity.LOGIN_STATE;

/**
 * Created by xupeng on 2018/4/2.
 */

public class ManageAndCheckActivity extends AppCompatActivity {

    private MyApplication myApplication;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_and_check);
        myApplication=(MyApplication)getApplicationContext();

        Toolbar toolbar=(Toolbar)findViewById(R.id.page_module_toolbar);
        Button deleteUser=(Button)findViewById(R.id.delete_user);
        Button deleteArticle=(Button)findViewById(R.id.delete_article);

        toolbar.setTitle(this.getString(R.string.manage));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Object loginStateObj = SharedPreferencesUtil.getObject(this, FILE_KEY, LOGIN_STATE);

        if (loginStateObj != null) {
            boolean loginState = (boolean) loginStateObj;
            if(!loginState){
                deleteUser.setVisibility(View.INVISIBLE);
                deleteArticle.setVisibility(View.INVISIBLE);
            }else {
                Integer id=myApplication.getUserId();
                UserInfo user= DataSupport.find(UserInfo.class,id);
                if(user!=null) {
                    if (user.getUsername().equals("admin")){
                        deleteUser.setVisibility(View.VISIBLE);
                        deleteArticle.setVisibility(View.VISIBLE);
                    }else {
                        deleteUser.setVisibility(View.INVISIBLE);
                        deleteArticle.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),ManagerDeleteUserActivity.class);
                startActivity(intent);
            }
        });

        deleteArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getBaseContext(),ManagerDeleteArticleActivity.class);
                startActivity(intent);
            }
        });

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
