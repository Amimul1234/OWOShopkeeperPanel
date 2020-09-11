package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.owoshopkeeperpanel.Model.Qupon;
import com.owoshopkeeperpanel.R;

public class AddVoucharActivity extends AppCompatActivity {

    private EditText voucherEditText;
    private Button applyVoucherBtn;
    private String Voucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vouchar);

        voucherEditText=(EditText)findViewById(R.id.voucher);
        applyVoucherBtn=(Button)findViewById(R.id.apply_voucher_btn);

        applyVoucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check() {

        Voucher = voucherEditText.getText().toString();

        if(TextUtils.isEmpty(voucherEditText.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "Please enter a voucher", Toast.LENGTH_SHORT).show();
        }
        else {
            checkValidation(Voucher);
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

                        String qupon_code = qupon.getQupon_code();

                        if(voucher.equals(qupon_code))
                        {
                            //Do required steps here
                            Toast.makeText(AddVoucharActivity.this, qupon.getQupon_discount(), Toast.LENGTH_SHORT).show();
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
