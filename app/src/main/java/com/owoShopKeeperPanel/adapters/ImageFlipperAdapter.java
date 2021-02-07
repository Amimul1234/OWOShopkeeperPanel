package com.owoShopKeeperPanel.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.owoShopKeeperPanel.R;

import java.util.ArrayList;
import java.util.List;

public class ImageFlipperAdapter extends RecyclerView.Adapter<ImageFlipperAdapter.ViewHolder>{

    private Context mCtx;
    private List<String> images = new ArrayList<String>();
    private FragmentManager fragmentManager;
    private Lifecycle lifecycle;

    public ImageFlipperAdapter(Context mCtx, List<String> images, FragmentManager fragmentManager, Lifecycle lifecycle) {
        this.mCtx = mCtx;
        this.images.addAll(images);
        this.fragmentManager = fragmentManager;
        this.lifecycle = lifecycle;
    }

    @NonNull
    @Override
    public ImageFlipperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.banner_slider, parent, false);
        return new ImageFlipperAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageFlipperAdapter.ViewHolder holder, int position) {
        ViewHolder bannerFlipper = holder;
        fliptheView(bannerFlipper.bannerFlipper);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewFlipper bannerFlipper;
        public TabLayout tabLayout;
        public ViewPager2 viewPager2;
        public ViewPagerAdapter viewPagerAdapter;


        public ViewHolder(@NonNull View itemView){
            super(itemView);
            bannerFlipper = itemView.findViewById(R.id.view_flipper_offer);

            tabLayout = itemView.findViewById(R.id.tabbed_layout);
            viewPager2 = itemView.findViewById(R.id.category_and_brand_showing);
            viewPagerAdapter = new ViewPagerAdapter(fragmentManager, lifecycle);

            viewPager2.setAdapter(viewPagerAdapter);

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager2.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    tabLayout.selectTab(tabLayout.getTabAt(position));
                }
            });


            DisplayMetrics displaymetrics = new DisplayMetrics(); //Resizing things dynamically
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels;

            int deviceheight = devicewidth / 2;

            bannerFlipper.getLayoutParams().width = devicewidth;

            bannerFlipper.getLayoutParams().height = deviceheight;
        }
    }

    private void fliptheView(ViewFlipper banner) {

        int size = images.size();

        for(int i=0; i<size; i++)
        {
            flipperImage(images.get(i), banner);
        }

    }

    public void flipperImage(String image, ViewFlipper viewFlipper)
    {
        ImageView imageView=new ImageView(mCtx);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(mCtx).load(image).into(imageView);
        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(6000);
        viewFlipper.setAutoStart(true);
        viewFlipper.startFlipping();
        viewFlipper.setInAnimation(mCtx, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(mCtx, R.anim.slide_out_left);
    }

    public void updateItems(List<String> newList) {    //this method is for handling async image responses
        images.clear();
        images.addAll(newList);
        notifyDataSetChanged();
    }


}
