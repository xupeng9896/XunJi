package com.example.xupeng.xunji.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.activity.MainActivity;
import com.example.xupeng.xunji.activity.MainRecyclerItemActivity;
import com.example.xupeng.xunji.activity.PublishArticleActivity;
import com.example.xupeng.xunji.adpter.MainPageRecyclerAdapter;
import com.example.xupeng.xunji.bean.MainBannerItem;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.utils.GlideImageLoader;
import com.example.xupeng.xunji.bean.MainPageRecyclerItem;
import com.example.xupeng.xunji.utils.SearchTrackRecyclerItemDecoration;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;
import com.example.xupeng.xunji.utils.SharedPreferencesUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.xupeng.xunji.activity.MainActivity.FILE_KEY;
import static com.example.xupeng.xunji.activity.MainActivity.LOGIN_STATE;

/**
 * Created by xupeng on 2018/3/11.
 */

public class MainFragment extends Fragment {

    private Banner mBanner;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingButton;
    private MainPageRecyclerAdapter mAdapter;
    private MyApplication myApplication;
    private List<MainPageRecyclerItem> rvItemList;
    private List<MainBannerItem> bannerItemList;
    private List<String> bannerImages;
    private List<String> bannerTitles;
    private Integer userId;
    private Intent mIntent;
    private final int BANNER_MARK = 0;
    private final int RECYCLER_MARK = 1;
    private final int REQUEST_CODE=0;
    private boolean isPause=false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myApplication=(MyApplication)context.getApplicationContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bannerImages = new ArrayList<>();
        bannerTitles = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        mBanner = (Banner) view.findViewById(R.id.main_banner);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.main_rv);
        mFloatingButton= (FloatingActionButton) view.findViewById(R.id.floating_button);

        Object loginStateObj = SharedPreferencesUtil.getObject(getActivity(), FILE_KEY, LOGIN_STATE);
        if (loginStateObj != null) {
            boolean loginState = (boolean) loginStateObj;
            if (loginState) {
                mFloatingButton.setVisibility(View.VISIBLE);
            } else {
                mFloatingButton.setVisibility(View.INVISIBLE);
            }
        }else {
            mFloatingButton.setVisibility(View.INVISIBLE);
        }
        initImagesAndTitles();
        initBanner();

        //配置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //列表在底部开始展示，反转后由上面开始展示
        layoutManager.setStackFromEnd(true);
        //列表翻转
        layoutManager.setReverseLayout(true);
        mRecyclerView.addItemDecoration(new SearchTrackRecyclerItemDecoration(10));
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new MainPageRecyclerAdapter(getContext(), rvItemList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                //Toast.makeText(getActivity(),"item序号："+position,Toast.LENGTH_SHORT).show();
                mIntent = new Intent(getActivity(), MainRecyclerItemActivity.class);
                mIntent.putExtra("Mark", RECYCLER_MARK);
                mIntent.putExtra("ArticleId",rvItemList.get(position).getArticleId());
                startActivity(mIntent);
            }

            @Override
            public void onLoveClickListener(int position, boolean state) {
                String articleId="r"+(position+1);
                MainPageRecyclerItem item=rvItemList.get(position);
                Log.d("searchRecord", "clickLove7: "+state);
                if(state){
                    item.setLoveState(state);
                }else {
                    item.setToDefault("loveState");
                }
                item.updateAll("articleId=?",articleId);
            }
        });

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(getActivity(), PublishArticleActivity.class);
                startActivity(mIntent);
            }
        });

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int i) {
                //Toast.makeText(getActivity(),"item序号："+i,Toast.LENGTH_SHORT).show();
                mIntent = new Intent(getActivity(), MainRecyclerItemActivity.class);
                mIntent.putExtra("Mark", BANNER_MARK);
                mIntent.putExtra("ArticleId",bannerItemList.get(i).getArticleId());
                startActivity(mIntent);
            }
        });

        return view;
    }


    private void initBanner() {
        //设置banner样式
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(bannerImages);
        //设置banner动画效果
        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合
        mBanner.setBannerTitles(bannerTitles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(5000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();
    }

    private void initImagesAndTitles() {

        //获取数据库中的已有数据
        rvItemList = DataSupport.findAll(MainPageRecyclerItem.class);
        bannerItemList = DataSupport.findAll(MainBannerItem.class);

        //若数据库中没有数据就分别插入一条数据
        if (bannerItemList.size()==0) {
            bannerItemList=new ArrayList<>();
            bannerItemList.add(new MainBannerItem(1,"b"+1,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "main_banner1.jpg",
                    "民间故宫：张谷英村", "张谷英村"));
            bannerItemList.add(new MainBannerItem(1, "b"+2,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "main_banner2.jpg",
                    "柔水之美：彝族故居", "彝族故居"));
            bannerItemList.add(new MainBannerItem(1, "b"+3,
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "main_banner3.jpg",
                    "义塾祖庭：鱼镇石门", "鱼镇石门"));
            DataSupport.saveAll(bannerItemList);
        }

        if (rvItemList.size()==0) {

            rvItemList=new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

            rvItemList.add(new MainPageRecyclerItem(1, "m"+1,
                    "民间故宫,张谷英村", "#耕读济世，孝友传家#",
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "main_banner1.jpg",false, dateFormat.format(date)));
            rvItemList.add(new MainPageRecyclerItem(1, "m"+2,
                    "一匹蜀锦，锦官城", "#手工艺、四川成都#",
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "main_banner3.jpg",false, dateFormat.format(date)));
            rvItemList.add(new MainPageRecyclerItem(1, "m"+3,
                    "依山傍水，浙水村", "#山清水秀，自然美#",
                    MainActivity.IMAGE_URL_PRE_ADDRESS + "main_banner2.jpg",false, dateFormat.format(date)));
            DataSupport.saveAll(rvItemList);
        }

        for (int i = bannerItemList.size() - 1; i >= 0; i--) {
            bannerImages.add(bannerItemList.get(i).getImageUrl());
            bannerTitles.add(bannerItemList.get(i).getTitle());
        }
    }

    private int getNumFromString(String string){

        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(string);
        int num=Integer.parseInt(m.replaceAll("").trim());
        return num;
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause=true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPause) {
            isPause=false;
            /*MainBannerItem bannerItem = DataSupport.findLast(MainBannerItem.class);
            int bannerIndex = getNumFromString(bannerItem.getArticleId());
            if (bannerIndex > bannerItemList.size()) {
                bannerItemList.add(bannerItem);
            }
            */
            if(!myApplication.isMainLove()){
                rvItemList=DataSupport.findAll(MainPageRecyclerItem.class);
                mAdapter.updateList(rvItemList);
            }
            if(myApplication.getPublishIndex()==0) {
                MainPageRecyclerItem rvItem = DataSupport.findLast(MainPageRecyclerItem.class);
                int rvIndex = getNumFromString(rvItem.getArticleId());
                if (rvIndex > rvItemList.size()) {
                    mAdapter.addData(rvItem, rvIndex);
                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        mBanner.stopAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
