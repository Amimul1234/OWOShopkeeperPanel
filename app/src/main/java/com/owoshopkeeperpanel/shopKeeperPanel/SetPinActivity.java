package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.User_shopkeeper;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.hashing.hashing_algo;

import java.security.NoSuchAlgorithmException;

public class SetPinActivity extends AppCompatActivity {

    private Boolean isShowPin = false;
    private ImageView showPin;
    private EditText setNewPin;
    private String mobilenumber;
    private Button setPinBtn;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin);

        showPin=(ImageView)findViewById(R.id.show_pin);
        setNewPin=(EditText)findViewById(R.id.new_pin);
        setPinBtn=(Button)findViewById(R.id.forget_set_pin_btn);
        loadingbar = new ProgressDialog(this);

        mobilenumber = getIntent().getStringExtra("mobilenumber");

        showPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShowPin) {
                    setNewPin.setTransformationMethod(new PasswordTransformationMethod());
                    showPin.setImageResource(R.drawable.ic_visibility_off);
                    isShowPin = false;

                }else{
                    setNewPin.setTransformationMethod(null);
                    showPin.setImageResource(R.drawable.ic_visibility);
                    isShowPin = true;
                }
            }
        });

        setPinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingbar.setTitle("Set New Pin");
                loadingbar.setMessage("Please wait while we are setting the new pin...");
                loadingbar.setCanceledOnTouchOutside(false);
                loadingbar.show();

                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference().child("Shopkeeper");

                Query query = RootRef.orderByKey().equalTo(mobilenumber);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            User_shopkeeper users = snapshot.child(mobilenumber).getValue(User_shopkeeper.class);
                            String Pin = setNewPin.getText().toString();

                            try {
                                Pin = hashing_algo.toHexString(hashing_algo.getSHA(Pin));
                            } catch (NoSuchAlgorithmException e) {
                                e.printStackTrace();
                            }
                            users.setPin(Pin);//setting the new pin to firebase

                            RootRef.child(users.getPhone()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        loadingbar.dismiss();
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(SetPinActivity.this, "Can not set new pin"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                    }
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}
