package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.Cart;
import com.owoshopkeeperpanel.Model.Qupon;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.CartListAdapter;
import com.owoshopkeeperpanel.sheetHandeler.CouponSheet;
import com.owoshopkeeperpanel.sheetHandeler.User_Info_Update_Bottom_Sheet;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView listView;
    private RecyclerView.LayoutManager layoutManager;
    private Button place_order_button;
    private TextView totalAmount, subTotalAmount, vouchartxt, empty_text;
    private double totalPrice=0;
    private ImageView back_from_cart, empty_image;
    private static AllianceLoader loader;
    private RelativeLayout tag4;

    private ArrayList<Cart> cartList = new ArrayList<>();
    private CartListAdapter cartListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView = findViewById(R.id.cart_list);

        place_order_button=(Button)findViewById(R.id.place_order_btn);
        vouchartxt=(TextView)findViewById(R.id.cart_vouchar);
        totalAmount=(TextView)findViewById(R.id.cart_total_amount);
        subTotalAmount=(TextView)findViewById(R.id.cart_sub_total_amount);
        back_from_cart=(ImageView)findViewById(R.id.back_arrow_from_cart);
        empty_image = findViewById(R.id.empty_image);
        empty_text = findViewById(R.id.empty_text);
        tag4 = findViewById(R.id.tag4);

        loader = findViewById(R.id.loader);

        loaderVisible();

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
                intent.putExtra("products_id", cartList);
                startActivity(intent);
                finish();

            }
        });

        vouchartxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CouponSheet bottomSheet = new CouponSheet(getApplicationContext());
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        cartListRef.child(Prevalent.currentOnlineUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                totalPrice = 0.0;//This two line is for handling data change
                tag4.setVisibility(View.VISIBLE);
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                    {
                        Cart dummyCart = dataSnapshot1.getValue(Cart.class);
                        cartList.add(dummyCart);
                        totalPrice += dummyCart.getProduct_price() * Double.parseDouble(dummyCart.getNeeded_quantity());
                    }

                    loaderGone();
                    cartListAdapter = new CartListAdapter(CartActivity.this, cartList);
                    listView.setAdapter(cartListAdapter);
                    totalAmount.setText("৳ "+String.valueOf(totalPrice));
                }

                else
                {
                    empty_image.setVisibility(View.VISIBLE);
                    empty_text.setVisibility(View.VISIBLE);
                    cartList.clear();
                    totalPrice = 0.0;
                    tag4.setVisibility(View.INVISIBLE);
                    loaderGone();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void loaderVisible()
    {
        loader.setVisibility(View.VISIBLE);
    }
    public static void loaderGone()
    {
        loader.setVisibility(View.GONE);
    }

}
