package com.shopKPR.bakirKhata;

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
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shopKPR.R;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BakirKhataDashBoard extends AppCompatActivity
{
    private BakirKhataAdapter bakirKhataAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final List<BakirRecord> bakirRecordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bakir_khata_dash_board);

        FloatingActionButton floatingActionButton = findViewById(R.id.add_a_new_debt);
        ImageView back_button = findViewById(R.id.back_to_home);

        RecyclerView recyclerView = findViewById(R.id.bakirRecords);
        progressBar = findViewById(R.id.addDebtRecordProgressbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        bakirKhataAdapter = new BakirKhataAdapter(this, bakirRecordList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(bakirKhataAdapter);

        getDebtDetails();

        back_button.setOnClickListener(v ->
        {
            onBackPressed();
            finish();
        });

        floatingActionButton.setOnClickListener(v ->
        {
            Intent intent = new Intent(BakirKhataDashBoard.this, BakirRecordAddActivity.class);
            startActivity(intent);
        });

        swipeRefreshLayout.setOnRefreshListener(this::getDebtDetails);

    }

    private void getDebtDetails()
    {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .bakirRecordList(Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<List<BakirRecord>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<BakirRecord>> call, @NotNull Response<List<BakirRecord>> response) {
                        if(response.isSuccessful())
                        {
                            progressBar.setVisibility(View.GONE);
                            bakirRecordList.clear();
                            bakirRecordList.addAll(response.body());
                            bakirKhataAdapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(BakirKhataDashBoard.this, "Can not get bakir record, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<BakirRecord>> call, @NotNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("Bakir Khata", t.getMessage());
                        Toast.makeText(BakirKhataDashBoard.this, "Can not get bakir record, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getDebtDetails();
    }
}