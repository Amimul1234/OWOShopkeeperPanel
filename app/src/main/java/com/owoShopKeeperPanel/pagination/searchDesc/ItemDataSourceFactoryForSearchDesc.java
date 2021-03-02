package com.owoShopKeeperPanel.pagination.searchDesc;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoShopKeeperPanel.Model.OwoProduct;


public class ItemDataSourceFactoryForSearchDesc extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();
    private String[] categories;
    private String searchedProduct;

    public ItemDataSourceFactoryForSearchDesc(String[] categories, String searchedProduct) {
        this.categories = categories;
        this.searchedProduct = searchedProduct;
    }

    @Override
    public DataSource create() {
        ItemDataSourceForSearchDesc itemDataSourceCategoryDesc = new ItemDataSourceForSearchDesc(categories, searchedProduct);
        itemLiveDataSource.postValue(itemDataSourceCategoryDesc);
        return itemDataSourceCategoryDesc;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
