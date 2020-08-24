package com.owoshopkeeperpanel.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Model.Products;

public class ItemAdapter extends PagedListAdapter<Products, ItemAdapter.ItemViewHolder>{

    private Context mCtx;


    public ItemAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.product_availability_sample, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Products item = getItem(position);

        if (item != null) {

            Glide.with(mCtx).load(item.getProduct_image()).into(holder.imageView);

            holder.txtProductName.setText(item.getProduct_name());

            holder.txtProductPrice.setText("৳ "+item.getProduct_price());
            holder.txtProductPrice.setPaintFlags(holder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtProductPrice.setVisibility(View.VISIBLE);

            double discounted_price = Double.parseDouble(item.getProduct_price()) - Double.parseDouble(item.getProduct_discount());

            holder.txtProduct_discounted_price.setText("৳ "+ String.valueOf(discounted_price));

        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
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

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtProductName, txtProductPrice, txtProduct_discounted_price;
        public ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.product_image);
            txtProductName=(TextView)itemView.findViewById(R.id.product_name);
            txtProductPrice=(TextView)itemView.findViewById(R.id.product_price);

            txtProduct_discounted_price = itemView.findViewById(R.id.product_discounted_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Products products = getItem(position);

            Toast.makeText(mCtx, "Hello", Toast.LENGTH_SHORT).show();

            /*
            Intent intent = new Intent(mCtx, UpdateProductActivity.class);
            intent.putExtra("Products", products);
            mCtx.startActivity(intent);

             */

        }
    }
    
}