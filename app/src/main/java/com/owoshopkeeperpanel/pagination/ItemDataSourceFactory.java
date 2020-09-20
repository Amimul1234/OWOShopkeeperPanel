package com.owoshopkeeperpanel.pagination;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoshopkeeperpanel.Model.Products;


public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Products>> itemLiveDataSource = new MutableLiveData<>();
    private String[] categories;

    public ItemDataSourceFactory(String[] categories) {
        this.categories = categories;
    }

    @Override
    public DataSource create() {
        ItemDataSource itemDataSource = new ItemDataSource(categories);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Products>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
