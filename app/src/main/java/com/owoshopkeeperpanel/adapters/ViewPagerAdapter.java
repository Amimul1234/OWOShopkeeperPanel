package com.owoshopkeeperpanel.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.owoshopkeeperpanel.shopKeeperPanel.brands_fragment;
import com.owoshopkeeperpanel.shopKeeperPanel.category_fragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            Log.d("pos", String.valueOf(position));
            return new category_fragment();
        }
        else
        {
            Log.d("pos", String.valueOf(position));
            return new brands_fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}