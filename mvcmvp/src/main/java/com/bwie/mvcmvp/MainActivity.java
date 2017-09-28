package com.bwie.mvcmvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bwie.mvcmvp.mvp.Main2Activity;

public class MainActivity extends AppCompatActivity {
/*
mvc:control与view层是activity
model编写逻辑层
                            java.lang.RuntimeException: Can't create handler inside thread that has not called Looper.prepare()
                            子线程不能更新UI
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //得到控件
        final EditText username= (EditText) findViewById(R.id.username);
        final EditText password= (EditText) findViewById(R.id.password);
        Button login= (Button) findViewById(R.id.login);

        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);

        final MvcModel mpdel=new MvcModel();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进行判断
                String user=username.getText().toString();
                if(TextUtils.isEmpty(user))
                {
                    Toast.makeText(MainActivity.this, "user用户名为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwd=password.getText().toString();
                if(TextUtils.isEmpty(pwd))
                {
                    Toast.makeText(MainActivity.this, "user密码为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                    mpdel.login(user,pwd);
            }
        });
    //登录成功
        mpdel.setOnChilkLinster(new MvcModel.OnchilkLinster() {
            @Override
            public void Onchilk(final String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "成功"+result, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
