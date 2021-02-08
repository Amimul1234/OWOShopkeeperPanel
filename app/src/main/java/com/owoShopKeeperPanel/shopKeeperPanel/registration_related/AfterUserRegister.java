package com.owoShopKeeperPanel.shopKeeperPanel.registration_related;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.owoShopKeeperPanel.R;

public class AfterUserRegister extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);

        button=(Button)findViewById(R.id.create_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AfterUserRegister.this, ShopRegistrationRequest.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
