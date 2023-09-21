package com.example.stylishsloth.helper_classes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class TypesViewPagerAdapter extends FragmentStateAdapter {
    public ArrayList <Fragment> fragments = new ArrayList<>();

    public TypesViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments.add(new TypesFragment1());
        fragments.add(new TypesFragment2());
        fragments.add(new TypesFragement3());


    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return(fragments.get(position));
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}