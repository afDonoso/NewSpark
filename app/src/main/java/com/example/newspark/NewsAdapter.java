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
        holder.textViewDate.setText(formatDate(news.getDate()));
        holder.imageViewNews.setImageBitmap(news.getImage());
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

    private String formatDate(String dateTime) {
        String date = dateTime.split("T")[0];
        String time = dateTime.split("T")[1]
                .substring(0, 5);

        int year = Integer.parseInt(date.split("-")[0]);
        int month = Integer.parseInt(date.split("-")[1]);
        int day = Integer.parseInt(date.split("-")[2]);

        return day + " de " + getMonthName(month) + " de " + year + ", " + time;
    }

    private String getMonthName(int month) {
        switch(month) {
            case 1:
                return "Enero";

            case 2:
                return "Febrero";

            case 3:
                return "Marzo";

            case 4:
                return "Abril";

            case 5:
                return "Mayo";

            case 6:
                return "Junio";

            case 7:
                return "Julio";

            case 8:
                return "Agosto";

            case 9:
                return "Septiembre";

            case 10:
                return "Octubre";

            case 11:
                return "Noviembre";

            case 12:
                return "Diciembre";

            default:
                return "No es un mes válido";
        }
    }
}
