package com.shopKPR.login;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.esotericsoftware.minlog.Log;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.Shops;
import com.shopKPR.R;
import com.shopKPR.login.forgetPin.ForgetPin;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.hashing.hashing_algo;
import com.shopKPR.homeComponents.HomeActivity;
import com.shopKPR.shopRegistration.AfterUserRegister;
import com.shopKPR.shopRegistration.AfterShopRegisterRequest;
import com.shopKPR.userRegistration.ShopKeeperUser;
import com.shopKPR.userRegistration.UserRegistrationActivity;
import org.jetbrains.annotations.NotNull;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInActivity extends AppCompatActivity {

    private EditText mobile, pin;
    private ImageView visibility;
    private Boolean isShowPin = false;
    private CheckBox rememberMe;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

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

        Paper.init(this);

        Button loginButton = findViewById(R.id.login_btn);
        mobile = findViewById(R.id.shopkeeper_mobile);
        pin = findViewById(R.id.shopkeeper_pin);
        visibility = findViewById(R.id.show_pin);
        rememberMe = findViewById(R.id.remember_me);
        TextView forgetPin = findViewById(R.id.forget_pin);
        TextView signUp = findViewById(R.id.sign_up);
        progressDialog = new ProgressDialog(this);

        if(Paper.book().read(Prevalent.UserPhoneKey) != null && Paper.book().read(Prevalent.UserPinKey) != null)
        {
            progressDialog.setTitle("Account Login");
            progressDialog.setMessage("Please wait while we are checking your credentials....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            AllowAccessToAccount(Paper.book().read(Prevalent.UserPhoneKey), Paper.book().read(Prevalent.UserPinKey));
        }

        forgetPin.setOnClickListener(v -> {
            Intent intent=new Intent(LogInActivity.this, ForgetPin.class);
            startActivity(intent);
            finish();
        });

        signUp.setOnClickListener(v -> {
            Intent intent=new Intent(LogInActivity.this, UserRegistrationActivity.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(v -> loginUser());

        visibility.setOnClickListener(v -> {

            if (isShowPin) {
                pin.setTransformationMethod(new PasswordTransformationMethod());
                visibility.setImageResource(R.drawable.ic_visibility_off);
                isShowPin = false;

            }else{
                pin.setTransformationMethod(null);
                visibility.setImageResource(R.drawable.ic_visibility);
                isShowPin = true;
            }
        });
    }

    private void loginUser() {

        String Phone = mobile.getText().toString();
        String Pin = pin.getText().toString();

        if(Phone.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter a phone number", Toast.LENGTH_SHORT).show();
        }
        else if(Pin.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please write your pin", Toast.LENGTH_SHORT).show();
        }

        else
        {
            progressDialog.setTitle("Login Account");
            progressDialog.setMessage("Please wait while we are checking your credentials....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            try {
                AllowAccessToAccount(Phone, hashing_algo.toHexString(hashing_algo.getSHA(Pin)));
            } catch (NoSuchAlgorithmException e) {
                Toast.makeText(this, "Error occurred, please try again", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void AllowAccessToAccount(final String phone, final String pin)
    {

        RetrofitClient.getInstance().getApi()
                .getShopKeeper(phone)
                .enqueue(new Callback<ShopKeeperUser>() {
                    @Override
                    public void onResponse(@NotNull Call<ShopKeeperUser> call, @NotNull Response<ShopKeeperUser> response) {
                        if(response.isSuccessful())
                        {
                            ShopKeeperUser shopKeeperUser = response.body();

                            assert shopKeeperUser != null;
                            if(shopKeeperUser.getAccountEnabled()) //Account blocked checking
                            {
                                if(shopKeeperUser.getPin().equals(pin))
                                {
                                    if(rememberMe.isChecked())//Writing user data on android storage
                                    {
                                        Paper.book().write(Prevalent.UserPhoneKey, phone);
                                        Paper.book().write(Prevalent.UserPinKey, pin);
                                    }

                                    RetrofitClient.getInstance().getApi()
                                            .getShopInfo(phone)
                                            .enqueue(new Callback<Shops>() {
                                                @Override
                                                public void onResponse(@NotNull Call<Shops> call, @NotNull Response<Shops> response) {
                                                    if(response.isSuccessful())
                                                    {
                                                        Shops shops = response.body();

                                                        assert shops != null;
                                                        if(shops.getApproved()) //Shop is already approved. Proceed to checking permissions
                                                        {
                                                            List<Long> permitted = new ArrayList<>();

                                                            for(ShopKeeperPermissions shopKeeperPermissions : shops.getShopKeeperPermissions())
                                                            {
                                                                permitted.add(shopKeeperPermissions.getPermittedCategoryId());
                                                            }

                                                            Prevalent.category_to_display.clear();
                                                            Prevalent.category_to_display.addAll(permitted);

                                                            Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();

                                                            progressDialog.dismiss();
                                                            Prevalent.currentOnlineUser = shopKeeperUser;

                                                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                        }

                                                        else//Requested shop creation. But not approved.
                                                        {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(getApplicationContext(), AfterShopRegisterRequest.class);
                                                            Prevalent.currentOnlineUser = shopKeeperUser;
                                                            startActivity(intent);
                                                        }

                                                    }
                                                    else //Didn't requested for register yet
                                                    {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(getApplicationContext(), AfterUserRegister.class);
                                                        Prevalent.currentOnlineUser = shopKeeperUser;
                                                        startActivity(intent);
                                                    }
                                                    finish();
                                                }

                                                @Override
                                                public void onFailure(@NotNull Call<Shops> call, @NotNull Throwable t) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(LogInActivity.this, "Please try again abcd", Toast.LENGTH_SHORT).show();
                                                    Log.error("Log in activity", "Error is: "+t.getMessage());
                                                }
                                            });

                                }
                                else
                                {
                                    progressDialog.dismiss();
                                    Toast.makeText(LogInActivity.this, "Wrong pin number!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LogInActivity.this, "Your account is disabled by admin, please contact us for further information", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(LogInActivity.this, "No such account", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ShopKeeperUser> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(LogInActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        Log.error("Log in activity", "Error is: "+t.getMessage());
                    }
                });
    }
}
