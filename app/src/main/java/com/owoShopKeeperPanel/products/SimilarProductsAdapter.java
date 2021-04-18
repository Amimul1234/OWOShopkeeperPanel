package com.owoShopKeeperPanel.products;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.owoShopKeeperPanel.Model.OwoProduct;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.configurations.HostAddress;
import com.owoShopKeeperPanel.network.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilarProductsAdapter extends RecyclerView.Adapter<SimilarProductsAdapter.ViewHolder>
{

    private final Context mCtx;
    private final ProgressDialog progressDialog;
    private final List<OwoProduct> owoProductList;

    public SimilarProductsAdapter(Context mCtx, List<OwoProduct> owoProductList, ProgressDialog progressDialog) {
        this.mCtx = mCtx;
        this.owoProductList = owoProductList;
        this.progressDialog = progressDialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.product_availability_sample, parent, false);
        return new SimilarProductsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        OwoProduct item = owoProductList.get(position);

        if (item != null) {

            Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress()+item.getProductImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.imageView);

            holder.txtProductName.setText(item.getProductName());

            String price = "৳ "+ item.getProductPrice();

            holder.txtProductPrice.setText(price);

            holder.txtProductPrice.setPaintFlags(holder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtProductPrice.setVisibility(View.VISIBLE);

            double discounted_price = item.getProductPrice() - item.getProductDiscount();

            String discount = "৳ "+ discounted_price;

            holder.txtProduct_discounted_price.setText(discount);


            double percentage = (item.getProductDiscount() / item.getProductPrice()) * 100.00;

            int val = (int) percentage;
            String percent = val + " % ";
            holder.discounted_percent.setText(percent);

        } else {
            Toast.makeText(mCtx, "No Product Available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return owoProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView txtProductName, txtProductPrice, txtProduct_discounted_price, discounted_percent;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView)
        {
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
        public void onClick(View v)
        {
            Long id = Objects.requireNonNull(owoProductList.get(getBindingAdapterPosition())).getProductId();


            progressDialog.setTitle("Product Details");
            progressDialog.setMessage("Please wait while we are getting product details");
            progressDialog.setCancelable(false);
            progressDialog.show();

            RetrofitClient.getInstance().getApi()
                    .getProductById(id)
                    .enqueue(new Callback<OwoProduct>() {
                        @Override
                        public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                            if(response.isSuccessful())
                            {
                                progressDialog.dismiss();

                                Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
                                intent.putExtra("Products", response.body());
                                mCtx.startActivity(intent);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Log.e("Error", "Server error occurred");
                                Toast.makeText(mCtx, "Can not get product data, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                            Log.e("Error", t.getMessage());
                            progressDialog.dismiss();
                        }});
        }
    }
}
