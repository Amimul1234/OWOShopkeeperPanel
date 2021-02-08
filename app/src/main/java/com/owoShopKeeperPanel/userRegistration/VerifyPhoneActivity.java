package com.owoShopKeeperPanel.userRegistration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoShopKeeperPanel.Model.User_shopkeeper;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.login.LogInActivity;

public class VerifyPhoneActivity extends AppCompatActivity {

    private EditText inputOtp;
    private String mobileNumber, name, hashed_pin;
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();

        inputOtp = (EditText)findViewById(R.id.otp);
        Button continueBtn = (Button) findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressbar);

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        name = getIntent().getStringExtra("name");
        hashed_pin = getIntent().getStringExtra("hashed_pin");
        verificationId = getIntent().getStringExtra("verificationId");

        continueBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String code = inputOtp.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                inputOtp.setError("Enter code...");
                inputOtp.requestFocus();
                return;
            }

            verifyCode(code);
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final DatabaseReference RootRef;
                        RootRef = FirebaseDatabase.getInstance().getReference();

                        User_shopkeeper user_shopkeeper = new User_shopkeeper(name,
                                mobileNumber, hashed_pin, null);

                        RootRef.child("Shopkeeper").child(mobileNumber).setValue(user_shopkeeper).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful())
                            {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Congratulations ! Your account created successfully", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut(); 
                                Intent intent = new Intent(VerifyPhoneActivity.this, LogInActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Network error, please try again", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
