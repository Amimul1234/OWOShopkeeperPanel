package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    private ElegantNumberButton numberButton;
    private TextView productPrice,productDiscount,productQuantity,productDescription,productPriceWithDiscount;
    private Button addToCartBtn;
    private ImageView back_to_home,productImage;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int totalPrice=0;

    //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Products");

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
        int price_with_discount = Integer.parseInt(products.getProduct_price())-Integer.parseInt(products.getProduct_discount());
        productPriceWithDiscount.setText(String.valueOf(price_with_discount));

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here will be code for cartlist node
                addingToCartList();
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

        final HashMap<String, Object> cartMap = new HashMap<>();
        totalPrice=Integer.parseInt(numberButton.getNumber())*Integer.parseInt(productPriceWithDiscount.getText().toString());

        cartMap.put("product_id", products.getProduct_id());
        cartMap.put("product_name", products.getProduct_name());
        cartMap.put("product_price", productPriceWithDiscount.getText().toString());//it is single price
        cartMap.put("total_price", totalPrice);
        cartMap.put("product_image", products.getProduct_image());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("needed_quantity", numberButton.getNumber());

        cartList.child("Shopkeeper View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(String.valueOf(products.getProduct_id()))
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ProductDetailsActivity.this, "Plaease check your network connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
