package com.example.xupeng.xunji.bean;

import com.mob.MobSDK;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

public class MyApplication extends LitePalApplication {

    private Integer userId;
    private int publishIndex;
    private boolean isMainLove;
    private boolean isRecordLove ;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        MobSDK.init(this, "24c2c777ae198", "598359edd537cc7b56c972cdb9061ce9");
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getPublishIndex() {
        return publishIndex;
    }

    public void setPublishIndex(int publishIndex) {
        this.publishIndex = publishIndex;
    }

    public boolean isMainLove() {
        return isMainLove;
    }

    public void setMainLove(boolean mainLove) {
        isMainLove = mainLove;
    }

    public boolean isRecordLove() {
        return isRecordLove;
    }

    public void setRecordLove(boolean recordLove) {
        isRecordLove = recordLove;
    }
}
