package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.Cart;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProductDetailsActivity extends AppCompatActivity {

    private ElegantNumberButton numberButton;
    private TextView productPrice,productDiscount,productQuantity,productDescription,productPriceWithDiscount;
    private Button addToCartBtn;
    private ImageView back_to_home,productImage, add_product_to_wishList;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AllianceLoader allianceLoader;

    int clickState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        numberButton = findViewById(R.id.quantity_number_btn);
        back_to_home = findViewById(R.id.back_to_home);
        productImage=findViewById(R.id.product_image_details);
        productPriceWithDiscount=findViewById(R.id.product_price_with_discount_details);
        collapsingToolbarLayout = findViewById(R.id.product_name_details);
        productPrice=findViewById(R.id.product_price_details);
        productDiscount=findViewById(R.id.product_discount_details);
        productQuantity=findViewById(R.id.product_quantity_details);
        productDescription=findViewById(R.id.product_description_details);
        add_product_to_wishList = findViewById(R.id.add_to_wishList);
        productPriceWithDiscount=findViewById(R.id.product_price_with_discount_details);
        addToCartBtn=findViewById(R.id.add_to_cart_button);

        allianceLoader = findViewById(R.id.loader);

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
        productPrice.setText(String.valueOf(products.getProduct_price()));
        productDiscount.setText(products.getProduct_discount());
        productQuantity.setText(products.getProduct_quantity());
        double price_with_discount = products.getProduct_price() - Double.parseDouble(products.getProduct_discount());
        productPriceWithDiscount.setText(String.valueOf(price_with_discount));

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allianceLoader.setVisibility(View.VISIBLE);
                addingToCartList();
            }
        });

        add_product_to_wishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickState == 0)
                {
                    allianceLoader.setVisibility(View.VISIBLE);
                    add_product_to_wishList.setColorFilter(ContextCompat.getColor(ProductDetailsActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference();
                    wishListRef.child("Wish List").child(Prevalent.currentOnlineUser.getPhone()).child(String.valueOf(products.getProduct_id())).setValue(products).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProductDetailsActivity.this, "Added to wish list", Toast.LENGTH_SHORT).show();
                            allianceLoader.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProductDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            allianceLoader.setVisibility(View.GONE);
                        }
                    });

                    clickState++;
                }
            }
        });


    }

    private void addingToCartList() {

        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm::ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        final DatabaseReference cartList = FirebaseDatabase.getInstance().getReference().child("Cart List");
        final com.owoshopkeeperpanel.Model.Products products = (Products) getIntent().getSerializableExtra("Products");

        Cart cart = new Cart(products.getProduct_id(), products.getProduct_image(), products.getProduct_name(), Double.parseDouble(productPriceWithDiscount.getText().toString()),
                numberButton.getNumber(), saveCurrentDate, saveCurrentTime, products.getProduct_category());

        cartList.child(Prevalent.currentOnlineUser.getPhone()).child(String.valueOf(products.getProduct_id()))
                .setValue(cart)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                            allianceLoader.setVisibility(View.GONE);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(ProductDetailsActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                            allianceLoader.setVisibility(View.GONE);
                        }

                    }
                });

    }
}
