package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.owoshopkeeperpanel.R;

public class MyProductsActivity extends AppCompatActivity {

    //here will be list of all the products of that particular shop
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
    }
}
