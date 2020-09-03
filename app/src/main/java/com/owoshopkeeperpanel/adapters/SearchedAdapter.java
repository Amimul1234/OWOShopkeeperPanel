package com.owoshopkeeperpanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.shopKeeperPanel.ProductDetailsActivity;

import java.util.List;


public class SearchedAdapter extends RecyclerView.Adapter<SearchedAdapter.ProductViewHolder> {

    private Context mCtx;
    private List<Products> productsList;

    public SearchedAdapter(Context mCtx, List<Products> productsList) {
        this.mCtx = mCtx;
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public SearchedAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.product_availability_sample, parent, false);
       return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedAdapter.ProductViewHolder holder, int position) {
        Products products = productsList.get(position);

        holder.product_name.setText(products.getProduct_name());
        holder.product_price.setText(products.getProduct_price());

        Glide.with(mCtx).load(products.getProduct_image()).into(holder.product_image);

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView product_name, product_price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Products products = productsList.get(position);

                    Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
                    intent.putExtra("Products", products);
                    mCtx.startActivity(intent);
                }
            });
        }
    }
}
