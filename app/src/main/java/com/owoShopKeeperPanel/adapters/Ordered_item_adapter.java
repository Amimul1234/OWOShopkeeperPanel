package com.owoShopKeeperPanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.Model.OwoProduct;
import com.owoShopKeeperPanel.Model.Shop_keeper_ordered_products;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.shopKeeperPanel.Order_list;
import com.owoShopKeeperPanel.products.ProductDetailsActivity;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ordered_item_adapter extends  RecyclerView.Adapter<Ordered_item_adapter.ViewHolder>{

    private final Context mCtx;
    private final List<Shop_keeper_ordered_products> shop_keeper_ordered_products;

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
                    Order_list.allianceLoader.setVisibility(View.VISIBLE);

                    int position = getBindingAdapterPosition();

                    Shop_keeper_ordered_products shop_keeper_ordered_products1 = shop_keeper_ordered_products.get(position);

                    Call<OwoProduct> call = RetrofitClient.getInstance().getApi().getProductById(shop_keeper_ordered_products1.getProduct_id());

                    call.enqueue(new Callback<OwoProduct>() {
                        @Override
                        public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                            if(response.isSuccessful())
                            {
                                Order_list.allianceLoader.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
                                intent.putExtra("Products", (Serializable) response.body());
                                mCtx.startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(mCtx, "Server error", Toast.LENGTH_SHORT).show();
                                Order_list.allianceLoader.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                            Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_LONG).show();
                            Order_list.allianceLoader.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });
        }
    }
}
