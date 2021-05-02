package com.shopKPR.bakirKhata;

import android.content.Context;
import android.util.Log;
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
import com.shopKPR.R;
import com.shopKPR.network.RetrofitClient;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BakirKhataAdapter extends RecyclerView.Adapter<BakirKhataAdapter.ViewHolder>
{
    private final Context mCtx;
    private final List<BakirRecord> bakirRecordList;

    public BakirKhataAdapter(Context mCtx, List<BakirRecord> bakirRecordList) {
        this.mCtx = mCtx;
        this.bakirRecordList = bakirRecordList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.bakir_khata_sample, parent, false);
        return new BakirKhataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BakirRecord bakirRecord = bakirRecordList.get(position);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        char c = bakirRecord.getCustomerName().charAt(0);

        TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(c), color1);

        String amount = "৳ "+bakirRecord.getProductPrice();

        holder.letterImage.setImageDrawable(drawable);
        holder.name.setText(bakirRecord.getCustomerName());
        holder.productName.setText(bakirRecord.getProductName());
        holder.productPrice.setText(amount);
        holder.productDate.setText(bakirRecord.getDate());
        holder.paymentMethod.setText(bakirRecord.getPaymentMethod());

    }

    @Override
    public int getItemCount() {
        return bakirRecordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView letterImage;
        private final TextView name;
        private final TextView productName;
        private final TextView productPrice;
        private final TextView productDate;
        private final TextView paymentMethod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            letterImage = itemView.findViewById(R.id.letter_image_view);
            name = itemView.findViewById(R.id.name);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDate = itemView.findViewById(R.id.date);
            paymentMethod = itemView.findViewById(R.id.payment_method);

            itemView.setOnLongClickListener(v -> {
                CharSequence[] options = new CharSequence[]{"Delete Bakir record"};

                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

                builder.setTitle("Select Option");

                builder.setItems(options, (dialog, which) -> {

                    if(which == 0)
                    {
                        BakirRecord bakirRecord = bakirRecordList.get(getBindingAdapterPosition());

                        RetrofitClient.getInstance().getApi()
                                .deleteRecord(bakirRecordList.get(getBindingAdapterPosition()).getBakirRecordId())
                                .enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                        if(response.isSuccessful())
                                        {
                                            Toast.makeText(mCtx, "Record removed successfully", Toast.LENGTH_SHORT).show();
                                            bakirRecordList.remove(bakirRecord);
                                            notifyDataSetChanged();
                                        }
                                        else
                                        {
                                            Toast.makeText(mCtx, "Can not remove record, please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                        Log.e("BakirRecord", t.getMessage());
                                        Toast.makeText(mCtx, "Can not remove record, please try again", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                builder.show();

                return true;
            });
        }
    }
}
