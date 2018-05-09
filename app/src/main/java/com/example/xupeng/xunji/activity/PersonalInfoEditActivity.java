package com.example.xupeng.xunji.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.imp.OnInputCompleteListener;
import com.example.xupeng.xunji.utils.MyEditText;

import org.litepal.crud.DataSupport;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static com.example.xupeng.xunji.activity.PersonalInfoActivity.CONFIRM_EDIT_INFO;

/**
 * Created by xupeng on 2018/4/2.
 */

public class PersonalInfoEditActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private MyEditText mUsername;
    private MyEditText mEmail;
    private TextView mPhone;
    private Spinner mSex;
    private Button mSave;
    private Integer userId;
    private String edit_name;
    private String edit_email;
    private String edit_phone;
    private String edit_sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info_edit);
        MyApplication myApplication=(MyApplication)getApplicationContext();

        initView();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userId=myApplication.getUserId();
        UserInfo user = DataSupport.find(UserInfo.class, userId);
        if (user != null) {
            mUsername.setText(user.getUsername());
            mPhone.setText(user.getPhone());
            mEmail.setText(user.getEmail());
            String sex = user.getSex();
            if (sex == null) {
                mSex.setSelection(0);
            } else if (sex.equals("m") || sex.equals("帅男")) {
                mSex.setSelection(1);
            } else {
                mSex.setSelection(2);
            }
        }

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.page_module_toolbar);
        mUsername = (MyEditText) findViewById(R.id.edit_name);
        mPhone = (TextView) findViewById(R.id.edit_phone);
        mEmail = (MyEditText) findViewById(R.id.edit_email);
        mSex = (Spinner) findViewById(R.id.spinner_sex);
        mSave=(Button)findViewById(R.id.edit_save);

        mToolbar.setTitle(this.getString(R.string.personal_info_edit));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);

        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用mob提供的短信验证界面
                //打开注册页面
                RegisterPage registerPage = new RegisterPage();
                registerPage.setRegisterCallback(new EventHandler() {
                    public void afterEvent(int event, int result, Object data) {
                        // 解析注册结果
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            @SuppressWarnings("unchecked")
                            HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                            //String country = (String) phoneMap.get("country");
                            edit_phone = (String) phoneMap.get("phone");

                            // 提交用户信息
                            mPhone.setText(edit_phone);
                        }
                    }
                });
                registerPage.show(getBaseContext());
            }
        });

        mUsername.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String name=mUsername.getText().toString();
                if (name.startsWith(" ")) {
                    mUsername.setText("");
                }
                if (name.equals("")) {
                    Toast.makeText(getBaseContext(), "昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    edit_name=name;
                }
            }
        });

        mEmail.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                edit_email=mEmail.getText().toString();
            }
        });

        mSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //当选中某一个数据项时触发该方法
            /*
             * parent接收的是被选择的数据项所属的 Spinner对象，
             * view参数接收的是显示被选择的数据项的TextView对象
             * position接收的是被选择的数据项在适配器中的位置
             * id被选择的数据项的行号
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSex.setSelection(position, true);
                edit_sex=PersonalInfoEditActivity.this
                        .getResources()
                        .getStringArray(R.array.sexSpinnerArray)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSave.setFocusable(true);
                mSave.requestFocusFromTouch();
                UserInfo user=new UserInfo();
                user.setUsername(edit_name);
                user.setPhone(edit_phone);
                user.setEmail(edit_email);
                user.setSex(edit_sex);
                user.update(userId);
                setResult(CONFIRM_EDIT_INFO);
                finish();
                Toast.makeText(getBaseContext(), "保存成功", Toast.LENGTH_SHORT).show();
            }
        });
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
