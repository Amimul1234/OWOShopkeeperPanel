package com.shopKPR.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.R;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.products.ProductDetailsActivity;

import org.jetbrains.annotations.NotNull;

public class SearchedAdapter extends PagedListAdapter<OwoProduct, RecyclerView.ViewHolder>{

    private final Context mCtx;

    public SearchedAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.product_availability_sample, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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

            DisplayMetrics displaymetrics = new DisplayMetrics(); //Setting things dynamically
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 2;
            int deviceheight = displaymetrics.heightPixels / 3;

            itemView.getLayoutParams().width = devicewidth - 15;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            OwoProduct owoproduct = getItem(getBindingAdapterPosition());

            Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
            intent.putExtra("Products", owoproduct);
            mCtx.startActivity(intent);
        }
    }

}
