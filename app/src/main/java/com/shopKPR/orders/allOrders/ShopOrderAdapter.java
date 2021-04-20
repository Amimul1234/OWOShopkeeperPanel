package com.shopKPR.orders.allOrders;

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
import com.shopKPR.Model.ShopKeeperOrders;
import com.shopKPR.R;
import com.shopKPR.orders.orderItem.OrderedItemDetails;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class ShopOrderAdapter extends PagedListAdapter<ShopKeeperOrders, RecyclerView.ViewHolder> {

    private final Context mCtx;

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

        ShopKeeperOrders shopKeeperOrders = getItem(position);

        if (shopKeeperOrders != null)
        {
            itemViewHolder.order_number.setText(String.valueOf(shopKeeperOrders.getOrder_number()));
            itemViewHolder.order_status.setText(shopKeeperOrders.getShipping_state());

            Double total_with_discount = shopKeeperOrders.getTotal_amount() - shopKeeperOrders.getCoupon_discount();

            String totalWithDiscountString =  "৳ "+String.format(Locale.ENGLISH, "%.2f",total_with_discount);
            String orderDateAndTimeString = shopKeeperOrders.getDate()+", "+ shopKeeperOrders.getOrder_time();

            itemViewHolder.order_total_price.setText(totalWithDiscountString);
            itemViewHolder.order_date_and_time.setText(orderDateAndTimeString);

        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
        }

    }

    private static final DiffUtil.ItemCallback<ShopKeeperOrders> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ShopKeeperOrders>() {
                @Override
                public boolean areItemsTheSame(ShopKeeperOrders oldItem, ShopKeeperOrders newItem) {
                    return oldItem.getOrder_number() == newItem.getOrder_number();
                }

                @Override
                public boolean areContentsTheSame(@NotNull ShopKeeperOrders oldItem, @NotNull ShopKeeperOrders newItem) {
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
        public void onClick(View v)
        {
            ShopKeeperOrders shopKeeperOrders = getItem(getBindingAdapterPosition());

            Intent intent = new Intent(mCtx, OrderedItemDetails.class);
            intent.putExtra("Order", shopKeeperOrders);
            mCtx.startActivity(intent);
        }
    }



}
