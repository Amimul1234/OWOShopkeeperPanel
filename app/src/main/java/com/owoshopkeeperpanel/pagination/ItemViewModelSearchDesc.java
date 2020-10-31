package com.owoshopkeeperpanel.pagination;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoshopkeeperpanel.Model.Owo_product;


public class ItemViewModelSearchDesc extends ViewModel {

    public LiveData<PagedList<Owo_product>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, Owo_product>> liveDataSource;

    public ItemViewModelSearchDesc(String[] categories, String searchedProduct) {

        ItemDataSourceFactoryForSearchDesc itemDataSourceFactoryForSearchDesc = new ItemDataSourceFactoryForSearchDesc(categories, searchedProduct);
        liveDataSource = itemDataSourceFactoryForSearchDesc.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(30)
                        .setEnablePlaceholders(false)
                        .build();


        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactoryForSearchDesc, config)).build();
    }

    public void clear(){
        liveDataSource.getValue().invalidate();
    }

}
