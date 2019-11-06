package com.example.newspark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ArticleDetailActivity extends AppCompatActivity {

    private ImageView backButtonImage;

    private ImageView imageViewArticleDetail;
    private TextView textViewArticleDetailTitle;
    private TextView textViewArticleDetailBody;
    private TextView textViewArticleDetailSource;
    private TextView textViewArticleDetailAuthor;
    private ProgressBar progressBarArticleDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        backButtonImage = findViewById(R.id.imageViewBack);
        backButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        News news = (News) getIntent().getSerializableExtra("news");

        imageViewArticleDetail = findViewById(R.id.imageViewArticleDetail);
        imageViewArticleDetail.setImageResource(news.getImage());

        textViewArticleDetailBody = findViewById(R.id.textViewArticleDetailBody);
        textViewArticleDetailBody.setText(news.getArticle());

        textViewArticleDetailTitle = findViewById(R.id.textViewArticleDetailTitle);
        textViewArticleDetailTitle.setText(news.getTitle());

        textViewArticleDetailSource = findViewById(R.id.textViewArticleDetailSource);
        textViewArticleDetailSource.setText("Fuente: " + news.getSource());

        textViewArticleDetailAuthor = findViewById(R.id.textViewArticleDetailAuthor);
        textViewArticleDetailAuthor.setText("Autor: " + news.getAuthorName());

        progressBarArticleDetail = findViewById(R.id.progressBarArticleDetail);
        progressBarArticleDetail.setProgress(news.getParcialityPercentage());
    }
}
