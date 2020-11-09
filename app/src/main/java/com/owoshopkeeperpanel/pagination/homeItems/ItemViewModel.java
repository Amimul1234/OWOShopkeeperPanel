package com.owoshopkeeperpanel.pagination.homeItems;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoshopkeeperpanel.Model.Owo_product;


public class ItemViewModel extends ViewModel {

    public LiveData<PagedList<Owo_product>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, Owo_product>> liveDataSource;

    public ItemViewModel(String[] categories) {

        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory(categories);
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, config)).build();
    }

    public void clear(){
        liveDataSource.getValue().invalidate();
    }

}
