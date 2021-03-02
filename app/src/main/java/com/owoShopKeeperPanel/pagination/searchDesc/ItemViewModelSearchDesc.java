package com.owoShopKeeperPanel.pagination.searchDesc;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoShopKeeperPanel.Model.OwoProduct;


public class ItemViewModelSearchDesc extends ViewModel {

    public LiveData<PagedList<OwoProduct>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

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
