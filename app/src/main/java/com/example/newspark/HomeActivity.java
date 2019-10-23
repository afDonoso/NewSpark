package com.example.newspark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeActivity extends AppCompatActivity {

    public final static String GOOD_MORNING = "¡Buenos días,";
    public final static String GOOD_AFTERNOON = "¡Buenas tardes,";
    public final static String GOOD_NIGHT = "¡Buenas noches,";
    public static final String TAG = "HomeActivity";

    private CardView cardView1;
    private CardView cardView2;
    private CardView cardViewWelcome;
    private TextView textViewGreetings;

    private String userEmail;
    public String name;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        userEmail = getIntent().getStringExtra(SignupActivity.EMAIL_KEY);
        textViewGreetings = findViewById(R.id.textViewGreetings);

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
                                HomeActivity.this.setName(document.get(SignupActivity.NAME_KEY).toString());
                                //HomeActivity.this.name = document.get(SignupActivity.NAME_KEY).toString();
                                textViewGreetings.setText(GOOD_AFTERNOON + " " + name + "!");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents", task.getException());
                        }
                    }
                });
    }

    public void setName(String name) {
        this.name = name;
    }


}
