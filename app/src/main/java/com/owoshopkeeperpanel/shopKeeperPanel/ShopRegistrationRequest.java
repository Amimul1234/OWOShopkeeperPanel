package com.owoshopkeeperpanel.shopKeeperPanel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.owoshopkeeperpanel.Model.PendingShop;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShopRegistrationRequest extends AppCompatActivity {

    private ImageView shopImage,ownerNID,ownerTradeLicence;
    private Button createShopButton, select_place;
    private EditText shopName, shopServiceMobile, ownerName, ownerMobile, shopAddress;

    private Uri shopImageUri = null, shopKeeperNidUri = null, tradeLicenseUri = null;

    private int imageChooser = 0;

    private StorageReference storeImages;

    private CheckBox check_box_1, check_box_2, check_box_3;
    private Spinner category_1, category_2, category_3;

    private AllianceLoader allianceLoader;
    static final int PICK_MAP_POINT_REQUEST = 999;

    private PendingShop pendingShop = new PendingShop();

    private LatLng latlang;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();


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
                Intent pickPointIntent = new Intent(ShopRegistrationRequest.this, LocationFromMap.class);
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
                        .start(ShopRegistrationRequest.this);

            }
        });

        ownerNID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser = 2;
                CropImage.activity(shopKeeperNidUri)
                        .setAspectRatio(16, 10)
                        .start(ShopRegistrationRequest.this);
            }
        });

        ownerTradeLicence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser = 3;
                CropImage.activity(tradeLicenseUri)
                        .setMinCropResultSize(100, 100)
                        .setAspectRatio(1, 1)
                        .start(ShopRegistrationRequest.this);

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
        else if(!check_box_1.isChecked() && !check_box_2.isChecked() && !check_box_3.isChecked())
        {
            Toast.makeText(ShopRegistrationRequest.this, "Please select at least one category", Toast.LENGTH_SHORT).show();
            check_box_1.setError("At least one category selection is required");
            check_box_1.requestFocus();
        }
        else if(latlang == null)
        {
            Toast.makeText(ShopRegistrationRequest.this, "Please validate your address", Toast.LENGTH_SHORT).show();
        }
        else
        {
            WriteInDataBase();
        }

    }

    private void WriteInDataBase() {

        String shop_name, shop_address, shop_owner_name, service_mobile, shop_owner_mobile;

        shop_name = shopName.getText().toString();
        shop_address = shopAddress.getText().toString();
        shop_owner_name = ownerName.getText().toString();
        service_mobile = shopServiceMobile.getText().toString();
        shop_owner_mobile = ownerMobile.getText().toString();

        pendingShop.setShop_name(shop_name);
        pendingShop.setShop_address(shop_address);
        pendingShop.setShop_owner_name(shop_owner_name);
        pendingShop.setShop_service_mobile(service_mobile);
        pendingShop.setShop_owner_mobile(shop_owner_mobile);
        pendingShop.setLatLng(latlang);

        if(check_box_1.isChecked())
        {
            pendingShop.addAccess(category_1.getSelectedItem().toString());
        }
        if(check_box_2.isChecked())
        {
            pendingShop.addAccess(category_2.getSelectedItem().toString());
        }
        if(check_box_3.isChecked())
        {
            pendingShop.addAccess(category_3.getSelectedItem().toString());
        }

        final DatabaseReference myRef = database.getReference().child("PendingShopRequest").child(Prevalent.currentOnlineUser.getPhone());

        myRef.setValue(pendingShop).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(ShopRegistrationRequest.this, "Requested for shop creation", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShopRegistrationRequest.this, AfterShopRegisterRequest.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ShopRegistrationRequest.this, "Can not create shop, Please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_MAP_POINT_REQUEST) {
            // Making sure the request was successful
            if (resultCode == RESULT_OK) {

                latlang = (LatLng) data.getParcelableExtra("picked_point");

            }
            else
            {
                Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ShopRegistrationRequest.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.GONE);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ShopRegistrationRequest.this, "Shop Image uploaded successfully", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(ShopRegistrationRequest.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.GONE);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ShopRegistrationRequest.this, "NID uploaded successfully", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(ShopRegistrationRequest.this, "Error : "+message, Toast.LENGTH_SHORT).show();
                        allianceLoader.setVisibility(View.GONE);

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ShopRegistrationRequest.this, "Trade License uploaded successfully", Toast.LENGTH_SHORT).show();

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

}