package com.shopKPR.login.forgetPin;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.shopKPR.Model.UserShopKeeper;
import com.shopKPR.R;
import com.shopKPR.hashing.hashing_algo;
import com.shopKPR.login.LogInActivity;
import com.shopKPR.network.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import java.security.NoSuchAlgorithmException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static android.view.View.*;
import static com.google.firebase.auth.PhoneAuthProvider.*;

public class EnterNewPasswordActivity extends AppCompatActivity {

    private String mobileNumber;
    private Boolean isShowPin = false, isShowConfirmPin = false;

    private EditText shopKeeperNewPin, shopKeeperConfirmPin;
    private ImageView showPinImage, confirmShowPinImage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_new_password);

        shopKeeperNewPin = findViewById(R.id.new_shopkeeper_pin);
        shopKeeperConfirmPin = findViewById(R.id.new_shopkeeper_confirm_pin);
        progressBar = findViewById(R.id.progressBar);
        Button changeShopPin = findViewById(R.id.change_shop_pin);
        showPinImage = findViewById(R.id.show_pin);
        confirmShowPinImage = findViewById(R.id.show_confirmed_pin);

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        String verificationId = getIntent().getStringExtra("verificationId");
        ForceResendingToken forceResendingToken = getIntent().getParcelableExtra("force_resend_token");

        showPinImage.setOnClickListener(v ->
        {
            if (isShowPin) {
                shopKeeperNewPin.setTransformationMethod(new PasswordTransformationMethod());
                showPinImage.setImageResource(R.drawable.ic_visibility_off);
                isShowPin = false;
            }else{
                shopKeeperNewPin.setTransformationMethod(null);
                showPinImage.setImageResource(R.drawable.ic_visibility);
                isShowPin = true;
            }
        });

        confirmShowPinImage.setOnClickListener(v ->
        {
            if (isShowConfirmPin) {
                shopKeeperConfirmPin.setTransformationMethod(new PasswordTransformationMethod());
                confirmShowPinImage.setImageResource(R.drawable.ic_visibility_off);
                isShowConfirmPin = false;

            }else{
                shopKeeperConfirmPin.setTransformationMethod(null);
                confirmShowPinImage.setImageResource(R.drawable.ic_visibility);
                isShowConfirmPin = true;
            }
        });

        changeShopPin.setOnClickListener(v -> checkValidity());
    }

    private void checkValidity() {
        if(shopKeeperNewPin.getText().toString().isEmpty())
        {
            shopKeeperNewPin.setError("Pin number can not be empty");
            shopKeeperNewPin.requestFocus();
        }
        else if(shopKeeperConfirmPin.getText().toString().isEmpty())
        {
            shopKeeperConfirmPin.setError("Please provide pin confirmation");
            shopKeeperConfirmPin.requestFocus();
        }
        else if(!shopKeeperNewPin.getText().toString().equals(shopKeeperConfirmPin.getText().toString()))
        {
            Toast.makeText(this, "Pin number and confirm pin does not match", Toast.LENGTH_SHORT).show();
        }
        else
        {
            try {
                changeShopKeeperPinNumber();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Log.e("EnterNewPin", e.getMessage());
                Toast.makeText(this, "Can not change, please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeShopKeeperPinNumber() throws NoSuchAlgorithmException {
        String pin = shopKeeperNewPin.getText().toString();

        String hashedPin = hashing_algo.toHexString(hashing_algo.getSHA(pin));

        UserShopKeeper userShopKeeper = new UserShopKeeper();

        userShopKeeper.setPin(hashedPin);
        userShopKeeper.setPhone(mobileNumber);

        progressBar.setVisibility(VISIBLE);

        RetrofitClient.getInstance().getApi()
                .changeShopKeeperPin(userShopKeeper)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressBar.setVisibility(GONE);
                            Toast.makeText(EnterNewPasswordActivity.this,
                                    "Pin changed successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(EnterNewPasswordActivity.this, LogInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            progressBar.setVisibility(GONE);
                            Toast.makeText(EnterNewPasswordActivity.this, "Can not change pin, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressBar.setVisibility(GONE);
                        Log.e("EnterPassword", t.getMessage());
                        Toast.makeText(EnterNewPasswordActivity.this, "Can not change pin, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}