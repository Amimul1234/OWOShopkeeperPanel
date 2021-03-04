package com.owoShopKeeperPanel.homeComponents.brandsComponent;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;

public class BrandsFragment extends Fragment {
    int counter = 1;

    public BrandsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_brands_fragment, container, false);

        Button button = rootView.findViewById(R.id.more_button);

        RecyclerView recyclerView = rootView.findViewById(R.id.brands);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);//Configuring recyclerview to receive two layout manager
        recyclerView.setLayoutManager(layoutManager);
        BrandsAdapter brandsAdapter = new BrandsAdapter(getActivity());
        recyclerView.setAdapter(brandsAdapter);

        brandsAdapter.getItems(1);

        button.setOnClickListener(v -> {
            if(counter < Prevalent.category_to_display.size())
            {
                counter++;
                brandsAdapter.getItems(counter);
            }
            else
            {
                Toast.makeText(getActivity(), "No more brands", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}