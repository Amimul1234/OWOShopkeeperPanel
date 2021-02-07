package com.owoShopKeeperPanel.shopKeeperPanel;

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

import com.owoShopKeeperPanel.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ForgetPinOtpActivity extends AppCompatActivity {

    private EditText regMobile;
    private Button sendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pin_otp);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        regMobile = (EditText)findViewById(R.id.shopkeeper_forget_mobile);
        sendOTP = (Button)findViewById(R.id.forget_send_otp_btn);


        final ProgressDialog progressDialog = new ProgressDialog(ForgetPinOtpActivity.this);
        progressDialog.setMessage("Please wait while we are checking your credentials");
        progressDialog.setCanceledOnTouchOutside(false);


        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String number = regMobile.getText().toString();

                if (number.isEmpty() || number.length() < 11) {
                    regMobile.setError("Please enter a valid number");
                    regMobile.requestFocus();
                    return;
                }

                DatabaseReference shopkeeperRef = FirebaseDatabase.getInstance().getReference();
                final Query query = shopkeeperRef.child("Shopkeeper").orderByKey().equalTo(number);

                progressDialog.show();

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            String phoneNumber = "+" + "88" + number;
                            Intent intent = new Intent(ForgetPinOtpActivity.this, ForgetVerifyPhoneActivity.class);
                            intent.putExtra("phonenumber", phoneNumber);
                            intent.putExtra("mobilenumber", number);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(ForgetPinOtpActivity.this, "This number isn't registered yet.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ForgetPinOtpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

            }
        });
    }
}
