package com.owoShopKeeperPanel.shopKeeperSettings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.owoShopKeeperPanel.ApiAndClient.RetrofitClient;
import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.homeComponents.HomeActivity;
import com.owoShopKeeperPanel.userRegistration.ShopKeeperUser;
import org.jetbrains.annotations.NotNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoUpdateBottomSheet extends BottomSheetDialogFragment {

    private final Context mCtx;
    private final ShopKeeperUser shopKeeperUser;

    public UserInfoUpdateBottomSheet(Context applicationContext, ShopKeeperUser shopKeeperUser) {
        this.mCtx = applicationContext;
        this.shopKeeperUser = shopKeeperUser;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.name_and_mobile_edit_bottom_sheet, container, false);

        EditText name = v.findViewById(R.id.user_name);
        TextView phone = v.findViewById(R.id.user_mobile_number);

        name.setText(shopKeeperUser.getName());
        phone.setText(shopKeeperUser.getMobileNumber());

        Button update_info = v.findViewById(R.id.update_user_information);

        update_info.setOnClickListener(v1 -> {

            String nameChecking = name.getText().toString();

            if(nameChecking.isEmpty())
            {
                name.setError("Name can not be empty");
                name.requestFocus();
            }
            else
            {
                updateOnlyUserInfo(nameChecking);
            }
        });

        return v;
    }

    private void updateOnlyUserInfo(String nameChecking) {
        shopKeeperUser.setName(nameChecking);
        updateToDatabaseImageHandling();
    }


    private void updateToDatabaseImageHandling()
    {
        RetrofitClient.getInstance().getApi()
                .updateShopKeeperInfo(shopKeeperUser)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            Toast.makeText(mCtx, "User name updated successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(mCtx, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                        else
                        {
                            Toast.makeText(mCtx, "Sorry! Can not update your info. , Please try again", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        Log.e("Settings", "Error occurred, Error is: "+t.getMessage());
                        Toast.makeText(mCtx, "Sorry! Can not update your info. , Please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}