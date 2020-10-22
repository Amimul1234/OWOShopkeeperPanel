package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.owoshopkeeperpanel.Model.Ordered_products;
import com.owoshopkeeperpanel.Model.PendingShop;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText phoneEditText, delivery_address, additional_comments;
    private Button confirmOrderButton;
    private RadioGroup radioGroup;
    private RadioButton delivery_option_1, delivery_option_2;
    private String totalAmount = "";
    private AllianceLoader loader;
    private ArrayList<Cart> carts;
    int ORDER_NUMBER = 0;
    private Double discounted_price;
    int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalAmount = getIntent().getStringExtra("Total Price");
        carts = (ArrayList<Cart>) getIntent().getSerializableExtra("products_id");
        discounted_price = Double.parseDouble(getIntent().getStringExtra("discounted_price"));

        int size = carts.size();

        List<Ordered_products> ordered_products = new ArrayList<>();

        for(int i=0; i<size; i++)
        {
            ordered_products.add(new Ordered_products(carts.get(i).getProduct_id(), Integer.parseInt(carts.get(i).getNeeded_quantity()),
                    carts.get(i).getProduct_name(), carts.get(i).getProduct_image(), carts.get(i).getProduct_price()));
        }

        confirmOrderButton = findViewById(R.id.confirm_final_order_btn);
        phoneEditText = findViewById(R.id.shipment_phone_number);
        delivery_address = findViewById(R.id.delivery_address);
        additional_comments = findViewById(R.id.additional_comments);
        delivery_option_1 = findViewById(R.id.delivery_option_1);
        delivery_option_2 = findViewById(R.id.delivery_option_2);
        radioGroup = findViewById(R.id.radio_group);

        loader = findViewById(R.id.loader);

        phoneEditText.setText(Prevalent.currentOnlineUser.getPhone());

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValue(ordered_products);
            }
        });
    }

    private void CheckValue(List<Ordered_products> ordered_products) {

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
            UpdateValue(ordered_products);
        }
    }

    private void UpdateValue(List<Ordered_products> ordered_products) {

        final DatabaseReference orderNumber = FirebaseDatabase.getInstance().getReference();

        Query query = orderNumber.child("Shop Order Number");//Checking for the last shop_management_order

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    p = snapshot.getValue(Integer.class);
                    ORDER_NUMBER = p + 1;
                    orderNumber.child("Shop Order Number").setValue(ORDER_NUMBER).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ConfirmOrder(ordered_products);
                        }
                    });
                }
                else
                {
                    orderNumber.child("Shop Order Number").setValue(ORDER_NUMBER).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            ConfirmOrder(ordered_products);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConfirmFinalOrderActivity.this, "Error reading from database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ConfirmOrder(List<Ordered_products> ordered_products) {

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Shop Keeper Orders").child(String.valueOf(ORDER_NUMBER));//Creating new Unique node


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
        orderMap.put("additional_comments", additional_comments.getText().toString());
        orderMap.put("shop_number", Prevalent.currentOnlineUser.getPhone());
        if(radioGroup.getCheckedRadioButtonId() == R.id.delivery_option_1)
        {
            orderMap.put("delivery_method", "Cash on delivery");
        }
        else
        {
            orderMap.put("delivery_method", "Digital payment");
        }
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state", "Pending");
        orderMap.put("product_ids", ordered_products);
        orderMap.put("coupon_discount", discounted_price);

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
