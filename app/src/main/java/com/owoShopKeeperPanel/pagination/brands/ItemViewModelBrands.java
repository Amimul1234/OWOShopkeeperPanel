package com.owoShopKeeperPanel.pagination.brands;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.owoShopKeeperPanel.Model.Owo_product;
import com.owoShopKeeperPanel.pagination.NetworkState;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ItemViewModelBrands extends ViewModel {

    private final LiveData<NetworkState> networkState;
    public LiveData itemPagedList;
    LiveData<PageKeyedDataSource<Integer, Owo_product>> liveDataSource;

    public ItemViewModelBrands(String[] categories, String brand_name) {

        Executor executor = Executors.newFixedThreadPool(5);

        ItemDataSourceFactoryBrand itemDataSourceFactoryBrand = new ItemDataSourceFactoryBrand(categories, brand_name);

        networkState = Transformations.switchMap(itemDataSourceFactoryBrand.getMutableLiveData(),
                (Function<ItemDataSourceBrands, LiveData<NetworkState>>) ItemDataSourceBrands::getNetworkState);

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

    public LiveData<PagedList<Owo_product>> getItemPagedList() {
        return itemPagedList;
    }
}
