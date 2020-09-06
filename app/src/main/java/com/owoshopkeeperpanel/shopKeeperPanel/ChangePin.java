package com.owoshopkeeperpanel.shopKeeperPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.owoshopkeeperpanel.R;

public class ChangePin extends AppCompatActivity {

    private ImageView back_to_home, show_current_pin, show_new_pin, show_new_pin_confirm;
    private EditText current_pin, new_pin, confirm_pin;
    private Button change;

    private Boolean isShowPinCurrentPin = false;
    private Boolean isShowPinNewPin = false;
    private Boolean isShowNewPinConfirm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        show_current_pin = findViewById(R.id.show_current_pin);
        show_new_pin = findViewById(R.id.show_new_pin);
        show_new_pin_confirm = findViewById(R.id.show_new_pin_confirm);

        current_pin = findViewById(R.id.currentPin);
        new_pin = findViewById(R.id.newPinUpdate);
        confirm_pin = findViewById(R.id.newPinConfirmation);

        change = findViewById(R.id.change);
        back_to_home = findViewById(R.id.back_to_home);

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        show_current_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPinCurrentPin) {
                    current_pin.setTransformationMethod(new PasswordTransformationMethod());
                    show_current_pin.setImageResource(R.drawable.ic_visibility_off);
                    isShowPinCurrentPin = false;

                }else{
                    current_pin.setTransformationMethod(null);
                    show_current_pin.setImageResource(R.drawable.ic_visibility);
                    isShowPinCurrentPin = true;
                }
            }
        });

        show_new_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowPinNewPin) {
                    new_pin.setTransformationMethod(new PasswordTransformationMethod());
                    show_new_pin.setImageResource(R.drawable.ic_visibility_off);
                    isShowPinNewPin = false;

                }else{
                    new_pin.setTransformationMethod(null);
                    show_new_pin.setImageResource(R.drawable.ic_visibility);
                    isShowPinNewPin = true;
                }
            }
        });

        show_new_pin_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowNewPinConfirm) {
                    confirm_pin.setTransformationMethod(new PasswordTransformationMethod());
                    show_new_pin_confirm.setImageResource(R.drawable.ic_visibility_off);
                    isShowNewPinConfirm = false;

                }else{
                    confirm_pin.setTransformationMethod(null);
                    show_new_pin_confirm.setImageResource(R.drawable.ic_visibility);
                    isShowNewPinConfirm = true;
                }
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

    }

    private void validate() {

    }
}