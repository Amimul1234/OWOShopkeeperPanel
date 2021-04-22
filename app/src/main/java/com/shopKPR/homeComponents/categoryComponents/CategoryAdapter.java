package com.shopKPR.homeComponents.categoryComponents;

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
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.categorySpinner.entity.CategoryEntity;
import com.shopKPR.R;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private final Context mCtx;
    private final List<CategoryEntity> categoryEntityList = new ArrayList<>();
    private final ProgressDialog progressDialog;

    public CategoryAdapter(Context mCtx) {
        this.mCtx = mCtx;
        progressDialog = new ProgressDialog(mCtx);
        getData();
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.add_product_sample, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {

        Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress()+categoryEntityList.get(position).getCategoryImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.imageView);

        holder.textView.setText(categoryEntityList.get(position).getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categoryEntityList.size();
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

            int deviceWidth = displaymetrics.widthPixels / 3;
            int deviceHeight = displaymetrics.heightPixels / 5;

            itemView.getLayoutParams().width = deviceWidth - 20;
            itemView.getLayoutParams().height = deviceHeight - 10;

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                Intent intent = new Intent(mCtx, CategoryWiseProduct.class);
                intent.putExtra("category", categoryEntityList.get(position));
                mCtx.startActivity(intent);
            });
        }
    }

    public void getData() {
        progressDialog.setTitle("Get categories");
        progressDialog.setMessage("Please wait while we are getting category data");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .getSpecificCategoryData(Prevalent.category_to_display)
                .enqueue(new Callback<List<CategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CategoryEntity>> call, @NotNull Response<List<CategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            categoryEntityList.clear();
                            assert response.body() != null;
                            categoryEntityList.addAll(response.body());
                            notifyDataSetChanged();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(mCtx, "Can not get category data, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<CategoryEntity>> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("CategoryAdapter", "Error, Error is: "+t.getMessage());
                        Toast.makeText(mCtx, "Can not get category data, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
