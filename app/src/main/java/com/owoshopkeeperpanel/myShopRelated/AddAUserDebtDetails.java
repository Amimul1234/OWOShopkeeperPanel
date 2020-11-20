package com.owoshopkeeperpanel.myShopRelated;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import com.owoshopkeeperpanel.Model.User_debt_details;
import com.owoshopkeeperpanel.R;
import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAUserDebtDetails extends AppCompatActivity {

    private ImageView back_to_home, debt_taking_calendar_dialog;
    private EditText debt_description, debt_amount, debt_taking_date;
    private Button addDebtRecord;
    private AllianceLoader loader;

    final Calendar myCalendar = Calendar.getInstance();

    private Long user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_user_debt_details);

        back_to_home = findViewById(R.id.back_to_home);
        debt_description = findViewById(R.id.debt_description);
        debt_amount = findViewById(R.id.debt_amount);
        debt_taking_date = findViewById(R.id.debt_taking_date);
        debt_taking_calendar_dialog = findViewById(R.id.debt_taking_calendar_dialog);
        addDebtRecord = findViewById(R.id.addDebtRecord);
        loader = findViewById(R.id.loader);

        user_id = getIntent().getLongExtra("user_id", -1);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        debt_taking_calendar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddAUserDebtDetails.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addDebtRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {

        String debt_taka = debt_amount.getText().toString();
        String debt_date = debt_taking_date.getText().toString();

        if(debt_taka.isEmpty())
        {
            debt_amount.setError("Debt amount can not be empty");
            debt_amount.requestFocus();
        }
        else if(debt_date.isEmpty())
        {
            debt_taking_date.setError("Debt taking date can not be empty");
            debt_taking_date.requestFocus();
        }

        else
        {
            savetoDatabase();
        }
    }

    private void savetoDatabase() {
        User_debt_details user_debt_details = new User_debt_details();

        user_debt_details.setDescription(debt_description.getText().toString());

        try {
            user_debt_details.setTaka(Double.parseDouble(debt_amount.getText().toString()));
        }catch (Exception e)
        {
            return;
        }

        user_debt_details.setDate(debt_taking_date.getText().toString());

        loader.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .addADebtDetailsForACustomer(user_debt_details, user_id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.code() == 404)
                        {
                            Toast.makeText(AddAUserDebtDetails.this, "User not found", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.INVISIBLE);
                        }
                        else if(response.code() == 201)
                        {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddAUserDebtDetails.this, "Record added", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddAUserDebtDetails.this, DebtDetailsForACustomer.class);
                            intent.putExtra("user_id", user_id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else if(response.code() == 424)
                        {
                            loader.setVisibility(View.INVISIBLE);
                            Toast.makeText(AddAUserDebtDetails.this, "Failed to add record, Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        loader.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddAUserDebtDetails.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        debt_taking_date.setText(sdf.format(myCalendar.getTime()));
    }

}