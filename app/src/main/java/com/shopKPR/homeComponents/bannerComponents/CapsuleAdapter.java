package com.shopKPR.homeComponents.bannerComponents;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.R;
import com.shopKPR.configurations.HostAddress;

import java.util.List;

public class CapsuleAdapter extends RecyclerView.Adapter<CapsuleAdapter.ViewHolder>
{
    private final Context mctx;
    private final List<OwoProduct> owoProductList;

    public CapsuleAdapter(Context mctx, List<OwoProduct> owoProductList) {
        this.mctx = mctx;
        this.owoProductList = owoProductList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mctx).inflate(R.layout.horizontal_slider_products,
                parent, false);

        return new CapsuleAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        OwoProduct item = owoProductList.get(position);

        if (item != null) {

            Glide.with(mctx).load(HostAddress.HOST_ADDRESS.getHostAddress()+item.getProductImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.imageView);

            holder.txtProductName.setText(item.getProductName());

            String price = "৳ "+ item.getProductPrice();

            holder.txtProductPrice.setText(price);

            holder.txtProductPrice.setPaintFlags(holder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtProductPrice.setVisibility(View.VISIBLE);

            double discounted_price = item.getProductPrice() - item.getProductDiscount();

            String discount = "৳ "+ discounted_price;

            holder.txtProduct_discounted_price.setText(discount);

        } else {
            Toast.makeText(mctx, "No Product Available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return owoProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProductName, txtProductPrice, txtProduct_discounted_price;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image);
            txtProductName = itemView.findViewById(R.id.product_name);
            txtProductPrice = itemView.findViewById(R.id.product_price);
            txtProduct_discounted_price = itemView.findViewById(R.id.product_discounted_price);
        }
    }
}
