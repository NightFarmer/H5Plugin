package com.nightfarmer.h5plugin.sample;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.nightfarmer.h5plugin.H5Plugin;
import com.nightfarmer.h5plugin.H5PluginObject;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final View v = findViewById(R.id.hehe);
        final WebView webView = (WebView) findViewById(R.id.webView);

        final AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this, com.nightfarmer.h5plugin.R.animator.alpha_in);
        View view = webView.getView();
        view.setAlpha(0);
        set.setTarget(view);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    set.start();
                }
            }
        });

        H5Plugin.initWebView(this, webView);

//        webView.loadUrl("file:///android_asset/www/index.html");
        webView.loadUrl("http://m.sui.taobao.org/demos/");

        Intent intent = getIntent();
        String myData = intent.getStringExtra("myData");
        Toast.makeText(MainActivity.this, "" + myData, Toast.LENGTH_SHORT).show();
    }


}
