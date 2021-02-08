package com.owoShopKeeperPanel.shopRegistration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.registerRequest.ShopPendingRequest;

import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopRegistrationRequest extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private final int STORAGE_PERMISSION_CODE = 1;
    private final String TAG = "Shop Reg. Activity";
    private Double latitude, longitude;
    private String shopImageUri, shopKeeperNidUri, shopTradeLicenseUri;

    private ProgressBar shopAddressProgressbar, mainProgressBar;
    private ImageView shopImage, shopOwnerNid,shopTradeLicense;
    private EditText shopName, shopAddress, shopOwnerName, shopServiceMobile;
    private CheckBox checkBox1, checkBox2, checkBox3;
    private Spinner requestedCategory1, requestedCategory2, requestedCategory3;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_shop_request);

        shopAddressProgressbar = findViewById(R.id.shopAddressProgressbar);
        mainProgressBar = findViewById(R.id.mainProgressBar);

        shopImage = findViewById(R.id.shopImage);
        shopOwnerNid = findViewById(R.id.shopOwnerNid);
        shopTradeLicense = findViewById(R.id.shopTradeLicense);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        shopOwnerName = findViewById(R.id.shopOwnerName);
        shopServiceMobile = findViewById(R.id.shopServiceMobile);
        checkBox1 = findViewById(R.id.category_1_check_box);
        checkBox2 = findViewById(R.id.category_2_check_box);
        checkBox3 = findViewById(R.id.category_3_check_box);
        requestedCategory1 = findViewById(R.id.category_1);
        requestedCategory2 = findViewById(R.id.category_2);
        requestedCategory3 = findViewById(R.id.category_3);

        Button shopLocationButton = findViewById(R.id.shopLocationButton);
        Button createShopButton = findViewById(R.id.createShopButton);

        shopImage.setOnClickListener(v -> requestStoragePermission(1));
        shopOwnerNid.setOnClickListener(v -> requestStoragePermission(2));
        shopTradeLicense.setOnClickListener(v -> requestStoragePermission(3));
        shopLocationButton.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(ShopRegistrationRequest.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);

            } else {
                getCurrentLocation();
            }
        });
        createShopButton.setOnClickListener(v -> validateInputs());
    }

    private void validateInputs()
    {
        if(shopImage.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(
                ShopRegistrationRequest.this, R.drawable.create_shop)).getConstantState())
        {
            Toast.makeText(this, "Please select shop image", Toast.LENGTH_SHORT).show();
        }
        else if(shopName.getText().toString().isEmpty())
        {
            shopName.setError("Shop name is mandatory");
            shopName.requestFocus();
        }
        else if(shopAddress.getText().toString().isEmpty())
        {
            shopAddress.setError("Shop address is mandatory");
            shopAddress.requestFocus();
        }
        else if(latitude == null || longitude == null)
        {
            Toast.makeText(this, "Please validate your address", Toast.LENGTH_SHORT).show();
        }

        else if(!checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked())
        {
            Toast.makeText(this, "Minimum 1 category selection is required", Toast.LENGTH_SHORT).show();
        }

        else if(shopOwnerName.getText().toString().isEmpty())
        {
            shopOwnerName.setError("Shop owner name is mandatory");
            shopOwnerName.requestFocus();
        }
        else if(shopServiceMobile.getText().toString().isEmpty())
        {
            shopServiceMobile.setError("Shop service mobile can not be empty");
            shopServiceMobile.requestFocus();
        }
        else if(shopOwnerNid.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(
                ShopRegistrationRequest.this, R.drawable.nid)).getConstantState())
        {
            Toast.makeText(this, "Must provide NID front picture", Toast.LENGTH_SHORT).show();
        }
        else
        {
            doDbOperations();
        }
    }

    private void doDbOperations()
    {

        mainProgressBar.setVisibility(View.VISIBLE);

        Bitmap bitmap = ((BitmapDrawable) shopImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(ShopRegistrationRequest.this.getCacheDir() + File.separator + filename + ".jpg");

        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(byteArrayOutputStream.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
            mainProgressBar.setVisibility(View.GONE);
            Toast.makeText(ShopRegistrationRequest.this, "Can not make request, please try again", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part multipartFile = MultipartBody.Part.createFormData("multipartFile",
                file.getName(), requestBody);

        RetrofitClient.getInstance().getApi()
                .uploadImageToServer("Shops Image", multipartFile)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                assert response.body() != null;
                                shopImageUri = response.body().string();

                                //Scope Variables for local operations
                                {
                                    Bitmap bitmap = ((BitmapDrawable) shopOwnerNid.getDrawable()).getBitmap();
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                                    String filename = UUID.randomUUID().toString();

                                    File file = new File(ShopRegistrationRequest.this.getCacheDir() + File.separator + filename + ".jpg");

                                    try {
                                        FileOutputStream fo = new FileOutputStream(file);
                                        fo.write(byteArrayOutputStream.toByteArray());
                                        fo.flush();
                                        fo.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(ShopRegistrationRequest.this, "Can not make request, please try again", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                                    MultipartBody.Part multipartFile = MultipartBody.Part.createFormData("multipartFile",
                                            file.getName(), requestBody);

                                    RetrofitClient.getInstance().getApi()
                                            .uploadImageToServer("Shops Image", multipartFile)
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                    if(response.isSuccessful())
                                                    {
                                                        try
                                                        {
                                                            assert response.body() != null;
                                                            shopKeeperNidUri = response.body().string();

                                                            if(shopTradeLicense.getDrawable().getConstantState() != Objects.requireNonNull(ContextCompat.getDrawable(
                                                                    ShopRegistrationRequest.this, R.drawable.trade_license)).getConstantState()) //If trade license is not null then upload trade license to the server
                                                            {
                                                                Bitmap bitmap = ((BitmapDrawable) shopTradeLicense.getDrawable()).getBitmap();
                                                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                                                                String filename = UUID.randomUUID().toString();

                                                                File file = new File(ShopRegistrationRequest.this.getCacheDir() + File.separator + filename + ".jpg");

                                                                try {
                                                                    FileOutputStream fo = new FileOutputStream(file);
                                                                    fo.write(byteArrayOutputStream.toByteArray());
                                                                    fo.flush();
                                                                    fo.close();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                    Toast.makeText(ShopRegistrationRequest.this, "Can not make request, please try again", Toast.LENGTH_SHORT).show();
                                                                    return;
                                                                }

                                                                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
                                                                MultipartBody.Part multipartFile = MultipartBody.Part.createFormData("multipartFile",
                                                                        file.getName(), requestBody);

                                                                RetrofitClient.getInstance().getApi()
                                                                        .uploadImageToServer("Shops Image", multipartFile)
                                                                        .enqueue(new Callback<ResponseBody>() {
                                                                            @Override
                                                                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                                                if(response.isSuccessful())
                                                                                {
                                                                                    try {
                                                                                        assert response.body() != null;
                                                                                        shopTradeLicenseUri = response.body().string();

                                                                                        uploadRequestToServer(); //Upload data to server

                                                                                    }catch (Exception e)
                                                                                    {
                                                                                        mainProgressBar.setVisibility(View.GONE);
                                                                                        Log.e(TAG, "Error is: "+e.getLocalizedMessage());
                                                                                        Toast.makeText(ShopRegistrationRequest.this, "Can not make request, please try again", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                                else
                                                                                {
                                                                                    Toast.makeText(ShopRegistrationRequest.this, "Can not make request, please try again", Toast.LENGTH_SHORT).show();
                                                                                    mainProgressBar.setVisibility(View.GONE);
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                                                Toast.makeText(ShopRegistrationRequest.this, "Can not request for shop creation, please try again", Toast.LENGTH_SHORT).show();
                                                                                Log.e(TAG, "Error occurred, Error is: "+t.getMessage());
                                                                                mainProgressBar.setVisibility(View.GONE);
                                                                            }
                                                                        });

                                                            }
                                                            else
                                                            {
                                                                uploadRequestToServer(); ////Upload data to server
                                                            }

                                                        }catch (Exception e)
                                                        {
                                                            mainProgressBar.setVisibility(View.GONE);
                                                            Log.e(TAG, "Error is: "+e.getLocalizedMessage());
                                                            Toast.makeText(ShopRegistrationRequest.this, "Can not make request, please try again", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(ShopRegistrationRequest.this, "Can not make request, please try again", Toast.LENGTH_SHORT).show();
                                                        mainProgressBar.setVisibility(View.GONE);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                    Toast.makeText(ShopRegistrationRequest.this, "Can not request for shop creation, please try again", Toast.LENGTH_SHORT).show();
                                                    Log.e(TAG, "Error occurred, Error is: "+t.getMessage());
                                                    mainProgressBar.setVisibility(View.GONE);
                                                }
                                            });
                                }
                            }catch (Exception e)
                            {
                                Log.e(TAG, "Exception, Exception is: "+e.getLocalizedMessage());
                                Toast.makeText(ShopRegistrationRequest.this, "Can not make request, please try again", Toast.LENGTH_SHORT).show();
                                mainProgressBar.setVisibility(View.GONE);
                            }
                        }
                        else
                        {
                            Toast.makeText(ShopRegistrationRequest.this, "Can not make request, please try again", Toast.LENGTH_SHORT).show();
                            mainProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Toast.makeText(ShopRegistrationRequest.this, "Can not request for shop creation, please try again", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error occurred, Error is: "+t.getMessage());
                        mainProgressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void uploadRequestToServer() {
        List<String> permissions = new ArrayList<>();

        if(checkBox1.isChecked())
        {
            permissions.add(requestedCategory1.getSelectedItem().toString());
        }
        if(checkBox2.isChecked())
        {
            permissions.add(requestedCategory2.getSelectedItem().toString());
        }
        if(checkBox3.isChecked())
        {
            permissions.add(requestedCategory3.getSelectedItem().toString());
        }

        ShopPendingRequest shopPendingRequest = new ShopPendingRequest(latitude, longitude, shopAddress.getText().toString(),
                shopImageUri, shopKeeperNidUri, shopName.getText().toString(), shopOwnerName.getText().toString(),
                Prevalent.currentOnlineUser.getPhone(), shopServiceMobile.getText().toString(), shopTradeLicenseUri);

        List<String> listWithOutDuplication = shopPendingRequest.duplicateProtection(permissions);

        shopPendingRequest.setCategoryPermissions(listWithOutDuplication);

        RetrofitClient.getInstance().getApi()
                .shopRegisterRequest(shopPendingRequest)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            Toast.makeText(ShopRegistrationRequest.this, "Successfully registered shop", Toast.LENGTH_SHORT).show();
                            mainProgressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(ShopRegistrationRequest.this, AfterShopRegisterRequest.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            finish();
                        }

                        else
                        {
                            Toast.makeText(ShopRegistrationRequest.this, "Failed to register shop, please try again", Toast.LENGTH_SHORT).show();
                            mainProgressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e(TAG, "Error occurred, Error is: "+ t.getMessage());
                        mainProgressBar.setVisibility(View.GONE);
                        Toast.makeText(ShopRegistrationRequest.this, "Failed to register shop, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestStoragePermission(int i)
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("Ok", (dialog, which) -> {

                        ActivityCompat.requestPermissions(ShopRegistrationRequest.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(ShopRegistrationRequest.this, i);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(ShopRegistrationRequest.this, i);
        }
    }

    private void selectImage(Context context, int i)
    {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {
            //Implementing logic for 3 imageViewer
            if(i == 1)
            {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 10);
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 11);
                }
            }
            else if(i == 2)
            {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 20);
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 21);
                }
            }
            else
            {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 30);
                }
                else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 31);
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode)
            {
                case 10:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        shopImage.setImageBitmap(selectedImage);
                    }
                    break;
                case 11:
                    if (resultCode == RESULT_OK && data != null)
                    {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                shopImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;

                case 20:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        shopOwnerNid.setImageBitmap(selectedImage);
                    }
                    break;
                case 21:
                    if (resultCode == RESULT_OK && data != null)
                    {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                shopOwnerNid.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;

                case 30:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        shopTradeLicense.setImageBitmap(selectedImage);
                    }
                    break;
                case 31:
                    if (resultCode == RESULT_OK && data != null)
                    {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                shopTradeLicense.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getCurrentLocation();
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {

        shopAddressProgressbar.setVisibility(View.VISIBLE);

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(ShopRegistrationRequest.this)
                        .removeLocationUpdates(this);

                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    shopAddressProgressbar.setVisibility(View.GONE);
                }

            }

        };

        LocationServices.getFusedLocationProviderClient(ShopRegistrationRequest.this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

}
