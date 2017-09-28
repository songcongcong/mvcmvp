package com.bwie.mvcmvp.mvp;

import android.text.TextUtils;

/**
 * 作者：宋聪聪 on 2017/9/28.
 */

public class Presenter {
    private LoginView loginView;
    private ModelMvp modelMvp;

    public Presenter(LoginView loginView) {
        this.loginView = loginView;
        modelMvp = new ModelMvp();
    }

    public void login(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            loginView.usernamenull();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            loginView.pwdnull();
            return;
        }

        //登录成功的时候
        modelMvp.login(username, password, new ModelMvp.MvpOnChilk() {
            @Override
            public void Onchilk(String result) {
                //调用view中的成功的方法
                loginView.succenful();
            }
        });


    }
    public void detch(){
        loginView=null;
    }
}
