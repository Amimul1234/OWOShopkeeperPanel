package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.owoshopkeeperpanel.Model.Ordered_products_model;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.ViewHolder.OrderListItemViewHolder;
import com.owoshopkeeperpanel.ViewHolder.wishListItemHolder;

public class Order_list extends AppCompatActivity {

    private ImageView back_button, empty_image_view;
    private TextView empty_text_view;
    private RecyclerView order_list_recycler_view;
    private AllianceLoader allianceLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        back_button = findViewById(R.id.back_to_home);
        order_list_recycler_view = findViewById(R.id.order_list_recycler_view);

        empty_image_view = findViewById(R.id.empty_image_view);
        empty_text_view = findViewById(R.id.empty_text_view);

        allianceLoader = findViewById(R.id.loader);

        order_list_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        order_list_recycler_view.setLayoutManager(layoutManager);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        allianceLoader.setVisibility(View.VISIBLE);

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference();

        Query query = cartListRef.child("Shop Keeper Orders").orderByChild("shop_number").equalTo(Prevalent.currentOnlineUser.getPhone());

        FirebaseRecyclerOptions<Ordered_products_model> options =
                new FirebaseRecyclerOptions.Builder<Ordered_products_model>()
                        .setQuery(query, Ordered_products_model.class).build();


        FirebaseRecyclerAdapter<Ordered_products_model, OrderListItemViewHolder> adapter
                = new FirebaseRecyclerAdapter<Ordered_products_model, OrderListItemViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final OrderListItemViewHolder holder, int position, @NonNull final Ordered_products_model model) {

                holder.order_number.setText(model.getOrder_number());

                Double total_with_discount = Double.parseDouble(model.getTotalAmount()) - model.getCoupon_discount();
                holder.total_amount_with_discount.setText("৳ "+ String.valueOf(total_with_discount));

                holder.order_status.setText(model.getState());

                holder.time_and_date.setText(model.getDate() + ", "+model.getTime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Order_list.this, Order_details_for_single_item.class);//For giving product description to the user when clicks on a cart item
                        intent.putExtra("Order", model);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public OrderListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
                OrderListItemViewHolder holder = new OrderListItemViewHolder(view);
                allianceLoader.setVisibility(View.INVISIBLE);
                return holder;
            }

            @Override
            public void onDataChanged() {
                if(getItemCount() == 0)
                {
                    empty_image_view.setVisibility(View.VISIBLE);
                    empty_text_view.setVisibility(View.VISIBLE);
                    allianceLoader.setVisibility(View.INVISIBLE);
                }
            }
        };

        order_list_recycler_view.setAdapter(adapter);
        adapter.startListening();
    }
}