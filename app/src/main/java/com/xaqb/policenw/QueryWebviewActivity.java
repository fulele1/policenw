package com.xaqb.policenw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xaqb.policenw.Utils.LogUtils;


/**
 * Created by lenovo on 2016/12/2.
 */
public class QueryWebviewActivity extends BaseActivity {
    private QueryWebviewActivity instance;
    private WebView wvQuery;

    @Override
    public void initTitleBar() {
        setTitle(R.string.query);
        showBackwardView(true);
//        findViewById(R.id.iv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (wvQuery.canGoBack()) {
//                    wvQuery.goBack(); // goBack()表示返回webView的上一页面
//                } else if (!wvQuery.canGoBack()) {
//                    finish();
//                }
//            }
//        });
    }

    @Override
    public void initViews() {
        setContentView(R.layout.query_webview_activity);
        instance = this;
        assignViews();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        LogUtils.i(url);
        if (!url.isEmpty()) {
            wvQuery.loadUrl(url);
        }
    }

    private void assignViews() {
        wvQuery = (WebView) findViewById(R.id.wv_query);

        wvQuery.getSettings().setJavaScriptEnabled(true);
        wvQuery.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 允许js弹出窗口
        // 缩放开关
        wvQuery.getSettings().setSupportZoom(true);
        // 设置此属性，仅支持双击缩放，不支持触摸缩放（在android4.0是这样，其他平台没试过）
        // 设置是否可缩放
        wvQuery.getSettings().setBuiltInZoomControls(true);

//        wvQuery.addJavascriptInterface(new Object() {
//            @JavascriptInterface
//            public void callJavaMethod() {
//                finish();
//            }
//        }, "demo");

        // 如果设置了此属性，那么webView.getSettings().setSupportZoom(true);也默认设置为true
//        LogU.i(TAG, "webView访问的地址为:" + Consts.enterTest + "?accId=" + FuncUtil.getAccID(instance) + "&paperId=" + paperIdStr);
        wvQuery.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // 在当前的webview中跳转到新的url
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
        wvQuery.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                new AlertDialog.Builder(instance).setTitle("提示").setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                }).create().show();

                return true;
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}
