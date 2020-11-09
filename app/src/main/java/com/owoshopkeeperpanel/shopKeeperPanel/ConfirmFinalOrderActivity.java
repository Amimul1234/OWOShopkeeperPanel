package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import com.owoshopkeeperpanel.Model.Cart_list_product;
import com.owoshopkeeperpanel.Model.Shop_keeper_ordered_products;
import com.owoshopkeeperpanel.Model.Shop_keeper_orders;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private EditText phoneEditText, delivery_address, additional_comments;

    private Button confirmOrderButton;

    private RadioGroup radioGroup;
    private RadioButton delivery_option_1, delivery_option_2;

    private double grand_total_price, discount, grand_total_with_discount;

    private AllianceLoader loader;

    private ArrayList<Cart_list_product> cart_list_products;

    private Spinner time_slot_selector;
    private List<String> available_time_slots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        time_slot_selector = findViewById(R.id.time_slots);
        confirmOrderButton = findViewById(R.id.confirm_final_order_btn);
        phoneEditText = findViewById(R.id.shipment_phone_number);
        delivery_address = findViewById(R.id.delivery_address);
        additional_comments = findViewById(R.id.additional_comments);
        delivery_option_1 = findViewById(R.id.delivery_option_1);
        delivery_option_2 = findViewById(R.id.delivery_option_2);
        radioGroup = findViewById(R.id.radio_group);
        loader = findViewById(R.id.loader);


        grand_total_price = Double.parseDouble(getIntent().getStringExtra("grand_total"));
        cart_list_products = (ArrayList<Cart_list_product>) getIntent().getSerializableExtra("cart_list_products");
        discount = Double.parseDouble(getIntent().getStringExtra("discount"));
        grand_total_with_discount = Double.parseDouble(getIntent().getStringExtra("grand_total_with_discount"));


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Time Slots").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        available_time_slots.add(dataSnapshot.getValue(String.class));
                    }


                    ArrayAdapter<String> available_time_slots_adapter = new ArrayAdapter<String>(ConfirmFinalOrderActivity.this, android.R.layout.simple_spinner_item, available_time_slots);

                    time_slot_selector.setAdapter(available_time_slots_adapter);

                }
                else
                {
                    Toast.makeText(ConfirmFinalOrderActivity.this, "No time slots available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ConfirmFinalOrderActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        phoneEditText.setText(Prevalent.currentOnlineUser.getPhone());

        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValue(cart_list_products);
            }
        });
    }

    private void CheckValue(List<Cart_list_product> cart_list_products) {

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
            ConfirmOrder(cart_list_products);
        }
    }

    private void ConfirmOrder(List<Cart_list_product> cart_list_products) {
        final String saveCurrentDate, saveCurrentTime;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm::ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        Shop_keeper_orders shop_keeper_orders = new Shop_keeper_orders();

        shop_keeper_orders.setAdditional_comments(additional_comments.getText().toString());
        shop_keeper_orders.setCoupon_discount(discount);
        shop_keeper_orders.setDate(saveCurrentDate);
        shop_keeper_orders.setDelivery_address(delivery_address.getText().toString());

        if(radioGroup.getCheckedRadioButtonId() == R.id.delivery_option_1)
        {
            shop_keeper_orders.setMethod("Cash on delivery");
        }
        else
        {
            shop_keeper_orders.setMethod("Digital payment");
        }

        shop_keeper_orders.setReceiver_phone(phoneEditText.getText().toString());
        shop_keeper_orders.setShop_phone(Prevalent.currentOnlineUser.getPhone());
        shop_keeper_orders.setShipping_state("Pending");
        shop_keeper_orders.setTime_slot(time_slot_selector.getSelectedItem().toString());
        shop_keeper_orders.setOrder_time(saveCurrentTime);
        shop_keeper_orders.setTotal_amount(grand_total_price);

        int size = cart_list_products.size();


        List<Shop_keeper_ordered_products> shop_keeper_ordered_products = new ArrayList<>();

        for(int i=0; i<size; i++)
        {
            shop_keeper_ordered_products.add(new Shop_keeper_ordered_products(cart_list_products.get(i)));
        }

        shop_keeper_orders.setShop_keeper_ordered_products(shop_keeper_ordered_products);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().createOrder(shop_keeper_orders);

        loader.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful())
                {
                    loader.setVisibility(View.INVISIBLE);
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    loader.setVisibility(View.INVISIBLE);
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loader.setVisibility(View.INVISIBLE);
                Toast.makeText(ConfirmFinalOrderActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
