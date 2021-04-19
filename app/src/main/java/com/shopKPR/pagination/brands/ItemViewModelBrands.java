package com.shopKPR.pagination.brands;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.shopKPR.Model.OwoProduct;
import com.shopKPR.pagination.NetworkState;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ItemViewModelBrands extends ViewModel {

    private final LiveData<NetworkState> networkState;
    public LiveData itemPagedList;
    LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

    public ItemViewModelBrands(Long brandId) {

        Executor executor = Executors.newFixedThreadPool(5);

        ItemDataSourceFactoryBrand itemDataSourceFactoryBrand = new ItemDataSourceFactoryBrand(brandId);

        networkState = Transformations.switchMap(itemDataSourceFactoryBrand.getMutableLiveData(),
                ItemDataSourceBrands::getNetworkState);

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(10)
                        .setEnablePlaceholders(false)
                        .build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactoryBrand, config))
                .setFetchExecutor(executor)
                .build();

    }


    public LiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public LiveData<PagedList<OwoProduct>> getItemPagedList() {
        return itemPagedList;
    }
}
