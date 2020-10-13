package com.owoshopkeeperpanel.myShopRelated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.add_product_model;
import com.owoshopkeeperpanel.Model.product_database_model;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;

public class add_product_to_shop extends AppCompatActivity {

    private AllianceLoader loader;
    private ImageView back_to_home;
    private ImageView product_image;
    private TextView product_buying_price, saleable_quantity, product_description_details;
    private EditText product_new_price;
    private EditText profit;
    private CollapsingToolbarLayout product_name;
    private Button calculate_profit_button, add_to_shop_button;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_to_shop);

        add_product_model add_product_model = (com.owoshopkeeperpanel.Model.add_product_model) getIntent().getSerializableExtra("add_product_model");

        loader = findViewById(R.id.loader);
        back_to_home = findViewById(R.id.back_to_home);
        product_image = findViewById(R.id.product_image_details);
        product_buying_price = findViewById(R.id.product_price_details);
        saleable_quantity = findViewById(R.id.saleable_quantity);
        product_description_details = findViewById(R.id.product_description_details);
        product_new_price = findViewById(R.id.product_new_price);
        profit = findViewById(R.id.profit);
        calculate_profit_button = findViewById(R.id.calculate_profit_button);
        add_to_shop_button = findViewById(R.id.add_to_shop_button);
        product_name = findViewById(R.id.product_name_details);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Glide.with(this).load(add_product_model.getProduct_image()).into(product_image);
        product_buying_price.setText("৳ "+String.valueOf(add_product_model.getProduct_price()));
        product_name.setTitle(add_product_model.getProduct_name());
        product_description_details.setText(add_product_model.getProduct_description());
        saleable_quantity.setText(add_product_model.getProduct_quantity());

        calculate_profit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = product_new_price.getText().toString();

                if(!price.isEmpty())
                {
                    Double product_price = Double.parseDouble(price);
                    double labh = product_price - add_product_model.getProduct_price();
                    profit.setText(String.valueOf((labh/add_product_model.getProduct_price())* 100) + " %");
                }
                else
                {
                    product_new_price.setError("Please enter new price");
                    product_new_price.requestFocus();
                }
            }
        });

        add_to_shop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_selected(add_product_model);
            }
        });
    }

    private void add_selected(add_product_model add_product_model) {
        String price = product_new_price.getText().toString();

        loader.setVisibility(View.VISIBLE);


        if(!price.isEmpty())
        {
            Double product_price = Double.parseDouble(price);
            double labh = product_price - add_product_model.getProduct_price();
            profit.setText(String.valueOf((labh/add_product_model.getProduct_price())* 100) + " %");
        }
        else
        {
            product_new_price.setError("Please enter new price");
            product_new_price.requestFocus();
            loader.setVisibility(View.INVISIBLE);
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        product_database_model product_database_model = new product_database_model(add_product_model.getProduct_id(),
                add_product_model.getProduct_quantity(), price);

        databaseReference.child("Shop Products").child(Prevalent.currentOnlineUser.getPhone())
                .child(String.valueOf(add_product_model.getProduct_id())).setValue(product_database_model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(add_product_to_shop.this, "Product added to shop", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.INVISIBLE);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(add_product_to_shop.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}