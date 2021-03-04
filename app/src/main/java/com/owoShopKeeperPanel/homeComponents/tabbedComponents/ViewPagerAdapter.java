package com.owoShopKeeperPanel.homeComponents.tabbedComponents;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.owoShopKeeperPanel.homeComponents.brandsComponent.BrandsFragment;
import com.owoShopKeeperPanel.homeComponents.categoryComponents.CategoryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new CategoryFragment();
        }
        else
        {
            return new BrandsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}