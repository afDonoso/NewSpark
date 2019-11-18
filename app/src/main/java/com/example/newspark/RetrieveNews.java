package com.example.newspark;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class RetrieveNews extends AsyncTask<String, Void, JsonArray> {

    @Override
    protected JsonArray doInBackground(String... strings) {
        ArrayList<News> newsList = new ArrayList<>();

        try {
            // construct the search request URL (in the form of URL + query string)
            URL url = new URL(HomeActivity.HOST + HomeActivity.PATH + "?q=" + URLEncoder.encode(strings[0], "UTF-8"));
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", HomeActivity.API_KEY);
            connection.setConnectTimeout(120000);
            // receive JSON body
            InputStream stream = connection.getInputStream();
            String response = new Scanner(stream).useDelimiter("\\A").next();
            // construct result object for return
            SearchResults results = new SearchResults(new HashMap<String, String>(), response);
            // extract Bing-related HTTP headers
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (String header : headers.keySet()) {
                if (header == null) continue;      // may have null key
                if (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-")) {
                    results.relevantHeaders.put(header, headers.get(header).get(0));
                }
            }
            stream.close();

            System.out.println("\nRelevant HTTP Headers:\n");
            for (String header : results.relevantHeaders.keySet())
                System.out.println(header + ": " + results.relevantHeaders.get(header));
            System.out.println("\nJSON Response:\n");
            System.out.println(prettify(results.jsonResponse));

            /**
             * Arreglo de noticias en json.
             */
            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(results.jsonResponse).getAsJsonObject();
            JsonArray array = json.getAsJsonArray("value");
            /*for(int i = 0; i < array.size(); i++) {
                JsonObject object = array.get(i).getAsJsonObject();

                String urlText = object.get("url").toString();
                String urlRight = urlText.substring(1, urlText.length() - 1);
                System.out.println(urlRight);

                Document doc = Jsoup.connect(urlRight).get();
                String title = doc.body().getElementsByTag("h1").text();
                String date = object.get("datePublished").getAsString();
                System.out.println(object.get("image"));
                System.out.println("URL OBJECT: " + object.get("image").getAsJsonObject().get("thumbnail").getAsJsonObject().get("contentUrl"));
                System.out.println("URL: " + object.get("image").getAsJsonObject().get("thumbnail").getAsJsonObject().get("contentUrl").getAsString());
                String image = object.get("image").getAsJsonObject().get("thumbnail").getAsJsonObject().get("contentUrl").getAsString();

                newsList.add(new News(title, "", date, image, (int) (Math.random() * 100)));
            }*/

            return array;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String prettify(String json_text) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(json_text).getAsJsonObject();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(json);
    }
}

class SearchResults {
    HashMap<String, String> relevantHeaders;
    String jsonResponse;

    SearchResults(HashMap<String, String> headers, String json) {
        relevantHeaders = headers;
        jsonResponse = json;
    }
}