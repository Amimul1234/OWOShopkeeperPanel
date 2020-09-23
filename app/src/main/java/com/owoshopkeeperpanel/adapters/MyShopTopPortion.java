package com.owoshopkeeperpanel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoshopkeeperpanel.Model.PendingShop;
import com.owoshopkeeperpanel.R;

import java.util.HashMap;

public class MyShopTopPortion extends RecyclerView.Adapter<MyShopTopPortion.ViewHolder> {

    private Context mCtx;
    private HashMap<String, String> datas;

    public MyShopTopPortion(Context mCtx, HashMap<String, String> datas) {
        this.mCtx = mCtx;
        this.datas = datas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.my_shop_top_portion, parent, false);
        return new MyShopTopPortion.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mCtx).load(datas.get("shop_image")).into(holder.my_shop_image);
        holder.my_shop_name.setText(datas.get("shop_name"));
        holder.my_shop_address.setText(datas.get("shop_address"));
        holder.my_shop_service_mobile.setText(datas.get("shop_service_mobile"));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView my_shop_image;
        private TextView my_shop_name, my_shop_address, my_shop_service_mobile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            my_shop_image = itemView.findViewById(R.id.my_shop_image);
            my_shop_name = itemView.findViewById(R.id.my_shop_name);
            my_shop_address = itemView.findViewById(R.id.my_shop_address);
            my_shop_service_mobile = itemView.findViewById(R.id.my_shop_phone_number);
        }
    }

}
