package com.shopKPR.homeComponents.floatingComponents.gifts;

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
import com.shopKPR.homeComponents.floatingComponents.entities.Gifts;
import java.util.List;

public class AllGiftsAdapter extends RecyclerView.Adapter<AllGiftsAdapter.ViewHolder>{

    private final Context context;
    private final List<Gifts> giftsList;

    public AllGiftsAdapter(Context context, List<Gifts> giftsList) {
        this.context = context;
        this.giftsList = giftsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.gifts_sample, parent, false);
        return new AllGiftsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(HostAddress.HOST_ADDRESS.getHostAddress() + giftsList.get(position).getGiftImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.giftImage);

        holder.giftDetails.setText(giftsList.get(position).getGiftDetails());
    }

    @Override
    public int getItemCount() {
        return giftsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView giftImage;
        private final TextView giftDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            giftImage = itemView.findViewById(R.id.giftImage);
            giftDetails = itemView.findViewById(R.id.giftsDetails);
        }
    }
}
