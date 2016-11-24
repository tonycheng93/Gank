package com.sky.gank;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class WebActivity extends AppCompatActivity {

    public static final String EXTRA_URL = "url";

    private WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String url = getIntent().getStringExtra(EXTRA_URL);

        mWebView = (WebView) findViewById(R.id.web_view);

        mWebView.loadUrl(url);
    }

    public static Intent newIntent(Context packageContext, String url) {
        Intent intent = new Intent(packageContext, WebActivity.class);
        intent.putExtra(EXTRA_URL, url);
        return intent;
    }
}
