package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Network.RetrofitClient;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Response.OwoApiResponse;
import com.owoshopkeeperpanel.adapters.SearchedAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private SearchView search_product;
    private RecyclerView recyclerView;
    private List<Products> productsList;
    private SearchedAdapter adapter;
    private AllianceLoader loader;
    private String category;
    private int search_state = 0;

    private Button filter_product, sort_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_product = findViewById(R.id.search_product);
        search_product.setIconifiedByDefault(false);
        search_product.requestFocus();

        recyclerView = findViewById(R.id.searched_items);
        filter_product = findViewById(R.id.filter_product);
        sort_product = findViewById(R.id.sort_product);
        loader = findViewById(R.id.loader);


        filter_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("Choose a category");
                builder.setIcon(R.drawable.filter);

                int size = Prevalent.category_to_display.size();

                String[] categories = new String[size];

                for(int i=0; i<size; i++)
                    categories[i] = Prevalent.category_to_display.get(i);


                builder.setSingleChoiceItems(categories, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        category = categories[which];
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), CategoryWiseProduct.class);
                        intent.putExtra("category", category);
                        startActivity(intent);
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
                        getItem(query);
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
                getItem(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void getItem(String query) {

        recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));

        loader.setVisibility(View.VISIBLE);

        Call<OwoApiResponse> call;

        if(search_state == 0)
        {
            call = RetrofitClient.getInstance().getApi().searchProduct(query);
        }
        else
        {
            call = RetrofitClient.getInstance().getApi().searchProductDesc(query);
        }

        call.enqueue(new Callback<OwoApiResponse>() {
            @Override
            public void onResponse(Call<OwoApiResponse> call, Response<OwoApiResponse> response) {
                if(!response.body().error)
                {
                    loader.setVisibility(View.GONE);
                    productsList = response.body().products;
                    adapter = new SearchedAdapter(SearchActivity.this, productsList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<OwoApiResponse> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                loader.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}