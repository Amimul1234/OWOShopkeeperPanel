package com.owoShopKeeperPanel.sheetHandeler;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.owoShopKeeperPanel.Model.UserShopKeeper;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.shopKeeperPanel.SettingsActivity;

public class User_Info_Update_Bottom_Sheet extends BottomSheetDialogFragment {

    String user_name, user_mobile_number;

    private Context mCtx;

    public User_Info_Update_Bottom_Sheet(Context mCtx, String user_name, String user_mobile_number) {
        this.user_name = user_name;
        this.user_mobile_number = user_mobile_number;
        this.mCtx = mCtx;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.name_and_mobile_edit_bottom_sheet, container, false);

        EditText name = v.findViewById(R.id.user_name);
        TextView phone = v.findViewById(R.id.user_mobile_number);

        name.setText(user_name);
        phone.setText(user_mobile_number);

        Button update_info = v.findViewById(R.id.update_user_information);

        update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name_checking = name.getText().toString();
                String number_checking = phone.getText().toString();

                if(name_checking.isEmpty())
                {
                    name.setError("Name can not be empty");
                    name.requestFocus();
                }
                else
                {
                    updateOnlyUserInfo(name_checking, number_checking);
                }
            }
        });

        return v;
    }

    private void updateOnlyUserInfo(String user_name, String phone_number) {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Shopkeeper");

        UserShopKeeper user_shopKeeper = new UserShopKeeper();
        user_shopKeeper.setName(user_name);
        user_shopKeeper.setPhone(phone_number);
        user_shopKeeper.setImage(Prevalent.currentOnlineUser.getImageUri());
        user_shopKeeper.setPin(Prevalent.currentOnlineUser.getPin());

        ref.child(Prevalent.currentOnlineUser.getMobileNumber()).setValue(user_shopKeeper).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mCtx, "Profile information updated successfully", Toast.LENGTH_SHORT).show();
                Prevalent.currentOnlineUser.setName(user_name);
                Prevalent.currentOnlineUser.setMobileNumber(phone_number);
                startActivity(new Intent(mCtx, SettingsActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(mCtx, "Can not update user information", Toast.LENGTH_SHORT).show();
            }
        });
    }

}