package com.example.xupeng.xunji.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.imp.OnInputCompleteListener;
import com.example.xupeng.xunji.utils.MyEditText;
import com.example.xupeng.xunji.utils.RandNumUtils;
import com.example.xupeng.xunji.utils.SharedPreferencesUtil;
import com.mob.tools.utils.UIHandler;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

import static android.R.attr.action;
import static com.example.xupeng.xunji.activity.MainActivity.CLICK_KEY;
import static com.example.xupeng.xunji.activity.MainActivity.FILE_KEY;
import static com.example.xupeng.xunji.activity.MainActivity.LOGIN_STATE;
import static com.example.xupeng.xunji.activity.MainActivity.USER_ID_KEY;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener, PlatformActionListener, Handler.Callback {

    private Button mLoginBtn;
    private Button mRegisterBtn;
    private MyEditText mUsername;
    private MyEditText mPassword;
    private ImageView mUserImage;
    private ImageView mShowPwd;
    private ImageView mPhone;
    private ImageView mWeiBo;
    private ImageView mQQ;
    private ImageView mWeiChat;

    private MyApplication myApplication;
    private String password;
    private boolean isShowPwd = true;
    private boolean isAccTrue = false;
    private boolean isPwdTrue = false;

    private ProgressDialog progressDialog;
    private static final int MSG_ACTION_CALLBACK = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myApplication=(MyApplication)getApplicationContext();
        init();
        judgeEditText();

    }

    private void init() {
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mRegisterBtn = (Button) findViewById(R.id.registerBtn);
        mUsername = (MyEditText) findViewById(R.id.username);
        mPassword = (MyEditText) findViewById(R.id.password);
        mUserImage = (ImageView) findViewById(R.id.head_image);
        mShowPwd = (ImageView) findViewById(R.id.show_input_password);
        mPhone = (ImageView) findViewById(R.id.style_phone);
        mWeiBo = (ImageView) findViewById(R.id.style_weibo);
        mQQ = (ImageView) findViewById(R.id.style_qq);
        mWeiChat = (ImageView) findViewById(R.id.style_weichat);

        mUsername.setFocusable(true);
        mPassword.setFocusable(true);

        mLoginBtn.setOnClickListener(this);
        mRegisterBtn.setOnClickListener(this);
        mShowPwd.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        mWeiBo.setOnClickListener(this);
        mQQ.setOnClickListener(this);
        mWeiChat.setOnClickListener(this);
    }

    private void judgeEditText() {
        mUsername.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String account = mUsername.getText().toString();
                if (account.startsWith(" ")) {
                    mUsername.setText("");
                }
                if (account.equals("")) {
                    Toast.makeText(getBaseContext(), "昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    password = getPassword(account);
                }
            }
        });

        mPassword.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String pwd = mPassword.getText().toString();
                if (pwd.startsWith(" ")) {
                    mPassword.setText("");
                }
                if (pwd.equals("")) {
                    Toast.makeText(getBaseContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (pwd.equals(password)) {
                        isAccTrue = true;
                        isPwdTrue = true;
                    } else {
                        isAccTrue = false;
                        isPwdTrue = false;
                    }
                }
            }
        });


    }

    private String getPassword(String account) {
        String pwd = "";
        List<UserInfo> userList;
        if (isPhoneNum(account)) {
            userList = DataSupport.select("id", "imageUrl", "password")
                    .where("phone=?", account)
                    .find(UserInfo.class);
            if (userList.size() == 0) {
                isAccTrue = false;
                isPwdTrue = false;
                //Toast.makeText(this, "账户名或密码错误", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < userList.size(); i++) {
                    myApplication.setUserId(userList.get(i).getId());
                    pwd = userList.get(i).getPassword();
                    String imageUrl = userList.get(i).getImageUrl();
                    Glide.with(this).load(imageUrl).into(mUserImage);
                }
            }
        } else {
            userList = DataSupport.select("id", "imageUrl", "password")
                    .where("username=?", account)
                    .find(UserInfo.class);
            if (userList.size() == 0) {
                isAccTrue = false;
                isPwdTrue = false;
                //Toast.makeText(this, "账户名或密码错误", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < userList.size(); i++) {
                    myApplication.setUserId(userList.get(i).getId());
                    pwd = userList.get(i).getPassword();
                    String imageUrl = userList.get(i).getImageUrl();
                    Glide.with(this).load(imageUrl).into(mUserImage);
                }
            }
        }
        return pwd;
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

    private void saveUserStateInfo(){
        SharedPreferencesUtil.saveObject(this,
                FILE_KEY, LOGIN_STATE, false);
        SharedPreferencesUtil.saveObject(this,
                FILE_KEY, CLICK_KEY, false);
        SharedPreferencesUtil.saveObject(this, FILE_KEY, USER_ID_KEY, myApplication.getUserId());
    }

    private void phoneFastLogin() {
        //使用mob提供的短信验证界面
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    Toast.makeText(getBaseContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    // 提交用户信息
                    UserInfo user = new UserInfo();
                    user.setUsername(phone);
                    user.setImageUrl(
                            MainActivity.IMAGE_URL_PRE_ADDRESS + "head_image" + RandNumUtils.getRandImageNum() + ".jpg");
                    user.setPassword("123456");
                    user.setPhone(phone);
                    user.save();
                }
            }
        });
        registerPage.show(this);
    }

    //授权
    private void authorize(Platform plat, int type) {
        switch (type) {
            case 1:
                showProgressDialog("打开微信");
                break;
            case 2:
                showProgressDialog("打开QQ");
                break;
            case 3:
                showProgressDialog("打开微博");
                break;
        }
        if (plat.isAuthValid()) { //如果授权就删除授权资料
            plat.removeAccount(true);
        }
        plat.showUser(null);//授权并获取用户信息
    }

    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    //隐藏dialog
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                mLoginBtn.setFocusable(true);
                mLoginBtn.requestFocusFromTouch();
                if (isAccTrue && isPwdTrue) {
                    saveUserStateInfo();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "账户名或密码错误", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.registerBtn:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.show_input_password:
                if (isShowPwd) {// 显示密码
                    mShowPwd.setImageDrawable(getResources().getDrawable(R.mipmap.show_pwd));
                    mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPassword.setSelection(mPassword.getText().toString().length());
                    isShowPwd = !isShowPwd;
                } else {// 隐藏密码
                    mShowPwd.setImageDrawable(getResources().getDrawable(R.mipmap.hide_pwd));
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mPassword.setSelection(mPassword.getText().toString().length());
                    isShowPwd = !isShowPwd;
                }
                break;

            case R.id.style_phone:
                phoneFastLogin();
                saveUserStateInfo();
                setResult(MainActivity.LOGIN_RESULT_CODE);
                finish();
                break;

            case R.id.style_weibo:
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                sina.setPlatformActionListener(this);
                sina.SSOSetting(false);
                authorize(sina, 3);
                break;

            case R.id.style_qq:
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(this);
                qq.SSOSetting(false);
                authorize(qq, 2);
                break;

            case R.id.style_weichat:
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.SSOSetting(false);
                authorize(wechat, 1);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUsername.setFocusable(true);
        mPassword.setFocusable(true);
    }

    //登陆授权成功的回调
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);   //发送消息
    }


    //登陆授权错误的回调
    @Override
    public void onError(Platform platform, int i, Throwable t) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    //登陆授权取消的回调
    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    //登陆发送的handle消息在这里处理
    @Override
    public boolean handleMessage(Message message) {
        hideProgressDialog();
        switch (message.arg1) {
            case 1: { // 成功
                Toast.makeText(LoginActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();

                //获取用户资料
                Platform platform = (Platform) message.obj;
                String userName = platform.getDb().getUserName();//获取用户名字
                String userIcon = platform.getDb().getUserIcon();//获取用户头像
                String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
                //Log.d("login", "用户信息为:"+"用户账号"+userId+"用户名：" + userName + "头像："+userIcon+"性别：" + userGender);
                UserInfo user = new UserInfo();
                user.setUsername(userName);
                user.setImageUrl(userIcon);
                user.setPassword("123456");
                user.setSex(userGender);
                user.save();
                saveUserStateInfo();
                setResult(MainActivity.LOGIN_RESULT_CODE);
                finish();
            }
            break;
            case 2: { // 失败
                Toast.makeText(LoginActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
                setResult(-1);
            }
            break;
            case 3: { // 取消
                Toast.makeText(LoginActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
                setResult(-1);
            }
            break;
        }
        return false;
    }
}
