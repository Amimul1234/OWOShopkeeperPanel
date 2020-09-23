package com.owoshopkeeperpanel.sheetHandeler;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.Qupon;
import com.owoshopkeeperpanel.R;

public class CouponSheet extends BottomSheetDialogFragment {

    private Context mCtx;
    private EditText coupon_code_a;

    public CouponSheet(Context mCtx) {
        this.mCtx = mCtx;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.coupon, container, false);

        coupon_code_a = v.findViewById(R.id.coupon_code);

        Button check_coupon = v.findViewById(R.id.check_coupon);

        check_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check(coupon_code_a.getText().toString());
            }
        });

        return v;
    }

    private void check(String coupon_code) {

        if(coupon_code.isEmpty())
        {
            coupon_code_a.setError("Please provide coupon code");
            coupon_code_a.requestFocus();
        }
        else {
            checkValidation(coupon_code);
        }
    }


    private void checkValidation(String voucher) {

        DatabaseReference databaseReference  = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("Qupon").addListenerForSingleValueEvent(new ValueEventListener() {
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
                            Toast.makeText(mCtx, qupon.getQupon_discount(), Toast.LENGTH_SHORT).show();
                            break;
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}