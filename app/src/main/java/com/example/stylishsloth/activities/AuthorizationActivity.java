package com.example.stylishsloth.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stylishsloth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class AuthorizationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String email, password;
    TextInputEditText email_et, password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        //инициализация элементов управления
        email_et = findViewById(R.id.email_edittext);
        password_et = findViewById(R.id.password_edittext);
        mAuth = FirebaseAuth.getInstance();
        email = email_et.getText().toString();
        password = password_et.getText().toString();
        Button enterButton = findViewById(R.id.auth_button);
        TextView gotoRegistration = findViewById(R.id.goto_registration);

        //переход к окну регистрации
        gotoRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentReg = new Intent(AuthorizationActivity.this, RegistrationActivity.class);
                startActivity(intentReg);
            }
        });

        //обработка нажатия на кнопку "войти"
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView message = findViewById(R.id.message_textview);
                //если оба поля в форме заполнены
                if (fieldsIsNotEmpty(email_et, password_et)) {
                    //корректный email
                    if (isValidEmailAddress(email_et.getText().toString())) {
                        signIn();

                    } else {
                        message.setText("Некорректный email");
                        email_et.setText("");
                        email_et.setFocusable(true);
                    }
                } else {
                    message.setText("Заполните все поля");
                }

            }
        });

    }




    //метод авторизации
    private void signIn() {

        mAuth.signInWithEmailAndPassword(email_et.getText().toString(), password_et.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("AUTH", "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    //updateUI(user);
                    Intent intent = new Intent(AuthorizationActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("AUTH", "signInWithEmail:failure", task.getException());
                    TextView message = findViewById(R.id.message_textview);
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        password_et.setText("");
                        password_et.requestFocus();
                        message.setText("Неверный пароль");
                    } catch (FirebaseAuthInvalidUserException e) {
                        message.setText("Пользователя с таким email не существует");
                        email_et.setTextColor(Color.RED);
                        email_et.requestFocus();
                    } catch (Exception e) {
                        Log.e("AUTH", e.getMessage());
                    }


                }

            }
        });
    }
    //метод для валидации email
    public static boolean isValidEmailAddress(String email) {
        String emailFormate = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\." + "[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern p = Pattern.compile(emailFormate);
        if (email == null) {
            return false;
        }
        return p.matcher(email).matches();
    }

    //проверка на заполненность полей
    public static boolean fieldsIsNotEmpty(EditText email, EditText password) {
        if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            return false;
        } else return true;
    }
}