package com.owoShopKeeperPanel.home.productPagination;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.owoShopKeeperPanel.Model.OwoProduct;


public class ItemViewModel extends ViewModel {

    public LiveData<PagedList<OwoProduct>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

    public ItemViewModel(Long[] categories) {

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
