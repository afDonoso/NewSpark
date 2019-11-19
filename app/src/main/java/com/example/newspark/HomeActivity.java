package com.example.newspark;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    public final static String GOOD_MORNING = "¡Buenos días, ";
    public final static String GOOD_AFTERNOON = "¡Buenas tardes, ";
    public final static String GOOD_NIGHT = "¡Buenas noches, ";
    public static final String TAG = "HomeActivity";

    private TextView textViewGreetings;
    private ConstraintLayout constraintLayoutCardWelcome;
    private RecyclerView recyclerViewNews;
    private NewsAdapter newsAdapter;
    private BottomNavigationView bottomNavigationView;

    private String userEmail;

    private FirebaseFirestore db;

    private ArrayList<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        newsList = new ArrayList<>();

        userEmail = getIntent().getStringExtra(SignupActivity.EMAIL_KEY);
        textViewGreetings = findViewById(R.id.textViewGreetings);
        constraintLayoutCardWelcome = findViewById(R.id.constraintLayoutCardWelcome);

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
        recyclerViewNews.getRecycledViewPool().setMaxRecycledViews(0, 0);
        recyclerViewNews.setHasFixedSize(true);

        getNews();

        newsAdapter = new NewsAdapter(this, newsList);
        newsAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ArticleWebsiteActivity.class);
                intent.putExtra("url", newsList.get(recyclerViewNews.getChildAdapterPosition(view)).getUrl());
                startActivity(intent);
            }
        });
        recyclerViewNews.setAdapter(newsAdapter);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menuItemHome);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void getNews() {
        String searchTerm = "Colombia Politica";

        newsList.clear();

        try {
            JsonArray array = new RetrieveNews().execute(searchTerm).get();

            for(int i = 0; i < array.size(); i++) {
                JsonObject object = array.get(i).getAsJsonObject();

                String urlText = object.get("url").toString();
                String urlRight = urlText.substring(1, urlText.length() - 1);
                System.out.println(urlRight);
                String title = object.get("name").getAsString();
                String url = object.get("url").getAsString();
                String date = object.get("datePublished").getAsString();
                Bitmap image = null;
                if(object.get("image") != null) {
                    String imageUrl = object.get("image").getAsJsonObject().get("thumbnail").getAsJsonObject().get("contentUrl").getAsString();
                    image = new DownloadImageTask().execute(imageUrl).get();
                }

                newsList.add(new News(title, "", url, date, image, (int) (Math.random() * 100)));
            }

            Collections.sort(newsList);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
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