package com.example.newspark;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

public class ArticleWebsiteActivity extends AppCompatActivity {

    private WebView webViewArticle;
    private ImageButton imageButtonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_website);

        imageButtonBack = findViewById(R.id.imageButtonBack);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleWebsiteActivity.this.finish();
            }
        });

        webViewArticle = findViewById(R.id.webViewArticle);
        webViewArticle.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webViewArticle.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webViewArticle.loadUrl(getIntent().getStringExtra("url"));
    }
}
