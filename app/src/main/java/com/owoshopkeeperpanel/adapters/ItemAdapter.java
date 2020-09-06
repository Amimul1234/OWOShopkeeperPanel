package com.owoshopkeeperpanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.shopKeeperPanel.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends PagedListAdapter<Products, RecyclerView.ViewHolder>{

    private Context mCtx;
    public static final int View_Flipper = 1;
    public static final int Products_shower = 2;
    private List<String> images = new ArrayList<String>();

    public ItemAdapter(Context mCtx, List<String> images) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.images.addAll(images);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == View_Flipper)
        {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.banner_slider, parent, false);
            return new BannerFlipper(view);
        }
        else
        {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.product_availability_sample, parent, false);
            return new ItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == Products_shower)
        {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            Products item = getItem(position);

            if (item != null) {

                Glide.with(mCtx).load(item.getProduct_image()).into(itemViewHolder.imageView);

                itemViewHolder.txtProductName.setText(item.getProduct_name());

                itemViewHolder.txtProductPrice.setText("৳ "+item.getProduct_price());
                itemViewHolder.txtProductPrice.setPaintFlags(itemViewHolder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                itemViewHolder.txtProductPrice.setVisibility(View.VISIBLE);

                double discounted_price = item.getProduct_price() - Double.parseDouble(item.getProduct_discount());

                itemViewHolder.txtProduct_discounted_price.setText("৳ "+ String.valueOf(discounted_price));


                double percentage = (Double.parseDouble(item.getProduct_discount()) / item.getProduct_price()) * 100.00;

                int val = (int) percentage;

                itemViewHolder.discounted_percent.setText(String.valueOf(val) + " % ");



            } else {
                Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            BannerFlipper bannerFlipper = (BannerFlipper) holder;
            fliptheView(bannerFlipper.bannerFlipper);
        }
    }

    private static DiffUtil.ItemCallback<Products> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Products>() {
                @Override
                public boolean areItemsTheSame(Products oldItem, Products newItem) {
                    return oldItem.getProduct_id() == newItem.getProduct_id();
                }

                @Override
                public boolean areContentsTheSame(Products oldItem, Products newItem) {
                    return true;
                }
            };


    @Override
    public int getItemViewType(int position) {
        if(position == 0)
        {
            return View_Flipper;
        }
        else
            return Products_shower;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtProductName, txtProductPrice, txtProduct_discounted_price, discounted_percent;
        public ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.product_image);
            txtProductName = (TextView)itemView.findViewById(R.id.product_name);
            txtProductPrice = (TextView)itemView.findViewById(R.id.product_price);
            discounted_percent = itemView.findViewById(R.id.discount_percentage);
            txtProduct_discounted_price = itemView.findViewById(R.id.product_discounted_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Products products = getItem(getAdapterPosition());

            Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
            intent.putExtra("Products", products);
            mCtx.startActivity(intent);
        }
    }

    public class BannerFlipper extends RecyclerView.ViewHolder {

        public ViewFlipper bannerFlipper;

        public BannerFlipper(@NonNull View itemView) {
            super(itemView);

            bannerFlipper = itemView.findViewById(R.id.view_flipper_offer);
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