package com.owoShopKeeperPanel.myShopManagement.userDebts.debt;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.Model.UserDebts;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import org.jetbrains.annotations.NotNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAUserDebt extends AppCompatActivity {

    private EditText debtTakerName, debtTakerMobileNumber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_user_debtr);

        debtTakerName = findViewById(R.id.debt_taker_name);
        debtTakerMobileNumber = findViewById(R.id.debt_taker_mobile_number);
        progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Add An User Debt Record");
        progressDialog.setMessage("Please wait while we are adding user record");
        progressDialog.setCancelable(false);

        ImageView imageView = findViewById(R.id.back_to_home);
        Button addDebtRecord = findViewById(R.id.addDebtRecord);

        imageView.setOnClickListener(v -> onBackPressed());
        addDebtRecord.setOnClickListener(v -> check());
    }

    private void check()
    {
        String name = debtTakerName.getText().toString();
        String mobile_number = debtTakerMobileNumber.getText().toString();

        if(name.isEmpty())
        {
            debtTakerName.setError("Name can not be empty");
            debtTakerName.requestFocus();
        }
        else if(mobile_number.isEmpty())
        {
            debtTakerMobileNumber.setError("Mobile number can not be empty");
            debtTakerMobileNumber.requestFocus();
        }
        else
        {
            progressDialog.show();
            saveToDataBase(name, mobile_number);
        }

    }

    private void saveToDataBase(String name, String mobile_number)
    {
        UserDebts userDebts = new UserDebts();

        userDebts.setUserName(name);
        userDebts.setUserMobileNumber(mobile_number);
        userDebts.setUserTotalDebt(0.0);
        userDebts.setUserPaid(0.0);

        RetrofitClient.getInstance().getApi()
                .addAUserDebt(userDebts, Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.code() == 409)
                        {
                            Toast.makeText(AddAUserDebt.this, "User already exists", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else if(response.code() == 200)
                        {
                            progressDialog.dismiss();

                            Toast.makeText(AddAUserDebt.this, "Debt record added successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AddAUserDebt.this, DebtDetailsDashBoard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("AddAUserDebt", t.getMessage());
                        Toast.makeText(AddAUserDebt.this, "Can not add user record, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}