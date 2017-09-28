package com.bwie.mvp.bean;

/**
 * 作者：宋聪聪 on 2017/9/26.
 */

public class UserJavaBean {
    private int id;
    private String name;
    private String pwd;

    public UserJavaBean(int id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
