package com.bwie.mvp.model;

import com.bwie.mvp.bean.UserJavaBean;

/**
 * 作者：宋聪聪 on 2017/9/26.
 */

public interface IuserModel {
    //编写逻辑的接口类
    //保存和显示数据
    public void saveUserInfo(int id,String name,String pwd);
    //显示
    public UserJavaBean getUserInfo();
}
