package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.Product_tag;
import com.owoshopkeeperpanel.adapters.SearchedAdapter;
import com.owoshopkeeperpanel.pagination.ItemViewModelSearch;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    private SearchView search_product;
    private RecyclerView recyclerView;
    private AllianceLoader loader;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchedAdapter adapter;

    private int search_state = 0;

    private ItemViewModelSearch itemViewModelSearch;

    private Button filter_product, sort_product;

    private boolean[] checkedItems = new boolean[Prevalent.category_to_display.size()];
    String[] categories = new String[Prevalent.category_to_display.size()];
    List<String> filtered_categories = new ArrayList<>();

    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        int size = Prevalent.category_to_display.size();


        for(int i=0; i<size; i++)
            categories[i] = Prevalent.category_to_display.get(i); //getting the categories in which we will search

        search_product = findViewById(R.id.search_product);
        search_product.setIconifiedByDefault(false);
        search_product.requestFocus();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        recyclerView = findViewById(R.id.searched_items);
        filter_product = findViewById(R.id.filter_product);
        sort_product = findViewById(R.id.sort_product);
        loader = findViewById(R.id.loader);

        adapter = new SearchedAdapter(this);

        getItem(query, categories);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                itemViewModelSearch.clear();
                adapter.notifyDataSetChanged();
            }
        });

        filter_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("Choose categories");
                builder.setIcon(R.drawable.filter);

                builder.setMultiChoiceItems(categories, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        String value = categories[which];

                        if(!isChecked)
                        {
                            filtered_categories.remove(value);
                        }
                        else
                        {
                            filtered_categories.add(value);// Here we need to add value not while initializing
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int p = filtered_categories.size();

                        String[] cate = new String[p];

                        for(int j=0; j<p; j++)
                            cate[j] = filtered_categories.get(j);

                        getItem(query, cate);
                    }
                });

                builder.setNegativeButton("Cancel", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        sort_product.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("Sort Product");
                builder.setIcon(R.drawable.sort);

                String[] categories = {"Price Low to High", "Price High to Low"};

                builder.setSingleChoiceItems(categories, search_state, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        search_state = which;
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String query = search_product.getQuery().toString();
                        getItem(query, categories);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }); //should call different api

        search_product.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String abcd) {
                if(filtered_categories.isEmpty())
                {
                    int p = Prevalent.category_to_display.size();

                    String[] searching_on = new String[p];

                    for(int i=0; i<p; i++)
                        searching_on[i] = Prevalent.category_to_display.get(i);

                    query = abcd;

                    getItem(query, searching_on);
                }

                else
                {
                    int p = filtered_categories.size();

                    String[] searching_on = new String[p];

                    for(int i=0; i<p; i++)
                        searching_on[i] = filtered_categories.get(i);

                    query = abcd;

                    getItem(query, searching_on);
                }

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (search_product.getQuery().length() == 0) {

                    if(filtered_categories.isEmpty())
                    {
                        int p = Prevalent.category_to_display.size();

                        String[] searching_on = new String[p];

                        for(int i=0; i<p; i++)
                            searching_on[i] = Prevalent.category_to_display.get(i);

                        query = "";

                        getItem(query, searching_on);
                    }

                    else
                    {
                        int p = filtered_categories.size();

                        String[] searching_on = new String[p];

                        for(int i=0; i<p; i++)
                            searching_on[i] = filtered_categories.get(i);

                        query = "";

                        getItem(query, searching_on);
                    }

                }
                return false;
            }
        });
    }

    private void getItem(String query, String[] category) {

        itemViewModelSearch = new ItemViewModelSearch(category, query);//Refreshing the model for new filtration

        itemViewModelSearch.itemPagedList.observe(this, new Observer<PagedList<Products>>() {
            @Override
            public void onChanged(@Nullable PagedList<Products> items) {
                adapter.submitList(items);
                showOnRecyclerView();
            }
        });
    }


    private void showOnRecyclerView() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
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