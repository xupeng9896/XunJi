package com.example.xupeng.xunji.bean;

/**
 * Created by xupeng on 2018/4/1.
 */

public class DrawerLayoutRecyclerItem {

    private int imageId;
    private String title;

    public DrawerLayoutRecyclerItem(int imageId, String title) {
        this.imageId = imageId;
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
