package com.owoShopKeeperPanel.adapters;

import android.app.Activity;
import android.content.DialogInterface;
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
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.Model.Cart_list_product;
import com.owoShopKeeperPanel.Model.Owo_product;
import com.owoShopKeeperPanel.Prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.shopKeeperPanel.CartActivity;
import com.owoShopKeeperPanel.shopKeeperPanel.ProductDetailsActivity;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartListAdapter extends ArrayAdapter<Cart_list_product> {
    private Activity context;
    private List<Cart_list_product> cart_list_products;
    private CartActivity cartActivity;

    public CartListAdapter(Activity context, List<Cart_list_product> cart_list_products){
        super(context, R.layout.cart_items_sample, cart_list_products);
        this.context=context;
        this.cart_list_products = cart_list_products;
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartActivity.loaderVisible();

                Call<Owo_product> call = RetrofitClient.getInstance().getApi().getProductById(cart_list_products.get(position).getProduct_id());

                call.enqueue(new Callback<Owo_product>() {
                    @Override
                    public void onResponse(@NotNull Call<Owo_product> call, @NotNull Response<Owo_product> response) {
                        if(response.code() == 200)
                        {
                            cartActivity.loaderGone();
                            Intent intent = new Intent(context, ProductDetailsActivity.class);
                            intent.putExtra("Products", (Serializable) response.body());
                            context.startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(context, "Server error", Toast.LENGTH_SHORT).show();
                            cartActivity.loaderGone();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Owo_product> call, @NotNull Throwable t) {
                        Log.e("Error", t.getMessage());
                    }
                });
            }
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

            Cart_list_product cart_list_product = cart_list_products.get(position);

            Glide.with(context).load(cart_list_product.getProduct_image()).into(cart_product_image);

            cart_product_name.setText(cart_list_product.getProduct_name());
            cart_product_quantity.setText("৳ "+String.valueOf(cart_list_product.getProduct_price())+" × "+String.valueOf(cart_list_product.getProduct_quantity()));
            double product_total_price = cart_list_product.getProduct_price() * cart_list_product.getProduct_quantity();
            cart_product_price.setText("৳ "+String.format("%.2f", product_total_price));
            cart_item_change_button.setNumber(String.valueOf(cart_list_product.getProduct_quantity()));

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteItem(position);
                }
            });

            cart_item_change_button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
                @Override
                public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                    changeQuantity(position, newValue);
                }
            });
        }

    }


    private void changeQuantity(int position, int newValue) {

        cartActivity.loaderVisible();

        cart_list_products.get(position).setProduct_quantity(newValue);
        Cart_list_product cart_list_product = cart_list_products.get(position);

        Call<Cart_list_product> call = RetrofitClient.getInstance().getApi().updateCartList(cart_list_product, Prevalent.currentOnlineUser.getPhone());

        call.enqueue(new Callback<Cart_list_product>() {
            @Override
            public void onResponse(@NotNull Call<Cart_list_product> call, @NotNull Response<Cart_list_product> response) {
                if(response.isSuccessful())
                {
                    cart_list_products.set(position, response.body());

                    cartActivity.setGrand_total(0.0);

                    double grand_total = 0.0;

                    for(Cart_list_product cart_list_product : cart_list_products)
                    {
                        grand_total += cart_list_product.getProduct_price() * cart_list_product.getProduct_quantity();
                    }

                    cartActivity.grand_total_updater(String.format("%.2f", grand_total));

                    cartActivity.loaderGone();
                    notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    cartActivity.loaderGone();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Cart_list_product> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                cartActivity.loaderGone();
            }
        });
    }

    private void onDeleteItem(int position) {
        Cart_list_product cart_list_product = cart_list_products.get(position);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setMessage("Are you sure you want to remove this item from cart ?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        cartActivity.loaderVisible();

                        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().delete_product_from_cart(cart_list_product.getProduct_id(), Prevalent.currentOnlineUser.getPhone());
                        call.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                if(response.isSuccessful())
                                {
                                    cart_list_products.remove(cart_list_product);

                                    notifyDataSetChanged();

                                    if(cart_list_products.isEmpty())
                                    {
                                        cartActivity.getEmpty_image().setVisibility(View.VISIBLE);
                                        cartActivity.empty_text_setter("No item in cart");
                                        cartActivity.getTag4().setVisibility(View.INVISIBLE);
                                    }

                                    cartActivity.setGrand_total(0.0);

                                    double grand_total = 0.0;

                                    for(Cart_list_product cart_list_product : cart_list_products)
                                    {
                                        grand_total += cart_list_product.getProduct_price() * cart_list_product.getProduct_quantity();
                                    }

                                    cartActivity.grand_total_updater(String.format("%.2f", grand_total));

                                    cartActivity.loaderGone();
                                    cartActivity.loaderGone();
                                }
                                else
                                {
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
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}