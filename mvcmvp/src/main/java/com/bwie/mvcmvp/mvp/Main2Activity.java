package com.bwie.mvcmvp.mvp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bwie.mvcmvp.R;

public class Main2Activity extends AppCompatActivity implements LoginView{

    private Presenter pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //得到控件
        final EditText username= (EditText) findViewById(R.id.username);
        final EditText password= (EditText) findViewById(R.id.password);
        Button login= (Button) findViewById(R.id.login);
        pre = new Presenter(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user=username.getText().toString().trim();
                String pwd=password.getText().toString().trim();
                //调用登录的方法
                pre.login(user,pwd);
            }
        });
    }

    @Override
    public void usernamenull() {
        Toast.makeText(this, "用户名为空", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void pwdnull() {
        Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void succenful() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Main2Activity.this, "成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onDetachedFromWindow() {
        pre.detch();

    }
}
