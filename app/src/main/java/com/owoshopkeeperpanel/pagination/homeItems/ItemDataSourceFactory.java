package com.owoshopkeeperpanel.pagination.homeItems;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.owoshopkeeperpanel.Model.Owo_product;


public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> itemLiveDataSource = new MutableLiveData<>();
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

    public MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
