package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoshopkeeperpanel.Model.PendingShop;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Network.RetrofitClient;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Response.OwoApiResponse;
import com.owoshopkeeperpanel.adapters.CategoryAdapter;
import com.owoshopkeeperpanel.adapters.ImageFlipperAdapter;
import com.owoshopkeeperpanel.adapters.ItemAdapter;
import com.owoshopkeeperpanel.adapters.Product_tag;
import com.owoshopkeeperpanel.adapters.SearchedAdapter;
import com.owoshopkeeperpanel.pagination.ItemViewModel;
import com.owoshopkeeperpanel.pagination.ItemViewModelSearch;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private SearchView search_product;
    private RecyclerView recyclerView;
    private AllianceLoader loader;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchedAdapter adapter;
    private int search_state = 0;

    private Button filter_product, sort_product;

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
        filter_product = findViewById(R.id.filter_product);
        sort_product = findViewById(R.id.sort_product);
        loader = findViewById(R.id.loader);

        adapter = new SearchedAdapter(this);

        int size = Prevalent.category_to_display.size();

        String[] categories = new String[size];

        for(int i=0; i<size; i++)
        {
            categories[i] = Prevalent.category_to_display.get(i);
        }

        getItem("", categories);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getItem("", categories);
            }
        });

        filter_product.setOnClickListener(new View.OnClickListener() { //Must work with filtering
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("Choose categories");
                builder.setIcon(R.drawable.filter);

                int size = Prevalent.category_to_display.size();
                String[] categories = new String[size];

                for(int i=0; i<size; i++)
                {
                    categories[i] = Prevalent.category_to_display.get(i);
                }

                boolean[] checkedItems = new boolean[size];

                for(int i=0; i<size; i++)
                    checkedItems[i] = true;

                String[] filtered_categories = new String[size];

                builder.setMultiChoiceItems(categories, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] abcd  = {Prevalent.category_to_display.get(2)};
                        finish();
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
        });

        search_product.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getItem(query, categories);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void getItem(String query, String[] categories) {
        ItemViewModelSearch itemViewModelSearch = ViewModelProviders.of(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T)new ItemViewModelSearch (categories, query);//Provide here required arguments
            }
        }).get(ItemViewModelSearch.class);


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