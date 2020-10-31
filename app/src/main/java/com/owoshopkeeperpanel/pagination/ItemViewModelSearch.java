package com.owoshopkeeperpanel.pagination;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoshopkeeperpanel.Model.Owo_product;


public class ItemViewModelSearch extends ViewModel {

    public LiveData<PagedList<Owo_product>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, Owo_product>> liveDataSource;

    public ItemViewModelSearch(String[] categories, String searchedProduct) {

        ItemDataSourceFactoryForSearch itemDataSourceFactoryForSearch = new ItemDataSourceFactoryForSearch(categories, searchedProduct);
        liveDataSource = itemDataSourceFactoryForSearch.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(30)
                        .setEnablePlaceholders(false)
                        .build();


        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactoryForSearch, config)).build();
    }

    public void clear(){
        liveDataSource.getValue().invalidate();
    }

}
