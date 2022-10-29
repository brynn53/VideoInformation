package com.example.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;

import com.example.myapp.R;
import com.example.myapp.jsbridge.BridgeHandler;
import com.example.myapp.jsbridge.BridgeWebView;
import com.example.myapp.jsbridge.CallBackFunction;

//java与js页面通信
@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity {
    private BridgeWebView bridgeWebView;
    private String url;

    @Override
    protected int initLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        bridgeWebView = findViewById(R.id.bridgeWebView);
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
        registerJavaHandler();
        initWebView();
    }

    private void initWebView() {
        WebSettings settings = bridgeWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        bridgeWebView.loadUrl(url);
    }

    private void registerJavaHandler() {
        //js中预留给java注册的返回方法名称,注册js的返回方法，js中点击返回时会调用java中的回调方法
        bridgeWebView.registerHandler("goback", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                finish(); //关闭当前webActivity
            }
        });
    }
}