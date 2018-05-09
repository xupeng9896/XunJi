package com.example.xupeng.xunji.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.adpter.PoiKeywordSearchAdapter;
import com.example.xupeng.xunji.bean.PoiAddressItem;
import com.example.xupeng.xunji.utils.MyEditText;
import com.example.xupeng.xunji.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class PoiKeywordSearchActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener {

    private RecyclerView mRecyclerView;
    private MyEditText mKeyword;
    private String keyWord = "";// 要输入的poi搜索关键字
    private PoiResult poiResult; // poi返回的结果
    private int currentPage = 0;// 当前页面，从0开始计数
    private PoiSearch.Query query;// Poi查询条件类
    private PoiSearch poiSearch;// POI搜索
    public static String showAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.poi_search_rv);
        mKeyword = (MyEditText) findViewById(R.id.poi_search_keyword);
        Toolbar toolbar=(Toolbar)findViewById(R.id.page_module_toolbar);

        toolbar.setTitle(this.getString(R.string.poi_search));
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initListener() {

        mKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keyWord = String.valueOf(s);
                if ("".equals(keyWord)) {
                    ToastUtil.show(PoiKeywordSearchActivity.this,"请输入搜索关键字");
                    return;
                } else {
                    doSearchQuery();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        currentPage = 0;
        //不输入城市名称有些地方搜索不到
        query = new PoiSearch.Query(keyWord, "", "深圳");// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        //这里没有做分页加载了,默认给50条数据
        query.setPageSize(50);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        /*poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(mCurrentLat,
                mCurrentLng), 1500));//设置周边搜索的中心点以及半径*/
        poiSearch.searchPOIAsyn();
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {  // 搜索poi的结果
                if (result.getQuery().equals(query)) {  // 是否是同一条
                    poiResult = result;
                    ArrayList<PoiAddressItem> data = new ArrayList<PoiAddressItem>();//自己创建的数据集合
                    // 取得搜索到的poiitems有多少页
                    List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                    List<SuggestionCity> suggestionCities = poiResult
                            .getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    for(PoiItem item : poiItems){
                        //获取经纬度对象
                        LatLonPoint llp = item.getLatLonPoint();
                        double lon = llp.getLongitude();
                        double lat = llp.getLatitude();

                        String title = item.getTitle();
                        String text = item.getSnippet();
                        String provinceName = item.getProvinceName();
                        String cityName = item.getCityName();
                        String adName = item.getAdName();
                        data.add(new PoiAddressItem(String.valueOf(lon), String.valueOf(lat), title, text,provinceName,
                                cityName,adName));
                    }
                    PoiKeywordSearchAdapter adapter = new PoiKeywordSearchAdapter(PoiKeywordSearchActivity.this,data);
                    mRecyclerView.setAdapter(adapter);
                }
            } else {
                ToastUtil.show(PoiKeywordSearchActivity.this,
                        getResources().getString(R.string.poi_no_result));
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    /**
     * POI信息查询回调方法
     */
    @Override
    public void onPoiItemSearched(PoiItem item, int rCode) {
        // TODO Auto-generated method stub

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(2);
                finish();
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * 设置详情地址
     * @param detailAddress
     */
    public void setDetailAddress(String detailAddress) {
        showAddress=detailAddress;
        mKeyword.setText(showAddress);
    }

}
