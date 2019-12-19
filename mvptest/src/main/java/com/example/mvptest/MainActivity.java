package com.example.mvptest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvptest.bean.HomeBean;
import com.example.mvptest.contract.HomeContract;
import com.example.mvptest.presenter.HomePresenter;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author xiayiye
 * 查看当前类的子父类关系快捷键：ctrl + h
 * 查看当前类的结构 alt + 7
 */
public class MainActivity extends Activity implements HomeContract.Views {

    private HomePresenter homePresenter;
    private EditText etInput;
    private static Gson gson;

    static {
        gson = new Gson();
    }

    private TextView tvShoeResult;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etInput = findViewById(R.id.et_input);
        tvShoeResult = findViewById(R.id.tv_shoe_result);
        homePresenter = new HomePresenter(this);
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("https://wanandroid.com/wxarticle/list/408/1/json").build();
        Log.e("打印线程1", Thread.currentThread().getName());
        new Thread() {
            @Override
            public void run() {
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("打印线程2", Thread.currentThread().getName() + "==" + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.e("打印线程3", Thread.currentThread().getName());
                        String data = response.body().string();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,data,Toast.LENGTH_LONG).show();
                            }
                        });
                        Log.e("打印结果：", data);
                    }
                });
            }
        }.start();
    }

    public void showResult(View view) {
        String s = etInput.getText().toString();
        homePresenter.setShowData(s);
    }

    @Override
    public void showSuccessData(HomeBean data) {
        String receiverData = gson.toJson(data);
        tvShoeResult.setText(receiverData);
//        Toast.makeText(getApplicationContext(), receiverData, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showFailData(String data) {
        tvShoeResult.setText(data);
    }
}
