package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Network.RetrofitClient;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Response.SingleProductResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BridgeofCartAndProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridgeof_cart_and_product);

        String id = getIntent().getStringExtra("id");

        int product_id = Integer.parseInt(id);

        Call<SingleProductResponse> call = RetrofitClient.getInstance().getApi().getProductById(product_id);

        call.enqueue(new Callback<SingleProductResponse>() {
            @Override
            public void onResponse(Call<SingleProductResponse> call, Response<SingleProductResponse> response) {
                if(!response.body().error)
                {
                    Products clicked_products = response.body().products;
                    Intent intent = new Intent(BridgeofCartAndProduct.this, ProductDetailsActivity.class);
                    intent.putExtra("Products", clicked_products);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<SingleProductResponse> call, Throwable t) {
                Toast.makeText(BridgeofCartAndProduct.this, t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}