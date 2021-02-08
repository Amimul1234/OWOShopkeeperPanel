package com.owoShopKeeperPanel.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.owoShopKeeperPanel.shopKeeperPanel.BrandsFragment;
import com.owoShopKeeperPanel.shopKeeperPanel.CategoryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            Log.d("pos", String.valueOf(position));
            return new CategoryFragment();
        }
        else
        {
            Log.d("pos", String.valueOf(position));
            return new BrandsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}