package com.owoShopKeeperPanel.myShopRelated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.owoShopKeeperPanel.Model.Ordered_products_model;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.Ordered_products;
import com.owoShopKeeperPanel.adapters.Saleable_products_adapter;

import java.util.ArrayList;
import java.util.List;

public class Add_product_to_my_shop extends AppCompatActivity {

    private ImageView back_button;
    private RecyclerView bought_products;
    private Saleable_products_adapter saleable_products_adapter;
    private List<Ordered_products_model> ordered_products_modelList = new ArrayList<>();
    private List<Ordered_products> ordered_products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_my_shop);

        back_button = findViewById(R.id.back_to_home);
        bought_products = findViewById(R.id.purchased_products);

        saleable_products_adapter = new Saleable_products_adapter(this, ordered_products);

        bought_products.setHasFixedSize(true);
        bought_products.setLayoutManager(new LinearLayoutManager(this));
        bought_products.setAdapter(saleable_products_adapter);

        fetchForPurchasedProducts();

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

        Query query = RootRef.child("Shop Keeper Orders");

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
                    Toast.makeText(Add_product_to_my_shop.this, "Please purchase products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Add_product_to_my_shop.this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }



}