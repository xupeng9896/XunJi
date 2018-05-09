package com.example.xupeng.xunji.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by xupeng on 2018/3/14.
 */

public class UserInfo extends DataSupport implements Serializable {

    //用户id
    private Integer id;
    //头像
    private String imageUrl;
    //用户名
    private String username;
    //用户密码
    private String password;
    //手机
    private String phone;
    //邮箱,用于找回密码
    private String email;
    //用户性别
    private String sex;

    public UserInfo(Integer id, String imageUrl, String username, String password) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.username = username;
        this.password = password;
    }

    public UserInfo(Integer id, String imageUrl, String username, String password, String phone) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.username = username;
        this.password = password;
        this.phone = phone;
    }

    public UserInfo(Integer id, String imageUrl, String username, String password, String phone, String email, String sex) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.sex = sex;
    }

    public UserInfo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
