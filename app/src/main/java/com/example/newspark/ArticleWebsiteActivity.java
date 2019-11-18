package com.example.newspark;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticleWebsiteActivity extends AppCompatActivity {

    private WebView webViewArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_website);

        webViewArticle = findViewById(R.id.webViewArticle);
        webViewArticle.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webViewArticle.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webViewArticle.loadUrl(getIntent().getStringExtra("url"));
    }
}
