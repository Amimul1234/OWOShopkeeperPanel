package com.owoshopkeeperpanel.pagination.Category;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.pagination.Category.ItemDataSourceCategory;


public class ItemDataSourceFactoryCategory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> itemLiveDataSource = new MutableLiveData<>();
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

    public MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
