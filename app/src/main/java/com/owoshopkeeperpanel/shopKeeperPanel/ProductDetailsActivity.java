package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.R;

public class ProductDetailsActivity extends AppCompatActivity {

    private ElegantNumberButton numberButton;
    private TextView totalPrice,productPrice,productDiscount,productQuantity,productDescription,productPriceWithDiscount;
    private Button totalPriceBtn,addToCartBtn;
    private ImageView back_to_home,productImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        numberButton = findViewById(R.id.quantity_number_btn);
        back_to_home = findViewById(R.id.back_to_home);
        totalPrice = findViewById(R.id.total_price_details);
        totalPriceBtn = findViewById(R.id.new_price_calculate);
        productImage=findViewById(R.id.product_image_details);
        productPriceWithDiscount=findViewById(R.id.product_price_with_discount_details);
        collapsingToolbarLayout = findViewById(R.id.product_name_details);
        productPrice=findViewById(R.id.product_price_details);
        productDiscount=findViewById(R.id.product_discount_details);
        productQuantity=findViewById(R.id.product_quantity_details);
        productDescription=findViewById(R.id.product_description_details);
        productPriceWithDiscount=findViewById(R.id.product_price_with_discount_details);
        addToCartBtn=findViewById(R.id.add_to_cart_button);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        final com.owoshopkeeperpanel.Model.Products products = (Products) getIntent().getSerializableExtra("Products");

        Glide.with(this).load(products.getProduct_image()).into(productImage);
        collapsingToolbarLayout.setTitle(products.getProduct_name());
        productDescription.setText(products.getProduct_description());
        productPrice.setText(products.getProduct_price());
        productDiscount.setText(products.getProduct_discount());
        productQuantity.setText(products.getProduct_quantity());
        productPriceWithDiscount.setText(Integer.parseInt(products.getProduct_price())-Integer.parseInt(products.getProduct_discount()));

        totalPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalPrice.setText(Integer.parseInt(numberButton.getNumber())*Integer.parseInt(productPriceWithDiscount.getText().toString()));
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here will be code for cartlist node
                Intent intent=new Intent(ProductDetailsActivity.this,CartActivity.class);
                startActivity(intent);
            }
        });
    }
}
