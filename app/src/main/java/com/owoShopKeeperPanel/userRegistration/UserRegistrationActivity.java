package com.owoShopKeeperPanel.userRegistration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.hashing.hashing_algo;
import com.owoShopKeeperPanel.login.LogInActivity;
import org.jetbrains.annotations.NotNull;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText merchant_name, regMobile;
    private ProgressBar loader;
    private EditText newShopkeeperPin, newShopkeeperConfirmPin;
    private Boolean isShowPin = false, isShowConfirmPin = false;
    private ImageView showPin, showConfirmPin;
    private CheckBox checkBox;
    private Button sendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

            final WindowInsetsController insetsController = getWindow().getInsetsController();

            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        regMobile = findViewById(R.id.shopkeeper_register_mobile);
        merchant_name = findViewById(R.id.new_shopkeeper_name);
        sendOTP = findViewById(R.id.send_otp_btn);

        TextView term_condition = findViewById(R.id.terms_conditions);
        showPin = findViewById(R.id.show_pin);
        showConfirmPin = findViewById(R.id.show_confirmed_pin);
        newShopkeeperPin = findViewById(R.id.new_shopkeeper_pin);
        newShopkeeperConfirmPin = findViewById(R.id.new_shopkeeper_confirm_pin);
        checkBox = findViewById(R.id.terms);
        loader = findViewById(R.id.loader);

        //for showing the terms and conditions
        term_condition.setOnClickListener(v -> {
            Intent intent=new Intent(UserRegistrationActivity.this, TermsConditionsActivity.class);
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
                regMobile.setError("Please enter a valid mobile number");
                regMobile.requestFocus();
            }
            else if(merchant_name.getText().toString().isEmpty())
            {
                merchant_name.setError("Name can not be empty");
                merchant_name.requestFocus();
            }
            else if (!checkBox.isChecked())
            {
                Toast.makeText(UserRegistrationActivity.this, "Please accept our terms and conditions.", Toast.LENGTH_SHORT).show();
            }

            else if(verification() == null)
            {

            }
            else
            {
                loader.setVisibility(View.VISIBLE);

                RetrofitClient.getInstance().getApi()
                        .getShopKeeper(number)
                        .enqueue(new Callback<ShopKeeperUser>() {
                            @Override
                            public void onResponse(@NotNull Call<ShopKeeperUser> call, @NotNull Response<ShopKeeperUser> response) {
                                if(response.isSuccessful())
                                {
                                    Toast.makeText(UserRegistrationActivity.this, "This number is already registered, please log in", Toast.LENGTH_SHORT).show();
                                    loader.setVisibility(View.GONE);

                                    Intent intent = new Intent(UserRegistrationActivity.this, LogInActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    String phoneNumberWithCountryCode = "+" + "88" + number;
                                    sendOtpToUser(phoneNumberWithCountryCode);
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ShopKeeperUser> call, @NotNull Throwable t) {
                                Log.e("User Registration", "Error is: "+t.getMessage());
                                Toast.makeText(UserRegistrationActivity.this, "Error occurred, please try again", Toast.LENGTH_SHORT).show();
                                loader.setVisibility(View.GONE);
                            }
                        });
            }

        });

    }

    private void sendOtpToUser(String phoneNumberWithCountryCode)
    {
        loader.setVisibility(View.VISIBLE);
        sendOTP.setVisibility(View.INVISIBLE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumberWithCountryCode,
                60,
                TimeUnit.SECONDS,
                UserRegistrationActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        loader.setVisibility(View.GONE);
                        sendOTP.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        loader.setVisibility(View.GONE);
                        sendOTP.setVisibility(View.VISIBLE);
                        Log.e("Verify Phone Activity", e.getMessage());
                        Toast.makeText(UserRegistrationActivity.this, "Verification code sending failed, Please try again", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        loader.setVisibility(View.GONE);
                        sendOTP.setVisibility(View.VISIBLE);

                        Intent intent = new Intent(UserRegistrationActivity.this, VerifyPhoneActivity.class);
                        intent.putExtra("phoneNumberWithCountryCode", phoneNumberWithCountryCode);
                        intent.putExtra("mobileNumber", regMobile.getText().toString());
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("name", merchant_name.getText().toString());
                        intent.putExtra("hashed_pin", verification());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    private String verification()//Pin related checking
    {

        String pin = newShopkeeperPin.getText().toString();
        String confirmPin = newShopkeeperConfirmPin.getText().toString();

        if(pin.isEmpty())
        {
            newShopkeeperPin.setError("Please enter a pin");
            newShopkeeperPin.requestFocus();
        }
        else if(confirmPin.isEmpty())
        {
            newShopkeeperConfirmPin.setError("Please enter that pin to confirm");
            newShopkeeperConfirmPin.requestFocus();
        }

        else if(!confirmPin.equals(pin))
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
