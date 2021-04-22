package com.shopKPR.adapters;

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
import com.shopKPR.R;
import com.shopKPR.categorySpinner.entity.SubCategoryEntity;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.shopKeeperPanel.product_related.SubCategoryWiseProduct;
import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>{

    private final Context mCtx;
    private final List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();

    public SubCategoryAdapter(Context mCtx, List<SubCategoryEntity> subCategoryEntityList) {
        this.mCtx = mCtx;
        this.subCategoryEntityList.addAll(subCategoryEntityList);
    }

    @NonNull
    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.add_product_sample, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryAdapter.ViewHolder holder, int position) {

        Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress() + subCategoryEntityList.get(position).getSub_category_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.imageView);

        holder.textView.setText(subCategoryEntityList.get(position).getSub_category_name());
    }

    @Override
    public int getItemCount()
    {
        return subCategoryEntityList.size();
    }

    public void changeList(List<SubCategoryEntity> subCategoryEntityList) {
        this.subCategoryEntityList.clear();
        this.subCategoryEntityList.addAll(subCategoryEntityList);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageviewid);
            textView = itemView.findViewById(R.id.textviewid);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 3;
            int deviceheight =  displaymetrics.heightPixels / 5;

            itemView.getLayoutParams().width = devicewidth - 20;
            itemView.getLayoutParams().height = deviceheight - 10;

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();

                Intent intent = new Intent(mCtx, SubCategoryWiseProduct.class);
                intent.putExtra("sub_category", subCategoryEntityList.get(position));
                mCtx.startActivity(intent);

            });
        }
    }
}