package com.shopKPR.shopCart;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.shopKPR.network.RetrofitClient;
import com.shopKPR.Model.CartListProduct;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.configurations.HostAddress;
import com.shopKPR.prevalent.Prevalent;
import com.shopKPR.R;
import com.shopKPR.products.ProductDetailsActivity;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListAdapter extends ArrayAdapter<CartListProduct> {

    private final Activity context;
    private final List<CartListProduct> CartListProducts;
    private final CartActivity cartActivity;

    public CartListAdapter(Activity context, List<CartListProduct> CartListProducts){
        super(context, R.layout.cart_items_sample, CartListProducts);
        this.context=context;
        this.CartListProducts = CartListProducts;
        cartActivity = (CartActivity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater= context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.cart_items_sample,null,true);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.setPosition(position);
        holder.bindViews();

        convertView.setOnClickListener(v -> {

            cartActivity.loaderVisible();

            Call<OwoProduct> call = RetrofitClient.getInstance().getApi().getProductById(CartListProducts
                    .get(position).getProductId());

            call.enqueue(new Callback<OwoProduct>() {
                @Override
                public void onResponse(@NotNull Call<OwoProduct> call, @NotNull Response<OwoProduct> response) {
                    if(response.code() == 200)
                    {
                        cartActivity.loaderGone();
                        Intent intent = new Intent(context, ProductDetailsActivity.class);
                        intent.putExtra("Products", response.body());
                        context.startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                        cartActivity.loaderGone();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<OwoProduct> call, @NotNull Throwable t) {
                    Log.e("Error", t.getMessage());
                }
            });
        });

        return convertView;
    }

    public class ViewHolder {

        ImageView delete;
        ImageView cart_product_image;
        TextView cart_product_name;
        TextView cart_product_quantity;
        TextView cart_product_price;
        ElegantNumberButton cart_item_change_button;
        int position;

        public ViewHolder(View view) {
            delete = view.findViewById(R.id.cart_item_delete);
            cart_product_image = view.findViewById(R.id.cart_product_image);
            cart_product_name = view.findViewById(R.id.cart_product_name);
            cart_product_quantity = view.findViewById(R.id.cart_product_quantity);
            cart_product_price = view.findViewById(R.id.cart_product_price);
            cart_item_change_button = view.findViewById(R.id.cart_item_change_btn);
        }

        public void setPosition(int position) {
            this.position = position;
        }


        public void bindViews() {

            CartListProduct cartListProduct = CartListProducts.get(position);

            Glide.with(context).load(HostAddress.HOST_ADDRESS.getHostAddress()+cartListProduct.getProductImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).timeout(6000).into(cart_product_image);

            cart_product_name.setText(cartListProduct.getProductName());
            cart_product_quantity.setText("৳ "+String.valueOf(cartListProduct.getProductPrice())+" × "+String.valueOf(cartListProduct.getProductQuantity()));
            double product_total_price = cartListProduct.getProductPrice() * cartListProduct.getProductQuantity();
            cart_product_price.setText("৳ "+String.format("%.2f", product_total_price));
            cart_item_change_button.setNumber(String.valueOf(cartListProduct.getProductQuantity()));

            delete.setOnClickListener(view -> onDeleteItem(position));

            cart_item_change_button.setOnValueChangeListener((view, oldValue, newValue) -> changeQuantity(position, newValue));
        }

    }


    private void changeQuantity(int position, int newValue)
    {

        cartActivity.loaderVisible();

        CartListProducts.get(position).setProductQuantity(newValue);
        CartListProduct cartListProduct = CartListProducts.get(position);

        Call<CartListProduct> call = RetrofitClient.getInstance().getApi().updateCartList(cartListProduct, Prevalent.currentOnlineUser.getMobileNumber());

        call.enqueue(new Callback<CartListProduct>() {
            @Override
            public void onResponse(@NotNull Call<CartListProduct> call, @NotNull Response<CartListProduct> response) {
                if(response.isSuccessful())
                {
                    CartListProducts.set(position, response.body());

                    cartActivity.setGrandTotal(0.0);

                    double grand_total = 0.0;

                    for(CartListProduct cartListProduct : CartListProducts)
                    {
                        grand_total += cartListProduct.getProductPrice() * cartListProduct.getProductQuantity();
                    }

                    cartActivity.grand_total_updater(String.format("%.2f", grand_total));

                    cartActivity.loaderGone();
                    notifyDataSetChanged();
                }
                else
                {
                    assert response.errorBody() != null;
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    cartActivity.loaderGone();
                }
            }

            @Override
            public void onFailure(@NotNull Call<CartListProduct> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                cartActivity.loaderGone();
            }
        });
    }

    private void onDeleteItem(int position)
    {
        CartListProduct cartListProduct = CartListProducts.get(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setMessage("Are you sure you want to remove this item from cart ?");
        alertDialogBuilder.setPositiveButton("yes",
                (arg0, arg1) -> {

                    cartActivity.loaderVisible();

                    Call<ResponseBody> call = RetrofitClient.getInstance().getApi().delete_product_from_cart(cartListProduct.getProductId(), Prevalent.currentOnlineUser.getMobileNumber());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                            if(response.isSuccessful())
                            {
                                CartListProducts.remove(cartListProduct);

                                notifyDataSetChanged();

                                if(CartListProducts.isEmpty())
                                {
                                    cartActivity.getEmpty_image().setVisibility(View.VISIBLE);
                                    cartActivity.empty_text_setter("No item in cart");
                                    cartActivity.getTag4().setVisibility(View.INVISIBLE);
                                }

                                cartActivity.setGrandTotal(0.0);

                                double grand_total = 0.0;

                                for(CartListProduct cartListProduct1 : CartListProducts)
                                {
                                    grand_total += cartListProduct1.getProductPrice() * cartListProduct1.getProductQuantity();
                                }

                                cartActivity.grand_total_updater(String.format("%.2f", grand_total));

                                cartActivity.loaderGone();
                                cartActivity.loaderGone();
                            }
                            else
                            {
                                assert response.errorBody() != null;
                                Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                cartActivity.loaderGone();
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            cartActivity.loaderGone();
                        }
                    });
                });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> {

        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}