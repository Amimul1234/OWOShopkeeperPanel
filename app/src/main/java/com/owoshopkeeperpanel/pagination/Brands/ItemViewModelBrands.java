package com.owoshopkeeperpanel.pagination.Brands;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.owoshopkeeperpanel.Model.Owo_product;
import com.owoshopkeeperpanel.pagination.Category.ItemDataSourceFactoryCategory;
import com.owoshopkeeperpanel.pagination.HomeItems.ItemDataSourceFactory;


public class ItemViewModelBrands extends ViewModel {

    public LiveData<PagedList<Owo_product>> itemPagedList;
    LiveData<PageKeyedDataSource<Integer, Owo_product>> liveDataSource;

    public ItemViewModelBrands(String[] categories, String brand_name) {

        ItemDataSourceFactoryBrand itemDataSourceFactoryBrand = new ItemDataSourceFactoryBrand(categories, brand_name);
        liveDataSource = itemDataSourceFactoryBrand.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactoryBrand, config)).build();
    }

    public void clear(){
        liveDataSource.getValue().invalidate();
    }

}
