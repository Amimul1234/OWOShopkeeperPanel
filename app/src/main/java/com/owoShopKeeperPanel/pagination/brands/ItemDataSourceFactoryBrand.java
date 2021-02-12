package com.owoShopKeeperPanel.pagination.brands;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import org.jetbrains.annotations.NotNull;

public class ItemDataSourceFactoryBrand extends DataSource.Factory {

    private final MutableLiveData<ItemDataSourceBrands> itemLiveDataSource = new MutableLiveData<>();
    private final String[] categories;
    private final String brand_name;

    public ItemDataSourceFactoryBrand(String[] categories, String brand) {
        this.categories = categories;
        this.brand_name = brand;
    }

    @NotNull
    @Override
    public DataSource create() {
        ItemDataSourceBrands itemDataSourceBrands = new ItemDataSourceBrands(categories, brand_name);
        itemLiveDataSource.postValue(itemDataSourceBrands);
        return itemDataSourceBrands;
    }

    public MutableLiveData<ItemDataSourceBrands> getMutableLiveData() {
        return itemLiveDataSource;
    }
}
