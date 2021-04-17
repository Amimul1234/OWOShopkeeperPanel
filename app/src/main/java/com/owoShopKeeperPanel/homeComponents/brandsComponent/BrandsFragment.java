package com.owoShopKeeperPanel.homeComponents.brandsComponent;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.owoShopKeeperPanel.prevalent.Prevalent;
import com.owoShopKeeperPanel.R;

public class BrandsFragment extends Fragment {

    int counter = 1;

    private View rootView;
    private BrandsAdapter brandsAdapter;

    public BrandsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_brands_fragment, container, false);
        updateFragmentData();
        return rootView;
    }

    private void updateFragmentData() {

        RecyclerView recyclerView = rootView.findViewById(R.id.brandsRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);//Configuring recyclerview to receive two layout manager
        recyclerView.setLayoutManager(layoutManager);

        brandsAdapter = new BrandsAdapter(getActivity());
        recyclerView.setAdapter(brandsAdapter);

        Button button = rootView.findViewById(R.id.more_button);

        brandsAdapter.getItems(1);

        button.setOnClickListener(v ->
        {
            if(counter < Prevalent.category_to_display.size())
            {
                counter++;
                brandsAdapter.getItems(counter);
            }
            else
            {
                Toast.makeText(getActivity(), "No more brands", Toast.LENGTH_SHORT).show();
            }

            brandsAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        brandsAdapter.notifyDataSetChanged();
    }
}