package com.shopKPR.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shopKPR.Model.Shop_keeper_orders;
import com.shopKPR.R;
import com.shopKPR.shopKeeperPanel.Order_details_for_single_item;

import org.jetbrains.annotations.NotNull;

public class ShopOrderAdapter extends PagedListAdapter<Shop_keeper_orders, RecyclerView.ViewHolder> {

    private Context mCtx;

    public ShopOrderAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.order_card, parent, false);
        return new ShopOrderAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ShopOrderAdapter.ItemViewHolder itemViewHolder = (ShopOrderAdapter.ItemViewHolder) holder;

        Shop_keeper_orders shop_keeper_orders = getItem(position);

        if (shop_keeper_orders != null) {
            itemViewHolder.order_number.setText(String.valueOf(shop_keeper_orders.getOrder_number()));
            itemViewHolder.order_status.setText(shop_keeper_orders.getShipping_state());

            Double total_with_discount = shop_keeper_orders.getTotal_amount() - shop_keeper_orders.getCoupon_discount();

            itemViewHolder.order_total_price.setText("৳ "+String.format("%.2f",total_with_discount));
            itemViewHolder.order_date_and_time.setText(shop_keeper_orders.getDate()+", "+shop_keeper_orders.getOrder_time());

        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
        }

    }

    private static final DiffUtil.ItemCallback<Shop_keeper_orders> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Shop_keeper_orders>() {
                @Override
                public boolean areItemsTheSame(Shop_keeper_orders oldItem, Shop_keeper_orders newItem) {
                    return oldItem.getOrder_number() == newItem.getOrder_number();
                }

                @Override
                public boolean areContentsTheSame(@NotNull Shop_keeper_orders oldItem, @NotNull Shop_keeper_orders newItem) {
                    return true;
                }
            };


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView order_number, order_status, order_total_price, order_date_and_time;

        public ItemViewHolder(View itemView) {
            super(itemView);

            order_number = itemView.findViewById(R.id.order_number);
            order_status = itemView.findViewById(R.id.order_status);
            order_total_price = itemView.findViewById(R.id.order_total_price);
            order_date_and_time = itemView.findViewById(R.id.order_date_and_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Shop_keeper_orders shop_keeper_orders = getItem(getBindingAdapterPosition());
            Intent intent = new Intent(mCtx, Order_details_for_single_item.class);
            intent.putExtra("Order", shop_keeper_orders);
            mCtx.startActivity(intent);
        }
    }



}
