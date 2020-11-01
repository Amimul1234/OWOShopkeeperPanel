package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.owoshopkeeperpanel.Model.Brands;
import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.BrandsAdapter;
import com.owoshopkeeperpanel.adapters.ItemAdapterBrand;
import com.owoshopkeeperpanel.adapters.ItemAdapterSubCategory;
import com.owoshopkeeperpanel.adapters.Product_tag;
import com.owoshopkeeperpanel.pagination.Brands.ItemViewModelBrands;
import com.owoshopkeeperpanel.pagination.SubCategory.ItemViewModelSubCategory;

public class BrandWiseProduct extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView back_to_home;
    private AppCompatButton search_product;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Brands brands;
    private ItemAdapterBrand adapter;
    private String[] categories = (String[]) Prevalent.category_to_display.stream().toArray(String[]::new);

    private ItemViewModelBrands itemViewModelBrands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_wise_product);

        back_to_home = findViewById(R.id.back_to_home);
        search_product = findViewById(R.id.search_product);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        brands = (Brands) getIntent().getSerializableExtra("brand");

        recyclerView = findViewById(R.id.recycler_view_for_products);

        adapter = new ItemAdapterBrand(this);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BrandWiseProduct.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getProducts();

        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemViewModelBrands.clear();
                getProducts();
            }
        });
    }

    public void getProducts() {
        itemViewModelBrands = new ItemViewModelBrands(categories, brands.getBrand_name());
        itemViewModelBrands.itemPagedList.observe(this, new Observer<PagedList<Owo_product>>() {
            @Override
            public void onChanged(@Nullable PagedList<Owo_product> items) {
                adapter.submitList(items);
                showOnRecyclerView();
            }
        });
    }

    private void showOnRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 6);//Configuring recyclerview to receive two layout manager
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if(position == 0)
                {
                    return 6;
                }
                else
                    return 3;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        Product_tag product_tag = new Product_tag(this);
        ConcatAdapter concatAdapter = new ConcatAdapter(product_tag, adapter);
        recyclerView.setAdapter(concatAdapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}