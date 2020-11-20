package com.owoshopkeeperpanel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import com.owoshopkeeperpanel.Model.UserDebts;
import com.owoshopkeeperpanel.Model.User_debt_details;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.myShopRelated.DebtDetailsForACustomer;
import com.owoshopkeeperpanel.myShopRelated.UserDebtDetails;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDebtRecordForASingleUserAdapter extends PagedListAdapter<User_debt_details, RecyclerView.ViewHolder> {
    private Context mCtx;

    public UserDebtAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.user_debt_sample, parent, false);
        return new UserDebtAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        UserDebtAdapter.ItemViewHolder itemViewHolder = (UserDebtAdapter.ItemViewHolder) holder;

        UserDebts userDebts = getItem(position);

        if (userDebts != null) {
            ColorGenerator generator = ColorGenerator.MATERIAL;

            int color1 = generator.getRandomColor();

            char c = userDebts.getUser_name().charAt(0);

            TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(c), color1);

            itemViewHolder.letter_image_view.setImageDrawable(drawable);
            itemViewHolder.debt_taker_name.setText(userDebts.getUser_name());
            itemViewHolder.debt_taker_mobile_number.setText(userDebts.getUser_mobile_number());
            itemViewHolder.debt_taker_total_amount.setText("৳ "+String.valueOf(userDebts.getUser_total_debt()));

        } else {
            Toast.makeText(mCtx, "No record found", Toast.LENGTH_LONG).show();
        }

    }

    private static final DiffUtil.ItemCallback<UserDebts> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<UserDebts>() {
                @Override
                public boolean areItemsTheSame(UserDebts oldItem, UserDebts newItem) {
                    return oldItem.getUser_id() == newItem.getUser_id();
                }

                @Override
                public boolean areContentsTheSame(@NotNull UserDebts oldItem, @NotNull UserDebts newItem) {
                    return true;
                }
            };

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView letter_image_view;
        public TextView debt_taker_name, debt_taker_mobile_number, debt_taker_total_amount;

        public ItemViewHolder(View itemView) {
            super(itemView);

            letter_image_view = itemView.findViewById(R.id.letter_image_view);
            debt_taker_name = itemView.findViewById(R.id.debt_taker_name);
            debt_taker_mobile_number = itemView.findViewById(R.id.debt_taker_mobile_number);
            debt_taker_total_amount = itemView.findViewById(R.id.debt_taker_total_amount);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserDebtDetails.visible();

                    UserDebts userDebts = getItem(getBindingAdapterPosition());

                    if (userDebts != null) {
                        RetrofitClient.getInstance().getApi()
                                .getUserDebtDetails(userDebts.getUser_id())
                                .enqueue(new Callback<List<User_debt_details>>() {
                                    @Override
                                    public void onResponse(@NotNull Call<List<User_debt_details>> call, @NotNull Response<List<User_debt_details>> response) {

                                        if(response.code() == 200)
                                        {
                                            UserDebtDetails.invisible();

                                            if (response.body() != null) {
                                                userDebts.getUserDebtDetails().addAll(response.body());
                                            }

                                            Intent intent = new Intent(mCtx, DebtDetailsForACustomer.class);
                                            intent.putExtra("debtDetails", userDebts);
                                            mCtx.startActivity(intent);
                                        }

                                        else if(response.code() == 204)
                                        {
                                            UserDebtDetails.invisible();
                                            Toast.makeText(mCtx, "No debt record for this customer", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(mCtx, DebtDetailsForACustomer.class);
                                            intent.putExtra("debtDetails", userDebts);
                                            mCtx.startActivity(intent);
                                        }

                                        else
                                        {
                                            UserDebtDetails.invisible();
                                            Toast.makeText(mCtx, "Error fetching data", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<List<User_debt_details>> call, @NotNull Throwable t) {
                                        Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_SHORT).show();
                                        UserDebtDetails.invisible();
                                    }
                                });
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
    }
}
