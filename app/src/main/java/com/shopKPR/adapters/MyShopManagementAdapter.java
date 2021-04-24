package com.shopKPR.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.shopKPR.R;
import com.shopKPR.myShopManagement.userDebts.debt.DebtDetailsDashBoard;
import java.util.Arrays;
import java.util.List;

public class MyShopManagementAdapter extends RecyclerView.Adapter<MyShopManagementAdapter.ViewHolder> {

    private final Context mCtx;
    private final List<String> names;

    private final int[] images = {R.drawable.shop_statictics, R.drawable.add_new_product_to_shop, R.drawable.view_shop_products, R.drawable.add_offer,
            R.drawable.view_offers, R.drawable.shop_management_order, R.drawable.completed_orders, R.drawable.due_record, R.drawable.loan_khata};

    public MyShopManagementAdapter(Context mCtx) {
        this.mCtx = mCtx;
        names = Arrays.asList(mCtx.getResources().getStringArray(R.array.shop_management));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.shop_management_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.management_image.setImageResource(images[position]);
        holder.management_name.setText(names.get(position));

        holder.itemView.setOnClickListener(v -> {
            if(position == 0)
            {
                Toast.makeText(mCtx, "Feature coming soon...", Toast.LENGTH_SHORT).show();
            }
            else if(position == 1)
            {
                Toast.makeText(mCtx, "Feature coming soon...", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mCtx, AddProductToMyShop.class);
//                mCtx.startActivity(intent);
            }
            else if(position == 2)
            {
                Toast.makeText(mCtx, "Feature coming soon...", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mCtx, ViewAvailableProductToSell.class);
//                mCtx.startActivity(intent);
            }
            else if(position == 3)
            {
                Toast.makeText(mCtx, "Feature coming soon...", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mCtx, AddANewOffer.class);
//                mCtx.startActivity(intent);
            }
            else if(position == 4)
            {
                Toast.makeText(mCtx, "Feature coming soon...", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mCtx, ViewOffers.class);
//                mCtx.startActivity(intent);
            }
            else if(position == 5)
            {
                Toast.makeText(mCtx, "Feature coming soon...", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mCtx, PendingOrders.class);
//                mCtx.startActivity(intent);
            }
            else if(position == 6){
                Toast.makeText(mCtx, "Feature coming soon...", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(mCtx, CompletedOrders.class);
//                mCtx.startActivity(intent);
            }
            else if(position == 7)
            {
                Intent intent = new Intent(mCtx, DebtDetailsDashBoard.class);
                mCtx.startActivity(intent);
            }
            else
            {

            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView management_image;
        private final TextView management_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            management_image = itemView.findViewById(R.id.management_image);
            management_name  = itemView.findViewById(R.id.management_name);
        }
    }

}
