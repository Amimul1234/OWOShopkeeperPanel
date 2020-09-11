package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        Voucher=voucherEditText.getText().toString();

        if(TextUtils.isEmpty(voucherEditText.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "Please enter a voucher", Toast.LENGTH_SHORT).show();
        }
        else {
            checkValidation(Voucher);
        }
    }

    private void checkValidation(String voucher) {
        //it will be added later

    }
}
