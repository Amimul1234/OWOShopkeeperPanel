package com.owoshopkeeperpanel.shopKeeperPanel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.owoshopkeeperpanel.Model.PendingShop;
import com.owoshopkeeperpanel.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DummyPendingShopActivity extends AppCompatActivity {

    private ImageView shopImage,ownerNID,ownerTradeLicence;
    private Button createShopButton, select_place;
    private EditText shopName, shopServiceMobile, ownerName, ownerMobile, shopAddress;
    private double latitude, longitude;

    private Uri shopImageUri = null, shopKeeperNidUri = null, tradeLicenseUri = null;

    private int imageChooser = 0;

    private StorageReference storeImages;

    private CheckBox check_box_1, check_box_2, check_box_3;
    private Spinner category_1, category_2, category_3;

    private AllianceLoader allianceLoader;
    static final int PICK_MAP_POINT_REQUEST = 999;

    private PendingShop pendingShop = new PendingShop();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_pending_shop);

        storeImages = FirebaseStorage.getInstance().getReference().child("ShopRelatedImages");
        allianceLoader = findViewById(R.id.alliance_loader);

        shopImage = (ImageView)findViewById(R.id.shop_image);
        ownerNID = (ImageView)findViewById(R.id.shop_owner_nid);
        ownerTradeLicence = (ImageView)findViewById(R.id.shop_trade_licence);
        createShopButton = (Button)findViewById(R.id.create_shop_btn);
        shopName = findViewById(R.id.shop_name);
        shopServiceMobile = (EditText)findViewById(R.id.shop_service_mobile);
        ownerName = (EditText)findViewById(R.id.shop_owner_name);
        ownerMobile = (EditText)findViewById(R.id.shop_owner_mobile);
        shopAddress = findViewById(R.id.shop_address);
        select_place = findViewById(R.id.select_place);

        check_box_1 = findViewById(R.id.category_1_check_box);
        check_box_2 = findViewById(R.id.category_2_check_box);
        check_box_3 = findViewById(R.id.category_3_check_box);

        category_1 = findViewById(R.id.category_1);
        category_2 = findViewById(R.id.category_2);
        category_3 = findViewById(R.id.category_3);

        select_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPointIntent = new Intent(DummyPendingShopActivity.this, LocationFromMap.class);
                startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
            }
        });

        shopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser = 1;
                CropImage.activity(shopImageUri)
                        .setMinCropResultSize(100, 100)
                        .setAspectRatio(1, 1)
                        .start(DummyPendingShopActivity.this);

            }
        });

        ownerNID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser = 2;
                CropImage.activity(shopKeeperNidUri)
                        .setAspectRatio(16, 10)
                        .start(DummyPendingShopActivity.this);
            }
        });

        ownerTradeLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser = 3;
                CropImage.activity(tradeLicenseUri)
                        .setMinCropResultSize(100, 100)
                        .setAspectRatio(1, 1)
                        .start(DummyPendingShopActivity.this);

            }
        });



        createShopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationCheck();
            }
        });


    }

    private void validationCheck() {

        String shop_name, shop_address, shop_owner_name, service_mobile, shop_owner_mobile;

        shop_name = shopName.getText().toString();
        shop_address = shopAddress.getText().toString();
        shop_owner_name = ownerName.getText().toString();
        service_mobile = shopServiceMobile.getText().toString();
        shop_owner_mobile = ownerMobile.getText().toString();

        if(shopImageUri == null)
        {
            Toast.makeText(this, "Please Upload Your Shop Image", Toast.LENGTH_SHORT).show();
        }
        else if(shopKeeperNidUri == null)
        {
            Toast.makeText(this, "Please Upload Your NID Front", Toast.LENGTH_SHORT).show();
        }
        else if(shop_name.isEmpty())
        {
            shopName.setError("Please provide shop name");
            shopName.requestFocus();
        }
        else if(shop_address.isEmpty())
        {
            shopAddress.setError("Please provide shop address");
            shopAddress.requestFocus();
        }
        else if(shop_owner_name.isEmpty())
        {
            ownerName.setError("Please provide owner name");
            ownerName.requestFocus();
        }
        else if(service_mobile.isEmpty())
        {
            shopServiceMobile.setError("Please provide service mobile number");
            shopServiceMobile.requestFocus();
        }
        else if(shop_owner_mobile.isEmpty())
        {
            ownerMobile.setError("Please provide owner mobile number");
            ownerMobile.requestFocus();
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MAP_POINT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
                Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
            }
        }

        else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data!=null)//If user wants to update the profile picture
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(imageChooser == 1)
            {
                shopImageUri = result.getUri();
                shopImage.setImageURI(shopImageUri);

                allianceLoader.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                String saveCurrentTime = currentTime.format(calendar.getTime());

                String randomKey = saveCurrentDate+saveCurrentTime;


                final StorageReference filePath = storeImages.child(shopImageUri.getLastPathSegment() + randomKey +".jpg");

                final UploadTask uploadTask = filePath.putFile(shopImageUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        String message=e.toString();
                        Toast.makeText(DummyPendingShopActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.GONE);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(DummyPendingShopActivity.this, "Shop Image uploaded successfully", Toast.LENGTH_SHORT).show();

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful())
                                {
                                    throw  task.getException();
                                }
                                pendingShop.setShop_image_uri(filePath.getDownloadUrl().toString());
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful())
                                {
                                    pendingShop.setShop_image_uri(task.getResult().toString());
                                    allianceLoader.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });

            }
            else if(imageChooser == 2)
            {
                shopKeeperNidUri = result.getUri();
                ownerNID.setImageURI(shopKeeperNidUri);

                allianceLoader.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                String saveCurrentTime = currentTime.format(calendar.getTime());

                String randomKey = saveCurrentDate+saveCurrentTime;


                final StorageReference filePath = storeImages.child(shopKeeperNidUri.getLastPathSegment() + randomKey +".jpg");

                final UploadTask uploadTask = filePath.putFile(shopKeeperNidUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        String message=e.toString();
                        Toast.makeText(DummyPendingShopActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.GONE);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(DummyPendingShopActivity.this, "NID uploaded successfully", Toast.LENGTH_SHORT).show();

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful())
                                {
                                    throw  task.getException();
                                }
                                pendingShop.setShop_keeper_nid_front_uri(filePath.getDownloadUrl().toString());
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful())
                                {
                                    pendingShop.setShop_keeper_nid_front_uri(task.getResult().toString());
                                    allianceLoader.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });

            }
            else if(imageChooser == 3)
            {
                tradeLicenseUri = result.getUri();
                ownerTradeLicence.setImageURI(tradeLicenseUri);


                allianceLoader.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                String saveCurrentTime = currentTime.format(calendar.getTime());

                String randomKey = saveCurrentDate+saveCurrentTime;


                final StorageReference filePath = storeImages.child(tradeLicenseUri.getLastPathSegment() + randomKey +".jpg");

                final UploadTask uploadTask = filePath.putFile(tradeLicenseUri);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        String message=e.toString();
                        Toast.makeText(DummyPendingShopActivity.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.GONE);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(DummyPendingShopActivity.this, "Trade License uploaded successfully", Toast.LENGTH_SHORT).show();

                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful())
                                {
                                    throw  task.getException();
                                }
                                pendingShop.setTrade_license_uri(filePath.getDownloadUrl().toString());
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful())
                                {
                                    pendingShop.setTrade_license_uri(task.getResult().toString());
                                    allianceLoader.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });
            }

        }
        else
        {
            Toast.makeText(this, "Try again...", Toast.LENGTH_SHORT).show();
        }
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


    }

 */


}
