package com.owoshopkeeperpanel.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import com.owoshopkeeperpanel.Model.User_debt_details;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.myShopRelated.debt.DebtDetailsForACustomer;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDebtRecordForASingleUserAdapter extends RecyclerView.Adapter<UserDebtRecordForASingleUserAdapter.ItemViewHolder> {

    private Context mCtx;
    private List<User_debt_details> user_debt_detailsList;
    private long user_id;

    public UserDebtRecordForASingleUserAdapter(Context mCtx, List<User_debt_details> user_debt_detailsList, long user_id) {
        this.mCtx = mCtx;
        this.user_debt_detailsList = user_debt_detailsList;
        this.user_id = user_id;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.debt_details_sample, parent, false);
        return new UserDebtRecordForASingleUserAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        UserDebtRecordForASingleUserAdapter.ItemViewHolder itemViewHolder = (UserDebtRecordForASingleUserAdapter.ItemViewHolder) holder;

        User_debt_details user_debt_details = user_debt_detailsList.get(position);

        if (user_debt_details != null) {

            ColorGenerator generator = ColorGenerator.MATERIAL;

            int color1 = generator.getRandomColor();

            char c = user_debt_details.getDescription().charAt(0);

            TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(c), color1);

            itemViewHolder.letter_image_view.setImageDrawable(drawable);
            itemViewHolder.debt_description.setText(user_debt_details.getDescription());
            itemViewHolder.debt_taking_date.setText(user_debt_details.getDate());
            itemViewHolder.debt_amount.setText("৳ "+String.valueOf(user_debt_details.getTaka()));

        } else {
            Toast.makeText(mCtx, "No record found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return user_debt_detailsList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView letter_image_view;
        public TextView debt_description, debt_taking_date, debt_amount;

        public ItemViewHolder(View itemView) {
            super(itemView);

            letter_image_view = itemView.findViewById(R.id.letter_image_view);
            debt_description = itemView.findViewById(R.id.debt_description);
            debt_taking_date = itemView.findViewById(R.id.debt_taking_date);
            debt_amount = itemView.findViewById(R.id.debt_amount);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    CharSequence[] options = new CharSequence[]{"Edit debt report", "Delete debt report"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

                    builder.setTitle("Select Option");

                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                {
                                    break;
                                }

                                case 1:
                                {
                                    long record_id = user_debt_detailsList.get(getBindingAdapterPosition()).getId();
                                    User_debt_details user_debt_details = user_debt_detailsList.get(getBindingAdapterPosition());

                                    RetrofitClient.getInstance().getApi()
                                            .deleteADebtDetails(record_id, user_id)
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                    if(response.code() == 404)
                                                    {
                                                        Toast.makeText(mCtx, "Record not found", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else if(response.code() == 200)
                                                    {
                                                        user_debt_detailsList.remove(user_debt_details);
                                                        notifyDataSetChanged();
                                                        DebtDetailsForACustomer debtDetailsForACustomer = (DebtDetailsForACustomer) mCtx;
                                                        debtDetailsForACustomer.loadData(user_id);
                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(mCtx, "Error deleting record", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                    Toast.makeText(mCtx, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    break;
                                }
                            }
                        }
                    });

                    builder.show();

                    return true;
                }
            });
        }
    }
}
