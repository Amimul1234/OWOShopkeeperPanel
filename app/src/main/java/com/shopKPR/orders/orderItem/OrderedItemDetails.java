package com.shopKPR.orders.orderItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.badoualy.stepperindicator.StepperIndicator;
import com.shopKPR.Model.Shop_keeper_ordered_products;
import com.shopKPR.Model.ShopKeeperOrders;
import com.shopKPR.R;
import java.util.List;
import java.util.Locale;

public class OrderedItemDetails extends AppCompatActivity {
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details_for_single_item);

        ShopKeeperOrders shopKeeperOrders = (ShopKeeperOrders) getIntent().getSerializableExtra("Order");

        assert shopKeeperOrders != null;
        List<Shop_keeper_ordered_products> shop_keeper_ordered_products = shopKeeperOrders.getShop_keeper_ordered_products();

        TextView orderNumber = findViewById(R.id.order_number);
        TextView orderDate = findViewById(R.id.order_date);
        StepperIndicator stepperIndicator = findViewById(R.id.steps);
        RecyclerView orderedProducts = findViewById(R.id.ordered_products);
        TextView totalTaka = findViewById(R.id.total_taka);
        TextView discountTaka = findViewById(R.id.discount_taka);
        TextView subTotal = findViewById(R.id.sub_total);
        TextView shippingAddress = findViewById(R.id.shipping_address);
        TextView mobileNumber = findViewById(R.id.mobile_number);
        ImageView backButton = findViewById(R.id.back_from_order_details);
        TextView shippingMethod = findViewById(R.id.shipping_method);
        TextView additionalComments = findViewById(R.id.additional_comments);

        Ordered_item_adapter adapter = new Ordered_item_adapter(this, shop_keeper_ordered_products);

        orderedProducts.setLayoutManager(new LinearLayoutManager(this));
        orderedProducts.setHasFixedSize(true);
        orderedProducts.setAdapter(adapter);

        Double sub_total_taka = shopKeeperOrders.getTotal_amount() - shopKeeperOrders.getCoupon_discount();
        String orderNumberString = "#"+ shopKeeperOrders.getOrder_number();
        String totalTakaString = "৳ "+String.format(Locale.ENGLISH, "%.2f", shopKeeperOrders.getTotal_amount());
        String discountTakaString = "৳ " + shopKeeperOrders.getCoupon_discount();
        String subTotalTakaString = "৳ " + String.format(Locale.ENGLISH, "%.2f", sub_total_taka);

        orderNumber.setText(orderNumberString);
        orderDate.setText(shopKeeperOrders.getDate());
        totalTaka.setText(totalTakaString);
        discountTaka.setText(discountTakaString);
        subTotal.setText(subTotalTakaString);

        shippingAddress.setText(shopKeeperOrders.getDelivery_address());
        mobileNumber.setText(shopKeeperOrders.getReceiver_phone());

        shippingMethod.setText(shopKeeperOrders.getMethod());
        additionalComments.setText(shopKeeperOrders.getAdditional_comments());

        stepperIndicator.setLabels(new String[]{"Pending", "Confirmed", "Processing", "Picked", "Shipped", "Delivered"});

        String[] states = new String[7];

        states[0] = "Pending";
        states[1] = "Confirmed";
        states[2] = "Processing";
        states[3] = "Picked";
        states[4] = "Shipped";
        states[5] = "Delivered";
        states[6] = "Cancelled";

        for(int i=0; i<6; i++)
        {
            if(states[i].equals(shopKeeperOrders.getShipping_state()))
            {
                stepperIndicator.setCurrentStep(i+1);
                stepperIndicator.setShowDoneIcon(true);
                break;
            }
        }

        if(states[6].equals(shopKeeperOrders.getShipping_state()))
        {
            stepperIndicator.setCurrentStep(6);
            stepperIndicator.setDoneIcon(getResources().getDrawable(R.drawable.ic_baseline_cancel_24));
        }

        backButton.setOnClickListener(v -> onBackPressed());
    }
}