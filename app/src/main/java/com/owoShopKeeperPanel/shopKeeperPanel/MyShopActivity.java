package com.owoShopKeeperPanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.Model.Shops;
import com.owoShopKeeperPanel.configurations.HostAddress;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.MyShopManagementAdapter;
import com.owoShopKeeperPanel.adapters.Saleable_products;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyShopActivity extends AppCompatActivity {

    private TextView shopName, shop_address, shop_service_mobile;
    private ImageView shop_image;

    private Shops shops = new Shops();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);

        RecyclerView shop_controlling_options = findViewById(R.id.shop_control_options);
        shopName = findViewById(R.id.my_shop_name);
        shop_address = findViewById(R.id.my_shop_address);
        shop_service_mobile = findViewById(R.id.my_shop_phone_number);
        shop_image = findViewById(R.id.my_shop_image);

        fetchFromDatabase();

        MyShopManagementAdapter myShopManagementAdapter = new MyShopManagementAdapter(MyShopActivity.this);

        shop_controlling_options.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 6);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
        {
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
        ImageView back_button = findViewById(R.id.back_to_home);

        back_button.setOnClickListener(v -> onBackPressed());
    }


    private void fetchFromDatabase() {

        RetrofitClient.getInstance().getApi().getShopInfo(Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<Shops>() {
                    @Override
                    public void onResponse(@NotNull Call<Shops> call, @NotNull Response<Shops> response) {
                        if (response.isSuccessful()) {
                            shops = response.body();
                            assert shops != null;
                            shop_address.setText(shops.getShop_address());
                            shop_service_mobile.setText(shops.getShop_service_mobile());
                            shopName.setText(shops.getShop_name());

                            Glide.with(MyShopActivity.this).load(HostAddress.HOST_ADDRESS.getHostAddress() +
                                    shops.getShop_image_uri()).diskCacheStrategy(DiskCacheStrategy.ALL).into(shop_image);
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
