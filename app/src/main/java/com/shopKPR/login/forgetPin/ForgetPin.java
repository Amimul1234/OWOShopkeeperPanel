package com.shopKPR.login.forgetPin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.shopKPR.R;
import java.util.concurrent.TimeUnit;

public class ForgetPin extends AppCompatActivity {

    private EditText mobileNumber;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pin);

        mobileNumber = findViewById(R.id.mobile_number);
        Button sendOTPButton = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.loader);

        sendOTPButton.setOnClickListener(v -> checkMobileNumber());
    }

    private void checkMobileNumber() {
        if (mobileNumber.getText().toString().isEmpty() || mobileNumber.getText().toString().length() < 11)
        {
            mobileNumber.setError("Please enter a valid mobile number");
            mobileNumber.requestFocus();
        }
        else
        {
            sendOtpToUser();
        }
    }

    private void sendOtpToUser() {

        progressBar.setVisibility(View.VISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88" + mobileNumber.getText().toString(),
                60,
                TimeUnit.SECONDS,
                ForgetPin.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("ForgetPin", e.getMessage());
                        Toast.makeText(ForgetPin.this, "Verification code sending failed, Please try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(ForgetPin.this, VerifyPhoneWithOTP.class);

                        intent.putExtra("mobileNumber", mobileNumber.getText().toString());
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("force_resend_token", forceResendingToken);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }
}