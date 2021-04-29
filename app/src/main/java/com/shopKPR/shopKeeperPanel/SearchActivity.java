package com.shopKPR.shopKeeperPanel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.shopKPR.adapters.Product_tag;
import com.shopKPR.adapters.SearchedAdapter;
import com.shopKPR.homeComponents.HomeActivity;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.search.searchAlphabetically.ItemViewModelSearchAlphabetically;
import com.shopKPR.search.searchDescending.ItemViewModelSearchDesc;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.R;
import com.shopKPR.search.searchAscending.ItemViewModelSearch;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

      private RecyclerView recyclerView;
      private SwipeRefreshLayout swipeRefreshLayout;
      private SearchedAdapter adapter;

      private ProgressDialog progressDialog;

      private ItemViewModelSearch itemViewModelSearch;
      private ItemViewModelSearchDesc itemViewModelSearchDesc;
      private ItemViewModelSearchAlphabetically itemDataSourceForSearchAlphabetically;

      private int searchState = 0;
      private String search_alphabet = "A";
      private String searchQuery = "";

    private final List<String> subCategories = new ArrayList<>();
    private final List<String> filteredSubCategories = new ArrayList<>();
    private boolean[] checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchProduct = findViewById(R.id.search_product);
        searchProduct.setIconifiedByDefault(false);
        searchProduct.requestFocus();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        recyclerView = findViewById(R.id.searched_items);

        adapter = new SearchedAdapter(this);
        progressDialog = new ProgressDialog(this);

        Button filter_product = findViewById(R.id.filter_product);
        Button sort_product = findViewById(R.id.sort_product);

        getAllSubCategories();

        swipeRefreshLayout.setOnRefreshListener(() ->
        {
            if(searchState == 0)
                itemViewModelSearch.clear();
            else if(searchState == 1)
                itemViewModelSearchDesc.clear();

            adapter.notifyDataSetChanged();
        });

        searchProduct.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String abcd)
            {
                if(filteredSubCategories.isEmpty())
                {
                    searchQuery = abcd;
                    getItem(subCategories);
                }
                else
                {
                    searchQuery = abcd;
                    getItem(filteredSubCategories);
                }

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText)
            {
                if(filteredSubCategories.isEmpty())
                {
                    searchQuery = newText;
                    getItem(subCategories);
                }
                else {
                    searchQuery = newText;
                    getItem(filteredSubCategories);
                }

                return true;
            }
        });

        filter_product.setOnClickListener(v ->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
            builder.setTitle("Choose Sub-Categories");
            builder.setIcon(R.drawable.filter);

            int size = subCategories.size();

            builder.setMultiChoiceItems(subCategories.toArray(new String[size]), checkedItems, (dialog, which, isChecked) -> {

                String value = subCategories.get(which);

                if(!isChecked)
                {
                    filteredSubCategories.remove(value);
                }
                else
                {
                    filteredSubCategories.add(value);
                }
            });

            builder.setPositiveButton("OK", (dialog, which) -> getItem(filteredSubCategories));

            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        sort_product.setOnClickListener(v ->
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
            builder.setTitle("Sort Product");
            builder.setIcon(R.drawable.sort);

            String[] categories = {"Price Low to High", "Price High to Low", "Alphabetic Order"};

            builder.setSingleChoiceItems(categories, searchState, (dialog, which) ->
                    searchState = which);

            builder.setPositiveButton("OK", (dialog, which) ->
            {
                if(searchState == 0 || searchState == 1)
                {
                    if(filteredSubCategories.isEmpty())
                        getItem(subCategories);
                    else
                        getItem(filteredSubCategories);
                }
                else if(searchState == 2)
                {
                    AlertDialog.Builder alphabeticSortBuilder = new AlertDialog.Builder(SearchActivity.this);
                    alphabeticSortBuilder.setTitle("Sort Product in Alphabetic Order");
                    alphabeticSortBuilder.setIcon(R.drawable.sort);

                    String[] alphabets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

                    alphabeticSortBuilder.setSingleChoiceItems(alphabets, 0, (dialog1, which1) -> search_alphabet = alphabets[which1]);

                    alphabeticSortBuilder.setPositiveButton("OK", ((dialog1, which1) -> {
                        if(filteredSubCategories.isEmpty())
                            getItem(subCategories);
                        else
                            getItem(filteredSubCategories);
                    }));

                    alphabeticSortBuilder.show();
                }
                dialog.dismiss();
            });

            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        });



    }


    private void getAllSubCategories()
    {
        progressDialog.setTitle("Getting Subcategories");
        progressDialog.setMessage("Please wait while we are getting sub categories");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .getAllSubCategoriesForCategory(Prevalent.category_to_display.get(0))
                .enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<String>> call, @NotNull Response<List<String>> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            assert response.body() != null;
                            subCategories.addAll(response.body());

                            checkedItems = new boolean[subCategories.size()];

                            getItem(subCategories);
                        }
                        else
                        {
                            Toast.makeText(SearchActivity.this, "Can not get sub-category data, please try again",
                                    Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<String>> call, @NotNull Throwable t) {
                        Log.e("SearchActivity", t.getMessage());

                        Toast.makeText(SearchActivity.this, "Can not get sub-category data, please try again",
                                Toast.LENGTH_SHORT).show();

                        progressDialog.dismiss();
                    }
                });
    }



    private void getItem(List<String> subCategories)
    {
        if(searchState == 0)
        {
            itemViewModelSearch = new ItemViewModelSearch(subCategories, searchQuery);//Refreshing the model for new filtration

            itemViewModelSearch.itemPagedList.observe(this, items ->
            {
                adapter.submitList(items);
                showOnRecyclerView();
            });

        }
        else if(searchState == 1)
        {
            itemViewModelSearchDesc = new ItemViewModelSearchDesc(subCategories, searchQuery);

            itemViewModelSearchDesc.itemPagedList.observe(this, items ->
            {
                adapter.submitList(items);
                showOnRecyclerView();
            });
        }

        else {

            itemDataSourceForSearchAlphabetically = new ItemViewModelSearchAlphabetically(subCategories, search_alphabet);

            itemDataSourceForSearchAlphabetically.itemPagedList.observe(this, items ->
            {
                adapter.submitList(items);
                showOnRecyclerView();
            });
        }
    }


    private void showOnRecyclerView() {

        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
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

        recyclerView.setLayoutManager(gridLayoutManager);
        Product_tag product_tag = new Product_tag(getApplicationContext());
        ConcatAdapter concatAdapter = new ConcatAdapter(product_tag, adapter);
        recyclerView.setAdapter(concatAdapter);
        concatAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}