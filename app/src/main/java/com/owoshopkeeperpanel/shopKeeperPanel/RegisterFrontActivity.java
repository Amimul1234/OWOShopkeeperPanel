package com.owoshopkeeperpanel.shopKeeperPanel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Model.User_shopkeeper;
import com.owoshopkeeperpanel.hashing.hashing_algo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.security.NoSuchAlgorithmException;

public class RegisterFrontActivity extends AppCompatActivity {

    private ImageView newShopkeeperPic, showPin, showConfirmPin;

    private TextView term_condition;

    private EditText newShopkeeperPin, newShopkeeperConfirmPin;

    private Boolean isShowPin = false,
            isShowConfirmPin = false;

    private Uri imageuri;
    private Button createBtn;
    private String mobilenumber, name;
    private CheckBox checkBox;
    private String myUrl = "";
    private ProgressDialog loadingbar;

    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_front);

        term_condition = (TextView)findViewById(R.id.terms_conditions);
        newShopkeeperPic = (ImageView)findViewById(R.id.new_shopkeeper_pic);
        showPin = (ImageView)findViewById(R.id.show_pin);
        showConfirmPin = (ImageView)findViewById(R.id.show_confirmed_pin);
        newShopkeeperPin = (EditText)findViewById(R.id.new_shopkeeper_pin);
        newShopkeeperConfirmPin = (EditText)findViewById(R.id.new_shopkeeper_confirm_pin);
        createBtn = (Button)findViewById(R.id.create_account_btn);
        checkBox = (CheckBox)findViewById(R.id.terms);
        loadingbar = new ProgressDialog(this);

        loadingbar.setTitle("Create Account");
        loadingbar.setMessage("Please wait while we are creating a new account");
        loadingbar.setCanceledOnTouchOutside(false);

        mobilenumber = getIntent().getStringExtra("mobilenumber");
        name = getIntent().getStringExtra("name");

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Shopkeeper");

        term_condition.setOnClickListener(new View.OnClickListener() {//for showing the terms and conditions
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterFrontActivity.this, TermsConditionsActivity.class);
                startActivity(intent);
            }
        });

        newShopkeeperPic.setOnClickListener(new View.OnClickListener() {//For selecting the profile image

            @Override
            public void onClick(View v) {
                CropImage.activity(imageuri)
                        .setAspectRatio(1, 1)
                        .start(RegisterFrontActivity.this);
            }

        });


        showPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShowPin) {
                    newShopkeeperPin.setTransformationMethod(new PasswordTransformationMethod());
                    showPin.setImageResource(R.drawable.ic_visibility_off);
                    isShowPin = false;

                }else{
                    newShopkeeperPin.setTransformationMethod(null);
                    showPin.setImageResource(R.drawable.ic_visibility);
                    isShowPin = true;
                }
            }
        });

        showConfirmPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShowConfirmPin) {
                    newShopkeeperConfirmPin.setTransformationMethod(new PasswordTransformationMethod());
                    showConfirmPin.setImageResource(R.drawable.ic_visibility_off);
                    isShowConfirmPin = false;

                }else{
                    newShopkeeperConfirmPin.setTransformationMethod(null);
                    showConfirmPin.setImageResource(R.drawable.ic_visibility);
                    isShowConfirmPin = true;
                }
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isChecked())
                {
                    Toast.makeText(RegisterFrontActivity.this, "Please accept our terms and conditions.", Toast.LENGTH_SHORT).show();
                }
                else
                    verification();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data!=null)//If user wants to update the profile picture
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageuri = result.getUri();
            newShopkeeperPic.setImageURI(imageuri);
        }

        else
        {
            Toast.makeText(this, "Error, Please Try Again...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterFrontActivity.this, RegisterFrontActivity.class));
            finish();
        }
    }

    private void verification() {

        String mobile = mobilenumber;
        String pin = newShopkeeperPin.getText().toString();
        String confirmpin = newShopkeeperConfirmPin.getText().toString();

        if(pin.isEmpty())
        {
            newShopkeeperPin.setError("Please enter a pin");
            newShopkeeperPin.requestFocus();
        }
        else if(confirmpin.isEmpty())
        {
            newShopkeeperConfirmPin.setError("Please enter that pin to confirm");
            newShopkeeperConfirmPin.requestFocus();
        }

        else if(!confirmpin.equals(pin))
        {
            newShopkeeperConfirmPin.setError("Pin did not match");
            newShopkeeperConfirmPin.requestFocus();
        }
        else
        {
            try {
                pin = hashing_algo.toHexString(hashing_algo.getSHA(pin));// hashing the pin
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            uploadImageofShopkeeper(name, mobilenumber, pin);
        }
    }

    private void uploadImageofShopkeeper(final String shopkeeper_name, final String mobilenumber, final String pin) {

        if(imageuri!=null)
        {


            final StorageReference fileRef = storageProfilePictureRef.child(mobilenumber+".jpg");

            uploadTask = fileRef.putFile(imageuri);

            loadingbar.show();

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        final DatabaseReference RootRef;
                        RootRef = FirebaseDatabase.getInstance().getReference();

                        User_shopkeeper user_shopkeeper = new User_shopkeeper(shopkeeper_name,
                                mobilenumber, pin, myUrl);

                        RootRef.child("Shopkeeper").child(mobilenumber).setValue(user_shopkeeper).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(), "Congratulations ! Your account created successfully", Toast.LENGTH_SHORT).show();
                                    loadingbar.dismiss();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent intent = new Intent(RegisterFrontActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else {
                                    loadingbar.dismiss();
                                    Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                    else
                    {
                        Toast.makeText(RegisterFrontActivity.this, "Error...Can not upload image", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }
                }
            });
        }

        else
        {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            loadingbar.dismiss();
        }
    }
}
