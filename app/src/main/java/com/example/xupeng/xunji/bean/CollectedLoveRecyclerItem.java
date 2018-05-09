package com.example.xupeng.xunji.bean;

public class CollectedLoveRecyclerItem {

    private Integer userId;
    private String articleId;
    private String style;
    private String imageUrl;
    private Integer authorId;
    private String title;
    private String content;
    private boolean loveState;

    public CollectedLoveRecyclerItem(Integer userId, String articleId, String style, String imageUrl, Integer authorId, String title, String content) {
        this.userId = userId;
        this.articleId = articleId;
        this.style = style;
        this.imageUrl = imageUrl;
        this.authorId = authorId;
        this.title = title;
        this.content = content;
    }

    public CollectedLoveRecyclerItem() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
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

    public boolean isLoveState() {
        return loveState;
    }

    public void setLoveState(boolean loveState) {
        this.loveState = loveState;
    }
}
