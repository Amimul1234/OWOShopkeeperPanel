package com.owoShopKeeperPanel.pagination.category;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.owoShopKeeperPanel.Model.OwoProduct;


public class ItemDataSourceFactoryCategory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();
    private Long category;

    public ItemDataSourceFactoryCategory(Long category) {
        this.category = category;
    }

    @Override
    public DataSource create() {
        ItemDataSourceCategory itemDataSourceCategory = new ItemDataSourceCategory(category);
        itemLiveDataSource.postValue(itemDataSourceCategory);
        return itemDataSourceCategory;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
