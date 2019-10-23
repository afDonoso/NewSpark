package com.example.newspark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail;
    private EditText editTextLoginPassword;
    private Button buttonSignin;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);

        buttonSignin = findViewById(R.id.buttonSignin);
        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = editTextLoginEmail.getText().toString().trim();
                String password = editTextLoginPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "¡Ingreso éxitoso!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.putExtra(SignupActivity.EMAIL_KEY, email);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                try {
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
        });
    }

    public void onClickIngresar(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void onClickNoAccount(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }
}
