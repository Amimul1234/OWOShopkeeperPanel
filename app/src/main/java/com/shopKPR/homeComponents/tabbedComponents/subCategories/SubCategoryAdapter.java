package com.shopKPR.homeComponents.tabbedComponents.subCategories;

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
import com.shopKPR.categorySpinner.entity.SubCategoryEntity;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.R;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.shopKeeperPanel.productRelated.SubCategoryWiseProduct;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private final List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();
    private final Context mCtx;
    private final ProgressDialog progressDialog;

    public SubCategoryAdapter(Context mCtx) {
        this.mCtx = mCtx;
        progressDialog = new ProgressDialog(mCtx);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.add_product_sample, parent,false);
        return new SubCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress()+subCategoryEntityList.get(position).getSub_category_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.imageView);

        holder.textView.setText(subCategoryEntityList.get(position).getSub_category_name());
    }

    @Override
    public int getItemCount() {
        return subCategoryEntityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageviewid);
            textView = itemView.findViewById(R.id.textviewid);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 3;
            int deviceheight = displaymetrics.heightPixels / 5;

            itemView.getLayoutParams().width = devicewidth - 20;
            itemView.getLayoutParams().height = deviceheight - 10;

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();

                SubCategoryEntity subCategoryEntity = subCategoryEntityList.get(position);

                Intent intent = new Intent(mCtx, SubCategoryWiseProduct.class);
                intent.putExtra("sub_category", subCategoryEntity);
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
                .getSubCategoriesPaging(page, Prevalent.category_to_display)
                .enqueue(new Callback<List<SubCategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Response<List<SubCategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            if(response.body().size() == 0)
                                Toast.makeText(mCtx, "No more brands", Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                            subCategoryEntityList.addAll(response.body());
                            notifyDataSetChanged();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(mCtx, "No more brands", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("BrandsFrag", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(mCtx, "Error getting sub-category data, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
