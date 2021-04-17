package com.owoShopKeeperPanel.shopKeeperPanel.searchAlphabetically;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import com.owoShopKeeperPanel.Model.OwoProduct;
import java.util.List;
import java.util.Objects;


public class ItemViewModelSearchAlphabetically extends ViewModel
{

    public LiveData<PagedList<OwoProduct>> itemPagedList;
    private final LiveData<PageKeyedDataSource<Integer, OwoProduct>> liveDataSource;

    public ItemViewModelSearchAlphabetically(List<String> subCategories, String searchedProduct) {

        ItemDataSourceFactoryForSearchAlphabetically itemDataSourceFactoryForSearch = new ItemDataSourceFactoryForSearchAlphabetically(subCategories, searchedProduct);
        liveDataSource = itemDataSourceFactoryForSearch.getItemLiveDataSource();

        PagedList.Config config =
                (new PagedList.Config.Builder())
                        .setPageSize(30)
                        .setEnablePlaceholders(false)
                        .build();


        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactoryForSearch, config)).build();
    }

    public void clear()
    {
        Objects.requireNonNull(liveDataSource.getValue()).invalidate();
    }

}
