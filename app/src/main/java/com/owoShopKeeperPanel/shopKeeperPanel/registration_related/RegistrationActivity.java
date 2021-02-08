package com.owoShopKeeperPanel.shopKeeperPanel.registration_related;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoShopKeeperPanel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owoShopKeeperPanel.hashing.hashing_algo;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("deprecation")
public class RegistrationActivity extends AppCompatActivity {

    private EditText merchant_name, regMobile;
    private AllianceLoader loader;
    private EditText newShopkeeperPin, newShopkeeperConfirmPin;
    private Boolean isShowPin = false, isShowConfirmPin = false;
    private ImageView showPin, showConfirmPin;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        regMobile = (EditText)findViewById(R.id.shopkeeper_register_mobile);
        merchant_name = findViewById(R.id.new_shopkeeper_name);
        Button sendOTP = (Button) findViewById(R.id.send_otp_btn);

        TextView term_condition = (TextView) findViewById(R.id.terms_conditions);
        showPin = (ImageView)findViewById(R.id.show_pin);
        showConfirmPin = (ImageView)findViewById(R.id.show_confirmed_pin);
        newShopkeeperPin = (EditText)findViewById(R.id.new_shopkeeper_pin);
        newShopkeeperConfirmPin = (EditText)findViewById(R.id.new_shopkeeper_confirm_pin);
        checkBox = (CheckBox)findViewById(R.id.terms);

        loader = findViewById(R.id.loader);

        //for showing the terms and conditions
        term_condition.setOnClickListener(v -> {
            Intent intent=new Intent(RegistrationActivity.this, TermsConditionsActivity.class);
            startActivity(intent);
        });

        showPin.setOnClickListener(v -> {

            if (isShowPin) {
                newShopkeeperPin.setTransformationMethod(new PasswordTransformationMethod());
                showPin.setImageResource(R.drawable.ic_visibility_off);
                isShowPin = false;

            }else{
                newShopkeeperPin.setTransformationMethod(null);
                showPin.setImageResource(R.drawable.ic_visibility);
                isShowPin = true;
            }
        });

        showConfirmPin.setOnClickListener(v -> {

            if (isShowConfirmPin) {
                newShopkeeperConfirmPin.setTransformationMethod(new PasswordTransformationMethod());
                showConfirmPin.setImageResource(R.drawable.ic_visibility_off);
                isShowConfirmPin = false;

            }else{
                newShopkeeperConfirmPin.setTransformationMethod(null);
                showConfirmPin.setImageResource(R.drawable.ic_visibility);
                isShowConfirmPin = true;
            }
        });


        sendOTP.setOnClickListener(v -> {

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

            else if (!checkBox.isChecked())
            {
                Toast.makeText(RegistrationActivity.this, "Please accept our terms and conditions.", Toast.LENGTH_SHORT).show();
                return;
            }

            else if(verification() == null)
            {
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
                        Toast.makeText(RegistrationActivity.this, "This number is already registered, please log in", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);

                    }
                    else
                    {

                        String phoneNumber = "+" + "88" + number;//Shop Owner Mobile With Country Code
                        Intent intent = new Intent(RegistrationActivity.this, VerifyPhoneActivity.class);
                        intent.putExtra("phonenumber", phoneNumber);
                        intent.putExtra("mobilenumber", number);
                        intent.putExtra("name", name);
                        intent.putExtra("hashed_pin", verification());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(RegistrationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.GONE);
                }
            });

        });

    }

    private String verification() {

        String pin = newShopkeeperPin.getText().toString();
        String confirmpin = newShopkeeperConfirmPin.getText().toString();

        if(pin.isEmpty())
        {
            newShopkeeperPin.setError("Please enter a pin");
            newShopkeeperPin.requestFocus();
        }
        else if(confirmpin.isEmpty())
        {
            newShopkeeperConfirmPin.setError("Please enter that pin to confirm");
            newShopkeeperConfirmPin.requestFocus();
        }

        else if(!confirmpin.equals(pin))
        {
            newShopkeeperConfirmPin.setError("Pin did not match");
            newShopkeeperConfirmPin.requestFocus();
        }
        else
        {
            try {
                pin = hashing_algo.toHexString(hashing_algo.getSHA(pin));// hashing the pin
                return pin;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

}
