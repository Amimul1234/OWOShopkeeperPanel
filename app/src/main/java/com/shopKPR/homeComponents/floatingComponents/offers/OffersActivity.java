package com.shopKPR.homeComponents.floatingComponents.offers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import com.shopKPR.R;
import com.shopKPR.adapters.Product_tag;
import com.shopKPR.pagination.category.ItemViewModelCategory;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.shopKeeperPanel.SearchActivity;

public class OffersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OfferAdapter offerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);


        ImageView back_to_home = findViewById(R.id.back_to_home);
        AppCompatButton search_product = findViewById(R.id.search_product);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        recyclerView = findViewById(R.id.recycler_view_for_products);
        offerAdapter = new OfferAdapter(this);

        back_to_home.setOnClickListener(v -> onBackPressed());

        search_product.setOnClickListener(v ->
        {
            Intent intent = new Intent(OffersActivity.this, SearchActivity.class);
            startActivity(intent);
            finish();
        });

        getProducts();

        swipeRefreshLayout.setColorSchemeResources(R.color.blue);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getProducts();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public void getProducts() {

        ItemViewModelCategory itemViewModelCategory = new ItemViewModelCategory(Prevalent.category_to_display.get(0));

        itemViewModelCategory.itemPagedList.observe(this, items -> {
            offerAdapter.submitList(items);
            showOnRecyclerView();
        });
    }

    private void showOnRecyclerView()
    {
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        Product_tag product_tag = new Product_tag(this);
        ConcatAdapter concatAdapter = new ConcatAdapter(product_tag, offerAdapter);

        recyclerView.setAdapter(concatAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        concatAdapter.notifyDataSetChanged();
    }


}