package com.owoshopkeeperpanel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.owoshopkeeperpanel.Model.User_debt_details;
import com.owoshopkeeperpanel.R;
import java.util.List;

public class UserDebtRecordForASingleUserAdapter extends RecyclerView.Adapter<UserDebtRecordForASingleUserAdapter.ItemViewHolder> {

    private Context mCtx;
    private List<User_debt_details> user_debt_detailsList;

    public UserDebtRecordForASingleUserAdapter(Context mCtx, List<User_debt_details> user_debt_detailsList) {
        this.mCtx = mCtx;
        this.user_debt_detailsList = user_debt_detailsList;
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
                    return false;
                }
            });
        }
    }
}
