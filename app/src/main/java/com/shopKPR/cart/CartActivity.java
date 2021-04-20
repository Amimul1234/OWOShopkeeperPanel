package com.shopKPR.cart;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.CartListProduct;
import com.shopKPR.Model.Qupon;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.R;
import com.shopKPR.orderConfirmation.ConfirmFinalOrderActivity;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private ListView listView;
    private TextView totalAmount;
    private TextView subTotalAmount;
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
        subTotalAmount = findViewById(R.id.cart_sub_total_amount);
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
            Toast.makeText(this, "Please enter coupon code", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
        else {
            Toast.makeText(CartActivity.this, "Please wait while we are checking coupon code", Toast.LENGTH_SHORT).show();
            checkValidation(coupon_code);
        }
    }

    private void checkValidation(String voucher) {

        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Qupon").addListenerForSingleValueEvent(new ValueEventListener() {
            int state = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        Qupon qupon = snapshot1.getValue(Qupon.class);

                        assert qupon != null;
                        String coupon_code_b = qupon.getQupon_code();

                        if(voucher.equals(coupon_code_b))
                        {
                            progressBar.setVisibility(View.INVISIBLE);
                            state = 1;
                            discount = Double.parseDouble(qupon.getQupon_discount());
                            Toast.makeText(CartActivity.this, "Congratulation! You got "+qupon.getQupon_discount()+" taka discount", Toast.LENGTH_LONG).show();

                            if(grandTotal !=0)
                            {
                                grand_total_with_discount = grandTotal - discount;
                                if(grand_total_with_discount < 0)
                                {
                                    Toast.makeText(CartActivity.this, "Purchase amount must be greater than discount", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else
                                {
                                    subTotalAmount.setText("৳ "+discount);
                                }
                            }

                            break;
                        }
                    }
                    if(state == 0)
                    {
                        Toast.makeText(CartActivity.this, "Coupon does not exists", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loaderGone();
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
