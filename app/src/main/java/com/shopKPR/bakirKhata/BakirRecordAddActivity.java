package com.shopKPR.bakirKhata;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.shopKPR.R;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BakirRecordAddActivity extends AppCompatActivity {

    private EditText name, productName, productPrice, date;
    private Spinner paymentMethod;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakir_record_add);

        name = findViewById(R.id.name);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        date = findViewById(R.id.date);
        paymentMethod = findViewById(R.id.payment_method_spinner);
        Button addRecordToDatabase = findViewById(R.id.addBakirRecord);
        progressDialog = new ProgressDialog(this);

        ImageView backToHome = findViewById(R.id.back_to_home);
        backToHome.setOnClickListener(v -> onBackPressed());

        addRecordToDatabase.setOnClickListener(v -> checkForValidation());

    }

    private void checkForValidation() {
        if(name.getText().toString().isEmpty())
        {
            name.setError("Name can not be empty");
            name.requestFocus();
        }
        else if(productName.getText().toString().isEmpty())
        {
            productName.setError("Product Name can not be empty");
            productName.requestFocus();
        }
        else
        {
            progressDialog.setTitle("Add New Bakir Record");
            progressDialog.setMessage("Please wait while we are adding new bakir record");
            progressDialog.show();

            saveToDatabase();
        }
    }

    private void saveToDatabase() {

        BakirRecord bakirRecord = new BakirRecord();

        bakirRecord.setShopMobileNumber(Prevalent.currentOnlineUser.getMobileNumber());
        bakirRecord.setCustomerName(name.getText().toString());
        bakirRecord.setProductName(productName.getText().toString());
        bakirRecord.setProductPrice(productPrice.getText().toString());
        bakirRecord.setDate(date.getText().toString());
        bakirRecord.setPaymentMethod(paymentMethod.getSelectedItem().toString());

        RetrofitClient.getInstance().getApi()
                .addNewBakirRecord(bakirRecord)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(BakirRecordAddActivity.this, "Record added successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(BakirRecordAddActivity.this, "Can not add record", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("BakirRecordAdd", t.getMessage());
                        Toast.makeText(BakirRecordAddActivity.this, "Can not add record", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}