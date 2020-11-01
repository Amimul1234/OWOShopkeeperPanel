package com.owoshopkeeperpanel.pagination.SubCategory;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoshopkeeperpanel.Model.Owo_product;


public class ItemDataSourceFactorySubCategory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> itemLiveDataSource = new MutableLiveData<>();
    private String category;

    public ItemDataSourceFactorySubCategory(String category) {
        this.category = category;
    }

    @Override
    public DataSource create() {
        ItemDataSourceSubCategory itemDataSource = new ItemDataSourceSubCategory(category);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
