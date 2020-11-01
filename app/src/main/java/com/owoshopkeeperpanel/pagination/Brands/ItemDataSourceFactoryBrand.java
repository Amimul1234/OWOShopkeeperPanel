package com.owoshopkeeperpanel.pagination.Brands;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.pagination.Category.ItemDataSourceCategory;


public class ItemDataSourceFactoryBrand extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> itemLiveDataSource = new MutableLiveData<>();
    private String[] categories;
    private String brand_name;

    public ItemDataSourceFactoryBrand(String[] categories, String brand) {
        this.categories = categories;
        this.brand_name = brand;
    }

    @Override
    public DataSource create() {
        ItemDataSourceBrands itemDataSourceCategory = new ItemDataSourceBrands(categories, brand_name);
        itemLiveDataSource.postValue(itemDataSourceCategory);
        return itemDataSourceCategory;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
