package com.example.xupeng.xunji.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by xupeng on 2018/3/14.
 */

public class SearchSkillTopCard extends DataSupport{

    private Integer authorId;
    private String articleId;
    private String imageUrl;
    private String title;
    private String content;

    public SearchSkillTopCard(Integer authorId, String articleId, String imageUrl, String title, String content) {
        this.authorId = authorId;
        this.articleId = articleId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.content = content;
    }

    public SearchSkillTopCard() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
