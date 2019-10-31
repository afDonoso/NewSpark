package com.example.newspark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    public enum TimeOfDay{
        Morning,
        Noon,
        Afternoon,
        Night
    }

    public final static String GOOD_MORNING = "¡Buenos días, ";
    public final static String GOOD_AFTERNOON = "¡Buenas tardes, ";
    public final static String GOOD_NIGHT = "¡Buenas noches, ";
    public static final String TAG = "HomeActivity";

    private CardView cardView1;
    private CardView cardView2;
    private CardView cardViewWelcome;
    private TextView textViewGreetings;
    private ImageView imageViewTimeDay;
    private ConstraintLayout constraintLayoutCardWelcome;

    private String userEmail;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        userEmail = getIntent().getStringExtra(SignupActivity.EMAIL_KEY);
        textViewGreetings = findViewById(R.id.textViewGreetings);
        //imageViewTimeDay = findViewById(R.id.imageViewTimeDay);
        constraintLayoutCardWelcome = findViewById(R.id.constraintLayoutCardWelcome);

        final Context context = this;

        String nameTemp = "";

        cardView1 = findViewById(R.id.cardViewNews1);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleDetailActivity.class);
                startActivity(intent);
            }
        });

        cardView2 = findViewById(R.id.cardViewNews2);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArticleDetail2Activity.class);
                startActivity(intent);
            }
        });

        cardViewWelcome = findViewById(R.id.cardViewWelcome);
        cardViewWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
                intent.putExtra(SignupActivity.EMAIL_KEY, userEmail);
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
                            for(QueryDocumentSnapshot document: task.getResult()) {
                                Log.d(TAG, document.getId() + "=>" + document.getData());
                                String name = document.get(SignupActivity.NAME_KEY).toString();
                                updateUI(name);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents", task.getException());
                        }
                    }
                });
    }

    /**
     * Actualiza la interfaz dada la hora del día.
     */
    public void updateUI(String userName) {
        TimeOfDay timeOfDay = getTimeOfDay();
        Log.d(TAG, timeOfDay.toString());

        if(timeOfDay.equals(TimeOfDay.Morning)) {
            textViewGreetings.setText(GOOD_MORNING + userName + "!");
            //imageViewTimeDay.setImageResource(R.drawable.day);
            constraintLayoutCardWelcome.setBackgroundResource(R.drawable.background_greetings_morning);
        } else if(timeOfDay.equals(TimeOfDay.Noon)) {
            textViewGreetings.setText(GOOD_AFTERNOON + userName + "!");
            //imageViewTimeDay.setImageResource(R.drawable.noon);
            constraintLayoutCardWelcome.setBackgroundResource(R.drawable.background_greetings_noon);
        } else if(timeOfDay.equals(TimeOfDay.Afternoon)) {
            textViewGreetings.setText(GOOD_AFTERNOON + userName + "!");
            //imageViewTimeDay.setImageResource(R.drawable.afternoon);
            constraintLayoutCardWelcome.setBackgroundResource(R.drawable.background_greetings_afternoon);
        } else if(timeOfDay.equals(TimeOfDay.Night)) {
            textViewGreetings.setText(GOOD_NIGHT + userName + "!");
            //imageViewTimeDay.setImageResource(R.drawable.night);
            constraintLayoutCardWelcome.setBackgroundResource(R.drawable.background_greetings_night);
        }
    }

    /**
     * Retorna el tiempo del día basándose en la hora del día.
     * @return  Morning, si es entre las 4 y las 11.
     *          Noon, si es entre las 12 y las 14.
     *          Afternoon, si es entre las 15 y las 18.
     *          Night, de lo contrario.
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
