package com.owoshopkeeperpanel.myShopRelated;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.Model.add_product_model;
import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Response.SingleProductResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class new_product_adding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridgeof_cart_and_product);

        String id = getIntent().getStringExtra("id");
        String quantity = getIntent().getStringExtra("quantity");

        int product_id = Integer.parseInt(id);

        Call<SingleProductResponse> call = RetrofitClient.getInstance().getApi().getProductById(product_id);

        call.enqueue(new Callback<SingleProductResponse>() {
            @Override
            public void onResponse(Call<SingleProductResponse> call, Response<SingleProductResponse> response) {
                if (response.body() != null) {
                    if(!response.body().error)
                    {
                        Owo_product clicked_owoproduct = response.body().owoproduct;

                        add_product_model add_product_model = new add_product_model(clicked_owoproduct.getProduct_id(), clicked_owoproduct.getProduct_price(),
                                clicked_owoproduct.getProduct_image(), clicked_owoproduct.getProduct_name(), quantity, clicked_owoproduct.getProduct_description());

                        Intent intent = new Intent(new_product_adding.this, add_product_to_shop.class);
                        intent.putExtra("add_product_model", add_product_model);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(new_product_adding.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(new_product_adding.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SingleProductResponse> call, Throwable t) {
                Toast.makeText(new_product_adding.this, t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}