package com.owoShopKeeperPanel.shopRegistration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.owoShopKeeperPanel.R;

public class AfterUserRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_register);

        Button button = findViewById(R.id.create_btn);

        button.setOnClickListener(v -> {
            Intent intent=new Intent(AfterUserRegister.this, ShopRegistrationRequest.class);
            startActivity(intent);
            finish();
        });
    }
}
