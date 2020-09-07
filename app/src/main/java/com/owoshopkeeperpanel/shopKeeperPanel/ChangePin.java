package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoshopkeeperpanel.Model.User_shopkeeper;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.hashing.hashing_algo;

import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

public class ChangePin extends AppCompatActivity {

    private ImageView back_to_home, show_current_pin, show_new_pin, show_new_pin_confirm;
    private EditText current_pin, new_pin, confirm_pin;
    private Button change;

    private ProgressBar progressBar;

    private Boolean isShowPinCurrentPin = false;
    private Boolean isShowPinNewPin = false;
    private Boolean isShowNewPinConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        show_current_pin = findViewById(R.id.show_current_pin);
        show_new_pin = findViewById(R.id.show_new_pin);
        show_new_pin_confirm = findViewById(R.id.show_new_pin_confirm);

        progressBar = findViewById(R.id.loader);

        current_pin = findViewById(R.id.currentPin);
        new_pin = findViewById(R.id.newPinUpdate);
        confirm_pin = findViewById(R.id.newPinConfirmation);

        change = findViewById(R.id.change);
        back_to_home = findViewById(R.id.back_to_home);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        show_current_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPinCurrentPin) {
                    current_pin.setTransformationMethod(new PasswordTransformationMethod());
                    show_current_pin.setImageResource(R.drawable.ic_visibility_off);
                    isShowPinCurrentPin = false;

                }else{
                    current_pin.setTransformationMethod(null);
                    show_current_pin.setImageResource(R.drawable.ic_visibility);
                    isShowPinCurrentPin = true;
                }
            }
        });

        show_new_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPinNewPin) {
                    new_pin.setTransformationMethod(new PasswordTransformationMethod());
                    show_new_pin.setImageResource(R.drawable.ic_visibility_off);
                    isShowPinNewPin = false;

                }else{
                    new_pin.setTransformationMethod(null);
                    show_new_pin.setImageResource(R.drawable.ic_visibility);
                    isShowPinNewPin = true;
                }
            }
        });

        show_new_pin_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowNewPinConfirm) {
                    confirm_pin.setTransformationMethod(new PasswordTransformationMethod());
                    show_new_pin_confirm.setImageResource(R.drawable.ic_visibility_off);
                    isShowNewPinConfirm = false;

                }else{
                    confirm_pin.setTransformationMethod(null);
                    show_new_pin_confirm.setImageResource(R.drawable.ic_visibility);
                    isShowNewPinConfirm = true;
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    private void validate() {
        String user_current_pin = current_pin.getText().toString();
        String user_new_pin = new_pin.getText().toString();
        String user_confirm_new_pin = confirm_pin.getText().toString();
        String hashed_pin = null;

        try {
            hashed_pin = hashing_algo.toHexString(hashing_algo.getSHA(user_current_pin));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (!hashed_pin.equals(Prevalent.currentOnlineUser.getPin())) {
            current_pin.setError("Pin did not match with current pin");
            current_pin.requestFocus();
        }
        else if(user_new_pin.isEmpty())
        {
            new_pin.setError("New pin can not be null");
            new_pin.requestFocus();
        }
        else if(user_confirm_new_pin.isEmpty())
        {
            confirm_pin.setError("Please confirm your new pin");
            confirm_pin.requestFocus();
        }
        else if(!user_new_pin.equals(user_confirm_new_pin))
        {
            confirm_pin.setError("Confirm pin did not matched");
            confirm_pin.requestFocus();
        }
        else
        {
            changeUserInformation(user_new_pin);
        }
    }

    private void changeUserInformation(String new_pin) {

        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Shopkeeper");

        User_shopkeeper user_shopkeeper = new User_shopkeeper();

        try {
            String new_hashed_pin = hashing_algo.toHexString(hashing_algo.getSHA(new_pin));
            user_shopkeeper.setName(Prevalent.currentOnlineUser.getName());
            user_shopkeeper.setPhone(Prevalent.currentOnlineUser.getPhone());
            user_shopkeeper.setImage(Prevalent.currentOnlineUser.getImage());
            user_shopkeeper.setPin(new_hashed_pin);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        ref.child(Prevalent.currentOnlineUser.getPhone()).setValue(user_shopkeeper).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ChangePin.this, "Pin number updated successfully", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                Paper.book().destroy();
                Intent intent=new Intent(ChangePin.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChangePin.this, "Can not update pin number", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}