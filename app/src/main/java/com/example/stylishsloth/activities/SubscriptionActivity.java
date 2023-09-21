package com.example.stylishsloth.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stylishsloth.R;
import com.example.stylishsloth.entities.User;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubscriptionActivity extends AppCompatActivity {

    String[] sizes = {"XS", "S", "M", "L", "XL", "XXL"};
    String[] shoe_sizes = {"36", "37", "38", "39", "40", "41", "42", "43", "44", "45"};
    List<String> height = new ArrayList<>();
    //Subscription subscription = new Subscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        User user = (User) getApplicationContext();;
        MaterialCardView type1 = findViewById(R.id.type1_cardview);
        MaterialCardView type2 = findViewById(R.id.type2_cardview);
        MaterialCardView type3 = findViewById(R.id.type3_cardview);
        Spinner spinner_top_sizes = findViewById(R.id.spinner);
        Spinner spinner_bottom_sizes = findViewById(R.id.spinner2);
        Spinner spinner_shoes_sizes = findViewById(R.id.spinner4);
        Spinner spinner_height = findViewById(R.id.spinner3);
        // Создаем адаптер ArrayAdapter с помощью массива строк и стандартной разметки элемета spinner
        ArrayAdapter<String> sizesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sizes);
        // Определяем разметку для использования при выборе элемента
        sizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Применяем адаптер к элементу spinner
        spinner_bottom_sizes.setAdapter(sizesAdapter);
        spinner_top_sizes.setAdapter(sizesAdapter);
        ArrayAdapter<String> shoeSizesAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, shoe_sizes);
        shoeSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_shoes_sizes.setAdapter(shoeSizesAdapter);

        type1.setChecked(true);

        for (int i = 148; i <= 210; i++) {
            height.add(String.valueOf(i));
        }
        ArrayAdapter<String> heightAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, height);
        shoeSizesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_height.setAdapter(heightAdapter);

        Button nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> subscription = new HashMap<>();
                subscription.put("TopSize", spinner_top_sizes.getSelectedItem().toString());
                subscription.put("PantsSize",spinner_bottom_sizes.getSelectedItem().toString());
                subscription.put("FootSize", spinner_shoes_sizes.getSelectedItem().toString());
                if (type1.isChecked()==true) subscription.put("Type", "Чёрная");
                if (type2.isChecked()==true) subscription.put("Type", "Базовая");
                if (type3.isChecked()==true) subscription.put("Type", "Все включено");

                Intent intent = new Intent(SubscriptionActivity.this, SubscriptionActivity2.class);
                intent.putExtra("subscription", subscription);
                startActivity(intent);

            }
        });

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubscriptionActivity.this.finish();
            }
        });

        TextView price = findViewById(R.id.priceTv);
        price.setText("3500 руб.");
        type1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type1.setCheckable(true);
                if (!type1.isChecked()) {
                    type2.setChecked(false);
                    type1.setChecked(true);
                    type3.setChecked(false);
                    price.setText("3500 руб.");
                }
                else{
                    type1.setChecked(false);
                }

            }
        });



        type2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type2.setCheckable(true);
                if (!type2.isChecked()) {
                    type1.setChecked(false);
                    type2.setChecked(true);
                    type3.setChecked(false);
                    price.setText("2500 руб.");
                }
                else{

                    type2.setChecked(false);
                }
            }
        });

        type3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type3.setCheckable(true);
                if (!type3.isChecked()) {
                    type3.setChecked(true);
                    type2.setChecked(false);
                    type1.setChecked(false);
                    price.setText("4500 руб.");
                }
                else{

                    type3.setChecked(false);
                }

            }
        });
    }


    }
