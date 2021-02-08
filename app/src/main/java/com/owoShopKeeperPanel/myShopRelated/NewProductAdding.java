package com.owoShopKeeperPanel.myShopRelated;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.owoShopKeeperPanel.Model.Owo_product;
import com.owoShopKeeperPanel.Model.add_product_model;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
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

        int product_id = Integer.parseInt(id);

        Call<Owo_product> call = RetrofitClient.getInstance().getApi().getProductById(product_id);

        call.enqueue(new Callback<Owo_product>() {
            @Override
            public void onResponse(@NotNull Call<Owo_product> call, @NotNull Response<Owo_product> response) {
                if (response.body() != null) {
                    if(response.isSuccessful())
                    {
                        Owo_product clicked_owoproduct = response.body();

                        add_product_model add_product_model = new add_product_model(clicked_owoproduct.getProduct_id(), clicked_owoproduct.getProduct_price(),
                                clicked_owoproduct.getProduct_image(), clicked_owoproduct.getProduct_name(), quantity, clicked_owoproduct.getProduct_description());

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
            public void onFailure(@NotNull Call<Owo_product> call, @NotNull Throwable t) {
                Toast.makeText(NewProductAdding.this, t.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }
}