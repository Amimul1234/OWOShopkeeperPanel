package com.owoshopkeeperpanel.pagination;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoshopkeeperpanel.Model.Products;


public class ItemDataSourceFactoryForSearchDesc extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Products>> itemLiveDataSource = new MutableLiveData<>();
    private String[] categories;
    private String searchedProduct;

    public ItemDataSourceFactoryForSearchDesc(String[] categories, String searchedProduct) {
        this.categories = categories;
        this.searchedProduct = searchedProduct;
    }

    @Override
    public DataSource create() {
        ItemDataSourceForSearch itemDataSourceCategory = new ItemDataSourceForSearch(categories, searchedProduct);
        itemLiveDataSource.postValue(itemDataSourceCategory);
        return itemDataSourceCategory;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Products>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
