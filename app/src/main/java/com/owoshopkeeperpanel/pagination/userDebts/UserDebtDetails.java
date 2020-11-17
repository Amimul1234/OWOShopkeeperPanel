package com.owoshopkeeperpanel.pagination.userDebts;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import com.owoshopkeeperpanel.Model.UserDebts;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDebtDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_debt_details);

        UserDebts userDebts = new UserDebts();

        userDebts.setUser_name("Amimul Ehsan");
        userDebts.setUser_mobile_number("01762303638");
        userDebts.setUser_total_debt(50000.5050);

        RetrofitClient.getInstance().getApi()
                .addAUserDebt(userDebts, Prevalent.currentOnlineUser.getPhone())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                Toast.makeText(UserDebtDetails.this, response.body().string(), Toast.LENGTH_SHORT).show(); //Got response successfully
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Toast.makeText(UserDebtDetails.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}