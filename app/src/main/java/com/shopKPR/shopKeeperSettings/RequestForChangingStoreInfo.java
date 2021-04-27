package com.shopKPR.shopKeeperSettings;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPR.Model.Shops;
import com.shopKPR.R;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.prevalent.Prevalent;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestForChangingStoreInfo extends AppCompatActivity {

    private ImageView shopImage, shopOwnerNIDFront, shopTradeLicense;
    private EditText shopName, shopAddress, shopOwnerName, shopServiceMobile;
    private Button validateAddress, updateShopInformation;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_changing_store_info);

        progressDialog = new ProgressDialog(this);

        ImageView backButton = findViewById(R.id.back_button);
        shopImage = findViewById(R.id.shopImage);
        shopOwnerNIDFront = findViewById(R.id.shopOwnerNid);
        shopTradeLicense = findViewById(R.id.shopTradeLicense);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        shopOwnerName = findViewById(R.id.shopOwnerName);
        shopServiceMobile = findViewById(R.id.shopServiceMobile);

        validateAddress = findViewById(R.id.shopLocationButton);
        updateShopInformation = findViewById(R.id.updateShopInfo);

        getShopInfo();

        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void getShopInfo()
    {
        progressDialog.setTitle("Shop Information");
        progressDialog.setMessage("Please wait while we are getting shop information");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .getShopInfo(Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<Shops>() {
                    @Override
                    public void onResponse(@NotNull Call<Shops> call, @NotNull Response<Shops> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();

                            Shops shops = response.body();

                            assert shops != null;
                            Glide.with(RequestForChangingStoreInfo.this).load(HostAddress.HOST_ADDRESS.getHostAddress() + shops.getShop_image_uri()).
                                    diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(shopImage);

                            Glide.with(RequestForChangingStoreInfo.this).load(HostAddress.HOST_ADDRESS.getHostAddress() + shops.getShop_keeper_nid_front_uri()).
                                    diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(shopOwnerNIDFront);

                            if(shops.getTrade_license_url() != null)
                            {
                                Glide.with(RequestForChangingStoreInfo.this).load(HostAddress.HOST_ADDRESS.getHostAddress() + shops.getTrade_license_url()).
                                        diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(shopTradeLicense);
                            }


                            shopName.setText(shops.getShop_name());
                            shopAddress.setText(shops.getShop_address());
                            shopOwnerName.setText(shops.getShop_owner_name());
                            shopServiceMobile.setText(shops.getShop_service_mobile());

                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(RequestForChangingStoreInfo.this, "Can not get shop information, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Shops> call, @NotNull Throwable t)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(RequestForChangingStoreInfo.this, "Failed to get shop information, please try again", Toast.LENGTH_SHORT).show();
                        Log.e("RequestForInfoChange", t.getMessage());
                    }
                });
    }
}