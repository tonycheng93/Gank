package com.sky.gank.mvp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.sky.gank.R;
import com.sky.gank.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GankDetailActivity extends BaseActivity {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.progressbar)
    NumberProgressBar mProgressBar;
    @BindView(R.id.tv_title)
    TextSwitcher mSwitcher;
    @BindView(R.id.web_view)
    WebView mWebView;

    private String mTitle;
    private String mUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank_detail);
        ButterKnife.bind(this);

        getData();

        initWebView();
        mWebView.loadUrl(mUrl);

        setSupportActionBar(mToolbar);

        mSwitcher.setFactory(mFactory);
        mSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        if (mTitle != null) {
            setTitle(mTitle);
        }

        mWebView.loadUrl(mUrl);

        mToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new WebClient());
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mSwitcher.setText(title);
    }

    private void getData() {
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);
        mUrl = getIntent().getStringExtra(EXTRA_URL);
    }

    public static Intent newIntent(Context packageContext, String url, String title) {
        Intent intent = new Intent(packageContext, GankDetailActivity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {

            final TextView textView = new TextView(GankDetailActivity.this);
            textView.setTextAppearance(GankDetailActivity.this, R.style.WebTitle);
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView.setSelected(true);
                }
            }, 1378);
            return textView;
        }
    };

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private class WebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) {
                view.loadUrl(url);
            }
            return true;
        }
    }
}
