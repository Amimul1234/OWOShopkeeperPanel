package com.owoShopKeeperPanel.myShopRelated.debt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.UserDebtAdapter;
import com.owoShopKeeperPanel.pagination.userDebts.UserDebtViewModel;

public class UserDebtDetails extends AppCompatActivity {

    private UserDebtAdapter userDebtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_debt_details);

        RecyclerView recyclerView = findViewById(R.id.userDebtDetails);
        FloatingActionButton floatingActionButton = findViewById(R.id.add_a_new_debt);
        ImageView back_button = findViewById(R.id.back_to_home);

        ProgressBar progressBar = findViewById(R.id.addDebtRecordProgressbar);

        userDebtAdapter = new UserDebtAdapter(this);

        UserDebtViewModel userDebtViewModel = new UserDebtViewModel();

        userDebtViewModel.itemPagedList.observe(this, userDebts ->
        {
            userDebtAdapter.submitList(userDebts);
            userDebtAdapter.notifyDataSetChanged();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userDebtAdapter);

        back_button.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserDebtDetails.this, AddAUserDebt.class);
            startActivity(intent);
        });
    }

}