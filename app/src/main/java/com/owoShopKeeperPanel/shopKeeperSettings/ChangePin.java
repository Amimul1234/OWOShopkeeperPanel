package com.owoShopKeeperPanel.shopKeeperSettings;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.homeComponents.HomeActivity;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.hashing.hashing_algo;
import com.owoShopKeeperPanel.userRegistration.ShopKeeperUser;
import org.jetbrains.annotations.NotNull;
import java.security.NoSuchAlgorithmException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePin extends AppCompatActivity {

    private ImageView show_current_pin;
    private ImageView show_new_pin;
    private ImageView show_new_pin_confirm;
    private EditText current_pin, new_pin, confirm_pin;
    private ProgressBar progressBar;
    private Boolean isShowPinCurrentPin = false;
    private Boolean isShowPinNewPin = false;
    private Boolean isShowNewPinConfirm = false;

    private ShopKeeperUser shopKeeperUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        shopKeeperUser = (ShopKeeperUser) getIntent().getSerializableExtra("shopKeeper");

        show_current_pin = findViewById(R.id.show_current_pin);
        show_new_pin = findViewById(R.id.show_new_pin);
        show_new_pin_confirm = findViewById(R.id.show_new_pin_confirm);

        progressBar = findViewById(R.id.loader);

        current_pin = findViewById(R.id.currentPin);
        new_pin = findViewById(R.id.newPinUpdate);
        confirm_pin = findViewById(R.id.newPinConfirmation);

        Button change = findViewById(R.id.change);
        ImageView back_to_home = findViewById(R.id.back_to_home);

        back_to_home.setOnClickListener(v -> onBackPressed());

        show_current_pin.setOnClickListener(v -> {
            if (isShowPinCurrentPin) {
                current_pin.setTransformationMethod(new PasswordTransformationMethod());
                show_current_pin.setImageResource(R.drawable.ic_visibility_off);
                isShowPinCurrentPin = false;

            }else{
                current_pin.setTransformationMethod(null);
                show_current_pin.setImageResource(R.drawable.ic_visibility);
                isShowPinCurrentPin = true;
            }
        });

        show_new_pin.setOnClickListener(v -> {
            if (isShowPinNewPin) {
                new_pin.setTransformationMethod(new PasswordTransformationMethod());
                show_new_pin.setImageResource(R.drawable.ic_visibility_off);
                isShowPinNewPin = false;

            }else{
                new_pin.setTransformationMethod(null);
                show_new_pin.setImageResource(R.drawable.ic_visibility);
                isShowPinNewPin = true;
            }
        });

        show_new_pin_confirm.setOnClickListener(v -> {
            if (isShowNewPinConfirm) {
                confirm_pin.setTransformationMethod(new PasswordTransformationMethod());
                show_new_pin_confirm.setImageResource(R.drawable.ic_visibility_off);
                isShowNewPinConfirm = false;

            }else{
                confirm_pin.setTransformationMethod(null);
                show_new_pin_confirm.setImageResource(R.drawable.ic_visibility);
                isShowNewPinConfirm = true;
            }
        });

        change.setOnClickListener(v -> validate());

    }

    private void validate() {
        String user_current_pin = current_pin.getText().toString();
        String user_new_pin = new_pin.getText().toString();
        String user_confirm_new_pin = confirm_pin.getText().toString();
        String hashed_pin = null;

        try {
            hashed_pin = hashing_algo.toHexString(hashing_algo.getSHA(user_current_pin));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        assert hashed_pin != null;
        if (!hashed_pin.equals(Prevalent.currentOnlineUser.getPin())) {
            current_pin.setError("Pin did not match with current pin");
            current_pin.requestFocus();
        }
        else if(user_new_pin.isEmpty())
        {
            new_pin.setError("New pin can not be null");
            new_pin.requestFocus();
        }
        else if(user_confirm_new_pin.isEmpty())
        {
            confirm_pin.setError("Please confirm your new pin");
            confirm_pin.requestFocus();
        }
        else if(!user_new_pin.equals(user_confirm_new_pin))
        {
            confirm_pin.setError("Confirm pin did not matched");
            confirm_pin.requestFocus();
        }
        else
        {
            String newHashedPin = null;
            try {
                newHashedPin = hashing_algo.toHexString(hashing_algo.getSHA(user_new_pin));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            
            changeUserInformation(newHashedPin);
        }
    }

    private void changeUserInformation(String new_pin) {
        progressBar.setVisibility(View.VISIBLE);

        shopKeeperUser.setPin(new_pin);

        RetrofitClient.getInstance().getApi()
                .updateShopKeeperInfo(shopKeeperUser)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChangePin.this, "Pin number updated successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ChangePin.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ChangePin.this, "Sorry! Can not update your info. , Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Settings", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(ChangePin.this, "Sorry! Can not update your info. , Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}