package com.example.newspark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class VerifyEmailActivity extends AppCompatActivity {

    public static final String TAG = "HomeActivity";

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    private Button buttonResendEmail;

    private TextView textViewVerifyEmailAddress;
    private TextView textViewVerifyEmailWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();



        textViewVerifyEmailAddress = findViewById(R.id.textViewVerifyEmailAddress);
        textViewVerifyEmailAddress.setText(user.getEmail());

        textViewVerifyEmailWelcome = findViewById(R.id.textViewVerifyEmailWelcome);

        db.collection("newspark")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document: task.getResult()) {
                                Log.d(TAG, document.getId() + "=>" + document.getData());
                                String name = document.get(SignupActivity.NAME_KEY).toString();
                                textViewVerifyEmailWelcome.setText("¡Hola, " + name + "!");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents", task.getException());
                        }
                    }
                });

        buttonResendEmail = findViewById(R.id.buttonResendEmail);
        buttonResendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(VerifyEmailActivity.this, "Se envió el correo de verificación", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerifyEmailActivity.this, "No se pudo enviar el correo. Intenta nuevamente", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(user.isEmailVerified()) {
            startHomeActivity();
        }
    }

    /**
     * Comienza la actividad de Home.
     */
    public void startHomeActivity() {
        Intent intent = new Intent(VerifyEmailActivity.this, HomeActivity.class);
        this.finish();
        startActivity(intent);
    }
}
