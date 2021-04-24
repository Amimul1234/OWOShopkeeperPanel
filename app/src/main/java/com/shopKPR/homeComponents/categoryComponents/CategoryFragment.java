package com.shopKPR.homeComponents.categoryComponents;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.shopKPR.R;

public class CategoryFragment extends Fragment
{

    private View rootView;
    private CategoryAdapter categoryAdapter;

    public CategoryFragment()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_category_fragment, container, false);
        updateFragmentData();
        return rootView;
    }

    private void updateFragmentData()
    {
        RecyclerView recyclerView = rootView.findViewById(R.id.categories_recyclerview);

        categoryAdapter = new CategoryAdapter(getActivity());

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        categoryAdapter.notifyDataSetChanged();
    }
}