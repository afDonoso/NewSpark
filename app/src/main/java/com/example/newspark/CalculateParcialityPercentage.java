package com.example.newspark;

import android.os.AsyncTask;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class CalculateParcialityPercentage extends AsyncTask<String, Void, Integer> {

    @Override
    protected Integer doInBackground(String... strings) {
        return calculateParcialityPercentage2(strings[0]);
    }

    public int calculateParcialityPercentageCorrect(String url) {
        int parcialityPercentage = -1;
        HttpsURLConnection urlConnection = null;
        InputStream is = null;
        OutputStream os = null;

        try {
            URL AIUrl = new URL("https://us-central1-valid-broker-255320.cloudfunctions.net/function-1");
            urlConnection = (HttpsURLConnection) AIUrl.openConnection();

            String json1 = "{\"text\":\"I love bananas and apples\",\"analyzeParams\":{\"url\":\"https://www.theguardian.com/uk-news/2019/nov/17/prince-andrew-calls-for-royal-to-say-sorry-and-speak-to-fbi\",\"features\":{\"sentiment\":{},\"emotion\":{}},\"returnAnalyzedText\":\"true\"}}";

            JSONObject jsonObject23 = new JSONObject(json1);
            jsonObject23.getJSONObject("analyzeParams").put("url", url);

            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setRequestProperty("Accept", "application/json");
            //urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Type", "application/json");

            os = new BufferedOutputStream(urlConnection.getOutputStream());
            os.write(jsonObject23.toString().getBytes("UTF-8"));

            //assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
            /*is = new BufferedInputStream(urlConnection.getInputStream());
            String resultS = new Scanner(is).useDelimiter("\\A").next();
            System.out.println("resultS: "+resultS);*/

            String resultS;
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                resultS = null;
                while ((resultS = br.readLine()) != null) {
                    response.append(resultS.trim());
                }
                System.out.println(response.toString());
            }


            JSONObject jsonObject = new JSONObject(resultS);
            String AT=jsonObject.getJSONObject("NLU").getJSONObject("result").getString("analyzed_text");


            double sentiment = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("sentiment").getJSONObject("document").getDouble("score");
            System.out.println("sentiment: "+sentiment);


            double sadness = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("sadness");
            double joy = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("joy");
            double fear = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("fear");
            double disgust = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("disgust");
            double anger = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("anger");

            //System.out.println("sadness: "+sadness);
            System.out.println("joy: "+joy);
            System.out.println("fear: "+fear);
            System.out.println("disgust: "+disgust);
            System.out.println("anger: "+anger);

            JSONObject R2= new JSONObject(json1);
            R2.put("text", AT);
            R2.getJSONObject("analyzeParams").getJSONObject("features").remove("emotion");
            R2.getJSONObject("analyzeParams").put("returnAnalyzedText", false);
            //System.out.println("R2: "+ R2.toString());

            os.write(R2.toString().getBytes("UTF-8"));

            String resultS2 = new Scanner(is).useDelimiter("\\A").next();
            System.out.println("resultS2: "+resultS2);

            JSONObject jsonObject2 = new JSONObject(resultS2);
            JSONArray tonos= jsonObject2.getJSONObject("tone").getJSONObject("result").getJSONObject("document_tone").getJSONArray("tones");

            double total=50;
            JSONObject tono=null;
            double tonoJoy=0;
            double tonoAnger=0;
            double tonoFear=0;
            double tonoSadness=0;
            double tonoAnalytical=0;
            double tonoTentative=0;

            for (int i = 0; i < tonos.length(); i++) {
                tono=tonos.getJSONObject(i);
                System.out.println(tonos.getJSONObject(i).toString());
                //anger, fear, sadness, joy
                //analytical, tentative
                // confident NO SE USA AUN.
                // 4 positivos, 2 negativos

                if(tono.getString("tone_id").equals("joy")) {//  positivo
                    tonoJoy=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("anger")) {//  negativo
                    tonoAnger=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("fear")) {//  negativo
                    System.out.println("entra fear");
                    tonoFear=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("sadness")) {// negativo
                    tonoSadness=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("analytical")) {//  disminuye Parcialidad
                    tonoAnalytical=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("tentative")) {//  aumenta parcialidad
                    tonoTentative=tono.getDouble("score");
                }
            }
            System.out.println("TONOS: ");
            System.out.println("Joy: "+tonoJoy);
            System.out.println("Anger: "+tonoAnger);
            System.out.println("Fear: "+tonoFear);
            System.out.println("Sasness: "+tonoSadness);
            System.out.println("Analytical: "+tonoAnalytical);
            System.out.println("Tentative: "+tonoTentative);

            //System.out.println(tonos.toList());
            double totalEmociones= (joy *(50))-(fear *(50/4))-(disgust *(50/4))-(anger *(50/4))-(sadness *(50/4));
            System.out.println("total Emociones: "+totalEmociones);
            double totalTonos=((tonoJoy *(50))-(tonoAnger *(50/3))-(tonoFear *(50/3))+(tonoSadness *(50/3)));
            System.out.println("total Tonos: "+totalTonos);
            total=(totalEmociones+totalTonos)+(sentiment*50);
            total = total*(1+(tonoTentative - tonoAnalytical));
            System.out.println("Parcialidad Total: " + total);
            parcialityPercentage = (int) total;

            urlConnection.disconnect();
            is.close();
            os.close();

        }catch (Exception err){
            err.printStackTrace();
        } finally {
            return parcialityPercentage;
        }
    }

    public int calculateParcialityPercentage2(String url) {
        int parcialityPercentage = -1;

        try {

            URL AIUrl = new URL("https://us-central1-valid-broker-255320.cloudfunctions.net/function-1");

            //CloseableHttpClient client = HttpClients.createDefault();
            //HttpPost httpPost = new HttpPost("https://us-central1-valid-broker-255320.cloudfunctions.net/function-1");// url del servicio AI

            String json1 = "{\"text\":\"I love bananas and apples\",\"analyzeParams\":{\"url\":\"https://www.theguardian.com/uk-news/2019/nov/17/prince-andrew-calls-for-royal-to-say-sorry-and-speak-to-fbi\",\"features\":{\"sentiment\":{},\"emotion\":{}},\"returnAnalyzedText\":\"true\"}}";

            JSONObject jsonObject23 = new JSONObject(json1);
            jsonObject23.getJSONObject("analyzeParams").put("url", url);

            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject23.toString());
            Request request = new Request.Builder()
                    .url(AIUrl)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String resultS = response.body().string();

            //assertThat(response.getStatusLine().getStatusCode(), equalTo(200));
            System.out.println("resultS: "+resultS);


            JSONObject jsonObject = new JSONObject(resultS);
            String AT=jsonObject.getJSONObject("NLU").getJSONObject("result").getString("analyzed_text");


            double sentiment = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("sentiment").getJSONObject("document").getDouble("score");
            System.out.println("sentiment: "+sentiment);


            double sadness = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("sadness");
            double joy = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("joy");
            double fear = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("fear");
            double disgust = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("disgust");
            double anger = jsonObject.getJSONObject("NLU").getJSONObject("result").getJSONObject("emotion").getJSONObject("document").getJSONObject("emotion").getDouble("anger");

            //System.out.println("sadness: "+sadness);
            System.out.println("joy: "+joy);
            System.out.println("fear: "+fear);
            System.out.println("disgust: "+disgust);
            System.out.println("anger: "+anger);

            JSONObject R2= new JSONObject(json1);
            R2.put("text", AT);
            R2.getJSONObject("analyzeParams").getJSONObject("features").remove("emotion");
            R2.getJSONObject("analyzeParams").put("returnAnalyzedText", false);
            //System.out.println("R2: "+ R2.toString());

            body = RequestBody.create(MediaType.parse("application/json"), R2.toString());
            request = new Request.Builder()
                    .url(AIUrl)
                    .post(body)
                    .build();
            response = client.newCall(request).execute();
            String resultS2 = response.body().string();
            System.out.println("resultS2: "+resultS2);

            JSONObject jsonObject2 = new JSONObject(resultS2);
            JSONArray tonos= jsonObject2.getJSONObject("tone").getJSONObject("result").getJSONObject("document_tone").getJSONArray("tones");

            double total=50;
            JSONObject tono=null;
            double tonoJoy=0;
            double tonoAnger=0;
            double tonoFear=0;
            double tonoSadness=0;
            double tonoAnalytical=0;
            double tonoTentative=0;

            for (int i = 0; i < tonos.length(); i++) {
                tono=tonos.getJSONObject(i);
                System.out.println(tonos.getJSONObject(i).toString());
                //anger, fear, sadness, joy
                //analytical, tentative
                // confident NO SE USA AUN.
                // 4 positivos, 2 negativos

                if(tono.getString("tone_id").equals("joy")) {//  positivo
                    tonoJoy=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("anger")) {//  negativo
                    tonoAnger=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("fear")) {//  negativo
                    System.out.println("entra fear");
                    tonoFear=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("sadness")) {// negativo
                    tonoSadness=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("analytical")) {//  disminuye Parcialidad
                    tonoAnalytical=tono.getDouble("score");
                }else if(tono.getString("tone_id").equals("tentative")) {//  aumenta parcialidad
                    tonoTentative=tono.getDouble("score");
                }
            }
            System.out.println("TONOS: ");
            System.out.println("Joy: "+tonoJoy);
            System.out.println("Anger: "+tonoAnger);
            System.out.println("Fear: "+tonoFear);
            System.out.println("Sasness: "+tonoSadness);
            System.out.println("Analytical: "+tonoAnalytical);
            System.out.println("Tentative: "+tonoTentative);

            //System.out.println(tonos.toList());
            double totalEmociones= (joy *(50))-(fear *(50/4))-(disgust *(50/4))-(anger *(50/4))-(sadness *(50/4));
            System.out.println("total Emociones: "+totalEmociones);
            double totalTonos=((tonoJoy *(50))-(tonoAnger *(50/3))-(tonoFear *(50/3))+(tonoSadness *(50/3)));
            System.out.println("total Tonos: "+totalTonos);
            total=(totalEmociones+totalTonos)+(sentiment*50);
            total = total*(1+(tonoTentative - tonoAnalytical));
            System.out.println("Parcialidad Total: " + total);
            parcialityPercentage = (int) Math.abs(total);
        }catch (Exception err){
            err.printStackTrace();
        } finally {
            return parcialityPercentage;
        }
    }
}
