package com.example.xupeng.xunji.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by xupeng on 2018/4/9.
 */

public class Manager extends DataSupport {

    private int id;
    private String imageUrl;
    private String name;
    private String password;
    private String sex;

    public Manager(int id, String imageUrl, String name, String password, String sex) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.password = password;
        this.sex = sex;
    }

    public Manager() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
