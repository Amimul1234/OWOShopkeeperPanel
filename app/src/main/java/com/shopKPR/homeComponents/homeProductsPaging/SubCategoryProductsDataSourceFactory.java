package com.shopKPR.homeComponents.homeProductsPaging;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;
import com.shopKPR.Model.OwoProduct;

import org.jetbrains.annotations.NotNull;

public class SubCategoryProductsDataSourceFactory extends DataSource.Factory
{
    private final MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> itemLiveDataSource = new MutableLiveData<>();
    private final Long subCategoryId;

    public SubCategoryProductsDataSourceFactory(Long subCategoryId) {
        this.subCategoryId = subCategoryId;
    }


    @NotNull
    @Override
    public DataSource create() {
        SubCategoryProductsDataSource itemDataSource = new SubCategoryProductsDataSource(subCategoryId);
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, OwoProduct>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
