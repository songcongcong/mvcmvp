package com.bwie.mvp.presenter;

import android.content.Context;
import android.widget.Toast;

import com.bwie.mvp.bean.UserJavaBean;
import com.bwie.mvp.model.UserModel;
import com.bwie.mvp.view.Userview;

/**
 * 作者：宋聪聪 on 2017/9/26.
 */

public class LoginPresenter {
    /*
    实现model与view的连接
     */
    UserModel userModel;
    Userview userview;
    Context context;

    public LoginPresenter( Userview userview, Context context) {
        this.userModel =  new UserModel(context);
        this.userview = userview;
        this.context = context;
    }
    //存值
    public void saveUser(){
        //从view中取出来的值
        int txetId = userview.getTxetId();
        String textName = userview.getTextName();
        String textPwd = userview.getTextPwd();
        //传入逻辑层，存入sp中
        userModel.saveUserInfo(txetId,textName,textPwd);
    }

    //展示数据
    public void showUser(){
        //从sp中取出
        UserJavaBean userInfo = userModel.getUserInfo();
        int id = userInfo.getId();
        if(id>0)
        {
            //将数据从sp中取出显示在控件上
            userview.setTextId(userInfo.getId());
            userview.setTextName(userInfo.getName());
            userview.setTextPwd(userInfo.getPwd());
        }

    }

//点击登录时进行判断
    public void Login(){
        int txetId = userview.getTxetId();
        String textName = userview.getTextName();
        String textPwd = userview.getTextPwd();
        if(txetId<0)
        {
            Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!textName.equals("zhangsan"))
        {
            Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
        }
        if(!textPwd.equals("12345"))
        {
            Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
        }

        //点击按钮时存入数据
        saveUser();
        //跳转
       /* Intent intent = new Intent(LoginPresenter.this, Main2Activity.class);
        context.startActivity(intent);*/
    }


}
