package com.shopKPR.shopKeeperPanel.product_related;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.shopKPR.homeComponents.brandsComponent.entity.Brands;
import com.shopKPR.R;
import com.shopKPR.adapters.ItemAdapterBrand;
import com.shopKPR.adapters.Product_tag;
import com.shopKPR.pagination.brands.ItemViewModelBrands;
import com.shopKPR.shopKeeperPanel.SearchActivity;

public class BrandWiseProduct extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Brands brands;
    private ItemAdapterBrand adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_wise_product);

        ImageView back_to_home = findViewById(R.id.back_to_home);
        AppCompatButton search_product = findViewById(R.id.search_product);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        brands = (Brands) getIntent().getSerializableExtra("brand");

        recyclerView = findViewById(R.id.recycler_view_for_products);

        adapter = new ItemAdapterBrand(this);

        back_to_home.setOnClickListener(v -> onBackPressed());

        search_product.setOnClickListener(v -> {
            Intent intent = new Intent(BrandWiseProduct.this, SearchActivity.class);
            startActivity(intent);
            finish();
        });

        getProducts();

        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

        swipeRefreshLayout.setOnRefreshListener(this::getProducts);
    }

    public void getProducts() {

        ItemViewModelBrands itemViewModelBrands = new ItemViewModelBrands(brands.getBrandId());

        itemViewModelBrands.getItemPagedList().observe(this, items -> {
            adapter.submitList(items);
            showOnRecyclerView();
        });

        itemViewModelBrands.getNetworkState().observe(this,
                networkState -> adapter.setNetworkState(networkState));
    }

    private void showOnRecyclerView() {

        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == 0)
                {
                    return 2;
                }
                else
                    return 1;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        Product_tag product_tag = new Product_tag(this);
        ConcatAdapter concatAdapter = new ConcatAdapter(product_tag, adapter);
        recyclerView.setAdapter(concatAdapter);
        swipeRefreshLayout.setRefreshing(false);
        adapter.notifyDataSetChanged();
    }
}