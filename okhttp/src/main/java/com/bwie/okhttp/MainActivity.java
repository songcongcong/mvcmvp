package com.bwie.okhttp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button phont = (Button) findViewById(R.id.photo);
        Button ceamer = (Button) findViewById(R.id.ceamer);
        phont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用方法
                toPhoto();
            }
        });
        ceamer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toCamera();
            }
        });
   //     postString();

      /*  //调用同步get的方法
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    synchronousMethod();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/

        //调用异步的方法
      //  asynchronousGet();

       // post();
        cache();
    }

    //标识是相册还是相机
    static final int INTENTFORCAMERA = 1;
    static final int INTENTFORPHOTO = 2;
    public String LocalPhotoName;
    //打开相册
    private void toPhoto() {
        createLocalPhotoName();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, INTENTFORPHOTO);
    }
    //相机
    public void toCamera(){
        try {
            Intent intentNow = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = null ;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {  //针对Android7.0，需要通过FileProvider封装过的路径，提供给外部调用
//                uri = FileProvider.getUriForFile(UploadPhotoActivity.this, "com.bw.dliao", SDCardUtils.getMyFaceFile(createLocalPhotoName()));//通过FileProvider创建一个content类型的Uri，进行封装
//            }else {
            uri = Uri.fromFile(SDCardUtils.getMyFaceFile(createLocalPhotoName())) ;
//            }
            intentNow.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intentNow, INTENTFORCAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String createLocalPhotoName() {
        LocalPhotoName = System.currentTimeMillis() + "face.jpg";
        return LocalPhotoName;
    }

    /*
        同步请求（get）需要在线程中执行
     */
    public void synchronousMethod() throws IOException {
        //创建OKhttp对象
        OkHttpClient client = new OkHttpClient();
        //创建请求对象
        Request request = new Request.Builder().url("https://publicobject.com/helloworld.txt").build();
        //封装call
        Call call = client.newCall(request);
        //进行网络请求
        Response execute = call.execute();
        //判断响应码是否连接
        if (execute.isSuccessful()) {
            //成功输出数据
            System.out.println("=====同步====" + execute.body().string());
        }

    }

    //异步请求（get）访问头
    public void asynchronousGet() {
        OkHttpClient client = new OkHttpClient();
        //得到请求对象
        Request request = new Request.Builder().url("https://publicobject.com/helloworld.txt")
                //添加请求头
                //如果有一个一模一样key的会覆盖
                .header("key", "values")
                .header("key", "values1")
                //不会覆盖，有几个会创建几个
                .addHeader("connection", "key-aplive")
                .addHeader("connection", "key-aplive")
                .build();
        //得到call对象
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("=====异步=====" + response.body().string());
            }
        });
    }


    public void postFile(File file) {
        //  sdcard/dliao/aaa.jpg
        String path = file.getAbsolutePath();

        String[] split = path.split("\\/");
        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
//                file
                .addFormDataPart("imageFileName", split[split.length - 1])
                .addFormDataPart("image", split[split.length - 1], RequestBody.create(MEDIA_TYPE_PNG, file))
                .build();
        Request request = new Request.Builder().post(requestBody).url("http://qhb.2dyt.com/Bwei/upload").build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                System.out.println("response = " + response.body().string());

            }
        });

    }

    //返回数据，进行压缩并处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case INTENTFORPHOTO:
                //相册
                try {
                    // 必须这样处理，不然在4.4.2手机上会出问题
                    Uri originalUri = data.getData();
                    File f = null;
                    if (originalUri != null) {
                        f = new File(SDCardUtils.photoCacheDir, LocalPhotoName);
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor actualimagecursor = this.getContentResolver().query(originalUri, proj, null, null, null);
                        if (null == actualimagecursor) {
                            if (originalUri.toString().startsWith("file:")) {
                                File file = new File(originalUri.toString().substring(7, originalUri.toString().length()));
                                if (!file.exists()) {
                                    //地址包含中文编码的地址做utf-8编码
                                    originalUri = Uri.parse(URLDecoder.decode(originalUri.toString(), "UTF-8"));
                                    file = new File(originalUri.toString().substring(7, originalUri.toString().length()));
                                }
                                FileInputStream inputStream = new FileInputStream(file);
                                FileOutputStream outputStream = new FileOutputStream(f);
                                copyStream(inputStream, outputStream);
                            }
                        } else {
                            // 系统图库
                            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            actualimagecursor.moveToFirst();
                            String img_path = actualimagecursor.getString(actual_image_column_index);
                            if (img_path == null) {
                                InputStream inputStream = this.getContentResolver().openInputStream(originalUri);
                                FileOutputStream outputStream = new FileOutputStream(f);
                                copyStream(inputStream, outputStream);
                            } else {
                                File file = new File(img_path);
                                FileInputStream inputStream = new FileInputStream(file);
                                FileOutputStream outputStream = new FileOutputStream(f);
                                copyStream(inputStream, outputStream);
                            }
                        }
                        Bitmap bitmap = ImageResizeUtils.resizeImage(f.getAbsolutePath(), 1080);
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        FileOutputStream fos = new FileOutputStream(f.getAbsolutePath());
                        if (bitmap != null) {

                            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos)) {
                                fos.close();
                                fos.flush();
                            }
                            if (!bitmap.isRecycled()) {
                                bitmap.isRecycled();
                            }

                            System.out.println("f = " + f.length());
                            postFile(f);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;
            case INTENTFORCAMERA:
               // 相机
                try {

                    //file 就是拍照完 得到的原始照片
                    File file = new File(SDCardUtils.photoCacheDir, LocalPhotoName);
                    Bitmap bitmap = ImageResizeUtils.resizeImage(file.getAbsolutePath(), 1080);
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
                    if (bitmap != null) {
                        if (bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos)) {
                            fos.close();
                            fos.flush();
                        }
                        if (!bitmap.isRecycled()) {
                            //通知系统 回收bitmap
                            bitmap.isRecycled();
                        }
                        System.out.println("f = " + file.length());

                        postFile(file);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    public static void copyStream(InputStream inputStream, OutputStream outStream) throws Exception {
        try {
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        }

    }


    //使用HTTP POST的请求体发送到服务器
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    public void postString(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File("本地图片路径");

                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder().url("https://api.github.com/markdown/raw")
                            .post(RequestBody.create(MEDIA_TYPE_MARKDOWN,file))
                            .build();
                    Response response =   client.newCall(request).execute();
                    if(response.isSuccessful()){
                        String result =   response.body().string() ;
                        System.out.println("result = " + result);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //post请求
    public void post(){
        OkHttpClient client=new OkHttpClient();
        //post请求需要有body对象
        RequestBody requestBody=new FormBody.Builder()
                .add("name","157d")
                .add("pwd","123")
                .add("post","1503d")
                .build();
        final Request request=new Request.Builder().url("http://qhb.2dyt.com/Bwei/login")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("=====post请求"+response.body().string());
            }
        });


    }

    //缓存
    public void cache(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    int cacheSize = 10 * 1024 * 1024; // 10 MiB
                    Cache cache = new Cache(getCacheDir(), cacheSize);

                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new LoggingInterceptor())
                            .cache(cache)
                            .build();
                    Request request = new Request.Builder()
                            //只使用网络请求
                           // .cacheControl(CacheControl.FORCE_NETWORK)
                            .url("http://publicobject.com/helloworld.txt")
                            .build();

                    String response1Body;
                    Response response1 = client.newCall(request).execute();

                    if (!response1.isSuccessful()) throw new IOException("Unexpected code " + response1);

                    response1Body = response1.body().string();
                    System.out.println("Response 1 response:          " + response1);
                    System.out.println("Response 1 cache response:    " + response1.cacheResponse());
                    System.out.println("Response 1 network response:  " + response1.networkResponse());
             //         }

                    String response2Body;

           // {
                    Response response2 = client.newCall(request).execute();

                //停止正在请求的call
                /*   Call call1 =   client.newCall(request);

                    call1.cancel();
*/
                    if (!response2.isSuccessful()) throw new IOException("Unexpected code " + response2);

                    response2Body = response2.body().string();
                    System.out.println("Response 2 response:          " + response2);
                    System.out.println("Response 2 cache response:    " + response2.cacheResponse());
                    System.out.println("Response 2 network response:  " + response2.networkResponse());


                    System.out.println("Response 2 equals Response 1? " + response1Body.equals(response2Body));
                }catch (Exception e) {

                }
            }
        }).start();
    }



}
