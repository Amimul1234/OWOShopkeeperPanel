package com.owoshopkeeperpanel.pagination.brands;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;


public class ItemDataSourceFactoryBrand extends DataSource.Factory {

    private MutableLiveData<ItemDataSourceBrands> itemLiveDataSource = new MutableLiveData<>();
    private String[] categories;
    private String brand_name;

    public ItemDataSourceFactoryBrand(String[] categories, String brand) {
        this.categories = categories;
        this.brand_name = brand;
    }

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
