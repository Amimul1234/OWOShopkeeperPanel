package com.shopKPR.homeComponents.bannerComponents;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.tabs.TabLayout;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.R;
import com.shopKPR.homeComponents.tabbedComponents.ViewPagerAdapter;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageFlipperAdapter extends RecyclerView.Adapter<ImageFlipperAdapter.ViewHolder>{

    private final Context mCtx;
    private final List<String> images = new ArrayList<>();
    private final FragmentManager fragmentManager;
    private final Lifecycle lifecycle;

    private CapsuleAdapter capsuleAdapter;
    private List<OwoProduct> owoProductList = new ArrayList<>();

    public ImageFlipperAdapter(Context mCtx, List<String> images, FragmentManager fragmentManager,
                               Lifecycle lifecycle)
    {
        this.mCtx = mCtx;
        this.images.addAll(images);
        this.fragmentManager = fragmentManager;
        this.lifecycle = lifecycle;
        capsuleAdapter = new CapsuleAdapter(mCtx, owoProductList);
    }

    @NonNull
    @Override
    public ImageFlipperAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.banner_slider, parent, false);
        return new ImageFlipperAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageFlipperAdapter.ViewHolder holder, int position) {
        fliptheView(holder.bannerFlipper);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewFlipper bannerFlipper;
        public TabLayout tabLayout;
        public ViewPager2 viewPager2;
        public ViewPagerAdapter viewPagerAdapter;
        public RecyclerView capsulesRecyclerView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            bannerFlipper = itemView.findViewById(R.id.view_flipper_offer);
            tabLayout = itemView.findViewById(R.id.tabbed_layout);
            viewPager2 = itemView.findViewById(R.id.category_and_brand_showing);
            viewPagerAdapter = new ViewPagerAdapter(fragmentManager, lifecycle);
            viewPager2.setAdapter(viewPagerAdapter);

            capsulesRecyclerView = itemView.findViewById(R.id.capsule_recycler_view);
            capsulesRecyclerView.setAdapter(capsuleAdapter);

            capsulesRecyclerView.setLayoutManager(new GridLayoutManager(mCtx, 1,
                    GridLayoutManager.HORIZONTAL, false));

            capsulesRecyclerView.setNestedScrollingEnabled(false);

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

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            bannerFlipper.getLayoutParams().width = displaymetrics.widthPixels;
        }
    }

    private void fliptheView(ViewFlipper banner) {

        getCapsules();

        int size = images.size();

        for(int i=0; i<size; i++)
        {
            flipperImage(images.get(i), banner);
        }

    }

    private void getCapsules()
    {
        RetrofitClient.getInstance().getApi()
                .getProductsViaSpecificCategory(0, Prevalent.category_to_display.get(0))
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.isSuccessful())
                        {
                            owoProductList.clear();
                            owoProductList.addAll(response.body());
                            capsuleAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(mCtx, "Can not get capsules", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.e("Capsule", t.getMessage());
                        Toast.makeText(mCtx, "Can not get capsules", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void flipperImage(String image, ViewFlipper viewFlipper)
    {
        ImageView imageView = new ImageView(mCtx);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress()+image)
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(imageView);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(6000);
        viewFlipper.setAutoStart(true);
        viewFlipper.startFlipping();
        viewFlipper.setInAnimation(mCtx, R.anim.slide_in_right);
        viewFlipper.setOutAnimation(mCtx, R.anim.slide_out_left);
    }

    public void updateItems(List<String> newList) {
        images.clear();
        images.addAll(newList);
        notifyDataSetChanged();
    }
}