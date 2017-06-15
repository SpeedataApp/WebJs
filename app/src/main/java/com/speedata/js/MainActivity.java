package com.speedata.js;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.speedata.r6lib.IMifareManager;
import com.speedata.r6lib.R6Manager;

import static com.speedata.r6lib.R6Manager.CardType.MIFARE;

public class MainActivity extends AppCompatActivity {

    private WebView mWvContent;
    private static final String URL = "http://kfcx.gudouhotspring.com:8093/test";
    private IMifareManager dev; //定义对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initReadCard();
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
        mWvContent = (WebView) findViewById(R.id.wv_content);
    }

    private void initReadCard() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
            Toast.makeText(this, "启动失败", Toast.LENGTH_SHORT).show();
        }
        dev = R6Manager.getMifareInstance(MIFARE); //选择卡类型：Mifare1
        if (dev.InitDev() != 0) {
            Toast.makeText(this, "初始化失败", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    @Override
    public void onDestroy() {
        dev.ReleaseDev();
        super.onDestroy();
        Intent i = getIntent();
        setResult(RESULT_OK, i);
    }

    // 拦截手持机侧键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int key = event.getKeyCode(); //获取物理按键的key类型：比如音量键，power键等
        int key1 = event.getAction(); //获取某一物理按键的对应的事件类型；比如音量键的按下（down）事件，音量键的松开（up）事件
        if (key == KeyEvent.KEYCODE_F5) { //按下的是侧键
            if (key1 == KeyEvent.ACTION_DOWN) { //侧键按压事件
                getCode();
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

    private void getCode() {
        //search a valid card
        byte[] ID = dev.SearchCard();
        if (ID == null) {
            Toast.makeText(this, "没有寻到卡片", Toast.LENGTH_SHORT).show();
            return;
        }
        String IDString = "";
        for (byte a : ID) {
            IDString += String.format("%02X", a);
        }
        // IDString由16进制数转化为10进制数，card就是结果。需要传递card+"\n"
        long card = Long.parseLong(IDString, 16);
        String scard = card + "";
        mWvContent.loadUrl(String.format("javascript:setCardNumber(" + "'%s'" + ")", scard));

    }
}
