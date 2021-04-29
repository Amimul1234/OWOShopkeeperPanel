package com.shopKPR.homeComponents.accountInfo.notifications;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.shopKPR.R;
import com.shopKPR.network.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView dealsRecyclerView;
    private final List<Notifications> notificationsList = new ArrayList<>();
    private NotificationsAdapter notificationsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_deals);

        ImageView backButton = findViewById(R.id.back_button);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        dealsRecyclerView = findViewById(R.id.all_deals_recycler_view);

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));

        swipeRefreshLayout.setOnRefreshListener(()->
        {
            fetchNotifications();
            showRecycler();
        });

        showRecycler();

        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void fetchNotifications()
    {
        RetrofitClient.getInstance().getApi()
                .getAllNotifications()
                .enqueue(new Callback<List<Notifications>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Notifications>> call,
                                           @NotNull Response<List<Notifications>> response)
                    {
                        if(response.isSuccessful())
                        {
                            notificationsList.clear();
                            assert response.body() != null;
                            notificationsList.addAll(response.body());
                            notificationsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(NotificationsActivity.this, "No notifications available", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Notifications>> call, @NotNull Throwable t) {
                        Log.e("AllNotifications", t.getMessage());
                        Toast.makeText(NotificationsActivity.this, "Can not get notifications, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void showRecycler() {
        notificationsAdapter = new NotificationsAdapter(NotificationsActivity.this, notificationsList);
        dealsRecyclerView.setAdapter(notificationsAdapter);
        dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dealsRecyclerView.setHasFixedSize(true);
        notificationsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchNotifications();
    }
}