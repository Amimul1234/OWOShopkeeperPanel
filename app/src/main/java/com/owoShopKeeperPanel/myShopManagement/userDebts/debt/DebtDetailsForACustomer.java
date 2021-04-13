package com.owoShopKeeperPanel.myShopManagement.userDebts.debt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.Model.UserDebts;
import com.owoShopKeeperPanel.Model.User_debt_details;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.UserDebtRecordForASingleUserAdapter;
import com.owoShopKeeperPanel.configurations.HostAddress;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DebtDetailsForACustomer extends AppCompatActivity {

    private ImageView letter_image_view;
    private AllianceLoader loader;
    private RecyclerView debt_details_recyclerview;
    private TextView debt_taker_name, debt_taker_mobile_number, debt_taker_total_amount;
    private final int STORAGE_PERMISSION_CODE = 1;
    private long downloadId;
    private Long user_id;
    private UserDebts userDebts;

    private UserDebtRecordForASingleUserAdapter userDebtRecordForASingleUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_details_for_a_customer);

        ImageView back_to_home = findViewById(R.id.back_to_home);
        letter_image_view = findViewById(R.id.letter_image_view);
        Button clearAllRecordForACustomer = findViewById(R.id.clear_all_record_for_a_customer);
        Button downloadInvoiceForAUser = findViewById(R.id.download_invoice_for_a_user);
        FloatingActionButton addANewDebtDetails = findViewById(R.id.add_a_new_debt_details);
        loader = findViewById(R.id.loader);
        debt_details_recyclerview = findViewById(R.id.debt_details_recyclerview);
        debt_taker_name = findViewById(R.id.debt_taker_name);
        debt_taker_mobile_number = findViewById(R.id.debt_taker_mobile_number);
        debt_taker_total_amount = findViewById(R.id.debt_taker_total_amount);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        addANewDebtDetails = findViewById(R.id.add_a_new_debt_details);

        user_id = getIntent().getLongExtra("user_id", -1);

        loadData(user_id);

        back_to_home.setOnClickListener(v -> {
            Intent intent = new Intent(DebtDetailsForACustomer.this, DebtDetailsDashBoard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        clearAllRecordForACustomer.setOnClickListener(v -> {
            if(userDebts!=null)
            {
                RetrofitClient.getInstance().getApi()
                        .deleteAUserDebtList(userDebts.getUserId())
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                if(response.code() == 200)
                                {
                                    Toast.makeText(DebtDetailsForACustomer.this, "Debt report deleted successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DebtDetailsForACustomer .this, DebtDetailsDashBoard.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                else if(response.code() == 424)
                                {
                                    Toast.makeText(DebtDetailsForACustomer.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                                }
                                else if(response.code() == 404){
                                    Toast.makeText(DebtDetailsForACustomer.this, "User not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                Log.e("Error", t.getMessage());
                            }
                        });
            }
            else
            {
                Toast.makeText(DebtDetailsForACustomer.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });

        downloadInvoiceForAUser.setOnClickListener(v -> requestStoragePermission());

        addANewDebtDetails.setOnClickListener(v -> {
            Intent intent = new Intent(DebtDetailsForACustomer.this, AddAUserDebtDetails.class);
            intent.putExtra("user_id", user_id);
            startActivity(intent);
        });
    }

    public void loadData(Long user_id) {

        loader.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .getUserDebtDetails(user_id)
                .enqueue(new Callback<UserDebts>() {
                    @Override
                    public void onResponse(@NotNull Call<UserDebts> call, @NotNull Response<UserDebts> response) {

                        if(response.code() == 200)
                        {
                            if (response.body() != null) {

                                userDebts = response.body();

                                ColorGenerator generator = ColorGenerator.MATERIAL;
                                int color1 = generator.getRandomColor();

                                char c;

                                c = userDebts.getUserName().charAt(0);

                                debt_taker_mobile_number.setText(userDebts.getUserMobileNumber());
                                debt_taker_name.setText(userDebts.getUserName());
                                debt_taker_total_amount.setText("৳ "+String.valueOf(userDebts.getUserTotalDebt()));

                                TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(c), color1);

                                letter_image_view.setImageDrawable(drawable);

                                loader.setVisibility(View.INVISIBLE);

                                RetrofitClient.getInstance().getApi()
                                        .getDebtListForAnSpecificUser(user_id)
                                        .enqueue(new Callback<List<User_debt_details>>() {
                                            @Override
                                            public void onResponse(@NotNull Call<List<User_debt_details>> call, @NotNull Response<List<User_debt_details>> response) {
                                                if(response.code() == 200)
                                                {
                                                    userDebts.setUserDebtDetails(response.body());
                                                    userDebtRecordForASingleUserAdapter = new UserDebtRecordForASingleUserAdapter(DebtDetailsForACustomer.this, userDebts.getUserDebtDetails(), user_id);
                                                    debt_details_recyclerview.setLayoutManager(new LinearLayoutManager(DebtDetailsForACustomer.this));
                                                    debt_details_recyclerview.setAdapter(userDebtRecordForASingleUserAdapter);
                                                }
                                                else if(response.code() == 204)
                                                {
                                                    Toast.makeText(DebtDetailsForACustomer.this, "No record for this user", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    Toast.makeText(DebtDetailsForACustomer.this, "User not found", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<List<User_debt_details>> call, @NotNull Throwable t) {

                                            }
                                        });
                            }
                        }
                        else
                        {
                            Toast.makeText(DebtDetailsForACustomer.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                            loader.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<UserDebts> call, @NotNull Throwable t) {
                        Toast.makeText(DebtDetailsForACustomer.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.INVISIBLE);
                    }
                });
    }

    public void beginDownload()
    {
        String name = null;
        long id;
        String query_string = null;

        if(userDebts!=null)
        {
            name = userDebts.getUserName();
            id = userDebts.getUserId();
            query_string = HostAddress.HOST_ADDRESS.getHostAddress() +  "/getAllDebtDetailsReport?user_id="+ id;
        }

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(query_string))
                .setTitle(name+"'s Invoice")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setDescription("Downloading debt record....")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, ""+System.currentTimeMillis()+".pdf");
        request.allowScanningByMediaScanner();

        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);
    }


    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if(downloadId == id)
            {
                Toast.makeText(DebtDetailsForACustomer.this, "Download completed", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of downloading the invoice pdf")
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(DebtDetailsForACustomer.this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE))
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                new Handler().postDelayed(this::beginDownload, 1000);
            }
            else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}