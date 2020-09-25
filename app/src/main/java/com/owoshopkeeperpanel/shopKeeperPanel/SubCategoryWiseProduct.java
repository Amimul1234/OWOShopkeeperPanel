package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Model.Sub_categories;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.ItemAdapterCategory;
import com.owoshopkeeperpanel.adapters.ItemAdapterSubCategory;
import com.owoshopkeeperpanel.adapters.Product_tag;
import com.owoshopkeeperpanel.adapters.SubCategoryAdapter;
import com.owoshopkeeperpanel.adapters.SubCategoryTag;
import com.owoshopkeeperpanel.pagination.ItemViewModelCategory;
import com.owoshopkeeperpanel.pagination.ItemViewModelSubCategory;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryWiseProduct extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView back_to_home;
    private AppCompatButton search_product;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemAdapterSubCategory adapter;
    static public String category = null;
    int sub_category_size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_product);

        back_to_home = findViewById(R.id.back_to_home);
        search_product = findViewById(R.id.search_product);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        recyclerView = findViewById(R.id.recycler_view_for_products);
        adapter = new ItemAdapterSubCategory(this);

        category = getIntent().getStringExtra("sub_category");

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategoryWiseProduct.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getProducts();

        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProducts();
            }
        });
    }

    public void getProducts() {
        ItemViewModelSubCategory itemViewModelSubCategory = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T)new ItemViewModelSubCategory(category);
            }
        }).get(ItemViewModelSubCategory.class);

        itemViewModelSubCategory.itemPagedList.observe(this, new Observer<PagedList<Products>>() {
            @Override
            public void onChanged(@Nullable PagedList<Products> items) {
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