package com.owoshopkeeperpanel.adapters;

import android.content.Context;
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
import com.owoshopkeeperpanel.Model.UserDebts;
import com.owoshopkeeperpanel.R;

import org.jetbrains.annotations.NotNull;

public class UserDebtAdapter extends PagedListAdapter<UserDebts, RecyclerView.ViewHolder>{

    private Context mCtx;

    public UserDebtAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.user_debt_sample, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

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

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView letter_image_view;
        public TextView debt_taker_name, debt_taker_mobile_number, debt_taker_total_amount;

        public ItemViewHolder(View itemView) {
            super(itemView);
            letter_image_view = itemView.findViewById(R.id.letter_image_view);
            debt_taker_name = itemView.findViewById(R.id.debt_taker_name);
            debt_taker_mobile_number = itemView.findViewById(R.id.debt_taker_mobile_number);
            debt_taker_total_amount = itemView.findViewById(R.id.debt_taker_total_amount);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            /*
            Owo_product owoproduct = getItem(getBindingAdapterPosition());
            Intent intent = new Intent(mCtx, ProductDetailsActivity.class);
            intent.putExtra("Products", owoproduct);
            mCtx.startActivity(intent);

             */
        }
    }

}
