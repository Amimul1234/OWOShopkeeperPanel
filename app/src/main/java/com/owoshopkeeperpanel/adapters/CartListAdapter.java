package com.owoshopkeeperpanel.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Interface.ItemClickListener;
import com.owoshopkeeperpanel.Model.Cart;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;

import java.util.ArrayList;
import java.util.List;


public class CartListAdapter extends ArrayAdapter<Cart> {
    private Activity context;
    private List<Cart> cartList;

    public CartListAdapter(Activity context, List<Cart> cartList){
        super(context, R.layout.cart_items_sample, cartList);
        this.context=context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater= context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.cart_items_sample,null,true);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setPosition(position);
        holder.bindViews();
        return convertView;
    }

    public class ViewHolder {

        ImageView delete;
        ImageView cart_product_image;
        TextView cart_product_name;
        TextView cart_product_quantity;
        TextView cart_product_price;
        ElegantNumberButton cart_item_change_button;
        int position;

        public ViewHolder(View view) {

            delete = view.findViewById(R.id.cart_item_delete);
            cart_product_image = view.findViewById(R.id.cart_product_image);
            cart_product_name = view.findViewById(R.id.cart_product_name);
            cart_product_quantity = view.findViewById(R.id.cart_product_quantity);
            cart_product_price = view.findViewById(R.id.cart_product_price);
            cart_item_change_button = view.findViewById(R.id.cart_item_change_btn);
        }

        public void setPosition(int position) {
            this.position = position;
        }


        public void bindViews() {

            Cart cart = cartList.get(position);

            Glide.with(context).load(cart.getProduct_image()).into(cart_product_image);

            cart_product_name.setText(cart.getProduct_name());
            cart_product_quantity.setText(cart.getProduct_price()+" × "+cart.getNeeded_quantity());
            double product_total_price = Double.parseDouble(cart.getProduct_price()) * Double.parseDouble(cart.getNeeded_quantity());
            cart_product_price.setText("৳ "+String.valueOf(product_total_price));
            cart_item_change_button.setNumber(cart.getNeeded_quantity());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteItem(position);
                }
            });
        }

    }


    private void onDeleteItem(int position) {

        Cart cart = cartList.get(position);

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setMessage("Are you sure you want to remove this item from cart ?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        cartListRef.child(Prevalent.currentOnlineUser.getPhone())
                                .child(String.valueOf(cart.getProduct_id()))
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(context, "Item Removed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}