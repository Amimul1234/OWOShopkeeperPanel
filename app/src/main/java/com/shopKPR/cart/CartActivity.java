package com.shopKPR.cart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.CartListProduct;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.R;
import com.shopKPR.orderConfirmation.ConfirmFinalOrderActivity;
import com.shopKPR.qupon.Qupon;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private ListView listView;
    private TextView totalAmount;
    private TextView discountText;
    private double grandTotal = 0.0;
    private double discount = 0.0, grand_total_with_discount = 0.0;
    private ImageView empty_image;
    private ProgressBar progressBar;
    private TextView empty_text;
    private RelativeLayout tag4;

    String couponCodeString;

    private ArrayList<CartListProduct> CartListProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView = findViewById(R.id.cart_list);

        Button place_order_button = findViewById(R.id.place_order_btn);
        TextView vouchartxt = findViewById(R.id.cart_vouchar);
        totalAmount = findViewById(R.id.cart_total_amount);
        discountText = findViewById(R.id.cart_discount);
        ImageView back_from_cart = findViewById(R.id.back_arrow_from_cart);
        empty_image = findViewById(R.id.empty_image);
        empty_text = findViewById(R.id.empty_text);
        tag4 = findViewById(R.id.tag4);

        progressBar = findViewById(R.id.loader);

        loaderVisible();

        fetch_from_database();

        back_from_cart.setOnClickListener(v -> finish());


        place_order_button.setOnClickListener(v ->
        {
            Intent intent = new Intent(getApplicationContext(), ConfirmFinalOrderActivity.class);

            intent.putExtra("grand_total", String.valueOf(grandTotal));
            intent.putExtra("CartListProducts", CartListProducts);
            intent.putExtra("grand_total_with_discount", String.valueOf(grand_total_with_discount));
            intent.putExtra("discount", String.valueOf(discount));

            startActivity(intent);
            finish();
        });

        vouchartxt.setOnClickListener(v -> inflateDialogueForCoupon());
    }


    public void inflateDialogueForCoupon()
    {

        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.get_coupon_code, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Coupon Code");

        alertDialog.setCancelable(false);

        EditText coupon_code = view.findViewById(R.id.coupon_code);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", (dialog, which) -> {
            loaderVisible();
            couponCodeString = coupon_code.getText().toString();
            check(couponCodeString);
            dialog.dismiss();
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.dismiss());
        alertDialog.setView(view);
        alertDialog.show();
    }

    public void fetch_from_database()
    {
        RetrofitClient.getInstance().getApi().
                getCartListProducts(Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<List<CartListProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CartListProduct>> call, @NotNull Response<List<CartListProduct>> response) {
                        if(response.isSuccessful())
                        {
                            CartListProducts = (ArrayList<CartListProduct>) response.body();

                            assert CartListProducts != null;
                            if(CartListProducts.isEmpty())
                            {
                                empty_image.setVisibility(View.VISIBLE);
                                empty_text.setVisibility(View.VISIBLE);
                                empty_text.setText(R.string.empty_ite);
                                tag4.setVisibility(View.INVISIBLE);
                            }

                            for(CartListProduct cartListProduct : CartListProducts)
                            {
                                grandTotal += cartListProduct.getProductPrice() * cartListProduct.getProductQuantity();
                            }

                            String grandTotalString = String.format(Locale.ENGLISH, "%.2f", grandTotal);
                            totalAmount.setText(grandTotalString);

                            loaderGone();

                            CartListAdapter cartListAdapter = new CartListAdapter(CartActivity.this, CartListProducts);
                            listView.setAdapter(cartListAdapter);

                        }
                        else{
                            empty_image.setVisibility(View.VISIBLE);
                            empty_text.setVisibility(View.VISIBLE);
                            empty_text.setText(R.string.noItem);
                            tag4.setVisibility(View.INVISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<CartListProduct>> call, @NotNull Throwable t) {
                        Log.e("CartActivity", "Error is: "+t.getMessage());
                        Toast.makeText(CartActivity.this, "Failed to get cart list, please try again", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void check(String coupon_code) {

        if(coupon_code.isEmpty())
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please enter coupon code", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(CartActivity.this, "Please wait while we are checking coupon code", Toast.LENGTH_SHORT).show();
            checkValidation(coupon_code);
        }
    }

    private void checkValidation(String voucher) {

        String head = voucher.substring(0, 8);
        String code = voucher.substring(8);

        progressBar.setVisibility(View.VISIBLE);

        if(!head.equalsIgnoreCase("ShopKPR#"))
        {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Please provide correct coupon code", Toast.LENGTH_SHORT).show();
        }
        else
        {
            RetrofitClient.getInstance().getApi()
                    .getQupon(Long.parseLong(code))
                    .enqueue(new Callback<Qupon>() {
                        @Override
                        public void onResponse(@NotNull Call<Qupon> call, @NotNull Response<Qupon> response) {
                            if(response.isSuccessful())
                            {
                                Qupon qupon = response.body();
                                assert qupon != null;

                                if(grandTotal < qupon.getDiscount())
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(CartActivity.this, "Please order minimum :"+ qupon.getDiscount() + "to get coupon discount", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                checkUserAlreadyTakenCouponOrNot(qupon);
                            }
                            else
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(CartActivity.this, "Wrong coupon code, please try with correct coupon code", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<Qupon> call, @NotNull Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "Can not get coupon code, please try again", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void checkUserAlreadyTakenCouponOrNot(Qupon qupon) {
        RetrofitClient.getInstance().getApi()
                .checkUserAlreadyTakenCoupon(Prevalent.currentOnlineUser.getMobileNumber(), qupon.getQuponId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CartActivity.this, "Sorry! Multiple time coupon taking is not allowed", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            RetrofitClient.getInstance().getApi()
                                    .addNewCouponToUser(Prevalent.currentOnlineUser.getMobileNumber(), qupon)
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                            if(response.isSuccessful())
                                            {
                                                progressBar.setVisibility(View.GONE);
                                                discount = qupon.getDiscount();
                                                Toast.makeText(CartActivity.this, "Congrats you got discount", Toast.LENGTH_SHORT).show();
                                                discountText.setText(String.valueOf(qupon.getDiscount()));
                                            }
                                            else
                                            {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(CartActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                            progressBar.setVisibility(View.GONE);
                                            Log.e("CartActivity", t.getMessage());
                                            Toast.makeText(CartActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("CartActivity", t.getMessage());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this, "Can not get data, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void loaderVisible()
    {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void loaderGone()
    {
        progressBar.setVisibility(View.GONE);
    }

    public void grand_total_updater(String string)
    {
        totalAmount.setText(string);
        grand_total_with_discount = grandTotal - discount;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public ImageView getEmpty_image() {
        return empty_image;
    }

    public void empty_text_setter(String empty_text) {
        this.empty_text.setVisibility(View.VISIBLE);
        this.empty_text.setText(empty_text);
    }

    public RelativeLayout getTag4() {
        return tag4;
    }

}
