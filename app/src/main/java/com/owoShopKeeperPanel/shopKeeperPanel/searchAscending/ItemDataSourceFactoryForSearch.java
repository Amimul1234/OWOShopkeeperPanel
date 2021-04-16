package com.owoShopKeeperPanel.shopKeeperPanel.searchAscending;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoShopKeeperPanel.Model.OwoProduct;

import java.util.List;


public class ItemDataSourceFactoryForSearch extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();
    private final List<String> subCategories;
    private final String searchedProduct;

    public ItemDataSourceFactoryForSearch(List<String> subCategories, String searchedProduct) {
        this.subCategories = subCategories;
        this.searchedProduct = searchedProduct;
    }

    @Override
    public DataSource create() {
        ItemDataSourceForSearch itemDataSourceCategory = new ItemDataSourceForSearch(subCategories, searchedProduct);
        itemLiveDataSource.postValue(itemDataSourceCategory);
        return itemDataSourceCategory;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
