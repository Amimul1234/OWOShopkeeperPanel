package com.shopKPR.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.R;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.pagination.NetworkState;
import com.shopKPR.products.ProductDetailsActivity;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemAdapterBrand extends PagedListAdapter<OwoProduct, RecyclerView.ViewHolder>{

    private final Context mCtx;
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

            OwoProduct item = getItem(position);

            if (item != null) {

                Glide.with(mCtx).load(HostAddress.HOST_ADDRESS.getHostAddress()+item.getProductImage())
                        .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(itemViewHolder.imageView);

                itemViewHolder.txtProductName.setText(item.getProductName());

                itemViewHolder.txtProductPrice.setText("৳ "+item.getProductPrice());
                itemViewHolder.txtProductPrice.setPaintFlags(itemViewHolder.txtProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                itemViewHolder.txtProductPrice.setVisibility(View.VISIBLE);

                double discounted_price = item.getProductPrice() - item.getProductDiscount();

                itemViewHolder.txtProduct_discounted_price.setText("৳ "+ String.valueOf(discounted_price));


                double percentage = (item.getProductDiscount() / item.getProductPrice()) * 100.00;

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
        if (hasExtraRow() && position == getItemCount()) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState != NetworkState.LOADED;
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

    private static final DiffUtil.ItemCallback<OwoProduct> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<OwoProduct>() {
                @Override
                public boolean areItemsTheSame(OwoProduct oldItem, OwoProduct newItem) {
                    return oldItem.getProductId().equals(newItem.getProductId());
                }

                @Override
                public boolean areContentsTheSame(OwoProduct oldItem, @NotNull OwoProduct newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public static class NetworkStateItemViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;
        private final TextView errorMsg;

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

            imageView = itemView.findViewById(R.id.product_image);
            txtProductName = itemView.findViewById(R.id.product_name);
            txtProductPrice = itemView.findViewById(R.id.product_price);
            discounted_percent = itemView.findViewById(R.id.discount_percentage);
            txtProduct_discounted_price = itemView.findViewById(R.id.product_discounted_price);

            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 2;
            int deviceheight = displaymetrics.heightPixels / 3;

            itemView.getLayoutParams().width = devicewidth;
            itemView.getLayoutParams().height = deviceheight;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ProgressDialog progressDialog = new ProgressDialog(mCtx);
            progressDialog.setTitle("Product Information");
            progressDialog.setMessage("Please wait while we are getting product information");
            progressDialog.show();

            Long id = Objects.requireNonNull(getItem(getBindingAdapterPosition())).getProductId();

            RetrofitClient.getInstance().getApi()
                    .getProductById(id)
                    .enqueue(new Callback<OwoProduct>() {
                        @Override
                        public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                            if(response.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
                                intent.putExtra("Products", response.body());
                                mCtx.startActivity(intent);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Log.e("Error", "Server error occurred");
                                Toast.makeText(mCtx, "Can not get product data, please try again", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                            Log.e("Error", t.getMessage());
                            progressDialog.dismiss();
                        }});
        }
    }

}
