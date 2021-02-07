package com.owoShopKeeperPanel.adapters;

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
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.Model.Owo_product;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.shopKeeperPanel.ProductDetailsActivity;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapter extends PagedListAdapter<Owo_product, RecyclerView.ViewHolder>{

    private Context mCtx;
    private ProgressBar progressBar;

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

            Owo_product item = getItem(position);

            if (item != null) {

                Glide.with(mCtx).load(item.getProduct_image()).into(itemViewHolder.imageView);

                itemViewHolder.txtProductName.setText(item.getProduct_name());

                itemViewHolder.txtProductPrice.setText("৳ "+item.getProduct_price());
                itemViewHolder.txtProductPrice.setPaintFlags(itemViewHolder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                itemViewHolder.txtProductPrice.setVisibility(View.VISIBLE);

                double discounted_price = item.getProduct_price() - item.getProduct_discount();

                itemViewHolder.txtProduct_discounted_price.setText("৳ "+ String.valueOf(discounted_price));


                double percentage = (item.getProduct_discount() / item.getProduct_price()) * 100.00;

                int val = (int) percentage;
                itemViewHolder.discounted_percent.setText(String.valueOf(val) + " % ");



            } else {
                Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
            }

    }

    private static DiffUtil.ItemCallback<Owo_product> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Owo_product>() {
                @Override
                public boolean areItemsTheSame(Owo_product oldItem, Owo_product newItem) {
                    return oldItem.getProduct_id() == newItem.getProduct_id();
                }

                @Override
                public boolean areContentsTheSame(Owo_product oldItem, Owo_product newItem) {
                    return true;
                }
            };

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

            DisplayMetrics displaymetrics = new DisplayMetrics(); //Setting things dynamically
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 2;

            itemView.getLayoutParams().width = devicewidth - 15;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            long id = getItem(getBindingAdapterPosition()).getProduct_id();

            progressBar.setVisibility(View.VISIBLE);

            RetrofitClient.getInstance().getApi()
                    .getProductById(id)
                    .enqueue(new Callback<Owo_product>() {
                        @Override
                        public void onResponse(@NotNull Call<Owo_product> call, @NotNull Response<Owo_product> response) {
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
                                Log.e("Error", "Server error occured");
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<Owo_product> call, @NotNull Throwable t) {
                            Log.e("Error", t.getMessage());
                            progressBar.setVisibility(View.INVISIBLE);
                        }});
        }
    }

}
