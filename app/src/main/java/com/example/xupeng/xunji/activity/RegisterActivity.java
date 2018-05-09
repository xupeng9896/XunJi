package com.example.xupeng.xunji.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.imp.OnInputCompleteListener;
import com.example.xupeng.xunji.utils.MyEditText;
import com.example.xupeng.xunji.utils.RandCodeUtils;
import com.example.xupeng.xunji.utils.RandNumUtils;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by xupeng on 2018/3/7.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mRegisterLoginBtn;
    private ImageView mShowPwd;
    private ImageView mShowConfirmPwd;
    private ImageView mConfirmNumImg;
    private MyEditText mRegisterName;
    private MyEditText mRegisterPwd;
    private MyEditText mRegisterConPwd;
    private MyEditText mConfirmNum;
    private MyEditText mRegisterTel;
    private MyEditText mSMSCode;
    private Button mRegister;
    private Button mTelConfirmNum;
    private Intent mIntent;
    private UserInfo userInfo;
    private RandCodeUtils mRandCode;
    private Context context;

    private boolean isShowPwd=true;
    private boolean isShowConfirmPwd=true;
    private boolean isPhoneNum = false;
    private boolean randCodeTrue = false;
    private boolean smsCodeTrue = false;
    private String phoneNum;

    private SMSCodeCountDownTimer smsCodeCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context=getBaseContext();
        userInfo=new UserInfo();
        userInfo.setImageUrl(
                MainActivity.IMAGE_URL_PRE_ADDRESS+"head_image"+ RandNumUtils.getRandImageNum()+".jpg");

        init();
        createConfirmCode();
        judgeEditText();
    }

    private void init() {
        mRegisterLoginBtn = (TextView) findViewById(R.id.register_login_btn);
        mShowPwd=(ImageView)findViewById(R.id.show_register_password);
        mShowConfirmPwd=(ImageView)findViewById(R.id.show_register_confirm_password);
        mRegisterName=(MyEditText)findViewById(R.id.register_username);
        mRegisterPwd=(MyEditText)findViewById(R.id.register_password);
        mRegisterConPwd=(MyEditText)findViewById(R.id.register_confirm_pwd);
        mRegister = (Button) findViewById(R.id.register);
        mRegisterTel = (MyEditText) findViewById(R.id.register_tel);
        mSMSCode = (MyEditText) findViewById(R.id.tel_confirm_num);
        mTelConfirmNum = (Button) findViewById(R.id.get_tel_confirm_num);
        mConfirmNumImg = (ImageView) findViewById(R.id.confirm_number_image);
        mConfirmNum = (MyEditText) findViewById(R.id.confirm_number);

        mRegisterLoginBtn.setOnClickListener(this);
        mShowPwd.setOnClickListener(this);
        mShowConfirmPwd.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mTelConfirmNum.setOnClickListener(this);
        mConfirmNumImg.setOnClickListener(this);

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandle.sendMessage(msg);

            }
        };
        SMSSDK.registerEventHandler(eventHandler);

    }

    private void judgeEditText() {

        mRegisterName.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String username=mRegisterName.getText().toString();
                if (username.startsWith(" ")) {
                    mRegisterName.setText("");
                }
                if (username.equals("")) {
                    Toast.makeText(context, "昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    List<UserInfo> userList= DataSupport.where("username=?",username).find(UserInfo.class);
                    if(userList.size()!=0){
                        //Toast.makeText(context,userList.get(0).getUsername(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "昵称已被占用，请重新输入", Toast.LENGTH_SHORT).show();
                    }else {
                        userInfo.setUsername(username);
                    }
                }
            }
        });
        mRegisterPwd.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String pwd=mRegisterPwd.getText().toString();
                if (pwd.startsWith(" ")) {
                    mRegisterPwd.setText("");
                }
                if (pwd.equals("")) {
                    Toast.makeText(context, "登录密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    userInfo.setPassword(pwd);
                }
            }
        });
        mRegisterConPwd.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String confirmPwd=mRegisterConPwd.getText().toString();
                if (confirmPwd.startsWith(" ")) {
                    mRegisterConPwd.setText("");
                }
                if (confirmPwd.equals("")) {
                    Toast.makeText(context, "确认密码不能为空", Toast.LENGTH_SHORT).show();
                } else if(!confirmPwd.equals(mRegisterPwd.getText().toString())){
                    Toast.makeText(context, "两次密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mRegisterTel.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                phoneNum = mRegisterTel.getText().toString();
                if (phoneNum.startsWith(" ")) {
                    mRegisterTel.setText("");
                }
                if (phoneNum.equals("")) {
                    Toast.makeText(context, "手机号码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (phoneNum.length() != 11) {
                        Toast.makeText(context, "手机号码不合法", Toast.LENGTH_SHORT).show();
                    } else {
                        isPhoneNum = true;
                        userInfo.setPhone(phoneNum);
                    }
                }
            }
        });
        mSMSCode.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String smsCode = mSMSCode.getText().toString();
                if (smsCode.startsWith(" ")) {
                    mSMSCode.setText("");
                }
                if (smsCode.equals("")) {
                    Toast.makeText(context, "短信验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    SMSSDK.submitVerificationCode("86", phoneNum, smsCode);
                    Toast.makeText(context, "正在提交验证", Toast.LENGTH_LONG).show();
                }
            }
        });
        mConfirmNum.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String confirmCode = mConfirmNum.getText().toString();
                if (confirmCode.startsWith(" ")) {
                    mConfirmNum.setText("");
                }
                if (confirmCode.equals("")) {
                    Toast.makeText(context, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String code = mRandCode.getCode();
                    if (code.equalsIgnoreCase(confirmCode)) {
                        randCodeTrue=true;
                    }else {
                        Toast.makeText(context, "验证码错误", Toast.LENGTH_SHORT).show();
                        randCodeTrue = false;
                    }
                }
            }
        });

    }

    private void createConfirmCode() {
        mRandCode = RandCodeUtils.getInstance();
        Bitmap bitmap = mRandCode.createBitmap();
        mConfirmNumImg.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_login_btn:
                mRegister.setFocusable(true);
                mRegister.requestFocusFromTouch();
                mIntent = new Intent(this, LoginActivity.class);
                startActivity(mIntent);
                break;

            case R.id.show_register_password:
                if (isShowPwd) {// 显示密码
                    mShowPwd.setImageDrawable(getResources().getDrawable(R.mipmap.hide_pwd));
                    mRegisterPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mRegisterPwd.setSelection(mRegisterPwd.getText().toString().length());
                    isShowPwd = !isShowPwd;
                } else {// 隐藏密码
                    mShowPwd.setImageDrawable(getResources().getDrawable(R.mipmap.show_pwd));
                    mRegisterPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mRegisterPwd.setSelection(mRegisterPwd.getText().toString().length());
                    isShowPwd = !isShowPwd;
                }
                break;

            case R.id.show_register_confirm_password:
                if (isShowConfirmPwd) {// 显示密码
                    mShowConfirmPwd.setImageDrawable(getResources().getDrawable(R.mipmap.hide_pwd));
                    mRegisterConPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mRegisterConPwd.setSelection(mRegisterConPwd.getText().toString().length());
                    isShowConfirmPwd = !isShowConfirmPwd;
                } else {// 隐藏密码
                    mShowConfirmPwd.setImageDrawable(getResources().getDrawable(R.mipmap.show_pwd));
                    mRegisterConPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mRegisterConPwd.setSelection(mRegisterConPwd.getText().toString().length());
                    isShowConfirmPwd = !isShowConfirmPwd;
                }
                break;

            case R.id.register:
                mIntent = new Intent(this, LoginActivity.class);
                if (smsCodeTrue && randCodeTrue) {
                    userInfo.save();
                    startActivity(mIntent);
                } else {
                    Toast.makeText(context, "短信验证码或图片验证码输入错误", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.get_tel_confirm_num:
                mTelConfirmNum.setFocusable(true);
                mTelConfirmNum.requestFocusFromTouch();
                if (isPhoneNum) {
                    Toast.makeText(context, "发送", Toast.LENGTH_LONG).show();
                    SMSSDK.getVerificationCode("86", phoneNum);
                    Toast.makeText(context, "验证码即将发送到您" + phoneNum + "的手机上", Toast.LENGTH_LONG).show();
                    smsCodeCountDownTimer = new SMSCodeCountDownTimer(60000, 1000);
                    smsCodeCountDownTimer.start();
                }else {
                    Toast.makeText(context, "请先输入手机号码", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.confirm_number_image:
                mConfirmNumImg.setFocusable(true);
                mConfirmNumImg.requestFocusFromTouch();
                createConfirmCode();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

    private class SMSCodeCountDownTimer extends CountDownTimer {

        public SMSCodeCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            //防止计时过程中重复点击
            mTelConfirmNum.setClickable(false);
            mTelConfirmNum.setBackgroundColor(getResources().getColor(R.color.gray));
            mTelConfirmNum.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {

            //重新给Button设置文字
            mTelConfirmNum.setText("重新获取");
            //设置可点击
            mTelConfirmNum.setClickable(true);
            mTelConfirmNum.setTextColor(getResources().getColor(R.color.black));
            mTelConfirmNum.setBackgroundColor(getResources().getColor(R.color.darkOrange));

        }
    }

    Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//验证码提交事件
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调成功
                    smsCodeTrue=true;
                    Toast.makeText(context, "提交验证码成功", Toast.LENGTH_LONG).show();
                } else if (result == SMSSDK.RESULT_ERROR) {
                    smsCodeTrue=false;
                    Toast.makeText(context, "提交验证码失败", Toast.LENGTH_LONG).show();
                }

            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取短信验证码事件

                //获取验证码成功
                if (result == SMSSDK.RESULT_COMPLETE) {
                    Toast.makeText(context, "获取短信验证码成功", Toast.LENGTH_LONG).show();
                    boolean mobcheck = (Boolean) data;
                    if (mobcheck) {
                        //通过智能验证
                        Toast.makeText(context, "mob云验证", Toast.LENGTH_LONG).show();
                    } else {
                        //依然走短信验证
                        Toast.makeText(context, "短信验证", Toast.LENGTH_LONG).show();
                    }
                } else if (result == SMSSDK.RESULT_ERROR) {
                    Toast.makeText(context, "获取短信验证码失败", Toast.LENGTH_LONG).show();
                }
            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                SMSSDK.getSupportedCountries();
            } else {

                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    JSONObject jsonObject = new JSONObject(throwable.getMessage());
                    String des = jsonObject.optString("detail");
                    int status = 0;
                    status = jsonObject.optInt("status");
                    if (TextUtils.isEmpty(des)) {

                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
        }
    };
}
