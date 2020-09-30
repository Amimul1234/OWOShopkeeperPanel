package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.AllianceLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.Cart;
import com.owoshopkeeperpanel.Model.Qupon;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.CartListAdapter;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ListView listView;
    private RecyclerView.LayoutManager layoutManager;
    private Button place_order_button;
    private TextView totalAmount, subTotalAmount, vouchartxt, empty_text;
    private double totalPrice = 0;
    private double discounted_price = 0, sub_total_amount = 0;
    private ImageView back_from_cart, empty_image;
    private static AllianceLoader loader;
    private RelativeLayout tag4;

    String coupon_code_string;

    private ArrayList<Cart> cartList = new ArrayList<>();
    private CartListAdapter cartListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listView = findViewById(R.id.cart_list);

        place_order_button=(Button)findViewById(R.id.place_order_btn);
        vouchartxt=(TextView)findViewById(R.id.cart_vouchar);
        totalAmount=(TextView)findViewById(R.id.cart_total_amount);
        subTotalAmount=(TextView)findViewById(R.id.cart_sub_total_amount);
        back_from_cart=(ImageView)findViewById(R.id.back_arrow_from_cart);
        empty_image = findViewById(R.id.empty_image);
        empty_text = findViewById(R.id.empty_text);
        tag4 = findViewById(R.id.tag4);

        loader = findViewById(R.id.loader);

        loaderVisible();

        back_from_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        place_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConfirmFinalOrderActivity.class);
                intent.putExtra("Total Price", String.valueOf(totalPrice));
                intent.putExtra("products_id", cartList);
                intent.putExtra("discounted_price", String.valueOf(discounted_price));
                startActivity(intent);
                finish();
            }
        });

        vouchartxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inflateDialogueForCoupon();
            }
        });
    }

    public void inflateDialogueForCoupon() {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.get_coupon_code, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Coupon Code");

        alertDialog.setCancelable(false);

        EditText coupon_code = (EditText) view.findViewById(R.id.coupon_code);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loader.setVisibility(View.VISIBLE);
                coupon_code_string = coupon_code.getText().toString();
                check(coupon_code_string);
                dialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        cartListRef.child(Prevalent.currentOnlineUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                totalPrice = 0.0;//This two line is for handling data change
                tag4.setVisibility(View.VISIBLE);
                if(snapshot.exists())
                {
                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                    {
                        Cart dummyCart = dataSnapshot1.getValue(Cart.class);
                        cartList.add(dummyCart);
                        totalPrice += dummyCart.getProduct_price() * Double.parseDouble(dummyCart.getNeeded_quantity());
                    }

                    loaderGone();
                    cartListAdapter = new CartListAdapter(CartActivity.this, cartList);
                    listView.setAdapter(cartListAdapter);
                    totalAmount.setText("৳ "+String.valueOf(totalPrice));
                }

                else
                {
                    empty_image.setVisibility(View.VISIBLE);
                    empty_text.setVisibility(View.VISIBLE);
                    cartList.clear();
                    totalPrice = 0.0;
                    tag4.setVisibility(View.INVISIBLE);
                    loaderGone();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void check(String coupon_code) {

        if(coupon_code.isEmpty())
        {
            Toast.makeText(this, "Please enter coupon code", Toast.LENGTH_SHORT).show();
            loaderGone();
        }
        else {
            Toast.makeText(CartActivity.this, "Please wait while we are checking coupon code", Toast.LENGTH_SHORT).show();
            checkValidation(coupon_code);
        }
    }


    private void checkValidation(String voucher) {

        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Qupon").addListenerForSingleValueEvent(new ValueEventListener() {
            int state = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        Qupon qupon = snapshot1.getValue(Qupon.class);

                        String coupon_code_b = qupon.getQupon_code();

                        if(voucher.equals(coupon_code_b))
                        {
                            //Should add methods to handle discount
                            loaderGone();
                            state = 1;
                            discounted_price = Double.parseDouble(qupon.getQupon_discount());
                            Toast.makeText(CartActivity.this, "Congratulation! You got "+qupon.getQupon_discount()+" taka discount", Toast.LENGTH_LONG).show();
                            if(totalPrice!=0)
                            {
                                sub_total_amount = totalPrice - discounted_price;
                                if(sub_total_amount < 0)
                                {
                                    Toast.makeText(CartActivity.this, "Purchase amount must be greater than discount", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else
                                {
                                    subTotalAmount.setText("৳ "+String.valueOf(discounted_price));
                                }
                            }
                            break;
                        }
                    }
                    if(state == 0)
                    {
                        Toast.makeText(CartActivity.this, "Coupon does not exists", Toast.LENGTH_SHORT).show();
                        loaderGone();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                loaderGone();
            }
        });
    }


    public static void loaderVisible()
    {
        loader.setVisibility(View.VISIBLE);
    }
    public static void loaderGone()
    {
        loader.setVisibility(View.GONE);
    }

}
