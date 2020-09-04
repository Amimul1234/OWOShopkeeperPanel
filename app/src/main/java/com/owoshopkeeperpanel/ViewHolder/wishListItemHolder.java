package com.owoshopkeeperpanel.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.owoshopkeeperpanel.Interface.ItemClickListener;
import com.owoshopkeeperpanel.R;

public class wishListItemHolder extends RecyclerView.ViewHolder{

    public TextView product_name, product_price;
    public ImageView delete, product_image;

    public wishListItemHolder(@NonNull View itemView) {
        super(itemView);
        product_image = itemView.findViewById(R.id.wishList_product_image);
        product_name = itemView.findViewById(R.id.wishList_product_name);
        product_price = itemView.findViewById(R.id.wishList_product_price);
        delete = itemView.findViewById(R.id.wishList_item_delete);
    }
}