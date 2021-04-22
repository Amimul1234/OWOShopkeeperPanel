package com.shopKPR.homeComponents.tabbedComponents.subCategories;

import android.app.Activity;
import android.content.Context;
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
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.homeComponents.brandsComponent.entity.Brands;
import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.ViewHolder> {

    private final Context mCtx;
    private final List<Brands> brandsList;

    public BrandsAdapter(Context mCtx, List<Brands> brandsList) {
        this.mCtx = mCtx;
        this.brandsList = brandsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.add_product_sample, parent,false);
        return new BrandsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
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

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int deviceWidth = displaymetrics.widthPixels / 3;
            int deviceHeight = displaymetrics.heightPixels / 5;

            itemView.getLayoutParams().width = deviceWidth - 20;
            itemView.getLayoutParams().height = deviceHeight - 10;

            itemView.setOnClickListener(v ->
            {
//                int position = getBindingAdapterPosition();
//                Intent intent = new Intent(mCtx, CategoryWiseProduct.class);
//                intent.putExtra("category", categoryEntityList.get(position));
//                mCtx.startActivity(intent);
            });
        }
    }
}
