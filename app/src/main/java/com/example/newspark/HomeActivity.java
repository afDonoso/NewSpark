package com.example.newspark;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity {

    public enum TimeOfDay {
        Morning,
        Noon,
        Afternoon,
        Night
    }

    public final static String API_KEY = "3c326101fc3e4b2fb726e0ddcca84fbb";
    public final static String HOST = "https://api.cognitive.microsoft.com";
    public final static String PATH = "/bing/v7.0/news";

    public final static String GOOD_MORNING = "¡Buenos días, ";
    public final static String GOOD_AFTERNOON = "¡Buenas tardes, ";
    public final static String GOOD_NIGHT = "¡Buenas noches, ";
    public static final String TAG = "HomeActivity";

    private CardView cardViewWelcome;
    private TextView textViewGreetings;
    private ConstraintLayout constraintLayoutCardWelcome;
    private RecyclerView recyclerViewNews;
    private NewsAdapter newsAdapter;

    private String userEmail;

    private FirebaseFirestore db;

    private RequestQueue requestQueue;

    private ArrayList<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        requestQueue = Volley.newRequestQueue(this);
        newsList = new ArrayList<>();

        userEmail = getIntent().getStringExtra(SignupActivity.EMAIL_KEY);
        textViewGreetings = findViewById(R.id.textViewGreetings);
        constraintLayoutCardWelcome = findViewById(R.id.constraintLayoutCardWelcome);

        final Context context = this;

        cardViewWelcome = findViewById(R.id.cardViewWelcome);
        cardViewWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(intent);*/
            }
        });

        db.collection("newspark")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + "=>" + document.getData());
                                String name = document.get(SignupActivity.NAME_KEY).toString();
                                updateUI(name);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents", task.getException());
                        }
                    }
                });

        recyclerViewNews = findViewById(R.id.recyclerViewNews);
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));

        newsAdapter = new NewsAdapter(this, getNews());
        newsAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ArticleDetailActivity.class);
                /*Bundle bundle = new Bundle();
                bundle.putSerializable("news", getNews().get(recyclerViewNews.getChildAdapterPosition(view)));
                intent.putExtras(bundle);*/
                //intent.putExtra("url", )
                startActivity(intent);
            }
        });
        recyclerViewNews.setAdapter(newsAdapter);

    }

    private ArrayList<News> getNews() {
        String searchTerm = "Colombia Politica";
        try {
            new RetrieveNews().execute(searchTerm);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        /*String url = "https://newsapi.org/v2/top-headlines?" +
                "country=co&" +
                "apiKey=e5f4f0edca4743ebb3799d983dfb5931";
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<News> temp = new ArrayList<>();

                    JSONArray array = response.getJSONArray("articles");
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String title = object.getString("title");
                        String imageUrl = object.getString("urlToImage");
                        String date = object.getString("publishedAt");
                        int parcialityPercentage = (int) (Math.random() * 100);

                        temp.add(new News(title, date, imageUrl, parcialityPercentage));
                    }
                    System.out.println(newsList);
                    newsList = temp;
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(objectRequest);*/

        //newsList.add(new News("Conservadores proponen normalización tributaria en Ley de Financiamiento", "12 de noviembre de 2019", "https://www.elheraldo.co/sites/default/files/styles/width_860/public/articulo/2019/11/12/ley-financiamiento.jpg?itok=NgiyGoxF", 38));
        //newsList.add(new News("Roy Barreras denuncia amenazas tras moción a exmindefensa", "12 de noviembre de 2019", "https://www.elheraldo.co/sites/default/files/styles/width_860/public/articulo/2019/11/12/dd64a38d-0df3-4597-a3bb-602aecfa12ec.jpg?itok=iJPGuLqp", 85));

        return newsList;
    }

    /**
     * Actualiza la interfaz dada la hora del día.
     */
    public void updateUI(String userName) {
        TimeOfDay timeOfDay = getTimeOfDay();
        Log.d(TAG, timeOfDay.toString());

        if (timeOfDay.equals(TimeOfDay.Morning)) {
            textViewGreetings.setText(GOOD_MORNING + userName + "!");
            constraintLayoutCardWelcome.setBackgroundResource(R.drawable.background_greetings_morning);
        } else if (timeOfDay.equals(TimeOfDay.Noon)) {
            textViewGreetings.setText(GOOD_AFTERNOON + userName + "!");
            constraintLayoutCardWelcome.setBackgroundResource(R.drawable.background_greetings_noon);
        } else if (timeOfDay.equals(TimeOfDay.Afternoon)) {
            textViewGreetings.setText(GOOD_AFTERNOON + userName + "!");
            constraintLayoutCardWelcome.setBackgroundResource(R.drawable.background_greetings_afternoon);
        } else if (timeOfDay.equals(TimeOfDay.Night)) {
            textViewGreetings.setText(GOOD_NIGHT + userName + "!");
            constraintLayoutCardWelcome.setBackgroundResource(R.drawable.background_greetings_night);
        }
    }

    /**
     * Retorna el tiempo del día basándose en la hora del día.
     *
     * @return Morning, si es entre las 4 y las 11.
     * Noon, si es entre las 12 y las 14.
     * Afternoon, si es entre las 15 y las 18.
     * Night, de lo contrario.
     */
    public TimeOfDay getTimeOfDay() {
        int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        if (currentTime >= 4 && currentTime <= 11) {
            return TimeOfDay.Morning;
        } else if (currentTime >= 12 && currentTime <= 16) {
            return TimeOfDay.Noon;
        } else if (currentTime >= 17 && currentTime <= 18) {
            return TimeOfDay.Afternoon;
        } else {
            return TimeOfDay.Night;
        }
    }

}