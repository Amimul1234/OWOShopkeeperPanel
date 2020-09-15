package com.owoshopkeeperpanel.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.shopKeeperPanel.CategoryWiseProduct;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private Context mCtx;
    private String[] category_names;
    private List<Pair<String, Integer>> permitted = new ArrayList<Pair<String, Integer>>();

    private int icons[] = {R.drawable.icon1, R.drawable.icon2, R.drawable.icon3, R.drawable.icon4, R.drawable.icon5, R.drawable.icon6,
            R.drawable.icon7, R.drawable.icon8, R.drawable.icon9, R.drawable.icon10, R.drawable.icon11, R.drawable.icon12,
            R.drawable.icon13, R.drawable.icon14, R.drawable.icon15, R.drawable.icon16, R.drawable.icon17, R.drawable.icon18,
            R.drawable.icon19, R.drawable.icon20, R.drawable.icon21, R.drawable.icon22, R.drawable.icon23, R.drawable.icon24,
            R.drawable.icon25, R.drawable.icon26, R.drawable.icon27, R.drawable.icon28, R.drawable.icon29, R.drawable.icon30,
            R.drawable.icon31, R.drawable.icon32, R.drawable.icon33, R.drawable.icon34, R.drawable.icon35, R.drawable.icon36,
            R.drawable.icon37, R.drawable.icon38, R.drawable.icon39, R.drawable.icon40, R.drawable.icon41};


    public CategoryAdapter(Context mCtx) {
        this.mCtx = mCtx;
        category_names = mCtx.getResources().getStringArray(R.array.productCategory);

        int size = category_names.length;

        int prevalent_size = Prevalent.category_to_display.size();

        for(int i=0; i<prevalent_size; i++)
        {
            for(int j=0; j<size; j++)
            {
                if(Prevalent.category_to_display.get(i).equals(category_names[j]))
                {
                    Pair<String, Integer> item = new Pair<>(category_names[j], icons[j]);
                    permitted.add(item);
                }
            }
        }
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.add_product_sample, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(permitted.get(position).second);
        holder.textView.setText(permitted.get(position).first);
    }

    @Override
    public int getItemCount() {
        return permitted.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageviewid);
            textView = itemView.findViewById(R.id.textviewid);

            DisplayMetrics displaymetrics = new DisplayMetrics(); //Resizing things dynamically
            ((Activity) mCtx).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            int devicewidth = displaymetrics.widthPixels / 3;

            int deviceheight = displaymetrics.heightPixels / 5;

            itemView.getLayoutParams().width = devicewidth - 20;
            itemView.getLayoutParams().height = deviceheight;

            imageView.getLayoutParams().width = devicewidth / 3; //setting category images dimensions
            imageView.getLayoutParams().height = devicewidth / 3; //setting category images dimensions

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    Intent intent = new Intent(mCtx, CategoryWiseProduct.class);
                    intent.putExtra("category", permitted.get(position).first);
                    mCtx.startActivity(intent);
                }
            });
        }
    }
}
