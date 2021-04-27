package com.shopKPR.shopKeeperSettings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.shopKPR.shopKeeperSettings.entitiy.ChangeShopInfo;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestForChangingStoreInfo extends AppCompatActivity 
{
    private ImageView shopImage, shopOwnerNIDFront, shopTradeLicense;
    private EditText shopName, shopAddress, shopOwnerName, shopServiceMobile;
    private ProgressDialog progressDialog;
    
    private String shopImageLink, shopKeeperNidFrontLink, shopTradeLicenseLink;

    private final int STORAGE_PERMISSION_CODE = 1;

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

        Button updateShopInformation = findViewById(R.id.updateShopInfo);

        getShopInfo();

        shopImage.setOnClickListener(v -> requestStoragePermission(1));
        shopOwnerNIDFront.setOnClickListener(v -> requestStoragePermission(2));
        shopTradeLicense.setOnClickListener(v -> requestStoragePermission(3));

        backButton.setOnClickListener(v -> onBackPressed());
        updateShopInformation.setOnClickListener(v -> checkFieldsValidity());
    }

    private void checkFieldsValidity() {
        if(shopName.getText().toString().isEmpty())
        {
            shopName.setError("Shop name can not be empty");
            shopName.requestFocus();
        }
        else if(shopAddress.getText().toString().isEmpty())
        {
            shopAddress.setError("Shop address can no be empty");
            shopAddress.requestFocus();
        }
        else if(shopOwnerName.getText().toString().isEmpty())
        {
            shopOwnerName.setError("Shop owner name can not be empty");
            shopOwnerName.requestFocus();
        }
        else if(shopServiceMobile.getText().toString().isEmpty())
        {
            shopServiceMobile.setError("Shop service mobile can not be empty");
            shopServiceMobile.requestFocus();
        }
        else
        {
            uploadShopImage();
        }
    }


    private void uploadShopImage() {
        
        progressDialog.setTitle("Change shop information");
        progressDialog.setMessage("Please wait while we are updating your information");
        progressDialog.setCancelable(false);
        progressDialog.show();
        
        
        Bitmap bitmap = ((BitmapDrawable) shopImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(RequestForChangingStoreInfo.this.getCacheDir() + 
                File.separator + filename + ".jpg");

        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(byteArrayOutputStream.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            progressDialog.dismiss();
            
            e.printStackTrace();
            Toast.makeText(RequestForChangingStoreInfo.this, 
                    "Can not make request, please try again", Toast.LENGTH_SHORT).show();
            
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
                                shopImageLink = response.body().string();
                                uploadNIDFrontImage();
                            } catch (IOException e) {
                                progressDialog.dismiss();
                                Toast.makeText(RequestForChangingStoreInfo.this, "Can not update info, please try again",
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(RequestForChangingStoreInfo.this, "Can not update info, please try again",
                                Toast.LENGTH_SHORT).show();
                        Log.e("RequestChange", t.getMessage());
                    }
                });
    }

    private void uploadNIDFrontImage() {
        Bitmap bitmap = ((BitmapDrawable) shopOwnerNIDFront.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(RequestForChangingStoreInfo.this.getCacheDir() +
                File.separator + filename + ".jpg");

        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(byteArrayOutputStream.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            progressDialog.dismiss();
            e.printStackTrace();
            Toast.makeText(RequestForChangingStoreInfo.this,
                    "Can not make request, please try again", Toast.LENGTH_SHORT).show();

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
                                shopKeeperNidFrontLink = response.body().string();
                                uploadTradeLicense();
                            } catch (IOException e) {
                                progressDialog.dismiss();
                                Toast.makeText(RequestForChangingStoreInfo.this, "Can not update info, please try again",
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(RequestForChangingStoreInfo.this, "Can not update info, please try again",
                                Toast.LENGTH_SHORT).show();
                        Log.e("RequestChange", t.getMessage());
                    }
                });
    }

    private void uploadTradeLicense() {
        Bitmap bitmap = ((BitmapDrawable) shopTradeLicense.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(RequestForChangingStoreInfo.this.getCacheDir() +
                File.separator + filename + ".jpg");

        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(byteArrayOutputStream.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            progressDialog.dismiss();

            e.printStackTrace();
            Toast.makeText(RequestForChangingStoreInfo.this,
                    "Can not make request, please try again", Toast.LENGTH_SHORT).show();

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
                                shopTradeLicenseLink = response.body().string();
                                proceedToFinalChange();
                            } catch (IOException e) {
                                progressDialog.dismiss();
                                Toast.makeText(RequestForChangingStoreInfo.this, "Can not update info, please try again",
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(RequestForChangingStoreInfo.this, "Can not update info, please try again",
                                Toast.LENGTH_SHORT).show();
                        Log.e("RequestChange", t.getMessage());
                    }
                });
    }

    private void proceedToFinalChange() {
        ChangeShopInfo changeShopInfo = new ChangeShopInfo();

        changeShopInfo.setShopOwnerMobileNumber(Prevalent.currentOnlineUser.getMobileNumber());

        changeShopInfo.setNewShopName(shopName.getText().toString());
        changeShopInfo.setNewShopAddress(shopAddress.getText().toString());
        changeShopInfo.setNewShopOwnerName(shopOwnerName.getText().toString());
        changeShopInfo.setNewShopServiceMobile(shopServiceMobile.getText().toString());

        changeShopInfo.setNewShopImageURL(shopImageLink);
        changeShopInfo.setNewShopOwnerNidFront(shopKeeperNidFrontLink);
        changeShopInfo.setNewShopTradeLicenseURI(shopTradeLicenseLink);


        saveToDatabase(changeShopInfo);
    }

    private void saveToDatabase(ChangeShopInfo changeShopInfo) {
        RetrofitClient.getInstance().getApi()
                .saveChangedShopInfoToDatabase(changeShopInfo)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(RequestForChangingStoreInfo.this,
                                    "Shop information request made, please wait for admin to approve", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(RequestForChangingStoreInfo.this,
                                    "Can not make shop information change request, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(RequestForChangingStoreInfo.this,
                                "Can not make shop information change request, please try again", Toast.LENGTH_SHORT).show();
                        Log.e("ShopInfoChange", t.getMessage());
                    }
                });
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


    private void requestStoragePermission(int i)
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("Ok", (dialog, which) -> {

                        ActivityCompat.requestPermissions(RequestForChangingStoreInfo.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(RequestForChangingStoreInfo.this, i);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(RequestForChangingStoreInfo.this, i);
        }
    }


    private void selectImage(Context context, int i)
    {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {
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
                        shopOwnerNIDFront.setImageBitmap(selectedImage);
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
                                shopOwnerNIDFront.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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

}