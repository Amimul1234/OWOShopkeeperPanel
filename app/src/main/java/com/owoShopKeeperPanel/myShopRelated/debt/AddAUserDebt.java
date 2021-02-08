package com.owoShopKeeperPanel.myShopRelated.debt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.Model.UserDebts;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;

import org.jetbrains.annotations.NotNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAUserDebt extends AppCompatActivity {

    private EditText debt_taker_name, debt_taker_mobile_number;
    private AllianceLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_user_debtr);

        debt_taker_name = findViewById(R.id.debt_taker_name);
        debt_taker_mobile_number = findViewById(R.id.debt_taker_mobile_number);
        Button addDebtRecord = findViewById(R.id.addDebtRecord);
        loader = findViewById(R.id.loader);

        addDebtRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {
        String name = debt_taker_name.getText().toString();
        String mobile_number = debt_taker_mobile_number.getText().toString();

        if(name.isEmpty())
        {
            debt_taker_name.setError("Name can not be empty");
            debt_taker_name.requestFocus();
        }
        else if(mobile_number.isEmpty())
        {
            debt_taker_mobile_number.setError("Mobile number can not be empty");
            debt_taker_mobile_number.requestFocus();
        }
        else
        {
            loader.setVisibility(View.VISIBLE);
            saveToDataBase(name, mobile_number);
        }

    }

    private void saveToDataBase(String name, String mobile_number) {
        UserDebts userDebts = new UserDebts();

        userDebts.setUser_name(name);
        userDebts.setUser_mobile_number(mobile_number);
        userDebts.setUser_total_debt(0.0);

        RetrofitClient.getInstance().getApi()
                .addAUserDebt(userDebts, Prevalent.currentOnlineUser.getPhone())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.code() == 409)
                        {
                            Toast.makeText(AddAUserDebt.this, "User already exists", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.INVISIBLE);
                        }
                        else if(response.code() == 200)
                        {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddAUserDebt.this, "Debt record added successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddAUserDebt.this, UserDebtDetails.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Toast.makeText(AddAUserDebt.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.INVISIBLE);
                    }
                });
    }
}