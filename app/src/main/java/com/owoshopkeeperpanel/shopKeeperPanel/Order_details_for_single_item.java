package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.badoualy.stepperindicator.StepperIndicator;
import com.owoshopkeeperpanel.Model.Ordered_products;
import com.owoshopkeeperpanel.Model.Ordered_products_model;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.Ordered_item_adapter;

import java.util.List;

public class Order_details_for_single_item extends AppCompatActivity {

    private TextView order_number, order_date;
    private StepperIndicator stepperIndicator;
    private RecyclerView ordered_products;
    private TextView total_taka, discount_taka,
            sub_total, shipping_address, mobile_number;

    private ImageView back_button;

    private Ordered_products_model ordered_products_model;

    private List<Ordered_products> ordered_products_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_for_single_item);

        ordered_products_model = (Ordered_products_model) getIntent().getSerializableExtra("Order");

        ordered_products_list = ordered_products_model.getProduct_ids();

        order_number = findViewById(R.id.order_number);
        order_date = findViewById(R.id.order_date);
        stepperIndicator = findViewById(R.id.steps); //For indicating shop_management_order status
        ordered_products = findViewById(R.id.ordered_products);
        total_taka = findViewById(R.id.total_taka);
        discount_taka = findViewById(R.id.discount_taka);
        sub_total = findViewById(R.id.sub_total);
        shipping_address = findViewById(R.id.shipping_address);
        mobile_number = findViewById(R.id.mobile_number);
        back_button = findViewById(R.id.back_from_order_details);

        Ordered_item_adapter adapter = new Ordered_item_adapter(this, ordered_products_list);
        ordered_products.setLayoutManager(new LinearLayoutManager(this));
        ordered_products.setHasFixedSize(true);
        ordered_products.setAdapter(adapter);

        order_number.setText("#"+ordered_products_model.getOrder_number());
        order_date.setText(ordered_products_model.getDate());
        total_taka.setText("৳ "+ordered_products_model.getTotalAmount());
        discount_taka.setText("৳ "+String.valueOf(ordered_products_model.getCoupon_discount()));
        Double sub_total_taka = Double.parseDouble(ordered_products_model.getTotalAmount()) - ordered_products_model.getCoupon_discount();
        sub_total.setText("৳ "+String.valueOf(sub_total_taka));
        shipping_address.setText(ordered_products_model.getDelivery_address());
        mobile_number.setText(ordered_products_model.getReceiver_phone());

        stepperIndicator.setLabels(new String[]{"Pending", "Confirmed", "Processing", "Picked", "Shipped", "Delivered"});

        String[] states = new String[6];

        states[0] = "Pending";
        states[1] = "Confirmed";
        states[2] = "Processing";
        states[3] = "Picked";
        states[4] = "Shipped";
        states[5] = "Delivered";

        int counter = 0;

        for(int i=0; i<6; i++)
        {
            if(states[i].equals(ordered_products_model.getState()))
            {
                stepperIndicator.setCurrentStep(i+1);
                break;
            }
        }

        stepperIndicator.setShowDoneIcon(true);


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}