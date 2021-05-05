package com.shopKPR.orderConfirmation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.shopKPR.Model.Shops;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.CartListProduct;
import com.shopKPR.Model.Shop_keeper_ordered_products;
import com.shopKPR.Model.ShopKeeperOrders;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.R;
import org.jetbrains.annotations.NotNull;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmFinalOrderActivity extends AppCompatActivity
{
    private EditText phoneEditText, delivery_address, additional_comments;
    private RadioGroup radioGroup;
    private double grand_total_price;
    private double discount;
    private ProgressBar loader;
    private ArrayList<CartListProduct> CartListProducts;
    private Spinner timeSlotSelector, dateSpinner;
    private final List<String> availableTimeSlots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        timeSlotSelector = findViewById(R.id.time_slots);
        dateSpinner = findViewById(R.id.date_spinner);
        Button confirmOrderButton = findViewById(R.id.confirm_final_order_btn);
        phoneEditText = findViewById(R.id.shipment_phone_number);
        delivery_address = findViewById(R.id.delivery_address);
        additional_comments = findViewById(R.id.additional_comments);
        radioGroup = findViewById(R.id.radio_group);
        loader = findViewById(R.id.loader);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date today = new Date();
        Date tomorrow = new Date(today.getTime() + ( 2 * 1000 * 60 * 60 * 24));

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("Today");
        spinnerArray.add("Tomorrow");
        spinnerArray.add(dateFormat.format(tomorrow));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter);


        grand_total_price = Double.parseDouble(getIntent().getStringExtra("grand_total"));
        CartListProducts = (ArrayList<CartListProduct>) getIntent().getSerializableExtra("CartListProducts");
        discount = Double.parseDouble(getIntent().getStringExtra("discount"));

        getTimeSlots();
        getDefaultAddress();

        phoneEditText.setText(Prevalent.currentOnlineUser.getMobileNumber());

        confirmOrderButton.setOnClickListener(v -> CheckValue(CartListProducts));
    }

    private void getDefaultAddress() {
        RetrofitClient.getInstance().getApi()
                .getShopInfo(Prevalent.currentOnlineUser.getMobileNumber())
                .enqueue(new Callback<Shops>() {
                    @Override
                    public void onResponse(@NotNull Call<Shops> call, @NotNull Response<Shops> response)
                    {
                        if(response.isSuccessful())
                        {
                            Shops shops = response.body();
                            assert shops != null;
                            delivery_address.setText(shops.getShop_address());
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Shops> call, @NotNull Throwable t) {

                    }
                });
    }

    private void getTimeSlots()
    {
        loader.setVisibility(View.VISIBLE);

        RetrofitClient.getInstance().getApi()
                .getAllAvailableTimeSlots()
                .enqueue(new Callback<List<TimeSlot>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<TimeSlot>> call, @NotNull Response<List<TimeSlot>> response) {
                        if(response.isSuccessful())
                        {
                            loader.setVisibility(View.GONE);

                            List<TimeSlot> timeSlotList = response.body();

                            assert timeSlotList != null;
                            for(TimeSlot timeSlot : timeSlotList)
                            {
                                availableTimeSlots.add(timeSlot.getTimeSlotString());
                            }

                            ArrayAdapter<String> timeSlotAdapter = new ArrayAdapter<>(
                                    ConfirmFinalOrderActivity.this, android.R.layout.simple_spinner_item, availableTimeSlots);
                            timeSlotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            timeSlotSelector.setAdapter(timeSlotAdapter);
                        }
                        else
                        {
                            loader.setVisibility(View.GONE);
                            Toast.makeText(ConfirmFinalOrderActivity.this, "Can not get time slots", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<TimeSlot>> call, @NotNull Throwable t) {
                        loader.setVisibility(View.GONE);
                        Log.e("TimeSlots", t.getMessage());
                        Toast.makeText(ConfirmFinalOrderActivity.this, "Can not get time slots", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void CheckValue(List<CartListProduct> CartListProducts) {

        if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            phoneEditText.setError("Please enter your mobile number");
            phoneEditText.requestFocus();
        }
        else if(TextUtils.isEmpty(delivery_address.getText().toString()))
        {
            delivery_address.setError("Please enter the delivery address");
            delivery_address.requestFocus();
        }
        else
        {
            loader.setVisibility(View.VISIBLE);
            ConfirmOrder(CartListProducts);
        }
    }

    private void ConfirmOrder(List<CartListProduct> CartListProducts)
    {
        final String saveCurrentDate, saveCurrentTime;

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        saveCurrentDate = currentDate.format(callForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
        saveCurrentTime = currentTime.format(callForDate.getTime());

        ShopKeeperOrders shopKeeperOrders = new ShopKeeperOrders();

        shopKeeperOrders.setAdditional_comments(additional_comments.getText().toString());
        shopKeeperOrders.setCoupon_discount(discount);
        shopKeeperOrders.setDate(saveCurrentDate);
        shopKeeperOrders.setDelivery_address(delivery_address.getText().toString());

        if(radioGroup.getCheckedRadioButtonId() == R.id.delivery_option_1)
        {
            shopKeeperOrders.setMethod("Cash on delivery");
        }
        else
        {
            shopKeeperOrders.setMethod("Digital payment");
        }

        shopKeeperOrders.setReceiver_phone(phoneEditText.getText().toString());
        shopKeeperOrders.setShop_phone(Prevalent.currentOnlineUser.getMobileNumber());
        shopKeeperOrders.setShipping_state("Pending");
        shopKeeperOrders.setTime_slot(dateSpinner.getSelectedItem().toString() + " - " +timeSlotSelector.getSelectedItem().toString());
        shopKeeperOrders.setOrder_time(saveCurrentTime);
        shopKeeperOrders.setTotal_amount(grand_total_price);

        int size = CartListProducts.size();

        List<Shop_keeper_ordered_products> shop_keeper_ordered_products = new ArrayList<>();

        for(int i=0; i<size; i++)
        {
            shop_keeper_ordered_products.add(new Shop_keeper_ordered_products(CartListProducts.get(i)));
        }

        shopKeeperOrders.setShop_keeper_ordered_products(shop_keeper_ordered_products);

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().createOrder(shopKeeperOrders,
                Prevalent.currentOnlineUser.getMobileNumber());

        loader.setVisibility(View.VISIBLE);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response)
            {
                if(response.isSuccessful())
                {
                    loader.setVisibility(View.INVISIBLE);
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Order placed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    loader.setVisibility(View.INVISIBLE);
                    Toast.makeText(ConfirmFinalOrderActivity.this, "Failed to place order, please try again!!!",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                loader.setVisibility(View.INVISIBLE);
                Log.e("ConfirmOrder", t.getMessage());
                Toast.makeText(ConfirmFinalOrderActivity.this, "Failed to place order, please try again!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
