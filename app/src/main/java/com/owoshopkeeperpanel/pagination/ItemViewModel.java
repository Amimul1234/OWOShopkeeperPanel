package com.owoshopkeeperpanel.pagination;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoshopkeeperpanel.Model.Products;


public class ItemViewModel extends ViewModel {

    public LiveData<PagedList<Products>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, Products>> liveDataSource;

    public ItemViewModel(String[] categories) {

        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory(categories);
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(30)
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();
    }

    public void clear(){
        liveDataSource.getValue().invalidate();
    }

}
