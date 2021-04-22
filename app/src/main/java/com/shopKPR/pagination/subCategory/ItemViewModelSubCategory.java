package com.shopKPR.pagination.subCategory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.shopKPR.Model.OwoProduct;

public class ItemViewModelSubCategory extends ViewModel {

    public LiveData<PagedList<OwoProduct>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

    public ItemViewModelSubCategory(Long subCategoryId) {

        ItemDataSourceFactorySubCategory itemDataSourceFactory = new ItemDataSourceFactorySubCategory(subCategoryId);
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
