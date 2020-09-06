package com.owoshopkeeperpanel.shopKeeperPanel;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.owoshopkeeperpanel.Model.User_shopkeeper;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.sheetHandeler.User_Info_Update_Bottom_Sheet;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity{

    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;

    private CardView profile_pic_card, pin_change_card;
    private CircleImageView profileImageView, profileImageSmall;
    private ImageView back_to_home;
    private TextView user_name, user_mobile, user_information_update;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private User_shopkeeper user_shopkeeper = new User_shopkeeper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfilePictureRef= FirebaseStorage.getInstance().getReference().child("Shopkeeper");

        profileImageView = (CircleImageView)findViewById(R.id.settings_profile_image);
        profileImageSmall = findViewById(R.id.settings_profile_image_small);
        back_to_home = findViewById(R.id.back_to_home);
        user_name = findViewById(R.id.user_information_name);
        user_mobile = findViewById(R.id.user_information_mobile);
        user_information_update = findViewById(R.id.edit_user_information);

        profile_pic_card = findViewById(R.id.profile_pic_card);
        pin_change_card = findViewById(R.id.pin_change_card);
        collapsingToolbarLayout = findViewById(R.id.settings_full_name_toolbar);

        userInfoDisplay();

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        profile_pic_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[]=new CharSequence[]{"Yes","No"};
                AlertDialog.Builder builder=new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Do you want to update profile picture?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (i==0)
                        {
                            CropImage.activity(imageUri)
                                    .setAspectRatio(1,1)
                                    .setCropShape(CropImageView.CropShape.OVAL)
                                    .start(SettingsActivity.this);

                        }
                        else if(i==1)
                        {
                            Intent intent=new Intent(SettingsActivity.this, SettingsActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });



        user_information_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User_Info_Update_Bottom_Sheet bottomSheet = new User_Info_Update_Bottom_Sheet(getApplicationContext(), user_shopkeeper.getName(), user_shopkeeper.getPhone());
                bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
            }
        });
    }


    private void uploadImage() {

        final ProgressDialog progressDialog=new ProgressDialog(this);

        progressDialog.setTitle("Update Profile");
        progressDialog.setMessage("Please wait while we are updating profile picture");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri!=null)
        {
            final StorageReference fileRef=storageProfilePictureRef.child(Prevalent.currentOnlineUser.getPhone() + ".jpg");

            StorageReference profile_pic = FirebaseStorage.getInstance().getReferenceFromUrl(user_shopkeeper.getImage());

            profile_pic.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    uploadTask = fileRef.putFile(imageUri);

                    uploadTask.continueWithTask(new Continuation() {
                        @Override
                        public Object then(@NonNull Task task) throws Exception {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }
                            return fileRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful())
                            {
                                Uri downloadUrl=task.getResult();
                                myUrl=downloadUrl.toString();
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Shopkeeper");
                                user_shopkeeper.setImage(myUrl);
                                Prevalent.currentOnlineUser.setImage(myUrl); //Setting the prevalent user image
                                ref.child(Prevalent.currentOnlineUser.getPhone()).setValue(user_shopkeeper);
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Profile information updated successfully", Toast.LENGTH_SHORT).show();
                                userInfoDisplay();
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(SettingsActivity.this, "Error Uploading Photo", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SettingsActivity.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(this, "Image not selected.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            imageUri=result.getUri();
            profileImageView.setImageURI(imageUri);
            profileImageSmall.setImageURI(imageUri);
            uploadImage();
        }
        else {
            Toast.makeText(this, "Error, Try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SettingsActivity.this,SettingsActivity.class));
            finish();
        }
    }

    private void userInfoDisplay() {

        DatabaseReference UsersRef= FirebaseDatabase.getInstance().getReference().child("Shopkeeper").child(Prevalent.currentOnlineUser.getPhone());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists())
                {
                    user_shopkeeper =  datasnapshot.getValue(User_shopkeeper.class);
                    Glide.with(getApplicationContext()).load(user_shopkeeper.getImage()).into(profileImageView);
                    Glide.with(getApplicationContext()).load(user_shopkeeper.getImage()).into(profileImageSmall);
                    collapsingToolbarLayout.setTitle(user_shopkeeper.getName());
                    user_name.setText(user_shopkeeper.getName());
                    user_mobile.setText(user_shopkeeper.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

