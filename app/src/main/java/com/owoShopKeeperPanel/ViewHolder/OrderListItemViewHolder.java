package com.owoShopKeeperPanel.ViewHolder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.owoShopKeeperPanel.R;

public class OrderListItemViewHolder extends RecyclerView.ViewHolder{

    public TextView order_number, order_status, total_amount_with_discount, time_and_date;

    public OrderListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        order_number = itemView.findViewById(R.id.order_number);
        order_status = itemView.findViewById(R.id.order_status);
        total_amount_with_discount = itemView.findViewById(R.id.order_total_price);
        time_and_date = itemView.findViewById(R.id.order_date_and_time);
    }
}