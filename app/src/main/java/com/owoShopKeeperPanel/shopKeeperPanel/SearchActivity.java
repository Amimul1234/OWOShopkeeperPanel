package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.owoShopKeeperPanel.homeComponents.HomeActivity;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.Product_tag;
import com.owoShopKeeperPanel.adapters.SearchedAdapter;
import com.owoShopKeeperPanel.pagination.search.ItemViewModelSearch;
import com.owoShopKeeperPanel.pagination.searchDesc.ItemViewModelSearchDesc;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity {

    private SearchView search_product;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchedAdapter adapter;

    private int search_state = 0;

    private ItemViewModelSearch itemViewModelSearch;
    private ItemViewModelSearchDesc itemViewModelSearchDesc;

    private final boolean[] checkedItems = new boolean[Prevalent.category_to_display.size()];
    String[] categories = new String[Prevalent.category_to_display.size()];
    List<String> filtered_categories = new ArrayList<>();

    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_product = findViewById(R.id.search_product);
        search_product.setIconifiedByDefault(false);
        search_product.requestFocus();

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
        recyclerView = findViewById(R.id.searched_items);
        Button filter_product = findViewById(R.id.filter_product);
        Button sort_product = findViewById(R.id.sort_product);

        adapter = new SearchedAdapter(this);

        getItem(query, categories);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if(search_state == 0)
                itemViewModelSearch.clear();
            else
                itemViewModelSearchDesc.clear();

            adapter.notifyDataSetChanged();
        });

        filter_product.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
            builder.setTitle("Choose Categories");
            builder.setIcon(R.drawable.filter);

            builder.setMultiChoiceItems(categories, checkedItems, (dialog, which, isChecked) -> {

                String value = categories[which];

                if(!isChecked)
                {
                    filtered_categories.remove(value);
                }
                else
                {
                    filtered_categories.add(value);// Here we need to add value not while initializing
                }
            });

            builder.setPositiveButton("OK", (dialog, which) -> {

                int p = filtered_categories.size();

                String[] cate = new String[p];

                for(int j=0; j<p; j++)
                    cate[j] = filtered_categories.get(j);

                getItem(query, cate);
            });

            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        sort_product.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
            builder.setTitle("Sort Product");
            builder.setIcon(R.drawable.sort);

            String[] categories = {"Price Low to High", "Price High to Low"};

            builder.setSingleChoiceItems(categories, search_state, (dialog, which) -> search_state = which);

            builder.setPositiveButton("OK", (dialog, which) -> {

                if(filtered_categories.isEmpty())
                {
                    int p = Prevalent.category_to_display.size();

                    String[] searching_on = new String[p];

                    getItem(query, searching_on);
                }

                else
                {
                    int p = filtered_categories.size();

                    String[] searching_on = new String[p];

                    for(int i=0; i<p; i++)
                        searching_on[i] = filtered_categories.get(i);

                    getItem(query, searching_on);
                }
                dialog.dismiss();
            });

            builder.setNegativeButton("Cancel", null);

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        search_product.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String abcd) {
                if(filtered_categories.isEmpty())
                {
                    int p = Prevalent.category_to_display.size();

                    String[] searching_on = new String[p];

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
        if(search_state == 0)
        {

            itemViewModelSearch = new ItemViewModelSearch(category, query);//Refreshing the model for new filtration

            itemViewModelSearch.itemPagedList.observe(this, items -> {
                adapter.submitList(items);
                showOnRecyclerView();
            });

        }

        else
        {
            itemViewModelSearchDesc = new ItemViewModelSearchDesc(category, query);

            itemViewModelSearchDesc.itemPagedList.observe(this, items -> {
                adapter.submitList(items);
                showOnRecyclerView();
            });
        }
    }


    private void showOnRecyclerView() {

        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
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