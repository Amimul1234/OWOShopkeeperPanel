package com.shopKPR.homeComponents.floatingComponents.deals;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.shopKPR.R;
import com.shopKPR.homeComponents.floatingComponents.entities.Deals;
import com.shopKPR.network.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllDealsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView dealsRecyclerView;

    private final List<Deals> dealsList = new ArrayList<>();
    private DealsAdapter dealsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_deals);

        ImageView backButton = findViewById(R.id.back_button);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        dealsRecyclerView = findViewById(R.id.all_deals_recycler_view);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(()->{
            fetChDealItems();
            showRecycler();
        });

        showRecycler();

        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void fetChDealItems()
    {
        RetrofitClient.getInstance().getApi()
                .getAllDeals()
                .enqueue(new Callback<List<Deals>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Deals>> call, @NotNull Response<List<Deals>> response) {
                        if(response.isSuccessful())
                        {
                            dealsList.clear();
                            dealsList.addAll(response.body());
                            dealsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(AllDealsActivity.this, "No deals available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Deals>> call, @NotNull Throwable t) {
                        Log.e("AllDeals", t.getMessage());
                        Toast.makeText(AllDealsActivity.this, "Can not get deals, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        dealsAdapter = new DealsAdapter(AllDealsActivity.this, dealsList);
        dealsRecyclerView.setAdapter(dealsAdapter);
        dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dealsRecyclerView.setHasFixedSize(true);
        dealsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetChDealItems();
    }
}