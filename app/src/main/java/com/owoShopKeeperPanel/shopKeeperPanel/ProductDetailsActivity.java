package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.Model.CartListFromClient;
import com.owoShopKeeperPanel.Model.CartListProduct;
import com.owoShopKeeperPanel.Model.OwoProduct;
import com.owoShopKeeperPanel.configurations.HostAddress;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    //private ImageView add_product_to_wishList;
    private ProgressDialog progressDialog;
    private OwoProduct owoproduct;
    private ElegantNumberButton numberButton;
    private TextView product_brand_name;

    //int clickState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        numberButton = findViewById(R.id.quantity_number_btn);

        ImageView back_to_home = findViewById(R.id.back_to_home);
        ImageView productImage = findViewById(R.id.product_image_details);

        TextView productPriceWithDiscount = findViewById(R.id.product_price_with_discount_details);
        TextView productPrice = findViewById(R.id.product_price_details);
        TextView productDiscount = findViewById(R.id.product_discount_details);
        TextView productQuantity = findViewById(R.id.product_quantity_details);
        TextView productDescription = findViewById(R.id.product_description_details);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.product_name_details);
        //add_product_to_wishList = findViewById(R.id.add_to_wishList);
        productPriceWithDiscount =findViewById(R.id.product_price_with_discount_details);
        Button addToCartBtn = findViewById(R.id.add_to_cart_button);
        product_brand_name = findViewById(R.id.product_brand_name);

        progressDialog = new ProgressDialog(this);

        back_to_home.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        owoproduct = (OwoProduct) getIntent().getSerializableExtra("Products");

        getBrand();

        Glide.with(this).load(HostAddress.HOST_ADDRESS.getHostAddress()+owoproduct.getProductImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(productImage);

        collapsingToolbarLayout.setTitle(owoproduct.getProductName());
        productDescription.setText(owoproduct.getProductDescription());
        productPrice.setText(String.valueOf(owoproduct.getProductPrice()));
        productDiscount.setText(String.valueOf(owoproduct.getProductDiscount()));
        productQuantity.setText(String.valueOf(owoproduct.getProductQuantity()));
        double price_with_discount = owoproduct.getProductPrice() - owoproduct.getProductDiscount();
        productPriceWithDiscount.setText(String.valueOf(price_with_discount));

        productImage.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailsActivity.this, ZoomProductImage.class);
            intent.putExtra("image", owoproduct.getProductImage());
            startActivity(intent);
        });

        addToCartBtn.setOnClickListener(v -> {
            progressDialog.setTitle("Add to Cart");
            progressDialog.setMessage("Please wait while we are adding product to cart");
            progressDialog.show();

            addingToCartList();
        });

//        add_product_to_wishList.setOnClickListener(v ->
//        {
//            if(clickState%2 == 0)
//            {
//                allianceLoader.setVisibility(View.VISIBLE);
//                add_product_to_wishList.setColorFilter(ContextCompat.getColor(ProductDetailsActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
//                final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference();
//                wishListRef.child("Wish List").child(Prevalent.currentOnlineUser.getMobileNumber()).child(String.valueOf(owoproduct.getProductId())).setValue(owoproduct).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ProductDetailsActivity.this, "Added to wish list", Toast.LENGTH_SHORT).show();
//                        allianceLoader.setVisibility(View.GONE);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ProductDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        allianceLoader.setVisibility(View.GONE);
//                    }
//                });
//
//                clickState++;
//            }
//            else
//            {
//                allianceLoader.setVisibility(View.VISIBLE);
//                add_product_to_wishList.setColorFilter(ContextCompat.getColor(ProductDetailsActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);
//                final DatabaseReference wishListRef = FirebaseDatabase.getInstance().getReference();
//                wishListRef.child("Wish List").child(Prevalent.currentOnlineUser.getMobileNumber()).child(String.valueOf(owoproduct.getProductId())).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ProductDetailsActivity.this, "Removed from wish list", Toast.LENGTH_SHORT).show();
//                        allianceLoader.setVisibility(View.GONE);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ProductDetailsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        allianceLoader.setVisibility(View.GONE);
//                    }
//                });
//                clickState++;
//            }
//        });

    }

    private void getBrand() {
        RetrofitClient.getInstance().getApi()
                .getBrandNameViaProductId(owoproduct.getProductId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            String brand = null;
                            try {
                                assert response.body() != null;
                                brand = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            product_brand_name.setText(brand);
                        }
                        else
                        {
                            Toast.makeText(ProductDetailsActivity.this, "Can not get brand info", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("ProductDetails", "Error occurred , Error is: "+t.getMessage());
                        Toast.makeText(ProductDetailsActivity.this, "Can not get brand info", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addingToCartList()
    {

        String saveCurrentTime, saveCurrentDate;

        Calendar callForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        saveCurrentDate = currentDate.format(callForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm::ss a", Locale.US);
        saveCurrentTime = currentTime.format(callForDate.getTime());

        CartListProduct cartListProduct = new CartListProduct(owoproduct.getProductId(),
                owoproduct.getProductName(), owoproduct.getProductCategoryId(), owoproduct.getProductSubCategoryId(),
                owoproduct.getProductPrice(), owoproduct.getProductDiscount(),
                Integer.parseInt(numberButton.getNumber()), saveCurrentDate, saveCurrentTime, owoproduct.getProductImage());

        CartListFromClient cartList1 = new CartListFromClient(Prevalent.currentOnlineUser.getMobileNumber(), cartListProduct);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().cartListItems(cartList1);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(ProductDetailsActivity.this, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Log.e("ProductDetails", "Error occurred, Error is: "+t.getMessage());
                Toast.makeText(ProductDetailsActivity.this, "Failed to add product to cart", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
