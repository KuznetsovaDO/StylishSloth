package com.example.stylishsloth.activities;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.stylishsloth.helper_classes.DepthPageTransformer;
import com.example.stylishsloth.helper_classes.TypesViewPagerAdapter;
import com.example.stylishsloth.R;
import com.example.stylishsloth.helper_classes.RecyclerViewQuestionsAdapter;
import com.example.stylishsloth.helper_classes.StoriesItem;
import com.example.stylishsloth.entities.User;
import com.example.stylishsloth.helper_classes.ReviewsViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    public static ViewPager2 pager;
    ImageButton gotoProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // определяем элементы управления в разметке
        gotoProfileButton = findViewById(R.id.gotoProfileButton);

        ViewPager viewPager = findViewById(R.id.viewpager_main2);
        pager = findViewById(R.id.viewpager_main1);

        User user = (User) getApplicationContext();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser!= null){
            user.setUsersData();
        }

        //работа с основным ViewPager (с типами подписок) на главном экране
        //создаем и устанавливаем адаптер
        FragmentStateAdapter pageAdapter = new TypesViewPagerAdapter(this);
        pager.setAdapter(pageAdapter);

        //анимация при пролистывании
        pager.setPageTransformer(new DepthPageTransformer());

        //работа с RecyclerView (вопросы и ответы)
        RecyclerView rvStories = findViewById(R.id.recyclerView);

        ArrayList<StoriesItem> items = new ArrayList<>();
        items.add(new StoriesItem("Что такое подписка на одежду?", R.drawable.stories2));
        items.add(new StoriesItem("Столько стоит?", R.drawable.main_fragment2));
        items.add(new StoriesItem("Оплата", R.drawable.main_fragment3));
        items.add(new StoriesItem("Доставка", R.drawable.stories2));
        items.add(new StoriesItem("Возврат", R.drawable.main_fragment1));

        RecyclerViewQuestionsAdapter adapter = new RecyclerViewQuestionsAdapter(items);
        rvStories.setAdapter(adapter);
        //rvStories.setLayoutManager(new CarouselLayoutManager());
        rvStories.setLayoutManager(new LinearLayoutManager(
                MainActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false));

        //БЛОК С ОТЗЫВАМИ
        ArrayList<Integer> pictureIds = new ArrayList<Integer>();
        pictureIds.add(R.drawable.review4);
        pictureIds.add(R.drawable.review4);

        ArrayList<Integer> examplesPictures = new ArrayList<Integer>();
        pictureIds.add(R.drawable.review4);
        pictureIds.add(R.drawable.review4);

        ViewPager examplesViewPager = findViewById(R.id.examplesViewPager);
        PagerAdapter adapterVP = new ReviewsViewPagerAdapter(MainActivity.this, pictureIds);

        gotoProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser != null){

                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                }
                else{
                    Intent authIntent = new Intent(MainActivity.this, AuthorizationActivity.class);
                    startActivity(authIntent);
                }

            }
        });


        PagerAdapter adapterV = new ReviewsViewPagerAdapter(MainActivity.this, pictureIds);
        viewPager.setAdapter(adapterV);
        viewPager.setAdapter(adapterV);
        gotoProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser != null){

                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                }
                else{
                    Intent authIntent = new Intent(MainActivity.this, AuthorizationActivity.class);
                    startActivity(authIntent);
                }

            }
        });

        Button subscribtion_button = findViewById(R.id.subscription_button);
        subscribtion_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    Intent intent = new Intent(MainActivity.this, SubscriptionActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(MainActivity.this, AuthorizationActivity.class);
                    startActivity(intent);
                }
            }
        });



    }

    public static void scrollPager(){
        pager.setCurrentItem(pager.getCurrentItem()+1);

    }

}

