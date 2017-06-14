package com.speedata.js;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWvContent;
    private static final String URL = "http://kfcx.gudouhotspring.com:8093/test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {
        mWvContent.getSettings().setJavaScriptEnabled(true);
        mWvContent.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWvContent.getSettings().setUseWideViewPort(true);
        mWvContent.getSettings().setLoadWithOverviewMode(true);
        mWvContent.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWvContent.setHorizontalScrollBarEnabled(false);
        mWvContent.loadUrl(URL);
        mWvContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }
        });
    }

    private void initView() {
        findViewById(R.id.btn_set).setOnClickListener(this);
        mWvContent = (WebView) findViewById(R.id.wv_content);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_set:
                mWvContent.loadUrl("javascript:setCardNumber(" + "'99999'" + ")");
                break;
        }
    }
}
