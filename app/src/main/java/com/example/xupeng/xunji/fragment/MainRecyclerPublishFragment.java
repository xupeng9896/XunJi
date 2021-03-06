package com.example.xupeng.xunji.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.bean.MainPageRecyclerItem;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.imp.OnInputCompleteListener;
import com.example.xupeng.xunji.utils.MyEditText;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class MainRecyclerPublishFragment extends Fragment {

    private MyApplication myApplication;
    private ImageView mImage;
    private MyEditText mTitleET;
    private MyEditText mBriefET;
    private MyEditText mContentET;
    private Button mPublishBtn;
    private String mTitle;
    private String mBrief;
    private String mContent;
    private String imageUrl;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myApplication=(MyApplication)context.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.main_recycler_article_fragment,container,false);

        initView(view);
        return view;
    }

    private void initView(View view){

        mImage=(ImageView)view.findViewById(R.id.main_rv_image);
        mTitleET=(MyEditText)view.findViewById(R.id.main_rv_title);
        //mBriefET=(MyEditText)view.findViewById(R.id.main_rv_brief);
        mContentET=(MyEditText)view.findViewById(R.id.main_rv_content);
        mPublishBtn=(Button)view.findViewById(R.id.main_rv_publish);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
            }
        });

        mTitleET.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String title=mTitleET.getText().toString();
                if (title.startsWith(" ")) {
                    mTitleET.setText("");
                }
                if (title.equals("")) {
                    Toast.makeText(getContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    mTitle=mTitleET.getText().toString();
                }
            }
        });

        mContentET.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String content=mContentET.getText().toString();
                if (content.startsWith(" ")) {
                    mContentET.setText("");
                }
                if (content.equals("")) {
                    Toast.makeText(getContext(), "文章内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    mContent=mContentET.getText().toString();
                }
            }
        });

        /*mBriefET.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                mBrief=mBriefET.getText().toString();
            }
        });*/

        mPublishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPublishBtn.setFocusable(true);
                mPublishBtn.requestFocusFromTouch();
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                List<MainPageRecyclerItem> itemList= DataSupport.findAll(MainPageRecyclerItem.class);
                int count=itemList.size();
                MainPageRecyclerItem item=new MainPageRecyclerItem();
                item.setArticleId("m"+count);
                item.setAuthorId(myApplication.getUserId());
                item.setImageUrl(imageUrl);
                item.setTitle(mTitle);
                item.setContent(mContent);
                item.setTime(dateFormat.format(date));
                item.save();
                Toast.makeText(getActivity(),"发布成功,请点击返回",Toast.LENGTH_SHORT).show();
                myApplication.setPublishIndex(0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK){
            if(data.getData()!=null){
                imageUrl=data.getData().toString();
                Glide.with(this)
                        .load(imageUrl)
                        .apply(new RequestOptions()
                                .placeholder(R.mipmap.loading)
                                .error(R.mipmap.error))
                        .into(mImage);
            }
        }
    }

}
