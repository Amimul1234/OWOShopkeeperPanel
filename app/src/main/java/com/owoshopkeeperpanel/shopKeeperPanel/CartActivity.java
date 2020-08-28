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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoshopkeeperpanel.Model.Cart;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.ViewHolder.CartViewHolder;
import com.squareup.picasso.Picasso;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button place_order_button;
    private TextView totalAmount,subTotalAmount,vouchartxt;
    private int totalPrice=0;
    private ImageView back_from_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        place_order_button=(Button)findViewById(R.id.place_order_btn);
        vouchartxt=(TextView)findViewById(R.id.cart_vouchar);
        totalAmount=(TextView)findViewById(R.id.cart_total_amount);
        subTotalAmount=(TextView)findViewById(R.id.cart_sub_total_amount);
        back_from_cart=(ImageView)findViewById(R.id.back_arrow_from_cart);

        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        place_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(totalPrice));
                startActivity(intent);
                finish();

            }
        });

        vouchartxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartActivity.this,AddVoucharActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("Shopkeeper View")
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model) {

                //Updating the cart items single card view

                String cart_product_image = model.getProduct_image();
                cart_product_image = cart_product_image.replace(".jpg", "_200x200.jpg");//Getting 200*200 Images
                Picasso.get().load(cart_product_image).into(holder.cart_image);

                holder.txtProductName.setText(model.getProduct_name());
                holder.txtProductQuantity.setText("৳ "+model.getProduct_price()+" X "+model.getNeeded_quantity());
                int one_product_total_price = Integer.parseInt(model.getTotal_price());
                holder.txtProductPrice.setText("৳ "+String.valueOf(one_product_total_price));
                holder.numberButton.setNumber(model.getNeeded_quantity());
                totalPrice += one_product_total_price;
                totalAmount.setText(String.valueOf(totalPrice));
                //vouchar add korar por ekhane change korte hbe
                subTotalAmount.setText(String.valueOf(totalPrice));

                //For deleting the cart item
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CartActivity.this);

                        alertDialogBuilder.setMessage("Are you sure you want to remove this item from cart ?");
                        alertDialogBuilder.setPositiveButton("yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        cartListRef.child("Shopkeeper View")
                                                .child(Prevalent.currentOnlineUser.getPhone())
                                                .child("Products")
                                                .child(String.valueOf(model.getProduct_id()))
                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    totalPrice = totalPrice-(Integer.parseInt(model.getTotal_price()));
                                                    totalAmount.setText(String.valueOf(totalPrice));
                                                    Toast.makeText(getApplicationContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
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
                });


                //If the user want to change the added items of the cart using elegant button

                holder.numberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                    @Override
                    public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                        int quantity = Integer.parseInt(holder.numberButton.getNumber());
                        model.setNeeded_quantity(String.valueOf(quantity));

                        //Updating data in the database

                        cartListRef.child("Shopkeeper View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(String.valueOf(model.getProduct_id())).child("needed_quantity")
                                .setValue(model.getNeeded_quantity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful())
                                {
                                    Toast.makeText(CartActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        holder.txtProductQuantity.setText("৳ "+model.getProduct_price()+" X "+model.getNeeded_quantity());
                        int one_product_price = Integer.parseInt(model.getProduct_price()) * Integer.parseInt(model.getNeeded_quantity());
                        holder.txtProductPrice.setText("৳ "+String.valueOf(one_product_price));

                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        totalPrice = 0;
                        Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);//For giving product description to the user when clicks on a cart item
                        intent.putExtra("product", model);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_sample, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
