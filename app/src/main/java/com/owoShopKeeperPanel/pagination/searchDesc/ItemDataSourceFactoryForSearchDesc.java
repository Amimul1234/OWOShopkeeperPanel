package com.owoShopKeeperPanel.pagination.searchDesc;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoShopKeeperPanel.Model.OwoProduct;

import java.util.List;


public class ItemDataSourceFactoryForSearchDesc extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();
    private final List<String> subCategories;
    private final String searchedProduct;

    public ItemDataSourceFactoryForSearchDesc(List<String> subCategories, String searchedProduct) {
        this.subCategories = subCategories;
        this.searchedProduct = searchedProduct;
    }

    @Override
    public DataSource create() {
        ItemDataSourceForSearchDesc itemDataSourceCategoryDesc = new ItemDataSourceForSearchDesc(subCategories, searchedProduct);
        itemLiveDataSource.postValue(itemDataSourceCategoryDesc);
        return itemDataSourceCategoryDesc;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
