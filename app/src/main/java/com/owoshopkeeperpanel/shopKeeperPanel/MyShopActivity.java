package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.Ordered_products;
import com.owoshopkeeperpanel.Model.Ordered_products_model;
import com.owoshopkeeperpanel.Model.PendingShop;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.MyShopTopPortion;
import com.owoshopkeeperpanel.adapters.Saleable_products;
import com.owoshopkeeperpanel.adapters.Saleable_products_adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyShopActivity extends AppCompatActivity {

    private RecyclerView my_shop_recyclerview;

    private MyShopTopPortion myShopTopPortion;
    private Saleable_products saleable_products_tags;
    private Saleable_products_adapter saleable_products_adapter;

    private ImageView back_button;

    private HashMap<String, String> datas = new HashMap<>();
    private List<Ordered_products_model> ordered_products_modelList = new ArrayList<>();
    private List<Ordered_products> ordered_products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);

        my_shop_recyclerview = findViewById(R.id.myshop_recyclerview);

        my_shop_recyclerview.setHasFixedSize(true);
        my_shop_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        fetchFromDatabase();
        fetchForPurchasedProducts();

        myShopTopPortion = new MyShopTopPortion(MyShopActivity.this, datas);
        saleable_products_tags = new Saleable_products(this);
        saleable_products_adapter = new Saleable_products_adapter(this, ordered_products);

        ConcatAdapter concatAdapter = new ConcatAdapter(myShopTopPortion, saleable_products_tags, saleable_products_adapter);

        my_shop_recyclerview.setAdapter(concatAdapter);


        back_button = findViewById(R.id.back_to_home);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fetchForPurchasedProducts() { //This is for fetching purchased products
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();
        Query query = RootRef.child("Shop Keeper Orders").child(Prevalent.currentOnlineUser.getPhone());
        query.orderByChild("state").equalTo("Delivered").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        ordered_products_modelList.add(snapshot1.getValue(Ordered_products_model.class));
                    }

                    int size = ordered_products_modelList.size();

                    for(int i=0; i<size; i++)
                    {
                        ordered_products.addAll(ordered_products_modelList.get(i).getProduct_ids());
                    }

                    saleable_products_adapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(MyShopActivity.this, "Please purchase products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyShopActivity.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
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
