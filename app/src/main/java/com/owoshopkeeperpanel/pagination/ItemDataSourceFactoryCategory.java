package com.owoshopkeeperpanel.pagination;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoshopkeeperpanel.Model.Products;


public class ItemDataSourceFactoryCategory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Products>> itemLiveDataSource = new MutableLiveData<>();
    private String category;

    public ItemDataSourceFactoryCategory(String category) {
        this.category = category;
    }

    @Override
    public DataSource create() {
        ItemDataSourceCategory itemDataSourceCategory = new ItemDataSourceCategory(category);
        itemLiveDataSource.postValue(itemDataSourceCategory);
        return itemDataSourceCategory;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Products>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
