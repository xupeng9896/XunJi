package com.example.xupeng.xunji.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angel.view.SWImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.xupeng.xunji.R;
import com.example.xupeng.xunji.adpter.DrawerLayoutRecyclerAdapter;
import com.example.xupeng.xunji.bean.DrawerLayoutRecyclerItem;
import com.example.xupeng.xunji.bean.MyApplication;
import com.example.xupeng.xunji.bean.UserInfo;
import com.example.xupeng.xunji.utils.RandNumUtils;
import com.example.xupeng.xunji.utils.SharedPreferencesUtil;
import com.example.xupeng.xunji.fragment.MainFragment;
import com.example.xupeng.xunji.fragment.SearchRecordFragment;
import com.example.xupeng.xunji.fragment.SearchSkillFragment;
import com.example.xupeng.xunji.fragment.SearchTrackFragment;
import com.example.xupeng.xunji.imp.OnRecyclerViewItemClickListener;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //初始化application
    private MyApplication myApplication;
    private TabLayout mTabLayout;
    private Fragment mainFragment;
    private Fragment sRecordFragment;
    private Fragment sTrackFragment;
    private Fragment sTechFragment;

    private DrawerLayout mDrawerLayout;
    private LinearLayout mSearchViewBtn;
    private ActionBarDrawerToggle mDrawerToggle;
    private ImageView mPersonalImage;
    private ImageView mMessage;
    private SWImageView mUserImage;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private DrawerLayoutRecyclerAdapter mAdapter;
    private TextView mLoginBtn;

    private Intent mIntent;
    private List<DrawerLayoutRecyclerItem> drawerLayoutItemList;
    private List<UserInfo> userList;
    private boolean menusState = false;

    private FragmentTransaction mFragmentTransaction;
    private FragmentManager mFragmentManager;

    private final String TAG = "mainActivity";
    private final int REQUEST_CODE = 0;
    public static final int LOGIN_RESULT_CODE = 1;
    public static final int SETTINGS_RESULT_CODE = 2;
    public static final int COLLECT_RESULT_CODE = 3;
    public static final int INFO_CHANGED_CODE = 4;
    public static final String IMAGE_URL_PRE_ADDRESS = "http://47.95.112.35:8080/";
    public static final String FILE_KEY = "user";
    public static final String USER_ID_KEY = "uerId";
    public static final String CLICK_KEY = "click";
    public static final String LOGIN_STATE = "loginState";
    public static final String IMAGE_KEY = "headImage";
    /**
     * 需要进行检测的权限数组
     */
    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            /*Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.WRITE_SETTINGS*/
    };

    private static final int PERMISSION_REQUEST_CODE = 0;

    /**
     * 判断是否需要检测，防止不停的弹框
     */
    private boolean isNeedCheck = true;

    //初始化tab标题和图片
    String[] titles;
    int[] images;

    //初始化侧滑菜单标题和图片
    String[] drawerTitles;
    int[] drawerImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        myApplication = (MyApplication) getApplicationContext();
        LitePal.getDatabase();
        //第一次初始化，添加一条数据到user表
        userList = DataSupport.findAll(UserInfo.class);
        if (userList.size() == 0) {
            userList = new ArrayList<>();
            //RandNumUtils.getInstance(100000, 999999, 100);
            UserInfo user = new UserInfo(1,
                    IMAGE_URL_PRE_ADDRESS + "head_image" + RandNumUtils.getRandImageNum() + ".jpg",
                    "admin", "admin", "13838383838", "admin@163.com", "未知");
            user.save();
            UserInfo user1 = new UserInfo(2,
                    IMAGE_URL_PRE_ADDRESS + "head_image" + RandNumUtils.getRandImageNum() + ".jpg",
                    "test", "123456", "13939393939", "123456@163.com", "未知");
            user1.save();
            userList.add(user);
            userList.add(user1);
        }
        if (savedInstanceState != null) {
            mainFragment = mFragmentManager.findFragmentByTag("MainFragment");
            sRecordFragment = mFragmentManager.findFragmentByTag("SearchRecordFragment");
            sTechFragment = mFragmentManager.findFragmentByTag("SearchSkillFragment");
            sTrackFragment = mFragmentManager.findFragmentByTag("SearchTrackFragment");
        }

        Object loginStateObj = SharedPreferencesUtil.getObject(this, FILE_KEY, LOGIN_STATE);
        Object clickableObj = SharedPreferencesUtil.getObject(this, FILE_KEY, CLICK_KEY);
        /*若第一次登录，从LoginActivity传来的UserId,
          若是第三方登录，就根据resultCode判断后获取UserId,
          若后期登录，从退出程序后保存LoginState来获取UserId，
        */
        Integer userId = null;
        if (loginStateObj != null) {
            boolean loginState = (boolean) loginStateObj;
            if (loginState) {
                userId = (Integer) SharedPreferencesUtil.getObject(this, FILE_KEY, USER_ID_KEY);
                myApplication.setUserId(userId);
            } else {
                userId = myApplication.getUserId();
            }
        }

        initTitleAndImage();
        initDrawerRecyclerItems();
        initView();
        setSelected(0);

        //根据得到的id获取数据库中的userInfo
        UserInfo user = null;
        if (userId != null)
            user = DataSupport.find(UserInfo.class, userId);
        if (user != null) {
            setPersonalPageInfo(user);
        }

        if (clickableObj != null) {
            boolean clickable = (boolean) clickableObj;
            mLoginBtn.setClickable(clickable);
        }

        //设置状态栏的颜色和亮色模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.grayWhite));
        }
    }

    private void initTitleAndImage() {
        titles = new String[]{
                getResources().getString(R.string.home_page),
                getResources().getString(R.string.record),
                getResources().getString(R.string.tech),
                getResources().getString(R.string.track)
        };
        images = new int[]{
                R.mipmap.home,
                R.mipmap.search_record,
                R.mipmap.search_skill,
                R.mipmap.search_track
        };

        drawerTitles = new String[]{
                getResources().getString(R.string.collected),
                getResources().getString(R.string.personal_info),
                getResources().getString(R.string.settings),
                getResources().getString(R.string.feed_back),
                getResources().getString(R.string.manage)
        };

        drawerImages = new int[]{
                R.mipmap.collected,
                R.mipmap.personal_info,
                R.mipmap.settings,
                R.mipmap.feed_back,
                R.mipmap.manage_center
        };

    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.exchange_tab);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar) findViewById(R.id.top_toolbar);
        mSearchViewBtn=(LinearLayout)findViewById(R.id.search_btn);
        mPersonalImage = (ImageView) findViewById(R.id.personal);
        mLoginBtn = (TextView) findViewById(R.id.login_btn);
        mRecyclerView = (RecyclerView) findViewById(R.id.drawer_layout_rv);
        mMessage = (ImageView) findViewById(R.id.message);
        mUserImage = (SWImageView) findViewById(R.id.user_image);

        for (int i = 0; i < titles.length; i++) {
            TabLayout.Tab tab = mTabLayout.newTab();
            tab.setText(titles[i]);
            tab.setIcon(images[i]);
            mTabLayout.addTab(tab);
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int position = tab.getPosition();
                setSelected(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerView.setClickable(true);
                menusState = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                menusState = false;
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        //侧滑菜单RecyclerView配置
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new DrawerLayoutRecyclerAdapter(this, drawerLayoutItemList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                addDrawerLayoutItem(position);
            }

            @Override
            public void onLoveClickListener(int position, boolean state) {

            }
        });

        mPersonalImage.setOnClickListener(this);
        mSearchViewBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);
        mMessage.setOnClickListener(this);
    }

    //初始化侧滑菜单列表
    private void initDrawerRecyclerItems() {
        drawerLayoutItemList = new ArrayList<>();
        Integer id = myApplication.getUserId();
        if (id != null) {
            UserInfo user = DataSupport.find(UserInfo.class, id);
            String name = "";
            if (user != null) {
                name = user.getUsername();
            }
            if (name.equals("admin")) {
                for (int i = 0; i < drawerImages.length; i++) {
                    drawerLayoutItemList.add(new DrawerLayoutRecyclerItem(drawerImages[i], drawerTitles[i]));
                }
            } else {
                for (int i = 0; i < drawerImages.length - 1; i++) {
                    drawerLayoutItemList.add(new DrawerLayoutRecyclerItem(drawerImages[i], drawerTitles[i]));
                }
            }
        } else {
            for (int i = 0; i < drawerImages.length - 1; i++) {
                drawerLayoutItemList.add(new DrawerLayoutRecyclerItem(drawerImages[i], drawerTitles[i]));
            }
        }

    }

    //设置tab选项对应fragment
    private void setSelected(int position) {

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        hideFragment(mFragmentTransaction);

        switch (position) {
            case 0:
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                    mFragmentTransaction.add(R.id.tab_fragment, mainFragment, "MainFragment");
                }
                mFragmentTransaction.show(mainFragment);
                break;
            case 1:
                if (sRecordFragment == null) {
                    sRecordFragment = new SearchRecordFragment();
                    mFragmentTransaction.add(R.id.tab_fragment, sRecordFragment, "SearchRecordFragment");
                }
                mFragmentTransaction.show(sRecordFragment);
                break;
            case 2:
                if (sTechFragment == null) {
                    sTechFragment = new SearchSkillFragment();
                    mFragmentTransaction.add(R.id.tab_fragment, sTechFragment, "SearchSkillFragment");
                }
                mFragmentTransaction.show(sTechFragment);
                break;
            case 3:
                if (sTrackFragment == null) {
                    sTrackFragment = new SearchTrackFragment();
                    mFragmentTransaction.add(R.id.tab_fragment, sTrackFragment, "SearchTrackFragment");
                }
                mFragmentTransaction.show(sTrackFragment);
                break;
            default:
                break;
        }
        mFragmentTransaction.commit();

    }

    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if (mainFragment != null) {
            fragmentTransaction.hide(mainFragment);
        }
        if (sTechFragment != null) {
            fragmentTransaction.hide(sTechFragment);
        }
        if (sRecordFragment != null) {
            fragmentTransaction.hide(sRecordFragment);
        }
        if (sTrackFragment != null) {
            fragmentTransaction.hide(sTrackFragment);
        }
    }

    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissionList = findDeniedPermissions(permissions);
        if (null != needRequestPermissionList
                && needRequestPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissionList.toArray(
                            new String[needRequestPermissionList.size()]),
                    PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissionList.add(perm);
            }
        }
        return needRequestPermissionList;
    }


    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("权限申请说明");
        builder.setMessage("此权限申请是为了更好地用户体验，请点击允许");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     *
     * @since 2.5.0
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    //侧滑菜单子菜单加载
    private void addDrawerLayoutItem(int position) {

        switch (position) {
            case 0:
                mIntent = new Intent(this, CollectedActivity.class);
                startActivityForResult(mIntent, REQUEST_CODE);
                break;

            case 1:
                mIntent = new Intent(this, PersonalInfoActivity.class);
                mIntent.putExtra("UserId", myApplication.getUserId());
                startActivityForResult(mIntent,REQUEST_CODE);
                break;

            case 2:
                mIntent = new Intent(this, SettingsActivity.class);
                startActivity(mIntent);
                break;

            case 3:
                mIntent = new Intent(this, FeedBackActivity.class);
                startActivity(mIntent);
                break;

            case 4:
                mIntent = new Intent(this, ManageAndCheckActivity.class);
                startActivity(mIntent);
            default:
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setPersonalPageInfo(UserInfo user) {
        //Log.d(TAG, "userInfo:"+userInfo.getUsername());
        Bitmap headImage = SharedPreferencesUtil.getBitmap(this, FILE_KEY, IMAGE_KEY);
        mLoginBtn.setText(user.getUsername());
        if (headImage != null) {
            Log.d("main", "headImage != null");
            mUserImage.setImageBitmap(headImage);
        } else {
            Log.d("main", "headImage == null");
            //使用Glide加载头像
            Glide.with(this)
                    .load(user.getImageUrl())
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            mUserImage.setImageDrawable(resource);
                            SharedPreferencesUtil.saveBitmap(getBaseContext(), FILE_KEY, IMAGE_KEY, resource);
                        }
                    });
        }

        //保存当前用户的id和登录状态，以便下次启动app读取用户信息
        SharedPreferencesUtil.saveObject(this,
                MainActivity.FILE_KEY, MainActivity.LOGIN_STATE, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal:
                if (menusState) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;

            case R.id.search_btn:
                mIntent=new Intent(getBaseContext(),SearchViewActivity.class);
                startActivity(mIntent);
                break;

            case R.id.login_btn:
                mIntent = new Intent(getBaseContext(), LoginActivity.class);
                startActivityForResult(mIntent, REQUEST_CODE);
                break;

            case R.id.message:
                mIntent = new Intent(getBaseContext(), MessageActivity.class);
                startActivity(mIntent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!verifyPermissions(grantResults)) {
                showMissingPermissionDialog();
                isNeedCheck = false;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == LOGIN_RESULT_CODE) {
                //第三方登录用户设置
                UserInfo user = DataSupport.findLast(UserInfo.class);
                if (user != null) {
                    myApplication.setUserId(user.getId());
                    setPersonalPageInfo(user);
                }
                SharedPreferencesUtil.saveObject(this, FILE_KEY, USER_ID_KEY, myApplication.getUserId());
            }else if (resultCode==INFO_CHANGED_CODE){
                UserInfo user = DataSupport.find(UserInfo.class,myApplication.getUserId());
                if(user!=null)
                  setPersonalPageInfo(user);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedCheck) {
            checkPermissions(needPermissions);
        }
    }
}
