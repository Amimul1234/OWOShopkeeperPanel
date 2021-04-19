package com.shopKPR.pagination.orders;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.shopKPR.Model.ShopKeeperOrders;

import org.jetbrains.annotations.NotNull;


public class OrderItemDataSourceFactory extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, ShopKeeperOrders>> itemLiveDataSource = new MutableLiveData<>();

    @NotNull
    @Override
    public DataSource create() {
        OrderItemDataSource orderItemDataSource = new OrderItemDataSource();
        itemLiveDataSource.postValue(orderItemDataSource);
        return orderItemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, ShopKeeperOrders>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
