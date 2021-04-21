package com.shopKPR.homeComponents.tabbedComponents;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.shopKPR.homeComponents.tabbedComponents.subCategories.SubCategoryFragment;
import com.shopKPR.homeComponents.categoryComponents.CategoryFragment;

public class ViewPagerAdapter extends FragmentStateAdapter
{

    private final CategoryFragment categoryFragment;
    private final SubCategoryFragment subCategoryFragment;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

        categoryFragment = new CategoryFragment();
        subCategoryFragment = new SubCategoryFragment();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return categoryFragment;
        }
        else
        {
            return subCategoryFragment;
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}