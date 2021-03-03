package com.owoShopKeeperPanel.categoryWiseProducts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.owoShopKeeperPanel.categorySpinner.entity.CategoryEntity;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.configurations.HostAddress;
import com.owoShopKeeperPanel.shopKeeperPanel.CategoryWiseProduct;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private final Context mCtx;
    private final List<CategoryEntity> categoryEntityList = new ArrayList<>();

    public CategoryAdapter(Context mCtx, List<CategoryEntity> categoryEntityList) {
        this.mCtx = mCtx;
        this.categoryEntityList.addAll(categoryEntityList);
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

            int devicewidth = displaymetrics.widthPixels / 3;

            int deviceheight = displaymetrics.heightPixels / 5;

            itemView.getLayoutParams().width = devicewidth - 20;
            itemView.getLayoutParams().height = deviceheight;

            //imageView.getLayoutParams().width = devicewidth / 2; //setting category images dimensions
            imageView.getLayoutParams().height = devicewidth / 2; //setting category images dimensions

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                Intent intent = new Intent(mCtx, CategoryWiseProduct.class);
                intent.putExtra("category", categoryEntityList.get(position));
                mCtx.startActivity(intent);
            });
        }
    }
}
