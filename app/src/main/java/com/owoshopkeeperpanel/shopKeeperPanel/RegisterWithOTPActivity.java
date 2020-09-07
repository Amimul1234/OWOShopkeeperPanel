package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoshopkeeperpanel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RegisterWithOTPActivity extends AppCompatActivity {

    private EditText merchant_name, regMobile;
    private Button sendOTP;
    private AllianceLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_with_o_t_p);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        regMobile = (EditText)findViewById(R.id.shopkeeper_register_mobile);
        merchant_name = findViewById(R.id.new_shopkeeper_name);
        sendOTP = (Button)findViewById(R.id.send_otp_btn);

        loader = findViewById(R.id.loader);


        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String number = regMobile.getText().toString();

                if (number.isEmpty() || number.length() < 11) {
                    regMobile.setError("Please enter a valid number");
                    regMobile.requestFocus();
                    return;
                }

                else if(merchant_name.getText().toString().isEmpty())
                {
                    merchant_name.setError("Name can not be empty");
                    merchant_name.requestFocus();
                    return;
                }



                DatabaseReference shopkeeperRef = FirebaseDatabase.getInstance().getReference();
                final Query query = shopkeeperRef.child("Shopkeeper").orderByKey().equalTo(number);
                final String name  = merchant_name.getText().toString();

                loader.setVisibility(View.VISIBLE);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Toast.makeText(RegisterWithOTPActivity.this, "This number is already registered, please log in", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.GONE);

                        }
                        else
                        {

                            String phoneNumber = "+" + "88" + number;
                            Intent intent = new Intent(RegisterWithOTPActivity.this, VerifyPhoneActivity.class);
                            intent.putExtra("phonenumber", phoneNumber);
                            intent.putExtra("mobilenumber", number);
                            intent.putExtra("name", name);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RegisterWithOTPActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                    }
                });

            }
        });

    }
}
