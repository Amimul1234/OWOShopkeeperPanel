package com.owoshopkeeperpanel.myShopRelated;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.owoshopkeeperpanel.Model.UserDebts;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.UserDebtAdapter;
import com.owoshopkeeperpanel.pagination.userDebts.UserDebtViewModel;

public class UserDebtDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private UserDebtAdapter userDebtAdapter;
    private ImageView back_button;
    private static AllianceLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_debt_details);

        recyclerView = findViewById(R.id.userDebtDetails);
        floatingActionButton = findViewById(R.id.add_a_new_debt);
        back_button = findViewById(R.id.back_to_home);

        loader = findViewById(R.id.loader);

        userDebtAdapter = new UserDebtAdapter(this);

        UserDebtViewModel userDebtViewModel = new UserDebtViewModel();

        userDebtViewModel.itemPagedList.observe(this, new Observer<PagedList<UserDebts>>() {
            @Override
            public void onChanged(PagedList<UserDebts> userDebts) {
                userDebtAdapter.submitList(userDebts);
                userDebtAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userDebtAdapter);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDebtDetails.this, AddAUserDebt.class);
                startActivity(intent);
            }
        });
    }

}