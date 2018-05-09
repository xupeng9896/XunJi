package com.example.xupeng.xunji.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.imp.OnInputCompleteListener;
import com.example.xupeng.xunji.utils.MyEditText;

import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManagerDeleteUserActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Button mSearch;
    private Button mDelete;
    private MyEditText mDeleteCondition;
    private TextView mDeleteUserInfo;
    private String condition;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        mToolbar = (Toolbar) findViewById(R.id.page_module_toolbar);
        mDeleteUserInfo=(TextView)findViewById(R.id.delete_user_info);
        mSearch=(Button)findViewById(R.id.search_delete_user);
        mDelete=(Button)findViewById(R.id.delete);
        mDeleteCondition=(MyEditText)findViewById(R.id.delete_condition);

        mToolbar.setTitle(getResources().getString(R.string.delete_user_title));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDeleteCondition.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String info=mDeleteCondition.getText().toString();
                if (info.startsWith(" ")) {
                    mDeleteCondition.setText("");
                }
                if (info.equals("")) {
                    Toast.makeText(getBaseContext(), "删除条件不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    condition=info;
                }
            }
        });

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelete.setFocusable(true);
                mDelete.requestFocusFromTouch();
                List<UserInfo> userInfoList=searchUser(condition);
                if(userInfoList!=null&&userInfoList.size()!=0){
                    UserInfo userInfo=userInfoList.get(0);
                    mDeleteUserInfo.setText("用户id："+userInfo.getId() +"\n"
                            +"用户昵称："+userInfo.getUsername()+"\n"
                            +"用户手机："+userInfo.getPhone());
                }else {
                    mDeleteUserInfo.setText("用户不存在");
                }

            }
        });
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelete.setFocusable(true);
                mDelete.requestFocusFromTouch();
                int result=deleteUser(condition);
                if(result>0){
                    mDeleteUserInfo.setText("删除用户成功");
                }else {
                    mDeleteUserInfo.setText("删除用户失败");
                }
            }
        });
    }

    private int deleteUser(String info) {
        if (isPhoneNum(info)) {
            return DataSupport.deleteAll(UserInfo.class,"phone=?",info);
        } else {
            return DataSupport.deleteAll(UserInfo.class,"username=?",info);
        }
    }

    private List<UserInfo> searchUser(String info) {
        List<UserInfo> userInfoList;
        if (isPhoneNum(info)) {
            userInfoList=DataSupport.where("phone=?",info).find(UserInfo.class);
        } else {
            userInfoList=DataSupport.where("username=?",info).find(UserInfo.class);
        }
        return userInfoList;
    }

    private boolean isPhoneNum(String phone) {
        if (!phone.equals("")) {
            String telRegex = "[1][345678]\\d{9}";
            Pattern pattern = Pattern.compile(telRegex);
            Matcher matcher = pattern.matcher(phone);
            return matcher.matches();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
