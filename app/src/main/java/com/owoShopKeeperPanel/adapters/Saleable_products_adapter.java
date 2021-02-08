package com.owoShopKeeperPanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.myShopRelated.NewProductAdding;

import java.util.List;

public class Saleable_products_adapter extends  RecyclerView.Adapter<Saleable_products_adapter.ViewHolder>{
    private Context mCtx;
    private List<Ordered_products> ordered_products;

    public Saleable_products_adapter(Context mCtx, List<Ordered_products> ordered_products) {
        this.mCtx = mCtx;
        this.ordered_products = ordered_products;
    }

    @NonNull
    @Override
    public Saleable_products_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.saleable_products, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Saleable_products_adapter.ViewHolder holder, int position) {
        Glide.with(mCtx).load(ordered_products.get(position).getProduct_image()).into(holder.imageView);
        holder.product_name.setText(ordered_products.get(position).getProduct_name());
        holder.product_total_quantity.setText(String.valueOf(ordered_products.get(position).getProduct_quantity()));
    }

    @Override
    public int getItemCount() {
        return ordered_products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView product_name, product_total_quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.single_product_image);
            product_name = itemView.findViewById(R.id.single_product_name);
            product_total_quantity = itemView.findViewById(R.id.single_product_total_quantity);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    Intent intent = new Intent(mCtx, NewProductAdding.class);
                    intent.putExtra("id", String.valueOf(ordered_products.get(position).getProduct_id()));
                    intent.putExtra("quantity", String.valueOf(ordered_products.get(position).getProduct_quantity()));
                    mCtx.startActivity(intent);
                }
            });
        }
    }
}