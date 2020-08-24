package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.owoshopkeeperpanel.R;

public class MyShopActivity extends AppCompatActivity {

    private ImageView storeSettingsImage,shopImage;
    private TextView shopName,shopAddress,shopkeeperName,shopkeeperPhone,tradeLicence;
    private Button myOrders,myProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);

        storeSettingsImage=(ImageView)findViewById(R.id.store_settings_img);
        shopImage=(ImageView)findViewById(R.id.myshop_image);
        shopName=(TextView)findViewById(R.id.myshop_name);
        shopAddress=(TextView)findViewById(R.id.myshopkeeper_address);
        shopkeeperName=(TextView)findViewById(R.id.myshopkeeper_name);
        shopkeeperPhone=(TextView)findViewById(R.id.myshopkeeper_number);
        tradeLicence=(TextView)findViewById(R.id.myshopkeeper_licence);
        myOrders=(Button)findViewById(R.id.myorders_button);
        myProducts=(Button)findViewById(R.id.myproducts_button);

        //here will be code for setting text to these textviews (it will be fetched from shop node)

        storeSettingsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyShopActivity.this,StoreSettingsActivity.class);
                startActivity(intent);
            }
        });

        myProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyShopActivity.this,MyProductsActivity.class);
                startActivity(intent);
            }
        });

        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //i will do it later
            }
        });
    }
}
