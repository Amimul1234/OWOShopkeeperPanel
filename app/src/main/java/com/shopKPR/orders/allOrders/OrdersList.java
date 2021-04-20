package com.shopKPR.orders.allOrders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.shopKPR.R;
import com.shopKPR.pagination.orders.OrderItemViewModel;

public class OrdersList extends AppCompatActivity {

    private RecyclerView order_list_recycler_view;
    public ProgressBar progressBar;
    private ShopOrderAdapter shopOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        ImageView backButton = findViewById(R.id.back_to_home);
        order_list_recycler_view = findViewById(R.id.order_list_recycler_view);

        ImageView emptyImageView = findViewById(R.id.empty_image_view);
        TextView emptyTextView = findViewById(R.id.empty_text_view);

        progressBar = findViewById(R.id.loader);

        shopOrderAdapter = new ShopOrderAdapter(this);

        order_list_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        order_list_recycler_view.setLayoutManager(layoutManager);

        backButton.setOnClickListener(v -> onBackPressed());

        OrderItemViewModel orderItemViewModel = new OrderItemViewModel();

        orderItemViewModel.itemPagedList.observe(this, shop_keeper_orders ->
        {
            shopOrderAdapter.submitList(shop_keeper_orders);
            showOnRecyclerView();
        });
    }

    private void showOnRecyclerView()
    {
        order_list_recycler_view.setHasFixedSize(true);
        order_list_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        order_list_recycler_view.setAdapter(shopOrderAdapter);
        shopOrderAdapter.notifyDataSetChanged();
    }
}