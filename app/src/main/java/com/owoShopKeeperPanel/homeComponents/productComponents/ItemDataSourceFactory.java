package com.owoShopKeeperPanel.homeComponents.productComponents;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.owoShopKeeperPanel.Model.OwoProduct;


public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();
    private Long[] categories;

    public ItemDataSourceFactory(Long[] categories) {
        this.categories = categories;
    }

    @Override
    public DataSource create() {
        ItemDataSource itemDataSource = new ItemDataSource(categories);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
