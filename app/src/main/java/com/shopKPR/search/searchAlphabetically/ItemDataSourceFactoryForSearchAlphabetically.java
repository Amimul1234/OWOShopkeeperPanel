package com.shopKPR.search.searchAlphabetically;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.shopKPR.Model.OwoProduct;
import java.util.List;


public class ItemDataSourceFactoryForSearchAlphabetically extends DataSource.Factory {

    private final MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();
    private final List<String> subCategories;
    private final String searchedProduct;

    public ItemDataSourceFactoryForSearchAlphabetically(List<String> subCategories, String searchedProduct) {
        this.subCategories = subCategories;
        this.searchedProduct = searchedProduct;
    }

    @Override
    public DataSource create()
    {
        ItemDataSourceForSearchAlphabetically itemDataSourceCategory =
                new ItemDataSourceForSearchAlphabetically(subCategories, searchedProduct);

        itemLiveDataSource.postValue(itemDataSourceCategory);
        return itemDataSourceCategory;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
