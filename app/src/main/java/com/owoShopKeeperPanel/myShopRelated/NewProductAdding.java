package com.owoShopKeeperPanel.myShopRelated;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.owoShopKeeperPanel.Model.OwoProduct;
import com.owoShopKeeperPanel.Model.add_product_model;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.R;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProductAdding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_adding);

        String id = getIntent().getStringExtra("id");
        String quantity = getIntent().getStringExtra("quantity");

        Long product_id = Long.parseLong(id);

        Call<OwoProduct> call = RetrofitClient.getInstance().getApi().getProductById(product_id);

        call.enqueue(new Callback<OwoProduct>() {
            @Override
            public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                if (response.body() != null) {
                    if(response.isSuccessful())
                    {
                        OwoProduct clicked_owoproduct = response.body();

                        add_product_model add_product_model = new add_product_model(clicked_owoproduct.getProductId(), clicked_owoproduct.getProductPrice(),
                                clicked_owoproduct.getProductImage(), clicked_owoproduct.getProductName(), quantity, clicked_owoproduct.getProductDescription());

                        Intent intent = new Intent(NewProductAdding.this, AddProductToShop.class);
                        intent.putExtra("add_product_model", add_product_model);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(NewProductAdding.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(NewProductAdding.this, "Server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                Toast.makeText(NewProductAdding.this, t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}