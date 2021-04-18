package com.owoShopKeeperPanel.wishList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.owoShopKeeperPanel.Model.OwoProduct;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.network.RetrofitClient;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.products.ProductDetailsActivity;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.owoShopKeeperPanel.configurations.HostAddress.*;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder>{

    private final Context mcTx;
    private final List<OwoProduct> owoProductList;

    public WishListAdapter(Context mcTx, List<OwoProduct> owoProductList)
    {
        this.mcTx = mcTx;
        this.owoProductList = owoProductList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Glide.with(mcTx).load(HOST_ADDRESS.getHostAddress() + owoProductList.get(position).getProductImage()).
                diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(holder.wishListProductImage);

        holder.wishListProductName.setText(owoProductList.get(position).getProductName());
        holder.wishListProductPrice.setText("৳ "+String.valueOf(owoProductList.get(position).getProductPrice()));

        holder.deleteProductFromWishList.setOnClickListener(v -> deleteProductFromWishList(position));

        holder.itemView.setOnClickListener(v ->
        {
            Intent intent = new Intent(mcTx, ProductDetailsActivity.class);
            intent.putExtra("Products", owoProductList.get(position));
            mcTx.startActivity(intent);
        });
    }

    private void deleteProductFromWishList(int position)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mcTx);

        alertDialogBuilder.setMessage("Are you sure you want to remove this item from cart ?");

        alertDialogBuilder.setPositiveButton("yes", (arg0, arg1) ->
                deleteFromWishList(owoProductList.get(position).getProductId()));

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {});

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteFromWishList(Long productId)
    {
        RetrofitClient.getInstance().getApi()
                .deleteWishListProduct(Prevalent.currentOnlineUser.getShopKeeperId(), productId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            Toast.makeText(mcTx, "Product removed from wish list", Toast.LENGTH_SHORT).show();

                            for(OwoProduct owoProduct : owoProductList)
                            {
                                if(owoProduct.getProductId().equals(productId))
                                {
                                    owoProductList.remove(owoProduct);
                                    break;
                                }
                            }

                            notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(mcTx, "Can not remove product from wish list, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("WishListAdapter", t.getMessage());
                        Toast.makeText(mcTx, "Can not remove product from wish list, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return owoProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView wishListProductImage, deleteProductFromWishList;
        TextView wishListProductName, wishListProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            wishListProductImage = itemView.findViewById(R.id.wishList_product_image);
            deleteProductFromWishList = itemView.findViewById(R.id.wishList_item_delete);
            wishListProductName = itemView.findViewById(R.id.wishList_product_name);
            wishListProductPrice = itemView.findViewById(R.id.wishList_product_price);
        }
    }
}
