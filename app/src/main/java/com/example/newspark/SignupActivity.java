package com.example.newspark;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    public final static String NAME_KEY = "name";
    public final static String LASTNAME_KEY = "lastName";
    public final static String EMAIL_KEY = "email";
    public final static String BIRTHDATE_KEY = "birthDate";
    public static final String TAG = "SignUpActivity";
    private static final String CERO = "0";
    private static final String BARRA = "/";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("newspark/usuario");

    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextBirthDate;
    private EditText editTextPassword;
    private EditText editTextVerifyPassword;

    private Button buttonSignUp;

    private ImageButton imageButtonDatePicker;

    private ProgressBar progressBarSignup;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextBirthDate = findViewById(R.id.editTextBirthDate);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextVerifyPassword = findViewById(R.id.editTextVerifyPassword);
        progressBarSignup = findViewById(R.id.progressBarSignup);
        imageButtonDatePicker = findViewById(R.id.imageButtonDatePicker);

        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = editTextName.getText().toString();
                final String lastName = editTextLastName.getText().toString();
                final String email = editTextEmail.getText().toString();
                final String birthDate = editTextBirthDate.getText().toString();
                final String password = editTextPassword.getText().toString();
                String verifyPassword = editTextVerifyPassword.getText().toString();

                // Verificación de que los campos no estén vacíos
                if(name.isEmpty()) {
                    editTextName.setError("Por favor ingrese su nombre");
                    editTextName.requestFocus();
                } else if (lastName.isEmpty()) {
                    editTextLastName.setError("Por favor ingrese su apellido");
                    editTextLastName.requestFocus();
                } else if (email.isEmpty()) {
                    editTextEmail.setError("Por favor ingrese su correo");
                    editTextEmail.requestFocus();
                } else if (birthDate.isEmpty()) {
                    editTextBirthDate.setError("Por favor ingrese su fecha de nacimiento");
                    editTextBirthDate.requestFocus();
                } else if (password.isEmpty()) {
                    editTextPassword.setError("Por favor ingrese una contraseña");
                    editTextPassword.requestFocus();
                } else if (!password.equals(verifyPassword)) {
                    editTextVerifyPassword.setText("");
                    editTextVerifyPassword.setError("Las contraseñas no coinciden");
                    editTextVerifyPassword.requestFocus();
                } else {
                    progressBarSignup.setVisibility(View.VISIBLE);
                    buttonSignUp.setText("");

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()) {
                                try {
                                    buttonSignUp.setText(R.string.Signup);
                                    progressBarSignup.setVisibility(View.GONE);
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                    editTextPassword.setError("La contraseña es muy débil");
                                    editTextPassword.requestFocus();
                                } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                    editTextEmail.setError("Ingrese un correo válido");
                                    editTextEmail.requestFocus();
                                } catch (FirebaseAuthUserCollisionException emailExists) {
                                    editTextEmail.setError("Ya existe un usuario con este correo");
                                    editTextEmail.requestFocus();
                                } catch (Exception e) {
                                    Toast.makeText(SignupActivity.this,
                                            "Surgió un error al crear su cuenta. Por favor intente nuevamente",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                currentUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        User user = new User(
                                                name,
                                                lastName,
                                                email,
                                                birthDate
                                        );

                                        db.collection("newspark").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.d(TAG, "Se agregó un nuevo usuario");
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "No se pudo agregar al nuevo usuario", e);
                                            }
                                        });

                                        Intent intent = new Intent(SignupActivity.this, VerifyEmailActivity.class);
                                        SignupActivity.this.finish();
                                        startActivity(intent);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignupActivity.this, "No se pudo enviar el correo de verificación.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }


            }
        });
    }

    public void onClickAlreadyUser(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        this.finish();
        startActivity(intent);
    }

    public void onClickImageButtonDatePicker(View view) {
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                editTextBirthDate.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }
}
