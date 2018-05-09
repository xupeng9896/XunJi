package com.example.xupeng.xunji.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by xupeng on 2018/3/17.
 */

public class MainPageRecyclerItem extends DataSupport {

    private Integer authorId;
    private String articleId;
    private String title;
    private String content;
    private String imageUrl;
    private String time;
    private boolean loveState;

    public MainPageRecyclerItem(Integer authorId, String articleId, String title, String content, String imageUrl, boolean loveState,String time) {
        this.authorId = authorId;
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.loveState=loveState;
        this.time = time;
    }

    public MainPageRecyclerItem() {
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isLoveState() {
        return loveState;
    }

    public void setLoveState(boolean loveState) {
        this.loveState = loveState;
    }
}
