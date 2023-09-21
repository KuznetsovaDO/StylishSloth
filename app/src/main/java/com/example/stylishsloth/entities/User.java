package com.example.stylishsloth.entities;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.stylishsloth.activities.ProfileActivity;
import com.example.stylishsloth.helper_classes.RecyclerViewSubscriptionsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class User extends Application {

    private String Name, Phone, Email, Adress;
    private String ID;
    ArrayList<Subscription> subscriptions = new ArrayList<>();

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAdress() {
        return Adress;
    }

    public void setAdress(String adress) {
        Adress = adress;
    }

    public ArrayList<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(ArrayList<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setUsersData(){
        final String[] id = new String[1];
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentEmail = currentUser.getEmail();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UsersData")
                .whereEqualTo("Email", currentEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                Email = document.get("Email").toString();
                                Name = document.get("Name").toString();
                                Phone = document.get("Phone").toString();
                               // Adress = document.get("Adress").toString();
                                id[0] = document.getId().toString();
                                //setSubscriptionData(document.getId().toString());
                                ID = id[0];

                            db.collection("UsersData")
                                    .document(ID)
                                    .collection("Subscribes")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                                    Subscription subscription = new Subscription(
                                                            document.getId(),
                                                            document.get("Type").toString(),
                                                            document.get("TopSize").toString(),
                                                            document.get("PantsSize").toString(),
                                                            document.get("FootSize").toString(),
                                                            document.get("Adress").toString(),
                                                            document.get("Payment").toString()
                                                    );

                                                    subscriptions.add(subscription);
                                                }

                                            }
                                        }

                                    });

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    public User() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public User(String name, String phone, String email) {
        Name = name;
        Phone = phone;
        Email = email;
    }
}
