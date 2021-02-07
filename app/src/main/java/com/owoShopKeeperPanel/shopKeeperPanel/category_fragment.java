package com.owoShopKeeperPanel.shopKeeperPanel;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.owoShopKeeperPanel.R;
import com.owoShopKeeperPanel.adapters.CategoryAdapter;

import java.util.Objects;

public class category_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public category_fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_category_fragment, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.categories_recyclerview);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);//Configuring recyclerview to receive two layout manager

        recyclerView.setLayoutManager(layoutManager);

        CategoryAdapter categoryAdapter = new CategoryAdapter(Objects.requireNonNull(getActivity()));

        recyclerView.setAdapter(categoryAdapter);

        return rootView;
    }
}