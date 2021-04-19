package com.shopKPR.homeComponents.categoryComponents;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shopKPR.configurations.ServiceMobile;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.R;
import com.shopKPR.shopCart.CartActivity;
import com.shopKPR.shopKeeperPanel.Calculator;
import com.shopKPR.shopKeeperPanel.Contact_us;
import com.shopKPR.shopKeeperPanel.SearchActivity;
import com.shopKPR.shopKeeperSettings.SettingsActivity;

public class Categories extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CategoryAdapter categoryAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        recyclerView = findViewById(R.id.recycler_view_for_products);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        AppCompatButton searchBar = findViewById(R.id.search_product);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

        categoryAdapter = new CategoryAdapter(Categories.this);

        categoryAdapter.getData();
        attachToRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(()->{
            categoryAdapter.getData();
            attachToRecyclerView();
            swipeRefreshLayout.setRefreshing(false);
        });

        searchBar.setOnClickListener(v -> {
            Intent intent = new Intent(Categories.this, SearchActivity.class);
            startActivity(intent);
            finish();
        });


        ImageView contact_us = findViewById(R.id.contact_us);

        contact_us.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(Categories.this);

            View view = LayoutInflater.from(Categories.this).inflate(R.layout.care_toast, null);

            Button call_us_now = view.findViewById(R.id.call_us_now);
            Button issue_a_complain = view.findViewById(R.id.issue_a_complain);

            builder.setView(view);

            final AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getWindow().setLayout(950, 800);

            call_us_now.setOnClickListener(v12 -> {
                Intent intent = new Intent(Intent.ACTION_DIAL); //calling activity
                intent.setData(Uri.parse("tel:"+ ServiceMobile.SERVICE_MOBILE.getServiceMobile()));
                startActivity(intent);
                alertDialog.cancel();

            });

            issue_a_complain.setOnClickListener(v1 -> {
                Intent intent = new Intent(Categories.this, Contact_us.class);
                intent.putExtra("mobileNumber", Prevalent.currentOnlineUser.getMobileNumber());
                startActivity(intent);
                alertDialog.cancel();
            });

        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item ->
        {
            switch (item.getItemId()) {
                case R.id.action_home:
                {
                    onBackPressed();
                    break;
                }
                case R.id.action_categories: {
                    break;
                }
                case R.id.action_calculator:
                {
                    Intent intent = new Intent(Categories.this, Calculator.class);
                    startActivity(intent);
                    break;
                }
                case R.id.action_cart:
                {
                    Intent intent = new Intent(Categories.this, CartActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.action_account:
                {
                    Intent intent=new Intent(Categories.this, SettingsActivity.class);
                    startActivity(intent);
                    break;
                }
            }
            return true;
        });
    }

    public void attachToRecyclerView()
    {
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.action_categories);
    }

}