package com.owoshopkeeperpanel.myShopRelated;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.owoshopkeeperpanel.Model.UserDebts;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.UserDebtAdapter;
import com.owoshopkeeperpanel.pagination.userDebts.UserDebtViewModel;

public class UserDebtDetails extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private UserDebtAdapter userDebtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_debt_details);

        recyclerView = findViewById(R.id.userDebtDetails);
        floatingActionButton = findViewById(R.id.add_a_new_debt);

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
    }
}