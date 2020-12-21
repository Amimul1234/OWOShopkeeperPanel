package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.Model.User_shopkeeper;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.hashing.hashing_algo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.shopKeeperPanel.registration_related.AfterRegisterActivity;
import com.owoshopkeeperpanel.shopKeeperPanel.registration_related.AfterShopRegisterRequest;
import com.owoshopkeeperpanel.shopKeeperPanel.registration_related.RegistrationActivity;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText mobile, pin;
    private ImageView visibility;
    private Boolean isShowPin = false;
    private TextView forgetPin,signUp;
    private CheckBox rememberMe;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Paper.init(this);


        loginButton=(Button)findViewById(R.id.login_btn);
        mobile = (EditText)findViewById(R.id.shopkeeper_mobile);
        pin = (EditText)findViewById(R.id.shopkeeper_pin);
        visibility = findViewById(R.id.show_pin);
        rememberMe=(CheckBox)findViewById(R.id.remember_me);
        forgetPin=(TextView)findViewById(R.id.forget_pin);
        signUp=(TextView)findViewById(R.id.sign_up);
        loadingbar = new ProgressDialog(this);

        if(Paper.book().read(Prevalent.UserPhoneKey) != null && Paper.book().read(Prevalent.UserPinKey) != null)
        {
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please wait while we are checking the credentials....");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            AllowAccessToAccount(Paper.book().read(Prevalent.UserPhoneKey), Paper.book().read(Prevalent.UserPinKey));
        }

        forgetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShowPin) {
                    pin.setTransformationMethod(new PasswordTransformationMethod());
                    visibility.setImageResource(R.drawable.ic_visibility_off);
                    isShowPin = false;

                }else{
                    pin.setTransformationMethod(null);
                    visibility.setImageResource(R.drawable.ic_visibility);
                    isShowPin = true;
                }
            }
        });
    }

    private void LoginUser() {

        String Phone = mobile.getText().toString();
        String Pin = pin.getText().toString();

        if(Phone.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please enter a phone number", Toast.LENGTH_SHORT).show();
        }
        else if(Pin.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please write your pin", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingbar.setTitle("Login Account");
            loadingbar.setMessage("Please wait while we are checking the credentials....");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            try {
                Pin = hashing_algo.toHexString(hashing_algo.getSHA(Pin));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            AllowAccessToAccount(Phone, Pin);
        }
    }

    private void AllowAccessToAccount(final String phone, final String pin) {

        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        Query query = RootRef.child("Shopkeeper").orderByKey().equalTo(phone);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    User_shopkeeper users = snapshot.child(phone).getValue(User_shopkeeper.class);

                    if(users.getPin().equals(pin))
                    {

                        if(rememberMe.isChecked())//Writing user data on android storage
                        {
                            Paper.book().write(Prevalent.UserPhoneKey, phone);
                            Paper.book().write(Prevalent.UserPinKey, pin);
                        }

                        Query query = RootRef.child("permittedShopKeeper").orderByKey().equalTo(phone); //Checking if the shop already permitted or not

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists())
                                {
                                    List<String> permitted = new ArrayList<>();

                                    permitted = (List<String>) snapshot.child(phone).child("permissions").getValue();

                                    Prevalent.category_to_display = permitted;

                                    Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    Prevalent.currentOnlineUser = users;
                                    loadingbar.dismiss();
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Query query = RootRef.child("PendingShopRequest").orderByKey().equalTo(phone);

                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists())
                                            {
                                                Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), AfterShopRegisterRequest.class);
                                                Prevalent.currentOnlineUser = users;
                                                loadingbar.dismiss();
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(), "Log in successful", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), AfterRegisterActivity.class);
                                                Prevalent.currentOnlineUser = users;
                                                loadingbar.dismiss();
                                                startActivity(intent);
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                            }
                        });
                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "Please enter correct pin", Toast.LENGTH_SHORT).show();
                        loadingbar.dismiss();
                    }
                }

                else {
                    Toast.makeText(MainActivity.this, "No such account", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
