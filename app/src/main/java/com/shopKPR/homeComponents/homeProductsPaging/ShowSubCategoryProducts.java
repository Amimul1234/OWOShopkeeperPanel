package com.shopKPR.homeComponents.homeProductsPaging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.shopKPR.R;
import com.shopKPR.adapters.Product_tag;
import com.shopKPR.adapters.SubCategoryAdapter;
import com.shopKPR.network.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowSubCategoryProducts extends AppCompatActivity {

    private String subCategoryName;
    private SubCategoryAdapter subCategoryAdapter;
    private SubCategoryProductsViewModel subCategoryProductsViewModel;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView productsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_sub_category_products);

        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        productsRecyclerView = findViewById(R.id.products_recycler_view);
        TextView subCategoryNameText = findViewById(R.id.sub_category_name);


        subCategoryName = getIntent().getStringExtra("subCategoryName");
        subCategoryNameText.setText(subCategoryName);

        getSubCategoryId(subCategoryName);

        subCategoryAdapter = new SubCategoryAdapter(this, progressBar);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        swipeRefreshLayout.setOnRefreshListener(()-> getSubCategoryId(subCategoryName));
    }

    private void getSubCategoryId(String subCategoryName)
    {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .getSubcategoryId(subCategoryName)
                .enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(@NotNull Call<Long> call, @NotNull Response<Long> response) {
                        if(response.isSuccessful())
                        {
                            subCategoryProductsViewModel = new SubCategoryProductsViewModel(response.body());

                            subCategoryProductsViewModel.itemPagedList.observe(ShowSubCategoryProducts.this, items ->
                            {
                                subCategoryAdapter.submitList(items);
                                showOnRecyclerView();
                            });
                        }
                        else
                        {
                            progressBar.setVisibility(View.GONE);

                            Toast.makeText(ShowSubCategoryProducts.this, "Can not get products, please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Long> call, @NotNull Throwable t)
                    {
                        progressBar.setVisibility(View.GONE);
                        Log.e("SubCateProducts", t.getMessage());
                        Toast.makeText(ShowSubCategoryProducts.this, "Can not get products, please try again",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showOnRecyclerView()
    {
        productsRecyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        productsRecyclerView.setLayoutManager(layoutManager);
        Product_tag product_tag = new Product_tag(getApplicationContext());

        ConcatAdapter concatAdapter = new ConcatAdapter(product_tag, subCategoryAdapter);

        productsRecyclerView.setAdapter(concatAdapter);
        subCategoryAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}