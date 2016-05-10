package com.nightfarmer.h5plugin;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by zhangfan on 2016/5/10 0010.
 */
public class H5Plugin {
    public static void initWebView(Activity activity, WebView webView) {

        //设置编码
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        //支持js
        webView.getSettings().setJavaScriptEnabled(true);
        //设置背景颜色 透明
//        webView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        //设置本地调用对象及其接口
        webView.addJavascriptInterface(new H5PluginObject(activity), "H5Plugin");
    }
}
