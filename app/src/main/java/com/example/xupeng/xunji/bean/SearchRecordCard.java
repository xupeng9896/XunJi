package com.example.xupeng.xunji.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by xupeng on 2018/3/14.
 */

public class SearchRecordCard extends DataSupport {

    private Integer authorId;
    private String articleId;
    //卡片宣传图
    private String imageUrl;
    //卡片标题
    private String title;
    //位置
    private String location;

    private boolean loveState;

    public SearchRecordCard(Integer authorId, String articleId, String imageUrl, String title, String location,boolean loveState) {
        this.authorId = authorId;
        this.articleId = articleId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.location = location;
        this.loveState=loveState;
    }

    public SearchRecordCard() {
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isLoveState() {
        return loveState;
    }

    public void setLoveState(boolean loveState) {
        this.loveState = loveState;
    }
}
