package com.shopKPR.shopKeeperSettings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.homeComponents.HomeActivity;
import com.shopKPR.R;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.userRegistration.ShopKeeperUser;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity{

    private CircleImageView profileImageView, profileImageSmall;
    private TextView userMobile;
    private TextView userName;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private String previousImagePath;
    private ShopKeeperUser shopKeeperUser;
    private final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImageView = findViewById(R.id.settings_profile_image);
        profileImageSmall = findViewById(R.id.settings_profile_image_small);
        ImageView back_to_home = findViewById(R.id.back_to_home);
        userName = findViewById(R.id.user_information_name);
        userMobile = findViewById(R.id.user_information_mobile);
        TextView userInformationUpdate = findViewById(R.id.edit_user_information);

        CardView profile_pic_card = findViewById(R.id.profile_pic_card);
        CardView pinChangeCard = findViewById(R.id.pin_change_card);
        collapsingToolbarLayout = findViewById(R.id.settings_full_name_toolbar);
        RelativeLayout changePinLayout = findViewById(R.id.change_pin_layout);

        userInfoDisplay();

        back_to_home.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        changePinLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ChangePin.class);
            intent.putExtra("shopKeeper", shopKeeperUser);
            startActivity(intent);
        });

        profile_pic_card.setOnClickListener(v -> {

            CharSequence[] options =new CharSequence[]{"Yes","No"};
            AlertDialog.Builder builder=new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Are you sure you want to update your profile picture?");
            builder.setCancelable(true);

            builder.setItems(options, (dialog, i) ->
            {
                if (i==0)
                {
                    requestStoragePermission();
                }
            });
            builder.show();
        });

        userInformationUpdate.setOnClickListener(v -> {
            UserInfoUpdateBottomSheet bottomSheet = new UserInfoUpdateBottomSheet(getApplicationContext(), shopKeeperUser);
            bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
        });
    }

    private void userInfoDisplay()
    {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("User Information");
        progressDialog.setMessage("Please wait while we are getting your information");
        progressDialog.show();


        RetrofitClient.getInstance().getApi()
                .getShopKeeper(Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<ShopKeeperUser>() {
                    @Override
                    public void onResponse(@NotNull Call<ShopKeeperUser> call, @NotNull Response<ShopKeeperUser> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            shopKeeperUser = response.body();
                            updateViews();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SettingsActivity.this, "Failed to load user information, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ShopKeeperUser> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("SettingsActivity", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(SettingsActivity.this, "Failed to load user information, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateViews()
    {

        if(shopKeeperUser.getImageUri() != null)
        {
            Glide.with(this).load(HostAddress.HOST_ADDRESS.getHostAddress()+shopKeeperUser.getImageUri())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(profileImageView);

            Glide.with(this).load(HostAddress.HOST_ADDRESS.getHostAddress()+shopKeeperUser.getImageUri())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(profileImageSmall);
        }

        collapsingToolbarLayout.setTitle(shopKeeperUser.getName());
        userName.setText(shopKeeperUser.getName());
        userMobile.setText(shopKeeperUser.getMobileNumber());
    }

    private void requestStoragePermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("ok", (dialog, which) -> {

                        ActivityCompat.requestPermissions(SettingsActivity.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(SettingsActivity.this);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(SettingsActivity.this);
        }
    }

    private void selectImage(Context context)
    {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
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
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profileImageView.setImageBitmap(selectedImage);
                        profileImageSmall.setImageBitmap(selectedImage);
                        uploadImageToServer();
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profileImageSmall.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                profileImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                                uploadImageToServer();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private void uploadImageToServer()
    {
        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setTitle("Update Profile Picture");
        progressDialog.setMessage("Please wait while we are updating your profile image");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Bitmap bitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(SettingsActivity.this.getCacheDir() + File.separator + filename + ".jpg");

        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(byteArrayOutputStream.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part multipartFile = MultipartBody.Part.createFormData("multipartFile", file.getName(), requestBody);

        RetrofitClient.getInstance().getApi()
                .uploadImageToServer("Profile Picture", multipartFile)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                assert response.body() != null;
                                String path = response.body().string();
                                previousImagePath = shopKeeperUser.getImageUri();
                                shopKeeperUser.setImageUri(path);
                                updateToDatabaseImageHandling(shopKeeperUser, progressDialog);
                            } catch (IOException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Log.e("AddAProduct", "Error occurred, Error is: "+e.getMessage());
                                Toast.makeText(SettingsActivity.this, "Error occurred, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SettingsActivity.this, "Failed to upload product image, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("AddAProduct", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(SettingsActivity.this, "Failed to upload image, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateToDatabaseImageHandling(ShopKeeperUser shopKeeperUser, ProgressDialog progressDialog)
    {
        RetrofitClient.getInstance().getApi()
                .updateShopKeeperInfo(shopKeeperUser)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            if(previousImagePath != null)
                            {
                                String cleanedAddress = previousImagePath.substring(34);

                                RetrofitClient.getInstance().getApi()
                                        .deleteImage(cleanedAddress)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                progressDialog.dismiss();
                                                Toast.makeText(SettingsActivity.this, "Updated your image successfully", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                progressDialog.dismiss();
                                                Log.e("Settings", "Error occurred, Error is:" + t.getMessage());
                                                Toast.makeText(SettingsActivity.this, "Updated your image successfully", Toast.LENGTH_SHORT).show();


                                                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            }

                        }

                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(SettingsActivity.this, "Sorry! Can not update your info. , Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("Settings", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(SettingsActivity.this, "Sorry! Can not update your info. , Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

