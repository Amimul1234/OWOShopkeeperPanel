package com.owoshopkeeperpanel.shopKeeperPanel;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.owoshopkeeperpanel.ApiAndClient.RetrofitClient;
import com.owoshopkeeperpanel.Model.Brands;
import com.owoshopkeeperpanel.Prevalent.Prevalent;
import com.owoshopkeeperpanel.R;
import com.owoshopkeeperpanel.adapters.BrandsAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class brands_fragment extends Fragment {

    int counter = 0;

    private List<Brands> brandsList = new ArrayList<>();

    private BrandsAdapter brandsAdapter;

    public brands_fragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_brands_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.brands);

        Button button = rootView.findViewById(R.id.more_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                getItems(counter);
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);//Configuring recyclerview to receive two layout manager

        recyclerView.setLayoutManager(layoutManager);

        getItems(0);

        brandsAdapter = new BrandsAdapter(brandsList, getActivity());

        recyclerView.setAdapter(brandsAdapter);

        return rootView;
    }


    public void getItems(int page)
    {
        String[] categories = Prevalent.category_to_display.stream().toArray(String[]::new);

        RetrofitClient.getInstance().getApi()
                .getBrandsViaCategory(page, categories)
                .enqueue(new Callback<List<Brands>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Brands>> call, @NotNull Response<List<Brands>> response) {
                        if(response.code() == 200)
                        {
                            brandsList.addAll((List<Brands>)response.body());
                            brandsAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "No more brands", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Brands>> call, @NotNull Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}