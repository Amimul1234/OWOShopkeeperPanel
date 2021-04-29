package com.shopKPR.shopKeeperPanel.productRelated;

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
import com.shopKPR.R;
import com.shopKPR.adapters.ItemAdapterSubCategory;
import com.shopKPR.adapters.Product_tag;
import com.shopKPR.categorySpinner.entity.SubCategoryEntity;
import com.shopKPR.homeComponents.brandsComponent.entity.Brands;
import com.shopKPR.homeComponents.tabbedComponents.subCategories.BrandsAdapter;
import com.shopKPR.homeComponents.tabbedComponents.subCategories.BrandsTag;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.pagination.subCategory.ItemViewModelSubCategory;
import com.shopKPR.shopKeeperPanel.SearchActivity;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryWiseProduct extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ItemAdapterSubCategory adapter;
    public SubCategoryEntity subCategoryEntity;
    private final List<Brands> brandsList = new ArrayList<>();
    private BrandsAdapter brandsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_wise_product);

        ImageView back_to_home = findViewById(R.id.back_to_home);
        AppCompatButton search_product = findViewById(R.id.search_product);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        recyclerView = findViewById(R.id.recycler_view_for_products);
        adapter = new ItemAdapterSubCategory(this);

        subCategoryEntity = (SubCategoryEntity) getIntent().getSerializableExtra("sub_category");

        brandsAdapter = new BrandsAdapter(this, brandsList);

        back_to_home.setOnClickListener(v -> onBackPressed());

        search_product.setOnClickListener(v ->
        {
            Intent intent = new Intent(SubCategoryWiseProduct.this, SearchActivity.class);
            startActivity(intent);
            finish();
        });

        getBrands();
        getProducts();

        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            getBrands();
            getProducts();
        });
    }

    private void getBrands()
    {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Brands");
        progressDialog.setMessage("Please wait while we are fetching brands");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .getAllBrandsViaSubCategory(subCategoryEntity.getSub_category_id())
                .enqueue(new Callback<List<Brands>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Brands>> call, @NotNull Response<List<Brands>> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            brandsList.clear();
                            assert response.body() != null;
                            brandsList.addAll(response.body());
                            brandsAdapter.notifyDataSetChanged();
                            showOnRecyclerView();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SubCategoryWiseProduct.this, "Can not get brands, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Brands>> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("SubCategoryWise", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(SubCategoryWiseProduct.this, "Can not get brands, please try again", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void getProducts() {

        ItemViewModelSubCategory itemViewModelSubCategory = new ItemViewModelSubCategory(subCategoryEntity.getSub_category_id());

        itemViewModelSubCategory.itemPagedList.observe(this, items -> {
            adapter.submitList(items);
            showOnRecyclerView();
        });
    }

    private void showOnRecyclerView() {
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);//Configuring recyclerview to receive two layout manager
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if(position == 0)
                {
                    return 6;
                }
                else if(position >0 && position <= brandsList.size())
                    return 2;
                else if(position == brandsList.size() + 1)
                    return 6;
                else
                    return  3;
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        Product_tag product_tag = new Product_tag(this);
        BrandsTag brandsTag = new BrandsTag(this);
        ConcatAdapter concatAdapter = new ConcatAdapter(brandsTag, brandsAdapter, product_tag, adapter);
        recyclerView.setAdapter(concatAdapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}