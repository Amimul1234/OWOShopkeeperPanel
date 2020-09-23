package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.PendingShop;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.MyShopTopPortion;

import java.util.HashMap;

public class MyShopActivity extends AppCompatActivity {

    private RecyclerView my_shop_recyclerview;
    private MyShopTopPortion myShopTopPortion;
    private HashMap<String, String> datas = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);

        my_shop_recyclerview = findViewById(R.id.myshop_recyclerview);
        my_shop_recyclerview.setHasFixedSize(true);
        fetchFromDatabase();
        my_shop_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        myShopTopPortion = new MyShopTopPortion(MyShopActivity.this, datas);
        my_shop_recyclerview.setAdapter(myShopTopPortion);
    }

    private void fetchFromDatabase() {

        //Loading shop related data to myShop

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query query = RootRef.child("approvedShops").child(Prevalent.currentOnlineUser.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    datas.put("shop_image", snapshot.child("shop_image_uri").getValue(String.class));
                    datas.put("shop_address", snapshot.child("shop_address").getValue(String.class));
                    datas.put("shop_name", snapshot.child("shop_name").getValue(String.class));
                    datas.put("shop_service_mobile", snapshot.child("shop_service_mobile").getValue(String.class));
                    myShopTopPortion.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyShopActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
