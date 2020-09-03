package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.owoshopkeeperpanel.Model.Products;
import com.owoshopkeeperpanel.Network.RetrofitClient;
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

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(SearchActivity.this);
                builderSingle.setIcon(R.drawable.ic_baseline_category_24);
                builderSingle.setTitle("Categories:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.select_dialog_singlechoice);

                arrayAdapter.addAll(getResources().getStringArray(R.array.productCategory));

                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);

                    }
                });
                builderSingle.show();
            }
        });

        sort_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(SearchActivity.this);
                builderSingle.setIcon(R.drawable.sort);
                builderSingle.setTitle("Sort By:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.select_dialog_singlechoice);

                arrayAdapter.add("Price Low to High");
                arrayAdapter.add("Pri");

                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);

                    }
                });
                builderSingle.show();
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

        Call<OwoApiResponse> call = RetrofitClient.getInstance().getApi().searchProduct(query);

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



}