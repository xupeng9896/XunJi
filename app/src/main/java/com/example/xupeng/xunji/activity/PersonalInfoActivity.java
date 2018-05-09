package com.example.xupeng.xunji.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.angel.view.SWImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.utils.SharedPreferencesUtil;

import org.litepal.crud.DataSupport;

import static com.example.xupeng.xunji.activity.MainActivity.FILE_KEY;
import static com.example.xupeng.xunji.activity.MainActivity.IMAGE_KEY;


/**
 * Created by xupeng on 2018/4/2.
 */

public class PersonalInfoActivity extends AppCompatActivity {

    private MyApplication myApplication;
    private Toolbar mToolbar;
    private SWImageView mHeadImage;
    private TextView mUserId;
    private TextView mUsername;
    private TextView mPhone;
    private TextView mEmail;
    private TextView mSex;
    private Button mEditInfo;
    private Integer userId;

    private final static int REQUEST_CODE = 0;
    public final static int CONFIRM_EDIT_INFO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);
        myApplication=(MyApplication)getApplicationContext();
        userId=myApplication.getUserId();

        mToolbar = (Toolbar) findViewById(R.id.page_module_toolbar);
        mHeadImage = (SWImageView) findViewById(R.id.user_head_image);
        mUserId = (TextView) findViewById(R.id.user_id);
        mUsername = (TextView) findViewById(R.id.user_name);
        mPhone = (TextView) findViewById(R.id.user_phone);
        mEmail=(TextView)findViewById(R.id.user_email);
        mSex = (TextView) findViewById(R.id.user_sex);
        mEditInfo = (Button) findViewById(R.id.edit_personal_info);

        mToolbar.setTitle(getResources().getString(R.string.personal_info));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //从相册取照片
        mHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 0);
            }
        });

        mEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PersonalInfoEditActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    String imageUrl=data.getData().toString();
                    //Glide.with(getBaseContext()).load(imageUrl).into(mHeadImage);
                    Glide.with(getBaseContext())
                            .load(imageUrl)
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    mHeadImage.setImageDrawable(resource);
                                    SharedPreferencesUtil.saveBitmap(getBaseContext(), FILE_KEY, IMAGE_KEY, resource);
                                }
                            });
                    UserInfo user=new UserInfo();
                    user.setImageUrl(imageUrl);
                    user.update(userId);
                }
            } else if (resultCode == CONFIRM_EDIT_INFO) {
                UserInfo user = DataSupport.find(UserInfo.class, userId);
                if (user != null) {
                    mUserId.setText(Integer.toString(user.getId()));
                    mUsername.setText(user.getUsername());
                    mPhone.setText(user.getPhone());
                    Log.d("test", "email :"+user.getEmail());
                    mEmail.setText(user.getEmail());
                    mSex.setText(user.getSex());
                } else {
                    mEditInfo.setClickable(false);
                }
            }
            setResult(MainActivity.INFO_CHANGED_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取从MainActivity获取的userId
        Bitmap headImage = SharedPreferencesUtil.getBitmap(this, FILE_KEY, IMAGE_KEY);
        UserInfo user=null;
        if(userId!=null)
          user=DataSupport.find(UserInfo.class,userId);
        if (user != null) {
            if (headImage != null) {
                Log.d("personalInfo", "headImage != null");
                mHeadImage.setImageBitmap(headImage);
            }else {
                Log.d("personalInfo", "headImage == null");
                Glide.with(this).load(user.getImageUrl()).into(mHeadImage);
            }
            mUserId.setText(Integer.toString(user.getId()));
            mUsername.setText(user.getUsername());
            mPhone.setText(user.getPhone());
            mEmail.setText(user.getEmail());
            mSex.setText(user.getSex());
        } else {
            mEditInfo.setVisibility(View.INVISIBLE);
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
