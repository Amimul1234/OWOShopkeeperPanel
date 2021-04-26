package com.shopKPR.userRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.UserShopKeeper;
import com.shopKPR.R;
import com.shopKPR.login.LogInActivity;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyPhoneActivity extends AppCompatActivity {

    private EditText inputOtp;
    private String mobileNumber, name, hashed_pin;
    private String verificationId;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView countDownTimer, resetOtpHeader;
    private Button resendOtp;

    private int counter;

    private ProgressDialog progressDialog;
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        mAuth = FirebaseAuth.getInstance();

        inputOtp = findViewById(R.id.otp);
        Button continueBtn = findViewById(R.id.nextButton);
        progressBar = findViewById(R.id.progressbar);

        countDownTimer = findViewById(R.id.countDownTimer);
        resetOtpHeader = findViewById(R.id.reset_otp_header);
        resendOtp = findViewById(R.id.resend_otp);

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        name = getIntent().getStringExtra("name");
        hashed_pin = getIntent().getStringExtra("hashed_pin");
        verificationId = getIntent().getStringExtra("verificationId");
        forceResendingToken = getIntent().getParcelableExtra("force_resend_token");

        resendOtp.setVisibility(View.GONE);

        new CountDownTimer(65000, 1000){
            public void onTick(long millisUntilFinished)
            {
                countDownTimer.setText(String.valueOf(65 - counter));
                counter++;
            }
            public  void onFinish(){
                resendOtp.setVisibility(View.VISIBLE);
                resetOtpHeader.setVisibility(View.GONE);
                countDownTimer.setVisibility(View.GONE);
                counter = 0;
            }
        }.start();

        continueBtn.setOnClickListener(v ->
        {
            progressBar.setVisibility(View.VISIBLE);
            String code = inputOtp.getText().toString().trim();

            if (code.isEmpty() || code.length() < 6) {
                progressBar.setVisibility(View.GONE);
                inputOtp.setError("Please Enter Code...");
                inputOtp.requestFocus();
                return;
            }
            verifyCode(code);
        });

        resendOtp.setOnClickListener(v -> sendOtpToUser("+88" + mobileNumber));
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {

                UserShopKeeper userShopKeeper = new UserShopKeeper(name, mobileNumber, hashed_pin, null);

                RetrofitClient.getInstance().getApi()
                        .registerShopKeeper(userShopKeeper)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                if(response.isSuccessful())
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Congratulations ! Your account created successfully", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    getDynamicLinks();
                                }
                                else
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(VerifyPhoneActivity.this, "Can not create account, please try again", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Network error, please try again", Toast.LENGTH_SHORT).show();
                                Log.e("Verify Phone class", t.getMessage());
                            }
                        });
            } else {
                Toast.makeText(VerifyPhoneActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDynamicLinks()
    {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData ->
                {
                    Uri deepLink;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        String queryParam = deepLink.getQueryParameter("customerMobileNumber");

                        creditToReferrer(queryParam);
                    }

                })
                .addOnFailureListener(this, e -> Log.w("Splash", "getDynamicLink:onFailure", e));
    }

    private void creditToReferrer(String queryParam) {
        RetrofitClient.getInstance().getApi()
                .addReferralPointForUser(queryParam)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        Intent intent = new Intent(VerifyPhoneActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("Verify phone", t.getMessage());

                        Intent intent = new Intent(VerifyPhoneActivity.this, LogInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void sendOtpToUser(String phoneNumberWithCountryCode)
    {
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Send OTP");
        progressDialog.setMessage("Please wait while we are sending you OTP");
        progressDialog.show();


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumberWithCountryCode)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Log.e("Verify Phone Activity", e.getMessage());
                                Toast.makeText(VerifyPhoneActivity.this, "Verification code sending failed, Please try again", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken)
                            {
                                progressDialog.dismiss();

                                Intent intent = new Intent(VerifyPhoneActivity.this, VerifyPhoneActivity.class);

                                intent.putExtra("mobileNumber", mobileNumber);
                                intent.putExtra("verificationId", verificationId);
                                intent.putExtra("hashed_pin", hashed_pin);
                                intent.putExtra("force_resend_token", forceResendingToken);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setForceResendingToken(forceResendingToken)     // ForceResendingToken from callbacks
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

}
