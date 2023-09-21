package com.example.stylishsloth.helper_classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.stylishsloth.R;

import java.util.ArrayList;

public class ReviewsViewPagerAdapter extends PagerAdapter {
    private Context mContext;

    private ArrayList<Integer> reviewPictires;

    public ReviewsViewPagerAdapter(Context context, ArrayList<Integer>  resids) {
        this.mContext = context;
        this.reviewPictires = resids;
    }

    @Override
    public int getCount() {
        return reviewPictires.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {


        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.reviews_pager, container,
                false);

        ImageView review = itemView.findViewById(R.id.review_image);

        ImageView avatarImageView = itemView.findViewById(R.id.review_image);
        avatarImageView.setImageResource(reviewPictires.get(position));

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}