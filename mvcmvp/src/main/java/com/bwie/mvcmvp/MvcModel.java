package com.bwie.mvcmvp;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * 作者：宋聪聪 on 2017/9/28.
 */

public class MvcModel {
    /*
    编写逻辑层

     */
    public void login(String username,String password){
        //请求网络
        OkHttpClient client=new OkHttpClient();
        final Request request=new Request.Builder().url("http://qhb.2dyt.com/Bwei/login?username=muhanxi&password=111&postkey=1503d").build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                //成功的数据返回到view层界面，需要用到接口
                //判断接口是否为空
                if(linster!=null)
                {
                    linster.Onchilk(response.body().string());
                }

            }
        });
    }
    public  OnchilkLinster linster;

    public interface  OnchilkLinster{
        public void Onchilk(String result);
    }
    public  void setOnChilkLinster(OnchilkLinster linster)
    {
       this.linster=linster;
    }

}
