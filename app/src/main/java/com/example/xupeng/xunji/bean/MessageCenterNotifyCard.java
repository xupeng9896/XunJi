package com.example.xupeng.xunji.bean;

/**
 * Created by xupeng on 2018/3/16.
 */

public class MessageCenterNotifyCard {

    private String title;
    private String content;

    public MessageCenterNotifyCard(String title, String content) {
        this.title = title;
        this.content = content;
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
