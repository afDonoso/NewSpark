package com.example.newspark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<News> newsList;
    private View.OnClickListener listener;

    public NewsAdapter(Context context, ArrayList<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_news, null);

        view.setOnClickListener(this);

        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        News news = newsList.get(position);

        holder.textViewTitle.setText(news.getTitle());
        holder.textViewDate.setText(news.getDate());
        holder.imageViewNews.setImageResource(news.getImage());
        holder.progressBarParciality.setProgress(news.getParcialityPercentage());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null) {
            listener.onClick(view);
        }
    }
}
