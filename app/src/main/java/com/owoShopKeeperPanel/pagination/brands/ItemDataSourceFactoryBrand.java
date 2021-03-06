package com.owoShopKeeperPanel.pagination.brands;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import org.jetbrains.annotations.NotNull;

public class ItemDataSourceFactoryBrand extends DataSource.Factory {

    private final MutableLiveData<ItemDataSourceBrands> itemLiveDataSource = new MutableLiveData<>();
    private final Long brandId;

    public ItemDataSourceFactoryBrand(Long brandId) {
        this.brandId = brandId;
    }

    @NotNull
    @Override
    public DataSource create() {
        ItemDataSourceBrands itemDataSourceBrands = new ItemDataSourceBrands(brandId);
        itemLiveDataSource.postValue(itemDataSourceBrands);
        return itemDataSourceBrands;
    }

    public MutableLiveData<ItemDataSourceBrands> getMutableLiveData() {
        return itemLiveDataSource;
    }
}
