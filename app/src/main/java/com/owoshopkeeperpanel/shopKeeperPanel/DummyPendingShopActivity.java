package com.owoshopkeeperpanel.shopKeeperPanel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.owoshopkeeperpanel.R;

public class DummyPendingShopActivity extends AppCompatActivity {

    private ImageView shopImage,ownerNID,ownerTradeLicence;
    private Button createShopButton;
    private EditText shopName, shopServiceMobile, ownerName, ownerMobile, shopAddress;
    private TextView shopMapAddress;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_pending_shop);

        shopImage=(ImageView)findViewById(R.id.shop_image);
        ownerNID=(ImageView)findViewById(R.id.shop_owner_nid);
        ownerTradeLicence=(ImageView)findViewById(R.id.shop_trade_licence);
        createShopButton=(Button)findViewById(R.id.create_shop_btn);
        shopName=(EditText)findViewById(R.id.shop_name);
        shopServiceMobile=(EditText)findViewById(R.id.shop_service_mobile);
        ownerName=(EditText)findViewById(R.id.shop_owner_name);
        ownerMobile=(EditText)findViewById(R.id.shop_owner_mobile);
        shopAddress = findViewById(R.id.shop_address);

        shopMapAddress = findViewById(R.id.select_place);

        shopAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DummyPendingShopActivity.this, LocationFromMap.class);
                startActivityForResult(intent, 1);
            }
        });

        createShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //it will be changed later, here will be an intermediate stage for waiting approve
                Intent intent=new Intent(DummyPendingShopActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {

            Toast.makeText(this, "Got your location successfully", Toast.LENGTH_SHORT).show();
            latitude = data.getDoubleExtra("latitude", 0.0);
            longitude = data.getDoubleExtra("longitude", 0.0);
            shopMapAddress.setText(String.valueOf(latitude)+" "+String.valueOf(longitude));
        }
        else
        {
            Toast.makeText(this, "Can not get your location", Toast.LENGTH_SHORT).show();
        }
    }


}
