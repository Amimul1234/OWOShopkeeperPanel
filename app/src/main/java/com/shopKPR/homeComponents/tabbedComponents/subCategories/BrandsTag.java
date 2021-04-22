package com.shopKPR.homeComponents.tabbedComponents.subCategories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shopKPR.R;

public class BrandsTag extends RecyclerView.Adapter<BrandsTag.ViewHolder>{

    private final Context mCtx;

    public BrandsTag(Context mCtx) {
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public BrandsTag.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.brands_tag, parent, false);
        return new BrandsTag.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
