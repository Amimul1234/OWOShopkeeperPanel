package com.shopKPR.wishList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.R;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishList extends AppCompatActivity {

    private ImageView empty_image;
    private TextView empty_text;
    private RecyclerView wishList;
    private WishListAdapter wishListAdapter;

    private final List<OwoProduct> owoProductList = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        ImageView backArrow = findViewById(R.id.back_arrow_from_wish_list);
        empty_image = findViewById(R.id.empty_image);
        empty_text = findViewById(R.id.empty_text);
        wishList = findViewById(R.id.wishList);
        progressDialog = new ProgressDialog(this);

        wishListAdapter = new WishListAdapter(this, owoProductList);

        getWishListProducts();

        backArrow.setOnClickListener(v -> onBackPressed());
    }

    private void getWishListProducts()
    {
        progressDialog.setTitle("Wish List Products");
        progressDialog.setMessage("Please wait while we are getting wish list products");
        progressDialog.setCancelable(false);
        progressDialog.show();


        RetrofitClient.getInstance().getApi()
                .wishListProducts(Prevalent.currentOnlineUser.getShopKeeperId())
                .enqueue(new Callback<List<OwoProduct>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<OwoProduct>> call, @NotNull Response<List<OwoProduct>> response) {
                        if(response.isSuccessful())
                        {
                            assert response.body() != null;
                            if(response.body().size() == 0)
                            {
                                progressDialog.dismiss();
                                empty_image.setVisibility(View.VISIBLE);
                                empty_text.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                owoProductList.clear();
                                owoProductList.addAll(response.body());

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WishList.this);
                                wishList.setLayoutManager(layoutManager);
                                wishList.setAdapter(wishListAdapter);
                                wishList.setHasFixedSize(true);

                                wishListAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<OwoProduct>> call, @NotNull Throwable t)
                    {
                        progressDialog.dismiss();
                        Log.e("WishList", t.getMessage());
                        Toast.makeText(WishList.this, "Error while getting wish list products", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}