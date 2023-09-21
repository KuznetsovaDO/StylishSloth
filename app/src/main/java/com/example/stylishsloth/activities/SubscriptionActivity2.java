package com.example.stylishsloth.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.stylishsloth.R;
import com.example.stylishsloth.entities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class SubscriptionActivity2 extends AppCompatActivity  {

    private static final List<String> EMPTY = new ArrayList<>();

    private AutoCompleteTextView textView;
    private Toast toast;
   // HashMap<String, String> subscription = (HashMap<String, String>) getIntent().getSerializableExtra("subscription");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription2);

        Intent intent = getIntent();
        HashMap<String, String> subscription = (HashMap<String, String>)intent.getSerializableExtra("subscription");
        EditText cityET = findViewById(R.id.cityEditText);
        User user = (User) getApplicationContext();
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscriptionActivity2.this.finish();
            }
        });

        Button toPayment = findViewById(R.id.goToPaymentBtn);
        toPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscription.put("Adress", cityET.getText().toString());
                subscription.put("Payment", "true");

                db.collection("UsersData").document(user.getID()).collection("Subscribes").document()
                        .set(subscription)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });
            }
        });



    }


}