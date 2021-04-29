package com.shopKPR.homeComponents.accountInfo.notifications;

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
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private final Context mctx;
    private final List<Notifications> notificationsList;

    public NotificationsAdapter(Context mctx, List<Notifications> notificationsList) {
        this.mctx = mctx;
        this.notificationsList = notificationsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mctx);
        View view = inflater.inflate(R.layout.notifications_sample, parent, false);
        return new NotificationsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {

        Glide.with(mctx).load(HostAddress.HOST_ADDRESS.getHostAddress() + notificationsList.get(position).getNotificationImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.notificationImage);

        holder.notificationName.setText(notificationsList.get(position).getNotificationName());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView notificationImage;
        private final TextView notificationName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationImage = itemView.findViewById(R.id.giftImage);
            notificationName = itemView.findViewById(R.id.giftsDetails);
        }
    }
}
