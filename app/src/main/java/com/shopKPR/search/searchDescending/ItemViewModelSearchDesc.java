package com.shopKPR.search.searchDescending;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.shopKPR.Model.OwoProduct;
import java.util.List;
import java.util.Objects;


public class ItemViewModelSearchDesc extends ViewModel {

    public LiveData<PagedList<OwoProduct>> itemPagedList;
    private final LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

    public ItemViewModelSearchDesc(List<String> subCategories, String searchedProduct) {

        ItemDataSourceFactoryForSearchDesc itemDataSourceFactoryForSearchDesc = new ItemDataSourceFactoryForSearchDesc(subCategories, searchedProduct);
        liveDataSource = itemDataSourceFactoryForSearchDesc.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(30)
                        .setEnablePlaceholders(false)
                        .build();


        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactoryForSearchDesc, config)).build();
    }

    public void clear(){
        Objects.requireNonNull(liveDataSource.getValue()).invalidate();
    }

}
