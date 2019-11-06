package com.example.newspark;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsHolder extends RecyclerView.ViewHolder {

    ImageView imageViewNews;
    TextView textViewTitle, textViewDate;
    ProgressBar progressBarParciality;

    public NewsHolder(@NonNull View itemView) {
        super(itemView);

        imageViewNews = itemView.findViewById(R.id.imageViewNews);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        textViewDate = itemView.findViewById(R.id.textViewDate);
        progressBarParciality = itemView.findViewById(R.id.progressBarParciality);
    }
}
