package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.annotation.NonNull;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.owoShopKeeperPanel.Prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.CategoryAdapter;

public class categories extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AppCompatButton searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        recyclerView = findViewById(R.id.recycler_view_for_products);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchBar = findViewById(R.id.search_product);

        CategoryAdapter categoryAdapter = new CategoryAdapter(categories.this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(categoryAdapter);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categories.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });


        ImageView contact_us = findViewById(R.id.contact_us);

        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(categories.this);

                View view = LayoutInflater.from(categories.this).inflate(R.layout.care_toast, null);

                Button call_us_now = view.findViewById(R.id.call_us_now);
                Button issue_a_complain = view.findViewById(R.id.issue_a_complain);

                builder.setView(view);

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getWindow().setLayout(950, 800);

                call_us_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL); //calling activity
                        intent.setData(Uri.parse("tel:+8801612201602"));
                        startActivity(intent);
                        alertDialog.cancel();

                    }
                });

                issue_a_complain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(categories.this, Contact_us.class);
                        intent.putExtra("mobileNumber", Prevalent.currentOnlineUser.getPhone());
                        startActivity(intent);
                        alertDialog.cancel();
                    }
                });

            }
        });




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
                        Intent intent = new Intent(categories.this, calculator.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_cart:
                    {
                        Intent intent = new Intent(categories.this, CartActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.action_account:
                    {
                        Intent intent=new Intent(categories.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.action_categories);
    }

}