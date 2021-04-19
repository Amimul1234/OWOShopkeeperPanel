package com.shopKPR.pagination.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.shopKPR.Model.ShopKeeperOrders;


public class OrderItemViewModel extends ViewModel {

    public LiveData<PagedList<ShopKeeperOrders>> itemPagedList;
    private final LiveData<PageKeyedDataSource<Integer, ShopKeeperOrders>> liveDataSource;

    public OrderItemViewModel() {

        OrderItemDataSourceFactory orderItemDataSourceFactory = new OrderItemDataSourceFactory();
        liveDataSource = orderItemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(30)
                        .setEnablePlaceholders(false)
                        .build();


        itemPagedList = (new LivePagedListBuilder(orderItemDataSourceFactory, config)).build();
    }

    public void clear(){
        liveDataSource.getValue().invalidate();
    }

}
