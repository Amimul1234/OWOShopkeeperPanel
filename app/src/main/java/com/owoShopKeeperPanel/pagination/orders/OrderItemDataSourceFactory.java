package com.owoShopKeeperPanel.pagination.orders;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.owoShopKeeperPanel.Model.Shop_keeper_orders;

import org.jetbrains.annotations.NotNull;


public class OrderItemDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, Shop_keeper_orders>> itemLiveDataSource = new MutableLiveData<>();

    @NotNull
    @Override
    public DataSource create() {
        OrderItemDataSource orderItemDataSource = new OrderItemDataSource();
        itemLiveDataSource.postValue(orderItemDataSource);
        return orderItemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Shop_keeper_orders>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
