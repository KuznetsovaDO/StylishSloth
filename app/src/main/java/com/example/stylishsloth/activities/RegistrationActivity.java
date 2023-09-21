package com.example.stylishsloth.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stylishsloth.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    TextInputEditText name_et, email_et, phone_et, password1_et, password2_et;
    TextInputLayout email_TIL, name_TIL, phone_TIL, password1_TIL, password2_TIL;
    static Resources resources;
    static int backgroundColor, bc2;
    static TextView message_tw;
    static String password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        name_et = findViewById(R.id.reg_name_edittext);
        email_et = findViewById(R.id.reg_email_edittext);
        phone_et = findViewById(R.id.reg_phone_edittext);
        password1_et = findViewById(R.id.reg_password1_edittext);
        password2_et = findViewById(R.id.reg_password2_edittext);
        email_TIL = findViewById(R.id.reg_email_inputlayout);
        name_TIL = findViewById(R.id.reg_name_inputlayout);
        phone_TIL = findViewById(R.id.reg_phone_inputlayout);
        password1_TIL = findViewById(R.id.reg_password1_inputlayout);
        password2_TIL = findViewById(R.id.reg_password2_inputlayout);
        message_tw = findViewById(R.id.message_registration);

        Button regisButton = findViewById(R.id.regis_button);
        ImageButton backToAuth = findViewById(R.id.back_to_auth);
        resources = getResources();
        backgroundColor = resources.getColor(R.color.translucent_red);
        bc2 = resources.getColor(R.color.translucent_black);
        password = password1_et.getText().toString();
        email = email_et.getText().toString();

//        email_TIL.setBoxBackgroundColor(backgroundColor);
        backToAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //переход к экрану авторизации
                Intent intentAuth = new Intent(RegistrationActivity.this, AuthorizationActivity.class);
                RegistrationActivity.this.finish();
                startActivity(intentAuth);
            }
        });
        regisButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                message_tw.setText("");
                changeFieldColor(name_TIL, bc2);
                changeFieldColor(email_TIL, bc2);
                changeFieldColor(phone_TIL, bc2);
                changeFieldColor(password1_TIL, bc2);
                changeFieldColor(password2_TIL, bc2);


                //проверяем, все ли поля заполнены
                if (!fieldsAreNotEmpty(name_TIL, phone_TIL, email_TIL, password1_TIL, password2_TIL)) {
                    message_tw.setText("Заполните все поля");
                    // выделяем цветом незаполненные поля
                    changeColors(backgroundColor, name_TIL, email_TIL, phone_TIL, password1_TIL, password2_TIL);

                } else if (!isValidEmail(email_TIL.getEditText().getText().toString())) {
                    message_tw.setText("Некорректный email");
                    changeFieldColor(email_TIL, backgroundColor);

                } else if (!isValidPhone(phone_et.getText().toString())) {
                    message_tw.setText("Некорректный номер телефона");
                    changeFieldColor(phone_TIL, backgroundColor);
                } else if (calculatePasswordStrength(password1_et.getText().toString()) < 8) {
                    message_tw.setText("Недостаточно надежный пароль");
                    changeFieldColor(password1_TIL, backgroundColor);
                } else if (!password1_et.getText().toString().equals(password2_et.getText().toString())) {
                    message_tw.setText("Пароли не совпадают");
                    changeFieldColor(password1_TIL, backgroundColor);
                    changeFieldColor(password2_TIL, backgroundColor);
                } else {
                    registrationUser();
                    addUsersData();
                }

            }
        });

    }

    //регистрация пользователя
    private void registrationUser() {
        mAuth.createUserWithEmailAndPassword(email_et.getText().toString(), password1_et.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("REGISTRATION", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(RegistrationActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else {
                    // If sign in fails, display a message to the user.
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException e) {
                        Log.w("REGISTRATION", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegistrationActivity.this, "Пользователь с таким email уже существует", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                }

            }
        });
    }

    //сохранение данных пользователя
    private void addUsersData() {
        Map<String, Object> user = new HashMap<>();
        user.put("Name", name_et.getText().toString());
        user.put("Phone", phone_et.getText().toString());
        user.put("Email", email_et.getText().toString());

        String id = db.collection("UserData").document().getId();

        db.collection("UsersData").document(id).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("REGISTRATION", "DocumentSnapshot successfully written!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("REGISTRATION", "Error writing document", e);
            }
        });
    }

    public boolean fieldsAreNotEmpty(@NonNull TextInputLayout field1, TextInputLayout field2, TextInputLayout field3, TextInputLayout field4, TextInputLayout field5) {
        if (field1.getEditText().getText().toString().equals("") || field2.getEditText().getText().toString().equals("") || field3.getEditText().getText().toString().equals("") || field4.getEditText().getText().toString().equals("") || field5.getEditText().getText().toString().equals("")) {
            return false;
        } else return true;

    }

    private void changeColors(int backgroundColor, @NonNull TextInputLayout field1, TextInputLayout field2, TextInputLayout field3, TextInputLayout field4, TextInputLayout field5) {
        if (field1.getEditText().getText().toString().equals("")) {
            field1.setBoxBackgroundColor(backgroundColor);
        }
        if (field2.getEditText().getText().toString().equals("")) {
            field2.setBoxBackgroundColor(backgroundColor);
        }
        if (field3.getEditText().getText().toString().equals("")) {
            field3.setBoxBackgroundColor(backgroundColor);
        }
        if (field4.getEditText().getText().toString().equals("")) {
            field4.setBoxBackgroundColor(backgroundColor);
        }
        if (field5.getEditText().getText().toString().equals("")) {
            field5.setBoxBackgroundColor(backgroundColor);
        }
    }

    private void changeFieldColor(TextInputLayout field, int color) {
        field.setBoxBackgroundColor(color);
    }

    private static int calculatePasswordStrength(String password) {

        //total score of password
        int iPasswordScore = 0;

        if (password.length() < 8) return 0;
        else if (password.length() >= 10) iPasswordScore += 2;
        else iPasswordScore += 1;

        //if it contains one digit, add 2 to total score
        if (password.matches("(?=.*[0-9]).*")) iPasswordScore += 2;

        //if it contains one lower case letter, add 2 to total score
        if (password.matches("(?=.*[a-z]).*")) iPasswordScore += 2;

        //if it contains one upper case letter, add 2 to total score
        if (password.matches("(?=.*[A-Z]).*")) iPasswordScore += 2;

        //if it contains one special character, add 2 to total score
        if (password.matches("(?=.*[~!@#$%^&*()_-]).*")) iPasswordScore += 2;

        return iPasswordScore;

    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) return true;
        else {
            message_tw.setText("Некорректный email");
            changeFieldColor(email_TIL, bc2);
            return false;
        }

    }

    private boolean isValidPhone(String phone) {
        Pattern pattern = Pattern.compile("^((8|\\+7)\\d{10})$");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) return true;
        else {
            message_tw.setText("Некорректный номер телефона");
            changeFieldColor(email_TIL, bc2);
            return false;
        }

    }

}