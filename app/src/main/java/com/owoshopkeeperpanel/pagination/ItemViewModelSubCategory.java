package com.owoshopkeeperpanel.pagination;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoshopkeeperpanel.Model.Owo_product;

public class ItemViewModelSubCategory extends ViewModel {

    public LiveData<PagedList<Owo_product>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, Owo_product>> liveDataSource;

    public ItemViewModelSubCategory(String category) {

        ItemDataSourceFactorySubCategory itemDataSourceFactory = new ItemDataSourceFactorySubCategory(category);
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
