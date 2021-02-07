package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoShopKeeperPanel.Model.Shop_keeper_orders;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.ShopOrderAdapter;
import com.owoShopKeeperPanel.pagination.orders.OrderItemViewModel;

public class Order_list extends AppCompatActivity {

    private ImageView back_button, empty_image_view;
    private TextView empty_text_view;
    private RecyclerView order_list_recycler_view;
    public static AllianceLoader allianceLoader;
    private ShopOrderAdapter shopOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        back_button = findViewById(R.id.back_to_home);
        order_list_recycler_view = findViewById(R.id.order_list_recycler_view);

        empty_image_view = findViewById(R.id.empty_image_view);
        empty_text_view = findViewById(R.id.empty_text_view);

        allianceLoader = findViewById(R.id.loader);

        shopOrderAdapter = new ShopOrderAdapter(this);

        order_list_recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        order_list_recycler_view.setLayoutManager(layoutManager);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        OrderItemViewModel orderItemViewModel = new OrderItemViewModel();
        orderItemViewModel.itemPagedList.observe(this, new Observer<PagedList<Shop_keeper_orders>>() {
            @Override
            public void onChanged(PagedList<Shop_keeper_orders> shop_keeper_orders) {
                shopOrderAdapter.submitList(shop_keeper_orders);
                showOnRecyclerView();
            }
        });
    }

    private void showOnRecyclerView() {
        order_list_recycler_view.setHasFixedSize(true);
        order_list_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        order_list_recycler_view.setAdapter(shopOrderAdapter);
        shopOrderAdapter.notifyDataSetChanged();
    }
}