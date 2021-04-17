package com.owoShopKeeperPanel.homeComponents.productComponents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.Model.OwoProduct;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.configurations.HostAddress;
import com.owoShopKeeperPanel.shopKeeperPanel.ProductDetailsActivity;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapter extends PagedListAdapter<OwoProduct, RecyclerView.ViewHolder>{

    private final Context mCtx;
    private final ProgressBar progressBar;

    public ItemAdapter(Context mCtx, ProgressBar progressBar) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.progressBar = progressBar;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.product_availability_sample, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            OwoProduct item = getItem(position);

            if (item != null) {

                Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress()+item.getProductImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(itemViewHolder.imageView);

                itemViewHolder.txtProductName.setText(item.getProductName());

                String price = "৳ "+ item.getProductPrice();

                itemViewHolder.txtProductPrice.setText(price);

                itemViewHolder.txtProductPrice.setPaintFlags(itemViewHolder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                itemViewHolder.txtProductPrice.setVisibility(View.VISIBLE);

                double discounted_price = item.getProductPrice() - item.getProductDiscount();

                String discount = "৳ "+ discounted_price;

                itemViewHolder.txtProduct_discounted_price.setText(discount);


                double percentage = (item.getProductDiscount() / item.getProductPrice()) * 100.00;

                int val = (int) percentage;
                String percent = val + " % ";
                itemViewHolder.discounted_percent.setText(percent);



            } else {
                Toast.makeText(mCtx, "No Product Available", Toast.LENGTH_LONG).show();
            }

    }

    private static final DiffUtil.ItemCallback<OwoProduct> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OwoProduct>() {
                @Override
                public boolean areItemsTheSame(OwoProduct oldItem, OwoProduct newItem) {
                    return oldItem.getProductId().equals(newItem.getProductId());
                }

                @Override
                public boolean areContentsTheSame(OwoProduct oldItem, @NotNull OwoProduct newItem) {
                    return oldItem.equals(newItem);
                }
            };


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtProductName, txtProductPrice, txtProduct_discounted_price, discounted_percent;
        public ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            
            imageView = itemView.findViewById(R.id.product_image);
            txtProductName = itemView.findViewById(R.id.product_name);
            txtProductPrice = itemView.findViewById(R.id.product_price);
            discounted_percent = itemView.findViewById(R.id.discount_percentage);
            txtProduct_discounted_price = itemView.findViewById(R.id.product_discounted_price);

            DisplayMetrics displaymetrics = new DisplayMetrics(); //Setting things dynamically
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 2;

            itemView.getLayoutParams().width = devicewidth - 15;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Long id = Objects.requireNonNull(getItem(getBindingAdapterPosition())).getProductId();

            progressBar.setVisibility(View.VISIBLE);

            RetrofitClient.getInstance().getApi()
                    .getProductById(id)
                    .enqueue(new Callback<OwoProduct>() {
                        @Override
                        public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                            if(response.isSuccessful())
                            {
                                progressBar.setVisibility(View.INVISIBLE);

                                Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
                                intent.putExtra("Products", response.body());
                                mCtx.startActivity(intent);
                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Log.e("Error", "Server error occurred");
                                Toast.makeText(mCtx, "Can not get product data, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                            Log.e("Error", t.getMessage());
                            progressBar.setVisibility(View.INVISIBLE);
                        }});
        }
    }

}
