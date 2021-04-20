package com.shopKPR.orders.orderItem;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.Model.Shop_keeper_ordered_products;
import com.shopKPR.R;
import com.shopKPR.products.ProductDetailsActivity;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ordered_item_adapter extends  RecyclerView.Adapter<Ordered_item_adapter.ViewHolder>{

    private final Context mCtx;
    private final List<Shop_keeper_ordered_products> shopKeeperOrderedProducts;

    public Ordered_item_adapter(Context mCtx, List<Shop_keeper_ordered_products> shopKeeperOrderedProducts) {
        this.mCtx = mCtx;
        this.shopKeeperOrderedProducts = shopKeeperOrderedProducts;
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

        Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress() +
                shopKeeperOrderedProducts.get(position).getProduct_image()).diskCacheStrategy(DiskCacheStrategy.ALL)
                .timeout(6000).into(holder.imageView);

        holder.product_name.setText(shopKeeperOrderedProducts.get(position).getProduct_name());

        String productPriceAndQuantityString = '৳'+
                String.valueOf(shopKeeperOrderedProducts.get(position).getProduct_price())+
                'x'+ shopKeeperOrderedProducts.get(position).getProduct_quantity();

        holder.product_price_and_quantity.setText(productPriceAndQuantityString);

        Double total = shopKeeperOrderedProducts.get(position).getProduct_quantity() *
                shopKeeperOrderedProducts.get(position).getProduct_price();

        String totalString = '৳'+ String.valueOf(total);
        holder.product_total_price.setText(totalString);
    }

    @Override
    public int getItemCount() {
        return shopKeeperOrderedProducts.size();
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

            itemView.setOnClickListener((View.OnClickListener) v ->
            {
                int position = getBindingAdapterPosition();

                Shop_keeper_ordered_products shop_keeper_ordered_products1 = shopKeeperOrderedProducts.get(position);

                Call<OwoProduct> call = RetrofitClient.getInstance().getApi().
                        getProductById(shop_keeper_ordered_products1.getProduct_id());

                call.enqueue(new Callback<OwoProduct>() {
                    @Override
                    public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                        if(response.isSuccessful())
                        {
                            Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
                            intent.putExtra("Products", (Serializable) response.body());
                            mCtx.startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(mCtx, "Server error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                        Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            });
        }
    }
}
