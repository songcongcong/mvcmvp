package com.bwie.mvp.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.bwie.mvp.bean.UserJavaBean;

/**
 * 作者：宋聪聪 on 2017/9/26.
 */

public class UserModel implements  IuserModel {
    private final SharedPreferences sp;
    private SharedPreferences.Editor editor;
    //存入sp中


    public UserModel(Context context) {
        this.sp = context.getSharedPreferences("config",context.MODE_PRIVATE);
        this.editor=sp.edit();
    }

    @Override
    public void saveUserInfo(int id, String name, String pwd) {
        editor.putInt("id",id);
        editor.putString("name",name);
        editor.putString("pwd",pwd);
       editor.commit();
    }

    @Override
    public UserJavaBean getUserInfo() {
        int id = sp.getInt("id",-1);
        String name = sp.getString("name","");
        String pwd=sp.getString("pwd","");
        UserJavaBean userJavaBean = new UserJavaBean(id, name, pwd);
        return userJavaBean;
    }
}
