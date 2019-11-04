package com.example.newspark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;
    private Button buttonSignin;
    private ProgressBar progressBarLogin;
    private TextView textViewForgotPassword;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        buttonSignin = findViewById(R.id.buttonSignin);
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = editTextLoginEmail.getText().toString().trim();
                String password = editTextLoginPassword.getText().toString();

                if(email.isEmpty() || password.isEmpty()) {
                    editTextLoginEmail.requestFocus();
                    editTextLoginEmail.setError("Llene todos los campos");
                } else {
                    buttonSignin.setText("");
                    progressBarLogin.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(LoginActivity.this, "¡Ingreso éxitoso!", Toast.LENGTH_SHORT).show();

                                    if (currentUser.isEmailVerified()) {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.putExtra(SignupActivity.EMAIL_KEY, email);
                                        LoginActivity.this.finish();
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(LoginActivity.this, VerifyEmailActivity.class);
                                        intent.putExtra(SignupActivity.EMAIL_KEY, email);
                                        LoginActivity.this.finish();
                                        startActivity(intent);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    try {
                                        buttonSignin.setText(R.string.Login);
                                        progressBarLogin.setVisibility(View.GONE);
                                        throw e;
                                    } catch (FirebaseAuthInvalidUserException invalidUser) {
                                        editTextLoginEmail.setError("No existe un usuario con este correo.");
                                        editTextLoginEmail.requestFocus();
                                    } catch (FirebaseAuthInvalidCredentialsException invalidPassword) {
                                        editTextLoginPassword.setError("La contraseña ingresada no corresponde con la del usuario registrado.");
                                        editTextLoginPassword.requestFocus();
                                    } catch (Exception e2) {
                                        Toast.makeText(LoginActivity.this, e2.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void onClickNoAccount(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        this.finish();
        startActivity(intent);
    }

    public void onClickForgotPassword(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
