package com.owoShopKeeperPanel.homeComponents.brandsComponent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.configurations.HostAddress;
import com.owoShopKeeperPanel.homeComponents.brandsComponent.entity.Brands;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.shopKeeperPanel.product_related.BrandWiseProduct;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.ViewHolder> {

    private final List<Brands> brandsList = new ArrayList<>();
    private final Context mCtx;
    private final ProgressDialog progressDialog;

    public BrandsAdapter(Context mCtx) {
        this.mCtx = mCtx;
        progressDialog = new ProgressDialog(mCtx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.add_product_sample, parent,false);
        return new BrandsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress()+brandsList.get(position).getBrandImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.imageView);

        holder.textView.setText(brandsList.get(position).getBrandName());
    }

    @Override
    public int getItemCount() {
        return brandsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageviewid);
            textView = itemView.findViewById(R.id.textviewid);

            DisplayMetrics displaymetrics = new DisplayMetrics(); //Resizing things dynamically
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 3;

            int deviceheight = displaymetrics.heightPixels / 5;

            itemView.getLayoutParams().width = devicewidth - 20;
            itemView.getLayoutParams().height = deviceheight;

            imageView.getLayoutParams().width = devicewidth / 3; //setting category images dimensions
            imageView.getLayoutParams().height = devicewidth / 3; //setting category images dimensions

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                Brands brands = brandsList.get(position);
                Intent intent = new Intent(mCtx, BrandWiseProduct.class);
                intent.putExtra("brand", brands);
                mCtx.startActivity(intent);
            });
        }
    }

    public void getItems(int page)
    {
        progressDialog.setTitle("Get Brands");
        progressDialog.setMessage("Please wait while we are fetching brands data");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .getBrandsViaCategory(page, Prevalent.category_to_display)
                .enqueue(new Callback<List<Brands>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Brands>> call, @NotNull Response<List<Brands>> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            brandsList.addAll(response.body());
                            notifyDataSetChanged();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(mCtx, "No more brands", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Brands>> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("BrandsFrag", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(mCtx, "Error getting brands data, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
