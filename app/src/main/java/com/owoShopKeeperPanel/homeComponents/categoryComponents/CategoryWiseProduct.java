package com.owoShopKeeperPanel.homeComponents.categoryComponents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.ItemAdapterCategory;
import com.owoShopKeeperPanel.adapters.Product_tag;
import com.owoShopKeeperPanel.adapters.SubCategoryAdapter;
import com.owoShopKeeperPanel.adapters.SubCategoryTag;
import com.owoShopKeeperPanel.categorySpinner.entity.CategoryEntity;
import com.owoShopKeeperPanel.categorySpinner.entity.SubCategoryEntity;
import com.owoShopKeeperPanel.pagination.category.ItemViewModelCategory;
import com.owoShopKeeperPanel.shopKeeperPanel.SearchActivity;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryWiseProduct extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemAdapterCategory adapter;
    private final List<SubCategoryEntity> subCategoryEntityList = new ArrayList<>();
    private CategoryEntity categoryEntity;

    private SubCategoryAdapter subCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_product);

        ImageView back_to_home = findViewById(R.id.back_to_home);
        AppCompatButton search_product = findViewById(R.id.search_product);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        recyclerView = findViewById(R.id.recycler_view_for_products);
        adapter = new ItemAdapterCategory(this);

        categoryEntity = (CategoryEntity) getIntent().getSerializableExtra("category");

        subCategoryAdapter = new SubCategoryAdapter(this, subCategoryEntityList);

        back_to_home.setOnClickListener(v -> onBackPressed());

        search_product.setOnClickListener(v -> {
            Intent intent = new Intent(CategoryWiseProduct.this, SearchActivity.class);
            startActivity(intent);
            finish();
        });


        getSubCategories();
        getProducts();

        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getSubCategories();
            getProducts();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void getSubCategories() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Sub Categories");
        progressDialog.setMessage("Please wait while we are fetching sub categories data");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .getAllSubCategories(categoryEntity.getCategoryId())
                .enqueue(new Callback<List<SubCategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Response<List<SubCategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            subCategoryEntityList.clear();
                            assert response.body() != null;
                            subCategoryEntityList.addAll(response.body());
                            subCategoryAdapter.changeList(subCategoryEntityList);
                            showOnRecyclerView();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(CategoryWiseProduct.this, "Can not get sub-categories, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<SubCategoryEntity>> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("CategoryWise", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(CategoryWiseProduct.this, "Can not get sub-categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getProducts() {

        ItemViewModelCategory itemViewModelCategory = new ItemViewModelCategory(categoryEntity.getCategoryId());

        itemViewModelCategory.itemPagedList.observe(this, items -> {
            adapter.submitList(items);
            showOnRecyclerView();
        });

    }

    private void showOnRecyclerView()
    {
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);//Configuring recyclerview to receive two layout manager
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == 0)
                {
                    return 6;
                }
                else if(position >=0 && position <= subCategoryEntityList.size())
                {
                    return 2;
                }

                else if(position == (subCategoryEntityList.size()+1))
                    return 6;
                else
                    return 3;
            }
        });

        SubCategoryTag subCategoryTag = new SubCategoryTag(this);
        Product_tag product_tag = new Product_tag(this);

        ConcatAdapter concatAdapter = new ConcatAdapter(subCategoryTag, subCategoryAdapter, product_tag, adapter);

        recyclerView.setAdapter(concatAdapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        subCategoryAdapter.notifyDataSetChanged();
    }
}