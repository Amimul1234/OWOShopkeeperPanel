package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import com.owoshopkeeperpanel.R;

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

    }
}