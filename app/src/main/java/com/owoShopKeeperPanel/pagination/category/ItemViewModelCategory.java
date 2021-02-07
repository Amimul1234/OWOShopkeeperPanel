package com.owoShopKeeperPanel.pagination.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoShopKeeperPanel.Model.Owo_product;


public class ItemViewModelCategory extends ViewModel {

    public LiveData<PagedList<Owo_product>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, Owo_product>> liveDataSource;

    public ItemViewModelCategory(String category) {

        ItemDataSourceFactoryCategory itemDataSourceFactoryCategory = new ItemDataSourceFactoryCategory(category);
        liveDataSource = itemDataSourceFactoryCategory.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactoryCategory, config)).build();
    }

    public void clear(){
        liveDataSource.getValue().invalidate();
    }

}
