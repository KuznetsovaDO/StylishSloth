package com.example.stylishsloth.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stylishsloth.R;
import com.example.stylishsloth.helper_classes.RecyclerViewSubscriptionsAdapter;
import com.example.stylishsloth.entities.Subscription;
import com.example.stylishsloth.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    Button editDataBtn;
    EditText emailEditText, adressEditText, phoneEditText, currentPasswordET, newPasswordET, nameEditText;



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    User user;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editDataBtn = findViewById(R.id.myDataButton);

        user = (User) getApplicationContext();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(ProfileActivity.this, AuthorizationActivity.class);
            startActivity(intent);
        }
        else {
            user.setUsersData();
        }

        TextView emailTV = findViewById(R.id.userEmailTextView);
        emailTV.setText(user.getEmail());
        TextView nameTV = findViewById(R.id.textView2);
        nameTV.setText(user.getName());
        RecyclerView rv = findViewById(R.id.recyclerView);

        RecyclerViewSubscriptionsAdapter rvadapter = new RecyclerViewSubscriptionsAdapter(user.getSubscriptions(), user.getID());
        rv.setAdapter(rvadapter);
        rv.setLayoutManager(new LinearLayoutManager(
                ProfileActivity.this,
                LinearLayoutManager.VERTICAL,
                false));





        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.this.finish();
            }
        });

        ImageButton exit = findViewById(R.id.exitBtn);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(ProfileActivity.this, AuthorizationActivity.class);
                startActivity(intent);

            }
        });


        editDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Получаем вид с файла prompt.xml, который применим для диалогового окна:
                LayoutInflater li = LayoutInflater.from(ProfileActivity.this);
                View promptsView = li.inflate(R.layout.edit_user_data_dialog, null);

                Button saveChangesBtn = promptsView.findViewById(R.id.saveChangesButton);

                nameEditText = promptsView.findViewById(R.id.nameET);
                phoneEditText = promptsView.findViewById(R.id.phoneET);



                nameEditText.setText(user.getName());
                phoneEditText.setText(user.getPhone());
                //adressEditText.setText(user.getAdress());

                //Создаем AlertDialog
                AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(ProfileActivity.this, R.style.CustomAlertDialog);

                //Настраиваем prompt.xml для нашего AlertDialog:
                mDialogBuilder.setView(promptsView);

                //Создаем AlertDialog:
                AlertDialog alertDialog = mDialogBuilder.create();

                //и отображаем его:
                alertDialog.show();


                saveChangesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        DocumentReference washingtonRef = db.collection("UsersData").document(user.getID());
                        washingtonRef
                                .update("Phone", phoneEditText.getText().toString(),
                                            "Name", nameEditText.getText().toString(),
                                                            "Email", emailEditText.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully updated!");
                                        alertDialog.hide();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error updating document", e);
                                    }
                                });


                    }
                });
            }
        });


    }


}