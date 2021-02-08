package com.owoShopKeeperPanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.myShopRelated.AddANewOffer;
import com.owoShopKeeperPanel.myShopRelated.AddProductToMyShop;
import com.owoShopKeeperPanel.myShopRelated.CompletedOrders;
import com.owoShopKeeperPanel.myShopRelated.PendingOrders;
import com.owoShopKeeperPanel.myShopRelated.debt.UserDebtDetails;
import com.owoShopKeeperPanel.myShopRelated.ViewAvailableProductToSell;
import com.owoShopKeeperPanel.myShopRelated.ViewOffers;
import java.util.Arrays;
import java.util.List;

public class MyShopManagementAdapter extends RecyclerView.Adapter<MyShopManagementAdapter.ViewHolder> {

    private Context mCtx;
    private List<String> names;

    private int[] images = {R.drawable.shop_statictics, R.drawable.add_new_product_to_shop, R.drawable.view_shop_products, R.drawable.add_offer,
            R.drawable.view_offers, R.drawable.shop_management_order, R.drawable.completed_orders, R.drawable.due_record};

    public MyShopManagementAdapter(Context mCtx) {
        this.mCtx = mCtx;
        names = Arrays.asList(mCtx.getResources().getStringArray(R.array.shop_management));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.shop_management_sample, parent, false);
        return new MyShopManagementAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.management_image.setImageResource(images[position]);
        holder.management_name.setText(names.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0)
                {

                }
                else if(position == 1)
                {
                    Intent intent = new Intent(mCtx, AddProductToMyShop.class);
                    mCtx.startActivity(intent);
                }
                else if(position == 2)
                {
                    Intent intent = new Intent(mCtx, ViewAvailableProductToSell.class);
                    mCtx.startActivity(intent);
                }
                else if(position == 3)
                {
                    Intent intent = new Intent(mCtx, AddANewOffer.class);
                    mCtx.startActivity(intent);
                }
                else if(position == 4)
                {
                    Intent intent = new Intent(mCtx, ViewOffers.class);
                    mCtx.startActivity(intent);
                }
                else if(position == 5)
                {
                    Intent intent = new Intent(mCtx, PendingOrders.class);
                    mCtx.startActivity(intent);
                }
                else if(position == 6){
                    Intent intent = new Intent(mCtx, CompletedOrders.class);
                    mCtx.startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(mCtx, UserDebtDetails.class);
                    mCtx.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView management_image;
        private TextView management_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            management_image = itemView.findViewById(R.id.management_image);
            management_name  = itemView.findViewById(R.id.management_name);
        }
    }

}
