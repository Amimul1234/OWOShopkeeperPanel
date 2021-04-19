package com.shopKPR.myShopManagement.userDebts.debt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shopKPR.R;
import com.shopKPR.myShopManagement.userDebts.adapter.UserDebtAdapter;
import com.shopKPR.myShopManagement.userDebts.model.DebtDashBoardResponse;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.pagination.userDebts.UserDebtViewModel;
import com.shopKPR.prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DebtDetailsDashBoard extends AppCompatActivity {

    private UserDebtAdapter userDebtAdapter;
    private TextView totalLoan, totalPaid;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_debt_details);

        recyclerView = findViewById(R.id.userDebtDetails);
        FloatingActionButton floatingActionButton = findViewById(R.id.add_a_new_debt);
        ImageView back_button = findViewById(R.id.back_to_home);

        progressBar = findViewById(R.id.addDebtRecordProgressbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        userDebtAdapter = new UserDebtAdapter(this);

        totalLoan = findViewById(R.id.total_loan);
        totalPaid = findViewById(R.id.total_paid);

        getTotalPaidAndTotalLoan();
        getDebtDetails();

        back_button.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        floatingActionButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(DebtDetailsDashBoard.this, AddAUserDebt.class);
            startActivity(intent);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getTotalPaidAndTotalLoan();
            getDebtDetails();
        });
    }

    private void getDebtDetails()
    {
        UserDebtViewModel userDebtViewModel = new UserDebtViewModel();

        userDebtViewModel.itemPagedList.observe(this, userDebts ->
        {
            userDebtAdapter.submitList(userDebts);
            userDebtAdapter.notifyDataSetChanged();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userDebtAdapter);
        userDebtAdapter.notifyDataSetChanged();

        swipeRefreshLayout.setRefreshing(false);
    }


    private void getTotalPaidAndTotalLoan()
    {

        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .getDebtDashBoardEntries(Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<DebtDashBoardResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<DebtDashBoardResponse> call, @NotNull Response<DebtDashBoardResponse> response) {
                        if(response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);

                            DebtDashBoardResponse dashBoardResponse = response.body();

                            assert dashBoardResponse != null;
                            String totalLoanString = "৳" + dashBoardResponse.getTotalLoan();
                            String totalPaidString = "৳" + dashBoardResponse.getTotalPaid();

                            totalLoan.setText(totalLoanString);
                            totalPaid.setText(totalPaidString);
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(DebtDetailsDashBoard.this, "Can not get total, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<DebtDashBoardResponse> call, @NotNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("DebtDash", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(DebtDetailsDashBoard.this, "Can not get total, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}