package com.shopKPR.products;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.CartListFromClient;
import com.shopKPR.Model.CartListProduct;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.R;
import com.shopKPR.shopKeeperPanel.ZoomProductImage;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private OwoProduct owoproduct;
    private ElegantNumberButton numberButton;
    private TextView product_brand_name;
    private ImageView addProductToWishList;

    private RecyclerView similarProducts;
    private SimilarProductsAdapter similarProductsAdapter;
    private int clickState = 0;

    private List<OwoProduct> owoProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        ImageView back_to_home = findViewById(R.id.back_to_home);
        ImageView productImage = findViewById(R.id.product_image_details);
        TextView productPrice = findViewById(R.id.product_price_details);
        TextView productDiscount = findViewById(R.id.product_discount_details);
        TextView productQuantity = findViewById(R.id.product_quantity_details);
        TextView productDescription = findViewById(R.id.product_description_details);
        TextView productPriceWithDiscount =findViewById(R.id.product_price_with_discount_details);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.product_name_details);
        addProductToWishList = findViewById(R.id.add_to_wishList);
        Button addToCartBtn = findViewById(R.id.add_to_cart_button);

        numberButton = findViewById(R.id.quantity_number_btn);
        similarProducts = findViewById(R.id.similarProducts);
        product_brand_name = findViewById(R.id.product_brand_name);
        progressDialog = new ProgressDialog(this);

        back_to_home.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        owoproduct = (OwoProduct) getIntent().getSerializableExtra("Products");

        getBrand();
        getSimilarProducts(owoproduct.getProductSubCategoryId());
        getWishListProducts(Prevalent.currentOnlineUser.getShopKeeperId());

        Glide.with(this).load(HostAddress.HOST_ADDRESS.getHostAddress()+owoproduct.getProductImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(productImage);

        collapsingToolbarLayout.setTitle(owoproduct.getProductName());
        productDescription.setText(owoproduct.getProductDescription());

        String productPriceText = "৳ "+ owoproduct.getProductPrice();
        productPrice.setText(productPriceText);

        String productDiscountText = "৳ " + owoproduct.getProductDiscount();
        productDiscount.setText(productDiscountText);

        productQuantity.setText(String.valueOf(owoproduct.getProductQuantity()));

        double price_with_discount = owoproduct.getProductPrice() - owoproduct.getProductDiscount();
        String productDiscountString = "৳ " + price_with_discount;
        productPriceWithDiscount.setText(productDiscountString);

        productImage.setOnClickListener(v ->
        {
            Intent intent = new Intent(ProductDetailsActivity.this, ZoomProductImage.class);
            intent.putExtra("image", owoproduct.getProductImage());
            startActivity(intent);
        });

        addToCartBtn.setOnClickListener(v ->
        {
            progressDialog.setTitle("Add to Cart");
            progressDialog.setMessage("Please wait while we are adding product to cart");
            progressDialog.show();

            addingToCartList();
        });

        numberButton.setRange(1, owoproduct.getProductQuantity());

        addProductToWishList.setOnClickListener(v ->
        {
            if(clickState % 2 == 0)
            {
                progressDialog.setTitle("Add product to wish list");
                progressDialog.setMessage("Please wait while we are adding product to wish list");
                progressDialog.setCancelable(false);
                progressDialog.show();

                addProductToWishList.setColorFilter(ContextCompat.getColor(
                        ProductDetailsActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

                addToWishList(owoproduct.getProductId());

                clickState++;
            }
            else
            {
                progressDialog.setTitle("Add product to wish list");
                progressDialog.setMessage("Please wait while we are removing product from wish list");
                progressDialog.setCancelable(false);
                progressDialog.show();

                addProductToWishList.setColorFilter(ContextCompat.getColor(
                        ProductDetailsActivity.this, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN);

                removeProductFromWishList(owoproduct.getProductId());

                clickState++;
            }
        });

    }

    private void getWishListProducts(Long shopKeeperId)
    {
        progressDialog.setTitle("Wish list");
        progressDialog.setMessage("Please wait while we are fetching products from wish list");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .getAllWishListProductsId(shopKeeperId)
                .enqueue(new Callback<List<Long>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Long>> call, @NotNull Response<List<Long>> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            List<Long> productsIdList = response.body();

                            assert productsIdList != null;
                            for(Long productId : productsIdList)
                            {
                                if(productId.equals(owoproduct.getProductId()))
                                {
                                    addProductToWishList.setColorFilter(ContextCompat.getColor(
                                            ProductDetailsActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

                                    clickState = 1;
                                    break;
                                }
                            }
                        }
                        else
                        {
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Long>> call, @NotNull Throwable t)
                    {
                        progressDialog.dismiss();
                        Log.e("WishList", t.getMessage());
                        Toast.makeText(ProductDetailsActivity.this, "Error while getting wish list", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeProductFromWishList(Long productId)
    {
        RetrofitClient.getInstance().getApi()
                .deleteWishListProduct(Prevalent.currentOnlineUser.getShopKeeperId(), productId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();

                            Toast.makeText(ProductDetailsActivity.this, "Product removed from wish list",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.dismiss();

                            Toast.makeText(ProductDetailsActivity.this, "Can not remove product from wish list",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t)
                    {
                        progressDialog.dismiss();
                        Log.e("WishList", t.getMessage());
                        Toast.makeText(ProductDetailsActivity.this, "Can not remove product from wish list",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addToWishList(Long productId)
    {
        RetrofitClient.getInstance().getApi()
                .addProductToWishList(Prevalent.currentOnlineUser.getShopKeeperId(), productId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ProductDetailsActivity.this, "Product added to wish list",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ProductDetailsActivity.this, "Can not add product to wish list",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t)
                    {
                        progressDialog.dismiss();
                        Log.e("WishList", t.getMessage());
                        Toast.makeText(ProductDetailsActivity.this, "Can not add product to wish list",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getSimilarProducts(Long productSubCategoryId)
    {
        RetrofitClient.getInstance().getApi()
                .getSimilarProducts(productSubCategoryId)
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.isSuccessful())
                        {
                            owoProductList = response.body();

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(ProductDetailsActivity.this, 2);
                            similarProductsAdapter = new SimilarProductsAdapter(ProductDetailsActivity.this, owoProductList, progressDialog);
                            similarProducts.setLayoutManager(gridLayoutManager);
                            similarProducts.setAdapter(similarProductsAdapter);
                        }
                        else
                        {
                            Toast.makeText(ProductDetailsActivity.this, "Can not get similar products, please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t) {
                        Log.e("ProductDetails", t.getMessage());
                        Toast.makeText(ProductDetailsActivity.this, "Can not get similar products, please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getBrand()
    {
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
