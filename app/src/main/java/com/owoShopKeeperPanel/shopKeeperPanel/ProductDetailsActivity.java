package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.Model.CartListFromClient;
import com.owoShopKeeperPanel.Model.Cart_list_product;
import com.owoShopKeeperPanel.Model.OwoProduct;
import com.owoShopKeeperPanel.configurations.HostAddress;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private ElegantNumberButton numberButton;
    private TextView productPrice,productDiscount,productQuantity,productDescription,productPriceWithDiscount, product_brand_name;
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
        product_brand_name = findViewById(R.id.product_brand_name);

        allianceLoader = findViewById(R.id.loader);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        final OwoProduct owoproduct = (OwoProduct) getIntent().getSerializableExtra("Products");

        Glide.with(this).load(HostAddress.HOST_ADDRESS.getHostAddress()+owoproduct.getProductImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(productImage);

        collapsingToolbarLayout.setTitle(owoproduct.getProductName());
        productDescription.setText(owoproduct.getProductDescription());
        productPrice.setText(String.valueOf(owoproduct.getProductPrice()));
        productDiscount.setText(String.valueOf(owoproduct.getProductDiscount()));
        productQuantity.setText(String.valueOf(owoproduct.getProductQuantity()));
        double price_with_discount = owoproduct.getProductPrice() - owoproduct.getProductDiscount();
        productPriceWithDiscount.setText(String.valueOf(price_with_discount));

        //product_brand_name.setText(owoproduct.getBrands());

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, ZoomProductImage.class);
                intent.putExtra("image", owoproduct.getProductImage());
                startActivity(intent);
            }
        });

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
                if(clickState%2 == 0)
                {
                    allianceLoader.setVisibility(View.VISIBLE);
                    add_product_to_wishList.setColorFilter(ContextCompat.getColor(ProductDetailsActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                    final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference();
                    wishListRef.child("Wish List").child(Prevalent.currentOnlineUser.getMobileNumber()).child(String.valueOf(owoproduct.getProductId())).setValue(owoproduct).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                else
                {
                    allianceLoader.setVisibility(View.VISIBLE);
                    add_product_to_wishList.setColorFilter(ContextCompat.getColor(ProductDetailsActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
                    final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference();
                    wishListRef.child("Wish List").child(Prevalent.currentOnlineUser.getMobileNumber()).child(String.valueOf(owoproduct.getProductId())).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProductDetailsActivity.this, "Removed from wish list", Toast.LENGTH_SHORT).show();
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

        final OwoProduct owoproduct = (OwoProduct) getIntent().getSerializableExtra("Products");


        /*
        Cart_list_product cart_list_product = new Cart_list_product(owoproduct.getProductId(),
                owoproduct.getProductName(), owoproduct.getProductCategoryId(), owoproduct.getProductPrice(), owoproduct.getProductDiscount(),
                Integer.parseInt(numberButton.getNumber()), owoproduct.getProductDescription(), saveCurrentDate, saveCurrentTime, owoproduct.getProductSubCategoryId(),
                owoproduct.getBrands(), owoproduct.getProductImage());

         */

        Cart_list_product cart_list_product = new Cart_list_product();

        CartListFromClient cartList1 = new CartListFromClient(Prevalent.currentOnlineUser.getMobileNumber(), cart_list_product);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().cartListItems(cartList1);

        allianceLoader.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    allianceLoader.setVisibility(View.INVISIBLE);
                    Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                allianceLoader.setVisibility(View.INVISIBLE);
                Toast.makeText(ProductDetailsActivity.this, "Failed to add product to cart", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
