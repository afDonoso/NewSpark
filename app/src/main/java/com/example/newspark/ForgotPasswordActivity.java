package com.example.newspark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {

    public final static String CORREO_ENVIADO = "Ya te enviamos las instrucciones para recuperar tu contraseña al correo.";
    public final static String CORREO_NO_ENVIADO = "No pudimos enviar un correo a la dirección especificado. Vuelve a intentarlo";

    private EditText editTextForgotPasswordEmail;
    private Button buttonRecoverPassword;
    private Button buttonForgotPasswordCancel;
    private TextView textViewForgotPassword;
    private ProgressBar progressBarForgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);
        editTextForgotPasswordEmail = findViewById(R.id.editTextForgotPasswordEmail);
        progressBarForgotPassword = findViewById(R.id.progressBarForgotPassword);

        buttonRecoverPassword = findViewById(R.id.buttonRecoverPassword);
        buttonRecoverPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextForgotPasswordEmail.getText().toString();

                if(email.isEmpty()) {
                    editTextForgotPasswordEmail.setError("Ingrese un correo");
                    editTextForgotPasswordEmail.requestFocus();
                } else {

                    progressBarForgotPassword.setVisibility(View.VISIBLE);
                    buttonRecoverPassword.setText("");

                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBarForgotPassword.setVisibility(View.INVISIBLE);
                                    buttonRecoverPassword.setText(R.string.Recover_password);
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    textViewForgotPassword.setText(CORREO_ENVIADO);
                                    textViewForgotPassword.setTextColor(Color.parseColor("#FF769D00"));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    textViewForgotPassword.setText(CORREO_NO_ENVIADO);
                                    textViewForgotPassword.setTextColor(Color.parseColor("#FFCC0000"));
                                }
                            });
                }
            }
        });

        buttonForgotPasswordCancel = findViewById(R.id.buttonForgotPasswordCancel);
        buttonForgotPasswordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasswordActivity.this.finish();
            }
        });
    }
}
