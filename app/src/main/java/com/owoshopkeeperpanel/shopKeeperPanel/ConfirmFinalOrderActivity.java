package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.Cart;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText phoneEditText, delivery_address;
    private Button confirmOrderButton;
    private String totalAmount = "";
    private AllianceLoader loader;
    private ArrayList<Cart> carts;
    int ORDER_NUMBER = 0;
    String p = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        carts = (ArrayList<Cart>) getIntent().getSerializableExtra("products_id");

        int size = carts.size();

        List<Integer> product_ids = new ArrayList<>();

        for(int i=0; i<size; i++)
        {
            product_ids.add(carts.get(i).getProduct_id());
        }

        confirmOrderButton = findViewById(R.id.confirm_final_order_btn);
        phoneEditText = findViewById(R.id.shipment_phone_number);
        delivery_address = findViewById(R.id.delivery_address);

        loader = findViewById(R.id.loader);

        phoneEditText.setText(Prevalent.currentOnlineUser.getPhone());

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValue(product_ids);
            }
        });
    }

    private void CheckValue(List<Integer> product_ids) {

        if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please enter your mobile name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(delivery_address.getText().toString()))
        {
            Toast.makeText(this, "PLease enter the delivery address", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loader.setVisibility(View.VISIBLE);
            UpdateValue(product_ids);
        }
    }

    private void UpdateValue(List<Integer> product_ids) {

        final DatabaseReference orderNumber = FirebaseDatabase.getInstance().getReference();

        Query query = orderNumber.child("Shop Keeper Orders").child(Prevalent.currentOnlineUser.getPhone()).orderByKey().limitToLast(1);//Checking for the last order

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    for(DataSnapshot ids : snapshot.getChildren())//key is retrieved by iteration because we have kept key under number
                    {
                        p = ids.getKey();
                    }

                    ORDER_NUMBER = Integer.parseInt(p) + 1;
                }
                ConfirmOrder(product_ids);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConfirmFinalOrderActivity.this, "Error reading from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ConfirmOrder(List<Integer> product_ids) {

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Shop Keeper Orders")
                .child(Prevalent.currentOnlineUser.getPhone()).child(String.valueOf(ORDER_NUMBER));//Creating new Unique node


        final String saveCurrentDate, saveCurrentTime;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm::ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());


        HashMap<String, Object> orderMap = new HashMap<>();

        orderMap.put("order_number", String.valueOf(ORDER_NUMBER));
        orderMap.put("totalAmount", String.valueOf(totalAmount));
        orderMap.put("name", Prevalent.currentOnlineUser.getName());
        orderMap.put("receiver_phone", phoneEditText.getText().toString());
        orderMap.put("delivery_address", delivery_address.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state", "Not Shipped");
        orderMap.put("product_ids", product_ids);

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {

                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Your order has been placed successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        loader.setVisibility(View.INVISIBLE);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                                        loader.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                }
            }
        });
    }
}
