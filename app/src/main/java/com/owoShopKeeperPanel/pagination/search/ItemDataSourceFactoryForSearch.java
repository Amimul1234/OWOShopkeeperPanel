package com.owoShopKeeperPanel.pagination.search;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoShopKeeperPanel.Model.Owo_product;


public class ItemDataSourceFactoryForSearch extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> itemLiveDataSource = new MutableLiveData<>();
    private String[] categories;
    private String searchedProduct;

    public ItemDataSourceFactoryForSearch(String[] categories, String searchedProduct) {
        this.categories = categories;
        this.searchedProduct = searchedProduct;
    }

    @Override
    public DataSource create() {
        ItemDataSourceForSearch itemDataSourceCategory = new ItemDataSourceForSearch(categories, searchedProduct);
        itemLiveDataSource.postValue(itemDataSourceCategory);
        return itemDataSourceCategory;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Owo_product>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
