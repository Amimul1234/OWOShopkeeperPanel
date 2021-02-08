package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.Model.Shops;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.MyShopManagementAdapter;
import com.owoShopKeeperPanel.adapters.Saleable_products;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyShopActivity extends AppCompatActivity {

    private RecyclerView shop_controlling_options;
    private TextView shopName, shop_address, shop_service_mobile;
    private ImageView back_button, shop_image;

    private Shops shops = new Shops();

    private MyShopManagementAdapter myShopManagementAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);

        shop_controlling_options = findViewById(R.id.shop_control_options);
        shopName = findViewById(R.id.my_shop_name);
        shop_address = findViewById(R.id.my_shop_address);
        shop_service_mobile = findViewById(R.id.my_shop_phone_number);
        shop_image = findViewById(R.id.my_shop_image);

        fetchFromDatabase();

        myShopManagementAdapter = new MyShopManagementAdapter(MyShopActivity.this);

        shop_controlling_options.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 6);//Configuring recyclerview to receive two layout manager
        ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == 0)
                {
                    return 6;
                }
                else
                    return 2;
            }
        });

        shop_controlling_options.setLayoutManager(layoutManager);
        ConcatAdapter concatAdapter  = new ConcatAdapter(new Saleable_products(this), myShopManagementAdapter);
        shop_controlling_options.setAdapter(concatAdapter);
        back_button = findViewById(R.id.back_to_home);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void fetchFromDatabase() {

        RetrofitClient.getInstance().getApi().getShopInfo(Prevalent.currentOnlineUser.getPhone())
                .enqueue(new Callback<Shops>() {
                    @Override
                    public void onResponse(@NotNull Call<Shops> call, @NotNull Response<Shops> response) {
                        if (response.isSuccessful()) {
                            shops = response.body();
                            shop_address.setText(shops.getShop_address());
                            shop_service_mobile.setText(shops.getShop_service_mobile());
                            shopName.setText(shops.getShop_name());
                            Glide.with(MyShopActivity.this).load(shops.getShop_image_uri()).into(shop_image);
                        } else {
                            Toast.makeText(MyShopActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Shops> call, @NotNull Throwable t) {
                        Toast.makeText(MyShopActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
