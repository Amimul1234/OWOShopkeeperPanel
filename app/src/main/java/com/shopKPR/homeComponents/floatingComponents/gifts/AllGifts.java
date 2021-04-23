package com.shopKPR.homeComponents.floatingComponents.gifts;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.shopKPR.R;
import com.shopKPR.homeComponents.floatingComponents.entities.Gifts;
import com.shopKPR.network.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllGifts extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView giftsRecyclerView;

    private final List<Gifts> giftsList = new ArrayList<>();
    private AllGiftsAdapter giftsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_gifts);

        ImageView backButton = findViewById(R.id.back_button);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        giftsRecyclerView = findViewById(R.id.gift_cards_recycler_view);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(()->{
            fetchGiftItem();
            showRecycler();
        });

        showRecycler();

        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void fetchGiftItem()
    {
        RetrofitClient.getInstance().getApi()
                .getGistsCardList()
                .enqueue(new Callback<List<Gifts>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Gifts>> call, @NotNull Response<List<Gifts>> response) {
                        if(response.isSuccessful())
                        {
                            giftsList.clear();
                            giftsList.addAll(response.body());
                            giftsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(AllGifts.this, "No gifts card available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Gifts>> call, @NotNull Throwable t) {
                        Log.e("AllGifts", t.getMessage());
                        Toast.makeText(AllGifts.this, "Can not get gift cards, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        giftsAdapter = new AllGiftsAdapter(AllGifts.this, giftsList);
        giftsRecyclerView.setAdapter(giftsAdapter);
        giftsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        giftsRecyclerView.setHasFixedSize(true);
        giftsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchGiftItem();
    }

}