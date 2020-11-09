package com.owoshopkeeperpanel.pagination.orders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.owoshopkeeperpanel.Model.Shop_keeper_orders;


public class OrderItemViewModel extends ViewModel {

    public LiveData<PagedList<Shop_keeper_orders>> itemPagedList;
    private final LiveData<PageKeyedDataSource<Integer, Shop_keeper_orders>> liveDataSource;

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
