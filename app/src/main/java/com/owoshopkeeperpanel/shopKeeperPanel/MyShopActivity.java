package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.MyShopManagementAdapter;
import com.owoshopkeeperpanel.adapters.Saleable_products;

public class MyShopActivity extends AppCompatActivity {

    private RecyclerView shop_controlling_options;
    private TextView shopName, shop_address, shop_service_mobile;
    private ImageView back_button, shop_image;

    private MyShopManagementAdapter myShopManagementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);

        shop_controlling_options = findViewById(R.id.shop_control_options);
        shopName = findViewById(R.id.my_shop_name);
        shop_address = findViewById(R.id.my_shop_address);
        shop_service_mobile = findViewById(R.id.my_shop_phone_number);
        shop_image = findViewById(R.id.my_shop_image);

        fetchFromDatabase();

        myShopManagementAdapter = new MyShopManagementAdapter(MyShopActivity.this);
        shop_controlling_options.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 6);//Configuring recyclerview to receive two layout manager
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == 0)
                {
                    return 6;
                }
                else
                    return 2;
            }
        });

        shop_controlling_options.setLayoutManager(layoutManager);

        ConcatAdapter concatAdapter  = new ConcatAdapter(new Saleable_products(this), myShopManagementAdapter);
        shop_controlling_options.setAdapter(concatAdapter);


        back_button = findViewById(R.id.back_to_home);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void fetchFromDatabase() {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query query = RootRef.child("approvedShops").child(Prevalent.currentOnlineUser.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    shop_address.setText(snapshot.child("shop_address").getValue(String.class));
                    shop_service_mobile.setText(snapshot.child("shop_service_mobile").getValue(String.class));
                    shopName.setText(snapshot.child("shop_name").getValue(String.class));
                    Glide.with(MyShopActivity.this).load(snapshot.child("shop_image_uri").getValue(String.class)).into(shop_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyShopActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
