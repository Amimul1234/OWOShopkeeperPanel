package com.owoShopKeeperPanel.login;

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
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.Model.Shops;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.hashing.hashing_algo;
import com.owoShopKeeperPanel.shopKeeperPanel.HomeActivity;
import com.owoShopKeeperPanel.shopRegistration.AfterUserRegister;
import com.owoShopKeeperPanel.shopRegistration.AfterShopRegisterRequest;
import com.owoShopKeeperPanel.userRegistration.ShopKeeperUser;
import com.owoShopKeeperPanel.userRegistration.UserRegistrationActivity;
import org.jetbrains.annotations.NotNull;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("deprecation")
public class LogInActivity extends AppCompatActivity {

    private EditText mobile, pin;
    private ImageView visibility;
    private Boolean isShowPin = false;
    private CheckBox rememberMe;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        loadingbar = new ProgressDialog(this);

        if(Paper.book().read(Prevalent.UserPhoneKey) != null && Paper.book().read(Prevalent.UserPinKey) != null)
        {
            loadingbar.setTitle("Account Login");
            loadingbar.setMessage("Please wait while we are checking your credentials....");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            AllowAccessToAccount(Paper.book().read(Prevalent.UserPhoneKey), Paper.book().read(Prevalent.UserPinKey));
        }

        forgetPin.setOnClickListener(v -> {
            Intent intent=new Intent(LogInActivity.this, UserRegistrationActivity.class);
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
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please wait while we are checking your credentials....");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            try {
                Pin = hashing_algo.toHexString(hashing_algo.getSHA(Pin));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            AllowAccessToAccount(Phone, Pin);
        }
    }

    //Should Shift to spring
    private void AllowAccessToAccount(final String phone, final String pin) {

        RetrofitClient.getInstance().getApi()
                .getShopKeeper(phone)
                .enqueue(new Callback<ShopKeeperUser>() {
                    @Override
                    public void onResponse(@NotNull Call<ShopKeeperUser> call, @NotNull Response<ShopKeeperUser> response) {
                        if(response.isSuccessful())
                        {
                            ShopKeeperUser shopKeeperUser = response.body();

                            assert shopKeeperUser != null;
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
                                                        List<String> permitted = new ArrayList<>();

                                                        for(ShopKeeperPermissions shopKeeperPermissions : shops.getShopKeeperPermissions())
                                                        {
                                                            permitted.add(shopKeeperPermissions.getPermittedCategory());
                                                        }

                                                        Prevalent.category_to_display = permitted;

                                                        Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();

                                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                                        Prevalent.currentOnlineUser = shopKeeperUser;
                                                        loadingbar.dismiss();
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                    }
                                                    
                                                    else//Requested shop creation. But not approved.
                                                    {
                                                        Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplicationContext(), AfterShopRegisterRequest.class);
                                                        Prevalent.currentOnlineUser = shopKeeperUser;
                                                        loadingbar.dismiss();
                                                        startActivity(intent);
                                                    }

                                                }
                                                else //Didn't requested for register yet
                                                {
                                                    Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), AfterUserRegister.class);
                                                    Prevalent.currentOnlineUser = shopKeeperUser;
                                                    loadingbar.dismiss();
                                                    startActivity(intent);
                                                }
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<Shops> call, @NotNull Throwable t) {
                                                Toast.makeText(LogInActivity.this, "Please try again abcd", Toast.LENGTH_SHORT).show();
                                                Log.error("Log in activity", "Error is: "+t.getMessage());
                                                loadingbar.dismiss();
                                            }
                                        });

                            }
                        }

                        else
                        {
                            Toast.makeText(LogInActivity.this, "No such account", Toast.LENGTH_SHORT).show();
                            loadingbar.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ShopKeeperUser> call, @NotNull Throwable t) {
                        Toast.makeText(LogInActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                        Log.error("Log in activity", "Error is: "+t.getMessage());
                        loadingbar.dismiss();
                    }
                });
    }
}
