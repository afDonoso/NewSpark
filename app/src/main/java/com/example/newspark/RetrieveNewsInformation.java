package com.example.newspark;

import android.os.AsyncTask;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class RetrieveNewsInformation extends AsyncTask<JsonArray, Void, ArrayList<News>> {

    @Override
    protected ArrayList<News> doInBackground(JsonArray... jsonArrays) {
        JsonArray array = jsonArrays[0];
        ArrayList<News> newsList = new ArrayList<>();

        try {
            for (int i = 0; i < array.size(); i++) {
                JsonObject object = array.get(i).getAsJsonObject();

                String urlText = object.get("url").toString();
                String urlRight = urlText.substring(1, urlText.length() - 1);
                System.out.println(urlRight);

                Document doc = Jsoup.connect(urlRight).get();
                String title = doc.body().getElementsByTag("h1").text();
                String date = object.get("datePublished").getAsString();
                String image = object.get("image").getAsJsonObject().get("thumbnail").getAsJsonObject().get("contentUrl").getAsString();

                //newsList.add(new News(title, "", date, image, (int) (Math.random() * 100)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return newsList;
        }
    }
}
