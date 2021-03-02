package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.owoShopKeeperPanel.Model.OwoProduct;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.ViewHolder.wishListItemHolder;

public class WishList extends AppCompatActivity {

    private ImageView backArrow, empty_image;
    private TextView empty_text;
    private RecyclerView wishList;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        backArrow = findViewById(R.id.back_arrow_from_wish_list);
        empty_image = findViewById(R.id.empty_image);
        empty_text = findViewById(R.id.empty_text);

        wishList = findViewById(R.id.wishList);
        wishList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        wishList.setLayoutManager(layoutManager);


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference();

        Query query = cartListRef.child("Wish List").child(Prevalent.currentOnlineUser.getMobileNumber()).orderByValue();

        FirebaseRecyclerOptions<OwoProduct> options =
                new FirebaseRecyclerOptions.Builder<OwoProduct>()
                        .setQuery(query, OwoProduct.class).build();


        FirebaseRecyclerAdapter<OwoProduct, wishListItemHolder> adapter
                = new FirebaseRecyclerAdapter<OwoProduct, wishListItemHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final wishListItemHolder holder, int position, @NonNull final OwoProduct model) {

                Glide.with(WishList.this).load(model.getProductImage()).into(holder.product_image);

                holder.product_name.setText(model.getProductName());
                holder.product_price.setText("৳ "+String.valueOf(model.getProductPrice()));

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WishList.this);

                        alertDialogBuilder.setMessage("Are you sure you want to remove this item from cart ?");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        cartListRef.child("Wish List").child(Prevalent.currentOnlineUser.getPhone())
                                                .child(String.valueOf(model.getProduct_id()))
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(getApplicationContext(), "Item removed from wish list", Toast.LENGTH_SHORT).show();
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
                        */
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(WishList.this, ProductDetailsActivity.class);//For giving product description to the user when clicks on a cart item
                        intent.putExtra("Products", model);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public wishListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_item, parent, false);
                wishListItemHolder holder = new wishListItemHolder(view);
                return holder;
            }

            @Override
            public void onDataChanged() {
                if(getItemCount() == 0)
                {
                    empty_image.setVisibility(View.VISIBLE);
                    empty_text.setVisibility(View.VISIBLE);
                }
            }
        };
        wishList.setAdapter(adapter);
        adapter.startListening();
    }
}