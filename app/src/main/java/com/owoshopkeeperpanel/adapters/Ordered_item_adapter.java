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
import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.Model.Shop_keeper_ordered_products;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.shopKeeperPanel.BridgeofCartAndProduct;
import com.owoshopkeeperpanel.shopKeeperPanel.ProductDetailsActivity;

import java.util.List;

public class Ordered_item_adapter extends  RecyclerView.Adapter<Ordered_item_adapter.ViewHolder>{

    private Context mCtx;
    private List<Shop_keeper_ordered_products> shop_keeper_ordered_products;

    public Ordered_item_adapter(Context mCtx, List<Shop_keeper_ordered_products> shop_keeper_ordered_products) {
        this.mCtx = mCtx;
        this.shop_keeper_ordered_products = shop_keeper_ordered_products;
    }

    @NonNull
    @Override
    public Ordered_item_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.ordered_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Ordered_item_adapter.ViewHolder holder, int position) {
        Glide.with(mCtx).load(shop_keeper_ordered_products.get(position).getProduct_image()).into(holder.imageView);
        holder.product_name.setText(shop_keeper_ordered_products.get(position).getProduct_name());
        holder.product_price_and_quantity.setText('৳'+String.valueOf(shop_keeper_ordered_products.get(position).getProduct_price())+'x'+String.valueOf(shop_keeper_ordered_products.get(position).getProduct_quantity()));
        Double total = shop_keeper_ordered_products.get(position).getProduct_quantity() * shop_keeper_ordered_products.get(position).getProduct_price();
        holder.product_total_price.setText('৳'+ String.valueOf(total));
    }

    @Override
    public int getItemCount() {
        return shop_keeper_ordered_products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView product_name, product_price_and_quantity, product_total_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.single_product_image);
            product_name = itemView.findViewById(R.id.single_product_name);
            product_price_and_quantity = itemView.findViewById(R.id.single_product_price_and_quantity);
            product_total_price = itemView.findViewById(R.id.single_product_total_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();

                    Shop_keeper_ordered_products shop_keeper_ordered_products1 = shop_keeper_ordered_products.get(position);

                    Owo_product owo_product = new Owo_product();

                    owo_product.setProduct_id(shop_keeper_ordered_products1.getProduct_id());
                    owo_product.setProduct_name(shop_keeper_ordered_products1.getProduct_name());
                    owo_product.setProduct_category(shop_keeper_ordered_products1.getProduct_category());
                    owo_product.setProduct_price(shop_keeper_ordered_products1.getProduct_price());
                    owo_product.setProduct_discount(shop_keeper_ordered_products1.getProduct_discount());
                    owo_product.setProduct_quantity(shop_keeper_ordered_products1.getProduct_quantity());
                    owo_product.setProduct_description(shop_keeper_ordered_products1.getProduct_description());
                    owo_product.setProduct_creation_date(shop_keeper_ordered_products1.getProduct_creation_date());
                    owo_product.setProduct_creation_time(shop_keeper_ordered_products1.getProduct_creation_time());
                    owo_product.setProduct_sub_category(shop_keeper_ordered_products1.getProduct_sub_category());
                    owo_product.setProduct_brand(shop_keeper_ordered_products1.getProduct_brand());
                    owo_product.setProduct_image(shop_keeper_ordered_products1.getProduct_image());

                    Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
                    intent.putExtra("Products", owo_product);
                    mCtx.startActivity(intent);
                }
            });
        }
    }
}
