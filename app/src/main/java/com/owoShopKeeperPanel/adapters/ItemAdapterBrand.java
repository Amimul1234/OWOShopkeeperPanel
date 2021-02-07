package com.owoShopKeeperPanel.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.owoShopKeeperPanel.Model.Owo_product;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.pagination.NetworkState;
import com.owoShopKeeperPanel.shopKeeperPanel.ProductDetailsActivity;

public class ItemAdapterBrand extends PagedListAdapter<Owo_product, RecyclerView.ViewHolder>{

    private Context mCtx;
    private NetworkState networkState;
    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    public ItemAdapterBrand(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TYPE_PROGRESS)
        {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.network_bond_layout, parent, false);
            return new NetworkStateItemViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.product_availability_sample, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder)
        {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            Owo_product item = getItem(position);

            if (item != null) {

                Glide.with(mCtx).load(item.getProduct_image()).into(itemViewHolder.imageView);

                itemViewHolder.txtProductName.setText(item.getProduct_name());

                itemViewHolder.txtProductPrice.setText("৳ "+item.getProduct_price());
                itemViewHolder.txtProductPrice.setPaintFlags(itemViewHolder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                itemViewHolder.txtProductPrice.setVisibility(View.VISIBLE);

                double discounted_price = item.getProduct_price() - item.getProduct_discount();

                itemViewHolder.txtProduct_discounted_price.setText("৳ "+ String.valueOf(discounted_price));


                double percentage = (item.getProduct_discount() / item.getProduct_price()) * 100.00;

                int val = (int) percentage;
                itemViewHolder.discounted_percent.setText(String.valueOf(val) + " % ");

            } else {
                Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
            }
        }

        else
        {
            ((NetworkStateItemViewHolder) holder).bindView(networkState);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }

    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private static DiffUtil.ItemCallback<Owo_product> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Owo_product>() {
                @Override
                public boolean areItemsTheSame(Owo_product oldItem, Owo_product newItem) {
                    return oldItem.getProduct_id() == newItem.getProduct_id();
                }

                @Override
                public boolean areContentsTheSame(Owo_product oldItem, Owo_product newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;
        private TextView errorMsg;

        public NetworkStateItemViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
            errorMsg = itemView.findViewById(R.id.errorMsg);
        }

        public void bindView(NetworkState networkState) {
            if (networkState != null && networkState.getStatus() == NetworkState.Status.RUNNING) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getStatus() == NetworkState.Status.FAILED) {
                errorMsg.setVisibility(View.VISIBLE);
                errorMsg.setText(networkState.getMsg());
            } else {
                errorMsg.setVisibility(View.GONE);
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtProductName, txtProductPrice, txtProduct_discounted_price, discounted_percent;
        public ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.product_image);
            txtProductName = (TextView)itemView.findViewById(R.id.product_name);
            txtProductPrice = (TextView)itemView.findViewById(R.id.product_price);
            discounted_percent = itemView.findViewById(R.id.discount_percentage);
            txtProduct_discounted_price = itemView.findViewById(R.id.product_discounted_price);

            DisplayMetrics displaymetrics = new DisplayMetrics(); //Setting things dynamically
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //if you need three fix imageview in width
            int devicewidth = displaymetrics.widthPixels / 2;

            //if you need 4-5-6 anything fix imageview in height
            int deviceheight = displaymetrics.heightPixels / 3;

            itemView.getLayoutParams().width = devicewidth;

            //if you need same height as width you can set devicewidth in holder.image_view.getLayoutParams().height
            itemView.getLayoutParams().height = deviceheight;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Owo_product owoproduct = getItem(getBindingAdapterPosition());
            Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
            intent.putExtra("Products", owoproduct);
            mCtx.startActivity(intent);
        }
    }

}
