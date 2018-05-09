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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.activity.PoiKeywordSearchActivity;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.SearchRecordCard;
import com.example.xupeng.xunji.imp.OnInputCompleteListener;
import com.example.xupeng.xunji.utils.MyEditText;

import org.litepal.crud.DataSupport;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RecordPublishFragment extends Fragment {

    private MyApplication myApplication;
    private ImageView mImage;
    private MyEditText mTitleET;
    private MyEditText mContentET;
    private String mTitle;
    private String mContent;
    private String imageUrl;
    private String location;
    private boolean isShowLocation = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myApplication = (MyApplication) context.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.record_article_fragment, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {
        mImage = (ImageView) view.findViewById(R.id.record_rv_image);
        mTitleET = (MyEditText) view.findViewById(R.id.record_rv_title);
        mContentET = (MyEditText) view.findViewById(R.id.record_rv_content);
        LinearLayout locationBtn = (LinearLayout) view.findViewById(R.id.publish_record_rv_location);
        Button publishBtn = (Button) view.findViewById(R.id.record_rv_publish);

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 0);
            }
        });
        mTitleET.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String title = mTitleET.getText().toString();
                if (title.startsWith(" ")) {
                    mTitleET.setText("");
                }
                if (title.equals("")) {
                    Toast.makeText(getContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    mTitle = mTitleET.getText().toString();
                }
            }
        });

        mContentET.setOnInputCompleteListener(new OnInputCompleteListener() {
            @Override
            public void onInputComplete() {
                String content = mContentET.getText().toString();
                if (content.startsWith(" ")) {
                    mContentET.setText("");
                }
                if (content.equals("")) {
                    Toast.makeText(getContext(), "文章内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    mContent = mContentET.getText().toString();
                }
            }
        });

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PoiKeywordSearchActivity.class);
                startActivityForResult(intent, 0);
            }
        });


        publishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SearchRecordCard> itemList = DataSupport.findAll(SearchRecordCard.class);
                int count = itemList.size();
                SearchRecordCard card = new SearchRecordCard();
                card.setArticleId("r" + count);
                card.setAuthorId(myApplication.getUserId());
                card.setImageUrl(imageUrl);
                card.setTitle(mTitle);
                //card.setContent(mContent);
                if (location.equals("")) {
                    card.setLocation("不显示");
                } else {
                    card.setLocation(location);
                }
                card.save();
                Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                myApplication.setPublishIndex(1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    imageUrl = data.getData().toString();
                    Glide.with(this)
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                    .placeholder(R.mipmap.loading)
                                    .error(R.mipmap.error))
                            .into(mImage);
                }
                if (resultCode == 2) {
                    location = PoiKeywordSearchActivity.showAddress;
                }
            }
        }
    }
}
