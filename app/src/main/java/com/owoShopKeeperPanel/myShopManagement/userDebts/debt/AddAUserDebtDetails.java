package com.owoShopKeeperPanel.myShopManagement.userDebts.debt;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.Model.User_debt_details;
import com.owoShopKeeperPanel.R;
import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAUserDebtDetails extends AppCompatActivity {

    private EditText debtDescription, debtAmount, debtTakingDate;
    private final Calendar myCalendar = Calendar.getInstance();
    private Long userId;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_user_debt_details);

        debtDescription = findViewById(R.id.debt_description);
        debtAmount = findViewById(R.id.debt_amount);
        debtTakingDate = findViewById(R.id.debt_taking_date);
        progressDialog = new ProgressDialog(this);

        userId = getIntent().getLongExtra("user_id", -1);

        ImageView back_to_home = findViewById(R.id.back_to_home);
        ImageView debt_taking_calendar_dialog = findViewById(R.id.debt_taking_calendar_dialog);
        Button addDebtRecord = findViewById(R.id.addDebtRecord);

        progressDialog.setTitle("Add A User Debt");
        progressDialog.setMessage("Please wait while we are adding record");

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) ->
        {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        debt_taking_calendar_dialog.setOnClickListener(v ->
                new DatePickerDialog(AddAUserDebtDetails.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        back_to_home.setOnClickListener(v -> finish());
        addDebtRecord.setOnClickListener(v -> check());
    }

    private void check() {

        String debt_taka = debtAmount.getText().toString();
        String debt_date = debtTakingDate.getText().toString();

        if(debt_taka.isEmpty())
        {
            debtAmount.setError("Debt amount can not be empty");
            debtAmount.requestFocus();
        }
        else if(debt_date.isEmpty())
        {
            debtTakingDate.setError("Debt taking date can not be empty");
            debtTakingDate.requestFocus();
        }
        else
        {
            saveToDatabase();
        }
    }

    private void saveToDatabase()
    {
        User_debt_details user_debt_details = new User_debt_details();

        user_debt_details.setDescription(debtDescription.getText().toString());

        try {
            user_debt_details.setTaka(Double.parseDouble(debtAmount.getText().toString()));
        }catch (Exception e)
        {
            return;
        }

        user_debt_details.setDate(debtTakingDate.getText().toString());

        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .addADebtDetailsForACustomer(user_debt_details, userId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.code() == 404)
                        {
                            Toast.makeText(AddAUserDebtDetails.this, "User not found", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else if(response.code() == 201)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(AddAUserDebtDetails.this, "Record added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddAUserDebtDetails.this, DebtDetailsForACustomer.class);
                            intent.putExtra("user_id", userId);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else if(response.code() == 424)
                        {
                            progressDialog.dismiss();
                            Toast.makeText(AddAUserDebtDetails.this, "Failed to add record, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("AddUserDebtDetails", t.getMessage());
                        Toast.makeText(AddAUserDebtDetails.this, "Failed to add record, Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        debtTakingDate.setText(sdf.format(myCalendar.getTime()));
    }

}