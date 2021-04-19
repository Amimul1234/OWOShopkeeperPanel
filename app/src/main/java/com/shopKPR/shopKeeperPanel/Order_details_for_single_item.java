package com.shopKPR.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.badoualy.stepperindicator.StepperIndicator;
import com.shopKPR.Model.Shop_keeper_ordered_products;
import com.shopKPR.Model.Shop_keeper_orders;
import com.shopKPR.R;
import com.shopKPR.adapters.Ordered_item_adapter;
import java.util.List;

public class Order_details_for_single_item extends AppCompatActivity {

    private TextView order_number, order_date;
    private StepperIndicator stepperIndicator;
    private RecyclerView ordered_products;
    private TextView total_taka, discount_taka,
            sub_total, shipping_address, mobile_number, shipping_method, additonal_comments;

    private ImageView back_button;

    private Shop_keeper_orders shop_keeper_orders;

    private List<Shop_keeper_ordered_products> shop_keeper_ordered_products;


    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_for_single_item);

        shop_keeper_orders = (Shop_keeper_orders) getIntent().getSerializableExtra("Order");

        assert shop_keeper_orders != null;
        shop_keeper_ordered_products = shop_keeper_orders.getShop_keeper_ordered_products();

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
        shipping_method = findViewById(R.id.shipping_method);
        additonal_comments = findViewById(R.id.additional_comments);

        Ordered_item_adapter adapter = new Ordered_item_adapter(this, shop_keeper_ordered_products);

        ordered_products.setLayoutManager(new LinearLayoutManager(this));
        ordered_products.setHasFixedSize(true);
        ordered_products.setAdapter(adapter);

        order_number.setText("#"+shop_keeper_orders.getOrder_number());
        order_date.setText(shop_keeper_orders.getDate());
        total_taka.setText("৳ "+String.format("%.2f", shop_keeper_orders.getTotal_amount()));
        discount_taka.setText("৳ "+String.valueOf(shop_keeper_orders.getCoupon_discount()));

        Double sub_total_taka = shop_keeper_orders.getTotal_amount() - shop_keeper_orders.getCoupon_discount();
        sub_total.setText("৳ "+String.format("%.2f", sub_total_taka));

        shipping_address.setText(shop_keeper_orders.getDelivery_address());
        mobile_number.setText(shop_keeper_orders.getReceiver_phone());

        shipping_method.setText(shop_keeper_orders.getMethod());
        additonal_comments.setText(shop_keeper_orders.getAdditional_comments());

        stepperIndicator.setLabels(new String[]{"Pending", "Confirmed", "Processing", "Picked", "Shipped", "Delivered"});

        String[] states = new String[7];

        states[0] = "Pending";
        states[1] = "Confirmed";
        states[2] = "Processing";
        states[3] = "Picked";
        states[4] = "Shipped";
        states[5] = "Delivered";
        states[6] = "Cancelled";

        int counter = 0;

        for(int i=0; i<6; i++)
        {
            if(states[i].equals(shop_keeper_orders.getShipping_state()))
            {
                stepperIndicator.setCurrentStep(i+1);
                stepperIndicator.setShowDoneIcon(true);
                break;
            }
        }

        if(states[6].equals(shop_keeper_orders.getShipping_state()))
        {
            stepperIndicator.setCurrentStep(6);
            stepperIndicator.setDoneIcon(getResources().getDrawable(R.drawable.ic_baseline_cancel_24));
        }

        back_button.setOnClickListener(v -> onBackPressed());
    }
}