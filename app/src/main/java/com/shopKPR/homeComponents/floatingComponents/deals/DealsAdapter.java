package com.shopKPR.homeComponents.floatingComponents.deals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPR.R;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.homeComponents.floatingComponents.entities.Deals;
import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.ViewHolder> {

    private final Context mctx;
    private final List<Deals> dealsList;

    public DealsAdapter(Context mctx, List<Deals> dealsList) {
        this.mctx = mctx;
        this.dealsList = dealsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.gifts_sample, parent, false);
        return new DealsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mctx).load(HostAddress.HOST_ADDRESS.getHostAddress() + dealsList.get(position).getDealImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.dealImage);

        holder.dealDetails.setText(dealsList.get(position).getDealDetails());
    }

    @Override
    public int getItemCount() {
        return dealsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView dealImage;
        private final TextView dealDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dealImage = itemView.findViewById(R.id.giftImage);
            dealDetails = itemView.findViewById(R.id.giftsDetails);
        }
    }
}
